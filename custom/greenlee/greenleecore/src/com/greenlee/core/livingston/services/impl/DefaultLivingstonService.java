/**
 *
 */
package com.greenlee.core.livingston.services.impl;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.greenlee.core.enums.UserTypes;
import com.greenlee.core.livingston.services.LivingstonService;


/**
 * @author xiaochen bian
 *
 */
public class DefaultLivingstonService implements LivingstonService
{

	private static final Logger LOG = Logger.getLogger(DefaultLivingstonService.class);

	public static final String B2B_UID = "100051";
	public static final String B2C_UID = "100050";

	public static final String DUMMY_UNIT_B2B = (null != Config.getParameter("greenlee.account.dummy.distributor.uid")) ? Config
			.getParameter("greenlee.account.dummy.distributor.uid") : "dummydistributor";
	public static final String DUMMY_UNIT_B2E = (null != Config.getParameter("greenlee.account.dummy.distributor.b2e.uid")) ? Config
			.getParameter("greenlee.account.dummy.distributor.b2e.uid") : B2B_UID;
	public static final String DUMMY_UNIT_B2C = (null != Config.getParameter("greenlee.account.dummy.distributor.b2c.uid")) ? Config
			.getParameter("greenlee.account.dummy.distributor.b2c.uid") : B2C_UID;

	public static final String DUMMY_UNIT_B2B_CAD = (null != Config.getParameter("greenlee.account.dummy.distributor.uid.cad")) ? Config
			.getParameter("greenlee.account.dummy.distributor.uid.cad") : "dummydistributor_CAD";
	public static final String DUMMY_UNIT_B2E_CAD = (null != Config.getParameter("greenlee.account.dummy.distributor.b2e.uid.cad")) ? Config
			.getParameter("greenlee.account.dummy.distributor.b2e.uid.cad") : "B2ECUST_CAD";
	public static final String DUMMY_UNIT_B2C_CAD = (null != Config.getParameter("greenlee.account.dummy.distributor.b2c.uid.cad")) ? Config
			.getParameter("greenlee.account.dummy.distributor.b2c.uid.cad") : "B2CCUST_CAD";

	public static final String NO_MATCH = "N";
	public static final String POTENTIAL_MATCH = "C";
	public static final String EXACT_MATCH = "Y";

	public static final String SUBORG_USER = Config.getString("greenlee.livingston.suborg.forcustomer.name", "GREENLEE");
	public static final String SUBORG_COMPANY = Config.getString("greenlee.livingston.suborg.forcompany.name", "GRNLE_ECOMM");

	public static final String COMPANY_PARTNER_TYPE = "Company";
	public static final String USER_PARTNER_TYPE = "User";

	private String username;
	private String password;
	private String serviceUrl;
	private UserService userService;

	public UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	public String getServiceUrl()
	{
		return serviceUrl;
	}

	@Required
	public void setServiceUrl(final String serviceUrl)
	{
		this.serviceUrl = serviceUrl;
	}

	public String getUsername()
	{
		return username;
	}

	@Required
	public void setUsername(final String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	@Required
	public void setPassword(final String password)
	{
		this.password = password;
	}

	@Override
	public String getAddressScreenMatchCode(final CustomerData customer, final AddressData address)
	{
		String xmlString = getRequestXml(customer, address, SUBORG_USER, customer.getName(), USER_PARTNER_TYPE);
		String responseBody = executeLivingstonCall(xmlString);
		final StringBuilder builder = new StringBuilder();
		LOG.info("Livingston call with Customer's name");
		builder.append(getMatchCode(responseBody, customer));
		if (isUserB2E())
		{
			builder.append(";");
			xmlString = getRequestXml(customer, address, SUBORG_COMPANY, customer.getCompanyName(), COMPANY_PARTNER_TYPE);
			responseBody = executeLivingstonCall(xmlString);
			LOG.info("Livingston call with Customer's Company's name");
			builder.append(getMatchCode(responseBody, customer));
		}
		return builder.toString();
	}

	/**
	 * @param xmlString
	 * @return
	 */
	private String executeLivingstonCall(final String xmlString)
	{
		final HttpClient client = new HttpClient();
		final Credentials credentials = new UsernamePasswordCredentials(getUsername(), getPassword());
		String responseBody = null;
		client.getState().setCredentials(AuthScope.ANY, credentials);
		final PostMethod post = new PostMethod(getServiceUrl());
		post.setDoAuthentication(true);
		StringRequestEntity requestEntity = null;
		try
		{
			requestEntity = new StringRequestEntity(xmlString, "application/xml", "UTF-8");
			post.setRequestEntity(requestEntity);
			client.executeMethod(post);
			responseBody = post.getResponseBodyAsString();
		}
		catch (final UnsupportedEncodingException e)
		{
			LOG.error("Request body error", e);
		}
		catch (final HttpException e)
		{
			LOG.error("Http Exception occurred", e);
			post.releaseConnection();
		}
		catch (final IOException e)
		{
			LOG.error("Returned XML error", e);
			post.releaseConnection();
		}
		finally
		{
			post.releaseConnection();
		}
		return responseBody;
	}

	public String getRequestXml(final CustomerData customer, final AddressData addressData, final String subOrg,
			final String nameForScreen, final String partnerType)
	{
		final Map<String, String> map = new HashMap<>();

		map.put("ptnr_id", customer.getEmail());
		map.put("ptnr_type", partnerType);
		//		map.put("sub_org", "GREENLEE");
		map.put("sub_org", subOrg);
		map.put("app_id", "TRPS4");
		//		map.put("name_1", customer.getName());
		map.put("name_1", nameForScreen);
		map.put("address_1", addressData.getLine1());
		map.put("address_2", addressData.getLine2());
		map.put("address_3", "");
		map.put("address_4", "");
		map.put("address_5", "");
		map.put("city", addressData.getTown());
		if (null != addressData.getRegion())
		{
			map.put("state_name", addressData.getRegion().getName());
		}
		else
		{
			map.put("state_name", addressData.getDistrict());
		}
		map.put("postal_code", addressData.getPostalCode());
		map.put("ctry_name", addressData.getCountry().getName());
		map.put("ctry_code", addressData.getCountry().getIsocode());
		map.put("created_by", "USER");
		map.put("request_url", "http://myurl");
		map.put("use_cached_result", "FALSE");
		map.put("persist", "TRUE");
		map.put("user_varchar1", "TRANSACTION_ID");
		map.put("user_varchar2", "GEOGRAPHICAL_LOCATION");
		map.put("user_varchar3", "TIME_SUBMITTED");

		LOG.info("Livingston params:" + map.toString());

		return writeValueAsString(map);
	}

	public String writeValueAsString(final Map<String, String> map)
	{
		final StringBuilder sb = new StringBuilder("<RPSL_PTNR ");
		final Iterator iter = map.entrySet().iterator();
		while (iter.hasNext())
		{
			final Map.Entry<String, String> entry = (Map.Entry) iter.next();
			final String key = entry.getKey();
			final String val = entry.getValue();
			sb.append(key + "=\"" + val + "\" ");
		}
		sb.append("/>");

		return sb.toString();
	}

	public String getMatchCode(final String responseXML, final CustomerData customer)
	{
		if (responseXML == null)
		{
			LOG.error("ERR_NTFY_SUPPORT_0007 - Livingston customer verification failure for customer " + customer.getEmail());
			return POTENTIAL_MATCH;
		}
		try
		{
			final String decision = String.valueOf(responseXML.charAt(responseXML.toLowerCase().indexOf("decision") + 10));
			final String rplInd = String.valueOf(responseXML.charAt(responseXML.toLowerCase().indexOf("rpl_ind") + 9));

			LOG.info("Livingston status flags:decision-" + decision + ";rplIndicator-" + rplInd);
			if (POTENTIAL_MATCH.equalsIgnoreCase(decision))
			{
				LOG.error("ERR_NTFY_SUPPORT_0007 - Livingston customer verification failure for customer " + customer.getEmail());
				return POTENTIAL_MATCH;
			}
			if (EXACT_MATCH.equalsIgnoreCase(decision))
			{
				LOG.error("ERR_NTFY_SUPPORT_0007 - Livingston customer verification failure for customer " + customer.getEmail());
				return EXACT_MATCH;
			}
			if (NO_MATCH.equalsIgnoreCase(decision))
			{
				return NO_MATCH;
			}
		}
		catch (final Exception exp)
		{
			LOG.error("ERR_NTFY_SUPPORT_0007 - Livingston customer verification failure for customer " + customer.getEmail());
			LOG.error("Error processing livingston response XML", exp);
			return POTENTIAL_MATCH;
		}
		return POTENTIAL_MATCH;
	}

	private boolean isUserB2E()
	{
		boolean b2eUser = false;
		final Set<B2BUnitModel> b2bUnits = new HashSet();
		final UserModel userModel = getUserService().getCurrentUser();
		final Set<PrincipalGroupModel> userGroup = userModel.getGroups();
		final Set<PrincipalGroupModel> newUserGroup = new HashSet<>();

		for (final PrincipalGroupModel userGroupModel : userGroup)
		{
			if (userGroupModel instanceof B2BUnitModel)
			{
				final B2BUnitModel b2bunit = (B2BUnitModel) userGroupModel;
				b2bUnits.add(b2bunit);
			}
			newUserGroup.add(userGroupModel);
		}
		for (final B2BUnitModel b2bUnit : b2bUnits)
		{
			if (UserTypes.B2E.equals(b2bUnit.getUserType()))
			{
				b2eUser = true;
			}
		}
		return b2eUser;
	}

	@Override
	public boolean isEligibleForScreen()
	{
		boolean eligibleForScreen = false;
		final Set<B2BUnitModel> b2bUnits = new HashSet();
		final UserModel userModel = getUserService().getCurrentUser();
		final Set<PrincipalGroupModel> userGroup = userModel.getGroups();
		final Set<PrincipalGroupModel> newUserGroup = new HashSet<>();

		for (final PrincipalGroupModel userGroupModel : userGroup)
		{
			if (userGroupModel instanceof B2BUnitModel)
			{
				final B2BUnitModel b2bunit = (B2BUnitModel) userGroupModel;
				b2bUnits.add(b2bunit);
			}
			newUserGroup.add(userGroupModel);
		}
		for (final B2BUnitModel dummyB2BUnit : b2bUnits)
		{
			/*
			 * final boolean f1 = dummyB2BUnit.getUid().equalsIgnoreCase(DUMMY_UNIT_B2C); final boolean f2 =
			 * dummyB2BUnit.getUid().equalsIgnoreCase(DUMMY_UNIT_B2E); final boolean f3 =
			 * dummyB2BUnit.getUid().equalsIgnoreCase(DUMMY_UNIT_B2B);
			 * 
			 * final boolean f4 = dummyB2BUnit.getUid().equalsIgnoreCase(DUMMY_UNIT_B2C_CAD); final boolean f5 =
			 * dummyB2BUnit.getUid().equalsIgnoreCase(DUMMY_UNIT_B2E_CAD); final boolean f6 =
			 * dummyB2BUnit.getUid().equalsIgnoreCase(DUMMY_UNIT_B2B_CAD);
			 */
			//LOG.error(" isEligibleForScreen " + "f1 " + f1 + " f2 " + f2 + " f3 " + f3 + " f4 " + f4 + " f5 " + f5 + " f6 " + f6);

			if (UserTypes.B2C.equals(dummyB2BUnit.getUserType())
					&& (dummyB2BUnit.getUid().equalsIgnoreCase(DUMMY_UNIT_B2C) || dummyB2BUnit.getUid().equalsIgnoreCase(
							DUMMY_UNIT_B2C_CAD)))
			{
				eligibleForScreen = true;
				return eligibleForScreen;
			}
			else if (UserTypes.B2E.equals(dummyB2BUnit.getUserType()) && dummyB2BUnit.getUid().equalsIgnoreCase(DUMMY_UNIT_B2E)
					|| dummyB2BUnit.getUid().equalsIgnoreCase(DUMMY_UNIT_B2E_CAD))
			{
				eligibleForScreen = true;
				return eligibleForScreen;
			}
			else if (UserTypes.B2B.equals(dummyB2BUnit.getUserType()) && dummyB2BUnit.getUid().equalsIgnoreCase(DUMMY_UNIT_B2B)
					|| dummyB2BUnit.getUid().equalsIgnoreCase(DUMMY_UNIT_B2B_CAD))
			{
				eligibleForScreen = false;
				return eligibleForScreen;
			}
		}
		return eligibleForScreen;
	}

}

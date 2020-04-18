package com.greenlee.core.salesforce.services.impl;

import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.strategies.CustomerNameStrategy;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Required;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.greenlee.core.model.GreenleeB2BCustomerModel;
import com.greenlee.core.salesforce.services.SalesforceService;
import com.greenlee.core.util.GreenleeUtils;


/**
 * @author xiaochen bian
 *
 */
public class DefaultSalesforceService implements SalesforceService
{
	private static final Logger LOG = Logger.getLogger(DefaultSalesforceService.class);
	private static final String B2E_CUSTOMER = "B2E";
	private static final String B2C_CUSTOMER = "B2C";
	private static final String B2B_CUSTOMER = "B2B";
	private static final String B2ECUST = "B2ECUST";
	private static final String B2ECUST_CAD = "B2ECUST_CAD";
	String[] requestBodyFields =
	{ "FirstName", "LastName", "Phone", "Email", "City", "Street", "State", "Country", "Company", "PostalCode", "User_Type__c",
			"ExistingUser__c", "Hybris_User_Id__c", "SAP_Account_Number__c", "Invoice_Number__c", "PO_Number__c", "RecordTypeId",
			"Privacy_Policy_Consent__c", "Sales_and_Marketing_Info_Request__c" };
	private String grantType;
	private String clientId;
	private String clientSecret;
	private String username;
	private String password;
	private String authenticationUrl;
	private String newLeadUrl;
	private String updateSapIdUrl;
	private String recordType;
	private ModelService modelService;
	private String accessToken;
	private String instanceUrl;
	private CustomerNameStrategy customerNameStrategy;
	@Resource(name = "customerAccountService")
	CustomerAccountService customerAccountService;

	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @return the accessToken
	 */
	public String getAccessToken()
	{
		return accessToken;
	}

	/**
	 * @return the instanceUrl
	 */
	public String getInstanceUrl()
	{
		return instanceUrl;
	}

	/**
	 * @param accessToken
	 *           the accessToken to set
	 */
	public void setAccessToken(final String accessToken)
	{
		this.accessToken = accessToken;
	}

	/**
	 * @param instanceUrl
	 *           the instanceUrl to set
	 */
	public void setInstanceUrl(final String instanceUrl)
	{
		this.instanceUrl = instanceUrl;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public String getGrantType()
	{
		return grantType;
	}

	@Required
	public void setGrantType(final String grantType)
	{
		this.grantType = grantType;
	}

	@Required
	public void setClientId(final String clientId)
	{
		this.clientId = clientId;
	}

	@Required
	public void setClientSecret(final String clientSecret)
	{
		this.clientSecret = clientSecret;
	}

	@Required
	public void setUsername(final String username)
	{
		this.username = username;
	}

	@Required
	public void setPassword(final String password)
	{
		this.password = password;
	}

	@Required
	public void setAuthenticationUrl(final String authenticationUrl)
	{
		this.authenticationUrl = authenticationUrl;
	}

	/**
	 * @return the recordType
	 */

	public String getRecordType()
	{
		return recordType;
	}

	/**
	 * @param recordType
	 *           the recordType to set
	 */
	public void setRecordType(final String recordType)
	{
		this.recordType = recordType;
	}

	@Required
	public void setNewLeadUrl(final String newLeadUrl)
	{
		this.newLeadUrl = newLeadUrl;
	}

	public String getClientId()
	{
		return clientId;
	}

	public String getClientSecret()
	{
		return clientSecret;
	}

	public String getUsername()
	{
		return username;
	}

	public String getPassword()
	{
		return password;
	}

	public String getAuthenticationUrl()
	{
		return authenticationUrl;
	}

	public String getNewLeadUrl()
	{
		return newLeadUrl;
	}

	public String getUpdateSapIdUrl()
	{
		return updateSapIdUrl;
	}

	@Required
	public void setUpdateSapIdUrl(final String updateSapIdUrl)
	{
		this.updateSapIdUrl = updateSapIdUrl;
	}

	public JSONObject getAuthenticationToken()
	{
		final HttpClient httpclient = new HttpClient();
		JSONObject authResponse = null;
		PostMethod post = null;
		try
		{
			post = new PostMethod(getAuthenticationUrl());
			LOG.error("Salesforce Authentication url  " + getAuthenticationUrl());
			post.addParameter("grant_type", getGrantType());
			post.addParameter("client_id", getClientId());
			post.addParameter("client_secret", getClientSecret());
			post.addParameter("username", getUsername());
			post.addParameter("password", getPassword());
			final int responseCode = httpclient.executeMethod(post);
			LOG.error("Salesforce Authentication token execution Completed with response code " + responseCode);
			authResponse = new JSONObject(new JSONTokener(new InputStreamReader(post.getResponseBodyAsStream())));
			LOG.error("Salesforce Authentication token successfully created " + authResponse);
			LOG.error("Auth response: " + authResponse.toString(2));
			accessToken = authResponse.getString("access_token");
			instanceUrl = authResponse.getString("instance_url");
			if (null != instanceUrl)
			{
				setInstanceUrl(instanceUrl);
			}
		}
		catch (final HttpException eHttpException)
		{
			LOG.error("ERR_NTFY_SUPPORT_00010 - Salesforce Authentication token failure");
			LOG.error("Exception occurred during token request");
			LOG.error(eHttpException);
		}
		catch (final IOException exception)
		{
			LOG.error("ERR_NTFY_SUPPORT_00010 - Salesforce Authentication token failure");
			LOG.error("JsonResponse error");
			LOG.error(exception);
		}
		return authResponse;
	}

	@SuppressWarnings("boxing")
	@Override
	public void postUserToSaleforce(final GreenleeB2BCustomerModel customer)
	{
		try
		{
			final JSONObject authParams = getAuthenticationToken();
			String accessToken = null;
			String instance_url = null;
			try
			{
				if (authParams.has("access_token"))
				{
					LOG.error("if >> postUserToSaleforce >> access_token: " + authParams.getString("access_token"));
					accessToken = authParams.getString("access_token");
				}
				else
				{
					accessToken = getAccessToken();
					LOG.error("postUserToSaleforce >> access_token: " + accessToken);
				}
				if (authParams.has("instance_url"))
				{
					LOG.error("if >> postUserToSaleforce >> instance_url: " + authParams.getString("instance_url"));
					instance_url = authParams.getString("instance_url");
				}
				else
				{
					instance_url = getInstanceUrl();
					LOG.error("else >> postUserToSaleforce >> instance_url: " + instance_url);
				}
				if (authParams.has("refresh_token"))
				{
					LOG.error("refresh_token: " + authParams.getString("refresh_token"));
				}
			}
			catch (final JSONException eJsonException)
			{
				LOG.error("ERR_NTFY_SUPPORT_00010 - Salesforce registration post failure for customer " + customer.getUid());
				LOG.error("Salesforce registration post failure for customer " + customer.getUid() + " JSONException");
				LOG.error("updateSapIdToSalesforce >> : JSONException " + eJsonException);
			}

			final String jsonString = getJsonString(customer);
			StringRequestEntity requestEntity = null;
			try
			{
				requestEntity = new StringRequestEntity(jsonString, "application/json", "UTF-8");
			}
			catch (final UnsupportedEncodingException e)
			{
				LOG.error("ERR_NTFY_SUPPORT_00010 - Salesforce registration post failure for customer " + customer.getUid());
				LOG.error("Salesforce registration post failure for customer " + customer.getUid() + " UnsupportedEncodingException");
				LOG.error("Request body error", e);
			}
			final PostMethod postMethod = new PostMethod(instance_url + getNewLeadUrl());
			postMethod.setRequestEntity(requestEntity);
			postMethod.addRequestHeader("Authorization", "Bearer " + accessToken);
			final HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(postMethod);
			if (postMethod.getStatusCode() == Integer.valueOf(HttpStatus.SC_CREATED))
			{
				final JSONObject authResponse = new JSONObject(new JSONTokener(new InputStreamReader(
						postMethod.getResponseBodyAsStream())));
				final String salesforceId = authResponse.getString("id");
				customer.setSalesforceId(salesforceId);
				getModelService().save(customer);
				LOG.error("Salesforce Leads are created for customer [" + customer.getUid() + "] User Type [" + customer.getUid()
						+ "] Salesforce Referemce Id:[" + salesforceId + "]");
				LOG.error("Query response: " + authResponse.toString(2));
			}
			else
			{
				LOG.error("ERR_NTFY_SUPPORT_00010 - Salesforce registration post failed for customer " + customer.getUid()
						+ " postUserToSaleforce Status Code :" + postMethod.getStatusCode());
			}
		}
		catch (final Exception exception)
		{
			LOG.error("ERR_NTFY_SUPPORT_00010 - Salesforce registration post failed for customer " + customer.getUid());
			LOG.error("Salesforce registration post failed for customer " + customer.getUid() + " Exception");
			LOG.error("Exception occurred", exception);
		}

	}

	@SuppressWarnings("boxing")
	@Override
	public void updateSapIdToSalesforce(final GreenleeB2BCustomerModel customer)
	{
		String accessToken = null;
		String instance_url = null;
		try
		{
			final JSONObject authParams = getAuthenticationToken();
			if (authParams.has("access_token"))
			{
				LOG.error("if >> updateSapIdToSalesforce >> access_token: " + authParams.getString("access_token"));
				accessToken = authParams.getString("access_token");
			}
			else
			{
				accessToken = getAccessToken();
				LOG.error("else >> updateSapIdToSalesforce >> access_token: " + accessToken);
			}
			if (authParams.has("instance_url"))
			{
				LOG.error("if >> updateSapIdToSalesforce >> instance_url: " + authParams.getString("instance_url"));
				instance_url = authParams.getString("instance_url");
			}
			else
			{
				instance_url = getInstanceUrl();
				LOG.error("else >> updateSapIdToSalesforce >> instance_url: " + instance_url);
			}
			if (authParams.has("refresh_token"))
			{
				LOG.error("updateSapIdToSalesforce >> refresh_token: " + authParams.getString("refresh_token"));
			}
		}
		catch (final JSONException eJsonException)
		{
			LOG.error("Salesforce registration updateSapIdToSalesforce failure for customer " + customer.getUid() + " JSONException");
			LOG.error("Salesforce registration updateSapIdToSalesforce failed for customer " + customer.getUid() + " Exception");
			LOG.error("updateSapIdToSalesforce >> : JSONException " + eJsonException);
		}
		LOG.error("updateSapIdToSalesforce in progress");
		final ObjectMapper om = new ObjectMapper();
		om.configure(SerializationFeature.INDENT_OUTPUT, true);
		om.setSerializationInclusion(Include.NON_NULL);

		final Map<String, String> map = new HashMap<>();
		if (customer.getSessionB2BUnit() != null)
		{
			final String b2bUnit = GreenleeUtils.getAccNoFromB2BUnit(customer.getSessionB2BUnit());
			if (b2bUnit != null && StringUtils.isNotBlank(b2bUnit) && StringUtils.isNotEmpty(b2bUnit))
			{
				if (StringUtils.equals(b2bUnit, B2ECUST))
				{
					LOG.error("if updateSapIdToSalesforce : getSessionB2BUnit :: SAP_Account_Number__c " + b2bUnit);
					map.put("SAP_Account_Number__c", StringUtils.EMPTY);
				}
				else if (StringUtils.equals(b2bUnit, B2ECUST_CAD))
				{
					LOG.error("else if updateSapIdToSalesforce : getSessionB2BUnit :: SAP_Account_Number__c " + b2bUnit);
					map.put("SAP_Account_Number__c", StringUtils.EMPTY);
				}
				else
				{
					LOG.error("else updateSapIdToSalesforce : getSessionB2BUnit :: SAP_Account_Number__c " + b2bUnit);
					map.put("SAP_Account_Number__c", b2bUnit);
				}
			}
		}
		if (customer.getDefaultB2BUnit() != null)
		{
			String defaultB2BUnitString = customer.getDefaultB2BUnit().getUid();
			if (defaultB2BUnitString != null && StringUtils.isNotBlank(defaultB2BUnitString)
					&& StringUtils.isNotEmpty(defaultB2BUnitString))
			{
				if (StringUtils.equals(defaultB2BUnitString, B2ECUST))
				{
					LOG.error("updateSapIdToSalesforce : getDefaultB2BUnit :: SAP_Account_Number__c " + defaultB2BUnitString);
					map.put("SAP_Account_Number__c", StringUtils.EMPTY);
				}
				else if (StringUtils.equals(defaultB2BUnitString, B2ECUST_CAD))
				{
					LOG.error("else if updateSapIdToSalesforce : getDefaultB2BUnit :: SAP_Account_Number__c " + defaultB2BUnitString);
					map.put("SAP_Account_Number__c", StringUtils.EMPTY);
				}
				else
				{
					defaultB2BUnitString = GreenleeUtils.getAccNoFromB2BUnit(customer.getDefaultB2BUnit());
					LOG.error("else updateSapIdToSalesforce : getDefaultB2BUnit :: SAP_Account_Number__c " + defaultB2BUnitString);
					map.put("SAP_Account_Number__c", defaultB2BUnitString);
				}
			}
		}
		else
		{
			LOG.error("updateSapIdToSalesforce : B2Bunit Never created: SAP_Account_Number__c ");
			map.put("SAP_Account_Number__c", StringUtils.EMPTY);
		}
		if (customer.getAgreeToPrivacyPolicy() != null)
		{
			map.put("Privacy_Policy_Consent__c", customer.getAgreeToPrivacyPolicy().toString());
		}
		if (customer.getRequestForInfo() != null)
		{
			map.put("Sales_and_Marketing_Info_Request__c", customer.getRequestForInfo().toString());
		}
		String jsonString = null;
		try
		{
			jsonString = om.writeValueAsString(map);
		}
		catch (final JsonProcessingException processingException)
		{
			LOG.error("ERR_NTFY_SUPPORT_00010 - Salesforce registration update failure for customer");
			LOG.error(processingException);
		}

		StringRequestEntity requestEntity = null;
		try
		{
			requestEntity = new StringRequestEntity(jsonString, "application/json", "UTF-8");
			LOG.error("Sales force update parama " + jsonString);
		}
		catch (final UnsupportedEncodingException encodingException)
		{
			LOG.error("UnsupportedEncodingException - Salesforce registration update failure for customer");
			LOG.error("ERR_NTFY_SUPPORT_00010 - Salesforce registration update failure for customer");
			LOG.error(encodingException);
		}
		LOG.error("Salesforce Update URL: " + instance_url + getUpdateSapIdUrl() + customer.getOriginalUid() + "?_HttpMethod=PATCH");
		final PostMethod patch = new PostMethod(instance_url + getUpdateSapIdUrl() + customer.getOriginalUid()
				+ "?_HttpMethod=PATCH");
		patch.setRequestEntity(requestEntity);
		patch.addRequestHeader("Authorization", "Bearer " + accessToken);
		final HttpClient httpclient = new HttpClient();
		try
		{
			final int responseCode = httpclient.executeMethod(patch);
			LOG.error("Salesforce PATCH call returned a status code of ...." + responseCode);
			if (patch.getStatusCode() == Integer.valueOf(HttpStatus.SC_NO_CONTENT))
			{
				LOG.error("Salesforce update successfully. Response code " + responseCode);
			}
			else
			{
				LOG.error("Salesforce update failed, Response code " + responseCode);
				LOG.error("ERR_NTFY_SUPPORT_00010 - Salesforce registration update failed");
			}
		}
		catch (final HttpException eHttpException)
		{
			LOG.error("ERR_NTFY_SUPPORT_00010 - Salesforce registration update failed, due to Connection issues");
			LOG.error("Exception occurred for creating lead");
			LOG.error(eHttpException);
		}
		catch (final IOException exception)
		{
			LOG.error("ERR_NTFY_SUPPORT_00010 - Salesforce registration update failed, due to Input/Output issues"
					+ exception.getMessage());
			LOG.error("Returned JSON error");
			LOG.error(exception);
		}
	}

	public String getJsonString(final GreenleeB2BCustomerModel customer)
	{
		final ObjectMapper om = new ObjectMapper();
		om.configure(SerializationFeature.INDENT_OUTPUT, true);
		om.setSerializationInclusion(Include.NON_NULL);

		final Map<String, String> map = new HashMap<>();
		if (customer.getDefaultShipmentAddress() == null) // when default shipment address is not setting/null, then set the default primary address/default address.
		{
			final AddressModel address = getAddressBooks(customer);
			if (address != null)
			{
				LOG.error("getJsonString >> setDefaultShipmentAddress on getAddressForCode ");
				customer.setDefaultShipmentAddress(address);
				getModelService().save(customer);
			}
			else
			{
				final AddressModel addressModel = customerAccountService.getDefaultAddress(customer);
				LOG.error("getJsonString >> setDefaultShipmentAddress on getDefaultAddress ");
				if (addressModel != null)
				{
					customer.setDefaultShipmentAddress(addressModel);
					getModelService().save(customer);
				}
			}
			getModelService().refresh(customer);
			LOG.error("end of setDefaultShipmentAddress.");
		}

		if (B2C_CUSTOMER.equals(customer.getUserType()))
		{
			if (customer.getDefaultShipmentAddress() != null)
			{
				final AddressModel address = customer.getDefaultShipmentAddress();
				map.put(requestBodyFields[4], address.getTown());
				map.put(requestBodyFields[5], address.getLine1() + address.getLine2());

				/**
				 * Fix for GRE-2081
				 */
				//			if ("United States".equals(address.getCountry().getName()))
				if (address.getRegion() != null)
				{
					map.put(requestBodyFields[6], address.getRegion().getName());
				}
				else
				{
					map.put(requestBodyFields[6], address.getDistrict());
				}
				map.put(requestBodyFields[7], address.getCountry().getName());
				map.put(requestBodyFields[9], address.getPostalcode());
				map.put(requestBodyFields[2], address.getPhone1());
				//			map.put(requestBodyFields[8], "NA");
				map.put(requestBodyFields[10], "Online Shopper");
				map.put(requestBodyFields[11], "false");
			}

		}
		else if (B2B_CUSTOMER.equals(customer.getUserType()))
		{
			map.put(requestBodyFields[4], "NA");
			map.put(requestBodyFields[5], "NA");
			map.put(requestBodyFields[6], "NA");
			map.put(requestBodyFields[7], "NA");
			map.put(requestBodyFields[9], "NA");
			map.put(requestBodyFields[2], "");
			map.put(requestBodyFields[8], customer.getCompanyName());
			map.put(requestBodyFields[10], "Greenlee Distributor");
			map.put(requestBodyFields[11], "true");

		}
		//		else if (B2E_CUSTOMER.equals(customer.getUserType()) && isB2EAdminUser(customer))
		//GRE-2102 fix, no need to check for user being admin, instead just check if the user has an address.
		else if (B2E_CUSTOMER.equals(customer.getUserType()) && customer.getDefaultShipmentAddress() != null)
		{
			final AddressModel address = customer.getDefaultShipmentAddress();
			map.put(requestBodyFields[4], address.getTown());
			map.put(requestBodyFields[5], address.getLine1() + address.getLine2());

			/**
			 * Fix for GRE-2081
			 */
			//			if ("United States".equals(address.getCountry().getName()))
			if (address.getRegion() != null)
			{
				map.put(requestBodyFields[6], address.getRegion().getName());
			}
			else
			{
				map.put(requestBodyFields[6], address.getDistrict());
			}
			map.put(requestBodyFields[7], address.getCountry().getName());
			map.put(requestBodyFields[9], address.getPostalcode());
			map.put(requestBodyFields[2], address.getPhone1());
			map.put(requestBodyFields[8], customer.getCompanyName());
			map.put(requestBodyFields[10], "Enterprise User");
			map.put(requestBodyFields[11], "false");
		}
		else
		{
			map.put(requestBodyFields[4], "NA");
			map.put(requestBodyFields[5], "NA");
			map.put(requestBodyFields[6], "NA");
			map.put(requestBodyFields[7], "NA");
			map.put(requestBodyFields[9], "NA");
			map.put(requestBodyFields[2], "");
			map.put(requestBodyFields[8], customer.getCompanyName());
			map.put(requestBodyFields[10], "Enterprise User");
			map.put(requestBodyFields[11], "false");

		}
		final String fullName = customer.getName();
		String firstName = null;
		String lastName = null;
		for (int i = 0; i < fullName.length(); i++)
		{
			if (" ".equals(String.valueOf(fullName.charAt(i))))
			{
				firstName = fullName.substring(0, i);
				lastName = fullName.substring(i + 1);
				break;
			}
		}
		map.put(requestBodyFields[0], firstName);
		map.put(requestBodyFields[1], lastName);
		/**
		 * Fix for GRE-1831
		 */
		if (B2C_CUSTOMER.equals(customer.getUserType()))
		{
			map.put(requestBodyFields[8], customerNameStrategy.getName(firstName, lastName));
		}
		/**
		 * Fix for GRE-1831
		 */
		map.put(requestBodyFields[3], customer.getEmail());
		map.put(requestBodyFields[12], customer.getOriginalUid());
		if ("invoiceNumber".equals(customer.getAccountInformation()))
		{
			map.put(requestBodyFields[14], customer.getAccountInformationNumber());
			map.put(requestBodyFields[15], "");
			map.put(requestBodyFields[13], "");
			map.put(requestBodyFields[11], "true");
		}
		else if ("purchaseOrderNumber".equals(customer.getAccountInformation()))
		{
			map.put(requestBodyFields[15], customer.getAccountInformationNumber());
			map.put(requestBodyFields[14], "");
			map.put(requestBodyFields[13], "");
			map.put(requestBodyFields[11], "true");
		}
		else if ("accountNumber".equals(customer.getAccountInformation()))
		{
			map.put(requestBodyFields[15], "");
			map.put(requestBodyFields[14], "");
			map.put(requestBodyFields[13], customer.getAccountInformationNumber());
			map.put(requestBodyFields[11], "true");
		}
		else
		{
			map.put(requestBodyFields[15], "");
			map.put(requestBodyFields[14], "");
			//GRE-2102 fix
			if (B2E_CUSTOMER.equals(customer.getUserType()))
			{
				map.put(requestBodyFields[13], StringUtils.EMPTY);
				map.put(requestBodyFields[11], "false");
			}
			/*
			 * else { map.put(requestBodyFields[13], ""); }
			 */
		}
		map.put(requestBodyFields[16], recordType);

		if (customer.getAgreeToPrivacyPolicy() != null)
		{
			map.put(requestBodyFields[17], customer.getAgreeToPrivacyPolicy().toString());
		}
		if (customer.getRequestForInfo() != null)
		{
			map.put(requestBodyFields[18], customer.getRequestForInfo().toString());
		}

		LOG.info("SalesForce json params in map [" + map.toString() + " ]");

		String jsonString = null;
		try
		{
			jsonString = om.writeValueAsString(map);
			LOG.info("SalesForce json params in string format [" + jsonString + " ]");

		}
		catch (final JsonProcessingException processingException)
		{
			LOG.error("ERR_NTFY_SUPPORT_00010 - Unable to prepare the salesforce param");
			LOG.error("Json Processing Exception");
			LOG.error(processingException);
		}

		return jsonString;
	}

	@SuppressWarnings("unused")
	private boolean isB2EAdminUser(final GreenleeB2BCustomerModel customer)
	{
		boolean result = false;
		if (B2E_CUSTOMER.equalsIgnoreCase(customer.getUserType()))
		{
			final Set<PrincipalGroupModel> userGroups = new HashSet<>(customer.getAllGroups());
			for (final PrincipalGroupModel group : userGroups)
			{
				if ("b2badmingroup".equalsIgnoreCase(group.getUid()))
				{
					result = true;
				}
			}
		}
		return result;
	}

	/**
	 * @return the customerNameStrategy
	 */
	public CustomerNameStrategy getCustomerNameStrategy()
	{
		return customerNameStrategy;
	}

	/**
	 * @param customerNameStrategy
	 *           the customerNameStrategy to set
	 */
	public void setCustomerNameStrategy(final CustomerNameStrategy customerNameStrategy)
	{
		this.customerNameStrategy = customerNameStrategy;
	}

	private AddressModel getAddressBooks(final GreenleeB2BCustomerModel currentCustomer)
	{
		final List<AddressModel> updatedAddressBookList = new ArrayList<AddressModel>();
		// Primary user address.
		/*
		 * if (getUser().getSessionB2BUnit() != null) { final List<AddressData> primaryAddresses =
		 * getUser().getSessionB2BUnit().getAddresses(); if (primaryAddresses != null && !primaryAddresses.isEmpty()) {
		 * LOG.info("Primary Address " + primaryAddresses.size()); addressBookList.addAll(primaryAddresses); } }
		 */

		final List<AddressModel> addresses = customerAccountService.getAddressBookEntries(currentCustomer);
		AddressModel data = null;
		for (final AddressModel addressModel : addresses)
		{
			if (addressModel.getPrimaryAddress() != null && addressModel.getPrimaryAddress().booleanValue())
			{
				updatedAddressBookList.add(addressModel);
				data = addressModel;
				break;
			}
			/*
			 * if (addressData.getDefaultShippingAddress() != null &&
			 * addressData.getDefaultShippingAddress().booleanValue()) { if
			 * (addressData.getDefaultShippingAddress().booleanValue() && addressData.getPrimaryAddress() != null &&
			 * addressData.getPrimaryAddress().booleanValue()) { updatedAddressBookList.add(addressData); } }
			 */
		}
		return data;
	}

}

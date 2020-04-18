/**
 *
 */
package com.greenlee.core.customer.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.commerceservices.security.SecureToken;
import de.hybris.platform.commerceservices.security.SecureTokenService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.daos.CurrencyDao;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.impl.UniqueAttributesInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.greenlee.core.customer.GreenleeCustomerAccountService;
import com.greenlee.core.model.GreenleeB2BCustomerModel;
import com.greenlee.core.salesforce.services.SalesforceService;
import com.greenlee.facades.customer.data.B2CRegisterData;


/**
 * @author aruna
 *
 */
public class GreenleeCustomerAccountServiceImpl implements GreenleeCustomerAccountService
{
	/**
	 *
	 */
	private static final Logger LOG = Logger.getLogger(GreenleeCustomerAccountServiceImpl.class);

	private static final String B2E_UNIT_ID = "greenlee.account.dummy.distributor.b2e.uid";
	private static final String B2C_UNIT_ID = "greenlee.account.dummy.distributor.b2c.uid";
	private static final String B2B_UNIT_ID = "greenlee.account.dummy.distributor.uid";

	private static final String B2E_UNIT_ID_CAD = "greenlee.account.dummy.distributor.b2e.uid.cad";
	private static final String B2C_UNIT_ID_CAD = "greenlee.account.dummy.distributor.b2c.uid.cad";
	private static final String B2B_UNIT_ID_CAD = "greenlee.account.dummy.distributor.uid.cad";

	private static final String DEFAULT_PASSWORD_ENCODING = "sha-512";
	private static final String B2C = "B2C";
	private static final String B2E = "B2E";
	private static final String B2B = "B2B";
	private static final String B2B_CUSTOMER_GROUP = "b2bcustomergroup";

	private UserService userService;

	private ModelService modelService;
	@Resource
	private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;
	private SecureTokenService secureTokenService;
	private long tokenValiditySeconds;
	private String passwordEncoding = DEFAULT_PASSWORD_ENCODING;

	@Autowired
	ConfigurationService configurationService;

	@Resource(name = "salesforceService")
	private SalesforceService salesforceService;

	@Resource(name = "currencyDao")
	private CurrencyDao currencyDao;

	/**
	 * @return the modelService
	 */

	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the userService
	 */

	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */

	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}


	/**
	 * @return the salesforceService
	 */
	public SalesforceService getSalesforceService()
	{
		return salesforceService;
	}

	/**
	 * @param salesforceService
	 *           the salesforceService to set
	 */
	public void setSalesforceService(final SalesforceService salesforceService)
	{
		this.salesforceService = salesforceService;
	}

	/**
	 * @return the currencyDao
	 */
	public CurrencyDao getCurrencyDao()
	{
		return currencyDao;
	}

	/**
	 * @param currencyDao
	 *           the currencyDao to set
	 */
	public void setCurrencyDao(final CurrencyDao currencyDao)
	{
		this.currencyDao = currencyDao;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.greenlee.core.customer.GLCustomerAccountService#saveB2CUser(com.greenlee.core.model.B2CCustomerModel,
	 * com.greenlee.facades.customer.data.B2CRegisterData)
	 */
	@Override
	public void saveB2BUser(final B2BCustomerModel customerModel, final B2CRegisterData registerData) throws DuplicateUidException
	{
		B2BUnitModel b2bUnitModel = null;
		CurrencyModel currencyModel = null;
		String b2bUnitId = null;
		final Set<PrincipalGroupModel> units = new HashSet<PrincipalGroupModel>();
		if (customerModel.getGroups() != null && !customerModel.getGroups().isEmpty())
		{
			units.addAll(customerModel.getGroups());
		}

		if (registerData.getCountry().equalsIgnoreCase("CA"))
		{
			if (registerData.getUserType().equalsIgnoreCase(B2B))
			{
				b2bUnitId = configurationService.getConfiguration().getProperty(B2B_UNIT_ID_CAD).toString();
			}
			if (registerData.getUserType().equalsIgnoreCase(B2C))
			{
				b2bUnitId = configurationService.getConfiguration().getProperty(B2C_UNIT_ID_CAD).toString();
			}
			if (registerData.getUserType().equalsIgnoreCase(B2E))
			{
				b2bUnitId = configurationService.getConfiguration().getProperty(B2E_UNIT_ID_CAD).toString();
			}
			currencyModel = currencyDao.findCurrenciesByCode("CAD").get(0);
			LOG.error("Choosing currency perference >> Country [" + registerData.getCountry() + " ] ISO CODE [ "
					+ currencyModel.getIsocode() + "] Reference Unit [" + b2bUnitId + " ] User Type [" + registerData.getUserType()
					+ " ]");
			b2bUnitModel = b2bUnitService.getUnitForUid(b2bUnitId);
			if (b2bUnitModel != null && !units.contains(b2bUnitModel))
			{
				units.add(b2bUnitModel);
				b2bUnitModel.setCurrencyPreference(currencyModel);
			}
			customerModel.setDefaultB2BUnit(b2bUnitModel);
			customerModel.setSessionCurrency(currencyModel); //GSM-09
			generateCustomerId(customerModel);
			customerModel.setGroups(units);
		}
		else
		{
			if (registerData.getUserType().equalsIgnoreCase(B2B))
			{
				b2bUnitId = configurationService.getConfiguration().getProperty(B2B_UNIT_ID).toString();
			}
			if (registerData.getUserType().equalsIgnoreCase(B2C))
			{
				b2bUnitId = configurationService.getConfiguration().getProperty(B2C_UNIT_ID).toString();
			}
			if (registerData.getUserType().equalsIgnoreCase(B2E))
			{
				b2bUnitId = configurationService.getConfiguration().getProperty(B2E_UNIT_ID).toString();
			}
			currencyModel = currencyDao.findCurrenciesByCode("USD").get(0);
			LOG.error("Choosing currency perference >> Country [" + registerData.getCountry() + " ] ISO CODE [ "
					+ currencyModel.getIsocode() + "] Reference Unit [" + b2bUnitId + " ] User Type [" + registerData.getUserType()
					+ " ]");
			b2bUnitModel = b2bUnitService.getUnitForUid(b2bUnitId);
			if (b2bUnitModel != null && !units.contains(b2bUnitModel))
			{
				units.add(b2bUnitModel);
				b2bUnitModel.setCurrencyPreference(currencyModel);
			}
			customerModel.setDefaultB2BUnit(b2bUnitModel);
			customerModel.setSessionCurrency(currencyModel); //GSM-09
			generateCustomerId(customerModel);
			customerModel.setGroups(units);
		}

		final UserGroupModel userGroupModel = this.getUserService().getUserGroupForUID(B2B_CUSTOMER_GROUP);
		if (userGroupModel != null)
		{
			final Set<String> userGroups = new HashSet<String>();
			userGroups.add(userGroupModel.getUid());
			updateUserGroups(Arrays.asList(new String[] {}), userGroups, customerModel);
		}
		if (registerData.getPassword() != null)
		{
			getUserService().setPassword(customerModel, registerData.getPassword(), DEFAULT_PASSWORD_ENCODING);
		}
		internalSaveCustomer(customerModel);
	}


	private Set<PrincipalGroupModel> updateUserGroups(final Collection<String> availableUserGroups,
			final Collection<String> selectedUserGroups, final B2BCustomerModel customerModel)
	{
		final Set<PrincipalGroupModel> customerGroups = new HashSet<PrincipalGroupModel>(customerModel.getGroups());

		// If you pass in NULL then nothing will happen
		if (selectedUserGroups != null)
		{
			for (final String group : availableUserGroups)
			{
				// add a group
				final UserGroupModel userGroupModel = getUserService().getUserGroupForUID(group);
				if (selectedUserGroups.contains(group))
				{
					customerGroups.add(userGroupModel);
				}
				else
				{ // remove a group
					customerGroups.remove(userGroupModel);
				}
			}
			customerModel.setGroups(customerGroups);
		}

		return customerGroups;
	}



	@Override
	public void updatePassword(final String token, final String newPassword) throws TokenInvalidatedException
	{
		Assert.hasText(token, "The field [token] cannot be empty");
		Assert.hasText(newPassword, "The field [newPassword] cannot be empty");

		final SecureToken data = getSecureTokenService().decryptData(token);
		if (getTokenValiditySeconds() > 0L)
		{
			final long delta = new Date().getTime() - data.getTimeStamp();
			if (delta / 1000 > getTokenValiditySeconds())
			{
				throw new IllegalArgumentException("token expired");
			}
		}

		final CustomerModel customer = getUserService().getUserForUID(data.getData(), CustomerModel.class);
		if (customer == null)
		{
			throw new IllegalArgumentException("user for token not found");
		}
		if (!token.equals(customer.getToken()))
		{
			throw new TokenInvalidatedException();
		}
		if (customer instanceof B2BCustomerModel)
		{
			((B2BCustomerModel) customer).setActive(Boolean.TRUE);
		}
		customer.setToken(null);
		customer.setLoginDisabled(false);
		getModelService().save(customer);
		getUserService().setPassword(data.getData(), newPassword, getPasswordEncoding());
		getModelService().refresh(customer);
	}


	/**
	 * Saves the customer translating model layer exceptions regarding duplicate identifiers
	 *
	 * @param customerModel
	 * @throws DuplicateUidException
	 *            if the uid is not unique
	 */
	protected void internalSaveCustomer(final B2BCustomerModel customerModel) throws DuplicateUidException
	{
		try
		{
			getModelService().save(customerModel);
		}
		catch (final ModelSavingException e)
		{
			if (e.getCause() instanceof InterceptorException
					&& ((InterceptorException) e.getCause()).getInterceptor().getClass().equals(UniqueAttributesInterceptor.class))
			{
				LOG.error("Duplicate uid exception " + customerModel.getUid());
				throw new DuplicateUidException(customerModel.getUid(), e);

			}
			else
			{
				LOG.error(e);
			}
		}
		catch (final AmbiguousIdentifierException e)
		{
			throw new DuplicateUidException(customerModel.getUid(), e);
		}
	}

	/**
	 * Generates a customer ID during registration
	 *
	 * @param customerModel
	 */
	protected void generateCustomerId(final B2BCustomerModel customerModel)
	{
		customerModel.setCustomerID(UUID.randomUUID().toString());
	}

	/**
	 * @return the secureTokenService
	 */

	public SecureTokenService getSecureTokenService()
	{
		return secureTokenService;
	}

	/**
	 * @param secureTokenService
	 *           the secureTokenService to set
	 */

	@Required
	public void setSecureTokenService(final SecureTokenService secureTokenService)
	{
		this.secureTokenService = secureTokenService;
	}

	/**
	 * @return the tokenValiditySeconds
	 */

	protected long getTokenValiditySeconds()
	{
		return tokenValiditySeconds;
	}

	/**
	 * @param tokenValiditySeconds
	 *           the tokenValiditySeconds to set
	 */

	@Required
	public void setTokenValiditySeconds(final long tokenValiditySeconds)
	{
		if (tokenValiditySeconds < 0)
		{
			throw new IllegalArgumentException("tokenValiditySeconds has to be >= 0");
		}
		this.tokenValiditySeconds = tokenValiditySeconds;
	}


	protected String getPasswordEncoding()
	{
		return passwordEncoding;
	}


	public void setPasswordEncoding(final String passwordEncoding)
	{
		Assert.hasText(passwordEncoding);
		this.passwordEncoding = passwordEncoding;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.greenlee.facades.customer.GreenleeCustomerFacade#updatePrivacyPolicyAndMarketingInfo(de.hybris.platform.core
	 * .model.user.CustomerModel, boolean, boolean)
	 */
	@Override
	public void updatePrivacyMarketingInfo(final CustomerModel customer, final boolean privacy, final boolean requestInfo)
	{
		try
		{
			customer.setRequestForInfo(Boolean.valueOf(requestInfo));
			customer.setAgreeToPrivacyPolicy(Boolean.valueOf(requestInfo));
			getModelService().save(customer);
			getSalesforceService().updateSapIdToSalesforce((GreenleeB2BCustomerModel) customer);
			LOG.error("Salesforce real time customer post successful.");
		}
		catch (final Exception error)
		{
			LOG.error("ERR_NTFY_SUPPORT_00011 - Salesforce update error, From the Update on Password Reset failure for customer "
					+ customer.getUid());
			LOG.error(error);
			LOG.error("Salesforce update error, While Password Reset", error);
		}

	}
}

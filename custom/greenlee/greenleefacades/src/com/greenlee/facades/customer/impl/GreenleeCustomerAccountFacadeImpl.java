/**
 *
 */
package com.greenlee.facades.customer.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.commerceservices.event.RegisterEvent;
import de.hybris.platform.commerceservices.security.SecureToken;
import de.hybris.platform.commerceservices.security.SecureTokenService;
import de.hybris.platform.commerceservices.strategies.CustomerNameStrategy;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.PasswordEncoderService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.greenlee.core.customer.GreenleeCustomerAccountService;
import com.greenlee.core.model.GreenleeB2BCustomerModel;
import com.greenlee.core.salesforce.services.SalesforceService;
import com.greenlee.facades.customer.GreenleeCustomerAccountFacade;
import com.greenlee.facades.customer.data.B2CRegisterData;


/**
 * @author aruna
 *
 */
public class GreenleeCustomerAccountFacadeImpl implements GreenleeCustomerAccountFacade
{
    private static final Logger            LOG          = Logger.getLogger(GreenleeCustomerAccountFacadeImpl.class);
    private static final String            B2B_CUSTOMER = "B2B";
    private static final String            B2C_CUSTOMER = "B2C";
    private static final String            B2E_CUSTOMER = "B2E";
    private ModelService                   modelService;
    private CustomerNameStrategy           customerNameStrategy;
    private PasswordEncoderService         passwordEncoderService;
    private UserService                    userService;
    private CommonI18NService              commonI18NService;
    private GreenleeCustomerAccountService glCustomerAccountService;
    private EventService                   eventService;
    private BaseSiteService                baseSiteService;
    private BaseStoreService               baseStoreService;
    private SecureTokenService             secureTokenService;
    private SalesforceService              salesforceService;

    /*
     * (non-Javadoc)
     *
     * @see com.greenlee.facades.customer.GLCustomerFacade#saveB2BCustomer()
     */
    @Override
    public GreenleeB2BCustomerModel saveCustomer(final B2CRegisterData registerData) throws DuplicateUidException
    {

        LOG.info("saveCustomer GreenleecustomeraccountFacade");
        logRegistrationData(registerData);
        Assert.hasText(registerData.getFirstName(), "The field [FirstName] cannot be empty");
        Assert.hasText(registerData.getLastName(), "The field [LastName] cannot be empty");
        Assert.hasText(registerData.getLogin(), "The field [Login] cannot be empty");
        final GreenleeB2BCustomerModel newCustomer = getModelService().create(GreenleeB2BCustomerModel.class);
        newCustomer.setUserAddedByAdmin(Boolean.valueOf(false));//GRE-2153 through User Registration, it was false
        newCustomer.setName(getCustomerNameStrategy().getName(registerData.getFirstName(), registerData.getLastName()));
        newCustomer.setEmail(registerData.getLogin());
        if (StringUtils.isNotBlank(registerData.getFirstName()) && StringUtils.isNotBlank(registerData.getLastName()))
        {
            newCustomer.setName(getCustomerNameStrategy().getName(registerData.getFirstName(), registerData.getLastName()));
        }
        final TitleModel title = getUserService().getTitleForCode(registerData.getTitleCode());
        newCustomer.setTitle(title);
        newCustomer.setUserType(registerData.getUserType());
        setUidForRegister(registerData, newCustomer);
        newCustomer.setSessionLanguage(getCommonI18NService().getCurrentLanguage());
        //        newCustomer.setSessionCurrency(getCommonI18NService().getCurrentCurrency()); // GSM-09

        if (StringUtils.isNotBlank(registerData.getFirstName()) && StringUtils.isNotBlank(registerData.getLastName()))
        {
            newCustomer.setName(getCustomerNameStrategy().getName(registerData.getFirstName(), registerData.getLastName()));
        }
        newCustomer.setEmail(registerData.getLogin());

        //Adding secure token for email verification
        final long timeStamp = new Date().getTime();
        final SecureToken data = new SecureToken(registerData.getLogin(), timeStamp);
        final String token = getSecureTokenService().encryptData(data);
        newCustomer.setToken(token);

        //Disable the login
        newCustomer.setAccountVerified(Boolean.FALSE);

        newCustomer.setUserType(registerData.getUserType());

        if (StringUtils.isNotBlank(registerData.getCompanyName()))
        {
            newCustomer.setCompanyName(registerData.getCompanyName());
        }
        if (StringUtils.isNotBlank(registerData.getAccountInformationNumber()))
        {
            newCustomer.setAccountInformationNumber(registerData.getAccountInformationNumber());
        }

        if (StringUtils.isNotBlank(registerData.getAccountInformation()))
        {
            newCustomer.setAccountInformation(registerData.getAccountInformation());
        }
        if (StringUtils.isNotBlank(registerData.getFirstName()) && StringUtils.isNotBlank(registerData.getLastName()))
        {
            newCustomer.setName(getCustomerNameStrategy().getName(registerData.getFirstName(), registerData.getLastName()));
        }
        if (registerData.isAgreeToPrivacyPolicy())
        {
            newCustomer.setAgreeToPrivacyPolicy(Boolean.valueOf(registerData.isAgreeToPrivacyPolicy()));
        }
        else
        {
            newCustomer.setAgreeToPrivacyPolicy(Boolean.valueOf(false));
        }
        if (registerData.isRequestForInfo())
        {
            newCustomer.setRequestForInfo(Boolean.valueOf(registerData.isRequestForInfo()));
        }
        else
        {
            newCustomer.setRequestForInfo(Boolean.valueOf(false));
        }

        logRegistrationData(registerData);
        glCustomerAccountService.saveB2BUser(newCustomer, registerData);

        if (B2E_CUSTOMER.equalsIgnoreCase(registerData.getUserType()) && !registerData.isHasExistingGreenleeAccount())
        {
            final UserGroupModel b2BAdminGroup = getUserService().getUserGroupForUID("b2badmingroup");
            final Set<PrincipalGroupModel> userGroups = new HashSet<>(newCustomer.getAllGroups());
            userGroups.add(b2BAdminGroup);
            newCustomer.setGroups(userGroups);
            getModelService().save(newCustomer);
            getModelService().refresh(newCustomer);
        }
        // GRE-1261 - Email should NOT trigger for B2B user. Greenlee customer service will trigger manually after verification process for b2b users.
        if (!B2B_CUSTOMER.equalsIgnoreCase(registerData.getUserType()))
        {
            getEventService().publishEvent(initializeEvent(new RegisterEvent(), newCustomer));
        }
        if (B2B_CUSTOMER.equalsIgnoreCase(registerData.getUserType()))
        {
            getSalesforceService().postUserToSaleforce(newCustomer);
        }

        return newCustomer;
    }

    /**
     * initializes an {@link AbstractCommerceUserEvent}
     *
     * @param event
     * @param customerModel
     * @return the event
     */
    protected AbstractCommerceUserEvent initializeEvent(final AbstractCommerceUserEvent event, final CustomerModel customerModel)
    {
        event.setBaseStore(getBaseStoreService().getCurrentBaseStore());
        event.setSite(getBaseSiteService().getCurrentBaseSite());
        event.setCustomer(customerModel);
        event.setLanguage(getCommonI18NService().getCurrentLanguage());
        event.setCurrency(getCommonI18NService().getCurrentCurrency());
        return event;
    }

    /**
     * Initializes a customer with given registerData
     */
    protected void setUidForRegister(final B2CRegisterData registerData, final B2BCustomerModel customer)
    {
        customer.setUid(registerData.getLogin().toLowerCase());
        customer.setOriginalUid(registerData.getLogin());
    }



    /**
     * @return the baseStoreService
     */
    public BaseStoreService getBaseStoreService()
    {
        return baseStoreService;
    }

    /**
     * @param baseStoreService
     *            the baseStoreService to set
     */
    public void setBaseStoreService(final BaseStoreService baseStoreService)
    {
        this.baseStoreService = baseStoreService;
    }

    /**
     * @return the baseSiteService
     */
    public BaseSiteService getBaseSiteService()
    {
        return baseSiteService;
    }

    /**
     * @param baseSiteService
     *            the baseSiteService to set
     */
    public void setBaseSiteService(final BaseSiteService baseSiteService)
    {
        this.baseSiteService = baseSiteService;
    }

    /**
     * @return the eventService
     */
    public EventService getEventService()
    {
        return eventService;
    }

    /**
     * @param eventService
     *            the eventService to set
     */
    public void setEventService(final EventService eventService)
    {
        this.eventService = eventService;
    }

    /**
     * @return the glCustomerAccountService
     */
    public GreenleeCustomerAccountService getGlCustomerAccountService()
    {
        return glCustomerAccountService;
    }

    /**
     * @param glCustomerAccountService
     *            the glCustomerAccountService to set
     */
    public void setGlCustomerAccountService(final GreenleeCustomerAccountService glCustomerAccountService)
    {
        this.glCustomerAccountService = glCustomerAccountService;
    }

    /**
     * @return the commonI18NService
     */
    public CommonI18NService getCommonI18NService()
    {
        return commonI18NService;
    }

    /**
     * @param commonI18NService
     *            the commonI18NService to set
     */
    public void setCommonI18NService(final CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
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
     *            the userService to set
     */
    public void setUserService(final UserService userService)
    {
        this.userService = userService;
    }

    /**
     * @return the modelService
     */
    public ModelService getModelService()
    {
        return modelService;
    }

    /**
     * @param modelService
     *            the modelService to set
     */
    public void setModelService(final ModelService modelService)
    {
        this.modelService = modelService;
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
     *            the customerNameStrategy to set
     */
    public void setCustomerNameStrategy(final CustomerNameStrategy customerNameStrategy)
    {
        this.customerNameStrategy = customerNameStrategy;
    }

    /**
     * @return the passwordEncoderService
     */
    public PasswordEncoderService getPasswordEncoderService()
    {
        return passwordEncoderService;
    }

    /**
     * @param passwordEncoderService
     *            the passwordEncoderService to set
     */
    public void setPasswordEncoderService(final PasswordEncoderService passwordEncoderService)
    {
        this.passwordEncoderService = passwordEncoderService;
    }

    /**
     * @return the secureTokenService
     */
    protected SecureTokenService getSecureTokenService()
    {
        return secureTokenService;
    }

    /**
     * @param secureTokenService
     *            the secureTokenService to set
     */
    @Required
    public void setSecureTokenService(final SecureTokenService secureTokenService)
    {
        this.secureTokenService = secureTokenService;
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
     *            the salesforceService to set
     */
    @Required
    public void setSalesforceService(final SalesforceService salesforceService)
    {
        this.salesforceService = salesforceService;
    }

    /* (non-Javadoc)
     * @see com.greenlee.facades.customer.GreenleeCustomerAccountFacade#logRegistrationData(com.greenlee.facades.customer.data.B2CRegisterData)
     */
    @Override
    public void logRegistrationData(final B2CRegisterData registerData)

    {

        final StringBuilder str = new StringBuilder("User Registration Details : \n");
        if (registerData != null)
        {
            if (registerData.isHasExistingGreenleeAccount())
            {
                str.append("\n[User has existing Account]\n");
            }
            for (final Field field : registerData.getClass().getDeclaredFields())
            {
                field.setAccessible(true);
                try
                {
                    if (field.get(registerData) != null)
                    {
                        str.append("[" + field.getName().toUpperCase() + " : " + field.get(registerData) + "]\n");
                    }
                }
                catch (final IllegalArgumentException e)
                {
                    LOG.error("Exception thrown", e);
                }
                catch (final IllegalAccessException e)
                {
                    LOG.error("Exception thrown", e);
                }

            }
        }

        LOG.error(str.toString());

    }
}

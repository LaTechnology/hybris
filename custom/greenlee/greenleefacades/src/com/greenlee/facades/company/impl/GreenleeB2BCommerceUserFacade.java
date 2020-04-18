/**
 *
 */
package com.greenlee.facades.company.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2bacceleratorfacades.company.impl.DefaultB2BCommerceUserFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.commerceservices.security.SecureToken;
import de.hybris.platform.commerceservices.security.SecureTokenService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.greenlee.core.event.GreenleeForgottenPwdEvent;
import com.greenlee.core.model.GreenleeB2BCustomerModel;
import com.greenlee.core.salesforce.services.SalesforceService;


/**
 * @author xiaochen bian
 *
 */

public class GreenleeB2BCommerceUserFacade extends DefaultB2BCommerceUserFacade
{
    private CommonI18NService      commonI18NService;
    private BaseStoreService       baseStoreService;
    private BaseSiteService        baseSiteService;
    private EventService           eventService;
    private SecureTokenService     secureTokenService;
    private CustomerAccountService customerAccountService;
    private SalesforceService      salesforceService;

    public SalesforceService getSalesforceService()
    {
        return salesforceService;
    }

    public void setSalesforceService(final SalesforceService salesforceService)
    {
        this.salesforceService = salesforceService;
    }

    public CustomerAccountService getCustomerAccountService()
    {
        return customerAccountService;
    }

    public void setCustomerAccountService(final CustomerAccountService customerAccountService)
    {
        this.customerAccountService = customerAccountService;
    }

    public SecureTokenService getSecureTokenService()
    {
        return secureTokenService;
    }

    public void setSecureTokenService(final SecureTokenService secureTokenService)
    {
        this.secureTokenService = secureTokenService;
    }

    public CommonI18NService getCommonI18NService()
    {
        return commonI18NService;
    }

    public void setCommonI18NService(final CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }

    @Override
    public BaseStoreService getBaseStoreService()
    {
        return baseStoreService;
    }

    @Override
    public void setBaseStoreService(final BaseStoreService baseStoreService)
    {
        this.baseStoreService = baseStoreService;
    }

    public BaseSiteService getBaseSiteService()
    {
        return baseSiteService;
    }

    public void setBaseSiteService(final BaseSiteService baseSiteService)
    {
        this.baseSiteService = baseSiteService;
    }

    public EventService getEventService()
    {
        return eventService;
    }

    public void setEventService(final EventService eventService)
    {
        this.eventService = eventService;
    }

    @Override
    public void updateCustomer(final CustomerData customerData)
    {
        validateParameterNotNullStandardMessage("customerData", customerData);
        Assert.hasText(customerData.getTitleCode(), "The field [TitleCode] cannot be empty");
        Assert.hasText(customerData.getFirstName(), "The field [FirstName] cannot be empty");
        Assert.hasText(customerData.getLastName(), "The field [LastName] cannot be empty");
        final GreenleeB2BCustomerModel customerModel;
        if (StringUtils.isEmpty(customerData.getUid()))
        {
            customerModel = this.getModelService().create(GreenleeB2BCustomerModel.class);
            //Setting new user to inactive so that it can be set to active once password is reset
            customerModel.setActive(Boolean.FALSE);
            customerModel.setRequestForInfo(Boolean.FALSE);
            customerModel.setAgreeToPrivacyPolicy(Boolean.FALSE);
            customerModel.setUserAddedByAdmin(Boolean.TRUE);
        }
        else
        {
            customerModel = getCompanyB2BCommerceService().getCustomerForUid(customerData.getUid());
        }
        getB2BCustomerReversePopulator().populate(customerData, customerModel);
        final long timeStamp = new Date().getTime();
        final SecureToken data = new SecureToken(customerData.getEmail(), timeStamp);
        final String token = getSecureTokenService().encryptData(data);
        customerModel.setToken(token);
        customerModel.setAccountVerified(Boolean.TRUE);
        customerModel.setUserType(((GreenleeB2BCustomerModel) getUserService().getCurrentUser()).getUserType());
        if (((GreenleeB2BCustomerModel) getUserService().getCurrentUser()).getCompanyName() != null)
        {
            customerModel.setCompanyName(((GreenleeB2BCustomerModel) getUserService().getCurrentUser()).getCompanyName());
        }
        else
        {
            customerModel.setCompanyName("NA");
        }
        //        getSalesforceService().postUserToSaleforce(customerModel); //GRE-2153
        getCompanyB2BCommerceService().saveModel(customerModel);
        getCustomerAccountService().forgottenPassword(customerModel);

        getEventService().publishEvent(
                initializeEvent(
                        new GreenleeForgottenPwdEvent(new Boolean(customerData.isUserAddedByAdmin()), customerModel.getToken()),
                        customerModel));
    }

    protected AbstractCommerceUserEvent initializeEvent(final AbstractCommerceUserEvent event,
            final B2BCustomerModel customerModel)
    {
        event.setBaseStore(getBaseStoreService().getCurrentBaseStore());
        event.setSite(getBaseSiteService().getCurrentBaseSite());
        event.setCustomer(customerModel);
        event.setLanguage(getCommonI18NService().getCurrentLanguage());
        event.setCurrency(getCommonI18NService().getCurrentCurrency());

        return event;
    }

}

/**
 *
 */
package com.greenlee.facades.customer.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bacceleratorfacades.customer.impl.DefaultB2BCustomerFacade;
import de.hybris.platform.b2bacceleratorservices.company.CompanyB2BCommerceService;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.commerceservices.security.SecureToken;
import de.hybris.platform.commerceservices.security.SecureTokenService;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.greenlee.core.event.GreenleeForgottenPwdEvent;
import com.greenlee.core.model.GreenleeB2BCustomerModel;
import com.greenlee.core.salesforce.services.SalesforceService;
import com.greenlee.facades.customer.GreenleeCustomerFacade;


/**
 * Default implementation for GreenleeCustomerFacade
 *
 * @author raja.santhanam
 *
 */
public class DefaultGreenleeCustomerFacade extends DefaultB2BCustomerFacade implements GreenleeCustomerFacade
{

    private static final Logger                  LOG = Logger.getLogger(DefaultGreenleeCustomerFacade.class);


    private CompanyB2BCommerceService            companyB2BCommerceService;
    private FlexibleSearchService                flexService;
    private Converter<B2BUnitModel, B2BUnitData> b2bUnitConverter;
    private SecureTokenService                   secureTokenService;
    private SalesforceService                    salesforceService;

    private CartService                          cartService;
    private EventService                         eventService;

    private BaseStoreService                     baseStoreService;
    private BaseSiteService                      baseSiteService;


    /* (non-Javadoc)
     * @see de.hybris.platform.commercefacades.customer.impl.DefaultCustomerFacade#updateProfile(de.hybris.platform.commercefacades.user.data.CustomerData)
     */
    @Override
    public void updateProfile(final CustomerData customerData) throws DuplicateUidException
    {
        validateDataBeforeUpdate(customerData);

        final String name = getCustomerNameStrategy().getName(customerData.getFirstName(), customerData.getLastName());
        final GreenleeB2BCustomerModel customer = (GreenleeB2BCustomerModel) getCurrentSessionCustomer();
        customer.setOriginalUid(customerData.getDisplayUid());
        getCustomerAccountService().updateProfile(customer, customerData.getTitleCode(), name, customerData.getUid());
        customer.setEmail(customerData.getEmail());
        getModelService().save(customer);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.greenlee.facades.customer.GreenleeCustomerFacade#updateSelectedB2BUnit(java.lang.String,
     * java.lang.String)
     */
    @Override
    public B2BUnitData updateSelectedB2BUnit(final String useruid, final String b2bUnit)
    {
        B2BUnitModel b2bUnitModel = companyB2BCommerceService.getUnitForUid(b2bUnit);
        final GreenleeB2BCustomerModel b2bUser = companyB2BCommerceService.getCustomerForUid(useruid);
        b2bUser.setSessionB2BUnit(b2bUnitModel);
        getModelService().save(b2bUser);
        LOG.error("Saved B2bUnitModel to Customer");
        b2bUnitModel = b2bUser.getSessionB2BUnit();
        final CartModel cart = cartService.getSessionCart();
        if (b2bUnitModel != null)
        {
            if (cart != null)
            {
                cart.setUnit(b2bUnitModel);
                getModelService().save(cart);
            }
        }
        else
        {
            b2bUnitModel = b2bUser.getDefaultB2BUnit();
            LOG.error("Setting B2bUnitModel to Order" + b2bUnitModel.getUid());
            if (cart != null)
            {
                cart.setUnit(b2bUnitModel);
                getModelService().save(cart);
            }
        }
        return b2bUnitConverter.convert(b2bUnitModel);
    }

    /* (non-Javadoc)
     * @see com.greenlee.facades.customer.GreenleeCustomerFacade#emailVerifiedAccount(java.lang.String)
     */
    @Override
    public void verifyUserEmailAddress(final String token) throws TokenInvalidatedException
    {
        final SecureToken data = getSecureTokenService().decryptData(token);

        final CustomerModel customer = getUserService().getUserForUID(data.getData(), CustomerModel.class);
        if (customer == null)
        {
            throw new IllegalArgumentException("user for token not found");
        }
        if (!token.equals(customer.getToken()))
        {
            throw new TokenInvalidatedException();
        }
        customer.setToken(null);
        customer.setAccountVerified(Boolean.TRUE);
        getModelService().save(customer);

        try
        {
            getSalesforceService().postUserToSaleforce((GreenleeB2BCustomerModel) customer);
        }
        catch (final Exception e)
        {
            LOG.error("ERR_NTFY_SUPPORT_00010 - Salesforce registration post failure for customer" + customer.getUid());
            LOG.info(e);
        }

    }

    /**
     * @return the companyB2BCommerceService
     */
    public CompanyB2BCommerceService getCompanyB2BCommerceService()
    {
        return companyB2BCommerceService;
    }

    /**
     * @param companyB2BCommerceService
     *            the companyB2BCommerceService to set
     */
    public void setCompanyB2BCommerceService(final CompanyB2BCommerceService companyB2BCommerceService)
    {
        this.companyB2BCommerceService = companyB2BCommerceService;
    }

    /**
     * @return the flexService
     */
    public FlexibleSearchService getFlexService()
    {
        return flexService;
    }

    /**
     * @param flexService
     *            the flexService to set
     */
    public void setFlexService(final FlexibleSearchService flexService)
    {
        this.flexService = flexService;
    }

    /**
     * @return the b2bUnitConverter
     */
    public Converter<B2BUnitModel, B2BUnitData> getB2bUnitConverter()
    {
        return b2bUnitConverter;
    }

    /**
     * @param b2bUnitConverter
     *            the b2bUnitConverter to set
     */
    public void setB2bUnitConverter(final Converter<B2BUnitModel, B2BUnitData> b2bUnitConverter)
    {
        this.b2bUnitConverter = b2bUnitConverter;
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

    /**
     * @return the cartService
     */
    @Override
    public CartService getCartService()
    {
        return cartService;
    }

    /**
     * @param cartService
     *            the cartService to set
     */
    @Override
    public void setCartService(final CartService cartService)
    {
        this.cartService = cartService;
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

    @Override
    public void forgottenPassword(final String uid)
    {
        Assert.hasText(uid, "The field [uid] cannot be empty");
        final CustomerModel customerModel = getUserService().getUserForUID(uid.toLowerCase(), CustomerModel.class);
        getCustomerAccountService().forgottenPassword(customerModel);
        getEventService().publishEvent(
                initializeEvent(new GreenleeForgottenPwdEvent(Boolean.FALSE, customerModel.getToken()), customerModel));
    }

    protected AbstractCommerceUserEvent initializeEvent(final AbstractCommerceUserEvent event, final CustomerModel customerModel)
    {
        event.setBaseStore(getBaseStoreService().getCurrentBaseStore());
        event.setSite(getBaseSiteService().getCurrentBaseSite());
        event.setCustomer(customerModel);
        event.setLanguage(getCommonI18NService().getCurrentLanguage());
        event.setCurrency(getCommonI18NService().getCurrentCurrency());

        return event;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.greenlee.facades.customer.GreenleeCustomerFacade#updateUserDetailsToSaleforceByPostCall(de.hybris.platform.core
     * .model.user.CustomerModel, boolean, boolean)
     */
    @Override
    public void updateUserDetailsToSaleforceByPostCall(final B2BCustomerModel customer)
    {
        try
        {
            getSalesforceService().postUserToSaleforce((GreenleeB2BCustomerModel) customer);
            LOG.error("Salesforce real time customer post successful.");
        }
        catch (final Exception error)
        {
            LOG.error("ERR_NTFY_SUPPORT_00011 - Salesforce update error, From the updateUserDetailsToSaleforceByPostCall on Password Reset failure for customer "
                    + customer.getUid());
            LOG.error(error);
            LOG.error("Salesforce updateUserDetailsToSaleforceByPostCall error, While Password Reset", error);
        }

    }


}

/**
 *
 */
package com.greenlee.facades.product.impl;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import com.greenlee.core.event.ShareProductEvent;
import com.greenlee.core.model.GreenleeProductModel;
import com.greenlee.facades.product.ShareProductFacade;


/**
 * Default implementation for {@link ShareProductFacade}
 */
public class DefaultShareProductFacade implements ShareProductFacade
{
    private EventService      eventService;
    private CommonI18NService commonI18NService;
    private BaseStoreService  baseStoreService;
    private BaseSiteService   baseSiteService;
    private UserService       userService;
    private ModelService      modelService;


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


    /*
     * (non-Javadoc)
     *
     * @see com.greenlee.facades.product.ShareProductFacade#shareProduct(de.hybris.platform.commercefacades.product.data.
     * ProductData, java.lang.String)
     */
    @Override
    public void shareProduct(final GreenleeProductModel greenleeProductModel, final String toAddress, final String fromAddress,
            final String message)
    {
        getEventService()
                .publishEvent(initializeEvent(new ShareProductEvent(greenleeProductModel, toAddress, fromAddress, message)));
    }

    protected AbstractCommerceUserEvent initializeEvent(final AbstractCommerceUserEvent event)
    {
        event.setBaseStore(getBaseStoreService().getCurrentBaseStore());
        event.setSite(getBaseSiteService().getCurrentBaseSite());
        event.setLanguage(getCommonI18NService().getCurrentLanguage());
        event.setCurrency(getCommonI18NService().getCurrentCurrency());
        return event;
    }
}

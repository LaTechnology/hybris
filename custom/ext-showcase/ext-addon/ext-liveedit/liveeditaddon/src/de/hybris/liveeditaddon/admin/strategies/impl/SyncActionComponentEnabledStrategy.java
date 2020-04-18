/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.liveeditaddon.admin.strategies.impl;

import de.hybris.liveeditaddon.admin.ComponentActionMenuRequestData;
import de.hybris.liveeditaddon.admin.facades.SynchronisationStatus;
import de.hybris.liveeditaddon.admin.strategies.ComponentAdminActionEnabledStrategy;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.sync.SynchronizationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * 
 */
public class SyncActionComponentEnabledStrategy implements ComponentAdminActionEnabledStrategy
{


	private CMSComponentService cmsComponentService;
    private CMSPageService cmsPageService;
    private UserService userService;
    private SessionService sessionService;
    private SynchronizationService synchronizationService;
    private TypeService typeService;

    /*
     * (non-Javadoc)
     *
     * @see
     * de.hybris.liveeditaddon.admin.strategies.ComponentAdminActionEnabledStrategy#isEnabled(de.hybris.liveeditaddon
     * .admin.ComponentActionMenuRequestData)
     */
    @Override
    public boolean isEnabled(final ComponentActionMenuRequestData request)
    {
        try
        {
            if (StringUtils.isBlank(request.getComponentUid()))
            {
                return false;
            }

            return !isComponentSynchronized(request.getComponentUid());

        }
        catch (final CMSItemNotFoundException e)
        {
            throw new IllegalStateException("slot is missing for uid [" + request.getSlotUid() + "]");
        }
    }

    public SynchronisationStatus getComponentSynchronizationStatus(final String componentUid) throws CMSItemNotFoundException {

        final Object result = sessionService.executeInLocalView(new SessionExecutionBody() {

            @Override
            public Object execute() {

                try {
                    final AbstractCMSComponentModel componentModel = cmsComponentService.getAbstractCMSComponent(componentUid);
                    TypedObject component = typeService.wrapItem(componentModel);
                    userService.setCurrentUser(userService.getAdminUser());//synchronizationService.isObjectSynchronized(slot);
                    return SynchronisationStatus.getStatus(synchronizationService.getSyncContext(component).isProductSynchronized());
                } catch (CMSItemNotFoundException e) {
                    return e;
                }

            }
        });
        if (result instanceof CMSItemNotFoundException) {
            throw (CMSItemNotFoundException) result;
        }

        return (SynchronisationStatus) result;

    }


    public boolean isComponentSynchronized(final String componentUid) throws CMSItemNotFoundException {
        return getComponentSynchronizationStatus(componentUid) == SynchronisationStatus.SYNCHRONIZATION_OK;
    }

    @Required
    public void setCmsComponentService(CMSComponentService cmsComponentService) {
        this.cmsComponentService = cmsComponentService;
    }

    @Required
    public void setUserService(final UserService userService)
    {
        this.userService = userService;
    }


    @Required
    public void setSessionService(final SessionService sessionService)
    {
        this.sessionService = sessionService;
    }

    @Required
    public void setSynchronizationService(SynchronizationService synchronizationService) {
        this.synchronizationService = synchronizationService;
    }

    @Required
    public void setTypeService(TypeService typeService) {
        this.typeService = typeService;
    }

    @Required
    public void setCmsPageService(CMSPageService cmsPageService) {
        this.cmsPageService = cmsPageService;
    }

    /*
                 * (non-Javadoc)
                 *
                 * @see
                 * de.hybris.liveeditaddon.admin.strategies.ComponentAdminActionEnabledStrategy#isVisible(de.hybris.liveeditaddon
                 * .admin.ComponentActionMenuRequestData, boolean)
                 */
    @Override
    public boolean isVisible(final ComponentActionMenuRequestData request, final boolean enabled)
    {
        try {
            AbstractPageModel pageModel = cmsPageService.getPageForId(request.getPageUid());
            boolean isStagedCatalog = !pageModel.getCatalogVersion().getActive();
            return isStagedCatalog;
        } catch (CMSItemNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

}

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
package de.hybris.liveeditaddon.admin.facades.impl;

import de.hybris.liveeditaddon.admin.facades.AbstractCMSAdminFacade;
import de.hybris.liveeditaddon.admin.facades.CMSSyncPageFacade;
import de.hybris.liveeditaddon.admin.facades.SyncResponse;
import de.hybris.liveeditaddon.admin.facades.SynchronisationStatus;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.preview.CMSPreviewTicketModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.sync.SynchronizationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.user.UserService;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;

import static de.hybris.liveeditaddon.admin.facades.SyncResponse.buildInfo;
import static de.hybris.liveeditaddon.admin.facades.SyncResponse.buildWarning;

public class DefaultCMSSyncPageFacade extends AbstractCMSAdminFacade implements CMSSyncPageFacade {

    private SynchronizationService synchronizationService;
    private TypeService typeService;
    private UserService userService;
    private CMSPageService cmsPageService;

    public SynchronisationStatus getPageSynchronizationStatus(final String previewTicket, final String pageUid) throws PreviewTicketInvalidException, CMSItemNotFoundException {

        final Object result = getSessionService().executeInLocalView(new SessionExecutionBody() {
            @Override
            public Object execute() {

                try {
                    final CMSPreviewTicketModel ticket = getCmsPreviewService().getPreviewTicket(previewTicket);
                    if (ticket == null) {
                        return new PreviewTicketInvalidException(previewTicket + " is not a valid preview ticket.");
                    }
                    loadCatalogVersions(ticket.getPreviewData().getCatalogVersions());
                    AbstractPageModel pageModel = cmsPageService.getPageForId(pageUid);
                    TypedObject page = typeService.wrapItem(pageModel);
                    userService.setCurrentUser(userService.getAdminUser());
                    return SynchronisationStatus.getStatus(synchronizationService.getSyncContext(page).isProductSynchronized());
                } catch (CMSItemNotFoundException e) {
                    return e;
                }

            }
        });
        if (result instanceof PreviewTicketInvalidException) {
            throw (PreviewTicketInvalidException) result;
        }
        if (result instanceof CMSItemNotFoundException) {
            throw (CMSItemNotFoundException) result;
        }

        return (SynchronisationStatus) result;

    }


    @Override
    public boolean isPageSynchronized(final String previewTicket, final String pageUid) throws PreviewTicketInvalidException, CMSItemNotFoundException {
        return getPageSynchronizationStatus(previewTicket, pageUid) == SynchronisationStatus.SYNCHRONIZATION_OK;
    }

    @Override
    public SyncResponse synchronizePage(final String previewTicket, final String pageUid) throws PreviewTicketInvalidException, CMSItemNotFoundException {
        SynchronisationStatus syncStatus = getPageSynchronizationStatus(previewTicket, pageUid);
        if (syncStatus == SynchronisationStatus.INITIAL_SYNC_IS_NEEDED || syncStatus == SynchronisationStatus.SYNCHRONIZATION_NOT_OK) {

            final Object result = getSessionService().executeInLocalView(new SessionExecutionBody() {
                @Override
                public Object execute() {
                    try {
                        final CMSPreviewTicketModel ticket = getCmsPreviewService().getPreviewTicket(previewTicket);
                        if (ticket == null) {
                            return new PreviewTicketInvalidException(previewTicket + " is not a valid preview ticket.");
                        }
                        loadCatalogVersions(ticket.getPreviewData().getCatalogVersions());
                        AbstractPageModel pageModel = cmsPageService.getPageForId(pageUid);
                        userService.setCurrentUser(userService.getAdminUser());
                        synchronizationService.performSynchronization(Collections.singletonList(pageModel), null, null, null);
                        final SynchronisationStatus syncStatusAfterSync = getPageSynchronizationStatus(previewTicket, pageUid);

                        if (syncStatusAfterSync == SynchronisationStatus.SYNCHRONIZATION_OK) {
                            return buildInfo(syncStatusAfterSync, "dialog.synchronized.message", "dialog.synchronized.message");
                        } else {
                            return buildWarning(syncStatusAfterSync, "dialog.itemNotSynchronized.message", "dialog.synchronizationNotPerformed.title");
                        }
                    } catch (PreviewTicketInvalidException | CMSItemNotFoundException e) {
                        return e;
                    }
                }
            });
            if (result instanceof PreviewTicketInvalidException) {
                throw (PreviewTicketInvalidException) result;
            }
            if (result instanceof CMSItemNotFoundException) {
                throw (CMSItemNotFoundException) result;
            }

            return (SyncResponse) result;

        } else if (syncStatus == SynchronisationStatus.SYNCHRONIZATION_NOT_AVAILABLE) {
            return buildWarning(syncStatus, "dialog.synchronizationNotPossible.message", "dialog.synchronizationNotPerformed.title");

        } else if (syncStatus == SynchronisationStatus.SYNCHRONIZATION_OK) {
            return buildWarning(syncStatus, "dialog.synchronizationNotRequired.message", "dialog.synchronizationNotPerformed.title");
        } else {
            throw new IllegalStateException("unexpected sync status [" + syncStatus + "]");
        }
    }


    @Required
    public void setCockpitTypeService(final TypeService typeService) {
        this.typeService = typeService;
    }

    @Required
    public void setSynchronizationService(SynchronizationService synchronizationService) {
        this.synchronizationService = synchronizationService;
    }

    @Required
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Required
    public void setCmsPageService(CMSPageService cmsPageService) {
        this.cmsPageService = cmsPageService;
    }

    @Override
    protected Converter getAdminMenuGroupConverter() {
        return null;
    }

}

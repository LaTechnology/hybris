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
package de.hybris.liveeditaddon.cockpit.service.impl;

import de.hybris.liveeditaddon.cockpit.service.CMSLockingService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.servicelayer.services.CMSContentSlotService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Messagebox;

/**
 * Created by fcanteloup on 25/09/14.
 */
public class DefaultCMSLockingService implements CMSLockingService {


    private ModelService modelService;

    private CatalogVersionService catalogVersionService;

    private SessionService sessionService;

    private CMSContentSlotService cmsContentSlotService;

    private CMSAdminContentSlotService cmsAdminContentSlotService;

    @Autowired
    public DefaultCMSLockingService(@Qualifier("modelService") ModelService modelService,
                                    @Qualifier("catalogVersionService") CatalogVersionService catalogVersionService,
                                    @Qualifier("sessionService") SessionService sessionService,
                                    @Qualifier("cmsContentSlotService") CMSContentSlotService cmsContentSlotService,
                                    @Qualifier("cmsAdminContentSlotService") CMSAdminContentSlotService cmsAdminContentSlotService) {
        this.modelService = modelService;
        this.catalogVersionService = catalogVersionService;
        this.sessionService = sessionService;
        this.cmsContentSlotService = cmsContentSlotService;
        this.cmsAdminContentSlotService = cmsAdminContentSlotService;
    }

    @Override
    public boolean isSectionLocked(final LiveEditView view, String slotId) throws InterruptedException {
        boolean isLocked = false;

        if (StringUtils.isNotEmpty(slotId)) {//empty for an empty section

            final ContentSlotModel contentSlot = getContentSlotForPreviewCatalogVersions(slotId, view);

            if (isLockable(contentSlot)){//not the case of a section even though we have created a persistent lockedState for all sectionSlots
                isLocked = contentSlot.getLocked();
                if (isLocked) {
                    final int choice = showMessageBox(contentSlot);
                    if (choice == Messagebox.OK) {
                        isLocked = false;
                        contentSlot.setLocked(false);
                        modelService.save(contentSlot);
                    }
                    view.update();
                }
            }

        }
        return isLocked;
    }


    protected ContentSlotModel getContentSlotForPreviewCatalogVersions(final String slotUid, final LiveEditView view) {
        return ((ContentSlotModel) sessionService.executeInLocalView(new SessionExecutionBody() {
            @Override
            public Object execute() {
                catalogVersionService.setSessionCatalogVersions(view.getModel().getCurrentPreviewData().getCatalogVersions());
                final ContentSlotModel contentSlot = cmsContentSlotService.getContentSlotForId(slotUid);
                return contentSlot;
            }
        }));
    }

    private boolean isLockable(final ContentSlotModel contentSlot)
    {
        return !cmsAdminContentSlotService.hasRelations(contentSlot);
    }

    protected int showMessageBox(ContentSlotModel contentSlot) throws InterruptedException {
        return Messagebox.show(Labels.getLabel("namedsectionlock.unlock.msg", new Object[]{contentSlot.getCurrentPosition()}),
                Labels.getLabel("sectionlock.unlock.title"), Messagebox.OK | Messagebox.CANCEL, Messagebox.EXCLAMATION);
    }

}

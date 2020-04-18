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
package de.hybris.liveeditaddon.cockpit.callbackevent;

import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.liveeditaddon.constants.LiveeditaddonConstants;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.cmscockpit.session.impl.CmsListBrowserSectionModel;
import de.hybris.platform.cmscockpit.session.impl.CmsPageBrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import org.zkoss.zkex.zul.LayoutRegion;

import javax.annotation.Resource;
import java.util.Map;


/**
 */
public class LockContentSlotCallbackEventHandler extends AbstractLiveEditCallbackEventHandler<LiveEditView>
{

	@Override
	public String getEventId()
	{
		return "lockSlot";
	}


    @Override
    public void onCallbackEvent(LiveEditView view, Map<String, Object> attributeMap) throws Exception
	{

		final String pageUid = (String)attributeMap.get(LiveeditaddonConstants.PAGE_ID);
		final String position = (String)attributeMap.get("position");
        final String slotId = (String)attributeMap.get(LiveeditaddonConstants.SLOT_ID);

		final CmsPageBrowserModel pageModel = getPageBrowserModel(view, pageUid);
		final CmsListBrowserSectionModel sectionModel = (CmsListBrowserSectionModel) getBrowserSectionForPosition(position,
				pageModel);

		if (sectionModel.isLockable())
		{
            final ContentSlotModel contentSlot = getContentSlotForPreviewCatalogVersions(slotId, view);

            contentSlot.setLocked(!contentSlot.getLocked());
            getModelService().save(contentSlot);
		}
        //editor may be open on a component the slot of which is now locked
        BaseUICockpitPerspective perspective = ((BaseUICockpitPerspective) UISessionUtils.getCurrentSession().getCurrentPerspective());
        perspective.getEditorArea().setCurrentObjectType(null);
        perspective.getEditorArea().setCurrentObject(null);
        perspective.setActiveItem(null);

        view.update();

	}







}

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

import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.cmscockpit.session.impl.CmsPageBrowserModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UISessionUtils;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Messagebox;

import java.util.Map;


/**
 */
public class RemoveContentSlotCallbackEventHandler extends AbstractLiveEditCallbackEventHandler<LiveEditView>
{
	@Override
	public String getEventId()
	{
		return "removeSlot";
	}

    @Override
    public void onCallbackEvent(final LiveEditView view, Map<String, Object> attributeMap) throws Exception
    {
		final String pageUid = (String)attributeMap.get("page_id");
		final String slotPosition = (String)attributeMap.get("position");
		final String slotUid = (String)attributeMap.get("slot_id");

		final String currentMessage = Labels.getLabel("prompt.confirm_remove_content_slot", new String[]
		{ slotPosition });

		if (Messagebox.show(currentMessage, Labels.getLabel("dialog.confirmRemove.title"), Messagebox.YES | Messagebox.NO,
				Messagebox.QUESTION) == Messagebox.YES)
		{

			final AbstractPageModel page = getPageForPreviewCatalogVersions(pageUid, view);
            final CmsPageBrowserModel pageModel = getApplicationContext().getBean("cmsPageBrowserModel", CmsPageBrowserModel.class);
			final ContentSlotModel contentSlot = getContentSlotForPreviewCatalogVersions(slotUid, view);
			final TypedObject pageObject = getCockpitTypeService().wrapItem(page);

			final UIBrowserArea area = UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea();

			pageModel.setCurrentPageObject(pageObject);
			pageModel.setArea(area);
			pageModel.initialize();

			if (contentSlot != null)
			{
				pageModel.deleteSlotContentForCurrentPage(contentSlot.getUid());
				pageModel.updateItems();
				view.update();
			}
			else
			{
				Messagebox.show(Labels.getLabel("dialog.confirmRemove.error.message"),
						Labels.getLabel("dialog.confirmRemove.error.title"), Messagebox.CANCEL, Messagebox.ERROR);
			}

		}

	}

}

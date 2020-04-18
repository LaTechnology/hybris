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

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.cmscockpit.session.impl.CmsPageBrowserModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Messagebox;

import java.util.Map;


/**
 * 
 */
public class CreateContentSlotCallbackEventHandler extends AbstractLiveEditCallbackEventHandler<LiveEditView>
{
	@Override
	public String getEventId()
	{
		return "createSlot";
	}

    @Override
    public void onCallbackEvent(LiveEditView view, Map<String, Object> attributeMap) throws Exception
	{

		final String pageUid = (String) attributeMap.get("page_id");
		final String slotPosition = (String) attributeMap.get("position");
		final String actionType = (String) attributeMap.get("action");

		String messageTitle = Labels.getLabel("dialog.confirmSlotCreate.title");
		String messageContent = Labels.getLabel("dialog.confirmSlotCreate.message");

		if (actionType.equals("OVERRIDE"))
		{
			messageTitle = Labels.getLabel("dialog.confirmOverride.title");
			messageContent = Labels.getLabel("prompt.confirm_override_content_slot", new String[]
			{ slotPosition });
		}

		try
		{
			if (Messagebox.show(messageContent, messageTitle, Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES)
			{

				final AbstractPageModel page = getPageForPreviewCatalogVersions(pageUid, view);
                final CmsPageBrowserModel pageModel = getApplicationContext().getBean("cmsPageBrowserModel", CmsPageBrowserModel.class);
                final TypedObject pageObject = getCockpitTypeService().wrapItem(page);

				final UIBrowserArea area = UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea();

				pageModel.setCurrentPageObject(pageObject);
				pageModel.setArea(area);
				pageModel.initialize();

				pageModel.createContentSlotForPage(slotPosition);
				pageModel.updateItems();
				view.update();
			}
		}
		catch (final ModelSavingException e)
		{
			throw new IllegalStateException("Could not create new content slot for position [" + slotPosition + "]", e);
		}
		catch (final InterruptedException e)
		{
			throw new IllegalStateException("Could not create new content slot for position [" + slotPosition + "]", e);
		}

	}

}

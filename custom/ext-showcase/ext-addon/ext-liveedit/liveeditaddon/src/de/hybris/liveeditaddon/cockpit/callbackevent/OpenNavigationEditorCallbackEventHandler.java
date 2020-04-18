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

import de.hybris.platform.cms2.jalo.contents.contentslot.ContentSlot;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.liveeditaddon.cockpit.callbackevent.AbstractLiveEditCallbackEventHandler;

import de.hybris.liveeditaddon.cockpit.navigationeditor.wizard.NavigationEditorWizard;
import de.hybris.liveeditaddon.cockpit.services.NavigationEditorService;

import java.util.Map;


public class OpenNavigationEditorCallbackEventHandler extends AbstractLockingLiveEditCallbackEventHandler<LiveEditView>
{

	private NavigationEditorService navigationEditorService;

	@Override
	public String getEventId()
	{
		return "openNavigationEditor";
	}

	@Override
	public void onCallbackEventInternal(final LiveEditView view, final Map<String, Object> attributeMap) throws Exception
	{
		//server path is always last, SHOW-1290
		final String serverPath = (String)attributeMap.get("serverPath");
		final String componentUID = (String)attributeMap.get("cmp_id");//can be either NavigationBarComponent or NavigationBarCollectionComponent
		final String contentSlotUID = (String)attributeMap.get("slot_id");
		new NavigationEditorWizard(view.getModel(), serverPath, componentUID, contentSlotUID, getNavigationEditorService())
				.show(view);
	}

	public NavigationEditorService getNavigationEditorService()
	{
		return navigationEditorService;
	}

	public void setNavigationEditorService(final NavigationEditorService navigationEditorService)
	{
		this.navigationEditorService = navigationEditorService;
	}
}

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
package de.hybris.liveeditaddon.cockpit.navigationeditor.model;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditViewModel;

import de.hybris.liveeditaddon.cockpit.services.NavigationEditorService;



/**
 * 
 */
public class NavigationEditorViewModel
{
	private String serverPath = "";
	private String componentUID = "";
	private String contentSlotUID = "";
	private LiveEditViewModel liveEditViewModel;

	public void initModel(final LiveEditViewModel liveEditViewModel, final String serverPath, final String componentUID,
			final String contentSlotUID)
	{
		this.serverPath = serverPath;
		this.componentUID = componentUID;
		this.contentSlotUID = contentSlotUID;
		this.liveEditViewModel = liveEditViewModel;
	}

	public String getServerPath()
	{
		return serverPath;
	}

	public String getComponentUID()
	{
		return componentUID;
	}

	public LiveEditViewModel getLiveEditViewModel()
	{
		return liveEditViewModel;
	}

	public String getContentSlotUID()
	{
		return contentSlotUID;
	}
}

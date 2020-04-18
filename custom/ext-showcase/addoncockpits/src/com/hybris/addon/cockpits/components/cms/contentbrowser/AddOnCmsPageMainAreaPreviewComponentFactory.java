/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 * 
 */
package com.hybris.addon.cockpits.components.cms.contentbrowser;

import de.hybris.platform.cmscockpit.components.liveedit.impl.DefaultLiveEditViewModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.yacceleratorcockpits.cmscockpit.components.contentbrowser.DefaultCmsPageMainAreaPreviewComponentFactory;
import de.hybris.platform.yacceleratorcockpits.components.liveedit.DefaultLiveEditView;

import com.hybris.addon.cockpits.components.liveedit.AddOnLiveEditView;


/**
 * @author rmcotton
 * 
 */
public class AddOnCmsPageMainAreaPreviewComponentFactory extends DefaultCmsPageMainAreaPreviewComponentFactory
{

	/**
	 * @param wrappedCurrentPageModel
	 */
	public AddOnCmsPageMainAreaPreviewComponentFactory(final TypedObject wrappedCurrentPageModel)
	{
		super(wrappedCurrentPageModel);
	}


	/**
	 * Hook for custom DefaultLiveEditView
	 */
	@Override
	protected DefaultLiveEditView newDefaultLiveEditView(final DefaultLiveEditViewModel liveEditViewModel)
	{
		return new AddOnLiveEditView(liveEditViewModel);
	}
}

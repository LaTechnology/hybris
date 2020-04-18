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
package de.hybris.liveeditaddon.cockpit.wizards.mediacontainermanagement.service.impl;


import de.hybris.platform.acceleratorcms.model.components.AbstractMediaContainerComponentModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.liveeditaddon.cockpit.wizards.productimagemanagement.events.ProductImageModelListener;

import java.util.ArrayList;
import java.util.Map;

/**
 */
public class DefaultManageMediaService extends AbstractManageMediaService
{
	public DefaultManageMediaService(final String serverPath, final String siteUid, final CatalogVersionModel catalogVersion,
			final MediaContainerModel mediaContainerModel, final AbstractMediaContainerComponentModel component,
			final Map<String, String> mediaFormatMap)
	{
		this.serverPath = serverPath;
		this.siteUid = siteUid;
		this.catalogVersion = catalogVersion;
		this.mediaContainerModel = mediaContainerModel;
		this.component = component;
		this.mediaFormatMap = mediaFormatMap;

		if (mediaContainerModel != null)
		{
			convertMediasToMap(mediaContainerModel.getMedias(), mediaModelMap);
		}
		listeners = new ArrayList<ProductImageModelListener>();
	}

	@Override
	public void save()
	{
		if (mediaContainerModel == null)
		{
			mediaContainerModel = getModelService().create(MediaContainerModel._TYPECODE);
			mediaContainerModel.setQualifier("base_" + System.nanoTime() + "_MediaContainer");
			mediaContainerModel.setCatalogVersion(catalogVersion);
			((AbstractMediaContainerComponentModel) component).setMedia(mediaContainerModel);
		}
		mediaContainerModel.setMedias(mediaModelMap.values());

		getModelService().save(mediaContainerModel);
		getModelService().save(component);
		getModelService().saveAll(mediaModelMap.values());
	}



}

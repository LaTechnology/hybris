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

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.liveeditaddon.cockpit.service.CockpitImageMediaService;
import de.hybris.liveeditaddon.cockpit.wizards.components.ImageTile;
import de.hybris.liveeditaddon.cockpit.wizards.mediacontainermanagement.service.ManageMediaService;
import de.hybris.liveeditaddon.cockpit.wizards.productimagemanagement.ImageResizePlugin;
import de.hybris.liveeditaddon.cockpit.wizards.productimagemanagement.events.ProductImageModelListener;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Iterables;


/**
 * 
 */
public abstract class AbstractManageMediaService implements ManageMediaService, CockpitImageMediaService
{

	protected final static String CODE_PREFIX = "Media";
	protected final static String CODE_TEMP_PREFIX = "_TempContainer";
	protected String serverPath;

	protected final String THUMBNAIL_FORMAT = "96Wx96H";

	protected ModelService modelService;
	protected MediaService mediaService;
	protected ImageResizePlugin imageResizePlugin;

	protected CatalogVersionModel catalogVersion;
	protected String siteUid;
	protected AbstractCMSComponentModel component;
	protected MediaContainerModel mediaContainerModel;
	protected final Map<String, MediaModel> mediaModelMap = Collections.synchronizedMap(new HashMap<String, MediaModel>());
	protected List<ProductImageModelListener> listeners;
	protected Map<String, String> mediaFormatMap;

	protected void convertMediasToMap(final Collection<MediaModel> medias, final Map<String, MediaModel> map)
	{
		if (medias.size() > 0)
		{
			final Iterator<MediaModel> iterator = medias.iterator();
			while (iterator.hasNext())
			{
				final MediaModel media = iterator.next();
				final MediaFormatModel formatModel = media.getMediaFormat();
				map.put(formatModel.getQualifier(), media);
			}
		}
	}

	@Override
	public void setImageWithFormat(final byte[] bytes, final ImageTile tile)
	{
		final String format = tile.getFormat();
		final MediaModel uploadedMediaModel = getModelService().create("Media");
		uploadedMediaModel.setCode(format + "_" + System.nanoTime() + "_" + CODE_PREFIX);
		uploadedMediaModel.setCatalogVersion(catalogVersion);
		uploadedMediaModel.setMediaFormat(getMediaService().getFormat(format));

		getModelService().save(uploadedMediaModel);
		getMediaService().setDataForMedia(uploadedMediaModel, bytes);
		getModelService().save(uploadedMediaModel);

		mediaModelMap.put(format, uploadedMediaModel);
	}

	@Override
	public void setImageWithAutoResize(final byte[] bytes, final ImageTile tile)
	{
		final MediaModel uploadedMediaModel = getModelService().create("Media");
		uploadedMediaModel.setCode("base_" + System.nanoTime() + "_" + CODE_PREFIX);
		uploadedMediaModel.setCatalogVersion(catalogVersion);

		getModelService().save(uploadedMediaModel);
		getMediaService().setDataForMedia(uploadedMediaModel, bytes);
		getModelService().save(uploadedMediaModel);

		final MediaContainerModel tempContainer = getModelService().create("MediaContainer");
		tempContainer.setMedias(Collections.singletonList(uploadedMediaModel));
		tempContainer.setCatalogVersion(catalogVersion);
		tempContainer.setQualifier(uploadedMediaModel.getCode() + CODE_TEMP_PREFIX);
		getModelService().save(tempContainer);

		final MediaContainerModel convertedMediaContainerModel = getImageResizePlugin().convertMediaForSite(tempContainer, siteUid);
		convertMediasToMap(convertedMediaContainerModel.getMedias(), mediaModelMap);
	}

	@Override
	public abstract void save();

	public void setServerPath(final String serverPath)
	{
		this.serverPath = serverPath;
	}

	@Override
	public String getServerPath()
	{
		return serverPath;
	}

	@Override
	public String getMediaPathForFormat(final String format)
	{
		final MediaModel mediaModel = mediaModelMap.get(format);
		if (mediaModel != null)
		{
			return serverPath + mediaModel.getURL();
		}
		return "";
	}

	@Override
	public MediaModel getMasterMedia(final Collection<MediaModel> medias)
	{
		final MediaFormatModel thumnailFormat = getMediaService().getFormat(THUMBNAIL_FORMAT);

		for (final MediaModel media : medias)
		{
			if (media.getMediaFormat().equals(thumnailFormat))
			{
				return media;
			}
		}

		if (medias.size() > 0)
		{
			return Iterables.get(medias, 0);
		}

		return null;
	}

	@Override
	public boolean isReadyForSave()
	{
		return mediaModelMap.size() > 0;
	}

	@Override
	public void removeMediaByFormat(final String format)
	{
		mediaModelMap.remove(format);
	}

	public MediaService getMediaService()
	{
		if (mediaService == null)
		{
			mediaService = (MediaService) Registry.getApplicationContext().getBean("mediaService");
		}
		return mediaService;
	}

	public ModelService getModelService()
	{
		if (modelService == null)
		{
			modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		}
		return modelService;
	}

	public ImageResizePlugin getImageResizePlugin()
	{
		if (imageResizePlugin == null)
		{
			imageResizePlugin = (ImageResizePlugin) Registry.getApplicationContext().getBean("liveEditImageResizePlugin");
		}
		return imageResizePlugin;
	}

	@Override
	public void setImagesFromZip(final List<byte[]> zipEntryBytes, final ImageTile container)
	{
		//
	}

	@Override
	public boolean isResizePluginIncluded()
	{
		return Registry.getApplicationContext().containsBean("liveEditImageResizePlugin");
	}

	private void fireChangeEvents()
	{
		for (final ProductImageModelListener listener : listeners)
		{
			listener.onChange();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.liveeditaddon.cockpit.wizards.mediacontainermanagement.service.ManageMediaService#addListener(de.
	 * hybris.liveeditaddon.cockpit.wizards.productimagemanagement.events.ProductImageModelListener)
	 */
	@Override
	public void addListener(final ProductImageModelListener listener)
	{
		listeners.add(listener);
	}

	/**
	 * @return the component
	 */
	public AbstractCMSComponentModel getComponent()
	{
		return component;
	}

	/**
	 * @param component
	 *           the component to set
	 */
	public void setComponent(final AbstractCMSComponentModel component)
	{
		this.component = component;
	}

	public Map<String, String> getMediaFormatMap()
	{
		return mediaFormatMap;
	}

	public MediaContainerModel getMediaContainerModel()
	{
		return mediaContainerModel;
	}
}

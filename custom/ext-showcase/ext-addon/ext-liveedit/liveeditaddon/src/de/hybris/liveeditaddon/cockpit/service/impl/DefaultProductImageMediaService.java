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

import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.sync.SynchronizationService;
import de.hybris.platform.cockpit.services.sync.impl.SynchronizationServiceImpl;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.liveeditaddon.cockpit.service.ProductImageMediaService;
import de.hybris.liveeditaddon.cockpit.wizards.productimagemanagement.ImageResizePlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * 
 */
public class DefaultProductImageMediaService implements ProductImageMediaService
{
	ModelService modelService;
	MediaService mediaService;
	ImageResizePlugin imageResizePlugin;
	SynchronizationServiceImpl synchronizationService;

	@Override
	public MediaModel updateMediaModels(final byte[] bytes, final ProductModel currentProduct)
	{
		final MediaModel uploadedMediaModel = getModelService().create("Media");
		uploadedMediaModel.setCode("base_" + System.nanoTime() + "_" + currentProduct.getCode());
		uploadedMediaModel.setCatalogVersion(currentProduct.getCatalogVersion());
		uploadedMediaModel.setFolder(getMediaService().getFolder("images"));
		getModelService().save(uploadedMediaModel);
		getMediaService().setDataForMedia(uploadedMediaModel, bytes);
		getModelService().save(uploadedMediaModel);
		return uploadedMediaModel;
	}

	@Override
	public MediaContainerModel updateMediaContainerModels(final MediaModel uploadedMediaModel, final ProductModel currentProduct,
			final String siteUid, final List<MediaContainerModel> tempContainers)
	{
		final MediaContainerModel tempContainer = getModelService().create("MediaContainer");
		tempContainer.setMedias(Collections.singletonList(uploadedMediaModel));
		tempContainer.setCatalogVersion(currentProduct.getCatalogVersion());
		tempContainer.setQualifier(uploadedMediaModel.getCode() + "_TempContainer");
		getModelService().save(tempContainer);
		final MediaContainerModel newContainer = getImageResizePlugin().convertMediaForSite(tempContainer, siteUid);
		getModelService().save(newContainer);
		if (tempContainers != null)
		{
			tempContainers.add(tempContainer);
		}

		return newContainer;
	}

	@Override
	public void attachContainerToProductModel(final Collection<MediaContainerModel> container, final ProductModel currentProduct)
	{
		final List<MediaContainerModel> containers = new ArrayList<MediaContainerModel>(currentProduct.getGalleryImages());
		containers.addAll(container);
		currentProduct.setGalleryImages(containers);
		getModelService().save(currentProduct);
	}

	@Override
	public void switchProductImageContainer(final MediaContainerModel toRemove, final MediaContainerModel toAdd,
			final ProductModel currentProduct)
	{
		final List<MediaContainerModel> containers = new ArrayList<MediaContainerModel>(currentProduct.getGalleryImages());
		containers.add(containers.indexOf(toRemove), toAdd);
		containers.remove(toRemove);
		currentProduct.setGalleryImages(containers);
	}


	@Override
	public MediaModel setImageWithFormat(final byte[] bytes, final ProductModel currentProduct, final String format,
			MediaContainerModel tempContainer)
	{
		final MediaModel uploadedMediaModel = getModelService().create("Media");
		uploadedMediaModel.setCode(format + "_" + System.nanoTime() + "_" + currentProduct.getCode());
		uploadedMediaModel.setCatalogVersion(currentProduct.getCatalogVersion());
		uploadedMediaModel.setMediaFormat(getMediaService().getFormat(format));
		uploadedMediaModel.setFolder(getMediaService().getFolder("images"));
		getModelService().save(uploadedMediaModel);
		getMediaService().setDataForMedia(uploadedMediaModel, bytes);
		getModelService().save(uploadedMediaModel);


		if (tempContainer == null)
		{
			tempContainer = getModelService().create("MediaContainer");
			tempContainer.setMedias(Collections.singletonList(uploadedMediaModel));
			tempContainer.setCatalogVersion(currentProduct.getCatalogVersion());
			tempContainer.setQualifier(uploadedMediaModel.getCode() + "_TempContainer");
		}
		else
		{
			final List<MediaModel> models = new ArrayList<MediaModel>(tempContainer.getMedias());
			if (getMediaForFormat(tempContainer, format) != null)
			{
				models.remove(getMediaForFormat(tempContainer, format));
			}
			models.add(uploadedMediaModel);

			tempContainer.setMedias(models);
		}
		getModelService().save(tempContainer);
		return uploadedMediaModel;
	}


	@Override
	public boolean isSynchronizationNeeded(final ProductModel currentProduct)
	{
		final List<TypedObject> wrapItems = new ArrayList<TypedObject>();
		wrapItems.addAll(UISessionUtils.getCurrentSession().getTypeService().wrapItems(currentProduct.getGalleryImages()));
		wrapItems.add(UISessionUtils.getCurrentSession().getTypeService().wrapItem(currentProduct));
		// we need to specifically add these medias in order for them to be synced properly.
		final List<MediaContainerModel> galleryImages = currentProduct.getGalleryImages();
		if (galleryImages != null)
		{
			for (final MediaContainerModel containerModel : galleryImages)
			{
				wrapItems.addAll(UISessionUtils.getCurrentSession().getTypeService().wrapItems(containerModel.getMedias()));
			}
		}
		for (final TypedObject to : wrapItems)
		{
			final int status = getSynchronizationService().isObjectSynchronized(to);
			if (status == SynchronizationService.SYNCHRONIZATION_NOT_OK || status == SynchronizationService.INITIAL_SYNC_IS_NEEDED)
			{
				return true;
			}
		}
		return false;
	}

	public MediaModel getMediaForFormat(final MediaContainerModel container, final String format)
	{
		for (final MediaModel media : container.getMedias())
		{
			if (media.getMediaFormat() != null && media.getMediaFormat().getQualifier().equals(format))
			{
				return media;
			}
		}
		return null;
	}

	@Override
	public MediaModel getGalleryImage(final MediaContainerModel container)
	{
		return getMediaForFormat(container, "65Wx65H");
	}

	@Override
	public MediaModel getMainImage(final MediaContainerModel container)
	{
		return getMediaForFormat(container, "300Wx300H");
	}

	private MediaModel getImageWithProperFormatIfPossible(final MediaModel model)
	{
		if (!model.getMediaFormat().getQualifier().equals("300Wx300H"))
		{
			if (getMainImage(model.getMediaContainer()) != null)
			{
				return getMainImage(model.getMediaContainer());
			}
		}
		return model;
	}

	@Override
	public void setBestMediaToMainPicture(final MediaModel model, final ProductModel currentProduct)
	{
		final MediaModel properMedia = getImageWithProperFormatIfPossible(model);
		currentProduct.setPicture(properMedia);
		getModelService().save(currentProduct);
	}

	@Override
	public void synchronize(final ProductModel currentProduct, final Collection<MediaModel> medias)
	{
		final SyncItemJobModel syncItemJobModel = currentProduct.getCatalogVersion().getSynchronizations().get(0);
		getSynchronizationService().performSynchronization(prepareItemsToSynchronize(currentProduct, medias),
				Collections.singletonList(syncItemJobModel.getPk().toString()), syncItemJobModel.getTargetVersion(), null);
	}

	private List<TypedObject> prepareItemsToSynchronize(final ProductModel currentProduct, final Collection<MediaModel> medias)
	{
		getModelService().save(currentProduct);
		getModelService().saveAll(medias);

		final List<TypedObject> wrapItems = UISessionUtils.getCurrentSession().getTypeService()
				.wrapItems(Collections.singletonList((medias)));
		wrapItems.add(UISessionUtils.getCurrentSession().getTypeService().wrapItem(currentProduct));
		// we need to specifically add these medias in order for them to be synced properly.
		final List<MediaContainerModel> galleryImages = currentProduct.getGalleryImages();
		if (galleryImages != null)
		{
			for (final MediaContainerModel containerModel : galleryImages)
			{
				wrapItems.addAll(UISessionUtils.getCurrentSession().getTypeService().wrapItems(containerModel.getMedias()));
			}
		}
		return wrapItems;
	}

	@Override
	public void removeMediaFromGalleryImages(final ProductModel currentProduct, final MediaModel media)
	{
		for (final MediaContainerModel container : currentProduct.getGalleryImages())
		{
			if (container.getMedias().contains(media))
			{
				final List<MediaModel> tempImages = new ArrayList<MediaModel>();
				tempImages.addAll(container.getMedias());
				tempImages.remove(media);//item in media collection
				container.setMedias(tempImages);
				getModelService().save(container);
			}
		}
		getModelService().save(currentProduct);
	}

	@Override
	public void removeGalleryContainer(final ProductModel currentProduct, final MediaModel media)
	{
		final List<MediaContainerModel> containers = new ArrayList<MediaContainerModel>(currentProduct.getGalleryImages());
		for (final Iterator<MediaContainerModel> it = containers.iterator(); it.hasNext();)
		{
			final MediaContainerModel model = it.next();
			if (model.getMedias().contains(media))
			{
				it.remove();
			}
		}
		currentProduct.setGalleryImages(containers);
		getModelService().save(currentProduct);
	}

	@Override
	public SynchronizationServiceImpl getSynchronizationService()
	{
		return synchronizationService;
	}

	@Override
	public void setSynchronizationService(final SynchronizationServiceImpl synchronizationService)
	{
		this.synchronizationService = synchronizationService;
	}

	@Override
	public ModelService getModelService()
	{
		return modelService;
	}

	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Override
	public MediaService getMediaService()
	{
		return mediaService;
	}

	@Override
	public void setMediaService(final MediaService mediaService)
	{
		this.mediaService = mediaService;
	}

	private ImageResizePlugin getImageResizePlugin()
	{
		if (imageResizePlugin == null && Registry.getApplicationContext().containsBean("liveEditImageResizePlugin"))
		{
			imageResizePlugin = (ImageResizePlugin) Registry.getApplicationContext().getBean("liveEditImageResizePlugin");
		}
		return imageResizePlugin;
	}

	@Override
	public boolean isResizePluginIncluded()
	{
		return getImageResizePlugin() != null;
	}



}

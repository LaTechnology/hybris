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
package de.hybris.liveeditaddon.cockpit.service;

import de.hybris.platform.cockpit.services.sync.impl.SynchronizationServiceImpl;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;
import java.util.List;



/**
 * 
 */
public interface ProductImageMediaService
{

	public MediaModel updateMediaModels(byte[] bytes, ProductModel currentProduct);

	public MediaContainerModel updateMediaContainerModels(MediaModel uploadedMediaModel, ProductModel currentProduct,
			String siteUid, List<MediaContainerModel> tempContainers);

	public void attachContainerToProductModel(Collection<MediaContainerModel> container, ProductModel currentProduct);

	public boolean isSynchronizationNeeded(ProductModel currentProduct);

	public MediaModel getGalleryImage(MediaContainerModel container);

	public MediaModel getMainImage(MediaContainerModel container);

	public void setBestMediaToMainPicture(MediaModel model, ProductModel currentProduct);

	public SynchronizationServiceImpl getSynchronizationService();

	public void setSynchronizationService(SynchronizationServiceImpl synchronizationService);

	public ModelService getModelService();

	public void setModelService(ModelService modelService);

	public MediaService getMediaService();

	public void setMediaService(MediaService mediaService);

	public boolean isResizePluginIncluded();

	public void synchronize(final ProductModel currentProduct, Collection<MediaModel> medias);

	public void removeMediaFromGalleryImages(final ProductModel currentProduct, final MediaModel media);

	public MediaModel setImageWithFormat(byte[] bytes, ProductModel currentProduct, String format,
			MediaContainerModel tempContainer);

	void switchProductImageContainer(MediaContainerModel toRemove, MediaContainerModel toAdd, ProductModel currentProduct);

	void removeGalleryContainer(ProductModel currentProduct, MediaModel media);
}
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
package de.hybris.liveeditaddon.cockpit.wizards.productimagemanagement;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.liveeditaddon.cockpit.service.CockpitImageMediaService;
import de.hybris.liveeditaddon.cockpit.service.ProductImageMediaService;
import de.hybris.liveeditaddon.cockpit.wizards.components.GalleryTile;
import de.hybris.liveeditaddon.cockpit.wizards.components.ImageTile;
import de.hybris.liveeditaddon.cockpit.wizards.components.MainTile;
import de.hybris.liveeditaddon.cockpit.wizards.productimagemanagement.events.ProductImageModelListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.Window;



/**
 * 
 */
public class DefaultProductImageManagementViewModel implements ProductImageManagementViewModel, CockpitImageMediaService
{

	private final String GALLERY_SCLASS = "productImageGallery";

	List<MediaContainerModel> tempContainer; //for manual-added pictures
	private ProductImageMediaService productImageMediaService;
	private ProductModel currentProduct;
	private final Map<GalleryTile, MediaModel> galleryTiles = new HashMap<GalleryTile, MediaModel>();
	private final Collection<ImageTile> imageTiles = new ArrayList<ImageTile>();
	private Map<String, String> mediaFormatsMap;
	private List<ProductImageModelListener> listeners;
	private MediaContainerModel selectedContainerModel;


	private Div galleryContainer;//ugly hack?
	private Div mainPictureContainer;
	private String serverPath;
	private String siteUid;
	private Component btnDone;

	private MediaContainerModel clickedGalleryImage = null;

	@Override
	public void initModel(final ProductModel previewProduct, final String serverPath, final Map<String, String> mediaFormatsMap,
			final String siteUid)
	{
		currentProduct = previewProduct;
		this.serverPath = serverPath;
		this.siteUid = siteUid;
		this.mediaFormatsMap = mediaFormatsMap;
		listeners = new ArrayList<ProductImageModelListener>();
	}

	@Override
	public void fireInitEvents()
	{
		for (final ProductImageModelListener listener : listeners)
		{
			listener.onLoad();
		}

	}

	public void fireChangeEvents()
	{
		for (final ProductImageModelListener listener : listeners)
		{
			listener.onChange();
		}

	}

	@Override
	public void registerTile(final ImageTile tile)
	{
		imageTiles.add(tile);
	}

	@Override
	public void reset()
	{
		currentProduct = null;
		tempContainer = null;
	}

	@Override
	public boolean save()
	{

		Clients.showBusy("busy.sync", true);
		final List<MediaModel> galleryTilesMedia = new ArrayList<MediaModel>(galleryTiles.values());

		// We need to specifically add these medias in order for them to be synced properly.
		final List<MediaModel> mediasToBeSaved = new ArrayList<MediaModel>();
		final List<MediaContainerModel> containersToBeSaved = new ArrayList<MediaContainerModel>();
		if (currentProduct.getGalleryImages() != null)
		{
			if (selectedContainerModel != null)
			{
				final List<MediaContainerModel> containerModels = new ArrayList<>(currentProduct.getGalleryImages());
				containerModels.add(selectedContainerModel);
				currentProduct.setGalleryImages(containerModels);
			}
			getProductImageMediaService().getModelService().save(currentProduct);
			for (final MediaContainerModel containerModel : currentProduct.getGalleryImages())
			{
				for (final MediaModel mediaModel : containerModel.getMedias())
				{
					mediasToBeSaved.add(mediaModel);
				}
				containersToBeSaved.add(containerModel);
			}
			getProductImageMediaService().getModelService().saveAll(galleryTilesMedia);
			getProductImageMediaService().getModelService().saveAll(mediasToBeSaved);
			getProductImageMediaService().getModelService().saveAll(containersToBeSaved);
			getProductImageMediaService().getModelService().save(currentProduct);
		}
		Clients.showBusy(null, false);

		refresh();
		return true;
	}

	private void refresh()
	{
		galleryTiles.clear();
		galleryContainer.getChildren().clear();
		mainPictureContainer.getChildren().clear();
		composeMainPicture(mainPictureContainer);
		composePictureGallery(galleryContainer);
		btnDone.setVisible(isSynchronizationNeeded());
		fireChangeEvents();
	}

	@Override
	public void composeMainPicture(final Div area)
	{
		mainPictureContainer = area;
		final MediaModel picture = currentProduct.getPicture();
		String imgPath = "";
		if (picture != null)
		{
			imgPath = serverPath + picture.getURL();
		}
		renderMainImage(area, imgPath);
	}

	@Override
	public void composePictureGallery(final Div area)
	{
		galleryContainer = area;

		if (currentProduct.getGalleryImages().isEmpty())
		{
			if (currentProduct instanceof VariantProductModel)
			{
				final ProductModel baseProduct = ((VariantProductModel) currentProduct).getBaseProduct();
				renderGallery(area, baseProduct);
			}
			else
			{
				final GalleryTile gt = new GalleryTile();
				gt.setParent(area);
			}
		}
		else
		{
			renderGallery(area, currentProduct);
		}
	}

	private void renderGallery(final Div area, final ProductModel product)
	{
		for (final MediaContainerModel container : product.getGalleryImages())
		{
			for (final MediaModel media : container.getMedias())
			{
				if (media.getMediaFormat() != null && media.getMediaFormat().getQualifier().contains("65Wx65H"))
				{
					renderGalleryImage(area, media);
				}
			}
		}
	}

	private void renderFormatTile(final ImageTile imageTile, final MediaModel media)
	{
		imageTile.renderPreviewImage(serverPath + media.getURL());
		imageTile.addRemoveEventListener(Events.ON_CLICK, new EventListener()
		{

			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				getProductImageMediaService().removeMediaFromGalleryImages(currentProduct, media);
				refresh();
			}
		});
	}

	private void renderGalleryImage(final Div area, final MediaModel media)
	{
		final GalleryTile gt = new GalleryTile();
		galleryTiles.put(gt, media);
		if (clickedGalleryImage != null && clickedGalleryImage.getMedias().contains(media))
		{
			gt.select();
		}
		gt.renderImage(serverPath + media.getURL());
		gt.setParent(area);
		gt.addRemoveEventListener(Events.ON_CLICK, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception //NOPMD: ZK specific
			{
				getProductImageMediaService().removeGalleryContainer(currentProduct, media);
				refresh();
			}
		});
		gt.addEventListener(Events.ON_DROP, new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception //NOPMD:ZK Specific
			{
				if (event instanceof DropEvent)
				{
					//re-arrange, show-1208
					if (isRearrange(event))
					{
						if (isFrontRearrange(event))
						{
							setGalleryItemToMainPicture(event);
						}
						rearrangeGalleryImages(event);
					}
					else
					{
						handleDropFromMainPictureToGallery(media);
					}
				}
			}


			private boolean isRearrange(final Event event)
			{
				final Div draggedDiv = (Div) ((DropEvent) event).getDragged();
				return draggedDiv.getSclass().contains(GALLERY_SCLASS);
			}

			private boolean isFrontRearrange(final Event event)
			{
				final Div targetDiv = (Div) ((DropEvent) event).getTarget();
				final MediaContainerModel targetMediaContainer = getMediaContainerFromDiv(targetDiv);
				return currentProduct.getGalleryImages().indexOf(targetMediaContainer) == 0;
			}

			private void handleDropFromMainPictureToGallery(final MediaModel media)
			{
				final List<MediaContainerModel> images = new ArrayList<MediaContainerModel>(currentProduct.getGalleryImages());
				images.set(currentProduct.getGalleryImages().indexOf(media.getMediaContainer()), currentProduct.getPicture()
						.getMediaContainer());
				currentProduct.setGalleryImages(images);
				//				getProductImageMediaService().getModelService().save(currentProduct);
				refresh();
			}

			private void rearrangeGalleryImages(final Event event)
			{
				final Div draggedDiv = (Div) ((DropEvent) event).getDragged();
				final MediaContainerModel draggedMediaContainer = getMediaContainerFromDiv(draggedDiv);

				final Div targetDiv = (Div) ((DropEvent) event).getTarget();
				final MediaContainerModel targetMediaContainer = getMediaContainerFromDiv(targetDiv);

				final int target = currentProduct.getGalleryImages().indexOf(targetMediaContainer);
				final int dragged = currentProduct.getGalleryImages().indexOf(draggedMediaContainer);

				final List<MediaContainerModel> images = new ArrayList<MediaContainerModel>(currentProduct.getGalleryImages());
				Collections.swap(images, target, dragged);
				currentProduct.setGalleryImages(images);
				//				getProductImageMediaService().getModelService().save(currentProduct);
				refresh();
			}
		});

		gt.addEventListener(Events.ON_CLICK, new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				if (event instanceof MouseEvent)
				{
					final GalleryTile div = (GalleryTile) event.getTarget();

					if (div.isSelected())
					{
						div.unselect();
						clickedGalleryImage = null;
					}
					else
					{
						for (final GalleryTile gt : galleryTiles.keySet())
						{
							gt.unselect();
						}
						div.select();
						final MediaContainerModel mediaContainerFromDiv = getMediaContainerFromDiv(div);
						clickedGalleryImage = mediaContainerFromDiv;
						updateImageTiles(mediaContainerFromDiv);
					}
				}
			}
		});
	}

	public final MediaContainerModel getMediaContainerFromDiv(final Div draggedDiv)
	{
		final MediaModel draggedModel = galleryTiles.get(draggedDiv);

		return getContainerFromProduct(currentProduct, draggedModel);
	}

	public MediaContainerModel getContainerFromProduct(final ProductModel product, final MediaModel model)
	{
		for (final MediaContainerModel container : product.getGalleryImages())
		{
			if (container.getMedias().contains(model))
			{
				return container;
			}
		}
		return null;
	}

	public MediaModel setGalleryItemToMainPicture(final Event event)
	{
		final Div div = (Div) ((DropEvent) event).getDragged();

		//gallery to main backend
		final MediaModel model = galleryTiles.get(div);

		getProductImageMediaService().setBestMediaToMainPicture(model, currentProduct);
		return model;
	}

	private void renderMainImage(final Div area, final String url)
	{
		final MainTile mt = new MainTile();
		mt.setParent(area);
		mt.renderImage(url);

		mt.addEventListener(Events.ON_DROP, new EventListener()
		{

			@Override
			public void onEvent(final Event event) throws Exception //NOPMD:ZK Specific
			{
				if (event instanceof DropEvent)
				{
					final MediaModel model = setGalleryItemToMainPicture(event);
					//show-1206
					movingNewMainPictureToTopOfGallery(model);
					refresh();
				}
			}

			private void movingNewMainPictureToTopOfGallery(final MediaModel model)
			{
				final List<MediaContainerModel> images = new ArrayList<MediaContainerModel>();
				//new main pic is now first element at gallery
				images.add(model.getMediaContainer());
				//removing moved element
				images.addAll(currentProduct.getGalleryImages());
				images.remove(images.lastIndexOf(model.getMediaContainer()));
				currentProduct.setGalleryImages(images);
				getProductImageMediaService().getModelService().save(currentProduct);
			}
		});

		mt.addRemoveEventListener(Events.ON_CLICK, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception //NOPMD: ZK specific
			{
				currentProduct.setPicture(null);
				getProductImageMediaService().getModelService().save(currentProduct);
				refresh();
			}
		});
	}

	@Override
	public void setImageWithFormat(final byte[] bytes, final ImageTile tile)
	{
		final String format = tile.getFormat();
		tile.setReady(true);

		MediaModel setImageWithFormat = null;
		if (clickedGalleryImage != null)
		{
			setImageWithFormat = getProductImageMediaService()
					.setImageWithFormat(bytes, currentProduct, format, clickedGalleryImage);
		}
		else
		{
			setImageWithFormat = getProductImageMediaService().setImageWithFormat(bytes, currentProduct, format, null);
			renderGalleryImage(galleryContainer, setImageWithFormat);
		}

		//hide message
		tile.getFirstChild().setVisible(false);
		//uploaded img to 'drop' area
		renderFormatTile(tile, setImageWithFormat);
		refresh();
	}

	@Override
	public void setImageWithAutoResize(final byte[] bytes, final ImageTile containerDiv)
	{
		final MediaModel uploadedMediaModel = getProductImageMediaService().updateMediaModels(bytes, currentProduct);
		final MediaContainerModel newContainer = getProductImageMediaService().updateMediaContainerModels(uploadedMediaModel,
				currentProduct, siteUid, tempContainer);

		if (clickedGalleryImage != null)
		{
			getProductImageMediaService().switchProductImageContainer(clickedGalleryImage, newContainer, currentProduct);
			clickedGalleryImage = newContainer;
		}
		else
		{
			getProductImageMediaService().attachContainerToProductModel(Collections.singleton(newContainer), currentProduct);
			renderGalleryImage(galleryContainer, getProductImageMediaService().getGalleryImage(newContainer));
		}

		//delete message
		containerDiv.getFirstChild().setVisible(false);
		//uploaded img to 'drop' area
		renderFormatTile(containerDiv, uploadedMediaModel);
		refresh();
		updateImageTiles(newContainer);
	}

	@Override
	public void setImagesFromZip(final List<byte[]> zipEntryBytes, final ImageTile container)
	{
		// Since we can display only one image, and do not want to refresh UI for each of the
		// zip file entries, remember the first entry and us it to refres UI later on, after saving all the models.
		final List<MediaModel> mediaModels = new ArrayList<MediaModel>();
		final List<MediaContainerModel> mediaContainerModels = new ArrayList<MediaContainerModel>();
		for (final byte[] bytes : zipEntryBytes)
		{
			final MediaModel uploadedMediaModel = getProductImageMediaService().updateMediaModels(bytes, currentProduct);
			final MediaContainerModel newContainer = getProductImageMediaService().updateMediaContainerModels(uploadedMediaModel,
					currentProduct, siteUid, tempContainer);
			mediaModels.add(uploadedMediaModel);
			mediaContainerModels.add(newContainer);
			if (clickedGalleryImage != null)
			{
				getProductImageMediaService().switchProductImageContainer(clickedGalleryImage, newContainer, currentProduct);
				clickedGalleryImage = newContainer;
			}
			else
			{
				getProductImageMediaService().attachContainerToProductModel(Collections.singleton(newContainer), currentProduct);
				renderGalleryImage(galleryContainer, getProductImageMediaService().getGalleryImage(newContainer));
			}
		}

		if (!mediaModels.isEmpty() && !mediaContainerModels.isEmpty())
		{
			final MediaContainerModel newContainer = mediaContainerModels.get(0);
			final MediaModel uploadedMediaModel = mediaModels.get(0);
			renderGalleryImage(galleryContainer, getProductImageMediaService().getGalleryImage(newContainer));

			updateImageTiles(newContainer);

			//delete message
			container.getFirstChild().setVisible(false);
			//uploaded img to 'drop' area
			renderFormatTile(container, uploadedMediaModel);
			refresh();
		}
	}

	private void updateImageTiles(final MediaContainerModel newContainer)
	{
		for (final ImageTile tile : imageTiles)
		{
			tile.reset();
			for (final MediaModel media : newContainer.getMedias())
			{
				if (media.getMediaFormat().getQualifier().contentEquals(tile.getFormat()))
				{
					//delete message
					tile.hideTitle();
					renderFormatTile(tile, media);
					tile.setReady(true);
				}
			}
		}
	}

	@Override
	public void setWindowTitle(final Window manageProductPictureWindow)
	{
		manageProductPictureWindow.setTitle("Manage Product Image - " + currentProduct.getName() + " (" + currentProduct.getCode()
				+ ")");
	}

	@Override
	public boolean isSynchronizationNeeded()
	{
		return getProductImageMediaService().isSynchronizationNeeded(currentProduct);
	}

	@Override
	public void setButton(final Component btnDone)
	{
		this.btnDone = btnDone;
	}

	@Override
	public Map<String, String> getMediaFormats()
	{
		return mediaFormatsMap;
	}

	@Override
	public boolean isResizePluginIncluded()
	{
		return getProductImageMediaService().isResizePluginIncluded();
	}

	@Override
	public void registerListener(final ProductImageModelListener modelListener)
	{
		listeners.add(modelListener);
	}

	public ProductImageMediaService getProductImageMediaService()
	{
		if (productImageMediaService == null)
		{
			productImageMediaService = (ProductImageMediaService) Registry.getApplicationContext().getBean(
					"productImageMediaService");
		}
		return productImageMediaService;
	}

	@Override
	public void publish()
	{
		final List<MediaModel> mediaModels = new ArrayList<MediaModel>(galleryTiles.values());
		getProductImageMediaService().synchronize(currentProduct, mediaModels);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.liveeditaddon.cockpit.wizards.productimagemanagement.ProductImageManagementViewModel#
	 * getSelectedCatalogVersionModel()
	 */
	@Override
	public CatalogVersionModel getSelectedCatalogVersionModel()
	{
		return currentProduct.getCatalogVersion();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.liveeditaddon.cockpit.wizards.productimagemanagement.ProductImageManagementViewModel#
	 * mediaContainerChanged(de.hybris.platform.core.model.media.MediaContainerModel)
	 */
	@Override
	public void mediaContainerChanged(final MediaContainerModel containerModel)
	{
		selectedContainerModel = containerModel;
	}
}
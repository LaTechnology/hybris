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
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.liveeditaddon.cockpit.wizards.components.ImageTile;
import de.hybris.liveeditaddon.cockpit.wizards.productimagemanagement.events.ProductImageModelListener;

import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Window;


/**
 * 
 */
public interface ProductImageManagementViewModel
{
	void initModel(final ProductModel previewProduct, final String serverPathfinal, final Map<String, String> mediaFormatsMap,
			final String siteUid);

	void reset();

	boolean save();

	void composeMainPicture(Div area);

	void composePictureGallery(Div area);

	void setImageWithFormat(byte[] bytes, ImageTile superDropTile);

	void setImageWithAutoResize(byte[] bytes, ImageTile autoResizeDropContainer);

	void setImagesFromZip(List<byte[]> zipEntryBytes, ImageTile container);

	boolean isResizePluginIncluded();

	void registerTile(ImageTile tile);

	void setWindowTitle(Window manageProductPictureWindow);

	boolean isSynchronizationNeeded();

	void setButton(Component btnDone);

	Map<String, String> getMediaFormats();

	void registerListener(ProductImageModelListener modelListener);

	void fireInitEvents();

	void publish();

	CatalogVersionModel getSelectedCatalogVersionModel();

	void mediaContainerChanged(MediaContainerModel containerModel);
}

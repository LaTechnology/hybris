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
package de.hybris.liveeditaddon.cockpit.wizards.mediacontainermanagement.service;


import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.liveeditaddon.cockpit.wizards.components.ImageTile;
import de.hybris.liveeditaddon.cockpit.wizards.productimagemanagement.events.ProductImageModelListener;

import java.util.Collection;


/**
 */
public interface ManageMediaService
{

	void setImageWithFormat(byte[] bytes, ImageTile tile);

	void setImageWithAutoResize(byte[] bytes, ImageTile tile);

	void save();

	String getServerPath();

	String getMediaPathForFormat(final String format);

	MediaModel getMasterMedia(final Collection<MediaModel> medias);

	boolean isReadyForSave();

	void removeMediaByFormat(final String format);

	void addListener(ProductImageModelListener listener);
}

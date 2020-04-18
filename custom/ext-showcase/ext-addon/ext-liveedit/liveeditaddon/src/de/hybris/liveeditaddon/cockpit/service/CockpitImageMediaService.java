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

import de.hybris.liveeditaddon.cockpit.wizards.components.ImageTile;

import java.util.List;



/**
 * 
 */
public interface CockpitImageMediaService
{
	void setImageWithFormat(byte[] bytes, ImageTile superDropTile);

	void setImageWithAutoResize(byte[] bytes, ImageTile autoResizeDropContainer);

	void setImagesFromZip(List<byte[]> zipEntryBytes, ImageTile container);

	boolean isResizePluginIncluded();
}
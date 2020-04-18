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

import de.hybris.platform.core.model.media.MediaContainerModel;


/**
 * 
 */
public interface ImageResizePlugin
{
	public MediaContainerModel convertMedia(MediaContainerModel container);

	public MediaContainerModel convertMediaForSite(MediaContainerModel container, final String siteUid);
}

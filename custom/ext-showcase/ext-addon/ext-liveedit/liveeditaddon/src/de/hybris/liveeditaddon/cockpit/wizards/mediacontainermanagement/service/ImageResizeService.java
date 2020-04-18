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

import java.util.Set;


/**
 */
public interface ImageResizeService
{
	Set<MediaModel> convertMedia(final MediaModel baseModel);

	Set<MediaModel> convertMedia(final MediaModel baseModel, final String siteUid);

}

/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 * 
 */
package com.hybris.addon.common.url;

import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.core.model.media.MediaModel;

import javax.servlet.http.HttpServletRequest;


/**
 * Resolves a Media url to its absolute URL
 * 
 * @author rmcotton
 */
public interface MediaAbsoluteUrlResolver
{
	String resolve(HttpServletRequest request, ImageData image, boolean secure);

	String resolve(HttpServletRequest request, MediaModel image, boolean secure);

}

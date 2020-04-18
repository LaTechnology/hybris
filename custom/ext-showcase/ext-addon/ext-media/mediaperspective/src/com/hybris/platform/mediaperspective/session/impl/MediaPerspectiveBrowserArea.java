/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2011 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package com.hybris.platform.mediaperspective.session.impl;

import de.hybris.platform.cockpit.session.impl.DefaultSearchBrowserArea;

import org.apache.log4j.Logger;


/**
 */
public class MediaPerspectiveBrowserArea extends DefaultSearchBrowserArea
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(MediaPerspectiveBrowserArea.class);

	@Override
	public String getDefaultBrowserClass()
	{
		return MediaPerspectiveSearchBrowserModel.class.getName();
	}


}

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

import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelModel;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitNavigationArea;

import org.apache.log4j.Logger;

import com.hybris.platform.mediaperspective.components.navigationarea.MediaPerspectiveNavigationAreaModel;


/**
 */
public class MediaPerspectiveNavigationArea extends BaseUICockpitNavigationArea
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(MediaPerspectiveNavigationArea.class);

	@Override
	public SectionPanelModel getSectionModel()
	{
		if (super.getSectionModel() == null)
		{
			final MediaPerspectiveNavigationAreaModel model = new MediaPerspectiveNavigationAreaModel(this);
			model.initialize();
			super.setSectionModel(model);
		}
		return super.getSectionModel();
	}
}

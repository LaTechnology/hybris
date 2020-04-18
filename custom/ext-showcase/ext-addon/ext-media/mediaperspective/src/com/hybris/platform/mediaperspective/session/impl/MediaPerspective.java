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

import de.hybris.platform.cmscockpit.events.impl.CmsPerspectiveInitEvent;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;

import java.util.Map;


/**
 */
public class MediaPerspective extends BaseUICockpitPerspective
{

	public MediaPerspective()
	{
		// Initialise
	}

	@Override
	public void initialize(final Map params)
	{
		UISessionUtils.getCurrentSession().sendGlobalEvent(new CmsPerspectiveInitEvent(this));
		super.initialize(params);
	}
}

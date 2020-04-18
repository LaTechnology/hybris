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
package de.hybris.liveeditaddon.cockpit.service.impl;

import de.hybris.liveeditaddon.cockpit.service.AbstractViewportRefreshContentHandler;


/**
 */
public class MobileViewportRefreshContentHandler extends AbstractViewportRefreshContentHandler
{
	@Override
	protected String getUiExperience()
	{
		return "Mobile";
	}

	@Override
	protected String getUiExperienceSclass()
	{
		return "liveedit-browser-mobile";
	}

	@Override
	protected String getDefaultViewportWidth()
	{
		return "320px";
	}
}

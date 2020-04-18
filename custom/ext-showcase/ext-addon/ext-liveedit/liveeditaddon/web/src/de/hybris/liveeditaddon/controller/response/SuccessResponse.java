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
package de.hybris.liveeditaddon.controller.response;

import de.hybris.liveeditaddon.admin.ComponentAdminMenuGroupData;


/**
 * 
 */
public class SuccessResponse
{
	ComponentAdminMenuGroupData menu;
	boolean success = true;

	public SuccessResponse(final ComponentAdminMenuGroupData menu)
	{
		this.menu = menu;
	}

	public boolean isSuccess()
	{
		return this.success;
	}

	public ComponentAdminMenuGroupData getMenu()
	{
		return this.menu;
	}
}

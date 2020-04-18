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
package de.hybris.liveeditaddon.admin.strategies.impl;

import de.hybris.liveeditaddon.admin.ComponentActionMenuRequestData;
import de.hybris.liveeditaddon.admin.strategies.ComponentAdminActionEnabledStrategy;


/**
 * 
 */
public class DisabledActionEnabledStrategy implements ComponentAdminActionEnabledStrategy
{
	private boolean visible = true;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.liveeditaddon.admin.strategies.ComponentAdminActionEnabledStrategy#isEnabled(de.hybris.liveeditaddon
	 * .admin.ComponentActionMenuRequestData)
	 */
	@Override
	public boolean isEnabled(final ComponentActionMenuRequestData request)
	{

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.liveeditaddon.admin.strategies.ComponentAdminActionEnabledStrategy#isVisible(de.hybris.liveeditaddon
	 * .admin.ComponentActionMenuRequestData, boolean)
	 */
	@Override
	public boolean isVisible(final ComponentActionMenuRequestData request, final boolean enabled)
	{
		return visible;
	}

	public void setVisible(final boolean visible)
	{
		this.visible = visible;
	}

}

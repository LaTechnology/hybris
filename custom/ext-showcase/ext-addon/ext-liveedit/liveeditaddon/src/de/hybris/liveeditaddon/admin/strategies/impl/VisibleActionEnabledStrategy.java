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

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.liveeditaddon.admin.ComponentActionMenuRequestData;
import de.hybris.liveeditaddon.admin.strategies.ComponentAdminActionEnabledStrategy;

import org.springframework.beans.factory.annotation.Required;


/**
 * 
 */
public class VisibleActionEnabledStrategy implements ComponentAdminActionEnabledStrategy
{

	private CMSComponentService cmsComponentService;
	private boolean enabledIfVisible = true;

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
		try
		{
			final AbstractCMSComponentModel component = getCmsComponentService().getAbstractCMSComponent(request.getComponentUid());
			return enabledIfVisible & component.getVisible().booleanValue();
		}
		catch (final CMSItemNotFoundException e)
		{
			throw new IllegalStateException("component is missing for uid [" + request.getComponentUid() + "]");
		}
	}

	public void setEnabledIfVisible(final boolean enabledIfVisible)
	{
		this.enabledIfVisible = enabledIfVisible;
	}


	/**
	 * @return the cmsComponentService
	 */
	public CMSComponentService getCmsComponentService()
	{
		return cmsComponentService;
	}

	/**
	 * @param cmsComponentService
	 *           the cmsComponentService to set
	 */
	@Required
	public void setCmsComponentService(final CMSComponentService cmsComponentService)
	{
		this.cmsComponentService = cmsComponentService;
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
		return true;
	}

}

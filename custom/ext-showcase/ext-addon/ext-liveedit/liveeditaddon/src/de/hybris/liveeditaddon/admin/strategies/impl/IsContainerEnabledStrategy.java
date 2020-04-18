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
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.model.contents.containers.AbstractCMSComponentContainerModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.liveeditaddon.admin.ComponentActionMenuRequestData;
import de.hybris.liveeditaddon.admin.strategies.ComponentAdminActionEnabledStrategy;
import org.springframework.beans.factory.annotation.Required;


/**
 */
public class IsContainerEnabledStrategy implements ComponentAdminActionEnabledStrategy
{
	private CMSComponentService cmsComponentService;

	@Override
	public boolean isEnabled(final ComponentActionMenuRequestData request)
	{
		return isContainer(request);
	}

	@Override
	public boolean isVisible(final ComponentActionMenuRequestData request, final boolean enabled)
	{
		return enabled;
	}

	protected boolean isContainer(final ComponentActionMenuRequestData request)
	{
		try
		{
			final String componentUid = request.getComponentUid();

			if (componentUid == null || componentUid.isEmpty())
			{
				return false;
			}

			final AbstractCMSComponentModel component = getCmsComponentService().getAbstractCMSComponent(componentUid);
			if (component instanceof SimpleCMSComponentModel)
			{
				final SimpleCMSComponentModel sc = (SimpleCMSComponentModel) component;
				if (!sc.getContainers().isEmpty())
				{
					final String slotUid = request.getSlotUid();
					for (final AbstractCMSComponentContainerModel container : sc.getContainers())
					{
						for (final ContentSlotModel slot : container.getSlots())
						{
							if (slot.getUid().equals(slotUid))
							{
								return true;
							}
						}
					}
				}
			}

			return false;
		}
		catch (final CMSItemNotFoundException e)
		{
			throw new IllegalStateException("Component is missing for uid [" + request.getComponentUid() + "]");
		}
	}

	public CMSComponentService getCmsComponentService()
	{
		return cmsComponentService;
	}

	@Required
	public void setCmsComponentService(final CMSComponentService cmsComponentService)
	{
		this.cmsComponentService = cmsComponentService;
	}
}

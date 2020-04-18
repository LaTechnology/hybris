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
package de.hybris.liveeditaddon.admin.facades.impl;

import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.liveeditaddon.admin.ComponentAdminMenuGroupData;
import de.hybris.liveeditaddon.admin.ComponentAdminMenuItemData;
import de.hybris.liveeditaddon.admin.SlotActionMenuRequestData;
import de.hybris.liveeditaddon.admin.facades.AbstractCMSAdminFacade;
import de.hybris.liveeditaddon.admin.facades.CMSSlotAdminFacade;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Preconditions;


/**
 * 
 */
public class DefaultCMSSlotAdminFacade extends AbstractCMSAdminFacade implements CMSSlotAdminFacade
{

	private Converter<SlotActionMenuRequestData, ComponentAdminMenuGroupData> slotAdminMenuGroupConverter;

	@Override
	public ComponentAdminMenuGroupData getLiveEditSlotAdminMenu(final SlotActionMenuRequestData request)
			throws PreviewTicketInvalidException
	{
		Preconditions.checkNotNull(request.getPreviewTicket(), "preview ticket should not be null");
		Preconditions.checkNotNull(request.getSlotUid(), "slot id should not be null");

		final ComponentAdminMenuGroupData menu = getAdminMenu(request);
		removeDisabled(menu);
		return menu;
	}

	@Required
	public void setSlotAdminMenuGroupConverter(
			final Converter<SlotActionMenuRequestData, ComponentAdminMenuGroupData> slotAdminMenuGroupConverter)
	{
		this.slotAdminMenuGroupConverter = slotAdminMenuGroupConverter;
	}

	@Override
	protected Converter getAdminMenuGroupConverter()
	{
		return slotAdminMenuGroupConverter;
	}

	void removeDisabled(final ComponentAdminMenuGroupData componentAdminMenuGroupData)
	{
		final List<ComponentAdminMenuItemData> items = componentAdminMenuGroupData.getItems();
		for (final Iterator iterator = items.iterator(); iterator.hasNext();)
		{
			final ComponentAdminMenuItemData componentAdminMenuItemData = (ComponentAdminMenuItemData) iterator.next();
			if (!Boolean.TRUE.equals(componentAdminMenuItemData.getEnabled()))
			{
				iterator.remove();
			}
		}
	}

}

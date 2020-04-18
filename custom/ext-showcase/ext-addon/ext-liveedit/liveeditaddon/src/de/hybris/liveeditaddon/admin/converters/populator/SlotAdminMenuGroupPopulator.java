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
package de.hybris.liveeditaddon.admin.converters.populator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.liveeditaddon.admin.ComponentAdminMenuActionData;
import de.hybris.liveeditaddon.admin.ComponentAdminMenuGroupData;
import de.hybris.liveeditaddon.admin.ComponentAdminMenuItemData;
import de.hybris.liveeditaddon.admin.SlotActionMenuRequestData;
import de.hybris.liveeditaddon.enums.CMSComponentAdminActionGroup;


/**
 * 
 */
public class SlotAdminMenuGroupPopulator extends
		AbstractPopulatingConverter<SlotActionMenuRequestData, ComponentAdminMenuGroupData>
{

	private CMSComponentAdminActionGroup type;
	private EnumerationService enumerationService;
	private List<Converter<SlotActionMenuRequestData, ComponentAdminMenuItemData>> menuItemConverters;

	@Override
	public void populate(final SlotActionMenuRequestData source, final ComponentAdminMenuGroupData target)
			throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setActionGroupType(getType());
		target.setName(getEnumerationService().getEnumerationName(getType()));
		target.setId(getType().getCode());

		final List<ComponentAdminMenuItemData> menuItems = new ArrayList<ComponentAdminMenuItemData>(menuItemConverters.size());
		for (final Converter<SlotActionMenuRequestData, ComponentAdminMenuItemData> menuItemConverter : getMenuItemConverters())
		{
			final ComponentAdminMenuItemData item = menuItemConverter.convert(source);
			if (Boolean.FALSE.equals(item.getVisible()))
			{
				continue;
			}

			if (Boolean.TRUE.equals(item.getEnabled()))
			{
				target.setEnabled(Boolean.TRUE);
			}

			if (item instanceof ComponentAdminMenuActionData)
			{
				item.setId(getType().getCode() + "_" + ((ComponentAdminMenuActionData) item).getActionType().getCode());
			}
			menuItems.add(item);
		}
		if (!menuItems.isEmpty())
		{
			target.setItems(menuItems);
		}

	}

	@Override
	protected ComponentAdminMenuGroupData createTarget()
	{
		return new ComponentAdminMenuGroupData();
	}

	public CMSComponentAdminActionGroup getType()
	{
		return this.type;
	}

	@Required
	public void setType(final CMSComponentAdminActionGroup type)
	{
		this.type = type;
	}

	public EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	@Required
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

	public List<Converter<SlotActionMenuRequestData, ComponentAdminMenuItemData>> getMenuItemConverters()
	{
		return this.menuItemConverters;
	}

	@Required
	public void setMenuItemConverters(
			final List<Converter<SlotActionMenuRequestData, ComponentAdminMenuItemData>> menuItemConverters)
	{
		this.menuItemConverters = menuItemConverters;
	}

}

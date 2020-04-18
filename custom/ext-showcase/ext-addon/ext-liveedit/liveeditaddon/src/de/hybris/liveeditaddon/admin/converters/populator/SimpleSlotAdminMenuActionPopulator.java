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

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.liveeditaddon.admin.ComponentAdminMenuActionData;
import de.hybris.liveeditaddon.admin.SlotActionMenuRequestData;
import de.hybris.liveeditaddon.admin.strategies.ComponentSlotAdminActionEnabledStrategy;
import de.hybris.liveeditaddon.enums.CMSComponentAdminAction;


/**
 * 
 */
public class SimpleSlotAdminMenuActionPopulator implements Populator<SlotActionMenuRequestData, ComponentAdminMenuActionData>
{
	ComponentSlotAdminActionEnabledStrategy componentSlotAdminActionEnabledStrategy;
	private CMSComponentAdminAction type;
	private EnumerationService enumerationService;
	private String addOn;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final SlotActionMenuRequestData source, final ComponentAdminMenuActionData target)
			throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setActionType(type);
		target.setName(getEnumerationService().getEnumerationName(getType()));
		target.setEnabled(isEnabled(source, target));
		target.setVisible(isVisible(source, target.getEnabled().booleanValue()));
		target.setAddon(getAddOn());
	}

	protected Boolean isEnabled(final SlotActionMenuRequestData source, final ComponentAdminMenuActionData target)
	{
		return componentSlotAdminActionEnabledStrategy != null ? Boolean.valueOf(componentSlotAdminActionEnabledStrategy
				.isEnabled(source)) : Boolean.TRUE;
	}

	protected Boolean isVisible(final SlotActionMenuRequestData source, final boolean enabled)
	{
		return componentSlotAdminActionEnabledStrategy != null ? Boolean.valueOf(componentSlotAdminActionEnabledStrategy.isVisible(
				source, enabled)) : Boolean.TRUE;
	}

	public CMSComponentAdminAction getType()
	{
		return type;
	}

	@Required
	public void setType(final CMSComponentAdminAction type)
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

	public void setComponentSlotAdminActionEnabledStrategy(
			final ComponentSlotAdminActionEnabledStrategy componentSlotAdminActionEnabledStrategy)
	{
		this.componentSlotAdminActionEnabledStrategy = componentSlotAdminActionEnabledStrategy;
	}

	public String getAddOn()
	{
		return addOn;
	}

	@Required
	public void setAddOn(final String addOn)
	{
		this.addOn = addOn;
	}
}

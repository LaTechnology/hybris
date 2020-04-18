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

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.liveeditaddon.admin.ComponentActionMenuRequestData;
import de.hybris.liveeditaddon.admin.ComponentAdminMenuActionData;


/**
 * 
 */
public class ComponentPkAdminMenuActionPopulator implements
		Populator<ComponentActionMenuRequestData, ComponentAdminMenuActionData>
{

	private CMSComponentService cmsComponentService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final ComponentActionMenuRequestData source, final ComponentAdminMenuActionData target)
			throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		try
		{
			final AbstractCMSComponentModel component = getCmsComponentService().getAbstractCMSComponent(source.getComponentUid());
			target.setPk(component.getPk().getLong());
		}
		catch (final CMSItemNotFoundException e)
		{
			throw new IllegalStateException("component is missing for uid [" + source.getComponentUid() + "]");
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

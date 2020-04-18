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
import de.hybris.liveeditaddon.admin.ComponentActionMenuRequestData;
import de.hybris.liveeditaddon.admin.ComponentAdminMenuGroupData;
import de.hybris.liveeditaddon.admin.facades.AbstractCMSAdminFacade;
import de.hybris.liveeditaddon.admin.facades.CMSComponentAdminFacade;

import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Preconditions;


/**
 * 
 */
public class DefaultCMSComponentAdminFacade extends AbstractCMSAdminFacade implements CMSComponentAdminFacade
{

	private Converter<ComponentActionMenuRequestData, ComponentAdminMenuGroupData> componentAdminMenuGroupConverter;


	@Override
	public ComponentAdminMenuGroupData getLiveEditAdminMenu(final ComponentActionMenuRequestData request)
			throws PreviewTicketInvalidException
	{
		Preconditions.checkNotNull(request.getPreviewTicket(), "preview ticket should not be null");
		Preconditions.checkNotNull(request.getComponentUid(), "component uid should not be null");

		return getAdminMenu(request);
	}


	@Required
	public void setComponentAdminMenuGroupConverter(
			final Converter<ComponentActionMenuRequestData, ComponentAdminMenuGroupData> componentAdminMenuGroupConverter)
	{
		this.componentAdminMenuGroupConverter = componentAdminMenuGroupConverter;
	}

	@Override
	protected Converter getAdminMenuGroupConverter()
	{
		return componentAdminMenuGroupConverter;
	}

}

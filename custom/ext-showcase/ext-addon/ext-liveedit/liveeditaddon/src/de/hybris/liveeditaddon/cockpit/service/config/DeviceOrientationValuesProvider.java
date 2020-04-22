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
package de.hybris.liveeditaddon.cockpit.service.config;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.services.config.AvailableValuesProvider;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.liveeditaddon.cockpit.strategies.VariableViewportStrategy;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 */
public class DeviceOrientationValuesProvider implements AvailableValuesProvider
{
	private SessionService sessionService;

	private VariableViewportStrategy variableViewportStrategy;

	@Override
	public List<? extends Object> getAvailableValues(final PropertyDescriptor propertyDescriptor)
	{
		return getSessionService().executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public Object execute()
			{
				return getVariableViewportStrategy().getOrientations();
			}

		});
	}

	public SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	public VariableViewportStrategy getVariableViewportStrategy()
	{
		return variableViewportStrategy;
	}

	@Required
	public void setVariableViewportStrategy(final VariableViewportStrategy variableViewportStrategy)
	{
		this.variableViewportStrategy = variableViewportStrategy;
	}
}
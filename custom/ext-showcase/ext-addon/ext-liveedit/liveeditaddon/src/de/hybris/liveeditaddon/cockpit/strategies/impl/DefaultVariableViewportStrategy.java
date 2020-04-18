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
package de.hybris.liveeditaddon.cockpit.strategies.impl;

import de.hybris.platform.acceleratorservices.uiexperience.UiExperienceService;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.liveeditaddon.cockpit.strategies.VariableViewportStrategy;
import de.hybris.liveeditaddon.enums.DeviceOrientation;
import de.hybris.liveeditaddon.model.DeviceSupportModel;
import de.hybris.liveeditaddon.service.DeviceSupportService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Lists;


/**
 */
public class DefaultVariableViewportStrategy implements VariableViewportStrategy
{
	private List<String> supporteduiexperiencecodes;
	private UiExperienceService uiExperienceService;
	private DeviceSupportService deviceSupportService;
	private Map<String, Map<String, String>> viewportSupportMap;

	@Override
	public boolean supportVariableViewport(final PreviewDataModel previewData, final String uiExperienceLevel)
	{
		final UiExperienceLevel previewUiExperienceLevel = previewData.getUiExperience();
		final UiExperienceLevel serviceUiExperienceLevel = getUiExperienceService().getUiExperienceLevel();
		return supporteduiexperiencecodes.contains(uiExperienceLevel)
				&& (uiExperienceLevel.equalsIgnoreCase(previewUiExperienceLevel.getCode()) || (serviceUiExperienceLevel != null && uiExperienceLevel
						.equalsIgnoreCase(serviceUiExperienceLevel.getCode())));
	}

	@Override
	public Map<String, String> getViewports()
	{
		final UiExperienceLevel serviceUiExperienceLevel = getUiExperienceService().getUiExperienceLevel();
		if (serviceUiExperienceLevel != null)
		{
			final String codeUiExperienceLevel = serviceUiExperienceLevel.getCode();
			if (viewportSupportMap.containsKey(codeUiExperienceLevel))
			{
				return viewportSupportMap.get(codeUiExperienceLevel);
			}
		}
		return Collections.emptyMap();
	}

	@Override
	public List<DeviceOrientation> getOrientations()
	{
		return Lists.newArrayList(DeviceOrientation.values());
	}

	@Override
	public List<DeviceSupportModel> getDevices()
	{
		final UiExperienceLevel serviceUiExperienceLevel = getUiExperienceService().getUiExperienceLevel();
		return deviceSupportService.getAllByUiExperience(serviceUiExperienceLevel);
	}

	public List<String> getSupporteduiexperiencecodes()
	{
		return supporteduiexperiencecodes;
	}

	@Required
	public void setSupporteduiexperiencecodes(final List<String> supporteduiexperiencecodes)
	{
		this.supporteduiexperiencecodes = supporteduiexperiencecodes;
	}

	public UiExperienceService getUiExperienceService()
	{
		return uiExperienceService;
	}

	@Required
	public void setUiExperienceService(final UiExperienceService uiExperienceService)
	{
		this.uiExperienceService = uiExperienceService;
	}

	public DeviceSupportService getDeviceSupportService()
	{
		return deviceSupportService;
	}

	@Required
	public void setDeviceSupportService(final DeviceSupportService deviceSupportService)
	{
		this.deviceSupportService = deviceSupportService;
	}

	public Map<String, Map<String, String>> getViewportSupportMap()
	{
		return viewportSupportMap;
	}

	@Required
	public void setViewportSupportMap(final Map<String, Map<String, String>> viewportSupportMap)
	{
		this.viewportSupportMap = viewportSupportMap;
	}
}

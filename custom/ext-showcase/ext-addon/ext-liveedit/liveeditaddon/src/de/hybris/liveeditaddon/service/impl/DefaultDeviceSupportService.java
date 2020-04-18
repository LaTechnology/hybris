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
package de.hybris.liveeditaddon.service.impl;

import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.liveeditaddon.model.DeviceSupportModel;
import de.hybris.liveeditaddon.service.DeviceSupportService;
import de.hybris.liveeditaddon.service.dao.DeviceSupportDao;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 */
public class DefaultDeviceSupportService implements DeviceSupportService
{

	private DeviceSupportDao deviceSupportDao;

	@Override
	public List<DeviceSupportModel> getAll()
	{
		return getDeviceSupportDao().find();
	}

	@Override
	public List<DeviceSupportModel> getAllByUiExperience(final UiExperienceLevel uiExperienceLevel)
	{
		if (uiExperienceLevel != null)
		{
			return deviceSupportDao.findByUiExperience(uiExperienceLevel);
		}
		else
		{
			return Collections.emptyList();
		}

	}

	public DeviceSupportDao getDeviceSupportDao()
	{
		return deviceSupportDao;
	}

	@Required
	public void setDeviceSupportDao(final DeviceSupportDao deviceSupportDao)
	{
		this.deviceSupportDao = deviceSupportDao;
	}
}

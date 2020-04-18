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
package de.hybris.liveeditaddon.service;

import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.liveeditaddon.model.DeviceSupportModel;

import java.util.List;


/**
 */
public interface DeviceSupportService
{

	List<DeviceSupportModel> getAll();

	List<DeviceSupportModel> getAllByUiExperience(UiExperienceLevel uiExperienceLevel);

}

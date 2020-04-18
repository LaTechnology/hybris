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
package de.hybris.liveeditaddon.cockpit.strategies;

import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.liveeditaddon.enums.DeviceOrientation;
import de.hybris.liveeditaddon.model.DeviceSupportModel;

import java.util.List;
import java.util.Map;


/**
 */
public interface VariableViewportStrategy
{

	boolean supportVariableViewport(PreviewDataModel previewData, final String uiExperienceLevel);

	Map<String, String> getViewports();

	List<DeviceOrientation> getOrientations();

	List<DeviceSupportModel> getDevices();

}

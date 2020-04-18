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
package de.hybris.liveeditaddon.cockpit.service.editor;


import de.hybris.platform.acceleratorservices.uiexperience.UiExperienceService;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.impl.DefaultEnumUIEditor;
import de.hybris.platform.cockpit.services.config.AvailableValuesProvider;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.enumeration.EnumerationService;

import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.HtmlBasedComponent;


public class UiExperienceLevelEnumEditor extends DefaultEnumUIEditor
{

	private UiExperienceService uiExperienceService;

	@Override
	public HtmlBasedComponent createViewComponent(Object initialValue, final Map<String, ?> parameters,
			final EditorListener listener)
	{
		final String beanName = (String) parameters.get("allowedValuesList");
		final AvailableValuesProvider valuesProvider = (AvailableValuesProvider) Registry.getApplicationContext().getBean(beanName);

		final List<? extends Object> availableValues = valuesProvider.getAvailableValues(null);

		if (availableValues.isEmpty())
		{
			final EnumerationService enumService = (EnumerationService) Registry.getApplicationContext().getBean(
					"enumerationService");
			setAvailableValues(enumService.getEnumerationValues(UiExperienceLevel._TYPECODE));
		}
		else
		{
			setAvailableValues(availableValues);
			final UiExperienceLevel uiExperienceLevel = getUiExperienceService().getUiExperienceLevel();
			if (uiExperienceLevel == null || !availableValues.contains(initialValue))
			{
				initialValue = availableValues.get(0);

			}
			getUiExperienceService().setOverrideUiExperienceLevel((UiExperienceLevel) initialValue);
		}
		return super.createViewComponent(initialValue, parameters, listener);
	}

	public UiExperienceService getUiExperienceService()
	{
		if (uiExperienceService == null)
		{
			uiExperienceService = (UiExperienceService) Registry.getApplicationContext().getBean("uiExperienceService");
		}
		return uiExperienceService;
	}
}

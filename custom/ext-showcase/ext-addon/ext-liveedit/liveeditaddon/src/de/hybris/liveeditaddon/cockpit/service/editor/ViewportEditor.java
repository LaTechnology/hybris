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

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.impl.DefaultSelectUIEditor;
import de.hybris.platform.cockpit.services.config.AvailableValuesProvider;
import de.hybris.platform.core.Registry;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import java.util.List;
import java.util.Map;


/**
 */
public class ViewportEditor extends DefaultSelectUIEditor
{

	private final static String DEFAULT_VIEWPORT = "default";
	private final static String LABEL_KEY_PATTERN = "viewport.label.format.";

	@Override
	public HtmlBasedComponent createViewComponent(Object initialValue, final Map<String, ?> parameters,
			final EditorListener listener)
	{
		final String beanName = (String) parameters.get("allowedValuesList");
		final AvailableValuesProvider valuesProvider = (AvailableValuesProvider) Registry.getApplicationContext().getBean(beanName);
		final List<? extends Object> availableValues = valuesProvider.getAvailableValues(null);

		if (!availableValues.isEmpty())
		{
			setAvailableValues(availableValues);
			if (initialValue == null)
			{
				int defaultIndex = availableValues.indexOf(DEFAULT_VIEWPORT);
				if (defaultIndex == -1)
				{
					initialValue = availableValues.get(0);
				}
				else
				{
					initialValue = availableValues.get(defaultIndex);
				}
			}
		}

		return super.createViewComponent(initialValue, parameters, listener);
	}

	@Override
	protected void addObjectToCombo(Object value, Combobox box)
	{
		if (value instanceof String)
		{
			final Comboitem comboitem = new Comboitem();
			final String viewportKey = (String) value;
			final String labelKey = LABEL_KEY_PATTERN + viewportKey;
			String labelFromProperties = Labels.getLabel(labelKey);
			if (labelFromProperties == null || labelFromProperties.isEmpty())
			{
				comboitem.setLabel(viewportKey);
			}
			else
			{
				comboitem.setLabel(labelFromProperties);
			}

			comboitem.setValue(value);
			comboitem.setImage(null);

			box.appendChild(comboitem);
		}
		else
		{
			super.addObjectToCombo(value, box);
		}
	}
}

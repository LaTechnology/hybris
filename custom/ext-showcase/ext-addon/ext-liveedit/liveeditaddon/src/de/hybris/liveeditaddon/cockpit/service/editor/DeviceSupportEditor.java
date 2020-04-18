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
import de.hybris.liveeditaddon.model.DeviceSupportModel;

import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;


/**
 */
public class DeviceSupportEditor extends DefaultSelectUIEditor
{
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
			for (final Object object : getAvailableValues())
			{
				final DeviceSupportModel deviceObject = (DeviceSupportModel) object;
				if (initialValue instanceof de.hybris.platform.cockpit.model.meta.TypedObject)
				{
					if (((de.hybris.platform.cockpit.model.meta.TypedObject) initialValue).getObject() != null)
					{
						if (deviceObject.getCode().equals(
								((DeviceSupportModel) ((de.hybris.platform.cockpit.model.meta.TypedObject) initialValue).getObject())
										.getCode()))
						{
							initialValue = object;
							break;
						}
					}
				}
				else if (initialValue instanceof DeviceSupportModel)
				{
					if (deviceObject.getCode().equals(((DeviceSupportModel) initialValue).getCode()))
					{
						initialValue = object;
						break;
					}
				}
				else if (initialValue == null && deviceObject.isDefault())
				{
					initialValue = object;
					break;
				}
			}
		}

		return super.createViewComponent(initialValue, parameters, listener);
	}

	@Override
	protected void addObjectToCombo(final Object value, final Combobox box)
	{
		if (value instanceof DeviceSupportModel)
		{
			final Comboitem comboitem = new Comboitem();
			final DeviceSupportModel deviceSupportModel = (DeviceSupportModel) value;

			comboitem.setLabel(deviceSupportModel.getName());
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

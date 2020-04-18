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
package de.hybris.liveeditaddon.cockpit.session.impl;

import de.hybris.platform.cockpit.components.listview.AdvancedListViewAction;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.EditorConfiguration;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.EditorArea;


/**
 * 
 */
public class LiveEditCallbackEditorArea extends EditorArea
{
	private String overrideEditorAreaConfigCode = EditorArea.EDITOR_AREA_CONFIG_CODE;

	@Override
	public EditorConfiguration getConfiguration(final String typecode)
	{
		if (overrideEditorAreaConfigCode != null)
		{

			final ObjectTemplate template = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(typecode);
			return getUIConfigurationService().getComponentConfiguration(template, getOverrideEditorAreaConfigCode(),
					EditorConfiguration.class);
		}
		return super.getConfiguration(typecode);
	}

	public String getOverrideEditorAreaConfigCode()
	{
		return overrideEditorAreaConfigCode;
	}

	public void setOverrideEditorAreaConfigCode(final String editorAreaConfigCode)
	{
		this.overrideEditorAreaConfigCode = editorAreaConfigCode;
	}

	@Override
	public void onCockpitEvent(final CockpitEvent event)
	{
		super.onCockpitEvent(event);
		// ensure actions force a refresh.
		if (event instanceof ItemChangedEvent && event.getSource() instanceof AdvancedListViewAction)
		{
			final TypedObject item = ((ItemChangedEvent) event).getItem();
			if (item.equals(getCurrentObject()))
			{
				getEditorAreaController().resetSectionPanelModel();
			}
		}
	}
}
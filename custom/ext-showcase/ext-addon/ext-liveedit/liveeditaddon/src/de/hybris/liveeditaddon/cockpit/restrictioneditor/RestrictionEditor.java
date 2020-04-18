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
package de.hybris.liveeditaddon.cockpit.restrictioneditor;

import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Textbox;


public class RestrictionEditor extends de.hybris.platform.cockpit.components.editor.CockpitEditorContainer
{
	private final Textbox restrictionNames;
	private Collection<AbstractRestrictionModel> restrictions = new ArrayList<AbstractRestrictionModel>();
	public final static String VALUE_MAP = "value_map";

	public RestrictionEditor()
	{
		super();

		restrictionNames = new Textbox();
		restrictionNames.setSclass("restrictionNames");
		this.appendChild(restrictionNames);

		this.addEventListener(Events.ON_CLICK, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				final Component newWindow = Executions.getCurrent().createComponents("/cmscockpit/RestrictionEditorComposer.zul",
						null, Collections.singletonMap(VALUE_MAP, getRestrictions()));
				newWindow.addEventListener(Events.ON_CLOSE, new EventListener()
				{
					@Override
					public void onEvent(final Event arg0) throws Exception
					{
						restrictionNames.setText(getRestrictionNames(getRestrictions()));
						raiseEvent();
					}
				});
			}
		});
	}

	public Collection<AbstractRestrictionModel> getRestrictions()
	{
		return restrictions;
	}

	public void setRestrictions(final Collection<AbstractRestrictionModel> restrictions)
	{
		this.restrictions = new ArrayList<AbstractRestrictionModel>(restrictions);
		restrictionNames.setText(getRestrictionNames(this.restrictions));
	}

	private String getRestrictionNames(final Collection<AbstractRestrictionModel> restrictions)
	{
		String infoText = "";
		if (restrictions != null)
		{
			for (final AbstractRestrictionModel restriction : restrictions)
			{
				infoText += restriction.getName() + ", ";
			}
		}
		return infoText;
	}

	private void raiseEvent()
	{
		final Event changeEvent = new Event(Events.ON_CHANGE, this, null);
		Events.postEvent(changeEvent);
	}
}

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
package de.hybris.liveeditaddon.cockpit.multitexteditor;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Image;
import org.zkoss.zul.Textbox;

import de.hybris.liveeditaddon.cockpit.util.NavigationPackHelper;


/**
 * 
 */
public class MultiLanguageTextBox extends de.hybris.platform.cockpit.components.editor.CockpitEditorContainer
{

	public final static String VALUE_MAP = "value_map";
	public final static String DEF_LANG = "defaultLanguage";
	private Textbox defaultText;
	private Image globeimage;
	private Map<String, String> names;
	private String defaultLanguage;

	public MultiLanguageTextBox()
	{
		super();
		names = new HashMap<String, String>(NavigationPackHelper.getDefaultLangMap());

		renderComponent();
		handleActions();
	}

	private void renderComponent()
	{
		defaultText = new Textbox();
		defaultText.setSclass("defaultText");
		this.appendChild(defaultText);

		globeimage = new Image("cockpit/images/language_logo.gif");
		globeimage.setSclass("globeImage");
		this.appendChild(globeimage);
	}

	private void handleActions()
	{
		globeimage.addEventListener(Events.ON_CLICK, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				final Map<String, Object> map = new HashMap<String, Object>();
				map.put(VALUE_MAP, getNames());
				map.put(DEF_LANG, defaultLanguage);
				final Component newWindow = Executions.getCurrent().createComponents("/cmscockpit/MultiLanguageTextEditor.zul", null,
						map);
				newWindow.addEventListener(Events.ON_CLOSE, new EventListener()
				{
					@Override
					public void onEvent(final Event arg0) throws Exception
					{
						defaultText.setText(getNames().get(defaultLanguage));
						raiseEvent();
					}
				});
			}
		});

		defaultText.addEventListener(Events.ON_CHANGE, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				final InputEvent ie = (InputEvent) arg0;
				getNames().put(defaultLanguage, ie.getValue());
				raiseEvent();
			}
		});
	}

	public Map<String, String> getNames()
	{
		if (names == null)
		{
			//might happen when editor was not yet rendered but used in java
			names = new HashMap<String, String>(NavigationPackHelper.getDefaultLangMap());
		}
		return names;
	}

	public void setNames(final Map<String, String> names, final String defaultLanguage)
	{
		this.defaultLanguage = defaultLanguage;
		if (names == null)
		{
			this.names = new HashMap<String, String>();
		}
		else
		{
			this.names = new HashMap<String, String>(names);
		}
		defaultText.setText(getNames().get(defaultLanguage));
		checkForNamesHealth();
	}

	public void setName(final String text)
	{
		names.put(defaultLanguage, text);
		defaultText.setText(text);
		checkForNamesHealth();
	}

	public String getName()
	{
		return getNames().get(defaultLanguage);
	}

	private void raiseEvent()
	{
		final Event changeEvent = new Event(Events.ON_CHANGE, this, null);
		Events.postEvent(changeEvent);
	}

	private void checkForNamesHealth()
	{
		if (names.size() != NavigationPackHelper.getDefaultLangMap().size())
		{
			//internal map can have not enough languages
			for (final String lang : NavigationPackHelper.getDefaultLangMap().keySet())
			{
				if (!names.containsKey(lang))
				{
					names.put(lang, "");
				}
			}
		}
	}
}

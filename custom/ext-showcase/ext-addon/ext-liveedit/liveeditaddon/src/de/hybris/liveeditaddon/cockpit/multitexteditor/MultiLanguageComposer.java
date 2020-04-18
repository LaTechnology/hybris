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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.zkoss.zhtml.Dd;
import org.zkoss.zhtml.Dl;
import org.zkoss.zhtml.Dt;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;


/**
 * 
 */
public class MultiLanguageComposer extends GenericForwardComposer
{
	Div mainArea;
	Map<String, String> values;
	private String defaultLanguage;


	@Override
	public void doAfterCompose(final Component comp) throws Exception
	{
		super.doAfterCompose(comp);

		final Map<String, Object> args = comp.getDesktop().getExecution().getArg();
		values = (Map<String, String>) args.get(MultiLanguageTextBox.VALUE_MAP);
		defaultLanguage = (String) args.get(MultiLanguageTextBox.DEF_LANG);

		//put current lang to the top
		final List<String> list = new ArrayList<String>(values.keySet());
		final int mainIndex = list.indexOf(defaultLanguage);
		if (mainIndex > 0)
		{
			Collections.swap(list, 0, mainIndex);
		}

		final Dl dl = new Dl();
		dl.setParent(mainArea);

		for (final String lang : list)
		{
			final Dt dt = new Dt();
			dt.setParent(dl);
			final Label label = new Label(lang);
			label.setSclass("langLabel");
			dt.appendChild(label);

			final Dd dd = new Dd();
			dd.setParent(dl);
			final Textbox value = new Textbox(values.get(lang));
			dd.appendChild(value);
			value.setSclass("textValue");
			value.addEventListener(Events.ON_CHANGE, new EventListener()
			{
				@Override
				public void onEvent(final Event arg0) throws Exception
				{
					final InputEvent ie = (InputEvent) arg0;
					values.put(lang, ie.getValue());
				}
			});
		}
	}
}

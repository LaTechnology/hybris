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
package de.hybris.liveeditaddon.cockpit.navigationeditor.composers;

import de.hybris.platform.cockpit.constants.ImageUrls;

import java.util.Collections;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import de.hybris.liveeditaddon.enums.CMSMenuItemType;
import de.hybris.liveeditaddon.cockpit.multitexteditor.MultiLanguageTextBox;
import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationLinkViewModel;
import de.hybris.liveeditaddon.cockpit.util.NavigationPackHelper;


/**
 * 
 */
public class ArbitraryLinkEditorComposer extends GenericForwardComposer
{
	private Textbox linkURL;
	private Groupbox arbitraryLinksBox;
	private MultiLanguageTextBox linkTitle;
	private String defaultLanguage;

	@Override
	public void doAfterCompose(final Component comp) throws Exception
	{
		super.doAfterCompose(comp);
		final Map<String, Object> args = comp.getDesktop().getExecution().getArg();
		defaultLanguage = (String) args.get(MultiLanguageTextBox.DEF_LANG);
		linkTitle.setNames(NavigationPackHelper.getDefaultLangMap(), defaultLanguage);
	}

	@SuppressWarnings("PMD")
	public void onClick$addButton(final ForwardEvent event)
	{
		final NavigationLinkViewModel navigationLink = new NavigationLinkViewModel(defaultLanguage);
		setValuesToModelFromInput(navigationLink);
		renderNewLinkInGroupbox(navigationLink);
		cleanTypedValues();
	}

	private void renderNewLinkInGroupbox(final NavigationLinkViewModel navigationLink)
	{
		final Hbox facetBox = new Hbox();
		facetBox.setDraggable("link");
		facetBox.setAttribute("link_value", navigationLink);
		facetBox.setParent(arbitraryLinksBox);
		final Label facetName = new Label(navigationLink.getName());
		final Button delButton = new Button();
		delButton.setImage(ImageUrls.REMOVE_BUTTON_IMAGE);
		delButton.addEventListener(Events.ON_CLICK, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				facetBox.detach();
			}
		});
		facetName.setParent(facetBox);
		delButton.setParent(facetBox);
		facetBox.setParent(arbitraryLinksBox);
	}

	private void setValuesToModelFromInput(final NavigationLinkViewModel navigationLink)
	{
		navigationLink.setMenuItemType(CMSMenuItemType.ARBITRARY_LINK);
		navigationLink.setNames(linkTitle.getNames());
		navigationLink.setURL(linkURL.getText());
	}

	private void cleanTypedValues()
	{
		linkTitle.setNames(Collections.EMPTY_MAP, defaultLanguage);
		linkURL.setText("");
	}
}

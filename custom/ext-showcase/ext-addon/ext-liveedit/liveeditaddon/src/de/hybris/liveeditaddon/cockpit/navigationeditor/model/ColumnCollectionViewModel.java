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
package de.hybris.liveeditaddon.cockpit.navigationeditor.model;

import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

import de.hybris.liveeditaddon.cockpit.multitexteditor.MultiLanguageTextBox;
import de.hybris.liveeditaddon.cockpit.navigationeditor.composers.SingleColumnComposer;
import de.hybris.liveeditaddon.cockpit.restrictioneditor.RestrictionEditor;
import de.hybris.liveeditaddon.cockpit.util.NavigationPackHelper;



/**
 * 
 */
public class ColumnCollectionViewModel
{
	private final String newColumnText = Labels.getLabel("cmscockpit.navigationwizard.newcolumn");//original value of column name

	private MultiLanguageTextBox selectedColumnTitle;
	private NavigationColumnViewModel currentColumn;

	private Label selectedLinkName;
	private MultiLanguageTextBox selectedLinkTitle;
	private Textbox selectedLinkURL;

	private Label currentColumnName;
	private NavigationLinkViewModel currentColumnLink;
	private Listitem currentListItem;

	private NavigationNodeViewModel node;
	RestrictionEditor restrictionsEditor;

	public void registerColumnFields(final MultiLanguageTextBox selectedColumnTitle)
	{
		this.selectedColumnTitle = selectedColumnTitle;
		this.selectedColumnTitle.addEventListener(Events.ON_CHANGE, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				final MultiLanguageTextBox box = (MultiLanguageTextBox) arg0.getTarget();
				if (currentColumnName != null)
				{
					currentColumnName.setValue("column '" + box.getName() + "'");
				}
				if (currentColumn != null)
				{
					currentColumn.setName(box.getName());
				}
			}
		});
	}

	public void registerLinkFields(final Label selectedLinkName, final MultiLanguageTextBox linkTitle,
			final Textbox selectedLinkURL, final RestrictionEditor linkRestrictions)
	{
		this.selectedLinkName = selectedLinkName;
		this.selectedLinkTitle = linkTitle;
		this.selectedLinkURL = selectedLinkURL;
		this.restrictionsEditor = linkRestrictions;

		this.selectedLinkTitle.addEventListener(Events.ON_CHANGE, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				final MultiLanguageTextBox container = (MultiLanguageTextBox) arg0.getTarget();
				if (currentColumnLink != null)
				{
					currentColumnLink.setName(getNode().getLiveEditView().getCurrentPreviewData().getLanguage().getIsocode(),
							container.getName());
				}
				if (currentListItem != null)
				{
					currentListItem.setLabel(container.getName());
				}
			}
		});

		this.selectedLinkURL.addEventListener(Events.ON_CHANGE, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				final InputEvent ie = (InputEvent) arg0;
				if (currentColumnLink != null)
				{
					currentColumnLink.setURL(ie.getValue());
				}
			}
		});
		restrictionsEditor.addEventListener(Events.ON_CHANGE, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				if (currentColumnLink != null)
				{
					currentColumnLink.setRestrictions(restrictionsEditor.getRestrictions());
				}
			}
		});
	}

	public void setCurrentColumn(final NavigationColumnViewModel columnModel, final Label columnLink)
	{
		this.currentColumn = columnModel;
		this.currentColumnName = columnLink;
		selectedColumnTitle.setFocus(true);
		if (!columnLink.getValue().equals(newColumnText))
		{
			String value = columnLink.getValue().replace("column '", "");
			value = value.replace("'", "");
			final Map<String, String> attribute = (Map<String, String>) columnLink.getAttribute(SingleColumnComposer.ALL_NAMES);
			selectedColumnTitle.setNames(attribute, getCurrentLanguage());
			selectedColumnTitle.setName(value.trim());
		}
		else
		{
			selectedColumnTitle.setNames(NavigationPackHelper.getDefaultLangMap(), getCurrentLanguage());
		}

		//if clicked node is new the add to current navigation node
		if (!getNode().getNavigationNodeColumns().contains(columnModel))
		{
			getNode().getNavigationNodeColumns().add(columnModel);
		}

		//clean clicked link info
		selectedLinkName.setValue("");
		selectedLinkTitle.setNames(NavigationPackHelper.getDefaultLangMap(), getCurrentLanguage());
		selectedLinkURL.setText("");
	}

	private String getCurrentLanguage()
	{
		return node.getLiveEditView().getCurrentPreviewData().getLanguage().getIsocode();
	}

	public void setCurrentColumnLink(final Listitem item)
	{
		final NavigationLinkViewModel link = (NavigationLinkViewModel) item.getValue();
		this.currentColumn = link.getColumn();
		selectedColumnTitle.setNames(link.getColumn().getNames(), getCurrentLanguage());

		currentListItem = item;
		currentColumnLink = link;
		selectedLinkTitle.setFocus(true);

		selectedLinkName.setValue(Labels.getLabel("cmscockpit.navigationwizard.selectedlink") + " ( " + link.getName() + ")");
		selectedLinkTitle.setNames(link.getNames(), getCurrentLanguage());

		handleURL(link);
		restrictionsEditor.setRestrictions(link.getRestrictions());
	}

	private void handleURL(final NavigationLinkViewModel link)
	{
		if (StringUtils.isBlank(link.getURL()))
		{
			selectedLinkURL.setText(NavigationPackHelper.showAlternativeUrl(link));
		}
		else
		{
			selectedLinkURL.setText(link.getURL());
		}
	}

	public NavigationNodeViewModel getNode()
	{
		return node;
	}

	public void setNode(final NavigationNodeViewModel navigationNodeViewModel)
	{
		this.node = navigationNodeViewModel;
	}

	public void setRestrictions(final Collection<AbstractRestrictionModel> models)
	{
		if (currentColumnLink != null)
		{
			currentColumnLink.setRestrictions(models);
		}

	}
}

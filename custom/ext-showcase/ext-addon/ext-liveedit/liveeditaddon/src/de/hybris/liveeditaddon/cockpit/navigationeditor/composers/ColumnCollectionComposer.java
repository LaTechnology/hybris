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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import de.hybris.liveeditaddon.cockpit.multitexteditor.MultiLanguageTextBox;
import de.hybris.liveeditaddon.cockpit.navigationeditor.model.ColumnCollectionViewModel;
import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationColumnViewModel;
import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationLinkCollectionViewModel;
import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationLinkViewModel;
import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationNodeTabViewModel;
import de.hybris.liveeditaddon.cockpit.restrictioneditor.RestrictionEditor;
import de.hybris.liveeditaddon.cockpit.util.NavigationPackHelper;


/**
 * 
 */
public class ColumnCollectionComposer extends GenericForwardComposer
{
	public static final String WIZARD_ARG = "wizardModel";
	public static final String COLUMN_MODEL = "columnsLinks";

	private MultiLanguageTextBox selectedColumnTitle;

	private Label selectedLinkName;
	private MultiLanguageTextBox linkTitle;

	private Textbox selectedLinkURL;

	private Label newColumnLink;

	private Div columnCollection;
	private Listbox trashedList;

	private Div categoryColumns;

	private RestrictionEditor linkRestrictions;
	private final Collection<Component> columns = new ArrayList<Component>();
	private final ColumnCollectionViewModel collectionModel = new ColumnCollectionViewModel();
	private Collection<NavigationColumnViewModel> columnModels = new ArrayList<NavigationColumnViewModel>();

	private NavigationNodeTabViewModel model;

	@Override
	public void doAfterCompose(final Component comp) throws Exception
	{
		super.doAfterCompose(comp);
		final Map<String, Object> args = comp.getDesktop().getExecution().getArg();
		columnModels = (Collection<NavigationColumnViewModel>) args.get(NavigationEditorComposer.NAVIGATION_COLUMNS);
		model = (NavigationNodeTabViewModel) args.get(WIZARD_ARG);

		renderExistingColumns();
		collectionModel.registerColumnFields(selectedColumnTitle);

		if (model != null)
		{
			collectionModel.setNode(model.getNavigationNode());
		}
		newColumnActionHandle();
		handleDropToTrash();

		categoryColumns.addEventListener(Events.ON_DROP, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				NavigationLinkCollectionViewModel coll = null;
				final DropEvent de = (DropEvent) arg0;
				if (de.getDragged().getAttribute("link_value") != null)
				{
					coll = (NavigationLinkCollectionViewModel) de.getDragged().getAttribute("link_value");
					NavigationPackHelper.cleanFacetValuesNames(coll.getNavLinks());
					renderNewColumn(coll);
				}
			}
		});
		collectionModel.registerLinkFields(selectedLinkName, linkTitle, selectedLinkURL, linkRestrictions);

	}

	private void handleDropToTrash()
	{
		trashedList.addEventListener(Events.ON_DROP, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				final DropEvent de = (DropEvent) arg0;

				NavigationLinkViewModel link = null;
				if (de.getDragged() instanceof Listitem)
				{
					final Listitem item = (Listitem) de.getDragged();
					link = (NavigationLinkViewModel) item.getValue();
				}
				if (de.getDragged() instanceof Treerow)
				{
					final Treerow treerow = (Treerow) de.getDragged();
					final Treeitem item = (Treeitem) treerow.getParent();
					link = (NavigationLinkViewModel) item.getValue();
				}
				if (link != null && columnModels != null)
				{
					//remove
					link.delete();
				}

			}
		});
	}

	private void renderExistingColumns()
	{
		for (final NavigationColumnViewModel column : columnModels)
		{
			final Map<String, Object> map = new HashMap<String, Object>();
			map.put(WIZARD_ARG, collectionModel);
			map.put(COLUMN_MODEL, column);
			//yes,we should go deeper
			final Component createComponents = Executions.getCurrent().createComponents("/cmscockpit/SingleColumnDesigner.zul",
					columnCollection, map);
			column.setComponent(createComponents);
			getColumns().add(createComponents);
		}
	}


	private void newColumnActionHandle()
	{
		newColumnLink.addEventListener(Events.ON_CLICK, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				renderNewColumn(null);
			}
		});
	}

	private void renderNewColumn(final NavigationLinkCollectionViewModel coll)
	{
		final NavigationColumnViewModel columnModel = new NavigationColumnViewModel();

		if (coll != null)
		{
			for (final NavigationLinkViewModel link : coll.getNavLinks())
			{
				link.setColumn(columnModel);
			}
			columnModel.setNavigationLinks(coll.getNavLinks());
			columnModel.setNames(coll.getColumnName());
		}
		columnModel.setParentNavigationNode(model.getNavigationNode());
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put(WIZARD_ARG, collectionModel);
		map.put(COLUMN_MODEL, columnModel);
		//yes,we should go deeper
		final Component createComponents = Executions.getCurrent().createComponents("/cmscockpit/SingleColumnDesigner.zul",
				columnCollection, map);
		columnModel.setComponent(createComponents);
		getColumns().add(createComponents);

		//add dropped node to  current navigation node
		model.getNavigationNode().getNavigationNodeColumns().add(columnModel);
	}

	public Collection<Component> getColumns()
	{
		return columns;
	}
}

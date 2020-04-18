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

import de.hybris.liveeditaddon.cockpit.navigationeditor.elements.NavigationParentElement;
import de.hybris.liveeditaddon.cockpit.navigationeditor.factory.NavigationParentFactory;
import de.hybris.liveeditaddon.cockpit.services.NavigationEditorService;
import de.hybris.platform.cockpit.constants.ImageUrls;
import de.hybris.platform.cockpit.model.meta.TypedObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationEditorViewModel;
import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationNodeTabViewModel;
import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationNodeViewModel;
import de.hybris.liveeditaddon.cockpit.navigationeditor.wizard.NavigationEditorWizard;


/**
 * 
 */
public class NavigationEditorComposer extends GenericForwardComposer
{
	private Tabbox tabbox;
	private NavigationEditorViewModel model;
	public static final String WIZARD_ARG = "wizardModel";
	public static final String NAVIGATION_COLUMNS = "navigationColumns";
	private Collection<NavigationNodeTabViewModel> navigations = new ArrayList<NavigationNodeTabViewModel>();
	private final String zulPath = "/cmscockpit/NavigationItemEditorComposer.zul";
	private Button btnPublish;
	private final BiMap<Tab, NavigationNodeTabViewModel> tabToModels = HashBiMap.create();
	private Collection<TypedObject> itemsToSynchronize = new ArrayList<TypedObject>();

	private NavigationNodeTabViewModel currentModel = null;
	private NavigationEditorService navigationEditorService;
	private NavigationParentElement parentElement;

	@Override
	public void doAfterCompose(final Component comp) throws Exception
	{
		super.doAfterCompose(comp);
		final Map<String, Object> args = comp.getDesktop().getExecution().getArg();
		model = (NavigationEditorViewModel) args.get(NavigationEditorWizard.WIZARD_ARG);
		navigationEditorService = (NavigationEditorService) args.get(NavigationEditorWizard.SERVICE_ARG);
		parentElement = NavigationParentFactory.build(navigationEditorService, model.getComponentUID(), model.getContentSlotUID(),
				model.getLiveEditViewModel());
		navigations = navigationEditorService.getNodesForCurrentVersion(model, parentElement);
		renderTabs();
	}

	private void renderTabs()
	{
		setSelection();
		for (final NavigationNodeTabViewModel nodeModel : navigations)
		{
			if (nodeModel.getNavigationNode().getName() != null)//workaround for not showing odd elements
			{
				renderSingleTab(nodeModel);
			}
		}

		//new item tab
		renderCreationTab();
	}

	private void setSelection()
	{
		NavigationNodeTabViewModel modelToSelect = null;
		boolean stillSearchin = true;
		for (final NavigationNodeTabViewModel nodeModel : navigations)
		{
			if (StringUtils.isNotBlank(nodeModel.getNavigationNode().getName()))//workaround for not showing odd elements
			{
				final Tab tab = new Tab(nodeModel.getNavigationNode().getName());
				tabToModels.put(tab, nodeModel);
				tabbox.getTabs().appendChild(tab);

				if (stillSearchin)//we cannot break this loop due to code above
				{
					if (model.getComponentUID().equals(nodeModel.getNavigationNode().getUid()))
					{
						modelToSelect = nodeModel;
					}
					if (currentModel != null && currentModel == nodeModel)//current tab have higher priority for selection than clicked node
					{
						modelToSelect = nodeModel;
						stillSearchin = false;
					}
				}
			}
		}

		final Tab tab = tabToModels.inverse().get(modelToSelect);
		if (tab != null)
		{
			tabbox.setSelectedTab(tab);
		}
	}

	private void renderCreationTab()
	{
		final NavigationNodeViewModel navigationNodeViewModel = new NavigationNodeViewModel(model.getLiveEditViewModel(),
				navigationEditorService.getNavigationBarContentSlot(model.getContentSlotUID(), model.getLiveEditViewModel()));
		final NavigationNodeTabViewModel creationModel = new NavigationNodeTabViewModel(navigationNodeViewModel);
		navigations.add(creationModel);

		creationModel.setServerPath(model.getServerPath());
		creationModel.setNavigationService(navigationEditorService);
		final Tab tab1 = new Tab("", ImageUrls.GREEN_ADD_PLUS_IMAGE);
		tabToModels.put(tab1, creationModel);
		tabbox.getTabs().appendChild(tab1);

		final Tabpanel panel1 = new Tabpanel();
		tabbox.getTabpanels().appendChild(panel1);

		Executions.getCurrent().createComponents(zulPath, panel1, Collections.singletonMap(WIZARD_ARG, creationModel));
	}

	private void renderSingleTab(final NavigationNodeTabViewModel model)
	{
		final Tab tab = tabToModels.inverse().get(model);
		if (tab == null)
		{
			return;
		}
		tab.setClosable(true);
		tab.setDraggable("tab");
		tab.setDroppable("tab");
		handleClosing(model, tab);
		final Tabpanel panel1 = new Tabpanel();
		tabbox.getTabpanels().appendChild(panel1);

		tab.addEventListener(Events.ON_DROP, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				final DropEvent de = (DropEvent) arg0;
				if (de.getDragged() instanceof Tab)
				{
					final Tab tab = (Tab) de.getDragged();
					tab.getParent().insertBefore(tab, de.getTarget());

					final NavigationNodeTabViewModel draggedModel = tabToModels.get(tab);
					final NavigationNodeTabViewModel targetModel = tabToModels.get(de.getTarget());
					navigations = model.getNavigationService().insertBefore(draggedModel, targetModel, parentElement, navigations);
				}
			}
		});

		Executions.getCurrent().createComponents(zulPath, panel1, Collections.singletonMap(WIZARD_ARG, model));
	}

	private void handleClosing(final NavigationNodeTabViewModel model, final Tab tab)
	{
		tab.addEventListener(Events.ON_CLOSE, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				if (Messagebox.show("Are you sure?", "Confirm", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES)
				{
					navigations.remove(model);
					model.getNavigationService().removeNavigation(model);
				}
				// workaround-start  TODO fix that-maybe stop event if don't close tab 
				tabbox.getTabs().getChildren().clear();
				tabbox.getTabpanels().getChildren().clear();
				tabToModels.clear();
				renderTabs();
				//workaround-end
			}
		});
	}

	@SuppressWarnings("PMD")
	public void onClick$btnPublish(final ForwardEvent event)
	{
		Clients.showBusy("busy.sync", true);
		navigationEditorService.performSynchronization(itemsToSynchronize);
		btnPublish.setVisible(false);
		Clients.showBusy(null, false);
	}

	@SuppressWarnings("PMD")
	public void onClick$btnSave(final ForwardEvent event)
	{
		currentModel = tabToModels.get(tabbox.getSelectedTab());
		itemsToSynchronize = navigationEditorService.saveModels(navigations, parentElement);
		//refresh
		tabbox.getTabs().getChildren().clear();
		tabbox.getTabpanels().getChildren().clear();
		tabToModels.clear();
		renderTabs();
		if (navigationEditorService.isSynchronizePossible(itemsToSynchronize))
		{
			btnPublish.setVisible(true);
		}
	}

}

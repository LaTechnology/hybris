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

import java.util.Collection;
import java.util.Map;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vbox;

import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationLinkCollectionViewModel;
import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationLinkViewModel;
import de.hybris.liveeditaddon.cockpit.navigationeditor.wizard.NavigationEditorWizard;
import de.hybris.liveeditaddon.cockpit.services.NavigationStateViewModel;


/**
 * 
 */
public class NavigationStateComposer extends GenericForwardComposer
{

	private static final String TAB_VAR_NAME = "tab_value";
	private static final String LINK_VAR_NAME = "link_value";
	public static final String WIZARD_ARG = "wizardModel";

	public static final String NAV_STATE_TAB_CSS = "navigationstate-tab";
	public static final String NAV_STATE_LABEL_CSS = "navigationstate-label";
	public static final String NAV_STATE_BUTTON_CSS = "navigationstate-del-button";
	public static final String NAV_STATE_NOFACETS_CSS = "navigationstate-no-facets-label";

	private Div facetsTabsDiv;
	private Div appliedFacetsWrapper;

	private NavigationStateViewModel model;

	@Override
	public void doAfterCompose(final Component comp) throws Exception
	{
		super.doAfterCompose(comp);
		final Map<String, Object> args = comp.getDesktop().getExecution().getArg();
		model = (NavigationStateViewModel) args.get(NavigationEditorWizard.WIZARD_ARG);


		renderAppliedFacetsBox();
		renderSolrIndexedPropsTabPanels();
	}

	public void renderSolrIndexedPropsTabPanels()
	{
		facetsTabsDiv.getChildren().clear();
		model.afterFacetsApplied();
		final Collection<NavigationLinkCollectionViewModel> facetSearchNavNodesCollections = model.getCurrentNavLinkCollection();
		for (final NavigationLinkCollectionViewModel navCollModel : facetSearchNavNodesCollections)
		{
			final Groupbox gb = new Groupbox();
			gb.setDraggable("facet");
			gb.setAttribute(LINK_VAR_NAME, navCollModel);
			new Caption(navCollModel.getName()).setParent(gb);
			gb.setOpen(false);
			facetsTabsDiv.appendChild(gb);
			gb.appendChild(renderSolrIndexedPropsTabPanel(navCollModel));
		}
	}

	private Div renderSolrIndexedPropsTabPanel(final NavigationLinkCollectionViewModel navLinkColl)
	{
		final Div tmpPanel = new Div();
		tmpPanel.setAttribute(TAB_VAR_NAME, navLinkColl);
		//Adding labels
		final Vbox vPanel = new Vbox();
		vPanel.setParent(tmpPanel);

		int counter = 0;
		for (final NavigationLinkViewModel navLink : navLinkColl.getNavLinks())
		{
			counter++;
			final Label tmpLabel = new Label();
			tmpLabel.setDraggable("link");
			tmpLabel.setParent(vPanel);
			if (counter % 2 == 0)
			{
				tmpLabel.setClass(NAV_STATE_LABEL_CSS + " evenRow");
			}
			else
			{
				tmpLabel.setClass(NAV_STATE_LABEL_CSS + " oddRow");
			}
			tmpLabel.setValue(navLink.getName());
			tmpLabel.setAttribute(LINK_VAR_NAME, navLink);
			tmpLabel.addEventListener(Events.ON_CLICK, new EventListener()
			{
				@Override
				public void onEvent(final Event arg0) throws Exception
				{
					//Rendering boxes again
					model.applyFacet(navLink);
					renderAppliedFacetsBox();
					renderSolrIndexedPropsTabPanels();
				}
			});
		}
		return tmpPanel;
	}

	/**
	 * Rendering box with applied facets
	 */
	public void renderAppliedFacetsBox()
	{
		appliedFacetsWrapper.getChildren().clear(); //Render again
		final Collection<NavigationLinkViewModel> appliedFacets = model.getAppliedFacets();
		if (appliedFacets.size() == 0)
		{
			final Label noFacetsLabel = new Label();
			noFacetsLabel.setClass(NAV_STATE_NOFACETS_CSS);
			noFacetsLabel.setValue(Labels.getLabel("cmscockpit.nofacetsapplied"));
			noFacetsLabel.setParent(appliedFacetsWrapper);
		}
		else
		{
			for (final NavigationLinkViewModel navigationLink : appliedFacets)
			{
				final Hbox facetBox = new Hbox();
				facetBox.setDraggable("link");
				facetBox.setAttribute(LINK_VAR_NAME, navigationLink);
				facetBox.setParent(appliedFacetsWrapper);
				final Label facetName = new Label(navigationLink.getName());
				final Button delButton = new Button();
				delButton.setImage(ImageUrls.REMOVE_BUTTON_IMAGE);
				delButton.setClass(NAV_STATE_BUTTON_CSS);
				delButton.addEventListener(Events.ON_CLICK, new EventListener()
				{
					@Override
					public void onEvent(final Event arg0) throws Exception
					{
						model.removeAppliedFacet(navigationLink);
						renderAppliedFacetsBox();
						renderSolrIndexedPropsTabPanels();
					}
				});
				facetName.setParent(facetBox);
				delButton.setParent(facetBox);
				facetBox.setParent(appliedFacetsWrapper);
			}
		}
	}
}
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
package de.hybris.liveeditaddon.cockpit.services.impl;

import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditViewModel;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.core.Registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationLinkCollectionViewModel;
import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationLinkViewModel;
import de.hybris.liveeditaddon.cockpit.services.FacetSearchDataService;
import de.hybris.liveeditaddon.cockpit.services.NavigationEditorService;
import de.hybris.liveeditaddon.cockpit.services.NavigationStateViewModel;
import de.hybris.liveeditaddon.cockpit.util.NavigationPackHelper;


/**
 *
 */
public class DefaultNavigationStateViewModel implements NavigationStateViewModel
{
	private NavigationEditorService navigationService;
	private FacetSearchDataService cmsFacetSearchDataService;
	private CMSAdminSiteService cmsAdminSiteService;

	private final List<NavigationLinkViewModel> appliedFacets = new ArrayList<>();
	private final Collection<NavigationLinkCollectionViewModel> currentNavLinkCollections = new ArrayList<>();
	private LiveEditViewModel liveEditView;

    public DefaultNavigationStateViewModel(NavigationEditorService navigationService, FacetSearchDataService cmsFacetSearchDataService, CMSAdminSiteService cmsAdminSiteService)
    {
        this.navigationService = navigationService;
        this.cmsFacetSearchDataService = cmsFacetSearchDataService;
        this.cmsAdminSiteService = cmsAdminSiteService;
    }

    public void setLiveEditView(LiveEditViewModel liveEditView) {
        this.liveEditView = liveEditView;
    }
	@Override
	public Collection<NavigationLinkViewModel> getAppliedFacets()
	{
		return appliedFacets;
	}

	@Override
	public void applyFacet(final NavigationLinkViewModel navLinkModel)
	{
		if (navLinkModel != null)
		{
			if (!appliedFacets.contains(navLinkModel))
			{
				appliedFacets.add(navLinkModel);
				afterFacetsApplied();
			}
		}
	}

	@Override
	public void removeAppliedFacet(final NavigationLinkViewModel navLinkModel)
	{
		if (navLinkModel != null && appliedFacets.contains(navLinkModel))
		{
			appliedFacets.remove(navLinkModel);
			afterFacetsApplied();
		}
	}

	@Override
	public void afterFacetsApplied()
	{
		prepareSolrSearch();
	}

	protected void prepareSolrSearch()
	{
		if (appliedFacets.size() == 0)
		{
			final SearchStateData searchState = new SearchStateData();
			final SearchQueryData searchQuery = new SearchQueryData();
			searchQuery.setValue("");
			searchState.setUrl("/");
			searchState.setQuery(searchQuery);
			final Collection<NavigationLinkCollectionViewModel> facetQuerySerarchNavNodes = cmsFacetSearchDataService.getFacetQuerySearchNavNodes(liveEditView.getSite(), liveEditView.getCurrentPreviewData().getLanguage(),
							liveEditView.getSite().getStores().get(0).getDefaultCurrency(), searchState);
			currentNavLinkCollections.clear();
			currentNavLinkCollections.addAll(facetQuerySerarchNavNodes);
		}
		else
		{
			//Getting last SOLR facet value
			final NavigationLinkViewModel navigationLinkViewModel = appliedFacets.get(appliedFacets.size() - 1);
			final SearchStateData searchState = new SearchStateData();
			final SearchQueryData searchQuery = new SearchQueryData();
			searchQuery.setValue(NavigationPackHelper.getSolrQueryFromNavLinkModel(navigationLinkViewModel));
			searchState.setUrl(navigationLinkViewModel.getURL());
			searchState.setQuery(searchQuery);

			final Collection<NavigationLinkCollectionViewModel> facetQuerySerarchNavNodes = cmsFacetSearchDataService.getFacetQuerySearchNavNodes(liveEditView.getSite(), liveEditView.getCurrentPreviewData().getLanguage(),
							liveEditView.getSite().getStores().get(0).getDefaultCurrency(), searchState);
			currentNavLinkCollections.clear();
			currentNavLinkCollections.addAll(facetQuerySerarchNavNodes);
		}
	}


	@Override
	public Collection<NavigationLinkCollectionViewModel> getCurrentNavLinkCollection()
	{
		return currentNavLinkCollections;
	}


	protected String buildSolrTextSearchQuery()
	{
		return "";
	}



}

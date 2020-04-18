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
package de.hybris.liveeditaddon.cockpit.services;

import java.util.Collection;

import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationLinkCollectionViewModel;
import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationLinkViewModel;


/**
 * View model for navigation state mode
 * 
 * 
 */
public interface NavigationStateViewModel
{

	/**
	 * Getting all applied facets
	 * 
	 * @return List<NavigationLinkViewModel>
	 */
	Collection<NavigationLinkViewModel> getAppliedFacets();

	void applyFacet(final NavigationLinkViewModel navLinkModel);

	void removeAppliedFacet(final NavigationLinkViewModel linkModel);

	/**
	 * Operations on component model after facets applied/removed.
	 */
	void afterFacetsApplied();


	/**
	 * Getting current NavigationLinkCollectionViewModel collection (for facet tabs generation puropses)
	 * 
	 * @return Collection<NavigationLinkCollectionViewModel>
	 */
	Collection<NavigationLinkCollectionViewModel> getCurrentNavLinkCollection();


}

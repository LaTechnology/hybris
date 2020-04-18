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

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;

import java.util.Collection;

import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationLinkCollectionViewModel;


/**
 * Facet data service
 * 
 * 
 */
public interface FacetSearchDataService
{

	/**
	 * The same operation as in getFacetCategorySearch, but result converted to collection of
	 * NavigationLinkCollectionViewModel
	 * 
	 * @return Collection<NavigationLinkCollectionViewModel>
	 */
	Collection<NavigationLinkCollectionViewModel> getFacetQuerySearchNavNodes(final BaseSiteModel baseSite,
			final LanguageModel language, final CurrencyModel currency, final SearchStateData searchStateData);


}

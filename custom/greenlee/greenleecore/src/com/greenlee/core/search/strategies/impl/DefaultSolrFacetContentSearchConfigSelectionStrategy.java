package com.greenlee.core.search.strategies.impl;

/**
 * @author aruna
 *
 */


import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.SolrFacetSearchConfigSelectionStrategy;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.exceptions.NoValidSolrConfigException;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import org.springframework.beans.factory.annotation.Autowired;


public class DefaultSolrFacetContentSearchConfigSelectionStrategy implements SolrFacetSearchConfigSelectionStrategy
{

	@Autowired
	private BaseSiteService baseSiteService;
	@Autowired
	private BaseStoreService baseStoreService;


	@Override
	public SolrFacetSearchConfigModel getCurrentSolrFacetSearchConfig() throws NoValidSolrConfigException
	{
		SolrFacetSearchConfigModel result = getSolrConfigForBaseSite();
		if (result == null)
		{
			result = getSolrConfigForBaseStore();
		}
		if (result == null)
		{
			throw new NoValidSolrConfigException(
					"No Valid SolrFacetSearchConfig configured neither for base site/base store/session product catalog versions.");
		}
		return result;
	}

	protected SolrFacetSearchConfigModel getSolrConfigForBaseStore()
	{
		final BaseStoreModel currentBaseStore = baseStoreService.getCurrentBaseStore();
		if (currentBaseStore != null)
		{
			return currentBaseStore.getSolrFacetContentSearchConfiguration();
		}
		return null;
	}

	protected SolrFacetSearchConfigModel getSolrConfigForBaseSite()
	{
		final BaseSiteModel currentBaseSite = baseSiteService.getCurrentBaseSite();
		if (currentBaseSite != null)
		{
			return currentBaseSite.getSolrFacetContentSearchConfiguration();
		}
		return null;
	}


}

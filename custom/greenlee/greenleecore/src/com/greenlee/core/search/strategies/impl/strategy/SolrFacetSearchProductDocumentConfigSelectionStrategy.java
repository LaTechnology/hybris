/**
 *
 */
package com.greenlee.core.search.strategies.impl.strategy;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.SolrFacetSearchConfigSelectionStrategy;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.exceptions.NoValidSolrConfigException;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;


/**
 * @author aruna
 *
 */
public class SolrFacetSearchProductDocumentConfigSelectionStrategy implements SolrFacetSearchConfigSelectionStrategy
{
	private BaseSiteService baseSiteService;
	private BaseStoreService baseStoreService;


	/**
	 * @return the baseSiteService
	 */
	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	/**
	 * @param baseSiteService
	 *           the baseSiteService to set
	 */
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	/**
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.SolrFacetSearchConfigSelectionStrategy#
	 * getCurrentSolrFacetSearchConfig()
	 */
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
			return currentBaseStore.getSolrFacetProductDocumentSearchConfiguration();
		}
		return null;
	}

	protected SolrFacetSearchConfigModel getSolrConfigForBaseSite()
	{
		final BaseSiteModel currentBaseSite = baseSiteService.getCurrentBaseSite();
		if (currentBaseSite != null)
		{
			return currentBaseSite.getSolrFacetProductDocumentSearchConfiguration();
		}
		return null;
	}
}

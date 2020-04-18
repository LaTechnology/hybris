/**
 *
 */
package com.greenlee.core.solrfacetsearch.populators;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.exceptions.NoValidSolrConfigException;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.search.FacetSearchService;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchQuery.Operator;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.greenlee.core.search.strategies.impl.strategy.SolrFacetSearchProductDocumentConfigSelectionStrategy;


/**
 * @author aruna
 *
 */
public class ProductDocumentSearchSolrQueryPopulator<INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE> implements
		Populator<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest<FacetSearchConfig, IndexedType, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE>>
{
	/**
	 *
	 */
	private static final String MISSING_SOLR_FACET_SEARCH_INDEXED_CATALOG_VERSIONS = "Missing solr facet search indexed catalog versions";
	/**
	 *
	 */
	private static final String NO_VALID_SOLR_FACET_SEARCH_CONFIG_FOUND_FOR_THE_CURRENT_CONTEXT = "No valid solrFacetSearchConfig found for the current context";
	private CommonI18NService commonI18NService;
	private BaseSiteService baseSiteService;
	private BaseStoreService baseStoreService;
	private CatalogVersionService catalogVersionService;
	private FacetSearchService facetSearchService;
	private FacetSearchConfigService facetSearchConfigService;
	private CMSSiteService cmsSiteService;
	private SolrFacetSearchProductDocumentConfigSelectionStrategy solrFacetSearchProductDocumentConfigSelectionStrategy;

	/**
	 * @return the commonI18NService
	 */
	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	/**
	 * @return the solrFacetSearchProductDocumentConfigSelectionStrategy
	 */
	public SolrFacetSearchProductDocumentConfigSelectionStrategy getSolrFacetSearchProductDocumentConfigSelectionStrategy()
	{
		return solrFacetSearchProductDocumentConfigSelectionStrategy;
	}

	/**
	 * @param solrFacetSearchProductDocumentConfigSelectionStrategy
	 *           the solrFacetSearchProductDocumentConfigSelectionStrategy to set
	 */
	public void setSolrFacetSearchProductDocumentConfigSelectionStrategy(
			final SolrFacetSearchProductDocumentConfigSelectionStrategy solrFacetSearchProductDocumentConfigSelectionStrategy)
	{
		this.solrFacetSearchProductDocumentConfigSelectionStrategy = solrFacetSearchProductDocumentConfigSelectionStrategy;
	}

	/**
	 * @param commonI18NService
	 *           the commonI18NService to set
	 */
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

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

	/**
	 * @return the catalogVersionService
	 */
	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	/**
	 * @param catalogVersionService
	 *           the catalogVersionService to set
	 */
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	/**
	 * @return the facetSearchService
	 */
	public FacetSearchService getFacetSearchService()
	{
		return facetSearchService;
	}

	/**
	 * @param facetSearchService
	 *           the facetSearchService to set
	 */
	public void setFacetSearchService(final FacetSearchService facetSearchService)
	{
		this.facetSearchService = facetSearchService;
	}

	/**
	 * @return the facetSearchConfigService
	 */
	public FacetSearchConfigService getFacetSearchConfigService()
	{
		return facetSearchConfigService;
	}

	/**
	 * @param facetSearchConfigService
	 *           the facetSearchConfigService to set
	 */
	public void setFacetSearchConfigService(final FacetSearchConfigService facetSearchConfigService)
	{
		this.facetSearchConfigService = facetSearchConfigService;
	}

	/**
	 * @return the cmsSiteService
	 */
	public CMSSiteService getCmsSiteService()
	{
		return cmsSiteService;
	}

	/**
	 * @param cmsSiteService
	 *           the cmsSiteService to set
	 */
	public void setCmsSiteService(final CMSSiteService cmsSiteService)
	{
		this.cmsSiteService = cmsSiteService;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final SearchQueryPageableData<SolrSearchQueryData> source,
			final SolrSearchRequest<FacetSearchConfig, IndexedType, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE> target)
					throws ConversionException
	{
		// YTODO Auto-generated method stub
		// Setup the SolrSearchRequest
		target.setSearchQueryData(source.getSearchQueryData());
		target.setPageableData(source.getPageableData());

		final Collection<CatalogVersionModel> catalogVersions = getSessionProductCatalogVersions();
		if (catalogVersions == null || catalogVersions.isEmpty())
		{
			throw new ConversionException(MISSING_SOLR_FACET_SEARCH_INDEXED_CATALOG_VERSIONS);
		}
		target.setCatalogVersions(new ArrayList<CatalogVersionModel>(catalogVersions));
		try
		{
			target.setFacetSearchConfig(getFacetSearchConfig());
		}
		catch (final NoValidSolrConfigException e)
		{
			throw new ConversionException(NO_VALID_SOLR_FACET_SEARCH_CONFIG_FOUND_FOR_THE_CURRENT_CONTEXT, e);
		}
		catch (final FacetConfigServiceException e)
		{
			throw new ConversionException(e.getMessage(), e);
		}

		target.setIndexedType(getIndexedType(target.getFacetSearchConfig()));
		// Create the solr search query for the config and type (this sets-up the default page size and sort order)
		final SearchQuery searchQuery = createSearchQuery(target.getFacetSearchConfig(), target.getIndexedType(),
				source.getSearchQueryData().getFreeTextSearch());

		searchQuery.setCurrency(getCommonI18NService().getCurrentCurrency().getIsocode());
		searchQuery.setLanguage(getCommonI18NService().getCurrentLanguage().getIsocode());

		// enable spell checker
		searchQuery.setEnableSpellcheck(true);

		target.setSearchQuery(searchQuery);
	}

	protected IndexedType getIndexedType(final FacetSearchConfig config)
	{
		final IndexConfig indexConfig = config.getIndexConfig();

		// Strategy for working out which of the available indexed types to use
		final Collection<IndexedType> indexedTypes = indexConfig.getIndexedTypes().values();
		if (indexedTypes != null && !indexedTypes.isEmpty())
		{
			// When there are multiple - select the first
			return indexedTypes.iterator().next();
		}

		// No indexed types
		return null;
	}

	protected SearchQuery createSearchQuery(final FacetSearchConfig facetSearchConfig, final IndexedType indexedType,
			final String freeTextSearch)
	{
		SearchQuery searchQuery;

		if (facetSearchConfig.getSearchConfig().isLegacyMode())
		{
			searchQuery = new SearchQuery(facetSearchConfig, indexedType);
			searchQuery.setDefaultOperator(Operator.OR);
			searchQuery.setUserQuery(freeTextSearch);
		}
		else
		{
			searchQuery = facetSearchService.createFreeTextSearchQuery(facetSearchConfig, indexedType, freeTextSearch);
		}

		return searchQuery;
	}

	/**
	 * Resolves suitable {@link FacetSearchConfig} for the query based on the configured strategy bean.<br>
	 *
	 * @return {@link FacetSearchConfig} that is converted from {@link SolrFacetSearchConfigModel}
	 * @throws NoValidSolrConfigException
	 *            , FacetConfigServiceException
	 */
	protected FacetSearchConfig getFacetSearchConfig() throws NoValidSolrConfigException, FacetConfigServiceException
	{

		final SolrFacetSearchConfigModel solrFacetSearchConfigModel = solrFacetSearchProductDocumentConfigSelectionStrategy
				.getCurrentSolrFacetSearchConfig();
		return facetSearchConfigService.getConfiguration(solrFacetSearchConfigModel.getName());
	}

	/**
	 * Get all the session catalog versions that belong to product catalogs of the current site.
	 *
	 * @return the list of session catalog versions
	 */
	protected Collection<CatalogVersionModel> getSessionProductCatalogVersions()
	{
		final BaseSiteModel currentSite = getBaseSiteService().getCurrentBaseSite();
		final List<CatalogModel> productCatalogs = getBaseSiteService().getProductCatalogs(currentSite);
		final Collection<CatalogVersionModel> sessionCatalogVersions = getCatalogVersionService().getSessionCatalogVersions();

		final Collection<CatalogVersionModel> result = new ArrayList<CatalogVersionModel>();
		for (final CatalogVersionModel sessionCatalogVersion : sessionCatalogVersions)
		{
			if (productCatalogs.contains(sessionCatalogVersion.getCatalog()))
			{
				result.add(sessionCatalogVersion);
			}
		}
		return result;
	}
}

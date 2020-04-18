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

import de.hybris.platform.acceleratorservices.urldecoder.FrontendUrlDecoder;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.FacetValueData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchResponse;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.site.BaseSiteService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.cmscockpit.cms.strategies.CounterpartProductCatalogVersionsStrategy;
import de.hybris.liveeditaddon.enums.CMSMenuItemType;
import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationLinkCollectionViewModel;
import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationLinkViewModel;
import de.hybris.liveeditaddon.cockpit.navigationeditor.model.factories.NavigationLinkCollectionViewModelFactory;
import de.hybris.liveeditaddon.cockpit.navigationeditor.model.factories.NavigationLinkViewModelFactory;
import de.hybris.liveeditaddon.cockpit.services.FacetSearchDataService;


/**
 * 
 */
public class DefaultFacetSearchDataService implements FacetSearchDataService
{

	private static final Logger LOG = Logger.getLogger(DefaultFacetSearchDataService.class);

	private CounterpartProductCatalogVersionsStrategy counterpartProductCatalogVersionsStrategy;
	private SessionService sessionService;
	private CatalogVersionService catalogVersionService;
	private BaseSiteService baseSiteService;
	private CommonI18NService commonI18NService;

	private Converter<SearchQueryData, SolrSearchQueryData> solrSearchQueryDecoder;
	private Converter<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest> searchQueryPageableConverter;
	private Converter<SolrSearchRequest, SolrSearchResponse> searchRequestConverter;
	private Converter<SolrSearchResponse, ProductCategorySearchPageData> searchResponseConverter;
	private Populator<SolrSearchQueryData, SearchStateData> solrSearchStatePopulator;
	private FrontendUrlDecoder<CategoryModel> categoryUrlDecoder;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.liveeditaddon.cockpit.services.FacetSearchDataService#getFacetQuerySearch(java.lang.String)
	 */
	public List<FacetData<SolrSearchQueryData>> getFacetQuerySearch(final BaseSiteModel baseSite, final LanguageModel language,
			final CurrencyModel currency, final SearchStateData searchQuery)
	{
		final PageableData pageableData = new PageableData();
		final ProductCategorySearchPageData<SolrSearchQueryData, ?, ?> categorySearch = executeSearch(baseSite, language, currency,
				searchQuery, pageableData);
		return categorySearch.getFacets();
	}

	protected final ProductCategorySearchPageData<SolrSearchQueryData, ?, ?> executeSearch(final BaseSiteModel baseSite,
			final LanguageModel language, final CurrencyModel currency, final SearchStateData searchStateData,
			final PageableData pageableData)
	{
		final ProductCategorySearchPageData result = executeInSessionLocalViewWithProductCatalogRestrictions(new SessionExecutionBody()
		{
			@Override
			public Object execute()
			{

				// Create the SearchQueryPageableData that contains our parameters
				final SolrSearchQueryData searchQuery = getSolrSearchQueryDecoder().convert(searchStateData.getQuery());
				final CategoryModel category = getCategoryUrlDecoder().decode(searchStateData.getUrl());
				if (category != null)
				{
					searchQuery.setCategoryCode(category.getCode());
				}

				getBaseSiteService().setCurrentBaseSite(baseSite, true);
				if (language != null)
				{
					getCommonI18NService().setCurrentLanguage(language);
				}
				if (currency != null)
				{
					getCommonI18NService().setCurrentCurrency(currency);
				}

				final SearchQueryPageableData<SolrSearchQueryData> searchQueryPageableData = buildSearchQueryPageableData(
						searchQuery, pageableData);

				// Build up the search request
				final SolrSearchRequest solrSearchRequest = getSearchQueryPageableConverter().convert(searchQueryPageableData);

				// Execute the search
				final SolrSearchResponse solrSearchResponse = getSearchRequestConverter().convert(solrSearchRequest);

				final ProductCategorySearchPageData<SolrSearchQueryData, ?, ?> searchPageData = getSearchResponseConverter().convert(
						solrSearchResponse);
				return searchPageData;
			}
		});
		return result;
	}

	protected SearchQueryPageableData<SolrSearchQueryData> buildSearchQueryPageableData(final SolrSearchQueryData searchQueryData,
			final PageableData pageableData)
	{
		final SearchQueryPageableData<SolrSearchQueryData> searchQueryPageableData = createSearchQueryPageableData();
		searchQueryPageableData.setSearchQueryData(searchQueryData);
		searchQueryPageableData.setPageableData(pageableData);
		return searchQueryPageableData;
	}

	// Create methods for data object - can be overridden in spring config

	protected SearchQueryPageableData<SolrSearchQueryData> createSearchQueryPageableData()
	{
		return new SearchQueryPageableData<SolrSearchQueryData>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.liveeditaddon.cockpit.services.FacetSearchDataService#getFacetQuerySerarchNavNodes(java.lang.String)
	 */
	@Override
	public Collection<NavigationLinkCollectionViewModel> getFacetQuerySearchNavNodes(final BaseSiteModel baseSite,
			final LanguageModel language, final CurrencyModel currency, final SearchStateData searchStateData)
	{
		final List<NavigationLinkCollectionViewModel> navNodesCollection = new ArrayList<NavigationLinkCollectionViewModel>();
		final List<FacetData<SolrSearchQueryData>> facetCategorySearch = getFacetQuerySearch(baseSite, language, currency,
				searchStateData);
		for (final FacetData<SolrSearchQueryData> facetData : facetCategorySearch)
		{
			try
			{
				navNodesCollection.add(createSingleNavigationCollectionItem(language, facetData));
			}
			catch (final Exception e)
			{
				LOG.error("Adding navigation node collection failed: " + e.getMessage());
			}
		}
		return navNodesCollection;
	}

	/**
	 * Creating single NavigationLinkCollectionViewModel object
	 * 
	 * @param facetData
	 * @return NavigationLinkCollectionViewModel
	 */
	protected NavigationLinkCollectionViewModel createSingleNavigationCollectionItem(final LanguageModel language,
			final FacetData<SolrSearchQueryData> facetData)
	{
		if (facetData == null)
		{
			throw new IllegalArgumentException("Facet data is null!");
		}
		final NavigationLinkCollectionViewModel navLinkCollModel = NavigationLinkCollectionViewModelFactory.create(
				facetData.getName(), createNavLinks(language, facetData));
		return navLinkCollModel;
	}

	/**
	 * Getting facet values for FacetData object
	 * 
	 * @param facetData
	 * @return List<FacetValue>
	 */
	protected LinkedList<NavigationLinkViewModel> createNavLinks(final LanguageModel language,
			final FacetData<SolrSearchQueryData> facetData)
	{
		final LinkedList<NavigationLinkViewModel> navLinks = new LinkedList<NavigationLinkViewModel>();
		final List<FacetValueData<SolrSearchQueryData>> values = facetData.getValues();
		for (final FacetValueData<SolrSearchQueryData> item : values)
		{
			final SearchStateData ssd = new SearchStateData();
			getSolrSearchStatePopulator().populate(item.getQuery(), ssd);

			navLinks.add(NavigationLinkViewModelFactory.create(buildNameForFacetValue(item), language.getIsocode(), ssd.getUrl(),
					CMSMenuItemType.NAVIGATION_STATE));
		}
		return navLinks;
	}

	protected String buildNameForFacetValue(final FacetValueData<SolrSearchQueryData> item)
	{
		return item.getName() + " (" + item.getCount() + ")";
	}



	protected <T extends Object> T executeInSessionLocalViewWithProductCatalogRestrictions(final SessionExecutionBody exec)
	{
		return getSessionService().executeInLocalView(new SessionExecutionBody()
		{

			@Override
			public Object execute()
			{
				final Collection<CatalogVersionModel> productCVs = getCounterpartProductCatalogVersionsStrategy()
						.getCounterpartProductCatalogVersions();
				getCatalogVersionService().setSessionCatalogVersions(productCVs);

				return exec.execute();
			}
		});
	}

	/**
	 * @return the counterpartProductCatalogVersionsStrategy
	 */
	public CounterpartProductCatalogVersionsStrategy getCounterpartProductCatalogVersionsStrategy()
	{
		return counterpartProductCatalogVersionsStrategy;
	}

	/**
	 * @param counterpartProductCatalogVersionsStrategy
	 *           the counterpartProductCatalogVersionsStrategy to set
	 */
	@Required
	public void setCounterpartProductCatalogVersionsStrategy(
			final CounterpartProductCatalogVersionsStrategy counterpartProductCatalogVersionsStrategy)
	{
		this.counterpartProductCatalogVersionsStrategy = counterpartProductCatalogVersionsStrategy;
	}

	/**
	 * @return the sessionService
	 */
	public SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
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
	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	protected Converter<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest> getSearchQueryPageableConverter()
	{
		return searchQueryPageableConverter;
	}

	@Required
	public void setSearchQueryPageableConverter(
			final Converter<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest> searchQueryPageableConverter)
	{
		this.searchQueryPageableConverter = searchQueryPageableConverter;
	}

	protected Converter<SolrSearchRequest, SolrSearchResponse> getSearchRequestConverter()
	{
		return searchRequestConverter;
	}

	@Required
	public void setSearchRequestConverter(final Converter<SolrSearchRequest, SolrSearchResponse> searchRequestConverter)
	{
		this.searchRequestConverter = searchRequestConverter;
	}

	protected Converter<SolrSearchResponse, ProductCategorySearchPageData> getSearchResponseConverter()
	{
		return searchResponseConverter;
	}

	@Required
	public void setSearchResponseConverter(
			final Converter<SolrSearchResponse, ProductCategorySearchPageData> searchResponseConverter)
	{
		this.searchResponseConverter = searchResponseConverter;
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
	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}



	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	public CommonI18NService getCommonI18NService()
	{
		return this.commonI18NService;
	}


	/**
	 * @return the categoryUrlDecoder
	 */
	public FrontendUrlDecoder<CategoryModel> getCategoryUrlDecoder()
	{
		return categoryUrlDecoder;
	}


	/**
	 * @param categoryUrlDecoder
	 *           the categoryUrlDecoder to set
	 */
	@Required
	public void setCategoryUrlDecoder(final FrontendUrlDecoder<CategoryModel> categoryUrlDecoder)
	{
		this.categoryUrlDecoder = categoryUrlDecoder;
	}


	/**
	 * @return the solrSearchQueryDecoder
	 */
	public Converter<SearchQueryData, SolrSearchQueryData> getSolrSearchQueryDecoder()
	{
		return solrSearchQueryDecoder;
	}


	/**
	 * @param solrSearchQueryDecoder
	 *           the solrSearchQueryDecoder to set
	 */
	@Required
	public void setSolrSearchQueryDecoder(final Converter<SearchQueryData, SolrSearchQueryData> solrSearchQueryDecoder)
	{
		this.solrSearchQueryDecoder = solrSearchQueryDecoder;
	}

	@Required
	public void setSolrSearchStatePopulator(final Populator<SolrSearchQueryData, SearchStateData> p)
	{
		this.solrSearchStatePopulator = p;
	}

	public Populator<SolrSearchQueryData, SearchStateData> getSolrSearchStatePopulator()
	{
		return this.solrSearchStatePopulator;
	}

}

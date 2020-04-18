/**
 *
 */
package com.greenlee.core.search.services.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.greenlee.facades.search.facetdata.ContentSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchResponse;

import java.util.Collections;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import com.greenlee.core.search.services.ContentSearchService;


/**
 * @author aruna
 *
 */
public class ContentSearchServiceImpl<ITEM> implements
		ContentSearchService<SolrSearchQueryData, ITEM, ContentSearchPageData<SolrSearchQueryData, ITEM>>
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(ContentSearchServiceImpl.class);


	@Autowired
	private Converter<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest> commerceContentSearchQueryPageableConverter;


	@Autowired
	private Converter<SolrSearchRequest, SolrSearchResponse> commerceSolrSearchRequestConverter;

	@Autowired
	private Converter<SolrSearchResponse, ContentSearchPageData<SolrSearchQueryData, ITEM>> contentSolrSearchResponseConverter;

	@Override
	public ContentSearchPageData<SolrSearchQueryData, SearchResultValueData> textSearch(final String text,
			final PageableData pageableData)
	{
		final SolrSearchQueryData searchQueryData = createSearchQueryData();
		searchQueryData.setFreeTextSearch(text);
		searchQueryData.setFilterTerms(Collections.<SolrSearchQueryTermData> emptyList());

		return doSearch(searchQueryData, pageableData);
	}


	@Override
	public ContentSearchPageData<SolrSearchQueryData, SearchResultValueData> searchAgain(
			final SolrSearchQueryData searchQueryData, final PageableData pageableData)
	{
		return doSearch(searchQueryData, pageableData);
	}


	protected ContentSearchPageData<SolrSearchQueryData, SearchResultValueData> doSearch(
			final SolrSearchQueryData searchQueryData, final PageableData pageableData)
	{
		validateParameterNotNull(searchQueryData, "SearchQueryData cannot be null");

		// Create the SearchQueryPageableData that contains our parameters
		final SearchQueryPageableData<SolrSearchQueryData> searchQueryPageableData = buildSearchQueryPageableData(searchQueryData,
				pageableData);

		// Build up the search request
		final SolrSearchRequest solrSearchRequest = commerceContentSearchQueryPageableConverter.convert(searchQueryPageableData);

		// Execute the search
		final SolrSearchResponse solrSearchResponse = commerceSolrSearchRequestConverter.convert(solrSearchRequest);

		// Convert the response
		return (ContentSearchPageData<SolrSearchQueryData, SearchResultValueData>) contentSolrSearchResponseConverter
				.convert(solrSearchResponse);
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

	protected SolrSearchQueryData createSearchQueryData()
	{
		return new SolrSearchQueryData();
	}


}

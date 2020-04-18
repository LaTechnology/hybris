/**
 *
 */
package com.greenlee.core.search.services.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchResponse;

import java.util.Collections;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import com.greenlee.core.search.services.ProductDocumentSearchService;
import com.hybris.greenlee.search.facetdata.ProductDocumentSearchPageData;


/**
 * @author aruna
 *
 */
public class ProductDocumentSearchServiceImpl<ITEM> implements
		ProductDocumentSearchService<SolrSearchQueryData, ITEM, ProductDocumentSearchPageData<SolrSearchQueryData, ITEM>>
{
	@Resource(name = "defaultCommerceProductSearchQueryPageableConverter")
	private Converter<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest> commerceProductDocumentSearchQueryPageableConverter;

	/**
	 * @return the commerceProductDocumentSearchQueryPageableConverter
	 */
	public Converter<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest> getCommerceProductDocumentSearchQueryPageableConverter()
	{
		return commerceProductDocumentSearchQueryPageableConverter;
	}

	/**
	 * @param commerceProductDocumentSearchQueryPageableConverter
	 *           the commerceProductDocumentSearchQueryPageableConverter to set
	 */
	public void setCommerceProductDocumentSearchQueryPageableConverter(
			final Converter<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest> commerceProductDocumentSearchQueryPageableConverter)
	{
		this.commerceProductDocumentSearchQueryPageableConverter = commerceProductDocumentSearchQueryPageableConverter;
	}

	/**
	 * @return the commerceSolrSearchRequestConverter
	 */
	public Converter<SolrSearchRequest, SolrSearchResponse> getCommerceSolrSearchRequestConverter()
	{
		return commerceSolrSearchRequestConverter;
	}

	/**
	 * @param commerceSolrSearchRequestConverter
	 *           the commerceSolrSearchRequestConverter to set
	 */
	public void setCommerceSolrSearchRequestConverter(
			final Converter<SolrSearchRequest, SolrSearchResponse> commerceSolrSearchRequestConverter)
	{
		this.commerceSolrSearchRequestConverter = commerceSolrSearchRequestConverter;
	}

	/**
	 * @return the productDocumentSolrSearchResponseConverter
	 */
	public Converter<SolrSearchResponse, ProductDocumentSearchPageData<SolrSearchQueryData, ITEM>> getProductDocumentSolrSearchResponseConverter()
	{
		return productDocumentSolrSearchResponseConverter;
	}

	/**
	 * @param productDocumentSolrSearchResponseConverter
	 *           the productDocumentSolrSearchResponseConverter to set
	 */
	public void setProductDocumentSolrSearchResponseConverter(
			final Converter<SolrSearchResponse, ProductDocumentSearchPageData<SolrSearchQueryData, ITEM>> productDocumentSolrSearchResponseConverter)
	{
		this.productDocumentSolrSearchResponseConverter = productDocumentSolrSearchResponseConverter;
	}

	@Autowired
	private Converter<SolrSearchRequest, SolrSearchResponse> commerceSolrSearchRequestConverter;

	@Autowired
	private Converter<SolrSearchResponse, ProductDocumentSearchPageData<SolrSearchQueryData, ITEM>> productDocumentSolrSearchResponseConverter;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.greenlee.core.search.services.ProductDocumentSearchService#textSearch(java.lang.String,
	 * de.hybris.platform.commerceservices.search.pagedata.PageableData)
	 */
	@Override
	public ProductDocumentSearchPageData<SolrSearchQueryData, SearchResultValueData> textSearch(final PageableData pageableData)
	{
		final SolrSearchQueryData searchQueryData = createSearchQueryData();
		searchQueryData.setFilterTerms(Collections.<SolrSearchQueryTermData> emptyList());
		// YTODO Auto-generated method stub
		return doSearch(searchQueryData, pageableData);
	}

	/**
	 * @param searchQueryData
	 * @param pageableData
	 * @return ProductDocumentSearchPageData
	 */
	private ProductDocumentSearchPageData<SolrSearchQueryData, SearchResultValueData> doSearch(
			final SolrSearchQueryData searchQueryData, final PageableData pageableData)
	{
		validateParameterNotNull(searchQueryData, "SearchQueryData cannot be null");

		// Create the SearchQueryPageableData that contains our parameters
		final SearchQueryPageableData<SolrSearchQueryData> searchQueryPageableData = buildSearchQueryPageableData(searchQueryData,
				pageableData);

		// Build up the search request
		final SolrSearchRequest solrSearchRequest = commerceProductDocumentSearchQueryPageableConverter
				.convert(searchQueryPageableData);
		// Execute the search
		final SolrSearchResponse solrSearchResponse = commerceSolrSearchRequestConverter.convert(solrSearchRequest);

		// Convert the response
		return (ProductDocumentSearchPageData<SolrSearchQueryData, SearchResultValueData>) productDocumentSolrSearchResponseConverter
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

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.greenlee.core.search.services.ProductDocumentSearchService#searchAgain(de.hybris.platform.commerceservices
	 * .search.solrfacetsearch.data.SolrSearchQueryData,
	 * de.hybris.platform.commerceservices.search.pagedata.PageableData)
	 */
	@Override
	public ProductDocumentSearchPageData<SolrSearchQueryData, SearchResultValueData> searchAgain(
			final SolrSearchQueryData solrSearchQueryData, final PageableData pageableData)
	{
		// YTODO Auto-generated method stub
		return doSearch(solrSearchQueryData, pageableData);
	}

	protected SolrSearchQueryData createSearchQueryData()
	{
		return new SolrSearchQueryData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.greenlee.core.search.services.ProductDocumentSearchService#textSearch(java.lang.String)
	 */
	@Override
	public ProductDocumentSearchPageData<SolrSearchQueryData, SearchResultValueData> textSearch(final String text,
			final PageableData pageableData)
	{
		final SolrSearchQueryData searchQueryData = createSearchQueryData();
		searchQueryData.setFreeTextSearch(text);
		searchQueryData.setFilterTerms(Collections.<SolrSearchQueryTermData> emptyList());

		return doSearch(searchQueryData, pageableData);
	}
}

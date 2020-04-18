/**
 *
 */
package com.greenlee.facades.search.impl;

import de.hybris.platform.commercefacades.search.data.AutocompleteSuggestionData;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.AutocompleteSuggestion;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.threadcontext.ThreadContextService;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.greenlee.core.search.services.ProductDocumentSearchAutocompleteService;
import com.greenlee.core.search.services.ProductDocumentSearchService;
import com.greenlee.facades.search.ProductDocumentSearchFacade;
import com.greenlee.search.data.ProductDocumentData;
import com.hybris.greenlee.search.facetdata.ProductDocumentSearchPageData;


/**
 * @author aruna
 *
 */
public class ProductDocumentSearchFacadeImpl<ITEM extends ProductDocumentData> implements ProductDocumentSearchFacade<ITEM>
{

	@Autowired
	private ProductDocumentSearchService productDocumentSearchService;

	@Autowired
	private ThreadContextService threadContextService;

	@Autowired
	private Converter<ProductDocumentSearchPageData<SolrSearchQueryData, SearchResultValueData>, ProductDocumentSearchPageData<SearchStateData, ITEM>> productDocumentSearchPageConverter;

	private Converter<SearchQueryData, SolrSearchQueryData> searchQueryDecoder;

	@Autowired
	private ProductDocumentSearchAutocompleteService<AutocompleteSuggestion> productDocumentSearchAutocompleteService;

	private Converter<AutocompleteSuggestion, AutocompleteSuggestionData> autocompleteSuggestionConverter;

	/**
	 * @return the autocompleteSuggestionConverter
	 */
	public Converter<AutocompleteSuggestion, AutocompleteSuggestionData> getAutocompleteSuggestionConverter()
	{
		return autocompleteSuggestionConverter;
	}

	/**
	 * @param autocompleteSuggestionConverter
	 *           the autocompleteSuggestionConverter to set
	 */
	public void setAutocompleteSuggestionConverter(
			final Converter<AutocompleteSuggestion, AutocompleteSuggestionData> autocompleteSuggestionConverter)
	{
		this.autocompleteSuggestionConverter = autocompleteSuggestionConverter;
	}

	/**
	 * @return the searchQueryDecoder
	 */
	public Converter<SearchQueryData, SolrSearchQueryData> getSearchQueryDecoder()
	{
		return searchQueryDecoder;
	}

	/**
	 * @param searchQueryDecoder
	 *           the searchQueryDecoder to set
	 */
	public void setSearchQueryDecoder(final Converter<SearchQueryData, SolrSearchQueryData> searchQueryDecoder)
	{
		this.searchQueryDecoder = searchQueryDecoder;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.greenlee.facades.search.ProductDocumentSearchFacade#textSearch(java.lang.String)
	 */
	@Override
	public ProductDocumentSearchPageData<SearchStateData, ITEM> textSearch(final String text)
	{
		return threadContextService
				.executeInContext(new ThreadContextService.Executor<ProductDocumentSearchPageData<SearchStateData, ITEM>, ThreadContextService.Nothing>()
				{
					@Override
					public ProductDocumentSearchPageData<SearchStateData, ITEM> execute()
					{
						return productDocumentSearchPageConverter.convert(productDocumentSearchService.textSearch(text, null));
					}
				});
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.greenlee.facades.search.ProductDocumentSearchFacade#textSearch(de.hybris.platform.commercefacades.search.data
	 * .SearchStateData, de.hybris.platform.commerceservices.search.pagedata.PageableData)
	 */
	@Override
	public ProductDocumentSearchPageData<SearchStateData, ITEM> textSearch(final SearchStateData searchState,
			final PageableData pageableData)
	{
		Assert.notNull(searchState, "SearchStateData must not be null.");
		return threadContextService
				.executeInContext(new ThreadContextService.Executor<ProductDocumentSearchPageData<SearchStateData, ITEM>, ThreadContextService.Nothing>()
				{
					@Override
					public ProductDocumentSearchPageData<SearchStateData, ITEM> execute()
					{
						return productDocumentSearchPageConverter.convert(productDocumentSearchService.searchAgain(
								decodeState(searchState), pageableData));
					}
				});
	}

	protected SolrSearchQueryData decodeState(final SearchStateData searchState)
	{
		final SolrSearchQueryData searchQueryData = getSearchQueryDecoder().convert(searchState.getQuery());
		return searchQueryData;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.greenlee.facades.search.ProductDocumentSearchFacade#getAutocompleteSuggestions(java.lang.String)
	 */
	@Override
	public List<AutocompleteSuggestionData> getAutocompleteSuggestions(final String input)
	{
		return threadContextService
				.executeInContext(new ThreadContextService.Executor<List<AutocompleteSuggestionData>, ThreadContextService.Nothing>()
				{
					@Override
					public List<AutocompleteSuggestionData> execute()
					{
						return Converters.convertAll(productDocumentSearchAutocompleteService.getAutocompleteSuggestions(input),
								getAutocompleteSuggestionConverter());
					}
				});
	}
}

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
package com.greenlee.facades.populators;

import de.hybris.platform.commerceservices.search.facetdata.BreadcrumbData;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.SpellingSuggestionData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.greenlee.search.data.ProductDocumentData;
import com.hybris.greenlee.search.facetdata.ProductDocumentSearchPageData;


/**
 */
public class ProductDocumentSearchPagePopulator<QUERY, STATE, RESULT, ITEM extends ProductDocumentData, SCAT>
        implements Populator<ProductDocumentSearchPageData<QUERY, RESULT>, ProductDocumentSearchPageData<STATE, ITEM>>
{
    /**
     * 
     */
    private static final String ONLINE = "Online";
    /**
     * 
     */
    private static final String CATALOG_VERSION = "catalogVersion";
    private Converter<QUERY, STATE>                                                 searchStateConverter;
    private Converter<BreadcrumbData<QUERY>, BreadcrumbData<STATE>>                 breadcrumbConverter;
    private Converter<FacetData<QUERY>, FacetData<STATE>>                           facetConverter;
    private Converter<SpellingSuggestionData<QUERY>, SpellingSuggestionData<STATE>> spellingSuggestionConverter;
    private Converter<RESULT, ITEM>                                                 searchResultProductDocumentConverter;


    /**
     * @return the searchResultProductDocumentConverter
     */
    public Converter<RESULT, ITEM> getSearchResultProductDocumentConverter()
    {
        return searchResultProductDocumentConverter;
    }

    /**
     * @param searchResultProductDocumentConverter
     *            the searchResultProductDocumentConverter to set
     */
    public void setSearchResultProductDocumentConverter(final Converter<RESULT, ITEM> searchResultProductDocumentConverter)
    {
        this.searchResultProductDocumentConverter = searchResultProductDocumentConverter;
    }

    protected Converter<QUERY, STATE> getSearchStateConverter()
    {
        return searchStateConverter;
    }

    @Required
    public void setSearchStateConverter(final Converter<QUERY, STATE> searchStateConverter)
    {
        this.searchStateConverter = searchStateConverter;
    }

    protected Converter<BreadcrumbData<QUERY>, BreadcrumbData<STATE>> getBreadcrumbConverter()
    {
        return breadcrumbConverter;
    }

    @Required
    public void setBreadcrumbConverter(final Converter<BreadcrumbData<QUERY>, BreadcrumbData<STATE>> breadcrumbConverter)
    {
        this.breadcrumbConverter = breadcrumbConverter;
    }

    protected Converter<FacetData<QUERY>, FacetData<STATE>> getFacetConverter()
    {
        return facetConverter;
    }

    @Required
    public void setFacetConverter(final Converter<FacetData<QUERY>, FacetData<STATE>> facetConverter)
    {
        this.facetConverter = facetConverter;
    }


    protected Converter<SpellingSuggestionData<QUERY>, SpellingSuggestionData<STATE>> getSpellingSuggestionConverter()
    {
        return spellingSuggestionConverter;
    }

    @Required
    public void setSpellingSuggestionConverter(
            final Converter<SpellingSuggestionData<QUERY>, SpellingSuggestionData<STATE>> spellingSuggestionConverter)
    {
        this.spellingSuggestionConverter = spellingSuggestionConverter;
    }

    @Override
    public void populate(final ProductDocumentSearchPageData<QUERY, RESULT> source,
            final ProductDocumentSearchPageData<STATE, ITEM> target)
    {
        target.setFreeTextSearch(source.getFreeTextSearch());
        target.setCategoryCode(source.getCategoryCode());


        if (source.getBreadcrumbs() != null)
        {
            target.setBreadcrumbs(Converters.convertAll(source.getBreadcrumbs(), getBreadcrumbConverter()));
        }

        target.setCurrentQuery(getSearchStateConverter().convert(source.getCurrentQuery()));

        if (source.getFacets() != null)
        {
            target.setFacets(Converters.convertAll(source.getFacets(), getFacetConverter()));
        }

        target.setPagination(source.getPagination());

        if (source.getSpellingSuggestion() != null)
        {
            target.setSpellingSuggestion(getSpellingSuggestionConverter().convert(source.getSpellingSuggestion()));
        }

        if (source.getResults() != null)
        {
            final List<SearchResultValueData> searchResults = (List<SearchResultValueData>) source.getResults();
            final List<SearchResultValueData> filteredSearchResult = new ArrayList<SearchResultValueData>();
            for (final SearchResultValueData result : searchResults)
            {
                if (this.<String> getValue(result, CATALOG_VERSION).equalsIgnoreCase(ONLINE))
                {
                    filteredSearchResult.add(result);
                }
            }
            final List<RESULT> searchResult = (List<RESULT>) filteredSearchResult;
            target.setResults(Converters.convertAll(searchResult, getSearchResultProductDocumentConverter()));

        }
        target.setKeywordRedirectUrl(source.getKeywordRedirectUrl());
    }


    protected <T> T getValue(final SearchResultValueData source, final String propertyName)
    {
        if (source.getValues() == null)
        {
            return null;
        }

        // DO NOT REMOVE the cast (T) below, while it should be unnecessary it is required by the javac compiler
        return (T) source.getValues().get(propertyName);
    }
}

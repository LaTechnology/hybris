/**
 *
 */
package com.greenlee.facades.search.impl;

import de.hybris.greenlee.facades.content.data.ContentData;
import de.hybris.greenlee.facades.search.facetdata.ContentSearchPageData;
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

import org.jgroups.protocols.pbcast.STATE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.greenlee.core.search.services.ContentSearchAutoCompleteService;
import com.greenlee.core.search.services.ContentSearchService;
import com.greenlee.facades.search.ContentSearchFacade;


/**
 * @author aruna
 *
 */
public class ContentSearchFacadeImpl<ITEM extends ContentData> implements ContentSearchFacade<ITEM>
{


    @Autowired
    private ContentSearchService<STATE, ITEM, ContentSearchPageData<STATE, ITEM>>                                                      contentSearchService;


    @Autowired
    private Converter<ContentSearchPageData<SolrSearchQueryData, SearchResultValueData>, ContentSearchPageData<SearchStateData, ITEM>> contentSearchPageConverter;


    @Autowired
    private Converter<SearchQueryData, SolrSearchQueryData>                                                                            solrSearchQueryDecoder;


    @Autowired
    private ThreadContextService                                                                                                       threadContextService;

    @Autowired
    private ContentSearchAutoCompleteService<AutocompleteSuggestion>                                                                   contentSearchAutoCompleteService;

    private Converter<AutocompleteSuggestion, AutocompleteSuggestionData>                                                              autocompleteSuggestionConverter;

    /**
     * @return the autocompleteSuggestionConverter
     */
    public Converter<AutocompleteSuggestion, AutocompleteSuggestionData> getAutocompleteSuggestionConverter()
    {
        return autocompleteSuggestionConverter;
    }

    /**
     * @param autocompleteSuggestionConverter
     *            the autocompleteSuggestionConverter to set
     */
    public void setAutocompleteSuggestionConverter(
            final Converter<AutocompleteSuggestion, AutocompleteSuggestionData> autocompleteSuggestionConverter)
    {
        this.autocompleteSuggestionConverter = autocompleteSuggestionConverter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hybris.greenlee.facades.search.ContentSearchFacade#textSearch(java.lang.String)
     */
    @Override
    public ContentSearchPageData<SearchStateData, ITEM> textSearch(final String text)
    {
        return threadContextService.executeInContext(
                new ThreadContextService.Executor<ContentSearchPageData<SearchStateData, ITEM>, ThreadContextService.Nothing>()
                {
                    @Override
                    public ContentSearchPageData<SearchStateData, ITEM> execute()
                    {
                        return contentSearchPageConverter.convert(contentSearchService.textSearch(text, null));
                    }
                });
    }



    /*
     * (non-Javadoc)
     * 
     * @see
     * de.hybris.greenlee.facades.search.ContentSearchFacade#textSearch(de.hybris.platform.commercefacades.search.data
     * .SearchStateData, de.hybris.platform.commerceservices.search.pagedata.PageableData)
     */
    @Override
    public ContentSearchPageData<SearchStateData, ITEM> textSearch(final SearchStateData searchState,
            final PageableData pageableData)
    {
        Assert.notNull(searchState, "SearchStateData must not be null.");

        return threadContextService.executeInContext(
                new ThreadContextService.Executor<ContentSearchPageData<SearchStateData, ITEM>, ThreadContextService.Nothing>()
                {
                    @Override
                    public ContentSearchPageData<SearchStateData, ITEM> execute()
                    {

                        return contentSearchPageConverter
                                .convert(contentSearchService.searchAgain(decodeState(searchState), pageableData));
                    }
                });
    }

    protected SolrSearchQueryData decodeState(final SearchStateData searchState)
    {
        final SolrSearchQueryData searchQueryData = solrSearchQueryDecoder.convert(searchState.getQuery());

        return searchQueryData;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.greenlee.facades.search.ContentSearchFacade#getAutocompleteSuggestions(java.lang.String)
     */
    @Override
    public List<AutocompleteSuggestionData> getAutocompleteSuggestions(final String input)
    {
        return threadContextService.executeInContext(
                new ThreadContextService.Executor<List<AutocompleteSuggestionData>, ThreadContextService.Nothing>()
                {
                    @Override
                    public List<AutocompleteSuggestionData> execute()
                    {
                        return Converters.convertAll(contentSearchAutoCompleteService.getAutoCompleteSuggestions(input),
                                getAutocompleteSuggestionConverter());
                    }
                });
    }

    /**
     * @return the contentSearchAutoCompleteService
     */
    public ContentSearchAutoCompleteService<AutocompleteSuggestion> getContentSearchAutoCompleteService()
    {
        return contentSearchAutoCompleteService;
    }

    /**
     * @param contentSearchAutoCompleteService
     *            the contentSearchAutoCompleteService to set
     */
    public void setContentSearchAutoCompleteService(
            final ContentSearchAutoCompleteService<AutocompleteSuggestion> contentSearchAutoCompleteService)
    {
        this.contentSearchAutoCompleteService = contentSearchAutoCompleteService;
    }
}

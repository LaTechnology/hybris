/**
 *
 */
package com.greenlee.core.search.services;

import de.hybris.greenlee.facades.search.facetdata.ContentSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;


/**
 * @author aruna
 *
 */

public interface ContentSearchService<STATE, ITEM, RESULT extends ContentSearchPageData<STATE, ITEM>>
{
	/**
	 * Initiate a new search using simple free text query.
	 *
	 * @param text
	 *           the search text
	 * @param pageableData
	 *           the page to return, can be null to use defaults
	 * @return the search results
	 */
	ContentSearchPageData<SolrSearchQueryData, SearchResultValueData> textSearch(String text, PageableData pageableData);


	/**
	 * Refine an exiting search. The query object allows more complex queries using facet selection. The SearchQueryData
	 * must have been obtained from the results of a call to either {@link #textSearch(String,PageableData)} or
	 * {@link #categorySearch(String,PageableData)}.
	 *
	 * @param solrSearchQueryData
	 *           the search query object
	 * @param pageableData
	 *           the page to return
	 * @return the search results
	 */
	ContentSearchPageData<SolrSearchQueryData, SearchResultValueData> searchAgain(SolrSearchQueryData solrSearchQueryData,
			PageableData pageableData);



}

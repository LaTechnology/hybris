/**
 *
 */
package com.greenlee.facades.search;

import de.hybris.greenlee.facades.content.data.ContentData;
import de.hybris.greenlee.facades.search.facetdata.ContentSearchPageData;
import de.hybris.platform.commercefacades.search.data.AutocompleteSuggestionData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;

import java.util.List;




/**
 * @author aruna
 *
 */
public interface ContentSearchFacade<ITEM extends ContentData>
{

	/**
	 * Initiate a new search using simple free text query.
	 *
	 * @param text
	 *           the search text
	 * @return the search results
	 */
	ContentSearchPageData<SearchStateData, ITEM> textSearch(String text);

	/**
	 * Refine an exiting search. The query object allows more complex queries using facet selection. The SearchStateData
	 * must have been obtained from the results of a call to {@link #textSearch(String)}.
	 *
	 * @param searchState
	 *           the search query object
	 * @param pageableData
	 *           the page to return
	 * @return the search results
	 */
	ContentSearchPageData<SearchStateData, ITEM> textSearch(SearchStateData searchState, PageableData pageableData);

	List<AutocompleteSuggestionData> getAutocompleteSuggestions(final String input);

}

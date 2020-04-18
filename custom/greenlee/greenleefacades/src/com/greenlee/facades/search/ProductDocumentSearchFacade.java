/**
 *
 */
package com.greenlee.facades.search;

import de.hybris.platform.commercefacades.search.data.AutocompleteSuggestionData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;

import java.util.List;

import com.greenlee.search.data.ProductDocumentData;
import com.hybris.greenlee.search.facetdata.ProductDocumentSearchPageData;


/**
 * @author aruna
 *
 */
public interface ProductDocumentSearchFacade<ITEM extends ProductDocumentData>
{
	ProductDocumentSearchPageData<SearchStateData, ITEM> textSearch(String text);

	ProductDocumentSearchPageData<SearchStateData, ITEM> textSearch(SearchStateData searchState, PageableData pageableData);

	/**
	 * Get the auto complete suggestions for the provided input.
	 *
	 * @param input
	 *           the user's input
	 * @return a list of suggested search terms
	 */
	List<AutocompleteSuggestionData> getAutocompleteSuggestions(String input);
}

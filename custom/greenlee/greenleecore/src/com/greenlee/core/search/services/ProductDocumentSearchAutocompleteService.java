/**
 *
 */
package com.greenlee.core.search.services;

import de.hybris.platform.commerceservices.search.solrfacetsearch.data.AutocompleteSuggestion;

import java.util.List;


/**
 * @author aruna
 * @param <AutocompleteSuggestion>
 *
 */
public interface ProductDocumentSearchAutocompleteService<RESULT extends AutocompleteSuggestion>
{
	/**
	 * Get the auto complete suggestions for the input provided.
	 *
	 * @param input
	 *           the user's input on which the autocomplete is based
	 * @return a list of suggested search terms
	 */
	List<RESULT> getAutocompleteSuggestions(String input);
}

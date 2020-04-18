/**
 *
 */
package com.greenlee.core.search.services;

import de.hybris.platform.commerceservices.search.solrfacetsearch.data.AutocompleteSuggestion;

import java.util.List;


/**
 * @author aruna
 *
 */
public interface ContentSearchAutoCompleteService<RESULT extends AutocompleteSuggestion>
{
	List<RESULT> getAutoCompleteSuggestions(String input);
}

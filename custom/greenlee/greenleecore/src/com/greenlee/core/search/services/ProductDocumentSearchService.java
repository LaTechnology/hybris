/**
 *
 */
package com.greenlee.core.search.services;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;

import com.hybris.greenlee.search.facetdata.ProductDocumentSearchPageData;


/**
 * @author aruna
 *
 */
public interface ProductDocumentSearchService<STATE, ITEM, RESULT extends ProductDocumentSearchPageData<STATE, ITEM>>
{

	ProductDocumentSearchPageData<SolrSearchQueryData, SearchResultValueData> searchAgain(SolrSearchQueryData solrSearchQueryData,
			PageableData pageableData);

	/**
	 * @param pageableData
	 * @return ProductDocumentSearchPageData
	 */
	ProductDocumentSearchPageData<SolrSearchQueryData, SearchResultValueData> textSearch(PageableData pageableData);

	public ProductDocumentSearchPageData<SolrSearchQueryData, SearchResultValueData> textSearch(final String text,
			final PageableData pageableData);

}

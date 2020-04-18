/**
 *
 */
package com.greenlee.storefront.controllers.pages;

import de.hybris.platform.acceleratorcms.model.components.SearchBoxComponentModel;
import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController.ShowMode;
import de.hybris.platform.acceleratorstorefrontcommons.util.XSSFilterUtil;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.commercefacades.search.data.AutocompleteResultData;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.greenlee.facades.search.ProductDocumentSearchFacade;
import com.greenlee.search.data.ProductDocumentData;
import com.hybris.greenlee.search.facetdata.ProductDocumentSearchPageData;


/**
 * @author aruna
 *
 */
@Controller
@RequestMapping("/**/productDocument")
public class ProductDocumentPageController extends AbstractPageController
{
	private static final String PRODUCT_DOCUMENT = "productDocument";
	public static final int MAX_PAGE_LIMIT = 100;
	private static final String PAGINATION_NUMBER_OF_RESULTS_COUNT = "pagination.number.results.count";

	@Autowired
	ProductDocumentSearchFacade<ProductDocumentData> productDocumentFacade;

	@Resource(name = "docSearchBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder docSearchBreadcrumbBuilder;

	@Resource(name = "siteConfigService")
	private SiteConfigService siteConfigService;

	@Resource(name = "cmsComponentService")
	private CMSComponentService cmsComponentService;

	private static final String COMPONENT_UID_PATH_VARIABLE_PATTERN = "{componentUid:.*}";

	/**
	 * @return the siteConfigService
	 */
	@Override
	public SiteConfigService getSiteConfigService()
	{
		return siteConfigService;
	}

	/**
	 * @param siteConfigService
	 *           the siteConfigService to set
	 */
	public void setSiteConfigService(final SiteConfigService siteConfigService)
	{
		this.siteConfigService = siteConfigService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String category(@RequestParam(value = "q", required = false) final String searchQuery,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode, final Model model,
			final HttpServletRequest request, final HttpServletResponse response)
			throws UnsupportedEncodingException, CMSItemNotFoundException
	{
		return performSearchAndGetResultsPage(searchQuery, page, showMode, sortCode, model, request, response);
	}

	public String performSearchAndGetResultsPage(final String searchQuery, final int page, final ShowMode showMode,
			final String sortCode, final Model model, final HttpServletRequest request, final HttpServletResponse response)
			throws CMSItemNotFoundException
	{
		final CategorySearchEvaluator categorySearch = new CategorySearchEvaluator(XSSFilterUtil.filter(searchQuery), page,
				showMode, sortCode);
		categorySearch.doSearch();

		storeCmsPageInModel(model, getContentPageForLabelOrId(PRODUCT_DOCUMENT));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PRODUCT_DOCUMENT));
		model.addAttribute("breadcrumbs", docSearchBreadcrumbBuilder.getBreadcrumbs(null));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		model.addAttribute(PRODUCT_DOCUMENT, "active");
		return getViewForPage(model);
	}

	protected void populateModel(final Model model, final SearchPageData<?> searchPageData, final ShowMode showMode)
	{
		final int numberPagesShown = getSiteConfigService().getInt(PAGINATION_NUMBER_OF_RESULTS_COUNT, 5);

		model.addAttribute("numberPagesShown", Integer.valueOf(numberPagesShown));
		model.addAttribute("noOfResults", Integer.valueOf(searchPageData.getResults().size()));
		model.addAttribute("searchPageData", searchPageData);
		model.addAttribute("isShowAllAllowed", calculateShowAll(searchPageData, showMode));
		model.addAttribute("isShowPageAllowed", calculateShowPaged(searchPageData, showMode));
		model.addAttribute("isSearchPage", Boolean.FALSE);
	}

	protected Boolean calculateShowPaged(final SearchPageData<?> searchPageData, final ShowMode showMode)
	{
		return Boolean.valueOf(showMode == ShowMode.All && (searchPageData.getPagination().getNumberOfPages() > 1
				|| searchPageData.getPagination().getPageSize() == MAX_PAGE_LIMIT));
	}

	protected Boolean calculateShowAll(final SearchPageData<?> searchPageData, final ShowMode showMode)
	{
		return Boolean.valueOf((showMode != ShowMode.All
				&& searchPageData.getPagination().getTotalNumberOfResults() > searchPageData.getPagination().getPageSize())
				&& isShowAllAllowed(searchPageData));
	}

	/**
	 * Special case, when total number of results > {@link #MAX_PAGE_LIMIT}
	 */
	protected boolean isShowAllAllowed(final SearchPageData<?> searchPageData)
	{
		return searchPageData.getPagination().getNumberOfPages() > 1
				&& searchPageData.getPagination().getTotalNumberOfResults() < MAX_PAGE_LIMIT;
	}

	protected class CategorySearchEvaluator
	{
		public static final int MAX_PAGE_LIMIT = 100;
		private final SearchQueryData searchQueryData = new SearchQueryData();
		private final int page;
		private final ShowMode showMode;
		private final String sortCode;
		ProductDocumentSearchPageData<SearchStateData, ProductDocumentData> searchPageData;

		/**
		 * @return the searchPageData
		 */
		public ProductDocumentSearchPageData<SearchStateData, ProductDocumentData> getSearchPageData()
		{
			return searchPageData;
		}

		/**
		 * @param searchPageData
		 *           the searchPageData to set
		 */
		public void setSearchPageData(final ProductDocumentSearchPageData<SearchStateData, ProductDocumentData> searchPageData)
		{
			this.searchPageData = searchPageData;
		}

		public CategorySearchEvaluator(final String searchQuery, final int page, final ShowMode showMode, final String sortCode)
		{
			this.searchQueryData.setValue(searchQuery);
			this.page = page;
			this.showMode = showMode;
			this.sortCode = sortCode;
		}

		public void doSearch()
		{
			if (searchQueryData.getValue() == null)
			{
				searchPageData = productDocumentFacade.textSearch(null);
			}
			else
			{

				final PageableData pageableData = createPageableData(page, getSearchPageSize(), sortCode, showMode);
				final SearchStateData searchState = new SearchStateData();
				searchState.setQuery(searchQueryData);
				searchPageData = productDocumentFacade.textSearch(searchState, pageableData);
			}

		}

		protected int getSearchPageSize()
		{
			return getSiteConfigService().getInt("storefront.search.productSearchPageSize", 0);
		}

		protected PageableData createPageableData(final int pageNumber, final int pageSize, final String sortCode,
				final ShowMode showMode)
		{
			final PageableData pageableData = new PageableData();
			pageableData.setCurrentPage(pageNumber);
			pageableData.setSort(sortCode);

			if (ShowMode.All == showMode)
			{
				pageableData.setPageSize(MAX_PAGE_LIMIT);
			}
			else
			{
				pageableData.setPageSize(pageSize);
			}
			return pageableData;
		}

	}

	@RequestMapping(method = RequestMethod.GET, params = "!q")
	public String searchDocument(@RequestParam(value = "text", defaultValue = "") final String searchText,
			final HttpServletRequest request, final Model model) throws CMSItemNotFoundException
	{
		if (StringUtils.isNotBlank(searchText))
		{
			final PageableData pageableData = createPageableData(0, getSearchPageSize(), null, ShowMode.Page);

			final String encodedSearchText = XSSFilterUtil.filter(searchText);

			final SearchStateData searchState = new SearchStateData();
			final SearchQueryData searchQueryData = new SearchQueryData();
			searchQueryData.setValue(encodedSearchText);
			searchState.setQuery(searchQueryData);

			final ProductDocumentSearchPageData<SearchStateData, ProductDocumentData> searchPageData = productDocumentFacade
					.textSearch(searchState, pageableData);
			model.addAttribute("searchText", searchText);
			model.addAttribute("searchPageData", searchPageData);
			model.addAttribute("isSearchPage", Boolean.TRUE);
			model.addAttribute("searchText", searchText);
			model.addAttribute("noOfResults", Integer.valueOf(searchPageData.getResults().size()));
		}

		storeCmsPageInModel(model, getContentPageForLabelOrId(PRODUCT_DOCUMENT));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PRODUCT_DOCUMENT));
		return getViewForPage(model);
	}

	@ResponseBody
	@RequestMapping(value = "/autocomplete/" + COMPONENT_UID_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public AutocompleteResultData getAutocompleteSuggestions(@PathVariable final String componentUid,
			@RequestParam("term") final String term) throws CMSItemNotFoundException
	{
		final AutocompleteResultData resultData = new AutocompleteResultData();

		final SearchBoxComponentModel component = (SearchBoxComponentModel) cmsComponentService.getSimpleCMSComponent(componentUid);

		if (component.isDisplaySuggestions())
		{
			resultData
					.setSuggestions(subList(productDocumentFacade.getAutocompleteSuggestions(term), component.getMaxSuggestions()));
		}

		if (component.isDisplayProducts())
		{
			resultData.setProductDocuments(subList(productDocumentFacade.textSearch(term).getResults(), component.getMaxProducts()));
		}

		return resultData;
	}

	protected <E> List<E> subList(final List<E> list, final int maxElements)
	{
		if (CollectionUtils.isEmpty(list))
		{
			return Collections.emptyList();
		}

		if (list.size() > maxElements)
		{
			return list.subList(0, maxElements);
		}

		return list;
	}

	/**
	 * Get the default search page size.
	 *
	 * @return the number of results per page, <tt>0</tt> (zero) indicated 'default' size should be used
	 */
	protected int getSearchPageSize()
	{
		return getSiteConfigService().getInt("storefront.search.productSearchPageSize", 0);
	}

	protected PageableData createPageableData(final int pageNumber, final int pageSize, final String sortCode,
			final ShowMode showMode)
	{
		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(pageNumber);
		pageableData.setSort(sortCode);

		if (ShowMode.All == showMode)
		{
			pageableData.setPageSize(MAX_PAGE_LIMIT);
		}
		else
		{
			pageableData.setPageSize(pageSize);
		}
		return pageableData;
	}
}

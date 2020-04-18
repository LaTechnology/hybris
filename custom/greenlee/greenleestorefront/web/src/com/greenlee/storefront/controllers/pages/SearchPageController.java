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
package com.greenlee.storefront.controllers.pages;

import de.hybris.greenlee.facades.content.data.ContentData;
import de.hybris.greenlee.facades.search.facetdata.ContentSearchPageData;
import de.hybris.platform.acceleratorcms.model.components.SearchBoxComponentModel;
import de.hybris.platform.acceleratorservices.customer.CustomerLocationService;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.SearchBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.acceleratorstorefrontcommons.util.MetaSanitizerUtil;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.commercefacades.search.data.AutocompleteResultData;
import de.hybris.platform.commercefacades.search.data.AutocompleteSuggestionData;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.FacetRefinement;
import de.hybris.platform.commerceservices.search.facetdata.FacetValueData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.greenlee.facades.search.ContentSearchFacade;
import com.sap.security.core.server.csi.XSSEncoder;


@Controller
@Scope("tenant")
@RequestMapping("/search")
public class SearchPageController extends AbstractSearchPageController
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(SearchPageController.class);

	private static final String SEARCH_META_DESCRIPTION_ON = "search.meta.description.on";
	private static final String SEARCH_META_DESCRIPTION_RESULTS = "search.meta.description.results";
	private static final String SEARCH_TEXT = "searchText";

	private static final String COMPONENT_UID_PATH_VARIABLE_PATTERN = "{componentUid:.*}";
	private static final String FACET_SEPARATOR = ":";

	private static final String SEARCH_CMS_PAGE_ID = "search";
	private static final String NO_RESULTS_CMS_PAGE_ID = "searchEmpty";
	protected static final String PRODUCT_LIST_PAGE = "search/searchListPage";
	protected static final String PRODUCT_GRID_PAGE = "search/searchGridPage";

	@Resource(name = "productSearchFacade")
	private ProductSearchFacade<ProductData> productSearchFacade;

	@Resource(name = "searchBreadcrumbBuilder")
	private SearchBreadcrumbBuilder searchBreadcrumbBuilder;

	@Resource(name = "customerLocationService")
	private CustomerLocationService customerLocationService;

	@Resource(name = "cmsComponentService")
	private CMSComponentService cmsComponentService;

	@Resource(name = "contentSearchFacade")
	private ContentSearchFacade contentSearchFacade;

	@RequestMapping(method = RequestMethod.GET, params = "!q")
	public String textSearch(@RequestParam(value = "text", defaultValue = "") final String searchText,
			final HttpServletRequest request, @RequestParam(value = "data", defaultValue = "product") final String data,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "viewAs", required = false) final String viewAs, final Model model) throws CMSItemNotFoundException
	{
		if (StringUtils.isNotBlank(searchText))
		{
			model.addAttribute(SEARCH_TEXT, searchText);
			model.addAttribute("viewAs", viewAs);
			final PageableData pageableData = createPageableData(page, getSearchPageSize(), null, ShowMode.Page);
			byte[] bytes = null;
			String encodedSearchText = null;
			try
			{
				bytes = searchText.getBytes("UTF-8");
				encodedSearchText = new String(bytes, "UTF-8");
			}
			catch (final UnsupportedEncodingException e)
			{
				LOG.info(e);
			}



			model.addAttribute("searchWord", encodedSearchText);
			final SearchStateData searchState = new SearchStateData();
			final SearchQueryData searchQueryData = new SearchQueryData();
			searchQueryData.setValue(encodedSearchText);
			searchState.setQuery(searchQueryData);
			ProductSearchPageData<SearchStateData, ProductData> searchPageData = productSearchFacade.textSearch(searchState,
					pageableData);
			ContentSearchPageData<SearchStateData, ContentData> contentSearchPageData = contentSearchFacade.textSearch(searchState,
					pageableData);
			searchPageData = encode(searchPageData);
			contentSearchPageData = encode(contentSearchPageData);


			model.addAttribute("contentSearchPageData", contentSearchPageData);

			if (searchPageData == null && contentSearchPageData == null)
			{
				storeCmsPageInModel(model, getContentPageForLabelOrId(NO_RESULTS_CMS_PAGE_ID));
			}
			else if (searchPageData != null && searchPageData.getKeywordRedirectUrl() != null)
			{
				// if the search engine returns a redirect, just
				return "redirect:" + searchPageData.getKeywordRedirectUrl();
			}
			else if (searchPageData != null && searchPageData.getPagination().getTotalNumberOfResults() == 0
					&& contentSearchPageData != null && contentSearchPageData.getPagination().getTotalNumberOfResults() == 0)
			{
				model.addAttribute("searchPageData", searchPageData);
				storeCmsPageInModel(model, getContentPageForLabelOrId(NO_RESULTS_CMS_PAGE_ID));
				updatePageTitle(encodedSearchText, model);
			}
			else
			{
				storeContinueUrl(request);
				populateModel(model, searchPageData, ShowMode.Page);
				storeCmsPageInModel(model, getContentPageForLabelOrId(SEARCH_CMS_PAGE_ID));
				updatePageTitle(encodedSearchText, model);

			}
			model.addAttribute("userLocation", customerLocationService.getUserLocation());
			getRequestContextData(request).setSearch(searchPageData);
			if (searchPageData != null)
			{
				model.addAttribute(WebConstants.BREADCRUMBS_KEY, searchBreadcrumbBuilder.getBreadcrumbs(null, encodedSearchText,
						CollectionUtils.isEmpty(searchPageData.getBreadcrumbs())));
			}
			getPageViewUsUrl(viewAs, model, searchPageData);
		}
		else
		{
			storeCmsPageInModel(model, getContentPageForLabelOrId(NO_RESULTS_CMS_PAGE_ID));
		}

		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_FOLLOW);

		final String metaDescription = MetaSanitizerUtil
				.sanitizeDescription(getMessageSource().getMessage(SEARCH_META_DESCRIPTION_RESULTS, null,
						SEARCH_META_DESCRIPTION_RESULTS, getI18nService().getCurrentLocale()) + " " + searchText + " "
				+ getMessageSource().getMessage(SEARCH_META_DESCRIPTION_ON, null, SEARCH_META_DESCRIPTION_ON,
						getI18nService().getCurrentLocale()) + " " + getSiteName());
		final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(searchText);
		setUpMetaData(model, metaKeywords, metaDescription);


		model.addAttribute("data", data);
		return getViewForPage(model);

	}

	@RequestMapping(method = RequestMethod.GET, params = "q")
	public String refineSearch(@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode,
			@RequestParam(value = "viewAs", required = false) final String viewAs,
			@RequestParam(value = "text", required = false) final String searchText, final HttpServletRequest request,
			final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute("viewAs", viewAs);
		model.addAttribute("sortCode", sortCode);

		final String searchQuery = request.getParameter("q");

		final ProductSearchPageData<SearchStateData, ProductData> searchPageData = performSearch(searchQuery, page, showMode,
				sortCode, getSearchPageSize());

		String encodedSearchText = searchQuery.split(":")[0];
		encodedSearchText = encodedSearchText.split("&")[0];

		model.addAttribute("searchWord", encodedSearchText);
		populateModel(model, searchPageData, showMode);

		//Block for doing content search
		final SearchStateData searchState = new SearchStateData();
		final SearchQueryData searchQueryData = new SearchQueryData();
		searchQueryData.setValue(searchQuery);
		searchState.setQuery(searchQueryData);
		model.addAttribute(SEARCH_TEXT, searchText);

		final PageableData pageableData = createPageableData(page, getSearchPageSize(), sortCode, showMode);



		ContentSearchPageData<SearchStateData, ContentData> contentSearchPageData = contentSearchFacade.textSearch(searchState,
				pageableData);

		contentSearchPageData = encode(contentSearchPageData);

		model.addAttribute("contentSearchPageData", contentSearchPageData);

		model.addAttribute("userLocation", customerLocationService.getUserLocation());

		if (searchPageData != null && searchPageData.getPagination().getTotalNumberOfResults() == 0)
		{
			updatePageTitle(searchPageData.getFreeTextSearch(), model);
			storeCmsPageInModel(model, getContentPageForLabelOrId(NO_RESULTS_CMS_PAGE_ID));
		}
		else
		{
			storeContinueUrl(request);

			storeCmsPageInModel(model, getContentPageForLabelOrId(SEARCH_CMS_PAGE_ID));
		}
		if (searchPageData != null)
		{
			encodedSearchText = (encodedSearchText == null || encodedSearchText.isEmpty()) ? "Shop" : encodedSearchText;
			model.addAttribute(WebConstants.BREADCRUMBS_KEY, searchBreadcrumbBuilder.getBreadcrumbs(null, encodedSearchText,
					CollectionUtils.isEmpty(searchPageData.getBreadcrumbs())));
		}


		final String metaDescription = MetaSanitizerUtil
				.sanitizeDescription(getMessageSource().getMessage(SEARCH_META_DESCRIPTION_RESULTS, null,
						SEARCH_META_DESCRIPTION_RESULTS, getI18nService().getCurrentLocale()) + " " + searchText + " "
				+ getMessageSource().getMessage(SEARCH_META_DESCRIPTION_ON, null, SEARCH_META_DESCRIPTION_ON,
						getI18nService().getCurrentLocale()) + " " + getSiteName());

		final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(searchText);
		setUpMetaData(model, metaKeywords, metaDescription);

		getPageViewUsUrl(viewAs, model, searchPageData);

		String data = "product";
		if (request.getParameter("data") != null)
		{
			data = request.getParameter("data");
		}
		model.addAttribute("data", data);
		model.addAttribute(SEARCH_TEXT, searchQuery);

		if ("grid".equals(viewAs))
		{
			return PAGE_ROOT + PRODUCT_GRID_PAGE;
		}
		else
		{
			return PAGE_ROOT + PRODUCT_LIST_PAGE;
		}


	}


	protected ProductSearchPageData<SearchStateData, ProductData> performSearch(final String searchQuery, final int page,
			final ShowMode showMode, final String sortCode, final int pageSize)
	{
		final PageableData pageableData = createPageableData(page, pageSize, sortCode, showMode);

		final SearchStateData searchState = new SearchStateData();
		final SearchQueryData searchQueryData = new SearchQueryData();
		searchQueryData.setValue(searchQuery);
		searchState.setQuery(searchQueryData);

		return encode(productSearchFacade.textSearch(searchState, pageableData));
	}

	@ResponseBody
	@RequestMapping(value = "/results", method = RequestMethod.GET)
	public SearchResultsData<ProductData> jsonSearchResults(@RequestParam("q") final String searchQuery,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode) throws CMSItemNotFoundException
	{
		final ProductSearchPageData<SearchStateData, ProductData> searchPageData = performSearch(searchQuery, page, showMode,
				sortCode, getSearchPageSize());
		final SearchResultsData<ProductData> searchResultsData = new SearchResultsData<>();
		searchResultsData.setResults(searchPageData.getResults());
		searchResultsData.setPagination(searchPageData.getPagination());
		return searchResultsData;
	}

	@ResponseBody
	@RequestMapping(value = "/facets", method = RequestMethod.GET)
	public FacetRefinement<SearchStateData> getFacets(@RequestParam("q") final String searchQuery,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode) throws CMSItemNotFoundException
	{
		final SearchStateData searchState = new SearchStateData();
		final SearchQueryData searchQueryData = new SearchQueryData();
		searchQueryData.setValue(searchQuery);
		searchState.setQuery(searchQueryData);

		final ProductSearchPageData<SearchStateData, ProductData> searchPageData = productSearchFacade.textSearch(searchState,
				createPageableData(page, getSearchPageSize(), sortCode, showMode));
		final List<FacetData<SearchStateData>> facets = refineFacets(searchPageData.getFacets(),
				convertBreadcrumbsToFacets(searchPageData.getBreadcrumbs()));
		final FacetRefinement<SearchStateData> refinement = new FacetRefinement<>();
		refinement.setFacets(facets);
		refinement.setCount(searchPageData.getPagination().getTotalNumberOfResults());
		refinement.setBreadcrumbs(searchPageData.getBreadcrumbs());
		return refinement;
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
			final List<AutocompleteSuggestionData> productSuggestions = subList(productSearchFacade.getAutocompleteSuggestions(term),
					component.getMaxSuggestions());
			final List<AutocompleteSuggestionData> contentSuggestions = subList(contentSearchFacade.getAutocompleteSuggestions(term),
					component.getMaxSuggestions());
			final List<AutocompleteSuggestionData> suggestions = new ArrayList<>();
			suggestions.addAll(productSuggestions);
			suggestions.addAll(contentSuggestions);
			resultData.setSuggestions(suggestions);
		}

		if (component.isDisplayProducts())
		{
			resultData.setProducts(subList(productSearchFacade.textSearch(term).getResults(), component.getMaxProducts()));
			resultData.setContent(subList(contentSearchFacade.textSearch(term).getResults(), component.getMaxProducts()));
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

	protected void updatePageTitle(final String searchText, final Model model)
	{
		storeContentPageTitleInModel(model, getPageTitleResolver().resolveContentPageTitle(
				getMessageSource().getMessage("search.meta.title", null, "search.meta.title", getI18nService().getCurrentLocale())
						+ " " + searchText));
	}

	@Override
	protected void populateModel(final Model model, final SearchPageData<?> searchPageData, final ShowMode showMode)
	{
		super.populateModel(model, searchPageData, showMode);
	}

	private ProductSearchPageData<SearchStateData, ProductData> encode(
			final ProductSearchPageData<SearchStateData, ProductData> searchPageData)
	{
		final SearchStateData currentQuery = searchPageData.getCurrentQuery();

		if (currentQuery != null)
		{
			try
			{
				final SearchQueryData query = currentQuery.getQuery();
				final String encodedQueryValue = XSSEncoder.encodeHTML(query.getValue());
				query.setValue(encodedQueryValue);
				currentQuery.setQuery(query);
				searchPageData.setCurrentQuery(currentQuery);
				searchPageData.setFreeTextSearch(XSSEncoder.encodeHTML(searchPageData.getFreeTextSearch()));

				final List<FacetData<SearchStateData>> facets = searchPageData.getFacets();
				if (CollectionUtils.isNotEmpty(facets))
				{
					populateFacets(facets);
				}

			}
			catch (final UnsupportedEncodingException e)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Error occured during Encoding the Search Page data values", e);
				}
			}
		}
		return searchPageData;
	}


	/**
	 * @param viewAs
	 * @param model
	 * @param searchPageData
	 */
	protected static void getPageViewUsUrl(final String viewAs, final Model model,
			final ProductSearchPageData<SearchStateData, ProductData> searchPageData)
	{
		//Set Current page view
		if (searchPageData != null)
		{
			final String url = searchPageData.getCurrentQuery().getUrl();
			model.addAttribute("viewUrl", url);
			if (StringUtils.isNotEmpty(viewAs))
			{
				searchPageData.getCurrentQuery().setUrl(url + "&viewAs=" + viewAs);
			}
		}
	}


	private ContentSearchPageData<SearchStateData, ContentData> encode(
			final ContentSearchPageData<SearchStateData, ContentData> searchPageData)
	{
		final SearchStateData currentQuery = searchPageData.getCurrentQuery();

		if (currentQuery != null)
		{
			try
			{
				final SearchQueryData query = currentQuery.getQuery();
				final String encodedQueryValue = XSSEncoder.encodeHTML(query.getValue());
				query.setValue(encodedQueryValue);
				currentQuery.setQuery(query);
				searchPageData.setCurrentQuery(currentQuery);
				searchPageData.setFreeTextSearch(XSSEncoder.encodeHTML(searchPageData.getFreeTextSearch()));

				final List<FacetData<SearchStateData>> facets = searchPageData.getFacets();
				if (CollectionUtils.isNotEmpty(facets))
				{
					populateFacets(facets);
				}

			}
			catch (final UnsupportedEncodingException e)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Error occured during Encoding the Search Page data values", e);
				}
			}
		}
		return searchPageData;
	}

	private void populateFacets(final List<FacetData<SearchStateData>> facets) throws UnsupportedEncodingException
	{

		for (final FacetData<SearchStateData> facetData : facets)
		{
			final List<FacetValueData<SearchStateData>> facetValueDatas = facetData.getValues();
			if (CollectionUtils.isNotEmpty(facetValueDatas))
			{
				populateFacetValueData(facetValueDatas);
			}

			final List<FacetValueData<SearchStateData>> topFacetValueDatas = facetData.getTopValues();
			if (CollectionUtils.isNotEmpty(topFacetValueDatas))
			{
				populateFacetValueData(topFacetValueDatas);
			}
		}

	}

	private void populateFacetValueData(final List<FacetValueData<SearchStateData>> facetValueDatas)
			throws UnsupportedEncodingException
	{

		for (final FacetValueData<SearchStateData> facetValueData : facetValueDatas)
		{
			final SearchStateData facetQuery = facetValueData.getQuery();
			final SearchQueryData queryData = facetQuery.getQuery();
			final String queryValue = queryData.getValue();
			if (StringUtils.isNotBlank(queryValue))
			{
				final String[] queryValues = queryValue.split(FACET_SEPARATOR);
				final StringBuilder queryValueBuilder = new StringBuilder();
				queryValueBuilder.append(XSSEncoder.encodeHTML(queryValues[0]));
				for (int i = 1; i < queryValues.length; i++)
				{
					queryValueBuilder.append(FACET_SEPARATOR).append(queryValues[i]);
				}
				queryData.setValue(queryValueBuilder.toString());
			}
		}

	}

}
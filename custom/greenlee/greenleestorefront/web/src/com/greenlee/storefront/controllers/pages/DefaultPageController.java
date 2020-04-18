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

import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.ContentPageBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UrlPathHelper;

import com.greenlee.storefront.controllers.ControllerConstants;


/**
 * Error handler to show a CMS managed error page. This is the catch-all controller that handles all GET requests that
 * are not handled by other controllers.
 */
@Controller
@Scope("tenant")
//@RequestMapping()
public class DefaultPageController extends AbstractPageController
{
	private static final Logger LOG = Logger.getLogger(DefaultPageController.class);
	private static final String ERROR_CMS_PAGE = "notFound";

	private static final String SERVER_ERROR_CMS_PAGE = "serverError";

	private final UrlPathHelper urlPathHelper = new UrlPathHelper();

	@Resource(name = "simpleBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;

	@Resource(name = "contentPageBreadcrumbBuilder")
	private ContentPageBreadcrumbBuilder contentPageBreadcrumbBuilder;

	@RequestMapping(method = RequestMethod.GET)
	public String get(final Model model, final HttpServletRequest request, final HttpServletResponse response)
			throws CMSItemNotFoundException
	{
		// Check for CMS Page where label or id is like /page
		final ContentPageModel pageForRequest = getContentPageForRequest(request);
		if (pageForRequest != null)
		{
			storeCmsPageInModel(model, pageForRequest);
			setUpMetaDataForContentPage(model, pageForRequest);
			model.addAttribute(WebConstants.BREADCRUMBS_KEY, contentPageBreadcrumbBuilder.getBreadcrumbs(pageForRequest));
			return getViewForPage(pageForRequest);
		}

		// No page found - display the notFound page with error from controller
		storeCmsPageInModel(model, getContentPageForLabelOrId(ERROR_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ERROR_CMS_PAGE));

		model.addAttribute(WebConstants.MODEL_KEY_ADDITIONAL_BREADCRUMB,
				resourceBreadcrumbBuilder.getBreadcrumbs("breadcrumb.not.found"));
		GlobalMessages.addErrorMessage(model, "system.error.page.not.found");

		response.setStatus(HttpServletResponse.SC_NOT_FOUND);

		return ControllerConstants.Views.Pages.Error.ErrorNotFoundPage;
	}

	/**
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = "/server-error", method = RequestMethod.GET)
	public String getServerError(final Model model, final HttpServletRequest request, final HttpServletResponse response)
			throws CMSItemNotFoundException
	{
		// 500 server error 
		storeCmsPageInModel(model, getContentPageForLabelOrId(SERVER_ERROR_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(SERVER_ERROR_CMS_PAGE));

		model.addAttribute(WebConstants.MODEL_KEY_ADDITIONAL_BREADCRUMB,
				resourceBreadcrumbBuilder.getBreadcrumbs("breadcrumb.not.found"));

		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		return ControllerConstants.Views.Pages.Error.ServerErrorPage;
	}

	/**
	 * Lookup the CMS Content Page for this request.
	 * 
	 * @param request
	 *           The request
	 * @return the CMS content page
	 */
	protected ContentPageModel getContentPageForRequest(final HttpServletRequest request)
	{
		// Get the path for this request.
		// Note that the path begins with a '/'
		final String lookupPathForRequest = urlPathHelper.getLookupPathForRequest(request);

		try
		{
			// Lookup the CMS Content Page by label. Note that the label value must begin with a '/'.
			return getCmsPageService().getPageForLabel(lookupPathForRequest);
		}
		catch (final CMSItemNotFoundException ignore)
		{
			LOG.error(ignore);
		}
		return null;
	}
}

/**
 *
 */
package com.greenlee.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * @author aruna
 *
 */
@Controller
@RequestMapping(value = "/thankyou")
public class ThankyouPageController extends AbstractPageController
{
	@Resource(name = "thankYouPageBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder thankYouPageBreadcrumbBuilder;

	private static final String THANKU_CMS_PAGE = "thankyouPage";

	@RequestMapping(method = RequestMethod.GET)
	public String thankuPage(final Model model) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(THANKU_CMS_PAGE));
		model.addAttribute("breadcrumbs", thankYouPageBreadcrumbBuilder.getBreadcrumbs(null));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return getViewForPage(model);
	}
}

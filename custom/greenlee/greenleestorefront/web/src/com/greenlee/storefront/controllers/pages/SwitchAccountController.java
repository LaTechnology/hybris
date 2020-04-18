/**
 *
 */
package com.greenlee.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;

import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.greenlee.core.constants.GreenleeCoreConstants;
import com.greenlee.core.model.GreenleeB2BCustomerModel;
import com.greenlee.core.util.CommonUtils;
import com.greenlee.facades.customer.GreenleeCommerceFacade;
import com.greenlee.facades.customer.GreenleeCustomerFacade;


/**
 * @author raja.santhanam
 *
 */

@Controller
@Scope("tenant")
@RequestMapping("/switch-account")
public class SwitchAccountController extends AbstractPageController
{
	private static final String ACCOUNT_CMS_PAGE = "switchaccount";
	private static final String MY_ACCOUNT = "/my-account";
	private static final String SWITCH_ACCOUNT = "/switch-account";
	private static final String SAP_ORDER_IDS = "SAP_ORDER_IDS";
	private static final String CART_PAGE_URL = "/cart";
	private static final String REDIRECT_TO_PRICE_CHECK = "/realTimePriceCheck";
	private static final Logger LOG = Logger.getLogger(SwitchAccountController.class);

	@Resource(name = "greenleeCustomerFacade")
	private GreenleeCustomerFacade customerFacade;

	@Resource(name = "commerceFacade")
	private GreenleeCommerceFacade commerceFacade;

	@Resource(name = "simpleBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder breadcrumbBuilder;

	@Resource(name = "cartFacade")
	private CartFacade cartFacade;

	@Resource(name = "userService")
	private UserService userService;

	@RequestMapping(method = RequestMethod.GET)
	public String account(final Model model, final RedirectAttributes redirectModel, final HttpSession session)
			throws CMSItemNotFoundException
	{

		//In case logout is clicked in switch account page, we need to handle as below other wise there will be a classcast exception.
		final UserModel currentUser = userService.getCurrentUser();
		if (!(currentUser instanceof GreenleeB2BCustomerModel))
		{
			return REDIRECT_PREFIX + ROOT;
		}
		final GreenleeB2BCustomerModel customerModel = (GreenleeB2BCustomerModel) currentUser;
		final List<B2BUnitData> switchableB2BUnits = commerceFacade.getSwitchableUnitsOfOrganization(customerModel);
		model.addAttribute("secondaryUnits", switchableB2BUnits);
		session.setAttribute(GreenleeCoreConstants.SWITCHABLE_SESSION_B2BUNIT_KEY, switchableB2BUnits);

		final int thresholdToShwSearch = Config.getInt(GreenleeCoreConstants.CONFKEY_SWTCHACCT_LIMIT_TOSHOW_SEARCHBOX, 10);
		session.setAttribute(GreenleeCoreConstants.SWTCHACCT_LIMIT_TOSHOW_SEARCHBOX, Integer.valueOf(thresholdToShwSearch));

		storeCmsPageInModel(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
		model.addAttribute("breadcrumbs", breadcrumbBuilder.getBreadcrumbs("text.breadcrumb.switchaccount"));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return getViewForPage(model);
	}

	@RequestMapping(value = "/setDefaultAccount", method =
	{ RequestMethod.GET, RequestMethod.POST })
	public String defaultAccount(@RequestParam("user") final String user, @RequestParam("unit") final String unit,
			@RequestParam(value = "requestURI", required = false) final String requestURI, final Model model,
			final HttpServletRequest request, @RequestParam(value = "redirectTo", required = false) final String redirectTo)
			throws CMSItemNotFoundException
	{
		String uri = requestURI;
		if (StringUtils.isBlank(redirectTo))
		{
			uri = getReturnRedirectUrl(request);
		}
		if (StringUtils.isNotBlank(redirectTo))
		{
			uri = REDIRECT_PREFIX + redirectTo;
		}
		if (StringUtils.equals(requestURI, SWITCH_ACCOUNT) && StringUtils.isBlank(redirectTo))
		{
			uri = REDIRECT_PREFIX + MY_ACCOUNT;
		}

		final B2BUnitData b2bUnit = customerFacade.updateSelectedB2BUnit(user, unit);
		request.getSession().setAttribute(GreenleeCoreConstants.SESSION_B2BUNIT_KEY, b2bUnit);
		request.getSession().setAttribute(SAP_ORDER_IDS, null);
		if (b2bUnit != null)
		{
			LOG.debug(CommonUtils.toStringFormatter("The session b2bunit set:", b2bUnit.getUid()));
		}

		return uri;
	}

	protected String getReturnRedirectUrl(final HttpServletRequest request)
	{
		String referer = request.getHeader("Referer");
		if (referer != null && !referer.isEmpty())
		{
			if (referer.endsWith(CART_PAGE_URL) && cartFacade.getSessionCart().isIsErpPrice())
			{
				referer = referer + REDIRECT_TO_PRICE_CHECK;
			}
			return REDIRECT_PREFIX + referer;
		}
		return REDIRECT_PREFIX + '/';
	}
}

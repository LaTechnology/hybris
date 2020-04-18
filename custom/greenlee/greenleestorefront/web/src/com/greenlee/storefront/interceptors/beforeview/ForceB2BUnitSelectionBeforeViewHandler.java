/**
 *
 */
package com.greenlee.storefront.interceptors.beforeview;



import de.hybris.platform.b2b.company.B2BCommerceUnitService;
import de.hybris.platform.b2bacceleratorservices.company.CompanyB2BCommerceService;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.ModelAndView;

import com.greenlee.core.constants.GreenleeCoreConstants;
import com.greenlee.core.enums.UserTypes;
import com.greenlee.core.model.GreenleeB2BCustomerModel;
import com.greenlee.facades.customer.GreenleeCommerceFacade;
import com.greenlee.facades.customer.GreenleeCustomerFacade;
import com.greenlee.storefront.interceptors.BeforeViewHandler;


/**
 * @author raja.santhanam
 *
 */
public class ForceB2BUnitSelectionBeforeViewHandler implements BeforeViewHandler
{
	private static final String CHECKOUT_URL = "/checkout";
	private static final String SWITCHACCT_URL = "/switch-account";
	private static final String CART_URL = "/cart";
	private static final String LOGIN_URL = "/login";
	private UserService userService;
	private GreenleeCommerceFacade commerceFacade;
	private RedirectStrategy redirectStrategy;
	private RequestMatcher requestMatcher;
	private CompanyB2BCommerceService companyB2BCommerceService;
	private GreenleeCustomerFacade customerFacade;
	private B2BCommerceUnitService b2bUnitService;

	@Resource(name = "userFacade")
	private UserFacade userFacade;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.greenlee.storefront.interceptors.BeforeViewHandler#beforeView(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public void beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelAndView modelAndView)
			throws Exception
	{
		if (requestMatcher.matches(request))
		{
			forceSwitchAccount(request, response);
		}
	}

	private void forceSwitchAccount(final HttpServletRequest request, final HttpServletResponse response) throws IOException
	{
		final String redirectTo = request.getServletPath();
		final String switched = request.getParameter("switched");
		final UserModel currentUser = userService.getCurrentUser();
		if (currentUser instanceof GreenleeB2BCustomerModel)
		{
			final GreenleeB2BCustomerModel customer = (GreenleeB2BCustomerModel) currentUser;
			if (customer.getSessionB2BUnit() == null)
			{
				final List<B2BUnitData> b2bUnitDataList = commerceFacade.getSwitchableUnitsOfOrganization(customer);
				final B2BUnitData b2bUnitFromSession = (B2BUnitData) request.getSession()
						.getAttribute(GreenleeCoreConstants.SESSION_B2BUNIT_KEY);

				//      In case,
				// Step 1 - remember me is chosen for a multi account user(and browser is closed)
				// Step 2 - and on accessing a secured page
				// Step 3 - after choosing a account, when login is prompted,
				// when a different user is entered, we need to clear the b2bunit stored in session (in step 3),
				// else there will be a wrong b2bunit assignment.
				boolean shdRemoveStaleB2BUnitFrmSession = true;
				if (b2bUnitFromSession != null)
				{
					for (final B2BUnitData b2bUnitData : b2bUnitDataList)
					{
						if (StringUtils.equals(b2bUnitFromSession.getUid(), b2bUnitData.getUid()) || b2bUnitDataList.size() == 1)
						{
							shdRemoveStaleB2BUnitFrmSession = false;
							break;
						}
					}
					if (shdRemoveStaleB2BUnitFrmSession)
					{
						request.getSession().removeAttribute(GreenleeCoreConstants.SESSION_B2BUNIT_KEY);
					}
				}
				if (b2bUnitDataList.size() == 1)
				{
					final B2BUnitData parentUnit = b2bUnitDataList.get(0);
					final B2BUnitData b2bUnit = customerFacade.updateSelectedB2BUnit(currentUser.getUid(), parentUnit.getUid());
					request.getSession().setAttribute(GreenleeCoreConstants.SESSION_B2BUNIT_KEY, b2bUnit);
					request.getSession().removeAttribute(GreenleeCoreConstants.SWITCHABLE_SESSION_B2BUNIT_KEY);
					StringBuilder builder = new StringBuilder(redirectTo);
					// Need to check the below condition and redirect the user to home page
					// otherwise we will end up showing a blank switch account page. This situation can happen only in
					// remember me flow for a returning customer logging in as a different user.
					if (redirectTo.contains(SWITCHACCT_URL))
					{
						builder = new StringBuilder("/");
					}
					builder = getRedirectURL(builder, request);
					redirectStrategy.sendRedirect(request, response, builder.toString());
				}
				else if (!isUserB2COrB2E(b2bUnitDataList))
				{
					request.getSession().setAttribute(GreenleeCoreConstants.SWITCHABLE_SESSION_B2BUNIT_KEY, b2bUnitDataList);
					final StringBuilder builder = new StringBuilder();
					builder.append("/switch-account?switched=false");
					if (redirectTo.contains(CHECKOUT_URL) || redirectTo.contains(CART_URL))
					{
						builder.append("&redirectTo=");
						builder.append(redirectTo);
					}
					if (StringUtils.isBlank(switched))
					{
						redirectStrategy.sendRedirect(request, response, builder.toString());
					}
				}
			}
		}
	}

	private boolean isUserB2COrB2E(final List<B2BUnitData> b2bUnitDataList)
	{
		boolean isB2COrB2E = false;
		for (final B2BUnitData unitData : b2bUnitDataList)
		{
			if (StringUtils.equalsIgnoreCase(unitData.getUserType(), UserTypes.B2E.getCode())
					|| StringUtils.equalsIgnoreCase(unitData.getUserType(), UserTypes.B2C.getCode()))
			{
				isB2COrB2E = true;
			}
		}
		return isB2COrB2E;
	}

	private StringBuilder getRedirectURL(final StringBuilder builder, final HttpServletRequest request)
	{
		final Enumeration<String> params = request.getParameterNames();
		int flag = 0;
		while (params.hasMoreElements())
		{
			final String param = params.nextElement();
			if (flag == 0)
			{
				builder.append("?");
			}
			else
			{
				builder.append("&");
			}
			builder.append(param);
			builder.append("=");
			builder.append(request.getParameter(param));
			flag++;
		}
		return builder;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @return the commerceFacade
	 */
	public GreenleeCommerceFacade getCommerceFacade()
	{
		return commerceFacade;
	}

	/**
	 * @param commerceFacade
	 *           the commerceFacade to set
	 */
	public void setCommerceFacade(final GreenleeCommerceFacade commerceFacade)
	{
		this.commerceFacade = commerceFacade;
	}

	/**
	 * @return the redirectStrategy
	 */
	public RedirectStrategy getRedirectStrategy()
	{
		return redirectStrategy;
	}

	/**
	 * @param redirectStrategy
	 *           the redirectStrategy to set
	 */
	public void setRedirectStrategy(final RedirectStrategy redirectStrategy)
	{
		this.redirectStrategy = redirectStrategy;
	}

	/**
	 * @return the requestMatcher
	 */
	public RequestMatcher getRequestMatcher()
	{
		return requestMatcher;
	}

	/**
	 * @param requestMatcher
	 *           the requestMatcher to set
	 */
	public void setRequestMatcher(final RequestMatcher requestMatcher)
	{
		this.requestMatcher = requestMatcher;
	}

	/**
	 * @return the companyB2BCommerceService
	 */
	public CompanyB2BCommerceService getCompanyB2BCommerceService()
	{
		return companyB2BCommerceService;
	}

	/**
	 * @param companyB2BCommerceService
	 *           the companyB2BCommerceService to set
	 */
	public void setCompanyB2BCommerceService(final CompanyB2BCommerceService companyB2BCommerceService)
	{
		this.companyB2BCommerceService = companyB2BCommerceService;
	}

	/**
	 * @return the b2bUnitService
	 */
	public B2BCommerceUnitService getB2bUnitService()
	{
		return b2bUnitService;
	}

	/**
	 * @param b2bUnitService
	 *           the b2bUnitService to set
	 */
	public void setB2bUnitService(final B2BCommerceUnitService b2bUnitService)
	{
		this.b2bUnitService = b2bUnitService;
	}

	/**
	 * @return the customerFacade
	 */
	public GreenleeCustomerFacade getCustomerFacade()
	{
		return customerFacade;
	}

	/**
	 * @param customerFacade
	 *           the customerFacade to set
	 */
	public void setCustomerFacade(final GreenleeCustomerFacade customerFacade)
	{
		this.customerFacade = customerFacade;
	}

}

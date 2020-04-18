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
package com.greenlee.storefront.security;

import de.hybris.platform.acceleratorservices.uiexperience.UiExperienceService;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;

import de.hybris.platform.acceleratorstorefrontcommons.security.BruteForceAttackCounter;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.commerceservices.order.CommerceCartMergingException;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import com.greenlee.core.enums.UserTypes;
import com.greenlee.facades.customer.GreenleeCommerceFacade;


/**
 * Success handler initializing user settings, restoring or merging the cart and ensuring the cart is handled correctly.
 * Cart restoration is stored in the session since the request coming in is that to j_spring_security_check and will be
 * redirected
 */
public class StorefrontAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler
{
	private CustomerFacade customerFacade;
	private UiExperienceService uiExperienceService;
	private CartFacade cartFacade;
	private SessionService sessionService;
	private BruteForceAttackCounter bruteForceAttackCounter;
	private Map<UiExperienceLevel, Boolean> forceDefaultTargetForUiExperienceLevel;
	private List<String> restrictedPages;
	private List<String> listRedirectUrlsForceDefaultTarget;

	private GreenleeCommerceFacade commerceFacade;
	@Resource(name = "userService")
	private UserService userService;
	private static String CHECKOUT_URL = "/checkout";
	private static String CART_URL = "/cart";
	private static String CART_MERGED = "cartMerged";
	private static String HOME_URL = "/";

	private static final Logger LOG = Logger.getLogger(StorefrontAuthenticationSuccessHandler.class);

	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
			final Authentication authentication) throws IOException, ServletException
	{
		//if redirected from some specific url, need to remove the cachedRequest to force use defaultTargetUrl
		final RequestCache requestCache = new HttpSessionRequestCache();
		final SavedRequest savedRequest = requestCache.getRequest(request, response);
		for (final String redirectUrlForceDefaultTarget : getListRedirectUrlsForceDefaultTarget())
		{
			if (savedRequest != null && savedRequest.getRedirectUrl().contains(redirectUrlForceDefaultTarget))
			{
				requestCache.removeRequest(request, response);
				break;
			}
		}

		getCustomerFacade().loginSuccess();
		request.setAttribute(CART_MERGED, Boolean.FALSE);

		if (!getCartFacade().hasEntries())
		{
			getSessionService().setAttribute(WebConstants.CART_RESTORATION_SHOW_MESSAGE, Boolean.TRUE);
			try
			{
				getSessionService().setAttribute(WebConstants.CART_RESTORATION, getCartFacade().restoreSavedCart(null));
			}
			catch (final CommerceCartRestorationException e)
			{
				getSessionService().setAttribute(WebConstants.CART_RESTORATION_ERROR_STATUS,
						WebConstants.CART_RESTORATION_ERROR_STATUS);
			}
		}
		else
		{
			final String sessionCartGuid = getCartFacade().getSessionCartGuid();
			final String mostRecentSavedCartGuid = getMostRecentSavedCartGuid(sessionCartGuid);
			if (StringUtils.isNotEmpty(mostRecentSavedCartGuid))
			{
				getSessionService().setAttribute(WebConstants.CART_RESTORATION_SHOW_MESSAGE, Boolean.TRUE);
				try
				{
					getSessionService().setAttribute(WebConstants.CART_RESTORATION,
							getCartFacade().restoreCartAndMerge(mostRecentSavedCartGuid, sessionCartGuid));
					request.setAttribute(CART_MERGED, Boolean.TRUE);
				}
				catch (final CommerceCartRestorationException e)
				{
					getSessionService().setAttribute(WebConstants.CART_RESTORATION_ERROR_STATUS,
							WebConstants.CART_RESTORATION_ERROR_STATUS);
				}
				catch (final CommerceCartMergingException e)
				{
					LOG.error("User saved cart could not be merged");
				}
			}
		}

		getBruteForceAttackCounter().resetUserCounter(getCustomerFacade().getCurrentCustomerUid());
		super.onAuthenticationSuccess(request, response, authentication);
	}

	protected List<String> getRestrictedPages()
	{
		return restrictedPages;
	}

	public void setRestrictedPages(final List<String> restrictedPages)
	{
		this.restrictedPages = restrictedPages;
	}

	protected CartFacade getCartFacade()
	{
		return cartFacade;
	}

	@Required
	public void setCartFacade(final CartFacade cartFacade)
	{
		this.cartFacade = cartFacade;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	protected CustomerFacade getCustomerFacade()
	{
		return customerFacade;
	}

	@Required
	public void setCustomerFacade(final CustomerFacade customerFacade)
	{
		this.customerFacade = customerFacade;
	}

	/*
	 * @see org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler#
	 * isAlwaysUseDefaultTargetUrl()
	 */
	@Override
	protected boolean isAlwaysUseDefaultTargetUrl()
	{
		final UiExperienceLevel uiExperienceLevel = getUiExperienceService().getUiExperienceLevel();
		if (getForceDefaultTargetForUiExperienceLevel().containsKey(uiExperienceLevel))
		{
			return Boolean.TRUE.equals(getForceDefaultTargetForUiExperienceLevel().get(uiExperienceLevel));
		}
		else
		{
			return false;
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

	@Override
	protected String determineTargetUrl(final HttpServletRequest request, final HttpServletResponse response)
	{
		String targetUrl = super.determineTargetUrl(request, response);
		/*
		 * final GreenleeB2BCustomerModel customer = (GreenleeB2BCustomerModel) userService.getCurrentUser(); final
		 * List<B2BUnitData> b2bUnitDataList = commerceFacade.getSwitchableUnitsOfOrganization(customer); if
		 * (isUserB2COrB2E(b2bUnitDataList)) { targetUrl = HOME_URL; }
		 */

		if (CollectionUtils.isNotEmpty(getRestrictedPages()))
		{
			for (final String restrictedPage : getRestrictedPages())
			{
				// When logging in from a restricted page, return user to homepage.
				if (targetUrl.contains(restrictedPage))
				{
					targetUrl = super.getDefaultTargetUrl();
				}
			}
		}
		/*
		 * If the cart has been merged and the user logging in through checkout, redirect to the cart page to display the
		 * new cart
		 */
		if (StringUtils.equals(targetUrl, CHECKOUT_URL) && ((Boolean) request.getAttribute(CART_MERGED)).booleanValue())
		{
			targetUrl = CART_URL;
		}



		return targetUrl;
	}

	/**
	 * Determine the most recent saved cart of a user for the site that is not the current session cart. The current
	 * session cart is already owned by the user and for the merging functionality to work correctly the most recently
	 * saved cart must be determined. getMostRecentCartGuidForUser(excludedCartsGuid) returns the cart guid which is
	 * ordered by modified time and is not the session cart.
	 *
	 * @param currentCartGuid
	 * @return most recently saved cart guid
	 */
	protected String getMostRecentSavedCartGuid(final String currentCartGuid)
	{
		return getCartFacade().getMostRecentCartGuidForUser(Arrays.asList(currentCartGuid));
	}

	protected Map<UiExperienceLevel, Boolean> getForceDefaultTargetForUiExperienceLevel()
	{
		return forceDefaultTargetForUiExperienceLevel;
	}

	@Required
	public void setForceDefaultTargetForUiExperienceLevel(
			final Map<UiExperienceLevel, Boolean> forceDefaultTargetForUiExperienceLevel)
	{
		this.forceDefaultTargetForUiExperienceLevel = forceDefaultTargetForUiExperienceLevel;
	}

	protected BruteForceAttackCounter getBruteForceAttackCounter()
	{
		return bruteForceAttackCounter;
	}

	@Required
	public void setBruteForceAttackCounter(final BruteForceAttackCounter bruteForceAttackCounter)
	{
		this.bruteForceAttackCounter = bruteForceAttackCounter;
	}

	protected UiExperienceService getUiExperienceService()
	{
		return uiExperienceService;
	}

	@Required
	public void setUiExperienceService(final UiExperienceService uiExperienceService)
	{
		this.uiExperienceService = uiExperienceService;
	}

	protected List<String> getListRedirectUrlsForceDefaultTarget()
	{
		return listRedirectUrlsForceDefaultTarget;
	}

	@Required
	public void setListRedirectUrlsForceDefaultTarget(final List<String> listRedirectUrlsForceDefaultTarget)
	{
		this.listRedirectUrlsForceDefaultTarget = listRedirectUrlsForceDefaultTarget;
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
}

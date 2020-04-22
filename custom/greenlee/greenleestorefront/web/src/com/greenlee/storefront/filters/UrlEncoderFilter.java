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
package com.greenlee.storefront.filters;

import de.hybris.platform.acceleratorfacades.urlencoder.UrlEncoderFacade;
import de.hybris.platform.acceleratorfacades.urlencoder.data.UrlEncoderData;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.filter.OncePerRequestFilter;

import com.greenlee.storefront.web.wrappers.UrlEncodeHttpRequestWrapper;


/**
 * This filter inspects the url and inject the url attributes if any for that CMSSite. Calls facades to fetch the list
 * of attributes and encode them in the URL.
 */
public class UrlEncoderFilter extends OncePerRequestFilter
{
	private static final Logger LOG = Logger.getLogger(UrlEncoderFilter.class.getName());
	protected static final String URLENCODER_FILTER_SKIP_CURRENCY_CHANGE_PROPERTY = "urlencoderfilter.config.skip.currencychange";

	private UrlEncoderFacade urlEncoderFacade;
	private SessionService sessionService;
	private ConfigurationService configurationService;

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain) throws ServletException, IOException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(" The incoming URL : [" + request.getRequestURL().toString() + "]");
		}
		final List<UrlEncoderData> currentUrlEncoderDatas = getUrlEncoderFacade().getCurrentUrlEncodingData();
		if (currentUrlEncoderDatas != null && !currentUrlEncoderDatas.isEmpty())
		{
			final String currentPattern = getSessionService().getAttribute(WebConstants.URL_ENCODING_ATTRIBUTES);
			final String newPattern = getUrlEncoderFacade().calculateAndUpdateUrlEncodingData(request.getRequestURI().toString(),
					request.getContextPath());
			final String newPatternWithSlash = "/" + newPattern;
			if (!StringUtils.equalsIgnoreCase(currentPattern, newPatternWithSlash))
			{
				getUrlEncoderFacade().updateSiteFromUrlEncodingData();
				getSessionService().setAttribute(WebConstants.URL_ENCODING_ATTRIBUTES, newPatternWithSlash);
			}

			final UrlEncodeHttpRequestWrapper wrappedRequest = new UrlEncodeHttpRequestWrapper(request, newPattern);
			wrappedRequest.setAttribute(WebConstants.URL_ENCODING_ATTRIBUTES, newPatternWithSlash);
			wrappedRequest.setAttribute("originalContextPath",
					StringUtils.isBlank(request.getContextPath()) ? "/" : request.getContextPath());
			if (LOG.isDebugEnabled())
			{
				LOG.debug("ContextPath=[" + wrappedRequest.getContextPath() + "]" + " Servlet Path= ["
						+ wrappedRequest.getServletPath() + "]" + " Request Url= [" + wrappedRequest.getRequestURL() + "]");
			}
			filterChain.doFilter(wrappedRequest, response);
		}
		else
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(" No URL attributes defined");
			}
			request.setAttribute(WebConstants.URL_ENCODING_ATTRIBUTES, "");
			filterChain.doFilter(request, response);
		}
	}

	protected UrlEncoderFacade getUrlEncoderFacade()
	{
		return urlEncoderFacade;
	}

	@Required
	public void setUrlEncoderFacade(final UrlEncoderFacade urlEncoderFacade)
	{
		this.urlEncoderFacade = urlEncoderFacade;
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

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.web.filter.OncePerRequestFilter#shouldNotFilter(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected boolean shouldNotFilter(final HttpServletRequest request) throws ServletException
	{
		return has2SkipURL(request);
	}

	/**
	 * Checks whether the current request URL contains any of the exception URLS for cuurency change
	 *
	 * @param request
	 * @return boolean
	 */
	protected boolean has2SkipURL(final HttpServletRequest request)
	{
		final List<String> skippedUrls = Arrays.asList(StringUtils.split(
				getConfigurationService().getConfiguration().getString(URLENCODER_FILTER_SKIP_CURRENCY_CHANGE_PROPERTY, StringUtils.EMPTY),
				","));

		if (!skippedUrls.isEmpty())
		{
			for (final String url : skippedUrls)
			{
				if (request.getRequestURI().contains(url))
				{
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}
}
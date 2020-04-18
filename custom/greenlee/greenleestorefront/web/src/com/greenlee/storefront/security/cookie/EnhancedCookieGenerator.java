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
package com.greenlee.storefront.security.cookie;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.CookieGenerator;


/**
 * Enhanced {@link CookieGenerator} sets additionally header attribute {@value #HEADER_COOKIE}
 */
public class EnhancedCookieGenerator extends CookieGenerator
{
	public static final String HEADER_COOKIE = "Set-Cookie";
	public static final boolean DEFAULT_HTTP_ONLY = true;
	public static final boolean DEFAULT_COOKIE_PATH = true;

	private boolean useDefaultPath = DEFAULT_COOKIE_PATH;
	private boolean httpOnly = DEFAULT_HTTP_ONLY;
	private static final Logger LOGGER = Logger.getLogger(EnhancedCookieGenerator.class);


	protected boolean isHttpOnly()
	{
		return httpOnly;
	}

	/**
	 * Marker to choose between only cookie based session and http header as addition
	 */
	public void setHttpOnly(final boolean httpOnly)
	{
		this.httpOnly = httpOnly;
	}

	protected boolean canUseDefaultPath()
	{
		return useDefaultPath;
	}

	/**
	 * Adjusts either dynamic {@link Cookie#setPath(String)} or static assignment. If true a cookie path is calculated by
	 * {@link #setEnhancedCookiePath(Cookie)} method.
	 */
	public void setUseDefaultPath(final boolean useDefaultPath)
	{
		this.useDefaultPath = useDefaultPath;
	}

	@Override
	public void addCookie(final HttpServletResponse response, final String cookieValue)
	{
		super.addCookie(new HttpServletResponseWrapper(response)
		{
			@Override
			public void addCookie(final Cookie cookie)
			{
				setEnhancedCookiePath(cookie);
				LOGGER.error("cookieValue [" + cookieValue + "]");
				LOGGER.error("Custom code to write the cookie " + "httpOnly flag ::" + isHttpOnly());
				if (isHttpOnly())
				{
					// Custom code to write the cookie including the httpOnly flag
					LOGGER.info("Cookie Name >> " + cookie.getName());
					final StringBuffer headerBuffer = new StringBuffer(100);
					ServerCookie.appendCookieValue(headerBuffer, cookie.getVersion(), cookie.getName(), cookie.getValue(),
							cookie.getPath(), cookie.getDomain(), cookie.getComment(), cookie.getMaxAge(), cookie.getSecure(), true);
					response.addHeader(HEADER_COOKIE, headerBuffer.toString());
					LOGGER.info(headerBuffer.toString());
				}
				else
				{
					// Write the cookie as normal
					LOGGER.error("Write the cookie as normal " + "httpOnly flag ::" + isHttpOnly());
					super.addCookie(cookie);
				}
			}
		}, cookieValue);
	}

	/**
	 * Sets dynamically the {@link Cookie#setPath(String)} value using available
	 * {@link HttpServletRequest#getContextPath()}.
	 */
	protected void setEnhancedCookiePath(final Cookie cookie)
	{
		if (!canUseDefaultPath())
		{
			final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
					.getRequest();
			cookie.setPath(request.getContextPath());
		}
	}
}

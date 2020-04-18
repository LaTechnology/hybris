/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 * 
 */
package com.hybris.addon.common.url.impl;

import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.addon.common.url.MediaAbsoluteUrlResolver;


/**
 * Builds an absolute url to a resource on this server
 * 
 * @author rmcotton
 * 
 */
public class DefaultMediaAbsoluteUrlResolver implements MediaAbsoluteUrlResolver
{
	private ConfigurationService configurationService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.social.common.url.MediaAbsoluteUrlResolver#resolve(javax.servlet.http.HttpServletRequest,
	 * de.hybris.platform.commercefacades.product.data.ImageData, boolean)
	 */
	@Override
	public String resolve(final HttpServletRequest request, final ImageData image, final boolean secure)
	{
		return resolve(request, image.getUrl(), secure);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.social.common.url.MediaAbsoluteUrlResolver#resolve(javax.servlet.http.HttpServletRequest,
	 * de.hybris.platform.core.model.media.MediaModel, boolean)
	 */
	@Override
	public String resolve(final HttpServletRequest request, final MediaModel image, final boolean secure)
	{
		return resolve(request, image.getURL(), secure);
	}

	public String resolve(final HttpServletRequest request, final String urlPart, final boolean secure)
	{
		// fully qualified already
		if (StringUtils.startsWithIgnoreCase(urlPart, "http"))
		{
			return urlPart;
		}

		final StringBuilder builder = new StringBuilder(secure ? "https://" : "http://");
		builder.append(getServerName(request));

		final Integer port = secure ? getHttpsPort(request) : getHttpPort(request);
		if ((secure && port.intValue() != 443) || (!secure && port.intValue() != 80))
		{
			builder.append(":").append(port);
		}

		if (urlPart.charAt(0) != '/')
		{
			builder.append("/");
		}
		builder.append(urlPart);
		return builder.toString();
	}

	protected String getServerName(final HttpServletRequest request)
	{
		return request.getServerName();
	}

	protected Integer getHttpsPort(@SuppressWarnings("unused") final HttpServletRequest request)
	{
		final String proxySSLPort = lookupConfig("proxy.ssl.port");

		if (proxySSLPort != null)
		{
			return Integer.valueOf(proxySSLPort);
		}

		return Integer.valueOf(lookupConfig("tomcat.ssl.port"));
	}

	protected Integer getHttpPort(@SuppressWarnings("unused") final HttpServletRequest request)
	{
		final String proxyPort = lookupConfig("proxy.http.port");

		if (proxyPort != null)
		{
			return Integer.valueOf(proxyPort);
		}

		return Integer.valueOf(lookupConfig("tomcat.http.port"));
	}

	protected String lookupConfig(final String key)
	{
		return getConfigurationService().getConfiguration().getString(key, null);
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}



}

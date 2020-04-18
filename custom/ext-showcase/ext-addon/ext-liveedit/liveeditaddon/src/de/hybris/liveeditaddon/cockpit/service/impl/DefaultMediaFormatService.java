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
package de.hybris.liveeditaddon.cockpit.service.impl;

import de.hybris.liveeditaddon.cockpit.service.MediaFormatService;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;


/**
 */
public class DefaultMediaFormatService implements MediaFormatService
{
	private final String DEFAULT_MAPPING_KEY = "default";
	private Map<String, Map<String, String>> siteMediaFormatMapping;

	public Map<String, Map<String, String>> getSiteMediaFormatMapping()
	{
		return siteMediaFormatMapping;
	}

	@Required
	public void setSiteMediaFormatMapping(final Map<String, Map<String, String>> siteMediaFormatMapping)
	{
		this.siteMediaFormatMapping = siteMediaFormatMapping;
	}

	@Override
	public Map<String, String> getMediaFormatsForCurrentSite(final String siteUid)
	{
		if (siteMediaFormatMapping.containsKey(siteUid) || siteUid == null)
		{
			return siteMediaFormatMapping.get(siteUid);
		}

		else
		{
			return siteMediaFormatMapping.get(DEFAULT_MAPPING_KEY);
		}
	}



}

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
package de.hybris.liveeditaddon.cockpit.util;

import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.c2l.LanguageModel;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationLinkViewModel;


/**
 * 
 */
public class NavigationPackHelper
{

	/**
	 * Getting SOLR query from navigation link model
	 * 
	 * @return String
	 */
	public static String getSolrQueryFromNavLinkModel(final NavigationLinkViewModel navLinkModel)
	{
		if (navLinkModel != null)
		{
			try
			{
				if (StringUtils.isNotBlank(navLinkModel.getURL()))
				{
					return getQueryByUrl(navLinkModel.getURL());
				}
			}
			catch (final UnsupportedEncodingException e)
			{
				return "";
			}
		}
		return "";
	}

	private static String getQueryByUrl(final String url) throws UnsupportedEncodingException
	{
		final String regex = "\\?q=([\\W\\w]*)";
		final Pattern compile = Pattern.compile(regex);
		final Matcher matcher = compile.matcher(url);
		if (matcher.find())
		{
			final String group = matcher.group(1);
			return URLDecoder.decode(group, "UTF-8");
		}
		return "";
	}

	public static void cleanFacetValuesNames(final Collection<NavigationLinkViewModel> input)
	{
		for (final NavigationLinkViewModel link : input)
		{
			cleanFacetValueName(link);
		}
	}

	public static void cleanFacetValueName(final NavigationLinkViewModel link)
	{
		for (final String iso : getLanguageIsoCodes())
		{
			final String name = link.getNames().get(iso);
			if (name != null)
			{
				link.getNames().put(iso, cleanFacetValueText(name));
			}
		}
	}

	public static String cleanFacetValueText(String name)
	{
		if (name != null)
		{
			final int indexOf = name.indexOf("(");
			if (indexOf > 0)
			{
				name = name.substring(0, indexOf);
				name = name.trim();
			}
		}
		return name;
	}

	public static Map<String, String> getDefaultLangMap()
	{
		final Map<String, String> langMap = new HashMap<String, String>();
		for (final String iso : getLanguageIsoCodes())
		{
			langMap.put(iso, "");
		}
		return langMap;
	}

	public static List<String> getLanguageIsoCodes()
	{
		final List<String> langs = new ArrayList<String>();
		final CMSAdminSiteService service = (CMSAdminSiteService) Registry.getApplicationContext().getBean("cmsAdminSiteService");
		for (final LanguageModel iso : service.getActiveCatalogVersion().getLanguages())
		{
			langs.add(iso.getIsocode());
		}
		return langs;
	}

	public static String showAlternativeUrl(final NavigationLinkViewModel link)
	{
		if (link.getCategory() != null && !link.getCategory().getCode().isEmpty())
		{
			return link.getCategory().getCode();
		}
		if (link.getPage() != null && !link.getPage().getLabel().isEmpty())
		{
			return link.getPage().getLabel();
		}
		if (link.getProduct() != null && !link.getProduct().getCode().isEmpty())
		{
			return link.getProduct().getCode();
		}
		return "";
	}
}

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
package de.hybris.liveeditaddon.cockpit.navigationbargenerator.data;

import java.util.HashMap;
import java.util.Map;


/**
 * Created: Jun 13, 2012
 * 
 * 
 */
public class LinkComponentData extends AbstractReferencedData
{
	private String uid = "";
	private String name = "";
	private String url = "";
	private Map<String, String> linkNames = new HashMap<String, String>();

	/**
	 * @return the uid
	 */
	public String getUid()
	{
		return uid;
	}

	/**
	 * @param uid
	 *           the uid to set
	 */
	public void setUid(final String uid)
	{
		this.uid = uid;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *           the name to set
	 */
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * @return the url
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * @param url
	 *           the url to set
	 */
	public void setUrl(final String url)
	{
		this.url = url;
	}

	/**
	 * @return the linkNames Map: lang => linkName
	 */
	public Map<String, String> getLinkNames()
	{
		return linkNames;
	}

	/**
	 * @param linkNames
	 *           the linkName to set
	 */
	public void setLinkNames(final Map<String, String> linkNames)
	{
		this.linkNames = linkNames;
	}

	/**
	 * @param languageIsoCode
	 *           ISO code for the language
	 * @param value
	 *           link name
	 */
	public void addLinkName(final String languageIsoCode, final String value)
	{
		linkNames.put(languageIsoCode, value);
	}

}

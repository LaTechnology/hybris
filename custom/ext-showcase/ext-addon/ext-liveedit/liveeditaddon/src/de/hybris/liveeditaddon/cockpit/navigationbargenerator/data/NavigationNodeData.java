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

import java.util.*;


/**
 * Created: Jun 13, 2012
 * 
 * 
 */
public class NavigationNodeData extends AbstractReferencedData
{
	private String uid = "";
	private String name = "";
	private NavigationNodeData parent = null;
	private final TreeMap<String, LinkComponentData> links = new TreeMap<String, LinkComponentData>();
	private Map<String, String> titles = new HashMap<String, String>();

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
	 * @return the parent
	 */
	public NavigationNodeData getParent()
	{
		return parent;
	}

	/**
	 * @param parent
	 *           the parent to set
	 */
	public void setParent(final NavigationNodeData parent)
	{
		this.parent = parent;
	}

	/**
	 * @return the links
	 */
	public Collection<LinkComponentData> getLinks()
	{
		if (links != null)
		{
			return links.values();
		}
		return new ArrayList<LinkComponentData>();
	}

	/**
	 * @param links
	 *           the links to set
	 */
	public void setLinks(final Collection<LinkComponentData> links)
	{
		for (final LinkComponentData l : links)
		{
			this.links.put(l.getName(), l);
		}
	}

	/**
	 * Adds single link to links
	 * 
	 * @param link
	 *           to be added
	 */
	public void addLink(final LinkComponentData link)
	{
		if (!links.containsKey(link.getName()))
		{
			links.put(link.getName(), link);
		}
	}

	/**
	 * @return the titles
	 */
	public Map<String, String> getTitles()
	{
		return titles;
	}

	/**
	 * @param titles
	 *           the title to set
	 */
	public void setTitles(final Map<String, String> titles)
	{
		this.titles = titles;
	}

	/**
	 * @param title
	 *           the title to set
	 * @param languageIsoCode
	 */
	public void addTitle(final String languageIsoCode, final String title)
	{
		titles.put(languageIsoCode, title);
	}

	public Collection<String> getLinkReferences()
	{
		final List<String> references = new ArrayList<String>();
		for (final LinkComponentData l : links.values())
		{
			references.add(l.getRef());
		}
		return references;
	}
}

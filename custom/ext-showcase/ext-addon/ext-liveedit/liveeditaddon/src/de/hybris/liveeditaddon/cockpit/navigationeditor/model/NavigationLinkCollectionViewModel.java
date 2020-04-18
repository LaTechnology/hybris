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
package de.hybris.liveeditaddon.cockpit.navigationeditor.model;

import de.hybris.platform.cockpit.session.UISessionUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Object contains all information about navigation link collection (dragged from navigation state panel as facet)
 * 
 * 
 */
public class NavigationLinkCollectionViewModel
{
	/**
	 * Set of names for new column name
	 */
	private final Map<String, String> columnName = new HashMap<String, String>();

	/**
	 * Navigation links
	 */
	private List<NavigationLinkViewModel> navLinks = new LinkedList<NavigationLinkViewModel>();

	public List<NavigationLinkViewModel> getNavLinks()
	{
		return navLinks;
	}

	/**
	 * Setting name for all languages
	 */
	public void setName(final String name)
	{
		this.getColumnName().put(UISessionUtils.getCurrentSession().getLanguageIso(), name);
	}

	/**
	 * Getting name by current language
	 */
	public String getName()
	{
		return getColumnName().get(UISessionUtils.getCurrentSession().getLanguageIso());
	}

	/**
	 * @param navLinks
	 *           the navLinks to set
	 */
	public void setNavLinks(final List<NavigationLinkViewModel> navLinks)
	{
		this.navLinks = navLinks;
	}

	/**
	 * @return the columnName
	 */
	public Map<String, String> getColumnName()
	{
		return columnName;
	}
}

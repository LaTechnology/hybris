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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;


/**
 * 
 */
public class NavigationColumnViewModel
{
	private List<NavigationLinkViewModel> links = new LinkedList<NavigationLinkViewModel>();
	private Map<String, String> name = new HashMap<String, String>();
	private NavigationNodeViewModel parentNavNode;
	private String navNode;
	private Component component;
	private boolean deleted = false;


	public void setNavigationLinks(final List<NavigationLinkViewModel> links)
	{
		this.links = links;

	}

	public List<NavigationLinkViewModel> getNavigationLinks()
	{
		return links;
	}

	public String getName()
	{
		return name.get(parentNavNode.getLiveEditView().getCurrentPreviewData().getLanguage().getIsocode());
	}

	public Map<String, String> getNames()
	{
		return name;
	}

	public void setName(final String value)
	{
		name.put(parentNavNode.getLiveEditView().getCurrentPreviewData().getLanguage().getIsocode(), value);
	}

	public void setNames(final Map<String, String> value)
	{
		name = value;
	}

	public NavigationNodeViewModel getParentNavigationNode()
	{
		return parentNavNode;
	}

	public void setParentNavigationNode(final NavigationNodeViewModel node)
	{
		parentNavNode = node;
	}

	public String getNavNodeUID()
	{
		return navNode;
	}

	public void setNavNodeUID(final String navNode)
	{
		this.navNode = navNode;
	}

	public Component getComponent()
	{
		return component;
	}

	public void setComponent(final Component component)
	{
		this.component = component;
	}

	public boolean isDeleted()
	{
		return deleted;
	}

	public void delete()
	{
		deleted = true;
	}
}

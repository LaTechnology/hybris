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

/**
 * Created: Jun 13, 2012
 * 
 * 
 */
public class NavigationBarComponentData extends AbstractReferencedData
{
	private String uid = "";
	private String name = "";
	private LinkComponentData link;
	private NavigationNodeData navigationNode;

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
	 * @return the link
	 */
	public LinkComponentData getLink()
	{
		return link;
	}

	/**
	 * @param link
	 *           the link to set
	 */
	public void setLink(final LinkComponentData link)
	{
		this.link = link;
	}

	/**
	 * @return the navigationNode
	 */
	public NavigationNodeData getNavigationNode()
	{
		return navigationNode;
	}

	/**
	 * @param navigationNode
	 *           the navigationNode to set
	 */
	public void setNavigationNode(final NavigationNodeData navigationNode)
	{
		this.navigationNode = navigationNode;
	}


}

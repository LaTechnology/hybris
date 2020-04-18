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
public class NavigationDataHolder
{
	private Map<String, LinkComponentData> linkComponents = new HashMap<String, LinkComponentData>();
	private Map<String, NavigationNodeData> navigationNodes = new HashMap<String, NavigationNodeData>();
	private Map<String, NavigationBarComponentData> navigationBarComponents = new HashMap<String, NavigationBarComponentData>();
	private ContentSlotData contentSlotData;
	private Collection<String> languages = new ArrayList<String>();
	private String productCatalogId;
	private String productCatalogVersionName;
	private String contentCatalogId;
	private String contentCatalogVersionName;

	/**
	 * @return the linkComponents
	 */
	public Collection<LinkComponentData> getLinkComponents()
	{
		return linkComponents.values();
	}

	/**
	 * @param linkComponents
	 *           the linkComponents to set
	 */
	public void setLinkComponents(final Collection<LinkComponentData> linkComponents)
	{
		this.linkComponents = new HashMap<String, LinkComponentData>(linkComponents.size());
		for (final LinkComponentData link : linkComponents)
		{
			this.linkComponents.put(link.getUid(), link);
		}
	}

	/**
	 * Adds single link component to linkComponents Collection
	 * 
	 * @param linkComponent
	 *           the linkComponent to add
	 */
	public void addLinkComponent(final LinkComponentData linkComponent)
	{
		if (!linkComponents.containsKey(linkComponent.getUid()))
		{
			linkComponents.put(linkComponent.getUid(), linkComponent);
		}
	}

	public LinkComponentData getLinkComponentForUid(final String uid)
	{
		return linkComponents.get(uid);
	}

	/**
	 * @return the navigationNodes
	 */
	public Collection<NavigationNodeData> getNavigationNodes()
	{
		return navigationNodes.values();
	}

	/**
	 * @param navigationNodes
	 *           the navigationNodes to set
	 */
	public void setNavigationNodes(final Collection<NavigationNodeData> navigationNodes)
	{
		this.navigationNodes = new HashMap<String, NavigationNodeData>(navigationNodes.size());
		for (final NavigationNodeData navigationNodeData : navigationNodes)
		{
			this.navigationNodes.put(navigationNodeData.getUid(), navigationNodeData);
		}
	}

	/**
	 * Adds single navigation node to navigationNodes Collection
	 * 
	 * @param navigationNode
	 */
	public void addNavigationNode(final NavigationNodeData navigationNode)
	{
		if (!navigationNodes.containsKey(navigationNode.getUid()))
		{
			navigationNodes.put(navigationNode.getUid(), navigationNode);
		}
	}

	/**
	 * 
	 * @param uid
	 *           uid of NavigationNodeData to search for
	 * @return NavigationNodeData if found or <code>null</code> otherwise
	 */
	public NavigationNodeData getNavigationNodeForUid(final String uid)
	{
		return navigationNodes.get(uid);
	}

	/**
	 * @return the navigationBarComponents
	 */
	public Collection<NavigationBarComponentData> getNavigationBarComponents()
	{
		return navigationBarComponents.values();
	}

	/**
	 * @param navigationBarComponents
	 *           the navigationBarComponent to set
	 */
	public void setNavigationBarComponents(final Set<NavigationBarComponentData> navigationBarComponents)
	{
		this.navigationBarComponents = new HashMap<String, NavigationBarComponentData>(navigationBarComponents.size());
		for (final NavigationBarComponentData naviBar : navigationBarComponents)
		{
			this.navigationBarComponents.put(naviBar.getUid(), naviBar);
		}
	}

	/**
	 * Adds single navigation bar component to navigationBarComponents Collection
	 * 
	 * @param navigationBarComponent
	 */
	public void addNavigationComponent(final NavigationBarComponentData navigationBarComponent)
	{
		if (!navigationBarComponents.containsKey(navigationBarComponent.getUid()))
		{
			navigationBarComponents.put(navigationBarComponent.getUid(), navigationBarComponent);
		}
	}

	public NavigationBarComponentData getNavigationBarComponentForUid(final String uid)
	{
		return navigationBarComponents.get(uid);
	}

	/**
	 * @return the contentSlotData
	 */
	public ContentSlotData getContentSlotData()
	{
		return contentSlotData;
	}

	/**
	 * @param contentSlotData
	 *           the contentSlotData to set
	 */
	public void setContentSlotData(final ContentSlotData contentSlotData)
	{
		this.contentSlotData = contentSlotData;
	}

	/**
	 * @return the languages
	 */
	public Collection<String> getLanguages()
	{
		return languages;
	}

	/**
	 * @param languages
	 *           the languages to set
	 */
	public void setLanguages(final Collection<String> languages)
	{
		this.languages = languages;
	}

	/**
	 * Adds single language to languages Collection
	 * 
	 * @param language
	 */
	public void addLanguage(final String language)
	{
		if (!languages.contains(language))
		{
			languages.add(language);
		}
	}

	/**
	 * @param productCatalogId
	 */
	public void setProductCatalogId(final String productCatalogId)
	{
		this.productCatalogId = productCatalogId;
	}

	/**
	 * @return the productCatalogId
	 */
	public String getProductCatalogId()
	{
		return productCatalogId;
	}

	/**
	 * @param productCatalogVersionName
	 *           the productCatalogVersionName to set
	 */
	public void setProductCatalogVersionName(final String productCatalogVersionName)
	{
		this.productCatalogVersionName = productCatalogVersionName;
	}

	/**
	 * @return the productCatalogVersionName
	 */
	public String getProductCatalogVersionName()
	{
		return productCatalogVersionName;
	}

	/**
	 * @return the contentCatalogId
	 */
	public String getContentCatalogId()
	{
		return contentCatalogId;
	}

	/**
	 * @param contentCatalogId
	 *           the contentCatalogId to set
	 */
	public void setContentCatalogId(final String contentCatalogId)
	{
		this.contentCatalogId = contentCatalogId;
	}

	/**
	 * @return the contentCatalogVersionName
	 */
	public String getContentCatalogVersionName()
	{
		return contentCatalogVersionName;
	}

	/**
	 * @param contentCatalogVersionName
	 *           the contentCatalogVersionName to set
	 */
	public void setContentCatalogVersionName(final String contentCatalogVersionName)
	{
		this.contentCatalogVersionName = contentCatalogVersionName;
	}
}

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

import de.hybris.liveeditaddon.cockpit.navigationbargenerator.utils.NavigationBarUtils;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Created: Jun 13, 2012
 * 
 * 
 */
public class ContentSlotData
{
	private String uid = "";
	private Collection<NavigationBarComponentData> components = new ArrayList<NavigationBarComponentData>();

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
	 * @return the components
	 */
	public Collection<NavigationBarComponentData> getComponents()
	{
		return components;
	}

	/**
	 * @param component
	 *           a component to add to components
	 */
	public void addComponent(final NavigationBarComponentData component)
	{
		if (!components.contains(component))
		{
			components.add(component);
		}
	}

	/**
	 * @param components
	 *           the components to set
	 */
	public void setComponents(final Collection<NavigationBarComponentData> components)
	{
		this.components = components;
	}

	/**
	 * 
	 * @return Collection of components' referernces
	 */
	public Collection<String> getComponentsReferences()
	{
		return NavigationBarUtils.getReferences(components);
	}
}

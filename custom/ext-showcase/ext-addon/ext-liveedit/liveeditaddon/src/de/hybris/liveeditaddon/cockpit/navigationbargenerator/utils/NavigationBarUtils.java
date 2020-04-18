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
package de.hybris.liveeditaddon.cockpit.navigationbargenerator.utils;

import de.hybris.liveeditaddon.cockpit.navigationbargenerator.data.AbstractReferencedData;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;


/**
 * Created: Jun 13, 2012
 * 
 * 
 */
public class NavigationBarUtils
{
	public static Collection<String> getReferences(final Collection<? extends AbstractReferencedData> components)
	{
		if (components == null)
		{
			return Collections.emptyList();
		}
		final Collection<String> references = new HashSet<String>(components.size());
		for (final AbstractReferencedData component : components)
		{
			references.add(component.getRef());
		}
		return references;
	}
}

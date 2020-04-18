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
 */
public abstract class AbstractReferencedData
{
	protected String ref;

	/**
	 * @param ref
	 *           reference to set
	 */
	public void setRef(final String ref)
	{
		this.ref = ref;
	}

	/**
	 * @return component's reference
	 */
	public String getRef()
	{
		return ref;
	}
}

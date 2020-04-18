/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.hybris.productcomparison.data;

import java.util.HashMap;
import java.util.Map;


/**
 *
 */
public class WrapperMapVariantAttributes
{
	/** VariantOptionQualifierData name */
	private String name;

	/** VariantOptionQualifierData qualifier */
	private String qualifier;

	/** Map with VariantOptionQualifierData values for compare products.<br/>
	 * Key = product code, value = VariantOptionQualifierData.value */
	private final Map<String, String> productAttrValueMap = new HashMap<String, String>();

	/** @return the name */
	public String getName()
	{
		return name;
	}

	/** @param name
	 *           the name to set */
	public void setName(final String name)
	{
		this.name = name;
	}

	/** @return the qualifier */
	public String getQualifier()
	{
		return qualifier;
	}

	/** @param qualifier
	 *           the qualifier to set */
	public void setQualifier(final String qualifier)
	{
		this.qualifier = qualifier;
	}

	/** @return the productAttrValueMap */
	public Map<String, String> getProductAttrValueMap()
	{
		return productAttrValueMap;
	}

}

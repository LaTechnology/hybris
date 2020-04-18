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
package de.hybris.liveeditaddon.cms.media;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;


public class CmsComponentMediaTypeMappingRegistry implements CmsComponentMediaTypeMapping
{

	private Map<String, String> mappings;

	@Required
	public void setMappings(final Map<String, String> mappings)
	{
		this.mappings = mappings;
	}

	@Override
	public Map<String, String> getMappings()
	{
		return mappings;
	}

}

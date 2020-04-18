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
package com.hybris.addon.cockpits.url.impl;

import de.hybris.platform.acceleratorservices.urldecoder.impl.BaseFrontendRegexUrlDecoder;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;



/**
 * Url decoder run inside the business tools
 * 
 * @author rmcotton
 * 
 */
public class CategoryCatalogVersionFrontendUrlDecoder<CategoryModel> extends BaseFrontendRegexUrlDecoder
{

	private FlexibleSearchService flexibleSearchService;
	private CatalogVersionService catalogVersionService;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.addon.common.url.impl.BaseFrontendRegexUrlDecoder#translateId(java.lang.String)
	 */
	@Override
	protected CategoryModel translateId(final String id)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("catalogVersions", getCatalogVersionService().getSessionCatalogVersions());
		final SearchResult<CategoryModel> result = getFlexibleSearchService().search(
				"select {PK} from {Category} where {code} = ?id and {catalogVersion} in (?catalogVersions)", params);

		if (result.getCount() > 0)
		{
			return result.getResult().get(0);

		}
		return null;
	}

	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	public CatalogVersionService getCatalogVersionService()
	{
		return this.catalogVersionService;
	}

}

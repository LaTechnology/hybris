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
package de.hybris.liveeditaddon.service.dao.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.liveeditaddon.service.dao.CmsObjectDao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;



/**
 * 
 */
public class DefaultCmsObjectDao extends AbstractItemDao implements CmsObjectDao
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hybris.cms.turbocmspages.cockpit.liveedit.service.dao.GenericCmsItemDao#findItemOrRelationByUid(java.lang.
	 * String)
	 */
	@Override
	public <K extends ItemModel> K findItemOrRelationByUidAndCatalogVersion(final String uid,
			final Collection<CatalogVersionModel> cv)
	{
		final String QUERY = "SELECT x.PK FROM ({{select {i.PK} from {CMSItem as i} where {i.uid} = ?uid and {i.catalogVersion} IN (?cvs) }} UNION {{select {r.PK} from {CMSRelation as r} where {r.uid} = ?uid and {r.catalogVersion}  IN (?cvs) }}) x";
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", uid);
		params.put("cvs", cv);
		final SearchResult<K> result = getFlexibleSearchService().search(QUERY, params);
		return result.getTotalCount() > 0 ? result.getResult().iterator().next() : null;
	}
}

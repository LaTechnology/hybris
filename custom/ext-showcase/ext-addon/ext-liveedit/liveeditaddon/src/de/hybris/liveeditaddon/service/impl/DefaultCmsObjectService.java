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
package de.hybris.liveeditaddon.service.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.liveeditaddon.service.CmsObjectService;
import de.hybris.liveeditaddon.service.dao.CmsObjectDao;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Required;


/**
 * 
 */
public class DefaultCmsObjectService implements CmsObjectService
{

	private CmsObjectDao cmsObjectDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.liveeditaddon.cockpit.service.CmsObjectService#getItemOrRelation(java.lang.String,
	 * java.util.Collection)
	 */
	@Override
	public <K extends ItemModel> K getItemOrRelation(final String uid, final Collection<CatalogVersionModel> catalogVersions)
			throws CMSItemNotFoundException
	{
		final K item = getCmsObjectDao().findItemOrRelationByUidAndCatalogVersion(uid, catalogVersions);
		if (item == null)
		{
			throw new CMSItemNotFoundException("Not CMSItem of CMSRelation found for uid [" + uid + "] and catalogVersions ["
					+ catalogVersions + "]");
		}
		return item;
	}

	public CmsObjectDao getCmsObjectDao()
	{
		return cmsObjectDao;
	}

	@Required
	public void setCmsObjectDao(final CmsObjectDao cmsObjectDao)
	{
		this.cmsObjectDao = cmsObjectDao;
	}

}

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
package de.hybris.liveeditaddon.service.dao;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;

import java.util.Collection;


/**
 * 
 */
public interface CmsObjectDao
{
	<K extends ItemModel> K findItemOrRelationByUidAndCatalogVersion(final String uid, final Collection<CatalogVersionModel> cvs);

}

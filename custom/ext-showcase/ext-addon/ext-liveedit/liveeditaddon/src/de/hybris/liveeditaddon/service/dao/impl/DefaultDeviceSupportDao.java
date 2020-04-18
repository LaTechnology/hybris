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

import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.liveeditaddon.model.DeviceSupportModel;
import de.hybris.liveeditaddon.service.dao.DeviceSupportDao;

import java.util.List;


/**
 */
public class DefaultDeviceSupportDao extends DefaultGenericDao<DeviceSupportModel> implements DeviceSupportDao
{

	private static final String QUERY_FIND_BY_UIEXPERIENCE = "select {ds." + DeviceSupportModel.PK + "} from {"
			+ DeviceSupportModel._TYPECODE + " as ds} where {ds." + DeviceSupportModel.SUPPORTEDUIEXPERIENCE
			+ "} = ?uiExperienceLevel";

	public DefaultDeviceSupportDao()
	{
		super(DeviceSupportModel._TYPECODE);
	}

	@Override
	public List<DeviceSupportModel> findByUiExperience(final UiExperienceLevel uiExperienceLevel)
	{
		final FlexibleSearchQuery fsq = new FlexibleSearchQuery(QUERY_FIND_BY_UIEXPERIENCE);
		fsq.addQueryParameter("uiExperienceLevel", uiExperienceLevel);
		final SearchResult<DeviceSupportModel> result = getFlexibleSearchService().search(fsq);
		return result.getResult();
	}
}

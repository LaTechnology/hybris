package com.greenlee.core.greenleeb2bcustomer.dao.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.greenlee.core.greenleeb2bcustomer.dao.GreenleeB2BCustomerDao;
import com.greenlee.core.model.GreenleeB2BCustomerModel;


/**
 * @author xiaochen bian
 *
 */
public class DefaultGreenleeB2BCustomerDao extends AbstractItemDao implements GreenleeB2BCustomerDao
{
	protected static final String QUARY = "SELECT {" + GreenleeB2BCustomerModel.PK + "} " + "FROM {"
			+ GreenleeB2BCustomerModel._TYPECODE + " AS gbc }" + "WHERE {gbc:" + GreenleeB2BCustomerModel.UID
			+ "}=?greenleeB2BCustomerUid";


	@Override
	public List<GreenleeB2BCustomerModel> findGreenleeB2BCustomerById(final String id)
	{
		validateParameterNotNull(id, "GreenleeB2BCustomerModel code must not be null!");

		final Map<String, Object> params = new HashMap<>();

		params.put("greenleeB2BCustomerUid", id);
		final FlexibleSearchQuery fsq = new FlexibleSearchQuery(QUARY, params);
		final SearchResult<GreenleeB2BCustomerModel> result = getFlexibleSearchService().search(fsq);

		return result.getResult();
	}

}

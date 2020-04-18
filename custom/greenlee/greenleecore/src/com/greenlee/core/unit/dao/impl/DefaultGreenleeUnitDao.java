/**
 *
 */
package com.greenlee.core.unit.dao.impl;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;

import com.greenlee.core.unit.dao.GreenleeUnitDao;


/**
 * Default implementation for <code>GreenleeUnitDao</code>
 *
 * @author raja.santhanam
 *
 */
public class DefaultGreenleeUnitDao implements GreenleeUnitDao
{

	private FlexibleSearchService flexibleSearchService;


	/*
	 * (non-Javadoc)
	 *
	 * @see com.greenlee.core.unit.dao.GreenleeUnitDao#getAllB2BUnits()
	 */
	@Override
	public List<B2BUnitModel> getAllB2BUnits()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("select {un.pk} from {B2BUnit as un} order by {un.name} asc");
		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		final SearchResult<B2BUnitModel> result = getFlexibleSearchService().search(query);
		return result.getResult();
	}


	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}


	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}
}

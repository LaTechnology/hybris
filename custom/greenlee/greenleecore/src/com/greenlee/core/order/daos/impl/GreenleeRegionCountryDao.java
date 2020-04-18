/**
 *
 */
package com.greenlee.core.order.daos.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.List;

import com.greenlee.core.order.daos.RegionCountryDao;


/**
 * @author raja.santhanam
 *
 */
public class GreenleeRegionCountryDao implements RegionCountryDao
{

	private FlexibleSearchService flexibleSearchService;

	private BaseStoreService baseStoreService;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.greenlee.core.order.daos.RegionCountryDao#hasRegion(java.lang.String)
	 */
	@Override
	public boolean hasRegion(final String countryIso)
	{
		final StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("select distinct({r.name}) from {Country as c join Region as r on {c.pk}={r.country} "
				+ "join BaseStore2CountryRel as brel on {c.pk}={brel.source} join BaseStore as b on {b.pk}={brel.target}} "
				+ "where {c.isocode}=?isocode and {b.pk} = ?baseStore");

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(queryBuilder.toString());
		fQuery.addQueryParameter("isocode", countryIso);
		fQuery.addQueryParameter("baseStore", baseStoreService.getCurrentBaseStore());
		final List<Class> resultClasses = new ArrayList<Class>();
		resultClasses.add(String.class);
		fQuery.setResultClassList(resultClasses);
		return flexibleSearchService.search(fQuery).getResult().size() > 0;
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

	/**
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

}

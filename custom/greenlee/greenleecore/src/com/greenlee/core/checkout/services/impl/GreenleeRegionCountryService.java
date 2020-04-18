/**
 *
 */
package com.greenlee.core.checkout.services.impl;

import com.greenlee.core.checkout.services.RegionCountryService;
import com.greenlee.core.order.daos.RegionCountryDao;


/**
 * @author raja.santhanam
 *
 */
public class GreenleeRegionCountryService implements RegionCountryService
{

	private RegionCountryDao regionCountryDao;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.greenlee.core.checkout.services.RegionCountryService#hasRegion(java.lang.String)
	 */
	@Override
	public boolean hasRegion(final String countryIso)
	{
		return regionCountryDao.hasRegion(countryIso);
	}

	/**
	 * @return the regionCountryDao
	 */
	public RegionCountryDao getRegionCountryDao()
	{
		return regionCountryDao;
	}

	/**
	 * @param regionCountryDao
	 *           the regionCountryDao to set
	 */
	public void setRegionCountryDao(final RegionCountryDao regionCountryDao)
	{
		this.regionCountryDao = regionCountryDao;
	}

}

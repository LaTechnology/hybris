package com.greenlee.core.maxmind.services.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * @author xiaochen bian
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class MaxmindReturnJson
{
	Country country;

	/**
	 * @return the country
	 */
	public Country getCountry()
	{
		return country;
	}

	/**
	 * @param country
	 *           the country to set
	 */
	public void setCountry(final Country country)
	{
		this.country = country;
	}

	@Override
	public String toString()
	{
		return "MaxmindReturnJsonValues{" + "country='" + country + '}';
	}

}

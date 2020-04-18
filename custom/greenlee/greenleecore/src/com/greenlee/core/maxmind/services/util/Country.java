package com.greenlee.core.maxmind.services.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * @author xiaochen bian
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country
{
	// This property name should be "iso_code" to get the maxmind response, so please don't change this property name to any other naming conversion.
	String iso_code;

	/**
	 * @return the iso_code
	 */
	public String getIso_code()
	{
		return iso_code;
	}

	/**
	 * @param iso_code
	 *           the iso_code to set
	 */
	public void setIso_code(final String iso_code)
	{
		this.iso_code = iso_code;
	}


	@Override
	public String toString()
	{
		return "MaxmindReturnJsonValues{" + "country='" + iso_code + '}';
	}
}

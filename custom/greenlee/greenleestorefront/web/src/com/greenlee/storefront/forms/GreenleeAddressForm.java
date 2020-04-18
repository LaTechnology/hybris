/**
 *
 */
package com.greenlee.storefront.forms;

import de.hybris.platform.acceleratorstorefrontcommons.forms.AddressForm;


/**
 * @author raja.santhanam
 *
 */
public class GreenleeAddressForm extends AddressForm
{
	private String district;
	private String regionIso;
	private String titleCode;
	private String primaryCountryIso;

	/**
	 * @return the primaryCountryIso
	 */
	public String getPrimaryCountryIso()
	{
		return primaryCountryIso;
	}

	/**
	 * @param primaryCountryIso
	 *           the primaryCountryIso to set
	 */
	public void setPrimaryCountryIso(final String primaryCountryIso)
	{
		this.primaryCountryIso = primaryCountryIso;
	}

	/**
	 * @return the district
	 */
	public String getDistrict()
	{
		return district;
	}

	/**
	 * @param district
	 *           the district to set
	 */
	public void setDistrict(final String district)
	{
		this.district = district;
	}

	//Region validation will be based on the selected country, and validation is handled in greenlee address validator. so override this method to avoid not null validation
	@Override
	public String getRegionIso()
	{
		return regionIso;
	}

	@Override
	public void setRegionIso(final String regionIso)
	{
		this.regionIso = regionIso;
	}

	/**
	 * @return the titleCode
	 */
	@Override
	public String getTitleCode()
	{
		return titleCode;
	}

	/**
	 * @param titleCode
	 *           the titleCode to set
	 */
	@Override
	public void setTitleCode(final String titleCode)
	{
		this.titleCode = titleCode;
	}

}

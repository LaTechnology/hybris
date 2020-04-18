/**
 *
 */
package com.greenlee.storefront.forms;

/**
 * @author aruna
 *
 */
public class B2CUserBillingAddressForm
{
	private String addressLane1;
	private String addressLane2;
	private String city;
	private String state;
	private Integer zipCode;
	private String country;

	/**
	 * @return the addressLane1
	 */
	public String getAddressLane1()
	{
		return addressLane1;
	}

	/**
	 * @param addressLane1
	 *           the addressLane1 to set
	 */
	public void setAddressLane1(final String addressLane1)
	{
		this.addressLane1 = addressLane1;
	}

	/**
	 * @return the addressLane2
	 */
	public String getAddressLane2()
	{
		return addressLane2;
	}

	/**
	 * @param addressLane2
	 *           the addressLane2 to set
	 */
	public void setAddressLane2(final String addressLane2)
	{
		this.addressLane2 = addressLane2;
	}

	/**
	 * @return the city
	 */
	public String getCity()
	{
		return city;
	}

	/**
	 * @param city
	 *           the city to set
	 */
	public void setCity(final String city)
	{
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState()
	{
		return state;
	}

	/**
	 * @param state
	 *           the state to set
	 */
	public void setState(final String state)
	{
		this.state = state;
	}

	/**
	 * @return the zipCode
	 */
	public Integer getZipCode()
	{
		return zipCode;
	}

	/**
	 * @param zipCode
	 *           the zipCode to set
	 */
	public void setZipCode(final Integer zipCode)
	{
		this.zipCode = zipCode;
	}

	/**
	 * @return the country
	 */
	public String getCountry()
	{
		return country;
	}

	/**
	 * @param country
	 *           the country to set
	 */
	public void setCountry(final String country)
	{
		this.country = country;
	}

}

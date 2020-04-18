/**
 *
 */
package com.greenlee.storefront.forms;

/**
 * @author aruna
 *
 */
public class B2CUserForm
{

	private String userType;
	private String password;
	private B2CUserBillingAddressForm billingAddress;

	/**
	 * @return the userType
	 */
	public String getUserType()
	{
		return userType;
	}

	/**
	 * @param userType
	 *           the userType to set
	 */
	public void setUserType(final String userType)
	{
		this.userType = userType;
	}

	/**
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @param password
	 *           the password to set
	 */
	public void setPassword(final String password)
	{
		this.password = password;
	}

	/**
	 * @return the billingAddress
	 */
	public B2CUserBillingAddressForm getBillingAddress()
	{
		return billingAddress;
	}

	/**
	 * @param billingAddress
	 *           the billingAddress to set
	 */
	public void setBillingAddress(final B2CUserBillingAddressForm billingAddress)
	{
		this.billingAddress = billingAddress;
	}

}

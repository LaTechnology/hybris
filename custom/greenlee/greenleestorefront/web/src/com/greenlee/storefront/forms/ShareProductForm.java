/**
 *
 */
package com.greenlee.storefront.forms;

/**
 * @author qili
 */
public class ShareProductForm
{
	private String toAddressMessage;
	private String fromAddressMessage;
	private String successMessage;

	/**
	 * @return the toAddressMessage
	 */
	public String getToAddressMessage()
	{
		return toAddressMessage;
	}

	/**
	 * @param toAddressMessage
	 *           the toAddressMessage to set
	 */
	public void setToAddressMessage(final String toAddressMessage)
	{
		this.toAddressMessage = toAddressMessage;
	}

	/**
	 * @return the fromAddressMessage
	 */
	public String getFromAddressMessage()
	{
		return fromAddressMessage;
	}

	/**
	 * @param fromAddressMessage
	 *           the fromAddressMessage to set
	 */
	public void setFromAddressMessage(final String fromAddressMessage)
	{
		this.fromAddressMessage = fromAddressMessage;
	}

	/**
	 * @return the successMessage
	 */
	public String getSuccessMessage()
	{
		return successMessage;
	}

	/**
	 * @param successMessage
	 *           the successMessage to set
	 */
	public void setSuccessMessage(final String successMessage)
	{
		this.successMessage = successMessage;
	}


}

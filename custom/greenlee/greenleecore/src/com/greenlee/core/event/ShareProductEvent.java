/**
 *
 */
package com.greenlee.core.event;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;

import com.greenlee.core.model.GreenleeProductModel;


/**
 * Share product event, implementation of {@link AbstractCommerceUserEvent}
 */
public class ShareProductEvent extends AbstractCommerceUserEvent<BaseSiteModel>
{
	private GreenleeProductModel greenleeProductModel;
	private String toAddress;
	private String fromAddress;
	private String message;

	/**
	 *
	 */
	public ShareProductEvent()
	{
		super();
	}



	/**
	 * @param greenleeProductModel
	 * @param toAddress
	 * @param fromAddress
	 * @param message
	 */
	public ShareProductEvent(final GreenleeProductModel greenleeProductModel, final String toAddress, final String fromAddress,
			final String message)
	{
		super();
		this.greenleeProductModel = greenleeProductModel;
		this.toAddress = toAddress;
		this.fromAddress = fromAddress;
		this.message = message;
	}



	/**
	 * @return the greenleeProductModel
	 */
	public GreenleeProductModel getGreenleeProductModel()
	{
		return greenleeProductModel;
	}



	/**
	 * @param greenleeProductModel
	 *           the greenleeProductModel to set
	 */
	public void setGreenleeProductModel(final GreenleeProductModel greenleeProductModel)
	{
		this.greenleeProductModel = greenleeProductModel;
	}



	/**
	 * @return the toAddress
	 */
	public String getToAddress()
	{
		return toAddress;
	}

	/**
	 * @param toAddress
	 *           the toAddress to set
	 */
	public void setToAddress(final String toAddress)
	{
		this.toAddress = toAddress;
	}

	/**
	 * @return the fromAddress
	 */
	public String getFromAddress()
	{
		return fromAddress;
	}

	/**
	 * @param fromAddress
	 *           the fromAddress to set
	 */
	public void setFromAddress(final String fromAddress)
	{
		this.fromAddress = fromAddress;
	}

	/**
	 * @return the message
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * @param message
	 *           the message to set
	 */
	public void setMessage(final String message)
	{
		this.message = message;
	}


}
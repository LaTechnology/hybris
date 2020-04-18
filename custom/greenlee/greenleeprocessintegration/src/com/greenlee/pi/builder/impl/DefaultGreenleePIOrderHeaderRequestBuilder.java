/**
 *
 */
package com.greenlee.pi.builder.impl;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import com.greenlee.pi.builder.GreenleePIOrderHeaderRequestBuilder;
import com.greenlee.pi.converter.DTHybrisToPIOrderHeaderRequestConverter;
import com.hybris.urn.xi.order_history_header.DTHybrisToPIOrderHeaderRequest;


/**
 * @author peter.asirvatham
 *
 */
public class DefaultGreenleePIOrderHeaderRequestBuilder implements GreenleePIOrderHeaderRequestBuilder
{

	private DTHybrisToPIOrderHeaderRequestConverter orderHeaderRequestConverter;

	/**
	 * @return the orderHeaderRequestConverter
	 */
	public DTHybrisToPIOrderHeaderRequestConverter getOrderHeaderRequestConverter()
	{
		return orderHeaderRequestConverter;
	}


	/**
	 * @param orderHeaderRequestConverter
	 *           the orderHeaderRequestConverter to set
	 */
	public void setOrderHeaderRequestConverter(final DTHybrisToPIOrderHeaderRequestConverter orderHeaderRequestConverter)
	{
		this.orderHeaderRequestConverter = orderHeaderRequestConverter;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.greenlee.pi.builder.GreenleePIOrderHeaderRequestBuilder#getPIOrderHeaderRequest(de.hybris.platform.b2b.model.
	 * B2BCustomerModel)
	 */
	@Override
	public DTHybrisToPIOrderHeaderRequest getPIOrderHeaderRequest(final CustomerData customerData)
	{
		return orderHeaderRequestConverter.convert(customerData);
	}


}

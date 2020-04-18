/**
 *
 */
package com.greenlee.pi.builder.impl;

import com.greenlee.pi.builder.GreenleePIOrderItemRequestBuilder;
import com.greenlee.pi.converter.DTHybrisToPIOrderItemRequestConverter;
import com.hybris.urn.xi.order_history_item.DTHybrisToPIOrderItemRequest;


/**
 * @author peter.asirvatham
 *
 */
public class DefaultGreenleePIOrderItemRequestBuilder implements GreenleePIOrderItemRequestBuilder
{

	private DTHybrisToPIOrderItemRequestConverter orderItemRequestConverter;


	/**
	 * @return the orderItemRequestConverter
	 */
	public DTHybrisToPIOrderItemRequestConverter getOrderItemRequestConverter()
	{
		return orderItemRequestConverter;
	}


	/**
	 * @param orderItemRequestConverter
	 *           the orderItemRequestConverter to set
	 */
	public void setOrderItemRequestConverter(final DTHybrisToPIOrderItemRequestConverter orderItemRequestConverter)
	{
		this.orderItemRequestConverter = orderItemRequestConverter;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.greenlee.pi.builder.GreenleePIOrderItemRequestBuilder#getPIOrderDetailsRequest(java.lang.String)
	 */
	@Override
	public DTHybrisToPIOrderItemRequest getPIOrderDetailsRequest(final String orderId)
	{
		return orderItemRequestConverter.convert(orderId);
	}

}

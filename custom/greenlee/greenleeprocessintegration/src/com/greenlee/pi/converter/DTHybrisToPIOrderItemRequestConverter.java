/**
 *
 */
package com.greenlee.pi.converter;

import de.hybris.platform.converters.impl.AbstractConverter;

import com.hybris.urn.xi.order_history_item.DTHybrisToPIOrderItemRequest;


/**
 * @author peter.asirvatham
 *
 */
public class DTHybrisToPIOrderItemRequestConverter extends AbstractConverter<String, DTHybrisToPIOrderItemRequest>
{

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.converters.impl.AbstractConverter#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final String orderId, final DTHybrisToPIOrderItemRequest request)
	{
		// YTODO Auto-generated method stub
		request.setSalesOrderNo(orderId);
	}

}

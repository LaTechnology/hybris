/**
 *
 */
package com.greenlee.pi.converter;

import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.converters.impl.AbstractConverter;

import com.hybris.urn.xi.order_history_header.DTHybrisToPIOrderHeaderRequest;
import com.hybris.urn.xi.order_history_header.DTHybrisToPIOrderHeaderRequest.OrderHistoryHederInput;


/**
 * @author peter.asirvatham
 *
 */
public class DTHybrisToPIOrderHeaderRequestConverter extends AbstractConverter<CustomerData, DTHybrisToPIOrderHeaderRequest>
{
	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.converters.impl.AbstractConverter#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final CustomerData customerData, final DTHybrisToPIOrderHeaderRequest request)
	{
		final OrderHistoryHederInput orderHistory = new OrderHistoryHederInput();
		orderHistory.setFromDate(customerData.getSapFromDate());
		orderHistory.setToDate(customerData.getSapToDate());
		orderHistory.setCustomerNo(customerData.getSapConsumerID());
		orderHistory.setSalesOrganization(customerData.getSalesOrganization());
		orderHistory.setDistributionChannel(customerData.getDistributionChannel());
		orderHistory.setDivision(customerData.getDivision());
		request.setOrderHistoryHederInput(orderHistory);
	}

}

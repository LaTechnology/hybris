/**
 * 
 */
package com.greenlee.sapintegration.orderexchange.datahub.inbound;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.ordercancel.OrderCancelException;


/**
 * @author nalini.ramarao
 * 
 */
public interface GreenleeDataHubInboundOrderEntryHelper
{
	void cancelOrderEntry( final String orderCode, final String orderCancellationInformation) throws ImpExException, OrderCancelException;
}

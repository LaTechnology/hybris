/**
 * 
 */
package com.greenlee.sapintegration.orderexchange.cancellation;

import java.util.List;

import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.ordercancel.OrderCancelEntry;
import de.hybris.platform.ordercancel.OrderCancelException;


/**
 * @author nalini.ramarao
 * 
 */
public interface GreenleeSapOrderEntryCancelService
{
	public void cancelOrderEntry(List<OrderCancelEntry> cancelEntries, final String erpRejectionReason)
			throws OrderCancelException;
}

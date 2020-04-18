/**
 * 
 */
package com.greenlee.sapintegration.orderexchange.datahub.inbound.impl;

import java.util.ArrayList;
import java.util.List;

import com.greenlee.sapintegration.orderexchange.cancellation.GreenleeSapOrderEntryCancelService;
import com.greenlee.sapintegration.orderexchange.datahub.inbound.GreenleeDataHubInboundOrderEntryHelper;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.ordercancel.OrderCancelEntry;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.sap.orderexchange.constants.DataHubInboundConstants;
import de.hybris.platform.sap.orderexchange.datahub.inbound.impl.AbstractDataHubInboundHelper;

/**
 * @author nalini.ramarao
 * 
 */
public class DefaultGreenleeDataHubInboundOrderEntryHelper extends AbstractDataHubInboundHelper
		implements GreenleeDataHubInboundOrderEntryHelper {
	private GreenleeSapOrderEntryCancelService sapOrderEntryCancelService;

	/**
	 * @param sapOrderEntryCancelService
	 *            the sapOrderEntryCancelService to set
	 */
	public void setSapOrderEntryCancelService(final GreenleeSapOrderEntryCancelService sapOrderEntryCancelService) {
		this.sapOrderEntryCancelService = sapOrderEntryCancelService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.greenlee.sapintegration.orderexchange.datahub.inbound.
	 * GreenleeDataHubInboundOrderEntryHelper#cancelOrderEntry
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void cancelOrderEntry(final String orderCode, final String orderCancellationInformation)
			throws ImpExException, OrderCancelException {

		final List<OrderCancelEntry> cancelEntries = getOrderCancelEntries(orderCode, orderCancellationInformation);

		sapOrderEntryCancelService.cancelOrderEntry(cancelEntries, "IDOC");
	}

	private List<OrderCancelEntry> getOrderCancelEntries(final String orderCode,
			java.lang.String orderCancellationInformation) {
		final String[] orderInfos = orderCancellationInformation.split(",");
		if (orderInfos.length > 0) {
			final List<OrderCancelEntry> cancelEntries = new ArrayList<OrderCancelEntry>();

			for (String orderInfo : orderInfos) {
				String[] entryCancelInfo = orderInfo.split(DataHubInboundConstants.POUND_SIGN);
				cancelEntries.add(new OrderCancelEntry(readOrderEntry(orderCode, entryCancelInfo[1]),
						Long.parseLong(entryCancelInfo[2]), entryCancelInfo[0]));

			}
			return cancelEntries;
		}
		return null;
	}

	protected OrderEntryModel readOrderEntry(final String orderCode, final String orderEntryNumber) {
		final OrderModel orderModel = readOrder(orderCode);
		for (final AbstractOrderEntryModel orderEntryModel : orderModel.getEntries()) {
			if (orderEntryModel.getEntryNumber().intValue() == (Integer.parseInt(orderEntryNumber) - 1)) {
				return (OrderEntryModel) orderEntryModel;
			}
		}
		return null;
	}

}

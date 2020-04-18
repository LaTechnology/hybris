/**
 * 
 */
package com.greenlee.sapintegration.orderexchange.inbound.events;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.sap.orderexchange.constants.DataHubInboundConstants;
import de.hybris.platform.sap.orderexchange.inbound.events.DataHubTranslator;

import com.greenlee.sapintegration.orderexchange.constants.GreenleeDataHubInboundConstants;
import com.greenlee.sapintegration.orderexchange.datahub.inbound.GreenleeDataHubInboundOrderEntryHelper;


/**
 * @author nalini.ramarao
 * 
 */
public class GreenleeDataHubOrderItemCancelTranslator extends DataHubTranslator<GreenleeDataHubInboundOrderEntryHelper>
{
	@SuppressWarnings("javadoc")
	public static final String HELPER_BEAN = "sapDataHubInboundOrderEntryHelper";

	@SuppressWarnings("javadoc")
	public GreenleeDataHubOrderItemCancelTranslator()
	{
		super(HELPER_BEAN);
	}

	@Override
	public void performImport(final String orderCancellationInformation, final Item processedItem) throws ImpExException
	{
		final String orderCode = getOrderCode(processedItem);

		try
		{
			getInboundHelper().cancelOrderEntry(orderCode, orderCancellationInformation);
		}
		catch (final OrderCancelException e)
		{
			e.printStackTrace();
		}
	}

	private String getOrderCode(final Item processedItem) throws ImpExException
	{
		String orderCode = null;

		try
		{
			orderCode = processedItem.getAttribute(DataHubInboundConstants.CODE).toString();
		}
		catch (final JaloSecurityException e)
		{
			throw new ImpExException(e);
		}
		return orderCode;
	}

}

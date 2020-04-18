/**
 *
 */
package com.greenlee.pi.converter;

import de.hybris.platform.converters.impl.AbstractConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.greenlee.orderhistory.data.PIHeaderDetails;
import com.hybris.urn.xi.order_history_header.DTSAPToPIOrderHeaderResponse.OrderHistoryHeaderResult.HeaderDetails;


/**
 * @author peter.asirvatham
 *
 */
public class DTHybrisToPIOrderHeaderResponseConverter extends AbstractConverter<HeaderDetails, PIHeaderDetails>
{
	private static final Logger LOG = Logger.getLogger(DTHybrisToPIOrderHeaderResponseConverter.class);
	private static final String DISPLAYPATTERN = "EEEEE, MMMMM dd, YYYY";
	private static final String DISPLAYPATTERN_SHORT = "EEEEE, MMMMM dd";
	private static final String PIPATTERN = "yyyyMMdd";

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.converters.impl.AbstractConverter#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final HeaderDetails source, final PIHeaderDetails target)
	{
		target.setCustomerNumber(source.getCustomerNumber());
		target.setCustomerPurchaseOrderNo(source.getCustomerPurchaseOrderNo());
		target.setDescription(source.getDescription());
		target.setDocumentCurrency(source.getDocumentCurrency());
		target.setName1(source.getName1());
		target.setNetValueInDocumentCurrency(source.getNetValueInDocumentCurrency());
		target.setRecordCreatedDate(orderHeaderDateFormat(source.getRecordCreatedDate(), DISPLAYPATTERN));
		target.setRecordCreatedDateShort(orderHeaderDateFormat(source.getRecordCreatedDate(), DISPLAYPATTERN_SHORT));
		target.setRequestedDeliveryDateOfDocument(source.getRequestedDeliveryDateOfDocument());
		target.setSalesOrderNo(source.getSalesOrderNo());
		target.setStatus(source.getStatus());
	}

	public String orderHeaderDateFormat(final String date, final String displayPattern)
	{
		String dateToDisplay = null;
		if (date != null && !date.isEmpty())
		{

			final SimpleDateFormat format = new SimpleDateFormat(PIPATTERN);
			try
			{
				final Date formattedDate = format.parse(date);
				final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(displayPattern);
				dateToDisplay = simpleDateFormat.format(formattedDate);
			}
			catch (final ParseException e)
			{
				LOG.error("Error processing the web service order data");
			}

		}
		return dateToDisplay;
	}
}
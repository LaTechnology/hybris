/**
 *
 */
package com.greenlee.core.util;

import de.hybris.platform.util.Config;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;


/**
 * Common utility methods that can be used across the application.
 *
 * @author raja.santhanam
 *
 */
public final class CommonUtils
{

	private static final Logger LOG = Logger.getLogger(CommonUtils.class);

	public static final DateFormat US_DATE_FORMAT = new SimpleDateFormat(
			Config.getString("greenlee.site.dateformat", "MM/dd/yyyy"));

	public static final DateFormat SAP_US_DATE_FORMAT = new SimpleDateFormat(
			Config.getString("greenlee.site.sap.dateformat", "yyyyMMdd"));

	private static final String PIPATTERN = "yyyyMMdd";

	private CommonUtils()
	{
		//do not instantiate
	}

	/**
	 * Helper method to provide a toString representation of any object.
	 *
	 * @param obj
	 *           - to be toStringed
	 * @return - THe string representation of the object.
	 */
	public static String toString(final Object obj)
	{
		return ToStringBuilder.reflectionToString(obj, ToStringStyle.MULTI_LINE_STYLE);
	}

	/**
	 * Helper method for loggers. Utilizes <code>StringBuilder</code> and avoids String concatenation.
	 *
	 * @param messages
	 *           - array of string messages to be logged.
	 * @return - Single concatenated String message that can be input to the logger
	 */
	public static String toStringFormatter(final String... messages)
	{
		final StringBuilder builder = new StringBuilder();
		for (final String msg : messages)
		{
			builder.append(msg);
		}

		return builder.toString();
	}

	public static Date getDateFromStr(final String date) throws ParseException
	{
		return US_DATE_FORMAT.parse(date);
	}

	public static String getStringFromDate(final Date date)
	{
		return US_DATE_FORMAT.format(date);
	}

	public static Date getSAPDateFromStr(final String date) throws ParseException
	{
		return SAP_US_DATE_FORMAT.parse(date);
	}

	public static String getStringFromSAPDate(final Date date)
	{
		return SAP_US_DATE_FORMAT.format(date);
	}

	/**
	 * @param dtPrecisPricingEnquiryRequest
	 * @throws JAXBException
	 */
	public static String marshallObjectToString(final Object jaxbObject) throws JAXBException
	{
		final JAXBContext ctx = JAXBContext.newInstance(jaxbObject.getClass());
		final Marshaller jaxbMarshaller = ctx.createMarshaller();
		final StringWriter sw = new StringWriter();
		jaxbMarshaller.marshal(jaxbObject, sw);
		return sw.toString();
	}

	public static String orderDetailDateFormat(final String date, final String displayPattern)
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

/**
 *
 */
package com.greenlee.pi.service.impl;

import de.hybris.platform.commercefacades.user.data.CustomerData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.log4j.Logger;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.greenlee.orderhistory.data.PIHeaderDetails;
import com.greenlee.pi.builder.impl.DefaultGreenleePIOrderHeaderRequestBuilder;
import com.greenlee.pi.converter.DTHybrisToPIOrderHeaderResponseConverter;
import com.greenlee.pi.exception.PIException;
import com.greenlee.pi.service.GreenleePIOrderHeaderService;
import com.hybris.urn.xi.order_history_header.DTHybrisToPIOrderHeaderRequest;
import com.hybris.urn.xi.order_history_header.DTSAPToPIOrderHeaderResponse;
import com.hybris.urn.xi.order_history_header.DTSAPToPIOrderHeaderResponse.OrderHistoryHeaderResult;
import com.hybris.urn.xi.order_history_header.DTSAPToPIOrderHeaderResponse.OrderHistoryHeaderResult.HeaderDetails;
import com.hybris.urn.xi.order_history_header.ObjectFactory;


/**
 * @author peter.asirvatham
 *
 */
public class DefaultGreenleePIOrderHeaderService implements GreenleePIOrderHeaderService
{

	private static final Logger LOG = Logger.getLogger(DefaultGreenleePIOrderHeaderService.class);
	private static final String DISPLAYPATTERN = "EEEEE,MMMMM dd";
	private static final String PIPATTERN = "yyyyMMdd";
	private DefaultGreenleePIOrderHeaderRequestBuilder greenleePIOrderHeaderRequestBuilder;
	private DTHybrisToPIOrderHeaderResponseConverter orderHeaderResponseConverter;
	private WebServiceTemplate webServiceTemplate;
	private String soapAction;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.greenlee.pi.service.GreenleePIOrderHeaderService#getPIOrderHeaderDetails(de.hybris.platform.b2b.model.
	 * B2BCustomerModel)
	 */
	@Override
	public List<PIHeaderDetails> getPIOrderHeaderDetails(final CustomerData customerData) throws PIException
	{
		JAXBElement<DTSAPToPIOrderHeaderResponse> response = null;
		List<PIHeaderDetails> responseList = new ArrayList<>();
		try
		{
			LOG.info("PI Order Header call invoked ");
			final DTHybrisToPIOrderHeaderRequest orderHeaderRequest = getGreenleePIOrderHeaderRequestBuilder()
					.getPIOrderHeaderRequest(customerData);
			LOG.info(" Customer No " + orderHeaderRequest.getOrderHistoryHederInput().getCustomerNo());
			LOG.info(" Sales organization " + orderHeaderRequest.getOrderHistoryHederInput().getSalesOrganization());
			LOG.info(" Distribution  channel " + orderHeaderRequest.getOrderHistoryHederInput().getDistributionChannel());
			LOG.info(" Division " + orderHeaderRequest.getOrderHistoryHederInput().getDivision());
			LOG.info(" From Data " + orderHeaderRequest.getOrderHistoryHederInput().getFromDate());
			LOG.info(" To Data " + orderHeaderRequest.getOrderHistoryHederInput().getToDate());

			response = (JAXBElement<DTSAPToPIOrderHeaderResponse>) getWebServiceTemplate().marshalSendAndReceive(
					new ObjectFactory().createMTHybrisToPIOrderHeaderRequest(orderHeaderRequest), new SoapActionCallback(soapAction));

			if (response != null)
			{
				LOG.info(" Response" + responseList);
				responseList = populateResultData(response.getValue());
			}
			else
			{
				LOG.info(" Response is null" + response);
			}
		}
		catch (final Exception exp)
		{
			LOG.error("Exception occurred in PI Order Header call " + exp);
			throw new PIException(exp.getMessage());
		}
		if (responseList == null || responseList.isEmpty())
		{
			LOG.info(" Result count in Response is empty");
		}
		return responseList;
	}

	private List<PIHeaderDetails> populateResultData(final DTSAPToPIOrderHeaderResponse headerResponse)
	{
		final List<PIHeaderDetails> responseList = new ArrayList<>();

		if (headerResponse != null)
		{
			final OrderHistoryHeaderResult result = headerResponse.getOrderHistoryHeaderResult();
			if (result != null)
			{
				for (final HeaderDetails detail : result.getHeaderDetails())
				{
					final PIHeaderDetails headerDetails = orderHeaderResponseConverter.convert(detail);
					responseList.add(headerDetails);
					//					LOG.info("Order History order Id :" + detail.getSalesOrderNo());
				}
			}
		}
		LOG.info("response count :" + responseList.size());
		return responseList;

	}


	private void testData(final List<PIHeaderDetails> responseList)
	{
		for (int i = 0; i < 25; i++)
		{
			final PIHeaderDetails piHeader = new PIHeaderDetails();
			piHeader.setStatus("Open");
			piHeader.setCustomerNumber("Leo" + i);
			if (i % 3 == 0)
			{
				piHeader.setCustomerPurchaseOrderNo("testtrackno" + i);
				piHeader.setStatus("Partial");
			}
			else
			{
				piHeader.setCustomerPurchaseOrderNo("testtrackno" + i + 20);
			}
			piHeader.setDescription("Standard Order" + i);
			piHeader.setDocumentCurrency("USD");
			piHeader.setName1("ELECTRIC SUPPLY CENTER INC" + i);
			piHeader.setNetValueInDocumentCurrency("13.65");
			if (i % 2 == 0)
			{
				piHeader.setRecordCreatedDate(orderHeaderDateFormat("20160115"));
				piHeader.setStatus("Shipped");
			}
			else
			{
				piHeader.setRecordCreatedDate(orderHeaderDateFormat("20160216"));
			}
			piHeader.setRequestedDeliveryDateOfDocument("01/19/2016");
			piHeader.setSalesOrderNo("0000015492" + i);

			responseList.add(piHeader);
		}

	}

	public String orderHeaderDateFormat(final String date)
	{
		String dateToDisplay = null;
		if (date != null && !date.isEmpty())
		{

			final SimpleDateFormat format = new SimpleDateFormat(PIPATTERN);
			try
			{
				final Date formattedDate = format.parse(date);
				final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DISPLAYPATTERN);
				dateToDisplay = simpleDateFormat.format(formattedDate);
			}
			catch (final ParseException e)
			{
				LOG.error("Error processing the web service order data");
			}

		}
		return dateToDisplay;
	}



	/**
	 * @return the greenleePIOrderHeaderRequestBuilder
	 */
	public DefaultGreenleePIOrderHeaderRequestBuilder getGreenleePIOrderHeaderRequestBuilder()
	{
		return greenleePIOrderHeaderRequestBuilder;
	}


	/**
	 * @param greenleePIOrderHeaderRequestBuilder
	 *           the greenleePIOrderHeaderRequestBuilder to set
	 */
	public void setGreenleePIOrderHeaderRequestBuilder(
			final DefaultGreenleePIOrderHeaderRequestBuilder greenleePIOrderHeaderRequestBuilder)
	{
		this.greenleePIOrderHeaderRequestBuilder = greenleePIOrderHeaderRequestBuilder;
	}


	/**
	 * @return the webServiceTemplate
	 */
	public WebServiceTemplate getWebServiceTemplate()
	{
		return webServiceTemplate;
	}


	/**
	 * @param webServiceTemplate
	 *           the webServiceTemplate to set
	 */
	public void setWebServiceTemplate(final WebServiceTemplate webServiceTemplate)
	{
		this.webServiceTemplate = webServiceTemplate;
	}


	/**
	 * @return the soapAction
	 */
	public String getSoapAction()
	{
		return soapAction;
	}


	/**
	 * @param soapAction
	 *           the soapAction to set
	 */
	public void setSoapAction(final String soapAction)
	{
		this.soapAction = soapAction;
	}


	/**
	 * @return the orderHeaderResponseConverter
	 */
	public DTHybrisToPIOrderHeaderResponseConverter getOrderHeaderResponseConverter()
	{
		return orderHeaderResponseConverter;
	}


	/**
	 * @param orderHeaderResponseConverter
	 *           the orderHeaderResponseConverter to set
	 */
	public void setOrderHeaderResponseConverter(final DTHybrisToPIOrderHeaderResponseConverter orderHeaderResponseConverter)
	{
		this.orderHeaderResponseConverter = orderHeaderResponseConverter;
	}

}

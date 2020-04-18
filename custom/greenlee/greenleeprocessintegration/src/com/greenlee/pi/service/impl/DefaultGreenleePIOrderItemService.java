/**
 *
 */
package com.greenlee.pi.service.impl;

import javax.xml.bind.JAXBElement;

import org.apache.log4j.Logger;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.greenlee.orderhistory.data.PIOrderItem;
import com.greenlee.pi.builder.impl.DefaultGreenleePIOrderItemRequestBuilder;
import com.greenlee.pi.converter.DTHybrisToPIOrderItemResponseConverter;
import com.greenlee.pi.exception.PIException;
import com.greenlee.pi.service.GreenleePIOrderItemService;
import com.hybris.urn.xi.order_history_item.DTHybrisToPIOrderItemRequest;
import com.hybris.urn.xi.order_history_item.DTPIToHybrisOrderItemResponse;
import com.hybris.urn.xi.order_history_item.DTPIToHybrisOrderItemResponse.OrderDetails;
import com.hybris.urn.xi.order_history_item.ObjectFactory;


/**
 * @author peter.asirvatham
 *
 */
public class DefaultGreenleePIOrderItemService implements GreenleePIOrderItemService
{

	private static final Logger LOG = Logger.getLogger(DefaultGreenleePIOrderItemService.class);
	private DefaultGreenleePIOrderItemRequestBuilder greenleePIOrderItemRequestBuilder;
	private DTHybrisToPIOrderItemResponseConverter orderItemResponseConverter;
	private WebServiceTemplate webServiceTemplate;
	private String soapAction;

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
	 * @return the greenleePIOrderItemRequestBuilder
	 */
	public DefaultGreenleePIOrderItemRequestBuilder getGreenleePIOrderItemRequestBuilder()
	{
		return greenleePIOrderItemRequestBuilder;
	}

	/**
	 * @param greenleePIOrderItemRequestBuilder
	 *           the greenleePIOrderItemRequestBuilder to set
	 */
	public void setGreenleePIOrderItemRequestBuilder(
			final DefaultGreenleePIOrderItemRequestBuilder greenleePIOrderItemRequestBuilder)
	{
		this.greenleePIOrderItemRequestBuilder = greenleePIOrderItemRequestBuilder;
	}

	/**
	 * @return the orderItemResponseConverter
	 */
	public DTHybrisToPIOrderItemResponseConverter getOrderItemResponseConverter()
	{
		return orderItemResponseConverter;
	}

	/**
	 * @param orderItemResponseConverter
	 *           the orderItemResponseConverter to set
	 */
	public void setOrderItemResponseConverter(final DTHybrisToPIOrderItemResponseConverter orderItemResponseConverter)
	{
		this.orderItemResponseConverter = orderItemResponseConverter;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.greenlee.pi.service.GreenleePIOrderItemService#getPIOrderDetails(java.lang.String)
	 */
	@Override
	public PIOrderItem getPIOrderDetails(final String orderId) throws PIException
	{
		JAXBElement<DTPIToHybrisOrderItemResponse> response = null;
		PIOrderItem orderItem = null;
		try
		{
			if (orderId != null)
			{
				LOG.error("getPIOrderDetails >> " +orderId);
				final DTHybrisToPIOrderItemRequest orderItemRequest = getGreenleePIOrderItemRequestBuilder()
						.getPIOrderDetailsRequest(orderId);

				response = (JAXBElement<DTPIToHybrisOrderItemResponse>) getWebServiceTemplate().marshalSendAndReceive(
						new ObjectFactory().createMTHybrisToPIOrderItemRequest(orderItemRequest), new SoapActionCallback(soapAction));
				LOG.error("End of getPIOrderDetails Response >> " +response);
			}
			if (response != null)
			{
				orderItem = new PIOrderItem();
				orderItem = populateResultData(response.getValue());
				LOG.error("getPIOrderDetails Response >> " +response);
			}
		}
		catch (final Exception exp)

		{
			LOG.error("Excption occurred in PI Order detail call", exp);
			throw new PIException(exp.getMessage());
		}

		return orderItem;
	}

	public PIOrderItem populateResultData(final DTPIToHybrisOrderItemResponse headerResponse)
	{
		final OrderDetails orderDetails = headerResponse.getOrderDetails();
		return orderItemResponseConverter.convert(orderDetails);
	}
}
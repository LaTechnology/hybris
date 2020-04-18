/**
 *
 */
package com.greenlee.pi.service.impl;

import java.util.regex.Pattern;

import javax.xml.bind.JAXBElement;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.greenlee.pi.builder.impl.DefaultGreenleePIDeliveryRequestBuilder;
import com.greenlee.pi.converter.DTHybrisToPIDeliveryDataResponseConverter;
import com.greenlee.pi.data.PIDeliveryData;
import com.greenlee.pi.service.GreenleePIDeliveryService;
import com.hybris.urn.xi.hybris_delivery_block_remove.DTHybrisToPIDeliveryNoRequest;
import com.hybris.urn.xi.hybris_delivery_block_remove.DTPIToHybrisDeliveryResponse;
import com.hybris.urn.xi.hybris_delivery_block_remove.ObjectFactory;


/**
 * @author dipankan
 * 
 */
public class DefaultGreenleePIDeliveryService implements GreenleePIDeliveryService
{

	private static final Logger LOG = Logger.getLogger(DefaultGreenleePIDeliveryService.class);

	private static final String STATUS_FAILURE = "ERROR";

	private static Pattern failurePattern = Pattern.compile(STATUS_FAILURE);

	private DefaultGreenleePIDeliveryRequestBuilder greenleePIDeliveryRequestBuilder;
	private DTHybrisToPIDeliveryDataResponseConverter deliveryDataResponseConverter;
	private WebServiceTemplate webServiceTemplate;
	private String soapAction;

	/**
	 * 
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


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.greenlee.pi.service.GreenleePIDeliveryService#piRemoveDelivery(com.greenlee.pi.data.PIDeliveryData)
	 */
	@Override
	public PIDeliveryData piRemoveDelivery(final PIDeliveryData piDeliveryData)
	{
		JAXBElement<DTPIToHybrisDeliveryResponse> response = null;
		PIDeliveryData deliveryData = new PIDeliveryData();
		try
		{
			LOG.info("PI delivery block clearance invoked ");
			if (piDeliveryData != null)
			{
				LOG.info("Delivery No :" + piDeliveryData.getDeliveryNo());
				final DTHybrisToPIDeliveryNoRequest request = getGreenleePIDeliveryRequestBuilder().getPIDeliveryRequest(
						piDeliveryData);

				response = (JAXBElement<DTPIToHybrisDeliveryResponse>) getWebServiceTemplate().marshalSendAndReceive(
						new ObjectFactory().createMTHybrisToPIDeliveryNoRequest(request), new SoapActionCallback(soapAction));
			}

			if (response != null)
			{
				deliveryData = deliveryDataResponseConverter.convert(response.getValue());
				LOG.info("Real time web service call was successful ");
				LOG.info("SAP Status :" + deliveryData.getStatus());
				LOG.info("SAP Error message :" + deliveryData.getErrorMessage());


				if (StringUtils.isNotEmpty(deliveryData.getStatus()) && failurePattern.matcher(deliveryData.getStatus()).lookingAt())
				{
					LOG.error("Delivery Block Removal process failed..." + deliveryData.getStatus());
					LOG.error("ERR_NTFY_SUPPORT_0004 - Delivery block release failure for order <" + piDeliveryData.getOrderNumber()
							+ "> and consignment number <" + piDeliveryData.getConsignmentNumber() + ">");
				}
				else
				{
					LOG.info(deliveryData.getStatus());
				}


			}

		}
		catch (final Exception exp)
		{
			LOG.error("Excption occurred during PI delivey block release call {}", exp);
			deliveryData.setStatus(STATUS_FAILURE);
		}
		return deliveryData;
	}

	/**
	 * @return the greenleePIDeliveryRequestBuilder
	 */
	public DefaultGreenleePIDeliveryRequestBuilder getGreenleePIDeliveryRequestBuilder()
	{
		return greenleePIDeliveryRequestBuilder;
	}


	/**
	 * @param greenleePIDeliveryRequestBuilder
	 *           the greenleePIDeliveryRequestBuilder to set
	 */
	@Required
	public void setGreenleePIDeliveryRequestBuilder(final DefaultGreenleePIDeliveryRequestBuilder greenleePIDeliveryRequestBuilder)
	{
		this.greenleePIDeliveryRequestBuilder = greenleePIDeliveryRequestBuilder;
	}


	/**
	 * @return the deliveryDataResponseConverter
	 */
	public DTHybrisToPIDeliveryDataResponseConverter getDeliveryDataResponseConverter()
	{
		return deliveryDataResponseConverter;
	}


	/**
	 * @param deliveryDataResponseConverter
	 *           the deliveryDataResponseConverter to set
	 */
	@Required
	public void setDeliveryDataResponseConverter(final DTHybrisToPIDeliveryDataResponseConverter deliveryDataResponseConverter)
	{
		this.deliveryDataResponseConverter = deliveryDataResponseConverter;
	}



}

/**
 *
 */
package com.greenlee.pi.service.impl;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;

import org.apache.log4j.Logger;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.springframework.xml.transform.StringResult;

import com.greenlee.pi.builder.impl.DefaultGreenleePICustomerRequestBuilder;
import com.greenlee.pi.converter.DTHybrisToPICustomerDataResponseConverter;
import com.greenlee.pi.data.PICustomerData;
import com.greenlee.pi.service.GreenleePICustomerService;
import com.hybris.urn.xi.ecc_real_time_customer_creation.DTHybrisToPICustomerDataRequest;
import com.hybris.urn.xi.ecc_real_time_customer_creation.DTPIToHYBRISCustomerDataResponse;
import com.hybris.urn.xi.ecc_real_time_customer_creation.ObjectFactory;


/**
 * @author peter.asirvatham
 *
 */
public class DefaultGreenleePICustomerService implements GreenleePICustomerService
{

	private static final Logger LOG = Logger.getLogger(DefaultGreenleePICustomerService.class);
	private DefaultGreenleePICustomerRequestBuilder greenleePICustomerRequestBuilder;
	private DTHybrisToPICustomerDataResponseConverter customerDataResponseConverter;
	private WebServiceTemplate webServiceTemplate;
	private String soapAction;

	/**
	 * @return the greenleePICustomerRequestBuilder
	 */
	public DefaultGreenleePICustomerRequestBuilder getGreenleePICustomerRequestBuilder()
	{
		return greenleePICustomerRequestBuilder;
	}


	/**
	 * @param greenleePICustomerRequestBuilder
	 *           the greenleePICustomerRequestBuilder to set
	 */
	public void setGreenleePICustomerRequestBuilder(final DefaultGreenleePICustomerRequestBuilder greenleePICustomerRequestBuilder)
	{
		this.greenleePICustomerRequestBuilder = greenleePICustomerRequestBuilder;
	}


	/**
	 *
	 *
	 * /**
	 *
	 * @return the webServiceTemplate
	 */
	public WebServiceTemplate getWebServiceTemplate()
	{
		return webServiceTemplate;
	}


	/**
	 * @return the customerDataResponseConverter
	 */
	public DTHybrisToPICustomerDataResponseConverter getCustomerDataResponseConverter()
	{
		return customerDataResponseConverter;
	}


	/**
	 * @param customerDataResponseConverter
	 *           the customerDataResponseConverter to set
	 */
	public void setCustomerDataResponseConverter(final DTHybrisToPICustomerDataResponseConverter customerDataResponseConverter)
	{
		this.customerDataResponseConverter = customerDataResponseConverter;
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
	 * @see com.greenlee.pi.service.GreenleePICustomerService#getPICreateCustomer(com.greenlee.pi.data.PICustomerData)
	 */
	@Override
	public PICustomerData piCreateCustomer(final PICustomerData piCustomerData)
	{
		JAXBElement<DTPIToHYBRISCustomerDataResponse> response = null;
		PICustomerData customerData = new PICustomerData();
		try
		{
			LOG.info("PI real time customer creation call invoked ");
			if (piCustomerData != null)
			{
				LOG.info("Base reference :" + piCustomerData.getBaseReference());
				LOG.info("Customer Name Or Company Name :" + piCustomerData.getCustomerName());
				LOG.info("Email id :" + piCustomerData.getEmailId());
				LOG.info("Bill to Customer No.: " + piCustomerData.getBillToCustomerNo());
				LOG.info("SAP Customer No.: " + piCustomerData.getSapCustomerNo());
				if (piCustomerData.getShippingAddress() != null)
				{
					LOG.info("Shipping address title :" + piCustomerData.getShippingAddress().getTitle());
					LOG.info("Shipping postalcode title :" + piCustomerData.getShippingAddress().getPostalCode());
					LOG.info("Shipping town title :" + piCustomerData.getShippingAddress().getTown());
					LOG.info("Shipping country title :" + piCustomerData.getShippingAddress().getCountry());
					LOG.info("Shipping region title :" + piCustomerData.getShippingAddress().getRegion());
					LOG.info("Shipping Livingston status :" + piCustomerData.getShippingAddress().getApartment());
				}
				if (piCustomerData.getBillingAddress() != null)
				{
					LOG.info("Primary address title :" + piCustomerData.getBillingAddress().getTitle());
					LOG.info("Primary postalcode title :" + piCustomerData.getBillingAddress().getPostalCode());
					LOG.info("Primary town title :" + piCustomerData.getBillingAddress().getTown());
					LOG.info("Primary country title :" + piCustomerData.getBillingAddress().getCountry());
					LOG.info("Primary region title :" + piCustomerData.getBillingAddress().getRegion());
				}
			}
			else
			{
				LOG.info("piCustomerData is null ");
			}
			final DTHybrisToPICustomerDataRequest request = getGreenleePICustomerRequestBuilder().getPICustomerRequest(
					piCustomerData);

			response = (JAXBElement<DTPIToHYBRISCustomerDataResponse>) getWebServiceTemplate().marshalSendAndReceive(
					new ObjectFactory().createMTHybrisToPICustomerDataRequest(request), new SoapActionCallback(soapAction));
			marshalRequest(request);
			if (response != null)
			{
				customerData = customerDataResponseConverter.convert(response.getValue());
				LOG.error("Real time web service call was successful ");
				LOG.error("---------------------------------------------------------------");
				LOG.error("SAP PI/SAP ECC Respones");
				LOG.error("---------------------------------------------------------------");
				LOG.error("Base reference :" + customerData.getBaseReference());
				LOG.error("Customer Name Or Company Name :" + customerData.getCustomerName());
				LOG.error("Email id :" + customerData.getEmailId());
				LOG.error("SAP customer number :" + customerData.getSapCustomerNo());
				LOG.error("Bill to Customer No.: " + customerData.getBillToCustomerNo());
				LOG.error("SAP ErrorMessage : " + customerData.getErrorMessage());
				LOG.error("---------------------------------------------------------------");
			}
			else
			{
				LOG.info(" Response is null" + response);
			}
			marshalResponse(response.getValue());
		}
		catch (final Exception exp)
		{
			LOG.error("ERR_NTFY_SUPPORT_00005 - Real time customer account creation failure for customer"
					+ piCustomerData.getEmailId());
			LOG.error("Excption occurred in PI real time customer creation", exp);
		}
		return customerData;
	}
	public void marshalRequest(DTHybrisToPICustomerDataRequest request ) throws JAXBException {
		  StringWriter stringrequset = new StringWriter();
			  JAXBContext jaxbContext = JAXBContext.newInstance(DTHybrisToPICustomerDataRequest.class);
			  Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			  jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.valueOf(true));
			  JAXBElement<DTHybrisToPICustomerDataRequest> root=new ObjectFactory().createMTHybrisToPICustomerDataRequest(request);
			  jaxbMarshaller.marshal(root, stringrequset);
			  String result = stringrequset.toString();  
			  LOG.error("---------------------------------------------------------------");
				LOG.error("Hybris Request for PI");
				LOG.error("---------------------------------------------------------------");
				LOG.error(result);
		}
	public void marshalResponse(DTPIToHYBRISCustomerDataResponse response) throws JAXBException {
		  StringWriter stringWriter = new StringWriter();
		  JAXBContext jaxbContext = JAXBContext.newInstance(DTPIToHYBRISCustomerDataResponse.class);
		  Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		  jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.valueOf(true));
		  JAXBElement<DTPIToHYBRISCustomerDataResponse> root=new ObjectFactory().createMTPIToHybrisCustomerDataResponse(response);
		  jaxbMarshaller.marshal(root, stringWriter);
		  String result = stringWriter.toString();
		  LOG.error("---------------------------------------------------------------");
			LOG.error("SAP PI/SAP ECC Respones");
			LOG.error("---------------------------------------------------------------");
			LOG.error(result);
		}
}

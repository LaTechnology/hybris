/**
 *
 */
package com.greenlee.pi.service.impl;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.greenlee.pi.builder.GreenleePIRequestBuilder;
import com.greenlee.pi.service.GreenleePIClientService;
import com.hybris.urn.xi.pricing_enquiry.DTHybrisPricingRequest;
import com.hybris.urn.xi.pricing_enquiry.DTHybrisPricingRequest.ORDERHEADER;
import com.hybris.urn.xi.pricing_enquiry.DTHybrisPricingResponse;
import com.hybris.urn.xi.pricing_enquiry.ObjectFactory;

import de.hybris.platform.core.model.order.AbstractOrderModel;


/**
 *
 */
public class DefaultGreenleePIClientService implements GreenleePIClientService
{
	private static final Logger LOG = Logger.getLogger(DefaultGreenleePIClientService.class);
	private String soapAction;
	private GreenleePIRequestBuilder greenleePIRequestBuilder;
	private WebServiceTemplate webServiceTemplate;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.greenlee.pi.service.GreenleePIClientService#callPricingEnquiry(de.hybris.platform.core.model.order.
	 * AbstractOrderModel)
	 */
	@Override
	public DTHybrisPricingResponse callPricingEnquiry(final AbstractOrderModel abstractOrderModel, final boolean isSimulate)
	{
		final DTHybrisPricingRequest dtPrecisPricingEnquiryRequest = getGreenleePIRequestBuilder().getPricingEnquiryRequest(
				abstractOrderModel);

		if (!isSimulate)
		{
			dtPrecisPricingEnquiryRequest.setDROPSHIPADDRESS(null);
			final ORDERHEADER header = dtPrecisPricingEnquiryRequest.getORDERHEADER();
			header.setINCOTERMS2(null);
			header.setINCOTERMS1(null);
			header.setSHIPCOND(null);
			header.setROUTE(null);
			dtPrecisPricingEnquiryRequest.setORDERHEADER(header);
		}
		try
		{
			marshalRequest(dtPrecisPricingEnquiryRequest);
		}
		catch (JAXBException e)
		{
			 LOG.error("Hybris Request for Pricing Enquiry PI " +e.getMessage());
		}
		final JAXBElement<DTHybrisPricingResponse> response = (JAXBElement<DTHybrisPricingResponse>) getWebServiceTemplate()
				.marshalSendAndReceive(new ObjectFactory().createMTHybrisPricingRequest(dtPrecisPricingEnquiryRequest),
						new SoapActionCallback(soapAction));
	   try
      {
          if (null != response)
          {
              marshalResponse(response.getValue());
          }
      }
      catch (final JAXBException e)
      {
          LOG.error("Hybris Response for Pricing Enquiry PI " +e.getMessage());
      }
		return response.getValue();
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
	 * @return the greenleePIRequestBuilder
	 */
	public GreenleePIRequestBuilder getGreenleePIRequestBuilder()
	{
		return greenleePIRequestBuilder;
	}

	/**
	 * @param greenleePIRequestBuilder
	 *           the greenleePIRequestBuilder to set
	 */
	public void setGreenleePIRequestBuilder(final GreenleePIRequestBuilder greenleePIRequestBuilder)
	{
		this.greenleePIRequestBuilder = greenleePIRequestBuilder;
	}
	
	public void marshalRequest(DTHybrisPricingRequest request ) throws JAXBException {
		  StringWriter stringrequset = new StringWriter();
		  JAXBContext jaxbContext = JAXBContext.newInstance(request.getClass());
		  Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		  jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.valueOf(true));
		  JAXBElement<DTHybrisPricingRequest> root=new ObjectFactory().createMTHybrisPricingRequest(request);
		  jaxbMarshaller.marshal(root, stringrequset);
		  LOG.error("---------------------------------------------------------------");
		  LOG.error("Hybris Request for Pricing Enquiry PI");
		  LOG.error(stringrequset.toString());
		  LOG.error("---------------------------------------------------------------");
			  
		}
	public void marshalResponse(DTHybrisPricingResponse response) throws JAXBException {
		  StringWriter stringWriter = new StringWriter();
		  JAXBContext jaxbContext = JAXBContext.newInstance(response.getClass());
		  Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		  jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.valueOf(true));
		  JAXBElement<DTHybrisPricingResponse> root=new ObjectFactory().createMTHybrisPricingResponse(response);
		  jaxbMarshaller.marshal(root, stringWriter);
		  LOG.error("---------------------------------------------------------------");
		  LOG.error("PI Response for Pricing Enquiry");
		  LOG.error(stringWriter.toString());
		  LOG.error("---------------------------------------------------------------");
		}
}

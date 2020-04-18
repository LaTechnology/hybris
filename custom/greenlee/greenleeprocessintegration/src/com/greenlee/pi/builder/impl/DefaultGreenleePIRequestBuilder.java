/**
 *
 */
package com.greenlee.pi.builder.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;

import com.greenlee.pi.builder.GreenleePIRequestBuilder;
import com.greenlee.pi.converter.DTHybrisPricingRequestConverter;
import com.hybris.urn.xi.pricing_enquiry.DTHybrisPricingRequest;



/**
 *
 */
public class DefaultGreenleePIRequestBuilder implements GreenleePIRequestBuilder
{
	private DTHybrisPricingRequestConverter converter;


	/**
	 * @return the converter
	 */
	public DTHybrisPricingRequestConverter getConverter()
	{
		return converter;
	}


	/**
	 * @param converter
	 *           the converter to set
	 */
	public void setConverter(final DTHybrisPricingRequestConverter converter)
	{
		this.converter = converter;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.greenlee.pi.builder.GreenleePIRequestBuilder#getPricingEnquiryRequest(de.hybris.platform.core.model.order.
	 * AbstractOrderModel)
	 */
	@Override
	public DTHybrisPricingRequest getPricingEnquiryRequest(final AbstractOrderModel abstractOrderModel)
	{
		return converter.convert(abstractOrderModel);
	}

}

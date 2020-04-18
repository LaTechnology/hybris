/**
 *
 */
package com.greenlee.pi.builder.impl;

import org.springframework.beans.factory.annotation.Required;

import com.greenlee.pi.builder.GreenleePIDeliveryRequestBuilder;
import com.greenlee.pi.converter.DTHybrisToPIDeliveryDataRequestConverter;
import com.greenlee.pi.data.PIDeliveryData;
import com.hybris.urn.xi.hybris_delivery_block_remove.DTHybrisToPIDeliveryNoRequest;


/**
 * @author dipankan
 *
 */
public class DefaultGreenleePIDeliveryRequestBuilder implements GreenleePIDeliveryRequestBuilder
{

	private DTHybrisToPIDeliveryDataRequestConverter deliveryDataRequestConverter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.greenlee.pi.builder.GreenleePICustomerRequestBuilder#getPICustomerRequest(com.greenlee.pi.data.PICustomerData)
	 */
	@Override
	public DTHybrisToPIDeliveryNoRequest getPIDeliveryRequest(final PIDeliveryData deliveryData)
	{
		return deliveryDataRequestConverter.convert(deliveryData);
	}

	/**
	 * @return the deliveryDataRequestConverter
	 */
	public DTHybrisToPIDeliveryDataRequestConverter getDeliveryDataRequestConverter()
	{
		return deliveryDataRequestConverter;
	}


	/**
	 * @param deliveryDataRequestConverter
	 *           the deliveryDataRequestConverter to set
	 */
	@Required
	public void setDeliveryDataRequestConverter(final DTHybrisToPIDeliveryDataRequestConverter deliveryDataRequestConverter)
	{
		this.deliveryDataRequestConverter = deliveryDataRequestConverter;
	}

}

/**
 *
 */
package com.greenlee.pi.builder.impl;

import com.greenlee.pi.builder.GreenleePICustomerRequestBuilder;
import com.greenlee.pi.converter.DTHybrisToPICustomerDataRequestConverter;
import com.greenlee.pi.data.PICustomerData;
import com.hybris.urn.xi.ecc_real_time_customer_creation.DTHybrisToPICustomerDataRequest;


/**
 * @author peter.asirvatham
 *
 */
public class DefaultGreenleePICustomerRequestBuilder implements GreenleePICustomerRequestBuilder
{

	private DTHybrisToPICustomerDataRequestConverter customerDataRequestConverter;



	/**
	 * @return the customerDataRequestConverter
	 */
	public DTHybrisToPICustomerDataRequestConverter getCustomerDataRequestConverter()
	{
		return customerDataRequestConverter;
	}



	/**
	 * @param customerDataRequestConverter
	 *           the customerDataRequestConverter to set
	 */
	public void setCustomerDataRequestConverter(final DTHybrisToPICustomerDataRequestConverter customerDataRequestConverter)
	{
		this.customerDataRequestConverter = customerDataRequestConverter;
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.greenlee.pi.builder.GreenleePICustomerRequestBuilder#getPICustomerRequest(com.greenlee.pi.data.PICustomerData)
	 */
	@Override
	public DTHybrisToPICustomerDataRequest getPICustomerRequest(final PICustomerData customerData)
	{
		return customerDataRequestConverter.convert(customerData);
	}

}

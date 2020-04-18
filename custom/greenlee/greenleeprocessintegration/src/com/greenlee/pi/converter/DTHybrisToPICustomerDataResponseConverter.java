/**
 *
 */
package com.greenlee.pi.converter;

import de.hybris.platform.converters.impl.AbstractConverter;

import com.greenlee.pi.data.PICustomerData;
import com.hybris.urn.xi.ecc_real_time_customer_creation.DTPIToHYBRISCustomerDataResponse;


/**
 * @author peter.asirvatham
 *
 */
public class DTHybrisToPICustomerDataResponseConverter extends AbstractConverter<DTPIToHYBRISCustomerDataResponse, PICustomerData>
{

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.converters.impl.AbstractConverter#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final DTPIToHYBRISCustomerDataResponse source, final PICustomerData target)
	{
		target.setSapCustomerNo(source.getSAPCustomerNo());
		target.setBillToCustomerNo(source.getBillToCustomerNo());
		target.setErrorMessage(source.getErrorMessage());
	}
}

/**
 *
 */
package com.greenlee.pi.converter;

import de.hybris.platform.converters.impl.AbstractConverter;

import com.greenlee.pi.data.PIDeliveryData;
import com.hybris.urn.xi.hybris_delivery_block_remove.DTHybrisToPIDeliveryNoRequest;


/**
 * @author dipankan
 *
 */
public class DTHybrisToPIDeliveryDataRequestConverter extends AbstractConverter<PIDeliveryData, DTHybrisToPIDeliveryNoRequest>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.impl.AbstractConverter#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final PIDeliveryData deliveryData, final DTHybrisToPIDeliveryNoRequest request)
	{
		request.setDeliveryNo(deliveryData.getDeliveryNo());
	}
}

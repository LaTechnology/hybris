/**
 *
 */
package com.greenlee.pi.converter;

import de.hybris.platform.converters.impl.AbstractConverter;

import com.greenlee.pi.data.PIDeliveryData;
import com.hybris.urn.xi.hybris_delivery_block_remove.DTPIToHybrisDeliveryResponse;


/**
 * @author dipankan
 *
 */
public class DTHybrisToPIDeliveryDataResponseConverter extends AbstractConverter<DTPIToHybrisDeliveryResponse, PIDeliveryData>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.impl.AbstractConverter#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final DTPIToHybrisDeliveryResponse source, final PIDeliveryData target)
	{
		target.setStatus(source.getStatus());
	}
}

/**
 *
 */
package com.greenlee.pi.builder;

import com.greenlee.pi.data.PIDeliveryData;
import com.hybris.urn.xi.hybris_delivery_block_remove.DTHybrisToPIDeliveryNoRequest;


/**
 * @author dipankan
 *
 */
public interface GreenleePIDeliveryRequestBuilder
{
	DTHybrisToPIDeliveryNoRequest getPIDeliveryRequest(PIDeliveryData deliveryData);
}

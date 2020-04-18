/**
 *
 */
package com.greenlee.pi.service;

import de.hybris.platform.core.model.order.AbstractOrderModel;

import com.hybris.urn.xi.pricing_enquiry.DTHybrisPricingResponse;


/**
 *
 */
public interface GreenleePIClientService
{
	DTHybrisPricingResponse callPricingEnquiry(AbstractOrderModel abstractOrderModel, boolean isSimulate);

}

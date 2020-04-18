/**
 *
 */
package com.greenlee.pi.builder;

import de.hybris.platform.core.model.order.AbstractOrderModel;

import com.hybris.urn.xi.pricing_enquiry.DTHybrisPricingRequest;


/**
 *
 */
public interface GreenleePIRequestBuilder
{
	DTHybrisPricingRequest getPricingEnquiryRequest(AbstractOrderModel abstractOrderModel);

}

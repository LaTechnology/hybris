/**
 *
 */
package com.greenlee.pi.builder;

import de.hybris.platform.commercefacades.user.data.CustomerData;

import com.hybris.urn.xi.order_history_header.DTHybrisToPIOrderHeaderRequest;


/**
 * @author peter.asirvatham
 *
 */
public interface GreenleePIOrderHeaderRequestBuilder
{
	DTHybrisToPIOrderHeaderRequest getPIOrderHeaderRequest(final CustomerData customerData);
}

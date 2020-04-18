/**
 *
 */
package com.greenlee.pi.builder;

import com.hybris.urn.xi.order_history_item.DTHybrisToPIOrderItemRequest;


/**
 * @author peter.asirvatham
 *
 */
public interface GreenleePIOrderItemRequestBuilder
{
	DTHybrisToPIOrderItemRequest getPIOrderDetailsRequest(final String orderId);
}

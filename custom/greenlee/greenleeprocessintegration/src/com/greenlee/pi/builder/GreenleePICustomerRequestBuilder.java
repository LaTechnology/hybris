/**
 *
 */
package com.greenlee.pi.builder;

import com.greenlee.pi.data.PICustomerData;
import com.hybris.urn.xi.ecc_real_time_customer_creation.DTHybrisToPICustomerDataRequest;


/**
 * @author peter.asirvatham
 *
 */
public interface GreenleePICustomerRequestBuilder
{
	DTHybrisToPICustomerDataRequest getPICustomerRequest(PICustomerData customerData);
}

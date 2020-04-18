package com.paymentech.rest.client;

import com.paymentech.jaxb.response.Response;



/**
 * A REST client to communicate to the Paymentech XML API. Sends XML data in request
 */
public interface PTSecurePaymentClient
{
	/**
	 * @param request
	 * @return Response
	 */

	public Response authPayment(final String xmlRequest);

}

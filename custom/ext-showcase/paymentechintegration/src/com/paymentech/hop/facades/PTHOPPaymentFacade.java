package com.paymentech.hop.facades;

import de.hybris.platform.acceleratorfacades.payment.data.PaymentSubscriptionResultData;
import de.hybris.platform.acceleratorservices.payment.data.PaymentData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.core.model.order.CartModel;

import java.util.Map;

/**
 * @author dipankan
 *
 */
public interface PTHOPPaymentFacade {

	public PaymentData beginHopPayment(CartData cartData,String merchantUrl,String channel);

	public PaymentSubscriptionResultData completeHOPCreateSubscription(
			Map<String, String> reqestMap, Map<String, String> responseMap,
			boolean save);

	public boolean hasPaymentError(Map<String, String> resultMap);

	public PaymentSubscriptionResultData completeHOPCreateSubscription(
			Map<String, String> reqestMap, CartModel cartModel,
			Map<String, String> resultMap, boolean save);

	public String buildHOPRequestURL(String url, Map<String, String> params);

	/**
	 * @return the paymentData
	 */
	public PaymentData getPaymentData();

	/**
	 * @param paymentData
	 *            the paymentData to set
	 */
	public void setPaymentData(PaymentData paymentData);
}

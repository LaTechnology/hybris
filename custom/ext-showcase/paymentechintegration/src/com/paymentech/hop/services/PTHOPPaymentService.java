/**
 *
 */
package com.paymentech.hop.services;

import java.util.Map;

import de.hybris.platform.acceleratorservices.payment.data.PaymentData;
import de.hybris.platform.acceleratorservices.payment.data.PaymentSubscriptionResultItem;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;

/**
 * @author dipankan
 *
 */
public interface PTHOPPaymentService {
	PaymentData beginHopPayment(CurrencyModel currency, Double totalAmount,
			CartData activeCartData, String merchantUrl, String channel);

	PaymentSubscriptionResultItem completeHopCreatePaymentSubscription(
			Map<String, String> reqestMap,
			CartModel cartModel, boolean saveInAccount, Map<String, String> parameters);
}

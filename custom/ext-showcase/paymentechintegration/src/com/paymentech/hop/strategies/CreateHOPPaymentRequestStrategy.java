package com.paymentech.hop.strategies;

import com.paymentech.payment.data.CreateHOPPaymentRequest;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.core.model.c2l.CurrencyModel;

/**
 * A strategy for creating a {@link CreateHOPPaymentRequest}
 *
 */
public interface CreateHOPPaymentRequestStrategy {
	CreateHOPPaymentRequest createHOPPaymentRequest(
			final CurrencyModel currency, final Double totalAmount,
			final CartData activeCart, final String merchantUrl)
			throws IllegalArgumentException;

}

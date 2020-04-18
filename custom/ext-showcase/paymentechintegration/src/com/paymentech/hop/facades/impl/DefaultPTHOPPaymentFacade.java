/**
 *
 */
package com.paymentech.hop.facades.impl;

import de.hybris.platform.acceleratorfacades.payment.data.PaymentSubscriptionResultData;
import de.hybris.platform.acceleratorservices.payment.data.PaymentData;
import de.hybris.platform.acceleratorservices.payment.data.PaymentSubscriptionResultItem;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.paymentech.hop.facades.PTHOPPaymentFacade;
import com.paymentech.hop.services.PTHOPPaymentService;

/**
 * @author dipankan
 *
 */
public class DefaultPTHOPPaymentFacade implements PTHOPPaymentFacade {
	private static final Logger LOG = Logger
			.getLogger(DefaultPTHOPPaymentFacade.class.getName());
	private CartService cartService;
	private PTHOPPaymentService ptHOPPaymentService;
	private UserService userService;
	private Converter<PaymentSubscriptionResultItem, PaymentSubscriptionResultData> paymentSubscriptionResultDataConverter;
	private PaymentData paymentData;
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.paymenttech.hop.facades.PTHOPPaymentFacade#beginHopPayment(java.lang
	 * .String, de.hybris.platform.core.model.order.CartModel, java.lang.String)
	 */
	@Override
	public PaymentData beginHopPayment(final CartData activeCartData, final String merchantUrl,
			final String channel) {

		LOG.info("begin HOP payment");
		final CurrencyModel currency = cartService.getSessionCart().getCurrency();
		final double cartTotal = activeCartData.getTotalPrice().getValue().doubleValue();
		LOG.error("cartTotal  >>> "+cartTotal);
		final Double totalAmount = new Double(cartTotal);
//		final AddressModel deliveryAddress = activeCart.getDeliveryAddress();
//		final AddressModel billingAddress = activeCart.getPaymentAddress();
		LOG.info("calling getDefaultPaymentAddressForCheckout service");
		paymentData= getPtHOPPaymentService().beginHopPayment(currency, totalAmount, activeCartData, merchantUrl,
				channel);
		this.setPaymentData(paymentData);
		return paymentData;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.paymenttech.hop.facades.PTHOPPaymentFacade#hasPaymentError(java.util
	 * .Map)
	 */
	@Override
	public boolean hasPaymentError(final Map<String, String> resultMap) {
		// YTODO Auto-generated method stub
		return false;
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.paymentech.hop.facades.PTHOPPaymentFacade#buildHOPRequestURL(java
	 * .lang.String, java.util.Map)
	 */
	@Override
	public String buildHOPRequestURL(final String url,
			final Map<String, String> params) {
		String postURL = StringUtils.EMPTY;
		try {
			final URIBuilder builder = new URIBuilder(url);
			final List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
			for (final Map.Entry<String, String> entry : params.entrySet()) {
				queryParams.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			}
			builder.setParameters(queryParams);
			final URI uri = builder.build();
			postURL = uri.toString();
			LOG.debug("HOF Request URL::::" + postURL);
		} catch (final URISyntaxException e) {
			LOG.error("URI Syntax is incorrect", e);
		}

		return postURL;
	}

	/**
	 * 
	 * @return
	 */
	private AddressModel getCurrentCustomerBillingAddress() {
		CustomerModel customerModel = (CustomerModel) getUserService()
				.getCurrentUser();
		try {
			for (final AddressModel address : customerModel.getAddresses()) {
				if (Boolean.TRUE.equals(address.getBillingAddress())) {
					return address;
				}
			}
		} catch (NullPointerException e) {
			LOG.error(e);
		}
		return null;
	}
	
	private AddressModel getDefaultPaymentAddressForCheckout() {
		try {
			final CustomerModel customerModel = (CustomerModel) getUserService().getCurrentUser();
			final AddressModel paymentAddress=customerModel.getDefaultPaymentAddress();
			LOG.error("Default Payment Address for the Customer ID [ "+customerModel.getUid() +" ] & The Selected Billing Address as a Payment Address ID is "
					+ "["+paymentAddress.getPk().getLongValue());
			return paymentAddress;
		} catch (Exception e) {
			LOG.error(e);
		}
		return null;
		
	}
	protected CartService getCartService() {
		return cartService;
	}

	@Required
	public void setCartService(final CartService cartService) {
		this.cartService = cartService;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * @param userService
	 *            the userService to set
	 */
	@Required
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * @return the ptHOPPaymentService
	 */
	public PTHOPPaymentService getPtHOPPaymentService() {
		return ptHOPPaymentService;
	}

	/**
	 * @param ptHOPPaymentService
	 *            the ptHOPPaymentService to set
	 */
	@Required
	public void setPtHOPPaymentService(
			final PTHOPPaymentService ptHOPPaymentService) {
		this.ptHOPPaymentService = ptHOPPaymentService;
	}

	public Converter<PaymentSubscriptionResultItem, PaymentSubscriptionResultData> getPaymentSubscriptionResultDataConverter() {
		return paymentSubscriptionResultDataConverter;
	}

	@Required
	public void setPaymentSubscriptionResultDataConverter(
			final Converter<PaymentSubscriptionResultItem, PaymentSubscriptionResultData> paymentSubscriptionResultDataConverter) {
		this.paymentSubscriptionResultDataConverter = paymentSubscriptionResultDataConverter;
	}

	@Override
	public PaymentData getPaymentData() {
		return paymentData;
	}

	@Override
	public void setPaymentData(PaymentData paymentData) {
		this.paymentData = paymentData;
	}

	@Override
	public PaymentSubscriptionResultData completeHOPCreateSubscription(
			Map<String, String> reqestMap, Map<String, String> responseMap,
			boolean save) {
		return completeHOPCreateSubscription(reqestMap, getCartService().getSessionCart(),
				responseMap, true);
	}

	@Override
	public PaymentSubscriptionResultData completeHOPCreateSubscription(
			Map<String, String> reqestMap, CartModel cartModel,
			Map<String, String> resultMap, boolean save) {
		LOG.info("Create HOP subscription");
		final PaymentSubscriptionResultItem paymentSubscriptionResultItem = getPtHOPPaymentService()
				.completeHopCreatePaymentSubscription(reqestMap,cartModel, save,
						resultMap);

		if (paymentSubscriptionResultItem != null) {
			return getPaymentSubscriptionResultDataConverter().convert(
					paymentSubscriptionResultItem);
		}
		return null;	}

}

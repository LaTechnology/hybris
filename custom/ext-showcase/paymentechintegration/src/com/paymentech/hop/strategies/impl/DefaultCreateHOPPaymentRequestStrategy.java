
package com.paymentech.hop.strategies.impl;

import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.storesession.data.CurrencyData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.paymentech.hop.strategies.CreateHOPPaymentRequestStrategy;
import com.paymentech.payment.data.CreateHOPPaymentRequest;


public class DefaultCreateHOPPaymentRequestStrategy implements CreateHOPPaymentRequestStrategy
{

	private static final Logger LOG = Logger.getLogger(DefaultCreateHOPPaymentRequestStrategy.class);

	private CartService cartService;
	private SiteConfigService siteConfigService;
	private Converter<CurrencyModel, CurrencyData> currencyConverter;
	private Converter<AddressModel, AddressData> addressConverter;
	private Converter<CartModel, CartData> cartConverter;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.paymenttech.hop.strategies.CreateHOPPaymentRequestStrategy#createSubscriptionRequest(de.hybris.platform.core
	 * .model.c2l.CurrencyModel, java.lang.Double, de.hybris.platform.core.model.user.AddressModel,
	 * de.hybris.platform.core.model.user.AddressModel, de.hybris.platform.core.model.order.CartData, java.lang.String)
	 */
	@Override
	public CreateHOPPaymentRequest createHOPPaymentRequest(final CurrencyModel currency, final Double totalAmount,
			final CartData activeCartData,
			final String merchantUrl) throws IllegalArgumentException
	{
		final CreateHOPPaymentRequest request = new CreateHOPPaymentRequest();
		request.setRequestId(UUID.randomUUID().toString());
		request.setCurrency(getCurrencyConverter().convert(currency));
		request.setTotalAmount(totalAmount);
		if (activeCartData.getBillingAddress() != null)
		{
			request.setBillingAddress(activeCartData.getBillingAddress());
//			request.setBillingAddress(getAddressConverter().convert(billingAddress));
		}
		if (activeCartData.getDeliveryAddress() != null)
		{
//			request.setDeliveryAddress(getAddressConverter().convert(deliveryAddress));
			request.setDeliveryAddress(activeCartData.getDeliveryAddress());
		}
//		request.setActiveCart(getCartConverter().convert(activeCartData));
		request.setActiveCart(activeCartData);
		return request;
	}


	protected CartService getCartService()
	{
		return cartService;
	}

	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	protected SiteConfigService getSiteConfigService()
	{
		return siteConfigService;
	}

	@Required
	public void setSiteConfigService(final SiteConfigService siteConfigService)
	{
		this.siteConfigService = siteConfigService;
	}

	public Converter<CurrencyModel, CurrencyData> getCurrencyConverter()
	{
		return currencyConverter;
	}

	@Required
	public void setCurrencyConverter(final Converter<CurrencyModel, CurrencyData> currencyConverter)
	{
		this.currencyConverter = currencyConverter;
	}

	protected Converter<AddressModel, AddressData> getAddressConverter()
	{
		return addressConverter;
	}

	@Required
	public void setAddressConverter(final Converter<AddressModel, AddressData> addressConverter)
	{
		this.addressConverter = addressConverter;
	}

	protected Converter<CartModel, CartData> getCartConverter()
	{
		return cartConverter;
	}

	@Required
	public void setCartConverter(final Converter<CartModel, CartData> cartConverter)
	{
		this.cartConverter = cartConverter;
	}

}

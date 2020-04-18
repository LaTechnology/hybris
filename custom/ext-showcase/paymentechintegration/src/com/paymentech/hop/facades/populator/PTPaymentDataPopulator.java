package com.paymentech.hop.facades.populator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.acceleratorservices.payment.cybersource.converters.populators.response.AbstractResultPopulator;

import com.paymentech.payment.data.CreateHOPPaymentRequest;

import de.hybris.platform.acceleratorservices.payment.data.PaymentData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.servicelayer.session.SessionService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


public class PTPaymentDataPopulator extends AbstractResultPopulator<CreateHOPPaymentRequest, PaymentData>
{
	private SiteConfigService siteConfigService;
	private SessionService sessionService;

	@Override
	public void populate(final CreateHOPPaymentRequest source, final PaymentData target)
	{
		//Validate parameters and related data
		validateParameterNotNull(source, "Parameter source (CreateHOPPaymentRequest) cannot be null");
		validateParameterNotNull(target, "Parameter target (PaymentData) cannot be null");
		Assert.isInstanceOf(CreateHOPPaymentRequest.class, source);
		//		Assert.notNull(source.getDeliveryAddress(), "deliveryAddress cannot be null");
		//		Assert.notNull(source.getBillingAddress(), "billingAddress cannot be null");
		Assert.notNull(source.getActiveCart(), "activeCartData cannot be null");

		populateParamData(source, target);

		target.setPostUrl(source.getRequestUrl());
	}

	private void populateParamData(final CreateHOPPaymentRequest source, final PaymentData target)
	{

		final Map<String, String> parameterMap = new HashMap<String, String>();

		final AddressData billingAddress = source.getBillingAddress();
		final AddressData deliveryAddress = source.getDeliveryAddress();

		parameterMap.put("hostedSecureID", getHostedSecureId(source.getCurrency().getIsocode()));
		parameterMap.put("action", getAction());
		//		parameterMap.put("return_url", getAction());
		//		parameterMap.put("content_template_url", getAction());
		//		parameterMap.put("cancel_url", getAction());
		if(getSessionService().getCurrentSession() != null)
		{
			parameterMap.put("sessionId", getSessionService().getCurrentSession().getSessionId());
		}
		parameterMap.put("hosted_tokenize", getHostedTokenize());
		parameterMap.put("trans_type", getTransactionType());
		parameterMap.put("payment_type", getPaymentType());
		parameterMap.put("allowed_types", getAllowedTypes());
		parameterMap.put("collectAddress", "0"); //0 - Represents No address field requried.
		parameterMap.put("required", getRequired());
		parameterMap.put("cardIndicators", getCardIndicators());
		parameterMap.put("account_verification", getAccountVerification());
//		parameterMap.put("amount", String.valueOf(source.getTotalAmount().longValue()));
		parameterMap.put("amount", "0");
		if(source.getActiveCart() != null && source.getActiveCart().getDescription() != null)
		parameterMap.put("order_desc", source.getActiveCart().getDescription());
		StringBuffer stringBuffer=new StringBuffer(source.getActiveCart().getCode()).append("_").
				append(String.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMHHmmssSSS"))));//It should be 22 char length
		parameterMap.put("order_id",stringBuffer.toString()); //GRE-2101 instead of card id, use timestamp of pattern

//		if (billingAddress != null && getFetchBillingAddressOnUI().equals("0"))
		if(billingAddress != null){
			if (billingAddress.getCompanyName() != null)
			{
				parameterMap.put("customer_company", billingAddress.getCompanyName());
			}
			if (billingAddress.getFirstName() != null)
			{
				parameterMap.put("customer_firstname", billingAddress.getFirstName());
			}
			if (billingAddress.getLastName() != null)
			{
				parameterMap.put("customer_lastname", billingAddress.getLastName());
			}
			//GRE-2218
			 if (billingAddress.getLine1() != null && billingAddress.getLine2() != null)
			{
				parameterMap.put("address", billingAddress.getLine2());
			}else{
				parameterMap.put("address", billingAddress.getLine2());
			}
			if (billingAddress.getLine1() != null && billingAddress.getLine2() == null)
			{
				parameterMap.put("address2", billingAddress.getLine1());
			}else{
				parameterMap.put("address2", billingAddress.getLine1());
			}
			//End of GRE-2118
			if (billingAddress.getEmail() != null)
			{
				parameterMap.put("customer_email", billingAddress.getEmail());
			}
			if (billingAddress.getPhone() != null)
			{
				parameterMap.put("customer_phone", billingAddress.getPhone());
			}
			if (billingAddress.getTown() != null)
			{
				parameterMap.put("city", billingAddress.getTown());
			}
			if ( billingAddress.getRegion() != null)
			{
				parameterMap.put("state", billingAddress.getRegion().getName());
			}
			parameterMap.put("zip", billingAddress.getPostalCode());
			if (billingAddress.getCountry() != null)
			{
				parameterMap.put("country", billingAddress.getCountry().getIsocode());
			}
		}
		parameterMap.put("collectAddress", getFetchBillingAddressOnUI());
		if (deliveryAddress != null)
		{
			if (deliveryAddress.getCompanyName() != null)
			{
				parameterMap.put("delivery_company", deliveryAddress.getCompanyName());
			}
			if (deliveryAddress.getFirstName() != null)
			{
				parameterMap.put("delivery_firstname", deliveryAddress.getFirstName());
			}
			if (deliveryAddress.getLastName() != null)
			{
				parameterMap.put("delivery_lastname", deliveryAddress.getLastName());
			}
			if (deliveryAddress.getLine1() != null)
			{
				parameterMap.put("delivery_address", deliveryAddress.getLine1());
			}
			if (deliveryAddress.getLine2() != null)
			{
				parameterMap.put("delivery_address2", deliveryAddress.getLine2());
			}
			if (deliveryAddress.getEmail() != null)
			{
				parameterMap.put("delivery_email", deliveryAddress.getEmail());
			}
			if (deliveryAddress.getPhone() != null)
			{
				parameterMap.put("delivery_phone", deliveryAddress.getPhone());
			}
			if (deliveryAddress.getTown() != null)
			{
				parameterMap.put("delivery_city", deliveryAddress.getTown());
			}
			if (deliveryAddress.getRegion() != null)
			{
				parameterMap.put("delivery_state", deliveryAddress.getRegion().getName());
			}
			parameterMap.put("delivery_postal_code", deliveryAddress.getPostalCode());
			if (deliveryAddress.getCountry() != null)
			{
				parameterMap.put("delivery_country", deliveryAddress.getCountry().getIsocode());
			}

		}
		final String currencyIso = source.getCurrency().getIsocode();

		parameterMap.put("currency_code", currencyIso);

		//		parameterMap.put("_charset_", "utf-8");

		/*
		 * if (secureTradingHOPPaymentData.getChannel().equalsIgnoreCase("cscockpit")) {
		 * parameterMap.put("accounttypedescription", "MOTO"); } else { parameterMap.put("accounttypedescription",
		 * "ECOM"); }
		 */


		target.setParameters(parameterMap);

		target.setPostUrl(source.getMerchantURL());
	}

	private String getHostedSecureId(String currencyIsoCode)
	{
		return getSiteConfigService().getString("paymentech.config.paymentpages.hostedSecureId." + currencyIsoCode, "cpt0000000SB");
	}

	private String getAction()
	{
		return getSiteConfigService().getString("paymentech.config.paymentpages.action", "buildForm");
	}

	private String getHostedTokenize()
	{
		return getSiteConfigService().getString("paymentech.config.paymentpages.hostedTokenize", "store_authorize");
	}

	private String getTransactionType()
	{
		return getSiteConfigService().getString("paymentech.config.paymentpages.transType", "store_authorize");
	}

	private String getPaymentType()
	{
		return getSiteConfigService().getString("paymentech.config.paymentpages.paymentType", "Credit_Card");
	}

	private String getAllowedTypes()
	{
		return getSiteConfigService().getString("paymentech.config.paymentpages.allowedTypes", "MasterCard");
	}

	private String getCollectAddress()
	{
		return getSiteConfigService().getString("paymentech.config.paymentpages.collectAddress", "0");
	}

	private String getRequired()
	{
		return getSiteConfigService().getString("paymentech.config.paymentpages.required", "all");
	}

	private String getCardIndicators()
	{
		return getSiteConfigService().getString("paymentech.config.paymentpages.cardIndicators", "N");
	}

	private String getAccountVerification()
	{
		return getSiteConfigService().getString("paymentech.config.paymentpages.accountVerification", "yes");
	}

	public SiteConfigService getSiteConfigService()
	{
		return siteConfigService;
	}
	
	private String getFetchBillingAddressOnUI()
	{
		return getSiteConfigService().getString("paymentech.config.paymentpages.fetchBillingAddressOnUI", "0");
	}
	

	@Required
	public void setSiteConfigService(final SiteConfigService siteConfigService)
	{
		this.siteConfigService = siteConfigService;
	}
	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}
}
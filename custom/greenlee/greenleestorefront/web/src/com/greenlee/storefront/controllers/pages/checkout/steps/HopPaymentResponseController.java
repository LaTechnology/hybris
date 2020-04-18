/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.greenlee.storefront.controllers.pages.checkout.steps;


import de.hybris.platform.acceleratorfacades.payment.data.PaymentSubscriptionResultData;
import de.hybris.platform.acceleratorservices.payment.data.PaymentData;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.validation.ValidationResults;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.b2bacceleratorfacades.checkout.data.PlaceOrderData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.Config;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.greenlee.facades.customer.GreenleeUserFacade;
import com.greenlee.storefront.controllers.ControllerConstants;
import com.paymentech.hop.facades.PTHOPPaymentFacade;


@Controller
@RequestMapping(value = "/checkout/multi/hop")
public class HopPaymentResponseController extends PaymentMethodCheckoutStepController
{
	private static final Logger LOG = Logger.getLogger(HopPaymentResponseController.class);

	private static final String ERROR_CODE = "errorCode";
	protected static final String HOP_CALLBACK_PAGE = "hopCallback/cardCallbackSuccess";
	protected static final String HOP_CALLBACK_ERROR_PAGE = "hopCallback/cardCallbackError";
	protected static final String HOP_CALLBACK_CANCEL_PAGE = "hopCallback/cardCallbackCancel";
	protected static final String HOP_CALLBACK_WHATSTHIS_PAGE = "hopCallback/cardCallbackWhatsthis";
	protected static final String REDIRECT_URL_ADD_PAYMENT_METHOD_ERROR = REDIRECT_PREFIX + "/checkout/multi/payment-method/error";

	protected static final String HOP_AVS_ALL_ERROR_CODE = "paymenttech.config.avs.error.code.all";
	protected static final String HOP_AVS_GREEN_ERROR_CODE = "paymenttech.config.avs.error.code.green";
	protected static final String HOP_AVS_YELLOW_ERROR_CODE = "paymenttech.config.avs.error.code.yellow";
	protected static final String HOP_AVS_PURPLE_ERROR_CODE = "paymenttech.config.avs.error.code.purple";

	protected static final String HOP_CVV_GREEN_ERROR_CODE = "paymenttech.config.cvv.error.code.green";
	protected static final String HOP_CVV_ALL_ERROR_CODE = "paymenttech.config.cvv.error.code.all";
	protected static final String HOP_CCV_YELLOW_ERROR_CODE = "paymenttech.config.cvv.error.code.yellow";
	protected static final String HOP_MAX_RETRY_ERROR_CODE = "paymentech.config.paymentpages.maxuserretries.response.code";
	//#AVS error code logic properties
	protected static final String avs_error_code_all = "1; 2; 3; 4; 5; 6; 7; 8; 9; A; B; C; D; E; F; G; H; J; JA; JB; JC; JD; M1; M2; M3; M4; M5; M6; M7; M8; N3; N4; N5; N6; N7; N8; N9; R; UK; X; Z";
	protected static final String avs_error_code_yellow = "1; 2; 3; 5; 7; A; C; D; E; F; G; M5; M6";
	protected static final String avs_error_code_purple = "3;4;R;6;8;J";
	protected static final String avs_error_code_green = "9;B;H;JA;JB;JC;JD;M1;M2;M3;M4";

	//#CVV error code logic properties
	protected static final String HOP_CVV_ALL_ERROR_CODE_CONST = "M;N;P;S;U;I;Y";
	protected static final String HOP_CVV_ALL_YELLOW_ERROR_CODE = "N;P;S;U;I;Y";
	protected static final String HOP_CVV_ALL_GREEN_ERROR_CODE = "M";
	protected static final String HOP_MAX_RETRY_ALL_YELLOW_ERROR_CODE = "9710;9711;9712;9713;9714;9715;9719";
	protected static final String HOP_BASE_EN_PROPERTIES_AVS = "checkout.multi.hostedOrderPageError.avs.";
	protected static final String HOP_BASE_EN_PROPERTIES_CVV = "checkout.multi.hostedOrderPageError.cvv.";
	protected static final String GREEN_G = "G";
	protected static final String YELLOW_Y = "Y";
	protected static final String PURPLE_P = "P";
	protected static final String PT_STATUS = "000";
	protected static final String GREEN = "777";
	protected static final String YELLOW = "999";
	protected static final String PURPLE = "888";
	protected static final String RETRY = "A5566";
	protected static final String YELLOW_MSG = "There is an error with the information provided for your credit card.   Please check information and resubmit.";
	protected static final String PURPLE_MSG = "There has been a system error in processing your credit card – please retry or contact Greenlee Customer Support.";
	protected static final String MAX_USER_RETRY_COUNT = "paymentech.config.paymentpages.maxuserretries";
	protected static final String MAX_USER_RETRY_COUNT_DEFAULT = "3";

	@Resource(name = "ptHOPPaymentFacade")
	private PTHOPPaymentFacade ptHOPPaymentFacade;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "cartFacade")
	private CartFacade cartFacade;

	private Map<String, String> verificationCCVErrors = new HashMap<String, String>();
	private Map<String, String> verificationAVSErrors = new HashMap<String, String>();

	@Resource(name = "greenleeUserFacade")
	private GreenleeUserFacade greenleeUserFacade;


	/**
	 * @return the verificationCCVErrors
	 */
	public Map<String, String> getVerificationCCVErrors()
	{
		return verificationCCVErrors;
	}

	/**
	 * @param verificationCCVErrors
	 *           the verificationCCVErrors to set
	 */
	public void setVerificationCCVErrors(final Map<String, String> verificationCCVErrors)
	{
		this.verificationCCVErrors = verificationCCVErrors;
	}

	/**
	 * @return the verificationAVSErrors
	 */
	public Map<String, String> getVerificationAVSErrors()
	{
		return verificationAVSErrors;
	}

	/**
	 * @param verificationAVSErrors
	 *           the verificationAVSErrors to set
	 */
	public void setVerificationAVSErrors(final Map<String, String> verificationAVSErrors)
	{
		this.verificationAVSErrors = verificationAVSErrors;
	}

	@RequestMapping(value = "/response", method = RequestMethod.GET)
	@RequireHardLogIn
	public String doHandleHopResponse(final HttpServletRequest request, final Model model,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		try
		{
			final int maxretry = Integer.valueOf(Config.getString(MAX_USER_RETRY_COUNT, MAX_USER_RETRY_COUNT_DEFAULT)).intValue();
			Integer totalErrCount = Integer.valueOf(0);
			LOG.info("Handle Response received from HOP Server....");
			final CartData cartData = cartFacade.getSessionCart();

			final Map<String, String> responseMap = getRequestParameterMap(request);
			final Enumeration myEnum = request.getParameterNames();
			while (myEnum.hasMoreElements())
			{
				final String paramName = (String) myEnum.nextElement();
				final String paramValue = request.getParameter(paramName);
				LOG.error("[HPP Response Name] : " + paramName + " [HPP Response Value] : " + paramValue);
			}
			// Handle HOP Data Validation errors
			LOG.error("Data Verification in progress....");
			final String errorCode = responseMap.get(ERROR_CODE);
			final String status = responseMap.get("status");
			final String cancel = responseMap.get("cancel");
			final String whatCVV2 = responseMap.get("whatCVV2");
			final String whatsThis = responseMap.get("whatsThis");
			final String ccType = responseMap.get("ccType");
			final String cardBrandSelected = responseMap.get("cardBrandSelected");
			if (StringUtils.isNotEmpty(ccType) && !ccType.equalsIgnoreCase(cardBrandSelected))
			{
				model.addAttribute(ERROR_CODE, "ERROR.001");
				return HOP_CALLBACK_ERROR_PAGE;
			}
			if (StringUtils.isNotEmpty(whatCVV2) && ("1").equals(whatCVV2) && StringUtils.isNotEmpty(whatsThis)
					&& ("cvv").equals(whatsThis))
			{
				LOG.info("Whats this popup for CVV...");
				return HOP_CALLBACK_WHATSTHIS_PAGE;
			}
			if (StringUtils.isNotEmpty(cancel) && ("1").equals(cancel))
			{
				LOG.error("Payment Transaction has been canceled...");
				return HOP_CALLBACK_CANCEL_PAGE;
			}

			if (StringUtils.isNotEmpty(errorCode))
			{
				final String errorCd = getErrorCode(errorCode);
				if (null != errorCd && StringUtils.isNotEmpty(errorCd) && StringUtils.isNotBlank(errorCd))
				{
					LOG.error("There was an error in the Payment Transaction..." + errorCd);
					model.addAttribute(ERROR_CODE, errorCd);
				}
				return HOP_CALLBACK_ERROR_PAGE;
			}
			// Handle AVS and CCV Verification errors
			if (status != null && errorCode != null && StringUtils.isNotEmpty(status) || status.equalsIgnoreCase(PT_STATUS)
					|| StringUtils.isBlank(errorCode))
			{
				final Map ccvYelloResponse = getCVVResponseCode(HOP_CCV_YELLOW_ERROR_CODE, YELLOW_Y);
				PerformCCVVerification(ccvYelloResponse, responseMap, cartData);

				final Map avsPurpleResponse = getAllAVSResponseCode(HOP_AVS_PURPLE_ERROR_CODE, PURPLE_P);
				final Map avsYellowResponse = getAllAVSResponseCode(HOP_AVS_YELLOW_ERROR_CODE, YELLOW_Y);
				PerformAVSVerification(avsPurpleResponse, avsYellowResponse, responseMap, cartData);

				final int ccvErrorCount = verificationCCVErrors.size();
				final int avsErrorCount = verificationAVSErrors.size();

				final boolean verificationAVSErrorFlg = verificationAVSErrors.containsKey(responseMap.get("AVSMatch").trim());
				final boolean avsPurpleMatchFlg = avsPurpleResponse.containsKey(responseMap.get("AVSMatch").trim());
				final boolean avsYellowMatchFlg = avsYellowResponse.containsKey(responseMap.get("AVSMatch").trim());

				final boolean cvvYellowMatchFlg = ccvYelloResponse.containsKey(responseMap.get("CVVMatch").trim());
				final boolean verificationCVVErrorFlg = verificationCCVErrors.containsKey(responseMap.get("CVVMatch").trim());
				boolean flag = false;
				if (getSessionService().getCurrentSession() != null)
				{
					if (getSessionService().getCurrentSession().getSessionId().equalsIgnoreCase(responseMap.get("sessionId").trim()))
					{
						int preError = 0;
						if (request.getSession().getAttribute("totalErrCount") == null)
						{ // 1st time
							totalErrCount = Integer.valueOf(preError + ccvErrorCount + avsErrorCount);
							flag = false;
						}
						if (request.getSession().getAttribute("totalErrCount") != null)
						{
							preError = Integer.valueOf(request.getSession().getAttribute("totalErrCount").toString()).intValue();
							if (preError != 0 && (ccvErrorCount == 0 && avsErrorCount == 0))
							{
								totalErrCount = Integer.valueOf(0);
								flag = false;
							}
							else
							{
								totalErrCount = Integer.valueOf(preError + ccvErrorCount + avsErrorCount);
								flag = false;
							}
						}
						verificationCCVErrors.clear();
						verificationAVSErrors.clear();
						request.getSession().setAttribute("totalErrCount", totalErrCount);
					}
				}
				if (!flag && totalErrCount.intValue() >= maxretry)
				{
					model.addAttribute(ERROR_CODE, "A5566");
					request.getSession().setAttribute(ERROR_CODE, "A5566");
					LOG.error("ERR_NTFY_SUPPORT_00025 - Max Retry Failed for the tranasction for the cart [" + cartData.getCode()
							+ "]");
					LOG.error("Max Retry Failed for the tranasction for the cart [" + cartData.getCode() + "]");
					return HOP_CALLBACK_ERROR_PAGE;
				}

				if (!flag && totalErrCount.intValue() != maxretry)
				{
					if (ccvErrorCount >= 1 && ccvErrorCount <= ((maxretry - 1)) && cvvYellowMatchFlg && verificationCVVErrorFlg)
					{
						model.addAttribute(ERROR_CODE, "999");
						return HOP_CALLBACK_ERROR_PAGE;
					}
					if (avsErrorCount >= 1 && avsErrorCount <= ((maxretry - 1)) && avsPurpleMatchFlg && verificationAVSErrorFlg)
					{
						model.addAttribute(ERROR_CODE, "888");
						return HOP_CALLBACK_ERROR_PAGE;
					}
					if (avsErrorCount >= 1 && avsErrorCount <= ((maxretry - 1)) && avsYellowMatchFlg && verificationAVSErrorFlg)
					{
						model.addAttribute(ERROR_CODE, "999");
						return HOP_CALLBACK_ERROR_PAGE;
					}
				}
				if (!flag && totalErrCount.intValue() == 0)
				{
					LOG.error("HOP Subscription Started");
					performHOPCreateSubscription(model, responseMap, cartData);
					totalErrCount = Integer.valueOf(0);
					request.setAttribute("totalErrCount", totalErrCount);// reset, if  there was no error raised
					LOG.error("Perform HOP Subscription Completed");
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error("Error occurred during processing Payment Transaction", e);
			LOG.error(e);
			LOG.error("ERR_NTFY_SUPPORT_00025 - Card Validataion failed for the cart ");
			model.addAttribute(ERROR_CODE, "ERROR.002");
			return HOP_CALLBACK_ERROR_PAGE;
		}
		return HOP_CALLBACK_PAGE;
	}

	private void PerformCCVVerification(final Map<String, String> ccvYelloResponse, final Map<String, String> responseParam,
			final CartData cartData)
	{
		final String cvvMatch = responseParam.get("CVVMatch").trim();
		LOG.error("PerformCCVVerification: " + cvvMatch);
		final Map ccvGreenResponse = getCVVResponseCode(HOP_CVV_GREEN_ERROR_CODE, GREEN_G);
		if (StringUtils.isNotEmpty(cvvMatch) && ccvGreenResponse.containsKey(cvvMatch) && cvvMatch == "M")
		{
			LOG.error("CVV Verification HOP Response [" + cvvMatch.trim() + " = "
					+ ccvGreenResponse.get(cvvMatch.toUpperCase().trim()) + "] for the cart [" + cartData.getCode() + "]");
		}

		if (StringUtils.isNotBlank(cvvMatch) && ccvYelloResponse.containsKey(cvvMatch) && cvvMatch != "M")
		{
			LOG.error("CVV Verification HOP Response [" + cvvMatch.trim() + " = "
					+ ccvYelloResponse.get(cvvMatch.toUpperCase().trim()) + "] for the cart [" + cartData.getCode() + "]");
			LOG.error("ERR_NTFY_SUPPORT_00027 - CCV Verification HOP Response [" + cvvMatch.trim() + " = "
					+ ccvYelloResponse.get(cvvMatch.toUpperCase().trim()) + "] for the cart [" + cartData.getCode() + "]");
			getVerificationCCVErrors().put(cvvMatch.toUpperCase().trim(),
					ccvYelloResponse.get(cvvMatch.toUpperCase().trim()).toString());
		}
	}

	private void PerformAVSVerification(final Map<String, String> avsPurpleResponse, final Map<String, String> avsYellowResponse,
			final Map<String, String> responseParam, final CartData cartData)
	{
		final String avsMatch = responseParam.get("AVSMatch").trim();
		LOG.error("PerformAVSVerification: " + avsMatch);
		if (StringUtils.isNotBlank(avsMatch) && StringUtils.isNotEmpty(avsMatch) && avsPurpleResponse.containsKey(avsMatch))
		{
			LOG.error("AVS Verification HOP Response [" + avsMatch.trim() + " = "
					+ avsPurpleResponse.get(avsMatch.toUpperCase().trim()) + "] for the cart [" + cartData.getCode() + "]");
			LOG.error("ERR_NTFY_SUPPORT_00028 - AVS Verification HOP Response [" + avsMatch.trim() + " = "
					+ avsPurpleResponse.get(avsMatch.toUpperCase().trim()) + "] for the cart [" + cartData.getCode() + "]");
			getVerificationAVSErrors().put(avsMatch.toUpperCase().trim(),
					avsPurpleResponse.get(avsMatch.toUpperCase().trim()).toString());
		}

		if (StringUtils.isNotBlank(avsMatch) && StringUtils.isNotEmpty(avsMatch) && avsYellowResponse.containsKey(avsMatch))
		{
			LOG.error("AVS Verification HOP Response [" + avsMatch.trim() + " = "
					+ avsYellowResponse.get(avsMatch.toUpperCase().trim()) + "] for the cart [" + cartData.getCode() + "]");
			LOG.error("ERR_NTFY_SUPPORT_00029 - AVS Verification HOP Response [" + avsMatch.trim() + " = "
					+ avsYellowResponse.get(avsMatch.toUpperCase().trim()) + "] for the cart [" + cartData.getCode() + "]");
			getVerificationAVSErrors().put(avsMatch.toUpperCase().trim(),
					avsYellowResponse.get(avsMatch.toUpperCase().trim()).toString());
		}

		final Map avsGreenResponse = getAllAVSResponseCode(HOP_AVS_GREEN_ERROR_CODE, GREEN_G);
		if (StringUtils.isNotBlank(avsMatch) && StringUtils.isNotEmpty(avsMatch) && avsGreenResponse.containsKey(avsMatch))
		{
			LOG.error("AVS Verification HOP Response [" + avsMatch.trim() + " = "
					+ avsGreenResponse.get(avsMatch.toUpperCase().trim()) + "] for the cart [" + cartData.getCode() + "]");
		}
	}

	private String performHOPCreateSubscription(final Model model, final Map<String, String> responseMap, final CartData cartData)
	{
		OrderData orderData = null;
		PaymentSubscriptionResultData paymentSubscriptionResultData = null;
		final PaymentData paymentData = ptHOPPaymentFacade.getPaymentData();

		final Map<String, String> inputData = paymentData.getParameters();
		for (final Map.Entry<String, String> entry : inputData.entrySet())
		{
			LOG.error("[Request HFP Name] : " + entry.getKey() + " [Request HFP Value] : " + entry.getValue());
		}

		paymentSubscriptionResultData = ptHOPPaymentFacade.completeHOPCreateSubscription(paymentData.getParameters(), responseMap,
				true);
		if (paymentSubscriptionResultData != null && paymentSubscriptionResultData.isSuccess()
				&& paymentSubscriptionResultData.getStoredCard() != null
				&& StringUtils.isNotBlank(paymentSubscriptionResultData.getStoredCard().getSubscriptionId()))
		{
			final CCPaymentInfoData newPaymentSubscription = paymentSubscriptionResultData.getStoredCard();

			if (getUserFacade().getCCPaymentInfos(true).size() <= 1)
			{
				getUserFacade().setDefaultPaymentInfo(newPaymentSubscription);
			}
			getCheckoutFacade().setPaymentDetails(newPaymentSubscription.getId());

			//Placing the order
			final PlaceOrderData placeOrderData = new PlaceOrderData();
			placeOrderData.setTermsCheck(Boolean.TRUE);
			try
			{
				orderData = getCheckoutFacade().placeOrder();
				//setDuplicateAddress
				setDuplicateAddress();
				model.addAttribute("orderData", orderData);
			}
			catch (final Exception e)
			{
				LOG.error("Failed to place Order", e);
				model.addAttribute(ERROR_CODE, "ERROR.003");
				return HOP_CALLBACK_ERROR_PAGE;
			}
		}
		else
		{
			LOG.error("ERR_NTFY_SUPPORT_00025 - Card Validataion failed for the cart [" + cartData.getCode() + "]");
			return HOP_CALLBACK_ERROR_PAGE;
		}
		return null;
	}

	protected void setDuplicateAddress()
	{
		final List<AddressData> addressBooks = greenleeUserFacade.getAddressBookEntries();
		for (final AddressData addressData : addressBooks)
		{
			LOG.info("Selected Address ID " + addressData.getSelectedBillingAddressId());
			final String selectedAddressId = addressData.getSelectedBillingAddressId();//8796159049751
			final String newGeneratedAddressId = addressData.getId();//8796159180823 cloned
			LOG.info("addressData.isBillingAddress() " + addressData.isBillingAddress()
					+ " addressData.getPrimaryAddress().booleanValue()  " + addressData.getPrimaryAddress().booleanValue());
			if (addressData.isBillingAddress())
			{
				if (!selectedAddressId.equals(newGeneratedAddressId))
				{
					LOG.info("Selected Address ID " + selectedAddressId + " newGeneratedAddressId " + newGeneratedAddressId);
					addressData.setBillingAddress(false);
					greenleeUserFacade.setFlagForBillingAddress(addressData);
				}
			}
			if (addressData.getPrimaryAddress() != null && addressData.getPrimaryAddress().booleanValue())
			{
				if (!selectedAddressId.equals(newGeneratedAddressId))
				{
					LOG.info("PrimaryAddress Selected Address ID " + selectedAddressId + " newGeneratedAddressId "
							+ newGeneratedAddressId);
					addressData.setPrimaryAddress(Boolean.FALSE);
					greenleeUserFacade.setFlagForBillingAddress(addressData);
				}
			}
		}
	}

	/**
	 *
	 * @param errorType
	 * @return Map<String, String>
	 */
	private Map<String, String> getAllAVSResponseCode(final String errorType, final String colorCode)
	{
		Map<String, String> mapResponse = null;
		String[] avsResponseString = null;
		if (StringUtils.isNotEmpty(errorType))
		{
			if (colorCode == GREEN_G)
			{
				avsResponseString = Config.getString(errorType, avs_error_code_green).split(";");
			}
			else if (colorCode == YELLOW_Y)
			{
				avsResponseString = Config.getString(errorType, avs_error_code_yellow).split(";");
			}
			else if (colorCode == PURPLE_P)
			{
				avsResponseString = Config.getString(errorType, avs_error_code_purple).split(";");
			}
			mapResponse = new HashMap<String, String>();
			for (final String resString : avsResponseString)
			{
				final String key = "avs.response.hop.error." + resString.trim();
				final String errorMessage = getMessageSource().getMessage(key, null, getI18nService().getCurrentLocale());
				mapResponse.put(resString.toString().toUpperCase().trim(), errorMessage);
			}
		}
		return mapResponse;
	}

	/**
	 * This service to provide the CVV respones code WRT the param passed.
	 */
	private Map<String, String> getCVVResponseCode(final String errorType, final String colorCode)
	{
		Map<String, String> mapResponse = null;
		String[] avsResponseString = null;

		if (colorCode == GREEN_G)
		{
			avsResponseString = Config.getString(errorType, HOP_CVV_ALL_GREEN_ERROR_CODE).split(";");
		}
		else if (colorCode == YELLOW_Y)
		{
			avsResponseString = Config.getString(errorType, HOP_CVV_ALL_YELLOW_ERROR_CODE).split(";");
		}
		mapResponse = new HashMap<String, String>();
		for (final String resString : avsResponseString)
		{
			final String key = "cvv.response.hop.error." + resString.trim();
			final String errorMessage = getMessageSource().getMessage(key, null, getI18nService().getCurrentLocale());
			mapResponse.put(resString.toString().toUpperCase().trim(), errorMessage);
		}
		return mapResponse;
	}

	@SuppressWarnings("unused")
	private Map<String, String> getMaxRetryErrorCodeAndMsg(final String errorType)
	{
		Map<String, String> mapResponse = null;
		String[] maxRetryErrorCode = null;
		maxRetryErrorCode = Config.getString(errorType, HOP_MAX_RETRY_ALL_YELLOW_ERROR_CODE).split(";");
		mapResponse = new HashMap<String, String>();
		for (final String resString : maxRetryErrorCode)
		{
			final String key = "paymentech.config.paymentpages.retry.response.code." + resString.trim();
			final String errorMessage = getMessageSource().getMessage(key, null, getI18nService().getCurrentLocale());
			mapResponse.put(resString.toString().toUpperCase().trim(), errorMessage);
		}
		return mapResponse;
	}

	@RequestMapping(value = "/error", method = RequestMethod.GET)
	public String doHostedOrderPageError(@RequestParam(required = true) final String errorCode, final Model model,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		String redirectUrl = REDIRECT_URL_ADD_PAYMENT_METHOD;
		final ValidationResults validationResults = getCheckoutStep().validate(redirectAttributes);
		if (getCheckoutStep().checkIfValidationErrors(validationResults))
		{
			redirectUrl = getCheckoutStep().onValidation(validationResults);
		}
		model.addAttribute("redirectUrl", redirectUrl.replace(REDIRECT_PREFIX, ""));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				getResourceBreadcrumbBuilder().getBreadcrumbs("checkout.multi.hostedOrderPageError.breadcrumb"));
		storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		setCheckoutStepLinksForModel(model, getCheckoutStep());

		model.addAttribute(ERROR_CODE, errorCode);
		GlobalMessages.addErrorMessage(model, "checkout.multi.hostedOrderPageError." + errorCode);

		return ControllerConstants.Views.Pages.MultiStepCheckout.HostedOrderPageErrorPage;
	}

	private String getErrorCode(final String errorCode)
	{
		String error = null;
		if (errorCode.endsWith("|"))
		{
			error = errorCode.substring(0, errorCode.indexOf('|'));
			LOG.info("getErrorCode >> " + error);
		}
		else
		{
			final StringBuffer buffer = new StringBuffer(errorCode);
			buffer.append("|");
			error = buffer.substring(0, buffer.toString().indexOf('|'));
			LOG.info("getErrorCode else >> " + error);
		}
		return error;
	}

}

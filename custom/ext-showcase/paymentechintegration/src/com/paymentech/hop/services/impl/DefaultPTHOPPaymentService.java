package com.paymentech.hop.services.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.acceleratorservices.payment.data.PaymentData;
import de.hybris.platform.acceleratorservices.payment.data.PaymentSubscriptionResultItem;
import de.hybris.platform.b2bacceleratorservices.enums.CheckoutPaymentType;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commerceservices.customer.CustomerEmailResolutionService;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.commerceservices.order.CommerceCheckoutService;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.core.enums.PaymentStatus;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.dto.TransactionStatusDetails;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.paymentech.constants.PaymentechintegrationConstants;
import com.paymentech.hop.services.PTHOPPaymentService;
import com.paymentech.hop.strategies.CreateHOPPaymentRequestStrategy;
import com.paymentech.payment.data.CreateHOPPaymentRequest;

/**
 * @author dipankan
 *
 */
public class DefaultPTHOPPaymentService implements PTHOPPaymentService {
	private static final Logger LOG = Logger
			.getLogger(DefaultPTHOPPaymentService.class);
	private CreateHOPPaymentRequestStrategy createHOPPaymentRequestStrategy;
	private Converter<CreateHOPPaymentRequest, PaymentData> paymentDataConverter;
	private ModelService modelService;
	private UserService userService;
	private CommerceCheckoutService commerceCheckoutService;
	private CommonI18NService commonI18NService;
	private CustomerEmailResolutionService customerEmailResolutionService;
	private SiteConfigService siteConfigService;
	private UserFacade userFacade;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.paymenttech.hop.services.PTHOPPaymentService#beginHopPayment(de.hybris
	 * .platform.core.model.c2l.CurrencyModel, java.lang.Double,
	 * de.hybris.platform.core.model.user.AddressModel,
	 * de.hybris.platform.core.model.user.AddressModel,
	 * de.hybris.platform.core.model.order.CartModel, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public PaymentData beginHopPayment(final CurrencyModel currency,
			final Double totalAmount, final CartData activeCartData,
			final String merchantUrl, final String channel) {

		Assert.notNull(merchantUrl, "The MerchantURL cannot be null");

		final CreateHOPPaymentRequest request = getCreateHOPPaymentRequestStrategy()
				.createHOPPaymentRequest(currency, totalAmount, activeCartData,
						merchantUrl);

		PaymentData data = getPaymentDataConverter().convert(request);
		if (data == null) {
			data = new PaymentData();
			data.setParameters(new HashMap<String, String>());
		}

		return data;
	}

	public boolean storePaymentInfoForUser(
			final PaymentInfoModel paymentInfoModel,
			final CustomerModel customerModel) {
		validateParameterNotNull(paymentInfoModel,
				"payment info model cannot be null");

		return false;
	}

	public boolean storePaymentInfoForCart(
			final PaymentInfoModel paymentInfoModel, final CartModel cartModel) {
		validateParameterNotNull(cartModel, "Cart model cannot be null");
		validateParameterNotNull(paymentInfoModel,
				"payment info model cannot be null");

		cartModel.setPaymentInfo(paymentInfoModel);
		getModelService().saveAll(paymentInfoModel, cartModel);
		getModelService().refresh(cartModel);

		if (paymentInfoModel != null) {
			final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
			parameter.setEnableHooks(true);
			parameter.setCart(cartModel);
			parameter.setPaymentInfo(paymentInfoModel);
			return getCommerceCheckoutService().setPaymentInfo(parameter);
		}

		return false;
	}

	protected CreditCardPaymentInfoModel saveAuthorization(
			final Map<String, String> inputParam,
			final CustomerModel customerModel,
			final Map<String, String> parameters,
			final String subscriptionInfo, final boolean saveInAccount, final CartModel cartModel) {

		validateParameterNotNull(subscriptionInfo,
				"subscriptionInfo cannot be null");
		LOG.error("cartModel.getSelectedBillingAddress() "+cartModel.getSelectedBillingAddress().getPk().getLongValue());
		final AddressModel billingAddress =cartModel.getSelectedBillingAddress();
//		final AddressModel billingAddress = getCustomerBillingAddress();
		// final AddressModel billingAddress =
		// createBillingAddress(customerModel,inputParam);
		final CreditCardPaymentInfoModel cardPaymentInfoModel = createCustomerCardInfo(
				subscriptionInfo, parameters, billingAddress, customerModel,
				saveInAccount);
		if (billingAddress != null && billingAddress.getOwner() == null) {
			billingAddress.setOwner(getUserService().getCurrentUser());
		}
		if (CustomerType.GUEST.equals(customerModel.getType())) {
			final StringBuilder name = new StringBuilder();
			if (!StringUtils.isBlank(parameters.get("customer_firstname"))) {
				name.append(parameters.get("customer_firstname"));
				name.append(" ");
			}
			if (!StringUtils.isBlank(parameters.get("customer_lastname"))) {
				name.append(parameters.get("customer_lastname"));
			}
			if (StringUtils.isNotEmpty(name.toString())) {
				customerModel.setName(name.toString());
				getModelService().save(customerModel);
			}
		}

		getModelService().save(cardPaymentInfoModel); //Duplication address created
		getModelService().refresh(customerModel);

		final List<PaymentInfoModel> paymentInfoModels = new ArrayList<PaymentInfoModel>(
				customerModel.getPaymentInfos());
		if (!paymentInfoModels.contains(cardPaymentInfoModel)) {
			paymentInfoModels.add(cardPaymentInfoModel);
			if (saveInAccount) {
				customerModel.setPaymentInfos(paymentInfoModels);
				getModelService().save(customerModel);
			}

			getModelService().save(cardPaymentInfoModel);
			getModelService().refresh(customerModel);
		}

		return cardPaymentInfoModel;
	}

	private AddressModel createBillingAddress(
			final CustomerModel customerModel,
			final Map<String, String> parameters) {
		AddressModel billingAddress = getModelService().create(
				AddressModel.class);
		if (parameters.get("customer_firstname") != null) {
			billingAddress.setFirstname(parameters.get("customer_firstname"));
			billingAddress.setLastname(parameters.get("customer_lastname"));
			/*
			 * if(StringUtils.isNotEmpty(parameters.get("customer_address")) &&
			 * StringUtils.isNotEmpty(parameters.get("customer_address2"))){
			 * billingAddress.setLine2(parameters.get("customer_address"));
			 * }else{ //address 2 is empty
			 * billingAddress.setLine2(parameters.get("customer_address")); }
			 * if(StringUtils.isNotEmpty(parameters.get("customer_address")) &&
			 * StringUtils.isNotEmpty(parameters.get("customer_address2"))){
			 * billingAddress.setLine1(parameters.get("customer_address2"));
			 * }else{
			 * billingAddress.setLine1(parameters.get("customer_address2")); }
			 */
			if (StringUtils.isNotEmpty(parameters.get("customer_address"))) {
				billingAddress.setLine1(parameters.get("customer_address"));
			}
			if (StringUtils.isNotEmpty(parameters.get("customer_address2"))) {
				billingAddress.setLine2(parameters.get("customer_address2"));
			}

			// billingAddress.setLine1(parameters.get("customer_address"));
			// billingAddress.setLine2(parameters.get("customer_address2"));

			billingAddress.setTown(parameters.get("customer_city"));
			// billingAddress.set(parameters.get("customer_state"));
			billingAddress
					.setPostalcode(parameters.get("customer_postal_code"));
			CountryModel country;

			if (parameters.get("customer_country") != null
					&& !StringUtils.isEmpty(parameters.get("customer_country"))) {
				country = getCommonI18NService().getCountry(
						parameters.get("customer_country"));
				billingAddress.setCountry(country);
			}
		} else if (parameters.get("name") != null) {
			String name = parameters.get("name");
			if (name.indexOf(" ") != -1
					&& name.lastIndexOf(" ") != name.length() - 1) {
				billingAddress.setFirstname(name.substring(0,
						name.lastIndexOf(" ")));
				billingAddress.setLastname(name.substring(
						name.lastIndexOf(" "), name.length()));
			} else {
				billingAddress.setFirstname(name);
				billingAddress.setLastname("");
			}
			billingAddress.setLine1(parameters.get("address1"));
			billingAddress.setLine2(parameters.get("address2"));
			billingAddress.setTown(parameters.get("city"));

			// billingAddress.set(parameters.get("customer_state"));
			billingAddress.setPostalcode(parameters.get("postal_code"));
			CountryModel country;

			if (parameters.get("country") != null
					&& !StringUtils.isEmpty(parameters.get("country"))) {
				country = getCommonI18NService().getCountry(
						parameters.get("country"));
				if ((StringUtils.equals(country.getIsocode(), "US") || StringUtils
						.equals(country.getIsocode(), "CA"))
						&& StringUtils.isNotBlank(parameters.get("province"))) {
					RegionModel region = getCommonI18NService().getRegion(
							country,
							country.getIsocode() + "-"
									+ parameters.get("province"));
					if (region != null) {
						billingAddress.setRegion(region);
					}
				}
				/*
				 * for(RegionModel
				 * region:getCommonI18NService().getAllRegions()) {
				 * if(region.getIsocodeShort
				 * ().equalsIgnoreCase(parameters.get("province"))) {
				 * billingAddress.setRegion(region); break; } }
				 */
				billingAddress.setCountry(country);
			}
		} else {
			billingAddress = getCurrentCustomerBillingAddress();
		}

		final String email = getCustomerEmailResolutionService()
				.getEmailForCustomer(customerModel);
		if (billingAddress != null) {
			billingAddress.setEmail(email);
		}
		return billingAddress;
	}

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

	private AddressModel getCustomerBillingAddress() {
		CustomerModel customerModel = (CustomerModel) getUserService()
				.getCurrentUser();
		for (final AddressModel billingAddress : customerModel.getAddresses()) {
			if (Boolean.TRUE.equals(billingAddress.getBillingAddress())
					&& billingAddress.getSelectedBillingAddressId() != null
					&& billingAddress.getSelectedBillingAddressId()
							.equalsIgnoreCase(
									billingAddress.getPk()
											.getLongValueAsString())) {
				LOG.error("Address Pk ["
						+ billingAddress.getPk().getLongValueAsString()
						+ " ] for the customer [ " + customerModel.getUid()
						+ "] Selected Billing Address Code [ "
						+ billingAddress.getSelectedBillingAddressId() + " ]");
				return billingAddress;
			}
		}
		// if null then return the default payment address stored during the add
		// method invoked.
		return customerModel.getDefaultPaymentAddress();
	}

	protected CreditCardPaymentInfoModel createCustomerCardInfo(
			final String subscriptionInfo,
			final Map<String, String> parameters,
			final AddressModel billingAddress,
			final CustomerModel customerModel, final boolean saveInAccount) {
		validateParameterNotNull(subscriptionInfo,
				"subscriptionInfo cannot be null");
		validateParameterNotNull(billingAddress,
				"billingAddress cannot be null");
		validateParameterNotNull(customerModel, "customerModel cannot be null");

		final CreditCardPaymentInfoModel cardPaymentInfoModel = getModelService()
				.create(CreditCardPaymentInfoModel.class);
		cardPaymentInfoModel.setBillingAddress(billingAddress);
		cardPaymentInfoModel.setCode(customerModel.getUid() + "_"
				+ UUID.randomUUID());
		cardPaymentInfoModel.setUser(customerModel);
		final String ccNumber = parameters.get("ccNumber");

		cardPaymentInfoModel.setNumber(ccNumber);
		cardPaymentInfoModel.setType(CreditCardType.valueOf(getCardType(
				parameters.get("ccType")).toUpperCase()));
		cardPaymentInfoModel.setCcOwner(getCCOwner(parameters.get("name")));

		cardPaymentInfoModel.setValidToMonth(parameters.get("expMonth"));
		cardPaymentInfoModel.setValidToYear(parameters.get("expYear"));

		cardPaymentInfoModel.setCustomerRefNumber(parameters
				.get("customerRefNum"));

		cardPaymentInfoModel.setSubscriptionId(subscriptionInfo);
		cardPaymentInfoModel.setSaved(saveInAccount);

		return cardPaymentInfoModel;
	}

	private String getCardType(final String cardType) {
		if (StringUtils.isNotEmpty(cardType)) {
			if (cardType.equalsIgnoreCase("mastercard")) {
				return "master";
			} else if (cardType.equalsIgnoreCase("american express")) {
				return "amex";
			}
		}

		return cardType;
	}

	protected String getCCOwner(final String owner) {
		String ccOwner = StringUtils.EMPTY;
		try {
			if (StringUtils.isNotEmpty(owner)) {
				ccOwner = URLDecoder.decode(owner, "UTF-8");
			}
		} catch (final UnsupportedEncodingException ex) {
			LOG.error("Unable to decode the String {} ", ex);
		}
		return ccOwner;
	}

	private PaymentTransactionEntryModel savePaymentTransactionEntry(
			final CustomerModel customerModel,
			final Map<String, String> parameters, final CartModel cartModel) {
		validateParameterNotNull(parameters.get("customerRefNum"),
				"orderInfoData cannot be null");

		final PaymentTransactionModel transaction = getModelService().create(
				PaymentTransactionModel.class);
		transaction.setCode(cartModel.getCode() + "_" + UUID.randomUUID());
		transaction.setRequestId(parameters.get("customerRefNum"));
		if (isAllowedCardType(parameters.get("ccType"))) {
			transaction.setRequestToken(String.valueOf(getSiteConfigService()
					.getInt("securetrading.config.paymentpages.settlestatus",
							-1)));
		} else {
			transaction.setRequestToken("0");
		}

		if (getCardType(parameters.get("ccType")).toUpperCase().equals(
				PaymentechintegrationConstants.STR_VISA)) {
			transaction
					.setPaymentProvider(PaymentechintegrationConstants.PAYMENT_PROVIDER_VISA);
		} else if (getCardType(parameters.get("ccType")).toUpperCase().equals(
				PaymentechintegrationConstants.STR_MASTERCARD)) {
			transaction
					.setPaymentProvider(PaymentechintegrationConstants.PAYMENT_PROVIDER_MC);
		} else if (getCardType(parameters.get("ccType")).toUpperCase().equals(
				PaymentechintegrationConstants.STR_AMEX)) {
			transaction
					.setPaymentProvider(PaymentechintegrationConstants.PAYMENT_PROVIDER_AMEX);
		} else {
			transaction
					.setPaymentProvider(PaymentechintegrationConstants.PAYMENT_PROVIDER);
		}
		transaction.setOwner(customerModel);
		transaction.setOrder(cartModel);
		getModelService().save(transaction);

		final PaymentTransactionEntryModel entry = getModelService().create(
				PaymentTransactionEntryModel.class);
		entry.setType(PaymentTransactionType.CARD_VERIFICATION);
		entry.setRequestId(parameters.get("transId"));
		entry.setRequestToken(parameters.get("transId"));
		final Date date = java.util.Calendar.getInstance(
				java.util.TimeZone.getTimeZone("GMT")).getTime();
		entry.setTime(date);
		entry.setPaymentTransaction(transaction);
		addTransactionStatus(entry, parameters);
		entry.setCurrency(cartModel.getCurrency());
		entry.setOwner(transaction);
		// entry.setAmount(new BigDecimal(cartModel.getTotalPrice()));
		addAuthorizedAmount(entry, parameters, cartModel);
		entry.setSubscriptionID(parameters.get("transId"));

		final String newEntryCode;
		if (transaction.getEntries() == null) {
			newEntryCode = transaction.getCode() + "-1";
		} else {
			newEntryCode = transaction.getCode() + "-"
					+ (transaction.getEntries().size() + 1);
		}

		entry.setCode(newEntryCode);
		getModelService().save(entry);
		getModelService().refresh(transaction);

		List<PaymentTransactionModel> paymentTransactionModels = null;

		if (cartModel.getPaymentTransactions() != null) {
			paymentTransactionModels = new ArrayList<PaymentTransactionModel>(
					cartModel.getPaymentTransactions());
		} else {
			paymentTransactionModels = new ArrayList<PaymentTransactionModel>();
		}

		paymentTransactionModels.add(transaction);
		cartModel.setPaymentTransactions(paymentTransactionModels);
		cartModel.setPaymentStatus(PaymentStatus.PAID);
		cartModel.setPaymentType(CheckoutPaymentType.CARD);
		getModelService().saveAll(cartModel);
		return entry;
	}

	private boolean isAllowedCardType(final String cardType) {
		final String cardTypes = getSiteConfigService().getString(
				"securetrading.config.paymentpages.settlestatus", null);
		if (StringUtils.isNotEmpty(cardTypes)) {
			final List<String> listOfCards = Arrays.asList(cardTypes
					.split(Pattern.quote("|")));
			if (listOfCards.contains(cardType)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param parameters
	 * @return hasError
	 */
	public boolean hasPaymentError(final Map<String, String> parameters) {
		boolean hasError = false;
		final String error = parameters
				.get(PaymentechintegrationConstants.ERROR_CODE);
		final String status = parameters
				.get(PaymentechintegrationConstants.TRANSACTION_STATUS);
		if (StringUtils.isNotEmpty(error)
				|| (StringUtils.isNotEmpty(status) && !status.equals("000"))) {
			hasError = true;
		}
		return hasError;

	}

	private void addAuthorizedAmount(final PaymentTransactionEntryModel entry,
			final Map<String, String> parameters, final CartModel cartModel) {
		final String amount = parameters.get("amount");
		if (StringUtils.isNotEmpty(amount)) {
			entry.setAmount(new BigDecimal(amount));
		} else {
			entry.setAmount(new BigDecimal(cartModel.getTotalPrice()
					.doubleValue()));
		}
	}

	private void addTransactionStatus(final PaymentTransactionEntryModel entry,
			final Map<String, String> parameters) {
		TransactionStatus status = TransactionStatus.ERROR;
		TransactionStatusDetails details = TransactionStatusDetails.COMMUNICATION_PROBLEM;
		if (hasPaymentError(parameters)) {
			status = TransactionStatus.REJECTED;
			details = TransactionStatusDetails.AUTHORIZATION_REJECTED_BY_PSP;
		} else {
			status = TransactionStatus.ACCEPTED;
			details = TransactionStatusDetails.SUCCESFULL;
		}
		entry.setTransactionStatus(status.name());
		entry.setTransactionStatusDetails(details.name());
	}

	/**
	 * @return the paymentDataConverter
	 */
	public Converter<CreateHOPPaymentRequest, PaymentData> getPaymentDataConverter() {
		return paymentDataConverter;
	}

	/**
	 * @param paymentDataConverter
	 *            the paymentDataConverter to set
	 */
	@Required
	public void setPaymentDataConverter(
			final Converter<CreateHOPPaymentRequest, PaymentData> paymentDataConverter) {
		this.paymentDataConverter = paymentDataConverter;
	}

	/**
	 * @return the createHOPPaymentRequestStrategy
	 */
	public CreateHOPPaymentRequestStrategy getCreateHOPPaymentRequestStrategy() {
		return createHOPPaymentRequestStrategy;
	}

	/**
	 * @param createHOPPaymentRequestStrategy
	 *            the createHOPPaymentRequestStrategy to set
	 */
	@Required
	public void setCreateHOPPaymentRequestStrategy(
			final CreateHOPPaymentRequestStrategy createHOPPaymentRequestStrategy) {
		this.createHOPPaymentRequestStrategy = createHOPPaymentRequestStrategy;
	}

	public ModelService getModelService() {
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService) {
		this.modelService = modelService;
	}

	public CommerceCheckoutService getCommerceCheckoutService() {
		return commerceCheckoutService;
	}

	@Required
	public void setCommerceCheckoutService(
			final CommerceCheckoutService commerceCheckoutService) {
		this.commerceCheckoutService = commerceCheckoutService;
	}

	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService) {
		this.commonI18NService = commonI18NService;
	}

	public CustomerEmailResolutionService getCustomerEmailResolutionService() {
		return customerEmailResolutionService;
	}

	@Required
	public void setCustomerEmailResolutionService(
			final CustomerEmailResolutionService customerEmailResolutionService) {
		this.customerEmailResolutionService = customerEmailResolutionService;
	}

	public SiteConfigService getSiteConfigService() {
		return siteConfigService;
	}

	@Required
	public void setSiteConfigService(final SiteConfigService siteConfigService) {
		this.siteConfigService = siteConfigService;
	}

	/**
	 * @return the userFacade
	 */
	public UserFacade getUserFacade() {
		return userFacade;
	}

	/**
	 * @param userFacade
	 *            the userFacade to set
	 */
	@Required
	public void setUserFacade(final UserFacade userFacade) {
		this.userFacade = userFacade;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.paymentech.hop.services.PTHOPPaymentService#
	 * completeHopCreatePaymentSubscription(de.hybris.platform.core.model
	 * .order.CartModel, boolean, java.util.Map)
	 */
	@Override
	public PaymentSubscriptionResultItem completeHopCreatePaymentSubscription(
			Map<String, String> reqestMap, CartModel cartModel,
			boolean saveInAccount, Map<String, String> parameters) {

		final CustomerModel customerModel = (CustomerModel) cartModel.getUser();
		customerModel.setCustomerRefNumber(parameters.get("customerRefNum"));
		final PaymentSubscriptionResultItem paymentSubscriptionResult = new PaymentSubscriptionResultItem();

		final String transStatus = parameters.get("status");
		if (!hasPaymentError(parameters)) {
			paymentSubscriptionResult.setSuccess(true);
			paymentSubscriptionResult.setDecision(transStatus);
			paymentSubscriptionResult.setResultCode(transStatus);
		} else {
			paymentSubscriptionResult.setDecision("ERROR");
			paymentSubscriptionResult.setResultCode("TAMPERED");
			return paymentSubscriptionResult;
		}

		final PaymentTransactionEntryModel entry = savePaymentTransactionEntry(
				customerModel, parameters, cartModel);
		final String paymentType = parameters.get("paymentType");
		if (StringUtils.isNotEmpty(paymentType) && paymentType.equals("CC")
				&& StringUtils.isNotEmpty(parameters.get("customerRefNum"))) {
			final CreditCardPaymentInfoModel cardPaymentInfoModel = saveAuthorization(
					reqestMap, customerModel, parameters,
					parameters.get("transId"), saveInAccount,cartModel);
			paymentSubscriptionResult.setStoredCard(cardPaymentInfoModel);
			getCommerceCheckoutService().setPaymentInfo(cartModel,
					cardPaymentInfoModel);
			cardPaymentInfoModel.setSubscriptionValidated(true);
			getModelService().save(cardPaymentInfoModel);
			entry.getPaymentTransaction().setInfo(cardPaymentInfoModel);
			getModelService().save(entry.getPaymentTransaction());

			storePaymentInfoForCart(cardPaymentInfoModel, cartModel);
		}

		return paymentSubscriptionResult;
	}
}

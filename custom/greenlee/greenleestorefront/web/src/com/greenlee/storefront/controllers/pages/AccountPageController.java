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
package com.greenlee.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.AddressForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.UpdateEmailForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.UpdatePasswordForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.AddressValidator;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.EmailValidator;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.PasswordValidator;
import de.hybris.platform.acceleratorstorefrontcommons.forms.verification.AddressVerificationResultHandler;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.b2bacceleratorservices.company.CompanyB2BCommerceService;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.address.AddressVerificationFacade;
import de.hybris.platform.commercefacades.address.data.AddressVerificationResult;
import de.hybris.platform.commercefacades.i18n.I18NFacade;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.commercefacades.user.data.TitleData;
import de.hybris.platform.commercefacades.user.exceptions.PasswordMismatchException;
import de.hybris.platform.commerceservices.address.AddressVerificationDecision;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.greenlee.core.enums.UserTypes;
import com.greenlee.core.model.GreenleeB2BCustomerModel;
import com.greenlee.facades.customer.GreenleeCustomerFacade;
import com.greenlee.facades.customer.GreenleeUserFacade;
import com.greenlee.orderhistory.data.PIHeaderDetails;
import com.greenlee.orderhistory.data.PIOrderItem;
import com.greenlee.pi.exception.PIException;
import com.greenlee.pi.service.GreenleePIOrderHeaderService;
import com.greenlee.pi.service.GreenleePIOrderItemService;
import com.greenlee.storefront.controllers.ControllerConstants;
import com.greenlee.storefront.forms.GreenleeAddressForm;
import com.greenlee.storefront.forms.UpdateProfileForm;
import com.greenlee.storefront.forms.validation.ProfileValidator;


/**
 * Controller for home page
 */
@Controller
@Scope("tenant")
@RequestMapping("/my-account")
public class AccountPageController extends AbstractSearchPageController
{
	private static final String TEXT_ACCOUNT_ADDRESS_BOOK = "text.account.addressBook";
	private static final String TEXT_ACCOUNT_PROFILE = "text.account.profile";
	private static final String TEXT_ORDER_INVOICE_URL = "orderinvoiceurl";
	private static final String TEXT_ACCOUNT_ADDRESS_BOOK_ADD_EDIT_ADDRESS = "text.account.addressBook.addEditAddress";

	private static final String PROFILE_CURRENT_PASSWORD_INVALID = "profile.currentPassword.invalid";
	private static final String IS_DEFAULT_ADDRESS = "isDefaultAddress";
	private static final String ADDRESS_BOOK_EMPTY = "addressBookEmpty";
	private static final String TITLE_DATA = "titleData";
	private static final String FORM_GLOBAL_ERROR = "form.global.error";
	private static final String CUSTOMER_DATA = "customerData";
	private static final String SHOW_INVOICE = "showInvoice";
	private static final String USERTYPE = "userType";
	private static final String BREADCRUMBS = "breadcrumbs";
	private static final String ADDRESS_DATA = "addressData";
	private static final String COMPANY_NAME = "companyName";
	private static final String ADDRESS_FORM = "addressForm";
	private static final String COUNTRY = "country";
	private static final String REGIONS = "regions";
	// Internal Redirects
	private static final String REDIRECT_TO_ADDRESS_BOOK_PAGE = REDIRECT_PREFIX + "/my-account/address-book";
	private static final String REDIRECT_TO_PAYMENT_INFO_PAGE = REDIRECT_PREFIX + "/my-account/payment-details";
	private static final String REDIRECT_TO_UPDATE_EMAIL_PAGE = REDIRECT_PREFIX + "/my-account/update-email";
	private static final String REDIRECT_TO_MY_ACCOUNT = REDIRECT_PREFIX + "/my-account";
	private static final String REDIRECT_TO_ORDER_HISTORY_PAGE = REDIRECT_PREFIX + "/my-account/orders";

	private static final String BILLINGADDRESS = "billingAddress";
	private static final String SHIPPINGADDRESS = "shippingAddress";
	private static final String DEFAULTSHIPPINGADDRESS = "defaultShippingAddress";
	private static final String PRIMARYADDRESS = "primaryAddress";
	private static final String DEFAULTBILLINGADDRESS = "defaultBillingAddress";

	/**
	 * We use this suffix pattern because of an issue with Spring 3.1 where a Uri value is incorrectly extracted if it
	 * contains on or more '.' characters. Please see https://jira.springsource.org/browse/SPR-6164 for a discussion on
	 * the issue and future resolution.
	 */
	private static final String ORDER_CODE_PATH_VARIABLE_PATTERN = "{orderCode:.*}";
	private static final String ADDRESS_CODE_PATH_VARIABLE_PATTERN = "{addressCode:.*}";

	// CMS Pages
	private static final String ACCOUNT_CMS_PAGE = "account";
	private static final String PROFILE_CMS_PAGE = "profile";
	private static final String UPDATE_PASSWORD_CMS_PAGE = "updatePassword";
	private static final String UPDATE_PROFILE_CMS_PAGE = "update-profile";
	private static final String UPDATE_EMAIL_CMS_PAGE = "update-email";
	private static final String ADDRESS_BOOK_CMS_PAGE = "address-book";
	private static final String ADD_EDIT_ADDRESS_CMS_PAGE = "add-edit-address";
	private static final String PAYMENT_DETAILS_CMS_PAGE = "payment-details";
	private static final String ORDER_HISTORY_CMS_PAGE = "orders";
	private static final String ORDER_DETAIL_CMS_PAGE = "order";
	private static final String REQUEST_FOR_REPAIR_CMS_PAGE = "accountRequestRepairPage";
	private static final String RETURNS_CMS_PAGE = "accountReturnsPage";
	private static final String DATE_FORMAT = "yyyyMMdd";
	private static final String RECENT_ORDER_HISTORY = "recent.order.history";
	private static final String ORDER_HISTORY = "order.history";
	private static final String ORDER_INVOICE_URL = "order.invoice.url";
	private static final String B2BUNIT_ERROR = "You currently have no orders";
	private static final String SAP_ORDER_IDS = "SAP_ORDER_IDS";
	private static final String DUMMY_UNIT_B2E = Config.getString("greenlee.account.dummy.distributor.b2e.uid", "100051");
	private static final String DUMMY_UNIT_B2C = Config.getString("greenlee.account.dummy.distributor.b2c.uid", "100050");
	private static final String B2B = "b2b";
	private static final String B2E = "b2e";
	private static final String ORDER_HISTORY_PAGEE = "history";
	private static final String ORDER_ERROR_MSG = "?errormsg=forbidden";
	private static final String ERROR_FORBIDDEN = "forbidden";
	private static final String IS_BILLING = "BILLING";
	private static final String IS_SHIPPING = "SHIPPING";

	private static final String PRIMARY_ADDRESS = "PRIMARY";
	private static final Logger LOG = Logger.getLogger(AccountPageController.class);

	@Resource(name = "orderFacade")
	private OrderFacade orderFacade;

	@Resource(name = "acceleratorCheckoutFacade")
	private CheckoutFacade checkoutFacade;

	@Resource(name = "userFacade")
	private UserFacade userFacade;

	@Resource(name = "greenleeCustomerFacade")
	private GreenleeCustomerFacade customerFacade;

	@Resource(name = "accountBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;

	@Resource(name = "passwordValidator")
	private PasswordValidator passwordValidator;

	@Resource(name = "greenleeAddressValidator")
	private AddressValidator addressValidator;

	@Resource(name = "greenleeProfileValidator")
	private ProfileValidator profileValidator;

	@Resource(name = "emailValidator")
	private EmailValidator emailValidator;

	@Resource(name = "i18NFacade")
	private I18NFacade i18NFacade;

	@Resource(name = "addressVerificationFacade")
	private AddressVerificationFacade addressVerificationFacade;

	@Resource(name = "addressVerificationResultHandler")
	private AddressVerificationResultHandler addressVerificationResultHandler;

	@Resource(name = "greenleePIOrderHeaderService")
	private GreenleePIOrderHeaderService greenleePIOrderHeaderService;

	@Resource(name = "greenleePIOrderItemService")
	private GreenleePIOrderItemService greenleePIOrderItemService;

	@Autowired
	private UserService userService;

	@Resource(name = "companyB2BCommerceService")
	private CompanyB2BCommerceService companyB2BCommerceService;

	@Resource(name = "storefrontMessageSource")
	private MessageSource messageSource;

	@Resource(name = "i18nService")
	private I18NService i18nService;

	@Resource(name = "greenleeUserFacade")
	private GreenleeUserFacade greenleeUserFacade;

	protected PasswordValidator getPasswordValidator()
	{
		return passwordValidator;
	}

	protected AddressValidator getAddressValidator()
	{
		return addressValidator;
	}

	protected ProfileValidator getProfileValidator()
	{
		return profileValidator;
	}

	protected EmailValidator getEmailValidator()
	{
		return emailValidator;
	}

	protected I18NFacade getI18NFacade()
	{
		return i18NFacade;
	}

	protected AddressVerificationFacade getAddressVerificationFacade()
	{
		return addressVerificationFacade;
	}

	protected AddressVerificationResultHandler getAddressVerificationResultHandler()
	{
		return addressVerificationResultHandler;
	}

	@ModelAttribute("countries")
	public Collection<CountryData> getCountries()
	{
		return checkoutFacade.getDeliveryCountries();
	}

	@ModelAttribute("titles")
	public Collection<TitleData> getTitles()
	{
		return userFacade.getTitles();
	}

	@ModelAttribute("countryDataMap")
	public Map<String, CountryData> getCountryDataMap()
	{
		final Map<String, CountryData> countryDataMap = new HashMap<>();
		for (final CountryData countryData : getCountries())
		{
			countryDataMap.put(countryData.getIsocode(), countryData);
		}
		return countryDataMap;
	}


	@RequestMapping(value = "/addressform", method = RequestMethod.GET)
	public String getCountryAddressForm(@RequestParam("addressCode") final String addressCode,
			@RequestParam(value = "countryIsoCode", required = false) final String countryIsoCode, final Model model,
			final GreenleeAddressForm addressForm)
	{
		model.addAttribute("supportedCountries", getCountries());
		if (StringUtils.isNotBlank(countryIsoCode))
		{
			addressForm.setCountryIso(countryIsoCode);
			model.addAttribute(REGIONS, getI18NFacade().getRegionsForCountryIso(countryIsoCode));
			model.addAttribute(COUNTRY, countryIsoCode);
			model.addAttribute("edit", Boolean.TRUE);
		}


		model.addAttribute(ADDRESS_FORM, addressForm);
		for (final AddressData addressData : userFacade.getAddressBook())
		{
			if (addressData.getId() != null && addressData.getId().equals(addressCode)
					&& addressData.getCountry().getIsocode().equals(countryIsoCode))
			{
				model.addAttribute(ADDRESS_DATA, addressData);
				addressForm.setAddressId(addressData.getId());
				addressForm.setTitleCode(addressData.getTitleCode());
				addressForm.setFirstName(addressData.getFirstName());
				addressForm.setLastName(addressData.getLastName());
				addressForm.setLine1(addressData.getLine1());
				addressForm.setLine2(addressData.getLine2());
				addressForm.setTownCity(addressData.getTown());
				addressForm.setPostcode(addressData.getPostalCode());
				addressForm.setCountryIso(addressData.getCountry().getIsocode());
				addressForm.setPhone(addressData.getPhone());

				if (addressData.getRegion() != null && !StringUtils.isEmpty(addressData.getRegion().getIsocode()))
				{
					addressForm.setRegionIso(addressData.getRegion().getIsocode());
				}
				else
				{
					addressForm.setDistrict(addressData.getDistrict());
				}

				break;
			}
		}
		return ControllerConstants.Views.Fragments.Account.CountryAddressForm;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String account(@RequestParam(value = "errormsg", required = false) final String errormsg, final Model model,
			final RedirectAttributes redirectModel, final HttpSession session) throws CMSItemNotFoundException
	{
		recentorders(model, session);
		storeCmsPageInModel(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
		model.addAttribute(BREADCRUMBS, accountBreadcrumbBuilder.getBreadcrumbs(null));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		model.addAttribute("myAccountLink", "active");
		if (errormsg != null && errormsg.equals(ERROR_FORBIDDEN))
		{
			GlobalMessages.addErrorMessage(model, "account.orderhistory.forbidden");
		}
		return getViewForPage(model);
	}

	@RequestMapping(value = "/orders", method = RequestMethod.GET)
	@RequireHardLogIn
	public String orders(@RequestParam(value = "errormsg", required = false) final String errormsg, final Model model,
			final HttpSession session) throws CMSItemNotFoundException
	{
		final SearchPageData<PIHeaderDetails> searchPageData = new SearchPageData<>();
		final int fromDate = Integer.parseInt(Config.getParameter(ORDER_HISTORY));
		final CustomerData customerData = getCustomerDataForOrderHistory(fromDate);
		try
		{
			LOG.error("Order Listing for customer " + customerData.getSapConsumerID());
			if (customerData.getSapConsumerID() != null)
			{
				final List<PIHeaderDetails> responseList = greenleePIOrderHeaderService.getPIOrderHeaderDetails(customerData);
				cacheSapOrderId(responseList, session);
				LOG.error("Order Listing for customer >> responseList.size" + responseList.size());
				searchPageData.setResults(responseList);
			}
		}
		catch (final PIException exp)
		{
			LOG.error("Order Listing for customer >> final PIException exp");
			GlobalMessages.addErrorMessage(model,
					"ERR_NTFY_SUPPORT_0008 - Order Listing failure for customer " + customerData.getSapConsumerID());
			LOG.error("ERR_NTFY_SUPPORT_0008 - Order Listing failure for customer " + customerData.getSapConsumerID());
			LOG.error(exp);
		}
		final B2BUnitData b2bUnit = getUser().getSessionB2BUnit();
		model.addAttribute(SHOW_INVOICE, Boolean.FALSE);
		if (b2bUnit != null && b2bUnit.getUserType() != null)
		{
			if (B2B.equalsIgnoreCase(b2bUnit.getUserType()) || B2E.equalsIgnoreCase(b2bUnit.getUserType()))
			{
				model.addAttribute(SHOW_INVOICE, Boolean.TRUE);
			}
		}
		final String invoice = Config.getParameter(ORDER_INVOICE_URL);
		model.addAttribute("searchPageData", searchPageData);
		storeCmsPageInModel(model, getContentPageForLabelOrId(ORDER_HISTORY_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORDER_HISTORY_CMS_PAGE));
		model.addAttribute(BREADCRUMBS, accountBreadcrumbBuilder.getBreadcrumbs("text.account.yourorder"));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		model.addAttribute(TEXT_ORDER_INVOICE_URL, invoice);
		model.addAttribute("customerData", customerData);
		if (errormsg != null && errormsg.equals(ERROR_FORBIDDEN))
		{
			GlobalMessages.addErrorMessage(model, "account.orderhistory.forbidden");
		}
		return getViewForPage(model);
	}

	protected void recentorders(final Model model, final HttpSession session) throws CMSItemNotFoundException
	{
		final SearchPageData<PIHeaderDetails> searchPageData = new SearchPageData<>();
		final int fromDate = Integer.parseInt(Config.getParameter(RECENT_ORDER_HISTORY));
		List<PIHeaderDetails> responseList = new ArrayList();
		final CustomerData customerData = getCustomerDataForOrderHistory(fromDate);
		try
		{
			if (customerData.getSapConsumerID() != null)
			{
				responseList = greenleePIOrderHeaderService.getPIOrderHeaderDetails(customerData);
				cacheSapOrderId(responseList, session);
				searchPageData.setResults(responseList);
			}
		}
		catch (final PIException exp)
		{
			final String error = messageSource.getMessage("err.nifty.support.0008", new Object[]
			{ customerData.getSapConsumerID() }, "ERR_NTFY_SUPPORT_0008 - Order Listing  failure for customer {0} ",
					i18nService.getCurrentLocale());
			GlobalMessages.addErrorMessage(model, error);
			LOG.error(error);
			LOG.error(exp);
		}
		final String invoice = Config.getParameter(ORDER_INVOICE_URL);
		model.addAttribute("searchPageData", searchPageData);
		model.addAttribute("customerData", customerData);
		model.addAttribute(TEXT_ORDER_INVOICE_URL, invoice);
	}

	@RequestMapping(value = "/order/" + ORDER_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	@RequireHardLogIn
	public String order(@PathVariable("orderCode") final String orderCode, final Model model,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		try
		{
			final OrderData orderDetails = orderFacade.getOrderDetailsForCode(orderCode);
			model.addAttribute("orderData", orderDetails);

			final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
			breadcrumbs.add(new Breadcrumb("/my-account/orders", getMessageSource().getMessage("text.account.orderHistory", null,
					getI18nService().getCurrentLocale()), null));
			breadcrumbs.add(new Breadcrumb("#", getMessageSource().getMessage("text.account.order.orderBreadcrumb", new Object[]
			{ orderDetails.getCode() }, "Order {0}", getI18nService().getCurrentLocale()), null));
			model.addAttribute(BREADCRUMBS, breadcrumbs);

		}
		catch (final UnknownIdentifierException e)
		{
			LOG.warn("Attempted to load a order that does not exist or is not visible", e);
			GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER, "system.error.page.not.found", null);
			return REDIRECT_TO_ORDER_HISTORY_PAGE;
		}
		storeCmsPageInModel(model, getContentPageForLabelOrId(ORDER_DETAIL_CMS_PAGE));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORDER_DETAIL_CMS_PAGE));
		return getViewForPage(model);
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	@RequireHardLogIn
	public String profile(final Model model) throws CMSItemNotFoundException
	{
		final List<TitleData> titles = userFacade.getTitles();

		final CustomerData customerData = customerFacade.getCurrentCustomer();
		if (customerData.getTitleCode() != null)
		{
			model.addAttribute("title", findTitleForCode(titles, customerData.getTitleCode()));
		}

		model.addAttribute(CUSTOMER_DATA, customerData);

		storeCmsPageInModel(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
		model.addAttribute(BREADCRUMBS, accountBreadcrumbBuilder.getBreadcrumbs(TEXT_ACCOUNT_PROFILE));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return getViewForPage(model);
	}

	protected TitleData findTitleForCode(final List<TitleData> titles, final String code)
	{
		if (code != null && !code.isEmpty() && titles != null && !titles.isEmpty())
		{
			for (final TitleData title : titles)
			{
				if (code.equals(title.getCode()))
				{
					return title;
				}
			}
		}
		return null;
	}

	@RequestMapping(value = "/update-email", method = RequestMethod.GET)
	@RequireHardLogIn
	public String editEmail(final Model model) throws CMSItemNotFoundException
	{
		final CustomerData customerData = customerFacade.getCurrentCustomer();
		final UpdateEmailForm updateEmailForm = new UpdateEmailForm();

		updateEmailForm.setEmail(customerData.getDisplayUid());

		model.addAttribute("updateEmailForm", updateEmailForm);

		storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_EMAIL_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_EMAIL_CMS_PAGE));
		model.addAttribute(BREADCRUMBS, accountBreadcrumbBuilder.getBreadcrumbs(TEXT_ACCOUNT_PROFILE));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return getViewForPage(model);
	}

	@RequestMapping(value = "/update-email", method = RequestMethod.POST)
	@RequireHardLogIn
	public String updateEmail(final UpdateEmailForm updateEmailForm, final BindingResult bindingResult, final Model model,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		getEmailValidator().validate(updateEmailForm, bindingResult);
		String returnAction = REDIRECT_TO_UPDATE_EMAIL_PAGE;

		if (!bindingResult.hasErrors() && !updateEmailForm.getEmail().equals(updateEmailForm.getChkEmail()))
		{
			bindingResult.rejectValue("chkEmail", "validation.checkEmail.equals", new Object[] {}, "validation.checkEmail.equals");
		}

		if (bindingResult.hasErrors())
		{
			returnAction = setErrorMessagesAndCMSPage(model, UPDATE_EMAIL_CMS_PAGE);
		}
		else
		{
			try
			{
				customerFacade.changeUid(updateEmailForm.getEmail(), updateEmailForm.getPassword());
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
						"text.account.profile.confirmationUpdated", null);

				// Replace the spring security authentication with the new UID
				final String newUid = customerFacade.getCurrentCustomer().getUid().toLowerCase();
				final Authentication oldAuthentication = SecurityContextHolder.getContext().getAuthentication();
				final UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(newUid, null,
						oldAuthentication.getAuthorities());
				newAuthentication.setDetails(oldAuthentication.getDetails());
				SecurityContextHolder.getContext().setAuthentication(newAuthentication);
			}
			catch (final DuplicateUidException e)
			{
				bindingResult.rejectValue("email", "profile.email.unique");
				returnAction = setErrorMessagesAndCMSPage(model, UPDATE_EMAIL_CMS_PAGE);
				LOG.error(e);
			}
			catch (final PasswordMismatchException passwordMismatchException)
			{
				bindingResult.rejectValue("password", PROFILE_CURRENT_PASSWORD_INVALID);
				returnAction = setErrorMessagesAndCMSPage(model, UPDATE_EMAIL_CMS_PAGE);
				LOG.error(passwordMismatchException);
			}
		}

		return returnAction;
	}

	protected String setErrorMessagesAndCMSPage(final Model model, final String cmsPageLabelOrId) throws CMSItemNotFoundException
	{
		GlobalMessages.addErrorMessage(model, FORM_GLOBAL_ERROR);
		storeCmsPageInModel(model, getContentPageForLabelOrId(cmsPageLabelOrId));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(cmsPageLabelOrId));
		model.addAttribute(BREADCRUMBS, accountBreadcrumbBuilder.getBreadcrumbs(TEXT_ACCOUNT_PROFILE));
		return getViewForPage(model);
	}


	@RequestMapping(value = "/update-profile", method = RequestMethod.GET)
	@RequireHardLogIn
	public String editProfile(final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute(TITLE_DATA, userFacade.getTitles());

		final CustomerData customerData = customerFacade.getCurrentCustomer();
		final UpdateProfileForm updateProfileForm = new UpdateProfileForm();

		updateProfileForm.setTitleCode(customerData.getTitleCode());
		updateProfileForm.setFirstName(customerData.getFirstName());
		updateProfileForm.setLastName(customerData.getLastName());
		updateProfileForm.setEmail(customerData.getEmail());

		model.addAttribute("updateProfileForm", updateProfileForm);

		storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_PROFILE_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_PROFILE_CMS_PAGE));

		model.addAttribute(BREADCRUMBS, accountBreadcrumbBuilder.getBreadcrumbs(TEXT_ACCOUNT_PROFILE));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return getViewForPage(model);
	}

	@RequestMapping(value = "/update-profile", method = RequestMethod.POST)
	@RequireHardLogIn
	public String updateProfile(final UpdateProfileForm updateProfileForm, final BindingResult bindingResult, final Model model,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		getProfileValidator().validate(updateProfileForm, bindingResult);

		String returnAction = REDIRECT_TO_MY_ACCOUNT + "/" + PROFILE_CMS_PAGE;
		final CustomerData currentCustomerData = customerFacade.getCurrentCustomer();
		final CustomerData customerData = new CustomerData();
		customerData.setTitleCode(updateProfileForm.getTitleCode());
		customerData.setFirstName(updateProfileForm.getFirstName());
		customerData.setLastName(updateProfileForm.getLastName());
		customerData.setUid(currentCustomerData.getUid());
		customerData.setDisplayUid(currentCustomerData.getDisplayUid());
		customerData.setEmail(updateProfileForm.getEmail());

		model.addAttribute(TITLE_DATA, userFacade.getTitles());

		storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_PROFILE_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_PROFILE_CMS_PAGE));

		if (bindingResult.hasErrors())
		{
			returnAction = setErrorMessagesAndCMSPage(model, UPDATE_PROFILE_CMS_PAGE);
		}
		else
		{
			try
			{
				customerFacade.updateProfile(customerData);
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
						"text.account.profile.confirmationUpdated", null);

			}
			catch (final DuplicateUidException e)
			{
				bindingResult.rejectValue("email", "registration.error.account.exists.title");
				returnAction = setErrorMessagesAndCMSPage(model, UPDATE_PROFILE_CMS_PAGE);
				LOG.error(e);
			}
		}


		model.addAttribute(BREADCRUMBS, accountBreadcrumbBuilder.getBreadcrumbs(TEXT_ACCOUNT_PROFILE));
		return returnAction;
	}

	@RequestMapping(value = "/update-password", method = RequestMethod.GET)
	@RequireHardLogIn
	public String updatePassword(final Model model) throws CMSItemNotFoundException
	{
		final UpdatePasswordForm updatePasswordForm = new UpdatePasswordForm();

		model.addAttribute("updatePasswordForm", updatePasswordForm);

		storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_PASSWORD_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_PASSWORD_CMS_PAGE));

		model.addAttribute(BREADCRUMBS, accountBreadcrumbBuilder.getBreadcrumbs("text.account.profile.updatePasswordForm"));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return getViewForPage(model);
	}

	@RequestMapping(value = "/update-password", method = RequestMethod.POST)
	@RequireHardLogIn
	public String updatePassword(final UpdatePasswordForm updatePasswordForm, final BindingResult bindingResult,
			final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		getPasswordValidator().validate(updatePasswordForm, bindingResult);
		if (!bindingResult.hasErrors())
		{
			if (updatePasswordForm.getNewPassword().equals(updatePasswordForm.getCheckNewPassword()))
			{
				try
				{
					customerFacade.changePassword(updatePasswordForm.getCurrentPassword(), updatePasswordForm.getNewPassword());
				}
				catch (final PasswordMismatchException localException)
				{
					bindingResult.rejectValue("currentPassword", PROFILE_CURRENT_PASSWORD_INVALID, new Object[] {},
							PROFILE_CURRENT_PASSWORD_INVALID);
					LOG.error(localException);
				}
			}
			else
			{
				bindingResult.rejectValue("checkNewPassword", "validation.checkPwd.equals", new Object[] {},
						"validation.checkPwd.equals");
			}
		}

		if (bindingResult.hasErrors())
		{
			int errorCountBlankPage = 0;
			for (final ObjectError objectError : bindingResult.getAllErrors())
			{
				LOG.info(objectError.getCode() + "error code" + objectError.getDefaultMessage());
				if (("updatePwd.pwd.length.invalid").equals(objectError.getCode()))
				{
					errorCountBlankPage = errorCountBlankPage + 1;
				}
			}
			if (errorCountBlankPage == 0)
			{
				GlobalMessages.addErrorMessage(model, "form.global.blank.error");
			}
			else
			{
				GlobalMessages.addErrorMessage(model, FORM_GLOBAL_ERROR);
			}
			storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_PASSWORD_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_PASSWORD_CMS_PAGE));

			model.addAttribute(BREADCRUMBS, accountBreadcrumbBuilder.getBreadcrumbs("text.account.profile.updatePasswordForm"));
			return getViewForPage(model);
		}
		else
		{
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
					"text.account.confirmation.password.updated", null);
			return REDIRECT_TO_MY_ACCOUNT;
		}
	}

	@RequestMapping(value = "/address-book", method = RequestMethod.GET)
	@RequireHardLogIn
	public String getAddressBook(final Model model) throws CMSItemNotFoundException
	{
		final List<AddressData> enrichedAddressList = new ArrayList<AddressData>();
		final B2BUnitData b2bUnitData = getUser().getSessionB2BUnit();
		final List<AddressData> getAllAddressBookList = getAddressBooks();
		LOG.info("getAllAddressBookList" + getAllAddressBookList.size());
		for (final AddressData addressData : getAllAddressBookList)
		{
			if (addressData.getPrimaryAddress() != null && addressData.getDefaultBillingAddress() != null
					&& addressData.getDefaultShippingAddress() != null)
			{
				final boolean isPrimayAddress = addressData.getPrimaryAddress().booleanValue();
				final boolean isDefaultBillingAddress = addressData.getDefaultBillingAddress().booleanValue();
				final boolean isDefaultShippingAddress = addressData.getDefaultShippingAddress().booleanValue();
				final boolean isShippingAddress = addressData.isShippingAddress();
				final boolean isBillingAddress = addressData.isBillingAddress();
				final boolean ruleFlag = (isPrimayAddress || isDefaultBillingAddress || isDefaultShippingAddress || isShippingAddress || isBillingAddress);
				LOG.error("Address Data [" + addressData.getId() + "] ruleFlag [" + ruleFlag + "] isPrimayAddress ["
						+ isPrimayAddress + "] isDefaultBillingAddress [" + isDefaultBillingAddress + "] isDefaultShippingAddress ["
						+ isDefaultShippingAddress + "] isShippingAddress [" + isShippingAddress + "] isBillingAddress "
						+ isBillingAddress);
 
				if (isPrimayAddress)
				{
					LOG.info("Step 1");
					addressData.setLabelName(PRIMARYADDRESS);
					addressData.setShippingAddress(Boolean.FALSE.booleanValue());
					addressData.setBillingAddress(Boolean.FALSE.booleanValue());
					addressData.setPrimaryAddress(Boolean.TRUE); // Primary Address is the high priority to show on address book.
					addressData.setDefaultBillingAddress(Boolean.FALSE);
					addressData.setDefaultShippingAddress(Boolean.FALSE);
					enrichedAddressList.add(addressData);
				}
				if (isDefaultBillingAddress)
				{
					LOG.info("Step 2");
					addressData.setShippingAddress(Boolean.FALSE.booleanValue());
					addressData.setBillingAddress(Boolean.FALSE.booleanValue());
					addressData.setDefaultBillingAddress(Boolean.TRUE);
					if (isShippingAddress && isPrimayAddress)
					{
						addressData.setDefaultBillingAddress(Boolean.FALSE);
					}
					else
					{
						addressData.setDefaultBillingAddress(Boolean.TRUE);
						addressData.setLabelName(DEFAULTBILLINGADDRESS);
					}
					addressData.setDefaultShippingAddress(Boolean.FALSE);
					enrichedAddressList.add(addressData);
				}
				if (isDefaultShippingAddress)
				{
					LOG.info("Step 3");
					addressData.setShippingAddress(Boolean.FALSE.booleanValue());
					addressData.setBillingAddress(Boolean.FALSE.booleanValue());
					addressData.setDefaultBillingAddress(Boolean.FALSE);
					if (isShippingAddress && isPrimayAddress)
					{
						addressData.setDefaultShippingAddress(Boolean.FALSE);
					}
					else
					{
						addressData.setDefaultShippingAddress(Boolean.TRUE);
						addressData.setLabelName(DEFAULTSHIPPINGADDRESS);
					}
					enrichedAddressList.add(addressData);
				}
				if (isShippingAddress)
				{
					LOG.info("Step 4");
					if (isShippingAddress && isPrimayAddress)
					{
						addressData.setShippingAddress(Boolean.FALSE.booleanValue());
					}
					else
					{
						addressData.setShippingAddress(Boolean.TRUE.booleanValue());
						addressData.setLabelName(SHIPPINGADDRESS);
					}
					addressData.setBillingAddress(Boolean.FALSE.booleanValue());
					addressData.setDefaultBillingAddress(Boolean.FALSE);
					addressData.setDefaultShippingAddress(Boolean.FALSE);
					enrichedAddressList.add(addressData);
				}
				if (isBillingAddress)
				{
					LOG.info("Step 5");
					addressData.setShippingAddress(Boolean.FALSE.booleanValue());
					if (isBillingAddress && isPrimayAddress)
					{
						addressData.setBillingAddress(Boolean.FALSE.booleanValue());
					}
					else
					{
						addressData.setBillingAddress(Boolean.TRUE.booleanValue());
						addressData.setLabelName(BILLINGADDRESS);
					}
					addressData.setDefaultBillingAddress(Boolean.FALSE);
					addressData.setDefaultShippingAddress(Boolean.FALSE);
					enrichedAddressList.add(addressData);
				}
			}
		}

		model.addAttribute(ADDRESS_DATA, enrichedAddressList);

		if (b2bUnitData != null && b2bUnitData.getUserType() != null && UserTypes.B2E.getCode().equals(b2bUnitData.getUserType()))
		{
			model.addAttribute(COMPANY_NAME, ((GreenleeB2BCustomerModel) userService.getCurrentUser()).getCompanyName());
		}

		storeCmsPageInModel(model, getContentPageForLabelOrId(ADDRESS_BOOK_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADDRESS_BOOK_CMS_PAGE));
		model.addAttribute(BREADCRUMBS, accountBreadcrumbBuilder.getBreadcrumbs(TEXT_ACCOUNT_ADDRESS_BOOK));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return getViewForPage(model);
	}

	private List<AddressData> getAddressBooks()
	{
		final List<AddressData> addressBookList = new ArrayList<AddressData>();
		// Primary user address.
		if (getUser().getSessionB2BUnit() != null)
		{
			final List<AddressData> primaryAddresses = getUser().getSessionB2BUnit().getAddresses();
			if (primaryAddresses != null && !primaryAddresses.isEmpty())
			{
				LOG.info("Primary Address " + primaryAddresses.size());
				addressBookList.addAll(primaryAddresses);
			}
		}

		//		final List<AddressData> addressBooks = userFacade.getAddressBook();
		final List<AddressData> addressBooks = greenleeUserFacade.getAddressBookEntries();
		LOG.info("Total getAddressBookEntries [ " + addressBookList.size() + "]");
		if (addressBooks != null && !addressBooks.isEmpty())
		{
			LOG.info("Address Books " + addressBooks.size());
			addressBookList.addAll(addressBooks);

		}
		LOG.info("Total Address in the Book List [ " + addressBookList.size() + "]");
		return addressBookList;
	}

	@RequestMapping(value = "/add-address", method = RequestMethod.GET)
	@RequireHardLogIn
	public String addAddress(final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute("countryData", checkoutFacade.getDeliveryCountries());
		model.addAttribute(TITLE_DATA, userFacade.getTitles());
		model.addAttribute(ADDRESS_FORM, new AddressForm());
		model.addAttribute(ADDRESS_BOOK_EMPTY, Boolean.valueOf(userFacade.isAddressBookEmpty()));
		model.addAttribute(IS_DEFAULT_ADDRESS, Boolean.FALSE);
		storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));

		final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
		breadcrumbs.add(new Breadcrumb("/my-account/address-book", getMessageSource().getMessage(TEXT_ACCOUNT_ADDRESS_BOOK, null,
				getI18nService().getCurrentLocale()), null));
		breadcrumbs.add(new Breadcrumb("#", getMessageSource().getMessage(TEXT_ACCOUNT_ADDRESS_BOOK_ADD_EDIT_ADDRESS, null,
				getI18nService().getCurrentLocale()), null));
		model.addAttribute(BREADCRUMBS, breadcrumbs);
		model.addAttribute("addAddress", Boolean.TRUE);
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);

		return getViewForPage(model);
	}

	protected AddressForm getPreparedAddressForm()
	{
		final CustomerData currentCustomerData = customerFacade.getCurrentCustomer();
		final AddressForm addressForm = new AddressForm();
		addressForm.setFirstName(currentCustomerData.getFirstName());
		addressForm.setLastName(currentCustomerData.getLastName());
		addressForm.setTitleCode(currentCustomerData.getTitleCode());
		return addressForm;
	}

	@RequestMapping(value = "/add-address", method = RequestMethod.POST)
	@RequireHardLogIn
	public String addAddress(final AddressForm addressForm, final BindingResult bindingResult, final Model model,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
		breadcrumbs.add(new Breadcrumb("/my-account/address-book", getMessageSource().getMessage(TEXT_ACCOUNT_ADDRESS_BOOK, null,
				getI18nService().getCurrentLocale()), null));
		breadcrumbs.add(new Breadcrumb("#", getMessageSource().getMessage(TEXT_ACCOUNT_ADDRESS_BOOK_ADD_EDIT_ADDRESS, null,
				getI18nService().getCurrentLocale()), null));
		model.addAttribute(BREADCRUMBS, breadcrumbs);

		getAddressValidator().validate(addressForm, bindingResult);
		if (bindingResult.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, FORM_GLOBAL_ERROR);
			storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
			setUpAddressFormAfterError(addressForm, model);
			model.addAttribute("addAddress", Boolean.TRUE);
			return getViewForPage(model);
		}

		final AddressData newAddress = new AddressData();
		newAddress.setTitleCode(addressForm.getTitleCode());
		newAddress.setFirstName(addressForm.getFirstName());
		newAddress.setLastName(addressForm.getLastName());
		newAddress.setLine1(addressForm.getLine1());
		newAddress.setLine2(addressForm.getLine2());
		newAddress.setTown(addressForm.getTownCity());
		newAddress.setPostalCode(addressForm.getPostcode());
		newAddress.setBillingAddress(false);
		newAddress.setShippingAddress(true);
		newAddress.setVisibleInAddressBook(true);
		newAddress.setCountry(getI18NFacade().getCountryForIsocode(addressForm.getCountryIso()));
		newAddress.setPhone(addressForm.getPhone());

		if (addressForm.getRegionIso() != null && !StringUtils.isEmpty(addressForm.getRegionIso()))
		{
			newAddress.setRegion(getI18NFacade().getRegion(addressForm.getCountryIso(), addressForm.getRegionIso()));
		}

		if (userFacade.isAddressBookEmpty())
		{
			newAddress.setDefaultAddress(true);
			newAddress.setVisibleInAddressBook(true);
		}
		else
		{
			newAddress.setDefaultAddress(addressForm.getDefaultAddress() != null && addressForm.getDefaultAddress().booleanValue());
		}

		final AddressVerificationResult<AddressVerificationDecision> verificationResult = getAddressVerificationFacade()
				.verifyAddressData(newAddress);
		final boolean addressRequiresReview = getAddressVerificationResultHandler().handleResult(verificationResult, newAddress,
				model, redirectModel, bindingResult, getAddressVerificationFacade().isCustomerAllowedToIgnoreAddressSuggestions(),
				"checkout.multi.address.added");

		model.addAttribute(REGIONS, getI18NFacade().getRegionsForCountryIso(addressForm.getCountryIso()));
		model.addAttribute(COUNTRY, addressForm.getCountryIso());
		model.addAttribute("edit", Boolean.TRUE);
		model.addAttribute(IS_DEFAULT_ADDRESS, Boolean.valueOf(isDefaultAddress(addressForm.getAddressId())));

		if (addressRequiresReview)
		{
			storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
			return getViewForPage(model);
		}

		greenleeUserFacade.addAddress(newAddress);


		GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER, "account.confirmation.address.added",
				null);

		return REDIRECT_TO_ADDRESS_BOOK_PAGE;
	}

	protected void setUpAddressFormAfterError(final AddressForm addressForm, final Model model)
	{
		model.addAttribute("countryData", checkoutFacade.getDeliveryCountries());
		model.addAttribute(TITLE_DATA, userFacade.getTitles());
		model.addAttribute(ADDRESS_BOOK_EMPTY, Boolean.valueOf(userFacade.isAddressBookEmpty()));
		model.addAttribute(IS_DEFAULT_ADDRESS, Boolean.valueOf(isDefaultAddress(addressForm.getAddressId())));
		if (addressForm.getCountryIso() != null)
		{
			model.addAttribute(REGIONS, getI18NFacade().getRegionsForCountryIso(addressForm.getCountryIso()));
			model.addAttribute(COUNTRY, addressForm.getCountryIso());
		}
	}

	@RequestMapping(value = "/edit-address/" + ADDRESS_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	@RequireHardLogIn
	public String editAddress(@PathVariable("addressCode") final String addressCode, final Model model)
			throws CMSItemNotFoundException
	{
		final AddressForm addressForm = new AddressForm();
		model.addAttribute("countryData", checkoutFacade.getDeliveryCountries());
		model.addAttribute(TITLE_DATA, userFacade.getTitles());
		model.addAttribute(ADDRESS_FORM, addressForm);
		model.addAttribute(ADDRESS_BOOK_EMPTY, Boolean.valueOf(userFacade.isAddressBookEmpty()));

		for (final AddressData addressData : userFacade.getAddressBook())
		{
			if (addressData.getId() != null && addressData.getId().equals(addressCode))
			{
				model.addAttribute(REGIONS, getI18NFacade().getRegionsForCountryIso(addressData.getCountry().getIsocode()));
				model.addAttribute(COUNTRY, addressData.getCountry().getIsocode());
				model.addAttribute(ADDRESS_DATA, addressData);
				addressForm.setAddressId(addressData.getId());
				addressForm.setTitleCode(addressData.getTitleCode());
				addressForm.setFirstName(addressData.getFirstName());
				addressForm.setLastName(addressData.getLastName());
				addressForm.setLine1(addressData.getLine1());
				addressForm.setLine2(addressData.getLine2());
				addressForm.setTownCity(addressData.getTown());
				addressForm.setPostcode(addressData.getPostalCode());
				addressForm.setCountryIso(addressData.getCountry().getIsocode());
				addressForm.setPhone(addressData.getPhone());

				if (addressData.getRegion() != null && !StringUtils.isEmpty(addressData.getRegion().getIsocode()))
				{
					addressForm.setRegionIso(addressData.getRegion().getIsocode());
				}

				if (isDefaultAddress(addressData.getId()))
				{
					addressForm.setDefaultAddress(Boolean.TRUE);
					model.addAttribute(IS_DEFAULT_ADDRESS, Boolean.TRUE);
				}
				else
				{
					addressForm.setDefaultAddress(Boolean.FALSE);
					model.addAttribute(IS_DEFAULT_ADDRESS, Boolean.FALSE);
				}
				break;
			}
		}

		storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));

		final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
		breadcrumbs.add(new Breadcrumb("/my-account/address-book", getMessageSource().getMessage(TEXT_ACCOUNT_ADDRESS_BOOK, null,
				getI18nService().getCurrentLocale()), null));
		breadcrumbs.add(new Breadcrumb("#", getMessageSource().getMessage(TEXT_ACCOUNT_ADDRESS_BOOK_ADD_EDIT_ADDRESS, null,
				getI18nService().getCurrentLocale()), null));
		model.addAttribute(BREADCRUMBS, breadcrumbs);
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		model.addAttribute("edit", Boolean.TRUE);
		return getViewForPage(model);
	}

	/**
	 * Method checks if address is set as default
	 *
	 * @param addressId
	 *           - identifier for address to check
	 * @return true if address is default, false if address is not default
	 */
	protected boolean isDefaultAddress(final String addressId)
	{
		final AddressData defaultAddress = userFacade.getDefaultAddress();
		return defaultAddress != null && defaultAddress.getId() != null && defaultAddress.getId().equals(addressId);
	}

	@RequestMapping(value = "/edit-address/" + ADDRESS_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.POST)
	@RequireHardLogIn
	public String editAddress(final AddressForm addressForm, final BindingResult bindingResult, final Model model,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		getAddressValidator().validate(addressForm, bindingResult);
		if (bindingResult.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, FORM_GLOBAL_ERROR);
			storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
			setUpAddressFormAfterError(addressForm, model);
			return getViewForPage(model);
		}

		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);

		final AddressData newAddress = new AddressData();
		newAddress.setId(addressForm.getAddressId());
		newAddress.setTitleCode(addressForm.getTitleCode());
		newAddress.setFirstName(addressForm.getFirstName());
		newAddress.setLastName(addressForm.getLastName());
		newAddress.setLine1(addressForm.getLine1());
		newAddress.setLine2(addressForm.getLine2());
		newAddress.setTown(addressForm.getTownCity());
		newAddress.setPostalCode(addressForm.getPostcode());
		newAddress.setBillingAddress(false);
		newAddress.setShippingAddress(true);
		newAddress.setVisibleInAddressBook(true);
		newAddress.setCountry(getI18NFacade().getCountryForIsocode(addressForm.getCountryIso()));
		newAddress.setPhone(addressForm.getPhone());

		if (addressForm.getRegionIso() != null && !StringUtils.isEmpty(addressForm.getRegionIso()))
		{
			newAddress.setRegion(getI18NFacade().getRegion(addressForm.getCountryIso(), addressForm.getRegionIso()));
		}

		if (Boolean.TRUE.equals(addressForm.getDefaultAddress()) || userFacade.getAddressBook().size() <= 1)
		{
			newAddress.setDefaultAddress(true);
			newAddress.setVisibleInAddressBook(true);
		}

		final AddressVerificationResult<AddressVerificationDecision> verificationResult = getAddressVerificationFacade()
				.verifyAddressData(newAddress);
		final boolean addressRequiresReview = getAddressVerificationResultHandler().handleResult(verificationResult, newAddress,
				model, redirectModel, bindingResult, getAddressVerificationFacade().isCustomerAllowedToIgnoreAddressSuggestions(),
				"checkout.multi.address.updated");

		model.addAttribute(REGIONS, getI18NFacade().getRegionsForCountryIso(addressForm.getCountryIso()));
		model.addAttribute(COUNTRY, addressForm.getCountryIso());
		model.addAttribute("edit", Boolean.TRUE);
		model.addAttribute(IS_DEFAULT_ADDRESS, Boolean.valueOf(isDefaultAddress(addressForm.getAddressId())));

		if (addressRequiresReview)
		{
			storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
			return getViewForPage(model);
		}

		userFacade.editAddress(newAddress);

		GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER, "account.confirmation.address.updated",
				null);
		return REDIRECT_TO_ADDRESS_BOOK_PAGE;
	}

	@RequestMapping(value = "/select-suggested-address", method = RequestMethod.POST)
	public String doSelectSuggestedAddress(final AddressForm addressForm, final RedirectAttributes redirectModel)
	{
		final Set<String> resolveCountryRegions = org.springframework.util.StringUtils.commaDelimitedListToSet(Config
				.getParameter("resolve.country.regions"));

		final AddressData selectedAddress = new AddressData();
		selectedAddress.setId(addressForm.getAddressId());
		selectedAddress.setTitleCode(addressForm.getTitleCode());
		selectedAddress.setFirstName(addressForm.getFirstName());
		selectedAddress.setLastName(addressForm.getLastName());
		selectedAddress.setLine1(addressForm.getLine1());
		selectedAddress.setLine2(addressForm.getLine2());
		selectedAddress.setTown(addressForm.getTownCity());
		selectedAddress.setPostalCode(addressForm.getPostcode());
		selectedAddress.setBillingAddress(false);
		selectedAddress.setShippingAddress(true);
		selectedAddress.setVisibleInAddressBook(true);
		selectedAddress.setPhone(addressForm.getPhone());

		final CountryData countryData = i18NFacade.getCountryForIsocode(addressForm.getCountryIso());
		selectedAddress.setCountry(countryData);

		if (resolveCountryRegions.contains(countryData.getIsocode()) && addressForm.getRegionIso() != null
				&& !StringUtils.isEmpty(addressForm.getRegionIso()))
		{
			final RegionData regionData = getI18NFacade().getRegion(addressForm.getCountryIso(), addressForm.getRegionIso());
			selectedAddress.setRegion(regionData);
		}

		if (resolveCountryRegions.contains(countryData.getIsocode()) && addressForm.getRegionIso() != null
				&& !StringUtils.isEmpty(addressForm.getRegionIso()))
		{
			final RegionData regionData = getI18NFacade().getRegion(addressForm.getCountryIso(), addressForm.getRegionIso());
			selectedAddress.setRegion(regionData);
		}

		if (Boolean.TRUE.equals(addressForm.getEditAddress()))
		{
			selectedAddress.setDefaultAddress(Boolean.TRUE.equals(addressForm.getDefaultAddress())
					|| userFacade.getAddressBook().size() <= 1);
			userFacade.editAddress(selectedAddress);
		}
		else
		{
			selectedAddress.setDefaultAddress(Boolean.TRUE.equals(addressForm.getDefaultAddress())
					|| userFacade.isAddressBookEmpty());
			userFacade.addAddress(selectedAddress);
		}

		GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER, "account.confirmation.address.added");

		return REDIRECT_TO_ADDRESS_BOOK_PAGE;
	}

	@RequestMapping(value = "/remove-address/" + ADDRESS_CODE_PATH_VARIABLE_PATTERN, method =
	{ RequestMethod.GET, RequestMethod.POST })
	@RequireHardLogIn
	public String removeAddress(@PathVariable("addressCode") final String addressCode, final RedirectAttributes redirectModel)
	{
		final AddressData addressData = new AddressData();
		addressData.setId(addressCode);
		userFacade.removeAddress(addressData);

		GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER, "account.confirmation.address.removed");
		return REDIRECT_TO_ADDRESS_BOOK_PAGE;
	}

	@RequestMapping(value = "/set-default-address/" + ADDRESS_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	@RequireHardLogIn
	public String setDefaultAddress(@PathVariable("addressCode") final String addressCode, final RedirectAttributes redirectModel)
	{
		final AddressData addressData = new AddressData();
		addressData.setDefaultAddress(true);
		addressData.setVisibleInAddressBook(true);
		addressData.setId(addressCode);
		userFacade.setDefaultAddress(addressData);
		GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
				"account.confirmation.default.address.changed");
		return REDIRECT_TO_ADDRESS_BOOK_PAGE;
	}

	@RequestMapping(value = "/payment-details", method = RequestMethod.GET)
	@RequireHardLogIn
	public String paymentDetails(final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute(CUSTOMER_DATA, customerFacade.getCurrentCustomer());
		model.addAttribute("paymentInfoData", userFacade.getCCPaymentInfos(true));
		storeCmsPageInModel(model, getContentPageForLabelOrId(PAYMENT_DETAILS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
		model.addAttribute(BREADCRUMBS, accountBreadcrumbBuilder.getBreadcrumbs("text.account.paymentDetails"));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return getViewForPage(model);
	}

	@RequestMapping(value = "/set-default-payment-details", method = RequestMethod.POST)
	@RequireHardLogIn
	public String setDefaultPaymentDetails(@RequestParam final String paymentInfoId)
	{
		CCPaymentInfoData paymentInfoData = null;
		if (StringUtils.isNotBlank(paymentInfoId))
		{
			paymentInfoData = userFacade.getCCPaymentInfoForCode(paymentInfoId);
		}
		userFacade.setDefaultPaymentInfo(paymentInfoData);
		return REDIRECT_TO_PAYMENT_INFO_PAGE;
	}

	@RequestMapping(value = "/remove-payment-method", method = RequestMethod.POST)
	@RequireHardLogIn
	public String removePaymentMethod(@RequestParam(value = "paymentInfoId") final String paymentMethodId,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		userFacade.unlinkCCPaymentInfo(paymentMethodId);
		GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
				"text.account.profile.paymentCart.removed");
		return REDIRECT_TO_PAYMENT_INFO_PAGE;
	}

	@RequestMapping(value = "/piOrderDetails", method = RequestMethod.GET)
	@RequireHardLogIn
	public String getOrderDetails(@RequestParam(value = "orderid", required = true) final String orderid,
			@RequestParam(value = "page", required = true) final String page, final Model model,
			final RedirectAttributes redirectModel, final HttpSession session) throws CMSItemNotFoundException
	{
		PIOrderItem orderItem = null;
		GreenleeB2BCustomerModel greenleeUser = null;
		final String customerId = getSapCustomerId(getUser());
		try
		{
			orderItem = new PIOrderItem();
			if (checkOrderIdInCache(orderid, session))
			{
				orderItem = greenleePIOrderItemService.getPIOrderDetails(orderid);
			}
			else
			{
				if (page != null && page.equals(ORDER_HISTORY_PAGEE))
				{
					LOG.error("REDIRECT_TO_ORDER_HISTORY_PAGE + ORDER_ERROR_MSG" + orderItem);
					return REDIRECT_TO_ORDER_HISTORY_PAGE + ORDER_ERROR_MSG;
				}
				else
				{
					LOG.error("REDIRECT_TO_MY_ACCOUNT + ORDER_ERROR_MSG " + orderItem);
					return REDIRECT_TO_MY_ACCOUNT + ORDER_ERROR_MSG;
				}
			}
		}
		catch (final PIException exp)
		{
			final String error = messageSource.getMessage("err.nifty.support.0009", new Object[]
			{ customerId, orderid }, "ERR_NTFY_SUPPORT_0009 - Order Details failure for customer {0} order number {1} ",
					i18nService.getCurrentLocale());
			GlobalMessages.addErrorMessage(model, error);
			LOG.error(error);
			LOG.error(exp);
		}
		model.addAttribute("orderData", orderItem);
		final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
		breadcrumbs.add(new Breadcrumb("/my-account/orders", getMessageSource().getMessage("text.account.yourorder", null,
				getI18nService().getCurrentLocale()), null));
		breadcrumbs.add(new Breadcrumb("#", getMessageSource().getMessage("text.account.order.orderdetails", null,
				getI18nService().getCurrentLocale()), null));
		final String invoice = Config.getParameter(ORDER_INVOICE_URL);
		final B2BUnitData b2bUnit = getUser().getSessionB2BUnit();
		model.addAttribute(SHOW_INVOICE, Boolean.FALSE);
		if (b2bUnit != null && b2bUnit.getUserType() != null)
		{
			if (B2B.equalsIgnoreCase(b2bUnit.getUserType()) || B2E.equalsIgnoreCase(b2bUnit.getUserType()))
			{
				model.addAttribute(SHOW_INVOICE, Boolean.TRUE);
			}
			final UserModel userModel = userService.getCurrentUser();

			if (userModel instanceof GreenleeB2BCustomerModel)
			{
				greenleeUser = (GreenleeB2BCustomerModel) userModel;
				model.addAttribute(COMPANY_NAME, greenleeUser.getCompanyName());
				model.addAttribute(USERTYPE, greenleeUser.getUserType());
			}
		}

		model.addAttribute(TEXT_ORDER_INVOICE_URL, invoice);
		model.addAttribute(BREADCRUMBS, breadcrumbs);
		storeCmsPageInModel(model, getContentPageForLabelOrId(ORDER_DETAIL_CMS_PAGE));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORDER_DETAIL_CMS_PAGE));
		return ControllerConstants.Views.Integration.ACCOUNTORDERDETILSPAGE;
	}

	@RequestMapping(value = "/request-repair", method = RequestMethod.GET)
	@RequireHardLogIn
	public String requestRepair(final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute(CUSTOMER_DATA, customerFacade.getCurrentCustomer());
		storeCmsPageInModel(model, getContentPageForLabelOrId(REQUEST_FOR_REPAIR_CMS_PAGE));
		model.addAttribute(BREADCRUMBS, accountBreadcrumbBuilder.getBreadcrumbs("text.account.orders.requestForRepair"));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return getViewForPage(model);
	}

	@RequestMapping(value = "/returns", method = RequestMethod.GET)
	@RequireHardLogIn
	public String returns(final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute(CUSTOMER_DATA, customerFacade.getCurrentCustomer());
		storeCmsPageInModel(model, getContentPageForLabelOrId(RETURNS_CMS_PAGE));

		model.addAttribute(BREADCRUMBS, accountBreadcrumbBuilder.getBreadcrumbs("text.account.orders.returns"));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return getViewForPage(model);
	}


	public CustomerData getCustomerDataForOrderHistory(final int startingMonth)
	{
		final CustomerData customerData = getUser();
		final Calendar prevYear = Calendar.getInstance();
		final Date date = Calendar.getInstance().getTime();
		final DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		getSapCustomerId(customerData);
		if (startingMonth <= 0)
		{
			prevYear.add(Calendar.MONTH, startingMonth);
			customerData.setSapFromDate(formatter.format(prevYear.getTime()));
		}
		else
		{
			customerData.setSapFromDate(String.valueOf(startingMonth));
		}

		customerData.setSapToDate(formatter.format(date));
		return customerData;
	}


	public String getSapCustomerId(final CustomerData customerData)
	{
		String sapId = null;
		if (customerData != null && customerData.getSessionB2BUnit() != null)
		{
			sapId = customerData.getSessionB2BUnit().getUid();
		}
		else if (customerData != null && customerData.getUnit() != null)
		{
			sapId = customerData.getUnit().getUid();
		}
		if (sapId != null)
		{
			final String[] b2bUnitKeyFields = sapId.split("_");
			customerData.setSapConsumerID((b2bUnitKeyFields.length == 4) ? b2bUnitKeyFields[0] : null);
			sapId = customerData.getSapConsumerID();
			customerData.setSalesOrganization((b2bUnitKeyFields.length == 4) ? b2bUnitKeyFields[1] : null);
			customerData.setDistributionChannel((b2bUnitKeyFields.length == 4) ? b2bUnitKeyFields[2] : null);
			customerData.setDivision((b2bUnitKeyFields.length == 4) ? b2bUnitKeyFields[3] : null);
		}
		if (sapId == null || sapId.equals(DUMMY_UNIT_B2C) || sapId.equals(DUMMY_UNIT_B2E))
		{
			LOG.error(B2BUNIT_ERROR);
		}
		return sapId;
	}

	public void cacheSapOrderId(final List<PIHeaderDetails> responseList, final HttpSession session)
	{
		final Set<String> orderIdList = new HashSet();
		if (responseList != null && !responseList.isEmpty())
		{
			for (final PIHeaderDetails order : responseList)
			{
				orderIdList.add(order.getSalesOrderNo());
			}
			session.setAttribute(SAP_ORDER_IDS, orderIdList);
		}
	}

	public boolean checkOrderIdInCache(final String orderId, final HttpSession session)
	{
		if (orderId != null && session.getAttribute(SAP_ORDER_IDS) != null)
		{
			final Set<String> orderIdlist = (Set<String>) session.getAttribute(SAP_ORDER_IDS);
			return orderIdlist.contains(orderId) ? true : false;
		}
		return false;
	}
}

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

import com.greenlee.storefront.security.exception.EmailNotVerifiedException;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractLoginPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.GuestForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.LoginForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.RegisterForm;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.i18n.I18NFacade;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.greenlee.core.checkout.services.RegionCountryService;
import com.greenlee.core.model.GreenleeB2BCustomerModel;
import com.greenlee.facades.customer.GreenleeCustomerAccountFacade;
import com.greenlee.facades.customer.GreenleeUserFacade;
import com.greenlee.facades.customer.data.B2CRegisterData;
import com.greenlee.storefront.controllers.ControllerConstants;
import com.greenlee.storefront.forms.B2BRegisterForm;
import com.greenlee.storefront.util.GreenleeGlobalMessages;


/**
 * Login Controller. Handles login and register for the account flow.
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/login")
public class LoginPageController extends AbstractLoginPageController
{
	/**
	 *
	 */
	private static final String GREENLEE_REGISTER_PAGE = "greenleeRegisterPage";
	private static final String B2C_CUSTOMER = "b2c";
	private static final String B2E_CUSTOMER = "b2e";
	protected static final String SPRING_SECURITY_LAST_EXCEPTION = "SPRING_SECURITY_LAST_EXCEPTION";
	private static final String HOME_PAGE = "homepage";
	public static final String THANK_MESSAGES_HOLDER = "accThankMsgs";
	private static final Logger LOG = Logger.getLogger(LoginPageController.class);

	private HttpSessionRequestCache httpSessionRequestCache;

	@Autowired
	private GreenleeCustomerAccountFacade glCustomerFacade;

	@Resource(name = "i18NFacade")
	private I18NFacade i18NFacade;

	@Resource(name = "userFacade")
	private UserFacade userFacade;

	@Autowired
	private CMSSiteService cmsSiteService;

	@Autowired
	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;

	@Resource(name = "acceleratorCheckoutFacade")
	private CheckoutFacade checkoutFacade;

	@Resource(name = "greenleeUserFacade")
	private GreenleeUserFacade greenleeUserFacade;

	@Resource
	CMSSiteService cmsService;

	@Resource(name = "greenLeeRegistrationValidator")
	private Validator greenLeeRegistrationValidator;

	@Resource(name = "registerBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder registerBreadcrumbBuilder;

	@Resource(name = "previewUrlResolverPageMappings")
	private Map<String, String> previewUrlResolverMap;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "greenleeRegionCountryService")
	private RegionCountryService regionCountryService;

	/**
	 * @return the greenleeUserFacade
	 */
	public GreenleeUserFacade getGreenleeUserFacade()
	{
		return greenleeUserFacade;
	}

	/**
	 * @param greenleeUserFacade
	 *           the greenleeUserFacade to set
	 */
	public void setGreenleeUserFacade(final GreenleeUserFacade greenleeUserFacade)
	{
		this.greenleeUserFacade = greenleeUserFacade;
	}

	/**
	 * @return the greenLeeRegistrationValidator
	 */
	public Validator getGreenLeeRegistrationValidator()
	{
		return greenLeeRegistrationValidator;
	}

	/**
	 * @param greenLeeRegistrationValidator
	 *           the greenLeeRegistrationValidator to set
	 */
	public void setGreenLeeRegistrationValidator(final Validator greenLeeRegistrationValidator)
	{
		this.greenLeeRegistrationValidator = greenLeeRegistrationValidator;
	}

	/**
	 * @return the userFacade
	 */
	@Override
	public UserFacade getUserFacade()
	{
		return userFacade;
	}

	/**
	 * @param userFacade
	 *           the userFacade to set
	 */
	public void setUserFacade(final UserFacade userFacade)
	{
		this.userFacade = userFacade;
	}

	/**
	 * @return the glCustomerFacade
	 */
	public GreenleeCustomerAccountFacade getGlCustomerFacade()
	{
		return glCustomerFacade;
	}

	/**
	 * @param glCustomerFacade
	 *           the glCustomerFacade to set
	 */
	public void setGlCustomerFacade(final GreenleeCustomerAccountFacade glCustomerFacade)
	{
		this.glCustomerFacade = glCustomerFacade;
	}

	/**
	 * @return the i18NFacade
	 */
	public I18NFacade getI18NFacade()
	{
		return i18NFacade;
	}

	/**
	 * @param i18nFacade
	 *           the i18NFacade to set
	 */
	public void setI18NFacade(final I18NFacade i18nFacade)
	{
		i18NFacade = i18nFacade;
	}

	@Override
	protected String getView()
	{
		return ControllerConstants.Views.Pages.Account.AccountLoginPage;
	}

	@Override
	protected String getSuccessRedirect(final HttpServletRequest request, final HttpServletResponse response)
	{
		if (httpSessionRequestCache.getRequest(request, response) != null)
		{
			return httpSessionRequestCache.getRequest(request, response).getRedirectUrl();
		}
		return "/";
	}

	@Override
	protected AbstractPageModel getCmsPage() throws CMSItemNotFoundException
	{
		return getContentPageForLabelOrId("login");
	}


	@Resource(name = "httpSessionRequestCache")
	public void setHttpSessionRequestCache(final HttpSessionRequestCache accHttpSessionRequestCache)
	{
		this.httpSessionRequestCache = accHttpSessionRequestCache;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String doLogin(@RequestHeader(value = "referer", required = false) final String referer,
			@RequestParam(value = "error", defaultValue = "false") final boolean loginError, final Model model,
			final HttpServletRequest request, final HttpServletResponse response, final HttpSession session)
			throws CMSItemNotFoundException
	{
		if (!loginError)
		{
			storeReferer(referer, request, response);
		}

		return getDefaultLoginPage(loginError, session, model);
	}

	@Override
	protected String getDefaultLoginPage(final boolean loginError, final HttpSession session, final Model model)
			throws CMSItemNotFoundException
	{
		final LoginForm loginForm = new LoginForm();
		model.addAttribute(loginForm);
		model.addAttribute(new RegisterForm());
		model.addAttribute(new GuestForm());
		UserModel userModel = null;
		final String username = (String) session.getAttribute(SPRING_SECURITY_LAST_USERNAME);
		try
		{
			if (username != null)
			{
				session.removeAttribute(SPRING_SECURITY_LAST_USERNAME);
				userModel = userService.getUserForUID(StringUtils.lowerCase(username));
			}
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.error(username + ":User id not found", e);
		}
		catch (final Exception e)
		{
			LOG.error("Error with login", e);
		}

		loginForm.setJ_username(username);
		storeCmsPageInModel(model, getCmsPage());
		setUpMetaDataForContentPage(model, (ContentPageModel) getCmsPage());
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.INDEX_NOFOLLOW);

		final Breadcrumb loginBreadcrumbEntry = new Breadcrumb("#", getMessageSource().getMessage("header.link.login", null,
				"header.link.login", getI18nService().getCurrentLocale()), null);
		model.addAttribute("breadcrumbs", Collections.singletonList(loginBreadcrumbEntry));

		if (loginError)
		{
			model.addAttribute("loginError", Boolean.valueOf(loginError));
			if (session.getAttribute(SPRING_SECURITY_LAST_EXCEPTION) instanceof EmailNotVerifiedException)
			{
				final EmailNotVerifiedException exception = (EmailNotVerifiedException) session
						.getAttribute(SPRING_SECURITY_LAST_EXCEPTION);
				GlobalMessages.addErrorMessage(model, exception.getMessage());

				session.setAttribute("SPRING_SECURITY_LAST_EXCEPTION", null);
			}
			else if (userModel != null && userModel.isLoginDisabled())
			{
				GlobalMessages.addErrorMessage(model, "login.error.account.disabled.title");
			}
			else
			{
				if (userModel != null && userModel instanceof GreenleeB2BCustomerModel)
				{
					if (!((GreenleeB2BCustomerModel) userModel).getActive().booleanValue())
					{
						GlobalMessages.addErrorMessage(model, "login.error.account.disable.msg");
					}
					else
					{
						GlobalMessages.addErrorMessage(model, "login.error.account.invalid.user");
					}
				}
				else
				{
					GlobalMessages.addErrorMessage(model, "login.error.account.not.registered");
				}
			}

		}

		return getView();

	}

	protected void storeReferer(final String referer, final HttpServletRequest request, final HttpServletResponse response)
	{
		if (StringUtils.isNotBlank(referer) && !StringUtils.endsWith(referer, "/login")
				&& StringUtils.contains(referer, request.getServerName()))
		{
			httpSessionRequestCache.saveRequest(request, response);
		}
	}

	@RequestMapping(value = "/registerPage", method = RequestMethod.GET)
	public String registerPage(final Model model) throws CMSItemNotFoundException, MalformedURLException
	{
		final String websiteUrlForSite = siteBaseUrlResolutionService.getWebsiteUrlForSite(cmsSiteService.getCurrentSite(), null,
				true, "/login/regions");
		model.addAttribute("countryData", checkoutFacade.getDeliveryCountries());
		model.addAttribute("titleData", userFacade.getTitles());
		model.addAttribute("registerForm", new B2BRegisterForm());
		model.addAttribute("regionsUrl", websiteUrlForSite);
		model.addAttribute("userTypeSelected", null);
		registrationFormData(model);

		storeCmsPageInModel(model, getContentPageForLabelOrId(GREENLEE_REGISTER_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(GREENLEE_REGISTER_PAGE));
		model.addAttribute("breadcrumbs", registerBreadcrumbBuilder.getBreadcrumbs(null));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		model.addAttribute("register", "active");

		return getViewForPage(model);
	}




	public void registrationFormData(final Model model)
	{
		final Map<String, String> userTypeSelection = new HashMap<String, String>();
		userTypeSelection.put("Consumer - Credit Card Payment", "B2C");
		userTypeSelection.put("Current Greenlee Distributor", "B2B");
		userTypeSelection.put("Business - Purchase Order Payment", "B2E");
		model.addAttribute("userTypeData", userTypeSelection);


		final Map<String, String> accountInformation = new HashMap<String, String>();
		accountInformation.put("Account Number", "accountNumber");
		accountInformation.put("Purchase Order Number", "purchaseOrderNumber");
		accountInformation.put("Invoice Number", "invoiceNumber");
		model.addAttribute("accountInformationData", accountInformation);

	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String doRegister(@ModelAttribute("registerForm") final B2BRegisterForm registerForm,
			final BindingResult bindingResult, final Model model, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException
	{
		getGreenLeeRegistrationValidator().validate(registerForm, bindingResult);
		if (bindingResult.hasErrors())
		{

			return redirectToRegisterPageWithErrors(registerForm, model);
		}

		return processRegisterUserRequest(registerForm, bindingResult, model, redirectModel);
	}

	public String redirectToRegisterPageWithErrors(final B2BRegisterForm registerForm, final Model model)
			throws CMSItemNotFoundException
	{
		final String websiteUrlForSite = siteBaseUrlResolutionService.getWebsiteUrlForSite(cmsSiteService.getCurrentSite(), null,
				true, "/login/regions");
		model.addAttribute("countryData", checkoutFacade.getDeliveryCountries());
		model.addAttribute("regionsForSelectedCountry", getI18NFacade().getRegionsForCountryIso(registerForm.getCountry()));
		model.addAttribute("titleData", userFacade.getTitles());
		model.addAttribute(registerForm);
		model.addAttribute("regionsUrl", websiteUrlForSite);
		model.addAttribute("userTypeSelected", registerForm.getUserType());
		model.addAttribute("selectedCountryIsoCode", registerForm.getCountry());
		model.addAttribute("selectedStateIsoCode", registerForm.getState());
		model.addAttribute("selectedAccountInformation", registerForm.getAccountInformation());
		model.addAttribute("selectedAccountInformationNumber", registerForm.getAccountInformationNumber());
		registrationFormData(model);
		model.addAttribute(new LoginForm());
		model.addAttribute(new GuestForm());

		GlobalMessages.addErrorMessage(model, "form.global.error");
		storeCmsPageInModel(model, getContentPageForLabelOrId(GREENLEE_REGISTER_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(GREENLEE_REGISTER_PAGE));
		return getViewForPage(model);
	}

	protected String getCurrencyFromCountryIsoCode(final String isoCode)
	{
		return ("CA").equalsIgnoreCase(isoCode) ? "CAD" : "USD";
	}

	protected String processRegisterUserRequest(final B2BRegisterForm form, final BindingResult bindingResult, final Model model,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		final B2CRegisterData data = new B2CRegisterData();
		data.setFirstName(form.getFirstName());
		data.setLastName(form.getLastName());
		data.setLogin(form.getEmail().toLowerCase());
		data.setPassword(form.getCheckPwd());
		data.setTitleCode(form.getTitleCode());
		data.setUserSelection(form.getUserSelection());

		data.setUserType(form.getUserType());
		data.setCompanyName(form.getCompanyName());
		if (form.getHasExistingGreenleeAccount() != null)
		{
			data.setHasExistingGreenleeAccount(form.getHasExistingGreenleeAccount().booleanValue());
		}
		if (!form.getAccountInformationNumber().isEmpty())
		{
			data.setAccountInformationNumber(form.getAccountInformationNumber());
		}
		if (form.getAccountInformation() != null && !form.getAccountInformation().isEmpty())
		{
			data.setAccountInformation(form.getAccountInformation());
		}
		if (form.getAgreeToPrivacyPolicy() != null)
		{
			data.setAgreeToPrivacyPolicy(form.getAgreeToPrivacyPolicy().booleanValue());
		}
		if (form.getRequestForInfo() != null)
		{
			data.setRequestForInfo(form.getRequestForInfo().booleanValue());
		}
		if (form.getCountry().equalsIgnoreCase("CA"))
		{
			data.setCountry("CA");
		}
		else
		{
			data.setCountry("US");
		}
		try
		{
			final boolean b2CCustomer = B2C_CUSTOMER.equalsIgnoreCase(form.getUserType());
			final boolean b2ECCustomer = B2E_CUSTOMER.equalsIgnoreCase(form.getUserType());
			final boolean isExistingAccount = form.getHasExistingGreenleeAccount().booleanValue();
			if (b2CCustomer || (b2ECCustomer && !isExistingAccount))
			{
				LOG.error("Checking Customer User Types " + form.getUserType() + "Is Existing User Account "
						+ form.getHasExistingGreenleeAccount().booleanValue());
				final GreenleeB2BCustomerModel customerModel = getGlCustomerFacade().saveCustomer(data);
				GreenleeGlobalMessages.addFlashMessage(redirectModel, GreenleeGlobalMessages.THANK_MESSAGES_HOLDER,
						"registration.confirmation.message.title");
				GreenleeGlobalMessages.accThankMessage(model, "registration.confirmation.message.title");

				createPrimaryAccountAddressForUser(form, customerModel);
			}
			else
			{
				LOG.error("Else - Checking Customer User Types " + form.getUserType() + "Is Existing User Account "
						+ form.getHasExistingGreenleeAccount().booleanValue());

				getGlCustomerFacade().saveCustomer(data);
				GreenleeGlobalMessages.addFlashMessage(redirectModel, GreenleeGlobalMessages.THANK_MESSAGES_HOLDER,
						"registration.confirmation,message.title.b2bb2enonadmin");
				GreenleeGlobalMessages.accThankMessage(model, "registration.confirmation,message.title.b2bb2enonadmin");
				/*
				 * if (B2E_CUSTOMER.equalsIgnoreCase(form.getUserType()) && !form.getAccountInformationNumber().isEmpty()) {
				 * createBillingAddressForUser(form, data); }
				 */
			}
		}
		catch (final DuplicateUidException e)
		{
			LOG.error(e);
			bindingResult.rejectValue("email", "registration.error.account.exists.title");
			return redirectToRegisterPageWithErrors(form, model);
		}
		return REDIRECT_PREFIX + getPreviewUrlResolverMap().get(HOME_PAGE);
	}

	/**
	 * @param form
	 * @param newCustomer
	 *           as GreenleeB2BCustomerModel
	 */
	public void createPrimaryAccountAddressForUser(final B2BRegisterForm form, final GreenleeB2BCustomerModel newCustomer)
	{
		final AddressData addressData = new AddressData();
		addressData.setTitleCode(form.getTitleCode());
		addressData.setFirstName(form.getFirstName());
		addressData.setLastName(form.getLastName());
		addressData.setPhone(form.getMobileNumber());
		addressData.setLine1(form.getAddressLane1());
		addressData.setLine2(form.getAddressLane2());
		addressData.setTown(form.getCity());
		addressData.setBillingAddress(false);
		addressData.setShippingAddress(false);
		addressData.setPrimaryAddress(Boolean.TRUE); // Primary Address GSD-36
		addressData.setCountry(getI18NFacade().getCountryForIsocode(form.getCountry()));
		addressData.setPostalCode(form.getZipCode());
		LOG.error("createPrimaryAccountAddressForUser >> Country >> "
				+ getI18NFacade().getCountryForIsocode(form.getCountry()).getIsocode());
		if (regionCountryService.hasRegion(form.getCountry()))
		{
			final String[] stateIsoCode = form.getState().split(",");

			if (form.getState() != null && !StringUtils.isEmpty(form.getState()))
			{
				addressData.setRegion(getI18NFacade().getRegion(form.getCountry(), stateIsoCode[0]));
			}
		}
		else
		{
			addressData.setDistrict(form.getEnteredState());
		}
		if (userFacade.isAddressBookEmpty())
		{
			addressData.setDefaultAddress(true);
			addressData.setVisibleInAddressBook(true);
		}
		greenleeUserFacade.addAddress(addressData, newCustomer);
	}

	@RequestMapping(value = "/regions")
	@ResponseBody
	public List<RegionData> getRegions(@RequestParam("isocode") final String isoCode)
	{
		return getI18NFacade().getRegionsForCountryIso(isoCode);
	}

	/**
	 * @return the previewUrlResolverMap
	 */
	public Map<String, String> getPreviewUrlResolverMap()
	{
		return previewUrlResolverMap;
	}

}

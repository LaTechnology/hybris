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

import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.ForgottenPwdForm;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.commerceservices.security.SecureToken;
import de.hybris.platform.commerceservices.security.SecureTokenService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.greenlee.facades.customer.GreenleeCustomerFacade;
import com.greenlee.storefront.controllers.ControllerConstants;
import com.greenlee.storefront.forms.UpdatePwdForm;
import com.greenlee.storefront.forms.validation.ResetPasswordValidator;


/**
 * Controller for the forgotten password pages. Supports requesting a password reset email as well as changing the
 * password once you have got the token that was sent via email.
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/login/pw")
public class PasswordResetPageController extends AbstractPageController
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(PasswordResetPageController.class);

	private static final String FORGOTTEN_PWD_TITLE = "forgottenPwd.title";
	private static final String REDIRECT_PWD_REQ_CONF = "redirect:/login/pw/request/external/conf";
	private static final String REDIRECT_LOGIN = "redirect:/login";
	private static final String REDIRECT_HOME = "redirect:/";
	private static final String UPDATE_PWD_CMS_PAGE = "updatePassword";

	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;

	@Resource(name = "b2bCustomerFacade")
	protected CustomerFacade b2bCustomerFacade;

	@Resource(name = "greenleeCustomerFacade")
	private GreenleeCustomerFacade greenleeCustomerFacade;

	@Resource(name = "simpleBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;

	@Resource(name = "resetPasswordValidator")
	private ResetPasswordValidator resetPasswordValidator;

	@Resource(name = "secureTokenService")
	private SecureTokenService secureTokenService;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@RequestMapping(value = "/request", method = RequestMethod.GET)
	public String getPasswordRequest(final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute(new ForgottenPwdForm());
		model.addAttribute("USER_ADDED_BY_ADMIN", false);
		return ControllerConstants.Views.Fragments.Password.PasswordResetRequestPopup;
	}

	@RequestMapping(value = "/request", method = RequestMethod.POST)
	public String passwordRequest(@Valid final ForgottenPwdForm form, final BindingResult bindingResult, final Model model)
			throws CMSItemNotFoundException
	{
		final String emailId = form.getEmail();
		if (bindingResult.hasErrors())
		{
			final CustomerModel customerModel = userService.getUserForUID(emailId.toLowerCase(), CustomerModel.class);
			if (customerModel != null)
			{
				final B2BCustomerModel b2bCustomerModel = (B2BCustomerModel) customerModel;
				b2bCustomerModel.setUserAddedByAdmin(Boolean.valueOf(false));
				modelService.save(b2bCustomerModel);
				model.addAttribute("USER_ADDED_BY_ADMIN", false);
			}
			return ControllerConstants.Views.Fragments.Password.PasswordResetRequestPopup;
		}
		else
		{
			try
			{
				final CustomerModel customerModel = userService.getUserForUID(emailId.toLowerCase(), CustomerModel.class);
				if (customerModel != null)
				{
					final B2BCustomerModel b2bCustomerModel = (B2BCustomerModel) customerModel;
					b2bCustomerModel.setUserAddedByAdmin(Boolean.valueOf(false));
					modelService.save(b2bCustomerModel);
					model.addAttribute("USER_ADDED_BY_ADMIN", false);
				}
				customerFacade.forgottenPassword(form.getEmail());
			}
			catch (final UnknownIdentifierException unknownIdentifierException)
			{
				LOG.warn("Email: " + form.getEmail() + " does not exist in the database.");
				LOG.error(unknownIdentifierException);
				return ControllerConstants.Views.Fragments.Password.ForgotPasswordNoEmailMessage;

			}
			return ControllerConstants.Views.Fragments.Password.ForgotPasswordValidationMessage;
		}
	}

	@RequestMapping(value = "/request/external", method = RequestMethod.GET)
	public String getExternalPasswordRequest(final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute(new ForgottenPwdForm());
		storeCmsPageInModel(model, getContentPageForLabelOrId(null));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(null));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, resourceBreadcrumbBuilder.getBreadcrumbs(FORGOTTEN_PWD_TITLE));
		return ControllerConstants.Views.Pages.Password.PasswordResetRequest;
	}

	@RequestMapping(value = "/request/external/conf", method = RequestMethod.GET)
	public String getExternalPasswordRequestConf(final Model model) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(null));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(null));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, resourceBreadcrumbBuilder.getBreadcrumbs(FORGOTTEN_PWD_TITLE));
		return ControllerConstants.Views.Pages.Password.PasswordResetRequestConfirmation;
	}

	@RequestMapping(value = "/request/external", method = RequestMethod.POST)
	public String externalPasswordRequest(@Valid final ForgottenPwdForm form, final BindingResult bindingResult,
			final Model model, final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(null));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(null));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, resourceBreadcrumbBuilder.getBreadcrumbs(FORGOTTEN_PWD_TITLE));

		if (bindingResult.hasErrors())
		{
			return ControllerConstants.Views.Pages.Password.PasswordResetRequest;
		}
		else
		{
			try
			{
				customerFacade.forgottenPassword(form.getEmail());
				GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
						"account.confirmation.forgotten.password.link.sent");
			}
			catch (final UnknownIdentifierException unknownIdentifierException)
			{
				LOG.warn("Email: " + form.getEmail() + " does not exist in the database.");
				LOG.error(unknownIdentifierException);
			}
			return REDIRECT_PWD_REQ_CONF;
		}
	}

	@SuppressWarnings("boxing")
	@RequestMapping(value = "/change", method = RequestMethod.GET)
	public String getChangePassword(@RequestParam(required = false) final String token, final Model model)
			throws CMSItemNotFoundException
	{
		if (StringUtils.isBlank(token))
		{
			return REDIRECT_HOME;
		}
		final UpdatePwdForm form = new UpdatePwdForm();
		form.setToken(token);
		//GRE-2153
		final SecureToken data = secureTokenService.decryptData(form.getToken());
		final CustomerModel customerModel = userService.getUserForUID(data.getData().toLowerCase(), CustomerModel.class);
		if (customerModel != null)
		{
			final B2BCustomerModel b2bCustomerModel = (B2BCustomerModel) customerModel;
			boolean flag = false;
			if (null == b2bCustomerModel.getUserAddedByAdmin())
			{
				form.setUpdatedByAdmin(false);
				flag = false;
				b2bCustomerModel.setUserAddedByAdmin(false);
			}
			if (b2bCustomerModel.getUserAddedByAdmin().booleanValue())
			{
				form.setUpdatedByAdmin(true);
				flag = true;
			}
			else
			{
				form.setUpdatedByAdmin(false);
				flag = false;
				b2bCustomerModel.setUserAddedByAdmin(false);
			}
			modelService.save(customerModel);
			LOG.info("form.setUpdatedByAdmin(" + form.isUpdatedByAdmin());
			model.addAttribute("USER_ADDED_BY_ADMIN", flag);
		}//End of GRE-2153
		model.addAttribute(form);
		storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_PWD_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_PWD_CMS_PAGE));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, resourceBreadcrumbBuilder.getBreadcrumbs("updatePwd.title"));
		return ControllerConstants.Views.Pages.Password.PasswordResetChangePage;
	}

	@RequestMapping(value = "/change", method = RequestMethod.POST)
	public String changePassword(@Valid final UpdatePwdForm form, final BindingResult bindingResult, final Model model,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException, TokenInvalidatedException
	{
		final SecureToken data = secureTokenService.decryptData(form.getToken());
		final B2BCustomerModel b2bCustomerModel = userService.getUserForUID(data.getData().toLowerCase(), B2BCustomerModel.class);
		if (b2bCustomerModel != null)
		{
			form.setUpdatedByAdmin(b2bCustomerModel.getUserAddedByAdmin());
		}
		resetPasswordValidator.validate(form, bindingResult);
		final Map modelMap = model.asMap();
		for (final Object modelKey : modelMap.keySet())
		{
			final Object modelValue = modelMap.get(modelKey);
			LOG.info(modelKey + " -- " + modelValue);
		}
		if (bindingResult.hasErrors())
		{
			prepareErrorMessage(model, UPDATE_PWD_CMS_PAGE);
			boolean flag = false;
			if (null == b2bCustomerModel.getUserAddedByAdmin())
			{
				form.setUpdatedByAdmin(false);
				flag = false;
				b2bCustomerModel.setUserAddedByAdmin(Boolean.valueOf(false));
			}
			if (b2bCustomerModel.getUserAddedByAdmin().booleanValue())
			{
				form.setUpdatedByAdmin(true);
				flag = true;
			}
			else
			{
				form.setUpdatedByAdmin(false);
				flag = false;
				b2bCustomerModel.setUserAddedByAdmin(Boolean.valueOf(false));
			}
			modelService.save(b2bCustomerModel);
			model.addAttribute("USER_ADDED_BY_ADMIN", flag);
			//End of GRE-2153
			return ControllerConstants.Views.Pages.Password.PasswordResetChangePage;
		}
		if (!StringUtils.isBlank(form.getToken()))
		{
			try
			{
				b2bCustomerFacade.updatePassword(form.getToken(), form.getPwd());
				//GRE-2153
				if (b2bCustomerModel != null)
				{
					LOG.info("B2BCustomerModel >>>>>" + b2bCustomerModel.getUserAddedByAdmin().booleanValue());
					if (b2bCustomerModel.getUserAddedByAdmin().booleanValue())
					{
						b2bCustomerModel.setAgreeToPrivacyPolicy(Boolean.valueOf(form.isAgreeToPrivacyPolicy()));
						b2bCustomerModel.setRequestForInfo(Boolean.valueOf(form.isRequestForInfo()));
						modelService.save(b2bCustomerModel);
						greenleeCustomerFacade.updateUserDetailsToSaleforceByPostCall(b2bCustomerModel);
						LOG.info("updateUserDetailsToSaleforceByPostCall Completed at Password Reset ");
					}
				}//End of GRE-2153
				GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
						"account.confirmation.password.updated");
			}
			catch (final TokenInvalidatedException e)
			{
				LOG.error(e);
				GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER, "updatePwd.token.invalidated");
			}
			catch (final RuntimeException e)
			{
				LOG.error(e);
				GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER, "updatePwd.token.invalid");
			}
		}
		return REDIRECT_LOGIN;
	}

	/**
	 * Prepares the view to display an error message
	 *
	 * @throws CMSItemNotFoundException
	 */
	protected void prepareErrorMessage(final Model model, final String page) throws CMSItemNotFoundException
	{
		GlobalMessages.addErrorMessage(model, "form.global.error");
		storeCmsPageInModel(model, getContentPageForLabelOrId(page));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(page));
	}
}

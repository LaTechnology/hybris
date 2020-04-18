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


import de.hybris.platform.acceleratorstorefrontcommons.annotations.PreValidateCheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.CheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.checkout.steps.AbstractCheckoutStepController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.PlaceOrderForm;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bacceleratorfacades.api.cart.CheckoutFacade;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.greenlee.core.model.GreenleeB2BCustomerModel;
import com.greenlee.core.salesforce.services.SalesforceService;
import com.greenlee.facades.cart.GreenLeeCartFacade;
import com.greenlee.facades.customer.GreenleeUserFacade;
import com.greenlee.storefront.checkout.forms.GreenleeTAndCForm;
import com.greenlee.storefront.controllers.ControllerConstants;


@Controller
@RequestMapping(value = "/checkout/multi/terms-condition")
public class TermsAndConditionCheckoutStepController extends AbstractCheckoutStepController
{
	private final static String TERMS_AND_CONDITIONS = "terms-condition";
	private static final Logger LOG = Logger.getLogger(TermsAndConditionCheckoutStepController.class);
	private static final String ERR_NTFY_SUPPORT_0006 = "ERR_NTFY_SUPPORT_0006";
	private static final String ERR_NTFY_SUPPORT_000500 = "ERR_NTFY_SUPPORT_000500";
	private static final String ERR_NTFY_SUPPORT_0007 = "ERR_NTFY_SUPPORT_0007";

	public static final String DUMMY_UNIT_B2B = Config.getString("greenlee.account.dummy.distributor.uid", "dummydistributor");
	public static final String DUMMY_UNIT_B2E = Config.getString("greenlee.account.dummy.distributor.b2e.uid", "0010000014");
	public static final String DUMMY_UNIT_B2C = Config.getString("greenlee.account.dummy.distributor.b2c.uid", "0010000012");

	public static final String DUMMY_UNIT_B2B_CAD = Config.getString("greenlee.account.dummy.distributor.uid.cad",
			"dummydistributor_CAD");
	public static final String DUMMY_UNIT_B2E_CAD = Config.getString("greenlee.account.dummy.distributor.b2e.uid.cad",
			"0010000014");
	public static final String DUMMY_UNIT_B2C_CAD = Config.getString("greenlee.account.dummy.distributor.b2c.uid.cad",
			"0010000012");


	@Resource(name = "greenleeB2BCheckoutFacade")
	private CheckoutFacade greenleeCheckoutFacade;

	@Resource(name = "greenleeUserFacade")
	private GreenleeUserFacade greenleeUserFacade;
	@Resource(name = "cartService")
	private CartService cartService;
	@Autowired
	private UserService userService;
	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = "salesforceService")
	private SalesforceService salesforceService;

	@Resource(name = "storefrontMessageSource")
	private MessageSource messageSource;

	@Resource(name = "i18nService")
	private I18NService i18nService;

	@Resource(name = "greenleeCartFacade")
	private GreenLeeCartFacade greenleeCartFacade;

	@Resource
	private SessionService sessionService;

	@SuppressWarnings("boxing")
	@RequestMapping(value = "/select", method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	@PreValidateCheckoutStep(checkoutStep = TERMS_AND_CONDITIONS)
	public String enterStep(final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		model.addAttribute("metaRobots", "noindex,nofollow");
		DeliveryModeData modeData = null;
		final String userCode = getUser().getUid();
		final CartData cartData = getCheckoutFacade().getCheckoutCart();
		final GreenleeTAndCForm greenleeTAndCForm = new GreenleeTAndCForm();
		GreenleeB2BCustomerModel currentUser = null;
		try
		{
			currentUser = (GreenleeB2BCustomerModel) userService.getCurrentUser();
			// This is to prevent the order processing / checkout process when there was an error in the order simulation process.
			if (sessionService.getAttribute(ERR_NTFY_SUPPORT_000500) != null
					&& ERR_NTFY_SUPPORT_000500.equalsIgnoreCase(ERR_NTFY_SUPPORT_000500))
			{
				final String msg = "ERR_NTFY_SUPPORT_000500 - There was a problem with the customer creation process for user "
						+ userCode + " Please try processing again.";
				LOG.error(msg); //performCustomerCreationSAPPI
				model.addAttribute("hasErrorInCart", Boolean.valueOf(true));
			}
			if (sessionService.getAttribute(ERR_NTFY_SUPPORT_0007) != null
					&& ERR_NTFY_SUPPORT_0007.equalsIgnoreCase(ERR_NTFY_SUPPORT_0007))
			{
				final String warning = "ERR_NTFY_SUPPORT_0007 - Livingston customer verification failed for the customer " + userCode
						+ " cart number " + cartData.getCode();
				LOG.error(warning); //performCustomerCreationSAPPI
				model.addAttribute("hasErrorInCart", Boolean.valueOf(true));
			}
			if (ERR_NTFY_SUPPORT_0006.equalsIgnoreCase(ERR_NTFY_SUPPORT_0006)
					&& sessionService.getAttribute(ERR_NTFY_SUPPORT_0006) != null)
			{
				final String warningMsg = "ERR_NTFY_SUPPORT_0006 - Real time ordersimulation failed for customer " + userCode
						+ " cart number " + cartData.getCode() + " Event: OrderSimulation Failed.";
				LOG.error(warningMsg);//performOrderSimulationOnSAPPI
				model.addAttribute("hasErrorInCart", Boolean.valueOf(true));
			}
			if (null != cartData.getSelectedDeliveryMode())
			{
				modeData = greenleeCartFacade.getDeliveryMethodForCode(cartData.getSelectedDeliveryMode());
				if (null != modeData)
				{
					cartData.setDeliveryMode(modeData);
				}
				cartData.setUnit(getUser().getSessionB2BUnit());
				LOG.info("enterStep >> get >> Selected Delivery Mode [ " + cartData.getDeliveryMode().getCode()
						+ " ] Delivery Name [ " + cartData.getDeliveryMode().getName() + " ]");
				model.addAttribute("deliveryNameBilling", modeData.getName());
			}
			model.addAttribute("cartData", cartData);
			model.addAttribute("greenleeTAndCForm", greenleeTAndCForm);
			prepareDataForCMSPage(model);
		}
		catch (final Exception exception)
		{
			final String error = messageSource.getMessage("err.nifty.support.0011", new Object[]
			{ currentUser.getUid() }, "ERR_NTFY_SUPPORT_00011 - Salesforce real time customer update failure for customer {0} ",
					i18nService.getCurrentLocale());
			LOG.error(error);
			LOG.error("Salesforce update error", exception);
		}
		return ControllerConstants.Views.Pages.MultiStepCheckout.TermsAndConditionPage;
	}

	/**
	 * This method gets called when the "Next" button is clicked. It sets the selected the terms and condition mode on
	 * the checkout.
	 *
	 * @param greenleeTAndCForm
	 * @param bindingResult
	 * @param model
	 * @param redirectAttributes
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@RequireHardLogIn
	public String add(@ModelAttribute final GreenleeTAndCForm greenleeTAndCForm, final BindingResult bindingResult,
			final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		final boolean termsAndCondition = greenleeTAndCForm.getTermsAndCondition().booleanValue();
		final PlaceOrderForm placeOrderForm = new PlaceOrderForm();
		final CartData cartData = getCheckoutFacade().getCheckoutCart();
		if (termsAndCondition != true)
		{
			GlobalMessages.addErrorMessage(model, "checkout.error.terms.not.accepted");
			model.addAttribute("greenleeTAndCForm", greenleeTAndCForm);
			model.addAttribute("cartData", cartData);
			prepareDataForCMSPage(model);
			return ControllerConstants.Views.Pages.MultiStepCheckout.TermsAndConditionPage;
		}
		placeOrderForm.setTermsCheck(termsAndCondition);

		final GreenleeB2BCustomerModel currentUser = (GreenleeB2BCustomerModel) userService.getCurrentUser();
		if (currentUser.getSalesforceId() != null && StringUtils.isNotEmpty(currentUser.getSalesforceId())
				&& StringUtils.isNotBlank(currentUser.getSalesforceId()))
		{
			LOG.error("Salesforce real time customer update in progress for customer " + currentUser.getUid());
			salesforceService.updateSapIdToSalesforce(currentUser);
		}
		else
		{
			LOG.error("ERR_NTFY_SUPPORT_00011 - Salesforce real time customer update failed for customer " + currentUser.getUid()
					+ " either post failed or System Exception occured.");
		}
		preparePostUpdateB2BUnit(currentUser);
		model.addAttribute("placeOrderForm", placeOrderForm);
		prepareDataForCMSPage(model);
		model.addAttribute("cartData", cartData);
		return getCheckoutStep().nextStep();
	}

	private void prepareDataForCMSPage(final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute("metaRobots", "noindex,nofollow");
		prepareDataForPage(model);
		storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		setCheckoutStepLinksForModel(model, getCheckoutStep());
	}

	private void preparePostUpdateB2BUnit(final GreenleeB2BCustomerModel currentUser)
	{
		 final CartModel cartModel = cartService.getSessionCart();
		if (cartModel != null && currentUser != null)
       {
			final B2BUnitModel b2bUnitModel = cartModel.getUnit();
			if (b2bUnitModel != null && b2bUnitModel.getUid().equalsIgnoreCase(DUMMY_UNIT_B2C)
					|| (b2bUnitModel.getUid().equalsIgnoreCase(DUMMY_UNIT_B2E))
					|| (b2bUnitModel.getUid().equalsIgnoreCase(DUMMY_UNIT_B2B))
					|| (b2bUnitModel.getUid().equalsIgnoreCase(DUMMY_UNIT_B2C_CAD))
					|| (b2bUnitModel.getUid().equalsIgnoreCase(DUMMY_UNIT_B2E_CAD))
					|| (b2bUnitModel.getUid().equalsIgnoreCase(DUMMY_UNIT_B2B_CAD)))
			{
				final B2BUnitModel getDefaultB2BUnitModel = currentUser.getDefaultB2BUnit();
				cartModel.setUnit(getDefaultB2BUnitModel);
				modelService.save(cartModel);
				LOG.error("Unit Model Saved for the Customer ID " + currentUser.getUid() + " In the Cart ID: " + cartModel.getCode());
			}
		}
	}
	@RequestMapping(value = "/back", method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	public String back(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().previousStep();
	}

	@RequestMapping(value = "/next", method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	public String next(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().nextStep();
	}

	protected CheckoutStep getCheckoutStep()
	{
		return getCheckoutStep(TERMS_AND_CONDITIONS);
	}
}

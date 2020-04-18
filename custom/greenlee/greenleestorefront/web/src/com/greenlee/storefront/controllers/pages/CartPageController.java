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

import de.hybris.platform.acceleratorfacades.flow.impl.SessionOverrideCheckoutFlowFacade;
import de.hybris.platform.acceleratorfacades.order.AcceleratorCheckoutFacade;
import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.acceleratorservices.enums.CheckoutFlowEnum;
import de.hybris.platform.acceleratorservices.enums.CheckoutPciOptionEnum;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractCartPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.UpdateQuantityForm;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.voucher.VoucherFacade;
import de.hybris.platform.commercefacades.voucher.data.VoucherData;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.greenlee.core.constants.GreenleeCoreConstants;
import com.greenlee.core.enums.UserTypes;
import com.greenlee.core.model.GreenleeB2BCustomerModel;
import com.greenlee.facades.cart.GreenLeeCartFacade;
import com.greenlee.facades.price.data.OrderSimulationData;
import com.greenlee.facades.price.data.OrderSimulationProductData;
import com.greenlee.facades.price.data.PriceDiffData;
import com.greenlee.storefront.controllers.ControllerConstants;


/**
 * Controller for cart page
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/cart")
public class CartPageController extends AbstractCartPageController
{
	private static final String CART = "/cart";

	private static final Logger LOG = Logger.getLogger(CartPageController.class);

	public static final String SHOW_CHECKOUT_STRATEGY_OPTIONS = "storefront.show.checkout.flows";
	public static final String ERROR_MSG_TYPE = "errorMsg";
	public static final String SUCCESSFUL_MODIFICATION_CODE = "success";

	@Resource(name = "simpleBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;

	@Resource(name = "enumerationService")
	private EnumerationService enumerationService;

	@Resource(name = "productVariantFacade")
	private ProductFacade productFacade;

	@Resource(name = "greenleeCartFacade")
	private GreenLeeCartFacade greenLeeCartFacade;

	@Resource(name = "voucherFacade")
	private VoucherFacade voucherFacade;

	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Resource(name = "cartFacade")
	private CartFacade cartFacade;

	private static final String CONTINUE_URL = "continueUrl";

	@Resource(name = "acceleratorCheckoutFacade")
	private AcceleratorCheckoutFacade checkoutFacade;

	private static final String CART_CMS_PAGE_LABEL = "cart";

	@Resource(name = "userService")
	private UserService userService;

	@ModelAttribute("showCheckoutStrategies")
	public boolean isCheckoutStrategyVisible()
	{
		return getSiteConfigService().getBoolean(SHOW_CHECKOUT_STRATEGY_OPTIONS, false);
	}

	/*
	 * Display the cart page
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String showCart(final Model model, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException, CommerceCartModificationException
	{
		try
		{
			greenLeeCartFacade.resetCheckoutAttributes();
		}
		catch (final CalculationException e)
		{
			e.printStackTrace();
		}
		prepareDataForPage(model);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, resourceBreadcrumbBuilder.getBreadcrumbs("breadcrumb.cart"));
		model.addAttribute("pageType", PageType.CART.name());
		model.addAttribute("switchAccountDisplayAddress", Boolean.TRUE);
		model.addAttribute("vouchersInCart", getAppliedVouchers());
		final List<CommerceCartModification> modifications = getSessionService()
				.getAttribute(GreenleeCoreConstants.CART_REMOVAL_MESSAGE);
		if (modifications != null && !modifications.isEmpty())
		{
			redirectModel.addFlashAttribute("validationData", modifications);
			getSessionService().removeAttribute(GreenleeCoreConstants.CART_REMOVAL_MESSAGE);
		}

		return ControllerConstants.Views.Pages.Cart.CartPage;
	}


	@Override
	protected void prepareDataForPage(final Model model) throws CMSItemNotFoundException
	{
		final String continueUrl = (String) sessionService.getAttribute(WebConstants.CONTINUE_URL);
		model.addAttribute(CONTINUE_URL, (continueUrl != null && !continueUrl.isEmpty()) ? continueUrl : ROOT);

		createProductList(model);

		setupCartPageRestorationData(model);
		clearSessionRestorationData();

		model.addAttribute("isOmsEnabled", Boolean.valueOf(getSiteConfigService().getBoolean("oms.enabled", false)));
		model.addAttribute("supportedCountries", cartFacade.getDeliveryCountries());
		model.addAttribute("expressCheckoutAllowed", Boolean.valueOf(checkoutFacade.isExpressCheckoutAllowedForCart()));
		model.addAttribute("taxEstimationEnabled", Boolean.valueOf(checkoutFacade.isTaxEstimationEnabledForCart()));
	}

	@Override
	protected void createProductList(final Model model) throws CMSItemNotFoundException
	{
		final CartData cartData = cartFacade.getSessionCartWithEntryOrdering(false);
		boolean hasPickUpCartEntries = false;
		if (cartData.getEntries() != null && !cartData.getEntries().isEmpty())
		{
			for (final OrderEntryData entry : cartData.getEntries())
			{
				final OrderEntryData entryData = checkForUserType(entry);
				if (!hasPickUpCartEntries && entryData.getDeliveryPointOfService() != null)
				{
					hasPickUpCartEntries = true;
				}
				final UpdateQuantityForm uqf = new UpdateQuantityForm();
				uqf.setQuantity(entryData.getQuantity());
				model.addAttribute("updateQuantityForm" + entry.getEntryNumber(), uqf);
			}
		}
		model.addAttribute("error", Boolean.TRUE);
		model.addAttribute("cartData", cartData);
		model.addAttribute("hasPickUpCartEntries", Boolean.valueOf(hasPickUpCartEntries));

		storeCmsPageInModel(model, getContentPageForLabelOrId(CART_CMS_PAGE_LABEL));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CART_CMS_PAGE_LABEL));
	}

	private OrderEntryData checkForUserType(final OrderEntryData entry)
	{
		final ProductData product = entry.getProduct();
		final UserModel user = userService.getCurrentUser();
		B2BUnitModel unit = null;

		if (user instanceof B2BCustomerModel)
		{
			unit = ((GreenleeB2BCustomerModel) user).getSessionB2BUnit();
		}

		final Boolean b2bProduct = product.getB2bProduct();
		final Boolean b2cProduct = product.getB2cProduct();
		final Boolean b2eProduct = product.getB2eProduct();

		if (unit != null && unit.getUserType() != null)
		{

			product.setB2bProduct(b2bProduct);
			product.setB2cProduct(b2cProduct);
			product.setB2eProduct(b2eProduct);

			if ((UserTypes.B2C.getCode().equals(unit.getUserType().getCode()))
					|| (UserTypes.B2E.getCode().equals(unit.getUserType().getCode())))
			{
				setProductShowPrice(b2cProduct, b2eProduct, product);
			}

			if (UserTypes.B2B.getCode().equals(unit.getUserType().getCode()))
			{
				product.setShowPrice(true);
			}

		}
		else
		{
			setProductShowPrice(b2cProduct, b2eProduct, product);
		}
		entry.setProduct(product);
		return entry;
	}

	/**
	 * Handle the '/cart/checkout' request url. This method checks to see if the cart is valid before allowing the
	 * checkout to begin. Note that this method does not require the user to be authenticated and therefore allows us to
	 * validate that the cart is valid without first forcing the user to login. The cart will be checked again once the
	 * user has logged in.
	 *
	 * @return The page to redirect to
	 */
	@RequestMapping(value = "/checkout", method = RequestMethod.GET)
	@RequireHardLogIn
	public String cartCheck(final Model model, final RedirectAttributes redirectModel) throws CommerceCartModificationException
	{
		SessionOverrideCheckoutFlowFacade.resetSessionOverrides();

		if (!getCartFacade().hasEntries())
		{
			LOG.info("Missing or empty cart");

			// No session cart or empty session cart. Bounce back to the cart page.
			return REDIRECT_PREFIX + CART;
		}


		if (validateCart(redirectModel))
		{
			return REDIRECT_PREFIX + CART;
		}

		// Redirect to the start of the checkout flow to begin the checkout process
		// We just redirect to the generic '/checkout' page which will actually select the checkout flow
		// to use. The customer is not necessarily logged in on this request, but will be forced to login
		// when they arrive on the '/checkout' page.
		return REDIRECT_PREFIX + "/checkout";
	}

	@RequestMapping(value = "/getProductVariantMatrix", method = RequestMethod.GET)
	@RequireHardLogIn
	public String getProductVariantMatrix(@RequestParam("productCode") final String productCode, final Model model)
	{

		final ProductData productData = productFacade.getProductForCodeAndOptions(productCode,
				Arrays.asList(ProductOption.BASIC, ProductOption.CATEGORIES, ProductOption.VARIANT_MATRIX_BASE,
						ProductOption.VARIANT_MATRIX_PRICE, ProductOption.VARIANT_MATRIX_MEDIA, ProductOption.VARIANT_MATRIX_STOCK,
						ProductOption.VARIANT_MATRIX_URL));

		model.addAttribute("product", productData);

		return ControllerConstants.Views.Fragments.Cart.ExpandGridInCart;
	}

	// This controller method is used to allow the site to force the visitor through a specified checkout flow.
	// If you only have a static configured checkout flow then you can remove this method.
	@RequestMapping(value = "/checkout/select-flow", method = RequestMethod.GET)
	@RequireHardLogIn
	public String initCheck(final Model model, final RedirectAttributes redirectModel,
			@RequestParam(value = "flow", required = false) final String flow,
			@RequestParam(value = "pci", required = false) final String pci) throws CommerceCartModificationException
	{
		SessionOverrideCheckoutFlowFacade.resetSessionOverrides();

		if (!getCartFacade().hasEntries())
		{
			LOG.info("Missing or empty cart");

			// No session cart or empty session cart. Bounce back to the cart page.
			return REDIRECT_PREFIX + CART;
		}

		// Override the Checkout Flow setting in the session
		if (StringUtils.isNotBlank(flow))
		{
			final CheckoutFlowEnum checkoutFlow = enumerationService.getEnumerationValue(CheckoutFlowEnum.class,
					StringUtils.upperCase(flow));
			SessionOverrideCheckoutFlowFacade.setSessionOverrideCheckoutFlow(checkoutFlow);
		}

		// Override the Checkout PCI setting in the session
		if (StringUtils.isNotBlank(pci))
		{
			final CheckoutPciOptionEnum checkoutPci = enumerationService.getEnumerationValue(CheckoutPciOptionEnum.class,
					StringUtils.upperCase(pci));
			SessionOverrideCheckoutFlowFacade.setSessionOverrideSubscriptionPciOption(checkoutPci);
		}

		// Redirect to the start of the checkout flow to begin the checkout process
		// We just redirect to the generic '/checkout' page which will actually select the checkout flow
		// to use. The customer is not necessarily logged in on this request, but will be forced to login
		// when they arrive on the '/checkout' page.
		return REDIRECT_PREFIX + "/checkout";
	}



	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateCartQuantities(@RequestParam("entryNumber") final long entryNumber, final Model model,
			@Valid final UpdateQuantityForm form, final BindingResult bindingResult, final HttpServletRequest request,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		if (bindingResult.hasErrors())
		{
			for (final ObjectError error : bindingResult.getAllErrors())
			{
				if (("typeMismatch").equals(error.getCode()))
				{
					GlobalMessages.addErrorMessage(model, "basket.error.quantity.invalid");
				}
				else
				{
					GlobalMessages.addErrorMessage(model, error.getDefaultMessage());
				}
			}
		}
		else if (getCartFacade().hasEntries())
		{
			try
			{
				final CartModificationData cartModification = getCartFacade().updateCartEntry(entryNumber,
						form.getQuantity().longValue());
				if (cartModification.getQuantity() == form.getQuantity().longValue())
				{
					// Success

					if (cartModification.getQuantity() == 0)
					{
						// Success in removing entry
						GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
								"basket.page.message.remove");
					}
					else
					{
						// Success in update quantity
						GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
								"basket.page.message.update");
					}
				}
				else if (cartModification.getQuantity() > 0)
				{
					// Less than successful
					GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
							"basket.page.message.update.reducedNumberOfItemsAdded.lowStock", new Object[]
					{ cartModification.getEntry().getProduct().getName(), cartModification.getQuantity(), form.getQuantity(),
							request.getRequestURL().append(cartModification.getEntry().getProduct().getUrl()) });
				}
				else
				{
					// No more stock available
					GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
							"basket.page.message.update.reducedNumberOfItemsAdded.noStock", new Object[]
					{ cartModification.getEntry().getProduct().getName(),
							request.getRequestURL().append(cartModification.getEntry().getProduct().getUrl()) });
				}

				// Redirect to the cart page on update success so that the browser doesn't re-post again
				return REDIRECT_PREFIX + CART;
			}
			catch (final CommerceCartModificationException ex)
			{
				LOG.warn("Couldn't update product with the entry number: " + entryNumber + ".", ex);
			}
		}

		prepareDataForPage(model);

		model.addAttribute(WebConstants.BREADCRUMBS_KEY, resourceBreadcrumbBuilder.getBreadcrumbs("breadcrumb.cart"));
		model.addAttribute("pageType", PageType.CART.name());

		return ControllerConstants.Views.Pages.Cart.CartPage;
	}

	@SuppressWarnings("boxing")
	@ResponseBody
	@RequestMapping(value = "/updateMultiD", method = RequestMethod.POST)
	public CartData updateCartQuantitiesMultiD(@RequestParam("entryNumber") final Integer entryNumber,
			@RequestParam("productCode") final String productCode, final Model model, @Valid final UpdateQuantityForm form,
			final BindingResult bindingResult)
	{
		if (bindingResult.hasErrors())
		{
			for (final ObjectError error : bindingResult.getAllErrors())
			{
				if (("typeMismatch").equals(error.getCode()))
				{
					GlobalMessages.addErrorMessage(model, "basket.error.quantity.invalid");
				}
				else
				{
					GlobalMessages.addErrorMessage(model, error.getDefaultMessage());
				}
			}
		}
		else
		{
			try
			{
				final CartModificationData cartModification = getCartFacade()
						.updateCartEntry(getOrderEntryData(form.getQuantity(), productCode, entryNumber));
				if (cartModification.getStatusCode().equals(SUCCESSFUL_MODIFICATION_CODE))
				{
					GlobalMessages.addMessage(model, GlobalMessages.CONF_MESSAGES_HOLDER, cartModification.getStatusMessage(), null);
				}
				else if (!model.containsAttribute(ERROR_MSG_TYPE))
				{
					GlobalMessages.addMessage(model, GlobalMessages.ERROR_MESSAGES_HOLDER, cartModification.getStatusMessage(), null);
				}
			}
			catch (final CommerceCartModificationException ex)
			{
				LOG.warn("Couldn't update product with the entry number: " + entryNumber + ".", ex);
			}

		}
		return getCartFacade().getSessionCart();
	}

	@SuppressWarnings("boxing")
	protected OrderEntryData getOrderEntryData(final long quantity, final String productCode, final Integer entryNumber)
	{
		final OrderEntryData orderEntry = new OrderEntryData();
		orderEntry.setQuantity(quantity);
		orderEntry.setProduct(new ProductData());
		orderEntry.getProduct().setCode(productCode);
		orderEntry.setEntryNumber(entryNumber);
		return orderEntry;
	}

	@RequestMapping(value = "/realTimePriceCheck", method = RequestMethod.GET)
	public String getRTPricesAndAvailability(final Model model, final RedirectAttributes redirectAttributes)
	{
		try
		{
			try
			{
				final OrderSimulationData orderSimulationData = greenLeeCartFacade.getOrderSimulation(false);
				if (StringUtils.isNotEmpty(orderSimulationData.getErrorMsg()))
				{
					/*
					 * GlobalMessages.addMessage(model, GlobalMessages.ERROR_MESSAGES_HOLDER, "ordersimulation.error.msg",
					 * new Object[] { orderSimulationData.getErrorMsg() });
					 */
					GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
							"ordersimulation.error.msg", new Object[]
					{ orderSimulationData.getErrorMsg() });

					LOG.error("Error message from SAP/ERP during Real time pricing");
				}
				if (orderSimulationData.getProducts() != null && orderSimulationData.getProducts().size() > 0)
				{
					final List<PriceDiffData> diffDataList = handlePriceDifferences(orderSimulationData, model, redirectAttributes);
					if (!diffDataList.isEmpty())
					{
						if (diffDataList.size() == 1)
						{
							GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.INFO_MESSAGES_HOLDER,
									"pricediff.error.count.one");
						}
						else
						{
							GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.INFO_MESSAGES_HOLDER,
									"pricediff.error.count.multi", new Object[]
							{ diffDataList.size() });
						}
						LOG.error("Price difference during Real time pricing call");
					}
				}

			}
			catch (final CalculationException e)
			{
				LOG.error("Error calculating totals after Real time pricing call to ERP", e);
				GlobalMessages.addErrorMessage(model, "ordersimulation.error.msg");
			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception while trying to check price from ERP", e);
			GlobalMessages.addErrorMessage(model, "realtime.pricecheck.generic.error.msg");
		}

		return REDIRECT_PREFIX + CART;
	}

	private List<PriceDiffData> handlePriceDifferences(final OrderSimulationData orderSimulationData, final Model model,
			final RedirectAttributes redirectAttributes)
	{
		final List<PriceDiffData> diffDataList = new ArrayList<>();
		for (final OrderSimulationProductData productData : orderSimulationData.getProducts())
		{
			if (productData.getPriceDifference() != null)
			{
				diffDataList.add(productData.getPriceDifference());
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.INFO_MESSAGES_HOLDER, "pricediff.error.message",
						new Object[]
				{ productData.getPriceDifference().getProductName(),
						productData.getPriceDifference().getActualPrice().getFormattedValue(),
						productData.getPriceDifference().getDiscountedPrice().getFormattedValue() });
			}
		}
		return diffDataList;
	}


	/**
	 * Apply voucher in standard way
	 *
	 * @param voucherCode
	 */
	@RequestMapping(value = "/applyVoucher", method = RequestMethod.POST)
	public String applyVoucher(@RequestParam("voucherCode") final String voucherCode,
			@RequestParam("vbackURL") final String vbackURL, final RedirectAttributes model)
	{
		try
		{
			if (!StringUtils.isEmpty(voucherCode))
			{
				voucherFacade.applyVoucher(voucherCode);
				GlobalMessages.addFlashMessage(model, GlobalMessages.INFO_MESSAGES_HOLDER, "vouchers.message.applied", null);
			}
			else
			{
				GlobalMessages.addFlashMessage(model, GlobalMessages.ERROR_MESSAGES_HOLDER, "vouchers.message.empty.msg", null);
			}
		}
		catch (final VoucherOperationException e)
		{
			LOG.error(e.getMessage(), e);
			GlobalMessages.addFlashMessage(model, GlobalMessages.ERROR_MESSAGES_HOLDER, "vouchers.message.code.wrong", null);
		}
		return createRedirect(vbackURL);
	}

	public String createRedirect(String backURL)
	{
		if (!StringUtils.isEmpty(backURL))
		{
			backURL = CART;
		}
		return "redirect:" + backURL;
	}

	/**
	 * Resolve voucher in standard way
	 *
	 * @param voucherCode
	 */
	@RequestMapping(value = "/resolveVoucher", method = RequestMethod.POST)
	public String resolveVoucher(@RequestParam("voucherCode") final String voucherCode,
			@RequestParam("vbackURL") final String vbackURL, final RedirectAttributes model)
	{
		try
		{
			if (!StringUtils.isEmpty(voucherCode))
			{
				voucherFacade.releaseVoucher(voucherCode);
				GlobalMessages.addFlashMessage(model, GlobalMessages.INFO_MESSAGES_HOLDER, "vouchers.message.removed", null);
			}
		}
		catch (final VoucherOperationException e)
		{
			LOG.error(e.getMessage(), e);
			model.addFlashAttribute("exception", e.getMessage());
		}
		return createRedirect(vbackURL);
	}

	public List<VoucherData> getAppliedVouchers()
	{
		final List<VoucherData> vouchers = voucherFacade.getVouchersForCart();
		if (CollectionUtils.isEmpty(vouchers))
		{
			return Collections.emptyList();
		}
		return vouchers;
	}

	/**
	 *
	 * @param b2cProduct
	 * @param b2eProduct
	 * @param product
	 */
	public void setProductShowPrice(final Boolean b2cProduct, final Boolean b2eProduct, final ProductData product)
	{
		if (b2cProduct.booleanValue() || b2eProduct.booleanValue())
		{
			product.setShowPrice(true);
		}
		else
		{
			product.setShowPrice(false);
		}
	}

}

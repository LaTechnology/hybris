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
package com.greenlee.storefront.checkout.steps.validation.impl;


import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.validation.AbstractCheckoutStepValidator;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.validation.ValidationResults;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.util.Config;

import javax.annotation.Resource;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;


public class DefaultMultiStepCheckoutStepValidator extends AbstractCheckoutStepValidator
{
	public static final String DUMMY_UNIT_B2B = Config.getString("greenlee.account.dummy.distributor.uid", "dummydistributor");
	public static final String DUMMY_UNIT_B2E = Config.getString("greenlee.account.dummy.distributor.b2e.uid", "0010000014");

	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;

	@Override
	public ValidationResults validateOnEnter(final RedirectAttributes redirectAttributes)
	{
		final CartData cartData = getCheckoutFacade().getCheckoutCart();
		final CustomerData customerData = customerFacade.getCurrentCustomer();
		if (customerData.getSessionB2BUnit() == null)
		{
			LOG.info("Missing session B2BUnit" + customerData.getUid());
			/*
			 * GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.INFO_MESSAGES_HOLDER,
			 * "customer.account.mapping.required");
			 */
			return ValidationResults.FAILED;
		}
		if (DUMMY_UNIT_B2E.equalsIgnoreCase(customerData.getSessionB2BUnit().getUid())
				&& (customerData.getAccountInformation() != null && !customerData.getAccountInformation().isEmpty()))
		{
			LOG.info("New B2E User trying to do checkout without account mapping" + customerData.getUid());
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.INFO_MESSAGES_HOLDER,
					"customer.account.mapping.required");
			return ValidationResults.FAILED;
		}
		if (DUMMY_UNIT_B2B.equalsIgnoreCase(customerData.getSessionB2BUnit().getUid())
				&& (customerData.getAccountInformation() != null && customerData.getAccountInformation().isEmpty()))
		{
			LOG.info("New B2B User trying to do checkout without account mapping" + customerData.getUid());
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.INFO_MESSAGES_HOLDER,
					"customer.account.mapping.required");
			return ValidationResults.FAILED;
		}
		if (cartData.getEntries() != null && !cartData.getEntries().isEmpty())
		{
			return (getCheckoutFacade().hasShippingItems()) ? ValidationResults.SUCCESS
					: ValidationResults.REDIRECT_TO_PICKUP_LOCATION;
		}
		LOG.info("Missing, empty or unsupported cart");
		return ValidationResults.FAILED;
	}
}

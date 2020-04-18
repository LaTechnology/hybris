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

import org.springframework.web.servlet.mvc.support.RedirectAttributes;


public class DefaultTermsAndConditionCheckoutStepValidator extends AbstractCheckoutStepValidator
{
	@Override
	public ValidationResults validateOnEnter(final RedirectAttributes redirectAttributes)
	{
		return ValidationResults.SUCCESS;
	}

	@Override
	public ValidationResults validateOnExit()
	{
		return ValidationResults.SUCCESS;
	}

}
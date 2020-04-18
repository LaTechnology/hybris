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
package com.greenlee.storefront.util;


import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Displays "confirmation, information, error" messages
 */
public class GreenleeGlobalMessages //extends GlobalMessages
{
	public static final String THANK_MESSAGES_HOLDER = "accThankMsgs";

	public static void accThankMessage(final Model model, final String messageKey)
	{
		GlobalMessages.addMessage(model, THANK_MESSAGES_HOLDER, messageKey, null);
	}

	public static void addFlashMessage(final RedirectAttributes model, final String messageHolder, final String
			messageKey)
	{
		GlobalMessages.addFlashMessage(model,messageHolder,messageKey);
	}
}

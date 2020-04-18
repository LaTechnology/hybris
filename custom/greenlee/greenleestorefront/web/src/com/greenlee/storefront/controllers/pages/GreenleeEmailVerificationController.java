package com.greenlee.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.greenlee.facades.customer.GreenleeCustomerFacade;


/**
 * Controller for verifying the email & activate the account to be used by customer
 */
@Controller
@RequestMapping(value = "/emailVerify")
public class GreenleeEmailVerificationController extends AbstractPageController
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(GreenleeEmailVerificationController.class);

	private static final String REDIRECT_HOME = "redirect:/";

	@Resource(name = "greenleeCustomerFacade")
	private GreenleeCustomerFacade customerFacade;

	@RequestMapping(method = RequestMethod.GET)
	public String getChangePassword(@RequestParam(required = false) final String token, final RedirectAttributes redirectAttributes)
			throws CMSItemNotFoundException
	{
		if (StringUtils.isNotBlank(token))
		{
			try
			{
				customerFacade.verifyUserEmailAddress(token);
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.INFO_MESSAGES_HOLDER,
						"registration.email.verification.success.message.title");
			}
			catch (final TokenInvalidatedException e)
			{
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
						"registration.email.verification.incomplete.message.title");
				LOG.error("Token Mismatched {}", e);
			}

			catch (final IllegalArgumentException ae)
			{
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
						"registration.email.verification.incomplete.message.title");
				LOG.error("Invalid customer account {}", ae);

			}
		}
		return REDIRECT_HOME;

	}
}

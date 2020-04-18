/**
 *
 */
package com.greenlee.core.cart.services;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.commerceservices.order.hook.CommerceCartCalculationMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commerceservices.strategies.CartValidationStrategy;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.greenlee.core.constants.GreenleeCoreConstants;


/**
 * @author raja.santhanam
 *
 */
public class GreenleeCommerceCartCalculationMethodHook implements CommerceCartCalculationMethodHook
{

	private static final Logger LOG = Logger.getLogger(GreenleeCommerceCartCalculationMethodHook.class);

	private CartValidationStrategy validationStrategy;
	private SessionService sessionService;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.commerceservices.order.hook.CommerceCartCalculationMethodHook#afterCalculate(de.hybris.platform
	 * .commerceservices.service.data.CommerceCartParameter)
	 */
	@Override
	public void afterCalculate(final CommerceCartParameter parameter)
	{
		//Nothing for now.
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.commerceservices.order.hook.CommerceCartCalculationMethodHook#beforeCalculate(de.hybris.
	 * platform.commerceservices.service.data.CommerceCartParameter)
	 */
	@Override
	public void beforeCalculate(final CommerceCartParameter parameter)
	{
		final List<CommerceCartModification> modifications = validationStrategy.validateCart(parameter);
		final List<CommerceCartModification> errorModifications = new ArrayList<>();
		if (!modifications.isEmpty())
		{
			for (final CommerceCartModification modification : modifications)
			{
				if (!CommerceCartModificationStatus.SUCCESS.equals(modification.getStatusCode()))
				{
					errorModifications.add(modification);
					if (modification.getEntry() != null && modification.getEntry().getProduct() != null)
					{
						LOG.error("Change in cart:" + modification.getEntry().getProduct().getCode()
								+ "is no longer available for purchase");
					}
				}
			}

			if (sessionService.hasCurrentSession() && !errorModifications.isEmpty())
			{
				List<CommerceCartModification> pastModifications = (List<CommerceCartModification>) sessionService
						.getAttribute(GreenleeCoreConstants.CART_REMOVAL_MESSAGE);
				if (pastModifications == null)
				{
					pastModifications = new ArrayList<CommerceCartModification>();
				}
				pastModifications.addAll(errorModifications);
				sessionService.setAttribute(GreenleeCoreConstants.CART_REMOVAL_MESSAGE, pastModifications);
			}
		}


	}

	/**
	 * @return the validationStrategy
	 */
	public CartValidationStrategy getValidationStrategy()
	{
		return validationStrategy;
	}

	/**
	 * @param validationStrategy
	 *           the validationStrategy to set
	 */
	public void setValidationStrategy(final CartValidationStrategy validationStrategy)
	{
		this.validationStrategy = validationStrategy;
	}

	/**
	 * @return the sessionService
	 */
	public SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}
}

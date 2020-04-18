/**
 *
 */
package com.greenlee.core.order.hook.impl;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.hook.CommerceUpdateCartEntryHook;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.model.ModelService;


/**
 * @author raja.santhanam
 *
 */
public class GreenleeUpdateCartEntryHook implements CommerceUpdateCartEntryHook
{

	private ModelService modelService;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.commerceservices.order.hook.CommerceUpdateCartEntryHook#afterUpdateCartEntry(de.hybris.platform
	 * .commerceservices.service.data.CommerceCartParameter,
	 * de.hybris.platform.commerceservices.order.CommerceCartModification)
	 */
	@Override
	public void afterUpdateCartEntry(final CommerceCartParameter parameter, final CommerceCartModification result)
	{
		final CartModel cart = parameter.getCart();
		if (cart.getSapPriceAvailability() == null || cart.getSapPriceAvailability().booleanValue())
		{
			cart.setSapPriceAvailability(Boolean.FALSE);
			modelService.save(cart);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.commerceservices.order.hook.CommerceUpdateCartEntryHook#beforeUpdateCartEntry(de.hybris.
	 * platform.commerceservices.service.data.CommerceCartParameter)
	 */
	@Override
	public void beforeUpdateCartEntry(final CommerceCartParameter parameter)
	{
		//Do Nothing
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

}

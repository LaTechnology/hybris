/**
 *
 */
package com.greenlee.core.order.hook.impl;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.hook.CommerceAddToCartMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.model.ModelService;


/**
 * @author raja.santhanam
 *
 */
public class GreenleeAddToCartHook implements CommerceAddToCartMethodHook
{

	private ModelService modelService;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.commerceservices.order.hook.CommerceAddToCartMethodHook#beforeAddToCart(de.hybris.platform.
	 * commerceservices.service.data.CommerceCartParameter)
	 */
	@Override
	public void beforeAddToCart(final CommerceCartParameter parameters) throws CommerceCartModificationException
	{
		//Do Nothing
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.commerceservices.order.hook.CommerceAddToCartMethodHook#afterAddToCart(de.hybris.platform.
	 * commerceservices.service.data.CommerceCartParameter,
	 * de.hybris.platform.commerceservices.order.CommerceCartModification)
	 */
	@Override
	public void afterAddToCart(final CommerceCartParameter parameters, final CommerceCartModification result)
			throws CommerceCartModificationException
	{
		final CartModel cart = parameters.getCart();
		if (cart.getSapPriceAvailability() == null || cart.getSapPriceAvailability().booleanValue())
		{
			cart.setSapPriceAvailability(Boolean.FALSE);
			modelService.save(cart);
		}
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

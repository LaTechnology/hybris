package com.greenlee.core.cart.strategies.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceUpdateCartEntryStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;

import com.greenlee.core.cart.strategies.GreenleeCommerceUpdateCartEntryStrategy;


/**
 * A strategy to update a cart entry for serial number.
 *
 */
public class GreenleeCommerceUpdateCartEntryStrategyImpl extends DefaultCommerceUpdateCartEntryStrategy implements
		GreenleeCommerceUpdateCartEntryStrategy
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.greenlee.core.cart.strategy.GreenleeCommerceUpdateCartEntryStrategy#updateSerialNoForCartEntry(de.hybris.platform
	 * .commerceservices.service.data.CommerceCartParameter)
	 */
	@Override
	public CommerceCartModification updateSerialNoForCartEntry(final CommerceCartParameter parameters)
			throws CommerceCartModificationException
	{
		final CartModel cartModel = parameters.getCart();
		final long newQuantity = parameters.getQuantity();

		validateParameterNotNull(cartModel, "Cart model cannot be null");

		final AbstractOrderEntryModel entryToUpdate = getEntryForNumber(cartModel, (int) parameters.getEntryNumber());
		final CommerceCartModification modification = new CommerceCartModification();

		validateEntryBeforeModification(newQuantity, entryToUpdate);
		entryToUpdate.setWarrantySerialNumbers(parameters.getSerialNumbers());

		getModelService().save(entryToUpdate);
		getModelService().refresh(cartModel);
		getCommerceCartCalculationStrategy().calculateCart(cartModel);

		modification.setStatusCode(CommerceCartModificationStatus.SUCCESS);
		modification.setEntry(entryToUpdate);
		return modification;
	}
}

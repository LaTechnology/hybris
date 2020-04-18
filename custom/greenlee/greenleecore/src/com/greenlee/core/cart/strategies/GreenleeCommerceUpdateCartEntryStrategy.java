package com.greenlee.core.cart.strategies;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;


public interface GreenleeCommerceUpdateCartEntryStrategy
{
	CommerceCartModification updateSerialNoForCartEntry(final CommerceCartParameter parameters)
			throws CommerceCartModificationException;
}

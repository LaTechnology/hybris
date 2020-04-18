package com.greenlee.core.cart.services;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;


/**
 * @author dipankan
 *
 */
public interface GreenleeCommerceCartService
{
	CommerceCartModification updateSerialNoForCartEntry(final CommerceCartParameter parameter)
			throws CommerceCartModificationException;
}

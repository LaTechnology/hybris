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
package com.greenlee.core.cart.services;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;

import com.greenlee.core.cart.strategies.GreenleeCommerceUpdateCartEntryStrategy;


/**
 * Extension of default commerce cart service
 */
public class GreenleeCommerceCartServiceImpl extends DefaultCommerceCartService implements GreenleeCommerceCartService
{

	private GreenleeCommerceUpdateCartEntryStrategy greenleeCommerceUpdateCartEntryStrategy;

	@Override
	public CommerceCartModification updateSerialNoForCartEntry(final CommerceCartParameter parameters)
			throws CommerceCartModificationException
	{
		return this.getGreenleeCommerceUpdateCartEntryStrategy().updateSerialNoForCartEntry(parameters);
	}

	/**
	 * @return the greenleeCommerceUpdateCartEntryStrategy
	 */
	public GreenleeCommerceUpdateCartEntryStrategy getGreenleeCommerceUpdateCartEntryStrategy()
	{
		return greenleeCommerceUpdateCartEntryStrategy;
	}

	/**
	 * @param greenleeCommerceUpdateCartEntryStrategy
	 *           the greenleeCommerceUpdateCartEntryStrategy to set
	 */
	public void setGreenleeCommerceUpdateCartEntryStrategy(
			final GreenleeCommerceUpdateCartEntryStrategy greenleeCommerceUpdateCartEntryStrategy)
	{
		this.greenleeCommerceUpdateCartEntryStrategy = greenleeCommerceUpdateCartEntryStrategy;
	}

}

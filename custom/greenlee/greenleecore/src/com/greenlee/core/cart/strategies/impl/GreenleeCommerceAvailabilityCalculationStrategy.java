/**
 *
 */
package com.greenlee.core.cart.strategies.impl;

import de.hybris.platform.commerceservices.stock.strategies.impl.DefaultCommerceAvailabilityCalculationStrategy;
import de.hybris.platform.ordersplitting.model.StockLevelModel;

import java.util.Collection;


/**
 * @author raja.santhanam
 *
 */
public class GreenleeCommerceAvailabilityCalculationStrategy extends DefaultCommerceAvailabilityCalculationStrategy
{

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.commerceservices.stock.strategies.CommerceAvailabilityCalculationStrategy#calculateAvailability
	 * (java.util.Collection)
	 */
	@Override
	public Long calculateAvailability(final Collection<StockLevelModel> stockLevels)
	{
		if (stockLevels == null || stockLevels.isEmpty())
		{
			// Equivalent to force in stock.
			return new Long(Long.MAX_VALUE);
		}
		return super.calculateAvailability(stockLevels);
	}

}

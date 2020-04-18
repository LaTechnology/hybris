/**
 *
 */
package com.greenlee.core.stock.strategies.impl;

import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commerceservices.stock.strategies.CommerceAvailabilityCalculationStrategy;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.stock.strategy.StockLevelStatusStrategy;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Required;


/**
 *
 */
public class GreenleeCommerceStockLevelStatusStrategy extends AbstractBusinessService implements StockLevelStatusStrategy
{
	private int lowStockThreshold = 5;
	private CommerceAvailabilityCalculationStrategy commerceStockLevelCalculationStrategy;

	@Override
	public StockLevelStatus checkStatus(final StockLevelModel stockLevel)
	{
		StockLevelStatus resultStatus = StockLevelStatus.INSTOCK;
		if (stockLevel == null)
		{
			resultStatus = StockLevelStatus.INSTOCK;
		}
		else if (InStockStatus.FORCEINSTOCK.equals(stockLevel.getInStockStatus()))
		{
			resultStatus = StockLevelStatus.INSTOCK;
		}
		else if (InStockStatus.FORCEOUTOFSTOCK.equals(stockLevel.getInStockStatus()))
		{
			resultStatus = StockLevelStatus.INSTOCK;
		}
		else
		{
			final int result = getCommerceStockLevelCalculationStrategy()
					.calculateAvailability(Collections.singletonList(stockLevel)).intValue();
			if (result <= 0)
			{
				resultStatus = StockLevelStatus.INSTOCK;
			}
			else if (result > getDefaultLowStockThreshold())
			{
				resultStatus = StockLevelStatus.INSTOCK;
			}
			else
			{
				resultStatus = StockLevelStatus.INSTOCK;
			}
		}

		return resultStatus;
	}

	@Override
	public StockLevelStatus checkStatus(final Collection<StockLevelModel> stockLevels)
	{
		StockLevelStatus resultStatus = StockLevelStatus.INSTOCK;
		StockLevelStatus tmpStatus = StockLevelStatus.INSTOCK;

		for (final StockLevelModel level : stockLevels)
		{
			tmpStatus = checkStatus(level);
			if (StockLevelStatus.INSTOCK == tmpStatus)
			{
				resultStatus = tmpStatus;
				break;
			}
			else if (StockLevelStatus.LOWSTOCK == tmpStatus)
			{
				resultStatus = tmpStatus;
			}
		}

		return resultStatus;
	}

	protected int getDefaultLowStockThreshold()
	{
		return lowStockThreshold;
	}

	public void setDefaultLowStockThreshold(final int lowStockThreshold)
	{
		this.lowStockThreshold = lowStockThreshold;
	}

	protected CommerceAvailabilityCalculationStrategy getCommerceStockLevelCalculationStrategy()
	{
		return commerceStockLevelCalculationStrategy;
	}

	@Required
	public void setCommerceStockLevelCalculationStrategy(
			final CommerceAvailabilityCalculationStrategy commerceStockLevelCalculationStrategy)
	{
		this.commerceStockLevelCalculationStrategy = commerceStockLevelCalculationStrategy;
	}
}
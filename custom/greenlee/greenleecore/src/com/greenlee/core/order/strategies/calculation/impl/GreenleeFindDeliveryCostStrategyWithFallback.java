/**
 *
 */
package com.greenlee.core.order.strategies.calculation.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.order.strategies.calculation.FindDeliveryCostStrategy;
import de.hybris.platform.util.PriceValue;

import org.apache.log4j.Logger;


/**
 * @author raja.santhanam
 *
 */
public class GreenleeFindDeliveryCostStrategyWithFallback implements FindDeliveryCostStrategy
{

	private static final Logger LOG = Logger.getLogger(GreenleeFindDeliveryCostStrategyWithFallback.class);

	private FindDeliveryCostStrategy fallbackFindDeliveryCostStrategy;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.order.strategies.calculation.FindDeliveryCostStrategy#getDeliveryCost(de.hybris.platform.core.
	 * model.order.AbstractOrderModel)
	 */
	@Override
	public PriceValue getDeliveryCost(final AbstractOrderModel order)
	{
		if (order.getSapPriceAvailability() != null && order.getSapPriceAvailability().booleanValue())
		{
			LOG.info("Real time price SAP already available, hence using the delivery price from SAP");
			return new PriceValue(order.getCurrency().getIsocode(), order.getDeliveryCost().doubleValue(),
					order.getNet().booleanValue());
		}
		else
		{
			LOG.info("Real time price SAP not available, hence using the delivery price from fallback");
			return fallbackFindDeliveryCostStrategy.getDeliveryCost(order);
		}
	}

	/**
	 * @return the fallbackFindDeliveryCostStrategy
	 */
	public FindDeliveryCostStrategy getFallbackFindDeliveryCostStrategy()
	{
		return fallbackFindDeliveryCostStrategy;
	}

	/**
	 * @param fallbackFindDeliveryCostStrategy
	 *           the fallbackFindDeliveryCostStrategy to set
	 */
	public void setFallbackFindDeliveryCostStrategy(final FindDeliveryCostStrategy fallbackFindDeliveryCostStrategy)
	{
		this.fallbackFindDeliveryCostStrategy = fallbackFindDeliveryCostStrategy;
	}

}

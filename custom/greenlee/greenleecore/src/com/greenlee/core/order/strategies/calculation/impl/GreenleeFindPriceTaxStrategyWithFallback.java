/**
 *
 */
package com.greenlee.core.order.strategies.calculation.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.strategies.calculation.FindPriceStrategy;
import de.hybris.platform.order.strategies.calculation.FindTaxValuesStrategy;
import de.hybris.platform.util.PriceValue;
import de.hybris.platform.util.TaxValue;

import java.util.Collection;

import org.apache.log4j.Logger;


/**
 * @author raja.santhanam
 *
 */
public class GreenleeFindPriceTaxStrategyWithFallback implements FindPriceStrategy, FindTaxValuesStrategy
{
	private static final Logger LOG = Logger.getLogger(GreenleeFindPriceTaxStrategyWithFallback.class);

	private FindPriceStrategy fallbackFindPriceStrategy;
	private FindTaxValuesStrategy fallbackFindTaxStrategy;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.order.strategies.calculation.FindPriceStrategy#findBasePrice(de.hybris.platform.core.model.
	 * order.AbstractOrderEntryModel)
	 */
	@Override
	public PriceValue findBasePrice(final AbstractOrderEntryModel entry) throws CalculationException
	{
		if (entry.getOrder().getSapPriceAvailability() != null && entry.getOrder().getSapPriceAvailability().booleanValue())
		{
			LOG.info("Real time price SAP already available, hence using the price from SAP");
			return new PriceValue(entry.getOrder().getCurrency().getIsocode(), entry.getBasePrice().doubleValue(),
					entry.getOrder().getNet().booleanValue());
		}
		else
		{
			LOG.info("Real time price SAP not available, hence using the price from fallback");
			return fallbackFindPriceStrategy.findBasePrice(entry);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.order.strategies.calculation.FindTaxValuesStrategy#findTaxValues(de.hybris.platform.core.model.
	 * order.AbstractOrderEntryModel)
	 */
	@Override
	public Collection<TaxValue> findTaxValues(final AbstractOrderEntryModel entry) throws CalculationException
	{
		if (entry.getOrder().getSapPriceAvailability() != null && entry.getOrder().getSapPriceAvailability().booleanValue())
		{
			LOG.info("Real time price SAP already available, hence using the tax from SAP");
			return entry.getTaxValues();
		}
		else
		{
			LOG.info("Real time price SAP not available, hence using the tax from fallback");
			return fallbackFindTaxStrategy.findTaxValues(entry);
		}
	}


	/**
	 * @return the fallbackFindPriceStrategy
	 */
	public FindPriceStrategy getFallbackFindPriceStrategy()
	{
		return fallbackFindPriceStrategy;
	}

	/**
	 * @param fallbackFindPriceStrategy
	 *           the fallbackFindPriceStrategy to set
	 */
	public void setFallbackFindPriceStrategy(final FindPriceStrategy fallbackFindPriceStrategy)
	{
		this.fallbackFindPriceStrategy = fallbackFindPriceStrategy;
	}

	/**
	 * @return the fallbackFindTaxStrategy
	 */
	public FindTaxValuesStrategy getFallbackFindTaxStrategy()
	{
		return fallbackFindTaxStrategy;
	}

	/**
	 * @param fallbackFindTaxStrategy
	 *           the fallbackFindTaxStrategy to set
	 */
	public void setFallbackFindTaxStrategy(final FindTaxValuesStrategy fallbackFindTaxStrategy)
	{
		this.fallbackFindTaxStrategy = fallbackFindTaxStrategy;
	}


}

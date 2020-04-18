/**
 *
 */
package com.greenlee.core.commerceservices.strategies;

import de.hybris.platform.commerceservices.strategies.DeliveryModeLookupStrategy;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.greenlee.core.model.GreenleeProductModel;
import com.greenlee.core.order.daos.CountryOrderWeightZoneDeliveryModeDao;


/**
 * @author raja.santhanam
 *
 */
public class GreenleeDeliveryModeLookupStrategy implements DeliveryModeLookupStrategy
{


	private CountryOrderWeightZoneDeliveryModeDao znDeliveryModeDao;

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.commerceservices.strategies.impl.DefaultDeliveryModeLookupStrategy#
	 * getSelectableDeliveryModesForOrder(de.hybris.platform.core.model.order.AbstractOrderModel)
	 */
	@Override
	public List<DeliveryModeModel> getSelectableDeliveryModesForOrder(final AbstractOrderModel abstractOrderModel)
	{
		final List<DeliveryModeModel> deliveryModes = new ArrayList<>();
		deliveryModes.addAll(
				znDeliveryModeDao.findDeliveryModesByWeight(abstractOrderModel, calculateTotalWeightOfOrder(abstractOrderModel)));
		return deliveryModes;
	}

	private Double calculateTotalWeightOfOrder(final AbstractOrderModel abstractOrderModel)
	{
		double totalWeight = 0.0;
		for (final AbstractOrderEntryModel entry : abstractOrderModel.getEntries())
		{
			final GreenleeProductModel product = (GreenleeProductModel) entry.getProduct();
			final double weightOfProduct = product.getWeight() != null ? product.getWeight().doubleValue() : 0;
			totalWeight += weightOfProduct * entry.getQuantity().doubleValue();
		}
		return Double.valueOf(totalWeight);
	}

	/**
	 * @return the znDeliveryModeDao
	 */
	public CountryOrderWeightZoneDeliveryModeDao getZnDeliveryModeDao()
	{
		return znDeliveryModeDao;
	}

	/**
	 * @param znDeliveryModeDao
	 *           the znDeliveryModeDao to set
	 */
	@Required
	public void setZnDeliveryModeDao(final CountryOrderWeightZoneDeliveryModeDao znDeliveryModeDao)
	{
		this.znDeliveryModeDao = znDeliveryModeDao;
	}

}

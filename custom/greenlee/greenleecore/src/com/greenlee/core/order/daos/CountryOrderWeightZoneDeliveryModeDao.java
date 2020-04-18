package com.greenlee.core.order.daos;

import de.hybris.platform.commerceservices.delivery.dao.CountryZoneDeliveryModeDao;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;

import java.util.Collection;


/**
 * @author raja.santhanam
 *
 */
public interface CountryOrderWeightZoneDeliveryModeDao extends CountryZoneDeliveryModeDao
{
	public Collection<DeliveryModeModel> findDeliveryModesByWeight(final AbstractOrderModel abstractOrder,
			Double totalWeightOfOrder);
}

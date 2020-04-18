/**
 *
 */
package com.greenlee.core.order.daos.impl;

import de.hybris.platform.commerceservices.delivery.dao.impl.DefaultCountryZoneDeliveryModeDao;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;
import de.hybris.platform.jalo.link.Link;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.greenlee.core.order.daos.CountryOrderWeightZoneDeliveryModeDao;


/**
 * @author raja.santhanam
 *
 */
public class DefaultCountryOrderWeightZoneDeliveryModeDao extends DefaultCountryZoneDeliveryModeDao
		implements CountryOrderWeightZoneDeliveryModeDao

{

	private static final String ZONE_COUNTRY_RELATION = "ZoneCountryRelation";
	private static final String STORE_TO_DELIVERY_MODE_RELATION = "BaseStore2DeliveryModeRel";


	@Override
	public Collection<DeliveryModeModel> findDeliveryModesByWeight(final AbstractOrderModel abstractOrder,
			final Double totalWeightOfOrder)
	{
		final StringBuilder query = new StringBuilder("SELECT DISTINCT {zdm:").append(ItemModel.PK).append("}");
		query.append(" FROM { ").append(ZoneDeliveryModeValueModel._TYPECODE).append(" AS val");
		query.append(" JOIN ").append(ZoneDeliveryModeModel._TYPECODE).append(" AS zdm");
		query.append(" ON {val:").append(ZoneDeliveryModeValueModel.DELIVERYMODE).append("}={zdm:").append(ItemModel.PK)
				.append('}');
		query.append(" JOIN ").append(ZONE_COUNTRY_RELATION).append(" AS z2c");
		query.append(" ON {val:").append(ZoneDeliveryModeValueModel.ZONE).append("}={z2c:").append(Link.SOURCE).append('}');
		query.append(" JOIN ").append(STORE_TO_DELIVERY_MODE_RELATION).append(" AS s2d");
		query.append(" ON {val:").append(ZoneDeliveryModeValueModel.DELIVERYMODE).append("}={s2d:").append(Link.TARGET).append('}');
		//query.append(" } WHERE {val:").append(ZoneDeliveryModeValueModel.CURRENCY).append("}=?currency");
		query.append(" } WHERE {s2d:").append(Link.SOURCE).append("}=?store");
		if (abstractOrder.getDeliveryAddress() != null)
		{
			query.append(" AND {z2c:").append(Link.TARGET).append("}=?deliveryCountry");
		}
		//query.append(" AND {s2d:").append(Link.SOURCE).append("}=?store");
		query.append(" AND {zdm:").append(ZoneDeliveryModeModel.NET).append("}=?net");
		query.append(" AND {zdm:").append(ZoneDeliveryModeModel.ACTIVE).append("}=?active");
		/*query.append(" AND NVL({zdm:").append(ZoneDeliveryModeModel.WEIGHTLOWERLIMIT).append("},0) <= ?totalWeightOfOrder");
		query.append(" AND NVL({zdm:").append(ZoneDeliveryModeModel.WEIGHTUPPERLIMIT).append("},").append(Integer.MAX_VALUE)
				.append(") >= ?totalWeightOfOrder");*/

		final Map<String, Object> params = new HashMap<>();
		if (abstractOrder.getDeliveryAddress() != null)
		{
			params.put("deliveryCountry", abstractOrder.getDeliveryAddress().getCountry());
		}
		//params.put("currency", abstractOrder.getCurrency());
		params.put("net", abstractOrder.getNet());
		params.put("active", Boolean.TRUE);
		params.put("store", abstractOrder.getStore());
//		params.put("totalWeightOfOrder", totalWeightOfOrder);

		return doSearch(query.toString(), params, DeliveryModeModel.class);

	}

}

package com.greenlee.sapintegration.orderexchange.outbound.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;




import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.sap.orderexchange.constants.OrderCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.SalesConditionCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.SaporderexchangeConstants;
import de.hybris.platform.sap.orderexchange.outbound.impl.DefaultSalesConditionsContributor;
import de.hybris.platform.util.TaxValue;

public class DefaultGreenleeSalesConditionsContributor extends
		DefaultSalesConditionsContributor {
	
	private String couponDiscountCode;
	
	
	public String getCouponDiscountCode() {
		return couponDiscountCode;
	}

	public void setCouponDiscountCode(String couponDiscountCode) {
		this.couponDiscountCode = couponDiscountCode;
	}

	@Override
	public List<Map<String, Object>> createRows(final OrderModel order)
	{
		List<Map<String, Object>> rows = new ArrayList<>();
		if(order.getTotalDiscounts() != null && order.getTotalDiscounts().doubleValue() > 0){
			final Map<String, Object> row = new HashMap<>();
			row.put(OrderCsvColumns.ORDER_ID, order.getCode());
			row.put(SalesConditionCsvColumns.CONDITION_ENTRY_NUMBER, SaporderexchangeConstants.HEADER_ENTRY);
			row.put(SalesConditionCsvColumns.CONDITION_CODE, getCouponDiscountCode());
			row.put(SalesConditionCsvColumns.CONDITION_VALUE, order.getTotalDiscounts());
			row.put(SalesConditionCsvColumns.CONDITION_CURRENCY_ISO_CODE, order.getCurrency().getIsocode());
			row.put(SalesConditionCsvColumns.CONDITION_COUNTER, "1");
			row.put(SalesConditionCsvColumns.ABSOLUTE, Boolean.TRUE);
			rows.add(row);
		}
		return rows;
	}

}

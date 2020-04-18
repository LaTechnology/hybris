/**
 * 
 */
package com.greenlee.sapintegration.orderexchange.outbound.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.sap.orderexchange.constants.OrderCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.OrderEntryCsvColumns;
import de.hybris.platform.sap.orderexchange.outbound.RawItemContributor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.greenlee.sapintegration.orderexchange.constants.GreenleeSDTextCsvColumns;


/**
 * @author nalini.ramarao
 * 
 */
public class DefaultGreenleeOrderSDTextContributor implements RawItemContributor<OrderModel>
{

	private String shippingNotesCode;
	
	public String getShippingNotesCode() {
		return shippingNotesCode;
	}

	public void setShippingNotesCode(String shippingNotesCode) {
		this.shippingNotesCode = shippingNotesCode;
	}

	public String getWarrentyProductCode() {
		return warrentyProductCode;
	}

	public void setWarrentyProductCode(String warrentyProductCode) {
		this.warrentyProductCode = warrentyProductCode;
	}

	private String warrentyProductCode;
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.sap.orderexchange.outbound.RawItemContributor#getColumns()
	 */
	@Override
	public Set<String> getColumns()
	{
		return new HashSet<>(Arrays.asList(GreenleeSDTextCsvColumns.TEXT_ID, GreenleeSDTextCsvColumns.TEXT_LINE));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.sap.orderexchange.outbound.RawItemContributor#createRows(de.hybris.platform.servicelayer.model
	 * .AbstractItemModel)
	 */
	@Override
	public List<Map<String, Object>> createRows(final OrderModel order)
	{
		final List<Map<String, Object>> result = new ArrayList<>();
		createShippingNotesRows(order, result);
		createWarrentyProductRows(order, result);
		return result;
	}

	public void createShippingNotesRows(final OrderModel order, final List<Map<String, Object>> result)
	{
		if (!StringUtils.isEmpty(order.getShippingNote()))
		{
			final Map<String, Object> row = new HashMap<>();
			row.put(OrderCsvColumns.ORDER_ID, order.getCode());
			row.put(GreenleeSDTextCsvColumns.TEXT_ID, getShippingNotesCode());
			row.put(GreenleeSDTextCsvColumns.TEXT_LINE, order.getShippingNote());
			result.add(row);
		}
	}

	public void createWarrentyProductRows(final OrderModel order, final List<Map<String, Object>> result)
	{
		final List<AbstractOrderEntryModel> entries = order.getEntries();

		for (final AbstractOrderEntryModel entry : entries)
		{
			if (!StringUtils.isEmpty(entry.getWarrantySerialNumbers()))
			{
				final Map<String, Object> row = new HashMap<>();
				row.put(OrderCsvColumns.ORDER_ID, order.getCode());
				row.put(OrderEntryCsvColumns.ENTRY_NUMBER, entry.getEntryNumber());
				row.put(GreenleeSDTextCsvColumns.TEXT_ID, getWarrentyProductCode());
				row.put(GreenleeSDTextCsvColumns.TEXT_LINE, entry.getWarrantySerialNumbers());
				result.add(row);
			}
		}
	}
}

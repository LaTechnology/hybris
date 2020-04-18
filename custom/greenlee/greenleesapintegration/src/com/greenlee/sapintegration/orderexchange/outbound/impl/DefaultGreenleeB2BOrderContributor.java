/**
 * 
 */
package com.greenlee.sapintegration.orderexchange.outbound.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greenlee.core.model.GreenleeB2BCustomerModel;
import com.greenlee.core.util.GreenleeUtils;
import com.greenlee.sapintegration.orderexchange.constants.GreenleeOrderCsvColumns;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.sap.orderexchangeb2b.outbound.impl.DefaultB2BOrderContributor;

/**
 * @author nalini.ramarao
 * 
 */
public class DefaultGreenleeB2BOrderContributor extends
		DefaultB2BOrderContributor {
	
	private static final Logger logger = LoggerFactory.getLogger(DefaultGreenleeB2BOrderContributor.class);

	private String poType;
	
	private String incoterms1;

	public String getIncoterms1() {
		return incoterms1;
	}

	public void setIncoterms1(String incoterms1) {
		this.incoterms1 = incoterms1;
	}

	public String getPoType() {
		return poType;
	}

	public void setPoType(String poType) {
		this.poType = poType;
	}

	@SuppressWarnings("javadoc")
	/*public DefaultGreenleeB2BOrderContributor() {
		super();
		getColumns().addAll(
				Arrays.asList(GreenleeOrderCsvColumns.SALES_ORG,
						GreenleeOrderCsvColumns.ORDERTYPE,
						GreenleeOrderCsvColumns.POTYPE,
						GreenleeOrderCsvColumns.INCOTERMS1,
						GreenleeOrderCsvColumns.INCOTERMS2,
						GreenleeOrderCsvColumns.SHIPBYDATE,
						GreenleeOrderCsvColumns.ORDERER));

	}*/

	@Override
	public List<Map<String, Object>> createRows(final OrderModel model) {
		final List<Map<String, Object>> rows = super.createRows(model);
		return enhanceRows(model, rows);
	}

	protected List<Map<String, Object>> enhanceRows(
			final OrderModel model, final List<Map<String, Object>> rows) {
		// There is only one row on order level
		final Map<String, Object> row = rows.get(0);
		if(null==model.getUnit()){
			GreenleeB2BCustomerModel  customerModel=(GreenleeB2BCustomerModel)model.getUser();
			if(customerModel.getDefaultB2BUnit()!=null){
				String salesOrg = GreenleeUtils.getSalesOrgFromB2BUnit(customerModel.getDefaultB2BUnit());
				row.put(GreenleeOrderCsvColumns.SALES_ORG, salesOrg);
				String orderType = GreenleeUtils.getSAPOrderType(salesOrg);
				row.put(GreenleeOrderCsvColumns.ORDERTYPE,orderType);
				logger.error("GreenleeB2BCustomerModel >> SALES ORG [ " +salesOrg +" ] OrderType [ "+orderType+" ]");
			}
		}else{
			String salesOrg = GreenleeUtils.getSalesOrgFromB2BUnit(model.getUnit());
			row.put(GreenleeOrderCsvColumns.SALES_ORG, salesOrg);
			String orderType = GreenleeUtils.getSAPOrderType(salesOrg);
			row.put(GreenleeOrderCsvColumns.ORDERTYPE,orderType);
			logger.error("SALES ORG [ " +salesOrg +" ] OrderType [ "+orderType+" ]");
		}
		
		row.put(GreenleeOrderCsvColumns.POTYPE, getPoType());
		if (StringUtils.isNotBlank(model.getShipmentAccountNumber()))
		{
			row.put(GreenleeOrderCsvColumns.INCOTERMS1, getIncoterms1());
			row.put(GreenleeOrderCsvColumns.INCOTERMS2, model.getShipmentAccountNumber());
		}
		if(model.getShippedByDate() != null){
			row.put(GreenleeOrderCsvColumns.SHIPBYDATE, model.getShippedByDate());
		}
		row.put(GreenleeOrderCsvColumns.ORDERER, model.getUser().getName());
		return rows;
	}
}

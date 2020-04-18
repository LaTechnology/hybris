/**
 * 
 */
package com.greenlee.sapintegration.orderexchange.datahub.inbound;

import java.util.Map;

/**
 * @author nalini.ramarao
 * 
 */
public interface GreenleeDataHubInboundDeliveryHelper {
	void processDeliveryAndGoodsIssue(final String orderCode,
			final String warehouseId, final String goodsIssueDate,
			final String deliveryNumber, final String[] orderEntryNumber,
			final Map<Integer, Double> qtyMap, final Double consignmentValue,
			final Double taxBreakUp, final Double dutyBreakUp,
			final Double promotionBreakUp, final Double freightBreakUp,
			final String trackingNumber, final String trackings);
 
	final String[] fields = new String[] { "plant", "deliveryNumber",
			"delvryEntryNumber", "delQuantity", "consignmentValue",
			"taxBreakUp", "dutyBreakUp", "promotionBreakUp", "freightBreakUp",
			"goodsIssueDate", "trackingNumber", "trackingInfos" };

	final String PLANT = "plant";
	final String DELIVERY_NUMBER = "deliveryNumber";
	final String DELIVERY_ENTRY_NUMBER = "delvryEntryNumber";

	final String DEL_QTY = "delQuantity";
	final String CONSIGNMENT_VALUE = "consignmentValue";
	final String TAX_BREAKUP = "taxBreakUp";

	final String DUTY_BREAKUP = "dutyBreakUp";
	final String PROMOTION_BREAKUP = "promotionBreakUp";
	final String FREIGHT_BREAKUP = "freightBreakUp";

	final String GOODISSUES_DATE = "goodsIssueDate";
	final String TRACKING_NUMBER = "trackingNumber";
	final String TRACKING_NUMBER_INFO = "trackingInfos";

	/**
	 * @param deliveryInfo
	 * @return delivery number on provided delivery information from Data Hub
	 */
	String determineDeliveryNumberFromDatahub(String deliveryInfo);

	/**
	 * @param deliveryInfo
	 * @return warehouse ID based on provided delivery information from Data Hub
	 */
	String determineWarehouseIdFromDatahub(String deliveryInfo);

	/**
	 * @param deliveryInfo
	 * @return goods issue date based on provided delivery information from Data
	 *         Hub
	 */
	String determineGoodsIssueDateFromDatahub(String deliveryInfo);

	/**
	 * @param deliveryInfo
	 * @return tracking number based on provided delivery information from Data
	 *         Hub
	 */
	String determineTrackingNumberFromDatahub(String deliveryInfo);

	/**
	 * @param deliveryInfo
	 * @return tracking number based on provided delivery information from Data
	 *         Hub
	 */
	String determineOrderEntryNumberFromDatahub(String deliveryInfo);

	/**
	 * @param deliveryInfo
	 * @return tracking number based on provided delivery information from Data
	 *         Hub
	 */
	Double determineQuantityFromDatahub(String deliveryInfo);

	/**
	 * @param deliveryInfo
	 * @return consignment value based on provided delivery information from
	 *         Data Hub
	 */
	Double determineConsignmentValueFromDatahub(String deliveryInfo);

	/**
	 * @param deliveryInfo
	 * @return tax break-up based on provided delivery information from Data Hub
	 */
	Double determineTaxBreakUpFromDatahub(String deliveryInfo);

	/**
	 * @param deliveryInfo
	 * @return duty break-up based on provided delivery information from Data
	 *         Hub
	 */
	Double determineDutyBreakUpFromDatahub(String deliveryInfo);

	/**
	 * @param deliveryInfo
	 * @return promotion break-up based on provided delivery information from
	 *         Data Hub
	 */
	Double determinePromotionBreakUpFromDatahub(String deliveryInfo);

	/**
	 * @param deliveryInfo
	 * @return freight break-up based on provided delivery information from Data
	 *         Hub
	 */
	Double determineFreightBreakUpFromDatahub(String deliveryInfo);

	/**
	 * @param deliveryInfo
	 * @return tracking number based on provided delivery information from Data
	 *         Hub, It contains array of tracking number send by SAP ERP
	 */
	String determineTrackingInfoFromDatahub(String deliveryInfo);

}

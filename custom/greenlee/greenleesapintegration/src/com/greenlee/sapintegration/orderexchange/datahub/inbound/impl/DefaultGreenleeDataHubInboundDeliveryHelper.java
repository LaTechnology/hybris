/**
 * 
 */
package com.greenlee.sapintegration.orderexchange.datahub.inbound.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.google.common.base.Strings;
import com.greenlee.sapintegration.orderexchange.datahub.inbound.GreenleeDataHubInboundDeliveryHelper;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.ConsignmentCreationException;
import de.hybris.platform.ordersplitting.ConsignmentService;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.sap.orderexchange.constants.DataHubInboundConstants;
import de.hybris.platform.sap.orderexchange.datahub.inbound.impl.DefaultDataHubInboundDeliveryHelper;
import de.hybris.platform.servicelayer.event.EventService;

/**
 * @author nalini.ramarao
 * 
 */
public class DefaultGreenleeDataHubInboundDeliveryHelper extends
		DefaultDataHubInboundDeliveryHelper implements
		GreenleeDataHubInboundDeliveryHelper {

	public static final String SEND_DELIVERY_EMAIL_PROCESS = "sendDeliveryEmailProcess";
	public static final String DATAHUB_INBOUND_CONSTANTS_STR = DataHubInboundConstants.POUND_SIGN;
	private int[] deliveryInfosLength = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
	private Logger logger = LoggerFactory
			.getLogger(DefaultGreenleeDataHubInboundDeliveryHelper.class);

	private ConsignmentService consignmentService;
	private EventService eventService;

	@Override
	@SuppressWarnings("javadoc")
	public ConsignmentService getConsignmentService() {
		return consignmentService;
	}

	@Override
	@SuppressWarnings("javadoc")
	public void setConsignmentService(
			final ConsignmentService consignmentService) {
		this.consignmentService = consignmentService;
	}

	@Override
	public void processDeliveryAndGoodsIssue(String orderCode,
			String warehouseId, String goodsIssueDate, String deliveryNumber,
			String[] orderEntryNumber, final Map<Integer, Double> qtyMap, Double consignmentValue,
			Double taxBreakUp, Double dutyBreakUp, Double promotionBreakUp,
			Double freightBreakUp, String trackingNumber, String trackings) {
		final OrderModel order = readOrder(orderCode);
		if (goodsIssueDate == null) {
			logger.error("step 1 - Non PGI: processDeliveryCreation for the consigment code is [ "
					+ deliveryNumber + "] Order #: "+orderCode);
			processDeliveryCreation(warehouseId, goodsIssueDate, order,
					deliveryNumber, orderEntryNumber, qtyMap, consignmentValue,
					taxBreakUp, dutyBreakUp, promotionBreakUp, freightBreakUp,
					trackingNumber, trackings);
		} else {
			logger.error("step 2 - PGI: processPGIDeliveryGoodsIssue for the consigment code is [ "
					+ deliveryNumber
					+ " Order Number "
					+ orderCode
					+ " Tracking Number Info [ "
					+ trackings
					+ " ]");
			processDeliveryGoodsIssue(warehouseId, goodsIssueDate, order,
					deliveryNumber, orderEntryNumber, qtyMap, consignmentValue,
					taxBreakUp, dutyBreakUp, promotionBreakUp, freightBreakUp,
					trackingNumber, trackings);
		}
	}

	protected Map<String, ConsignmentModel> getAllConsignmentFromOrder(
			final OrderModel order) {
		Map<String, ConsignmentModel> map = new LinkedHashMap<String, ConsignmentModel>();
		for (final ConsignmentModel consignmentModel : order.getConsignments()) {
			map.put(consignmentModel.getCode().trim(), consignmentModel);
		}
		logger.debug("getAllConsignmentFromOrder Map updated [>> " + map.size()
				+ "]");
		return map;
	}

	protected ArrayList<Double> getUpdatedQtyList(Double[] qty, int indexSize) {
		ArrayList<Double> modifiedQtyList = new ArrayList<Double>(indexSize);
		for (int i = 0; i <= (qty.length - 1); i++) {
			modifiedQtyList.add(qty[i]);
		}
		return modifiedQtyList;
	}

	protected void processDeliveryCreation(final String warehouseId,
			final String issueDate, final OrderModel order,
			final String deliveryNumber, final String[] orderEntryNumber,
			final Map<Integer, Double> qtyMap, final Double consignmentValue,
			final Double taxBreakUp, final Double dutyBreakUp,
			final Double promotionBreakUp, final Double freightBreakUp,
			final String trackingNumber, final String trackingNumberInfo) {
		ArrayList<Double> modifiedQtyList = null;
		ConsignmentModel consignment = null;
		List<AbstractOrderEntryModel> entryModel = null;
		Map<String, ConsignmentModel> map = getAllConsignmentFromOrder(order);
		logger.debug("getAllConsignmentFromOrder >> " + map.size());
		if (!map.containsKey(deliveryNumber.trim())) {
			logger.error("Creating Consignment and entry for the delviery Number [ "
					+ deliveryNumber
					+ " ] Order Code [ "
					+ order.getCode()
					+ " ]");
			consignment = createConsignmentAndPopulate(warehouseId, order,
					deliveryNumber, consignmentValue, taxBreakUp, dutyBreakUp,
					promotionBreakUp, freightBreakUp, trackingNumber,
					trackingNumberInfo);
			entryModel = getOrderEntry(order, orderEntryNumber);
			/* modifiedQtyList = new ArrayList<Double>(orderEntryLength);
			f (orderEntryLength != qtyLength) {
				for (int i = 0; i <= (orderEntryLength - 1); i++) {
					if (qtyLength >= 2) {
						if (i >= 2) {
							int j = i - 1;
							modifiedQtyList.add(qty[j]);
							logger.debug("i >= 2 >> " + modifiedQtyList);
						} else {
							modifiedQtyList.add(qty[i]);
						}
					}
					if (qtyLength == 1) {
						modifiedQtyList.add(qty[0]);
						logger.debug("qtyLength==1 >> " + modifiedQtyList);
					}
				}
			} else {
				modifiedQtyList = getUpdatedQtyList(qty, orderEntryLength);
				logger.debug("modifiedQty >> " + modifiedQtyList);
			}*/
			createConsignmentEntry(consignment, entryModel, qtyMap);
			getModelService().save(consignment);
			getModelService().refresh(consignment);
			logger.debug("Consignment created and saved.");
			getBusinessProcessService()
					.triggerEvent(
							DataHubInboundConstants.CONSIGNMENT_CREATION_EVENTNAME_PREFIX
									+ order.getCode());
		}

	}

	protected ConsignmentModel getConsignment(final String warehouseId,
			final String issueDate, final OrderModel order,
			final String deliveryNumber, final String[] orderEntryNumber,
			final Map<Integer, Double> qtyMap, final Double consignmentValue,
			final Double taxBreakUp, final Double dutyBreakUp,
			final Double promotionBreakUp, final Double freightBreakUp,
			final String trackingNumber, final String trackingNumberInfo) {
		for (ConsignmentModel consignment : order.getConsignments()) {
			boolean consignmentStatus = consignment.getCode().trim()
					.equalsIgnoreCase(deliveryNumber.trim());
			if (consignmentStatus) {
				logger.debug("getConsignment >> [" + consignment.getCode()
						+ "] deliveryNumber [" + deliveryNumber + "] Status [ "
						+ consignmentStatus + " ]");
				if (null != issueDate && consignmentStatus) {
					logger.debug("getConsignment >> [" + consignment.getCode()
							+ "] deliveryNumber [" + deliveryNumber
							+ "] issueDate [ " + issueDate + " ]");
					logger.debug("getConsignment >> [" + consignment.getCode()
							+ "] deliveryNumber [" + deliveryNumber
							+ "] trackingNumber [ " + trackingNumber
							+ " ] trackingNumberInfo [ " + trackingNumberInfo
							+ " ]");
					mapDeliveryToConsignment(issueDate, consignment, order); // OOTB
					 logger.debug("trackingNumber >> ["
					 +consignment.getCode()+"] trackingNumber ["+trackingNumber+"] trackingNumberInfo [ "+trackingNumberInfo+" ]");
					// mapQtyToConsignmentEntry(qty,consignment,order);
					mapTrackingNumberToConsignment(trackingNumber,
							trackingNumberInfo, consignment, order);
				}
				return consignment;
			}

		}
		return null;
	}

	protected List<AbstractOrderEntryModel> getOrderEntry(
			final OrderModel order, final String[] orderEntryNumber) {
		logger.debug("orderEntryNumber From Datahub [" + orderEntryNumber + "]");
		List<AbstractOrderEntryModel> orderEntryModelsList = new ArrayList<AbstractOrderEntryModel>();
		for (int i = 0; i <= (orderEntryNumber.length - 1); i++) {
			int orderIndexOnEntries = Integer.valueOf(orderEntryNumber[i])
					.intValue() - 1;
			final AbstractOrderEntryModel entryModel = order.getEntries().get(
					orderIndexOnEntries);
			logger.debug("Product Code [" + entryModel.getProduct().getCode()
					+ "] getEntryNumber >> ["
					+ entryModel.getEntryNumber().intValue()
					+ "] orderEntryNumber From Datahub ["
					+ (orderEntryNumber[i]) + "] Converted Entry Number ["
					+ orderIndexOnEntries + "]");
			orderEntryModelsList.add(entryModel);
		}
		return orderEntryModelsList;
	}

	protected OrderEntryModel getOrderEntry(final OrderModel order,
			final String orderEntryNumber) {
		final Iterator<AbstractOrderEntryModel> iterator = order.getEntries()
				.iterator();
		while (iterator.hasNext()) {
			final OrderEntryModel entryModel = (OrderEntryModel) iterator
					.next();
			if (entryModel.getEntryNumber().intValue() == Integer
					.parseInt(orderEntryNumber)) {
				return entryModel;
			}
		}
		return null;
	}

	protected ConsignmentModel createConsignmentAndPopulate(
			final String warehouseId, final OrderModel order,
			final String deliveryNumber, final Double consignmentValue,
			final Double taxBreakUp, final Double dutyBreakUp,
			final Double promotionBreakUp, final Double freightBreakUp,
			final String tranckingNumber, final String trackingNumberInfo) {
		final ConsignmentModel consignment = createConsignment(warehouseId,
				order, deliveryNumber, consignmentValue, taxBreakUp,
				dutyBreakUp, promotionBreakUp, freightBreakUp, tranckingNumber,
				trackingNumberInfo);
		final Date namedDeliverydate = determineEarliestDeliveryDate(order);
		if (namedDeliverydate != null) {
			consignment.setNamedDeliveryDate(namedDeliverydate);
		}
		consignment.setDeliveryMode(order.getDeliveryMode());
		return consignment;
	}

	protected ConsignmentModel createConsignment(final String warehouseId,
			final OrderModel order, final String deliveryNumber,
			final Double consignmentValue, final Double taxBreakUp,
			final Double dutyBreakUp, final Double promotionBreakUp,
			final Double freightBreakUp, final String trackingNumber,
			final String trackingNumberInfo) {
		try {
			final ConsignmentModel consignment = consignmentService
					.createConsignment(order, deliveryNumber,
							Collections.emptyList());
			consignment.setShippingAddress(order.getDeliveryAddress());
			consignment.setConsignmentValue(consignmentValue);
			consignment.setTaxBreakUp(taxBreakUp);
			consignment.setDutyBreakUp(dutyBreakUp);
			consignment.setPromotionBreakUp(promotionBreakUp);
			consignment.setFreightBreakUp(freightBreakUp);
			consignment.setStatus(ConsignmentStatus.READY_FOR_PICKUP);
			setWarehouseOfConsignment(warehouseId, order, consignment);
			return consignment;
		} catch (final ConsignmentCreationException e) {
			throw new IllegalArgumentException("IDoc processor "
					+ this.getClass().getName()
					+ " could not create consignment for order: "
					+ order.getCode(), e);
		}
	}

	protected void createConsignmentEntry(final ConsignmentModel consignment,
			final List<AbstractOrderEntryModel> entryModels,
			final  Map<Integer, Double> qtyList) {
//		int i = 0;
		for (AbstractOrderEntryModel abstractOrderEntryModel : entryModels) {
			final ConsignmentEntryModel entry = getModelService().create(
					ConsignmentEntryModel.class);
			entry.setOrderEntry(abstractOrderEntryModel);
//			entry.setQuantity(Long.valueOf(modifiedQtyList.get(i).longValue()));
			int orderIndexOnEntries = Integer.valueOf(abstractOrderEntryModel.getEntryNumber()).intValue() + 1;
			logger.debug("Before  [" + abstractOrderEntryModel.getEntryNumber() + "] After " +orderIndexOnEntries);
			entry.setQuantity(qtyList.get(orderIndexOnEntries).longValue());
			logger.debug("setQuantity  [" + qtyList.get(orderIndexOnEntries).longValue() + "] for Item entry " +orderIndexOnEntries);
			entry.setConsignment(consignment);

			Set<ConsignmentEntryModel> consignmentEntries = new HashSet<ConsignmentEntryModel>();
			consignmentEntries.addAll(consignment.getConsignmentEntries());
			consignmentEntries.add(entry);
			consignment.setConsignmentEntries(consignmentEntries);
//			i++;
		}
	}

	protected void createConsignmentEntry(final ConsignmentModel consignment,
			final OrderEntryModel orderEntry, final Double qty) {
		final ConsignmentEntryModel entry = getModelService().create(
				ConsignmentEntryModel.class);
		entry.setOrderEntry(orderEntry);
		entry.setQuantity(Long.valueOf(qty.longValue()));
		entry.setConsignment(consignment);

		Set<ConsignmentEntryModel> consignmentEntries = new HashSet<ConsignmentEntryModel>();
		consignmentEntries.addAll(consignment.getConsignmentEntries());
		consignmentEntries.add(entry);
		consignment.setConsignmentEntries(consignmentEntries);
	}

	protected void processDeliveryGoodsIssue(final String warehouseId,
			final String issueDate, final OrderModel order,
			final String deliveryNumber, final String[] orderEntryNumber,
			final  Map<Integer, Double> qtyList, final Double consignmentValue,
			final Double taxBreakUp, final Double dutyBreakUp,
			final Double promotionBreakUp, final Double freightBreakUp,
			final String trackingNumber, final String trackingNumberInfo) {
		ConsignmentModel consignment = getConsignment(warehouseId, issueDate,
				order, deliveryNumber, orderEntryNumber, qtyList, consignmentValue,
				taxBreakUp, dutyBreakUp, promotionBreakUp, freightBreakUp,
				trackingNumber, trackingNumberInfo);
		logger.debug("processDeliveryGoodsIssue >> completed & ready for shippment email.");
		if (null != consignment) {
			if (isOrderShipped(order)) {
				getBusinessProcessService().triggerEvent(
						DataHubInboundConstants.GOODS_ISSUE_EVENTNAME_PREFIX
								+ order.getCode());
			}
			if (consignment.getStatus().equals(ConsignmentStatus.SHIPPED)) {
				sendDeliveryEmail(consignment);
			}
			logger.debug("processDeliveryGoodsIssue >> Shippment email triggered.");
		} else {
			Assert.isNull(consignment,
					"Consingment model can not be empty. Create a consignment and try again.");
		}
	}

	private boolean isOrderShipped(OrderModel order) {
		for (ConsignmentModel consignment2 : order.getConsignments()) {
			if (!consignment2.getStatus().equals(ConsignmentStatus.SHIPPED)) {
				return false;
			}
		}
		for (AbstractOrderEntryModel orderEntry : order.getEntries()) {
			long total = 0;
			for (ConsignmentEntryModel consignmentEntry : orderEntry
					.getConsignmentEntries()) {
				if (null != consignmentEntry.getShippedQuantity()) {
					total += consignmentEntry.getShippedQuantity().longValue();
				}
			}
			if (total != orderEntry.getQuantity().longValue()) {
				return false;
			}
		}
		return true;
	}

	protected void mapTrackingNumberToConsignment(final String trackingNumber,
			final String trackingNumberInfo,
			final ConsignmentModel consignment, final OrderModel order) {
		consignment.setStatus(ConsignmentStatus.SHIPPED);
		final Date namedDeliverydate = determineEarliestDeliveryDate(order);
		if (namedDeliverydate != null) {
			consignment.setNamedDeliveryDate(namedDeliverydate);
		}
		// consignment.setCarrier(trackingNumber);
		if ( trackingNumberInfo!=("null") && StringUtils.isNotEmpty(trackingNumberInfo) && 
				StringUtils.isNotBlank(trackingNumberInfo) && trackingNumberInfo != null) {
			logger.error("trackingNumberInfo ::: " + trackingNumberInfo);
			consignment.setTrackingID(trackingNumberInfo);
			 logger.debug("Setting TrackingInfo for the order [ "+order.getCode()+" Consigment Code [ "
					 +consignment.getCode()+"] trackingNumber ["+trackingNumber+"] trackingNumberInfo [ "+trackingNumberInfo+" ]");
		}else{
			 logger.debug("trackingNumberInfo not setting for the order [ "+order.getCode()+" Consigment Code [ "
					 +consignment.getCode()+"] trackingNumber ["+trackingNumber+"] trackingNumberInfo [ "+trackingNumberInfo+" ]");
		}
		getModelService().save(consignment);
	}

	protected void mapQtyToConsignmentEntry(final Double[] quantity,
			final ConsignmentModel consignment, final OrderModel order) {
		int i = 0;
		for (final ConsignmentEntryModel consignmentEntry : consignment
				.getConsignmentEntries()) {
			final Integer orderEntryNumber = consignmentEntry.getOrderEntry()
					.getEntryNumber();
			logger.error("quantity recvd ::: " + quantity[i].longValue());
			final Long quantityLong = order.getEntries().get(orderEntryNumber)
					.getQuantity();
			logger.error("quantity recvd ::: quantityLong >>" + quantityLong);
			consignmentEntry.setShippedQuantity(quantityLong);
			i++;
		}
		getModelService().save(consignment);
		logger.error("setShippedQuantity saved ::: ");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.greenlee.sapintegration.orderexchange.datahub.inbound.
	 * GreenleeDataHubInboundDeliveryHelper#determineDeliveryNumber
	 * (java.lang.String)
	 */
	@Override
	public String determineDeliveryNumberFromDatahub(final String deliveryInfo) {
		final String[] deliveryInfos = deliveryInfo
				.split(DATAHUB_INBOUND_CONSTANTS_STR);
		if (deliveryInfos.length > deliveryInfosLength[0]) {
			return deliveryInfos[1];
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.greenlee.sapintegration.orderexchange.datahub.inbound.
	 * GreenleeDataHubInboundDeliveryHelper#
	 * determineWarehouseIdFromDatahub(java.lang.String)
	 */
	@Override
	public String determineWarehouseIdFromDatahub(final String deliveryInfo) {
		return determineWarehouseId(deliveryInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.greenlee.sapintegration.orderexchange.datahub.inbound.
	 * GreenleeDataHubInboundDeliveryHelper#
	 * determineGoodsIssueDateFromDatahub(java.lang.String)
	 */
	@Override
	public String determineGoodsIssueDateFromDatahub(final String deliveryInfo) {
		final String[] deliveryInfos = deliveryInfo
				.split(DATAHUB_INBOUND_CONSTANTS_STR);
		if (deliveryInfos.length > deliveryInfosLength[8]) {
			final String delivDate = deliveryInfos[9];
			if (!delivDate.equals(DataHubInboundConstants.DATE_NOT_SET)) {
				return delivDate;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.greenlee.sapintegration.orderexchange.datahub.inbound.
	 * GreenleeDataHubInboundDeliveryHelper#
	 * determineTrackingNumberFromDatahub(java.lang.String)
	 */
	@Override
	public String determineTrackingNumberFromDatahub(final String deliveryInfo) {
		final String[] deliveryInfos = deliveryInfo
				.split(DATAHUB_INBOUND_CONSTANTS_STR);
		if (deliveryInfos.length > deliveryInfosLength[9]) {
			return deliveryInfos[10];
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.greenlee.sapintegration.orderexchange.datahub.inbound.
	 * GreenleeDataHubInboundDeliveryHelper#
	 * determineTrackingInfoFromDatahub(java.lang.String)
	 */
	@Override
	public String determineTrackingInfoFromDatahub(final String deliveryInfo) {
		final String[] deliveryInfos = deliveryInfo
				.split(DATAHUB_INBOUND_CONSTANTS_STR);
		if (deliveryInfos.length > deliveryInfosLength[10]) {
			logger.error("TrackingNumberInfo >>> "
					+ deliveryInfos[11].toString());
			return deliveryInfos[11];
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.greenlee.sapintegration.orderexchange.datahub.inbound.
	 * GreenleeDataHubInboundDeliveryHelper#
	 * determineOrderEntryNumberFromDatahub(java.lang.String)
	 */
	@Override
	public String determineOrderEntryNumberFromDatahub(final String deliveryInfo) {
		final String[] deliveryInfos = deliveryInfo
				.split(DATAHUB_INBOUND_CONSTANTS_STR);
		if (deliveryInfos.length > deliveryInfosLength[1]) {
			return deliveryInfos[2];
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.greenlee.sapintegration.orderexchange.datahub.inbound.
	 * GreenleeDataHubInboundDeliveryHelper#
	 * determineQuantityFromDatahub(java.lang.String)
	 */
	@Override
	public Double determineQuantityFromDatahub(final String deliveryInfo) {
		final String[] deliveryInfos = deliveryInfo
				.split(DATAHUB_INBOUND_CONSTANTS_STR);
		if (deliveryInfos.length > deliveryInfosLength[2]) {
			return Double.valueOf(deliveryInfos[3]);
		}
		return null;
	}

	/**
	 * @return the eventService
	 */
	public EventService getEventService() {
		return eventService;
	}

	/**
	 * @param eventService
	 *            the eventService to set
	 */
	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	public void sendDeliveryEmail(ConsignmentModel consignment) {
		final ConsignmentProcessModel consignmentProcessModel = (ConsignmentProcessModel) getBusinessProcessService()
				.createProcess(
						SEND_DELIVERY_EMAIL_PROCESS
								+ System.currentTimeMillis(),
						SEND_DELIVERY_EMAIL_PROCESS);
		// consignmentProcessModel.setProcessDefinitionName(SEND_DELIVERY_EMAIL_PROCESS);
		consignmentProcessModel.setConsignment(consignment);
		getModelService().save(consignmentProcessModel);
		getBusinessProcessService().startProcess(consignmentProcessModel);
		// getEventService().publishEvent(new
		// SendDeliveryMessageEvent(consignmentProcessModel));

	}

	@Override
	public Double determineConsignmentValueFromDatahub(String deliveryInfo) {
		final String[] deliveryInfos = deliveryInfo
				.split(DATAHUB_INBOUND_CONSTANTS_STR);
		if (deliveryInfos.length > deliveryInfosLength[3]) {
			return Double.valueOf(deliveryInfos[4]);
		}
		return null;
	}

	@Override
	public Double determineTaxBreakUpFromDatahub(String deliveryInfo) {
		final String[] deliveryInfos = deliveryInfo
				.split(DATAHUB_INBOUND_CONSTANTS_STR);
		if (deliveryInfos.length > deliveryInfosLength[4]) {
			return Double.valueOf(deliveryInfos[5]);
		}
		return null;
	}

	@Override
	public Double determineDutyBreakUpFromDatahub(String deliveryInfo) {
		final String[] deliveryInfos = deliveryInfo
				.split(DATAHUB_INBOUND_CONSTANTS_STR);
		if (deliveryInfos.length > deliveryInfosLength[5]) {
			return Double.valueOf(deliveryInfos[6]);
		}
		return null;
	}

	@Override
	public Double determinePromotionBreakUpFromDatahub(String deliveryInfo) {
		final String[] deliveryInfos = deliveryInfo
				.split(DATAHUB_INBOUND_CONSTANTS_STR);
		if (deliveryInfos.length > deliveryInfosLength[6]) {
			return Double.valueOf(deliveryInfos[7]);
		}
		return null;
	}

	@Override
	public Double determineFreightBreakUpFromDatahub(String deliveryInfo) {
		final String[] deliveryInfos = deliveryInfo
				.split(DATAHUB_INBOUND_CONSTANTS_STR);
		if (deliveryInfos.length > deliveryInfosLength[7]) {
			return Double.valueOf(deliveryInfos[8]);
		}
		return null;
	}

}

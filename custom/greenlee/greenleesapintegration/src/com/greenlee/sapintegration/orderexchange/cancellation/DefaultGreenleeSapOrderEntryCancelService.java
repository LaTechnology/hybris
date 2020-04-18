/**
 * 
 */
package com.greenlee.sapintegration.orderexchange.cancellation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.hybris.platform.acceleratorservices.orderprocessing.model.OrderModificationProcessModel;
import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.basecommerce.enums.OrderCancelEntryStatus;
import de.hybris.platform.basecommerce.enums.OrderModificationEntryStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordercancel.OrderCancelCallbackService;
import de.hybris.platform.ordercancel.OrderCancelEntry;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.ordercancel.OrderCancelRecordsHandler;
import de.hybris.platform.ordercancel.OrderCancelRequest;
import de.hybris.platform.ordercancel.OrderCancelResponse;
import de.hybris.platform.ordercancel.OrderCancelService;
import de.hybris.platform.ordercancel.OrderCancelResponse.ResponseStatus;
import de.hybris.platform.ordercancel.exceptions.OrderCancelRecordsHandlerException;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.ordercancel.model.OrderCancelRecordModel;
import de.hybris.platform.ordercancel.model.OrderEntryCancelRecordEntryModel;
import de.hybris.platform.orderhistory.OrderHistoryService;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.ordermodify.model.OrderEntryModificationRecordEntryModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordEntryModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.sap.orderexchange.cancellation.DefaultSapOrderCancelService;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import reactor.util.CollectionUtils;


/**
 * @author nalini.ramarao
 * 
 */
public class DefaultGreenleeSapOrderEntryCancelService extends DefaultSapOrderCancelService implements
		GreenleeSapOrderEntryCancelService
{
	
	 public static final String SEND_ORDER_CANCELLED_EMAIL_PROCESS = "sendOrderCancelledEmailProcess";
	 public static final String SEND_PARTIAL_ORDER_CANCELLED_EMAIL_PROCESS = "sendOrderPartiallyCanceledEmailProcess";

	 private EventService eventService;
	 private BusinessProcessService businessProcessService;
	 private ModelService modelService;
	 private UserService userService;
	 private OrderCancelService orderCancelService;
	 private OrderCancelRecordsHandler orderCancelRecordsHandler;
	 /*private OrderHistoryService orderHistoryService;
	 */
	 private OrderCancelCallbackService orderCancelCallbackService;

	/**
	 * @param orderCancelCallbackService
	 *           the orderCancelCallbackService to set
	 */
	@Override
	public void setOrderCancelCallbackService(final OrderCancelCallbackService orderCancelCallbackService)
	{
		this.orderCancelCallbackService = orderCancelCallbackService;
	}

	@Override
	public void cancelOrderEntry(List<OrderCancelEntry> cancelEntries, final String erpRejectionReason)
			throws OrderCancelException
	{
		final OrderCancelResponse cancelResponse;
		OrderModel orderModel = (OrderModel) cancelEntries.get(0).getOrderEntry().getOrder();
		cancelResponse = new OrderCancelResponse(orderModel, cancelEntries, ResponseStatus.partial,
				erpRejectionReason);
		cancelResponse.setCancelReason(CancelReason.valueOf(erpRejectionReason));
		createOrderCancelEntryIfNecessary(orderModel, cancelResponse);
		orderCancelCallbackService.onOrderCancelResponse(cancelResponse);
		
		sendPartialCancelPendingNotification(orderModel, getOrderModificationRecordEntryModel(orderModel, cancelEntries));
	}
	
	private OrderModificationRecordEntryModel getOrderModificationRecordEntryModel(OrderModel order, List<OrderCancelEntry> cancelEntries) throws OrderCancelException {
		OrderCancelRecordEntryModel model = getModelService().create(OrderCancelRecordEntryModel.class);
		
		Collection<OrderEntryModificationRecordEntryModel> entryModelCollection = new ArrayList<>();
		for (OrderCancelEntry orderCancelEntry : cancelEntries) {
			OrderEntryCancelRecordEntryModel entryModel = getModelService().create(OrderEntryCancelRecordEntryModel.class);
			entryModel.setOriginalOrderEntry((OrderEntryModel) orderCancelEntry.getOrderEntry());
			entryModel.setCancelledQuantity(Long.valueOf(orderCancelEntry.getCancelQuantity()).intValue());
			entryModel.setCode(order.getCode() + "_" + System.currentTimeMillis());
			entryModel.setModificationRecordEntry(model);
			entryModelCollection.add(entryModel);
		}
		
		model.setOrderEntriesModificationEntries(entryModelCollection);
		model.setCode(order.getCode() + "_" + System.currentTimeMillis());
		model.setStatus(OrderModificationEntryStatus.SUCCESSFULL);
		model.setTimestamp(new Date());
		
		OrderCancelRecordModel orderModel = getOrderCancelService().getCancelRecordForOrder(order);//getModelService().create(OrderCancelRecordModel.class); 
		orderModel.setOrder(order);
		
		OrderHistoryEntryModel entryHistoryModel = getLastHistoryEntry(order);
		model.setOriginalVersion(entryHistoryModel);
		model.setModificationRecord(orderModel);
		
		
		getModelService().saveAll(orderModel, model);
		return model;
	}
	
	protected void createOrderCancelEntryIfNecessary(final OrderModel order, final OrderCancelResponse cancelResponse)
			throws OrderCancelException
	{
		final EmployeeModel adminUser = userService.getAdminUser();
		getOrderCancelRecordsHandler().createRecordEntry(cancelResponse, adminUser);
	}
	
	private OrderHistoryEntryModel getLastHistoryEntry(OrderModel order) {
		OrderHistoryEntryModel entryHistoryModel = null;
		List<OrderHistoryEntryModel> historyEntries = order.getHistoryEntries();
		if (!CollectionUtils.isEmpty(historyEntries )) {
			List<OrderHistoryEntryModel> historyEntries0 = new ArrayList<>();
			historyEntries0.addAll(historyEntries);
			historyEntries0.sort(new HistoryComparator());
			entryHistoryModel = historyEntries0.get(0);
		} else {
			entryHistoryModel =  getModelService().create(OrderHistoryEntryModel.class);
			entryHistoryModel.setPreviousOrderVersion(order);
			entryHistoryModel.setOrder(order);
			entryHistoryModel.setTimestamp(new Date());
			getModelService().saveAll(entryHistoryModel);
		}
		return entryHistoryModel;
	}
	
	private class HistoryComparator implements Comparator<OrderHistoryEntryModel> {

		@Override
		public int compare(OrderHistoryEntryModel history1, OrderHistoryEntryModel history2) {
			return history1.getTimestamp().compareTo(history2.getTimestamp());
		}
		
	}
	
	
	public void sendPartialCancelPendingNotification(OrderModel order, OrderModificationRecordEntryModel modificationRecordEntry) {
		
		final OrderModificationProcessModel orderModificationProcessModel = getBusinessProcessService()
				.createProcess(SEND_PARTIAL_ORDER_CANCELLED_EMAIL_PROCESS + "_" + order.getCode() + "_" + System.currentTimeMillis(), 
						SEND_PARTIAL_ORDER_CANCELLED_EMAIL_PROCESS);
		orderModificationProcessModel.setOrder(order);
		orderModificationProcessModel.setOrderModificationRecordEntry(modificationRecordEntry);
		getModelService().save(orderModificationProcessModel);
		getBusinessProcessService().startProcess(orderModificationProcessModel);
	}
	
	public void sendCancelPendingNotifications(OrderModel order)
	{
        final OrderModificationProcessModel orderModificationProcessModel = (OrderModificationProcessModel) getBusinessProcessService()
                .createProcess(SEND_ORDER_CANCELLED_EMAIL_PROCESS + "_" + order.getCode() + "_" + System.currentTimeMillis(), SEND_ORDER_CANCELLED_EMAIL_PROCESS);
        orderModificationProcessModel.setOrder(order);
        getModelService().save(orderModificationProcessModel);
        getBusinessProcessService().startProcess(orderModificationProcessModel);
	}

	public EventService getEventService() {
		return eventService;
	}

	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	public BusinessProcessService getBusinessProcessService() {
		return businessProcessService;
	}

	public void setBusinessProcessService(BusinessProcessService businessProcessService) {
		this.businessProcessService = businessProcessService;
	}

	public ModelService getModelService() {
		return modelService;
	}

	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public OrderCancelService getOrderCancelService() {
		return orderCancelService;
	}

	public void setOrderCancelService(OrderCancelService orderCancelService) {
		this.orderCancelService = orderCancelService;
	}

	public OrderCancelRecordsHandler getOrderCancelRecordsHandler() {
		return orderCancelRecordsHandler;
	}

	public void setOrderCancelRecordsHandler(OrderCancelRecordsHandler orderCancelRecordsHandler) {
		this.orderCancelRecordsHandler = orderCancelRecordsHandler;
	}

	public OrderCancelCallbackService getOrderCancelCallbackService() {
		return orderCancelCallbackService;
	}
}

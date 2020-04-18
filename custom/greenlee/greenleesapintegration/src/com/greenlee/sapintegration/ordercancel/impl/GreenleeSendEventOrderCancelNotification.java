package com.greenlee.sapintegration.ordercancel.impl;

import com.greenlee.sapintegration.ordercancel.GreenleeOrderCancelNotificationServiceAdapter;

import de.hybris.platform.acceleratorservices.orderprocessing.model.OrderModificationProcessModel;
import de.hybris.platform.commerceservices.event.OrderCancelledEvent;
import de.hybris.platform.ordercancel.OrderCancelRequest;
import de.hybris.platform.ordercancel.impl.SendEventOrderCancelNotification;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.processengine.BusinessProcessService;


public class GreenleeSendEventOrderCancelNotification extends SendEventOrderCancelNotification implements GreenleeOrderCancelNotificationServiceAdapter
{
    public static final String SEND_ORDER_CANCELLED_EMAIL_PROCESS = "sendOrderCancelledEmailProcess";

    private EventService eventService;
    private ModelService modelService;
    private BusinessProcessService businessProcessService;

    /**
     * @return the businessProcessService
     */
    public BusinessProcessService getBusinessProcessService()
    {
        return businessProcessService;
    }

    /**
     * @param businessProcessService the businessProcessService to set
     */
    public void setBusinessProcessService(BusinessProcessService businessProcessService)
    {
        this.businessProcessService = businessProcessService;
    }

    /**
     * @return the modelService
     */
    public ModelService getModelService()
    {
        return modelService;
    }

    /**
     * @param modelService the modelService to set
     */
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }

    /**
     * @return the eventService
     */
    public EventService getEventService()
    {
        return eventService;
    }

    /**
     * @param eventService the eventService to set
     */
    public void setEventService(EventService eventService)
    {
        this.eventService = eventService;
    }

    public void sendCancelPendingNotifications(OrderCancelRequest orderCancelRequest, OrderCancelRecordEntryModel cancelRequestRecordEntry)
    {
        final OrderModificationProcessModel orderModificationProcessModel = (OrderModificationProcessModel) getBusinessProcessService()
                .createProcess(SEND_ORDER_CANCELLED_EMAIL_PROCESS + System.currentTimeMillis(), SEND_ORDER_CANCELLED_EMAIL_PROCESS);
        orderModificationProcessModel.setOrder(orderCancelRequest.getOrder());
        //orderModificationProcessModel.setProcessDefinitionName(SEND_ORDER_CANCELLED_EMAIL_PROCESS);
        orderModificationProcessModel.setOrderModificationRecordEntry(cancelRequestRecordEntry);
        getModelService().save(orderModificationProcessModel);
        getBusinessProcessService().startProcess(orderModificationProcessModel);
        //this.eventService.publishEvent(new OrderCancelledEvent(orderModificationProcessModel));
    }
}

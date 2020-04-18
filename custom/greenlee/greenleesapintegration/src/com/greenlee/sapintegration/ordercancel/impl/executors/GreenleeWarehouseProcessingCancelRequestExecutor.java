package com.greenlee.sapintegration.ordercancel.impl.executors;

import de.hybris.platform.ordercancel.OrderCancelRequest;
import de.hybris.platform.ordercancel.impl.executors.WarehouseProcessingCancelRequestExecutor;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import org.apache.log4j.Logger;

import com.greenlee.sapintegration.ordercancel.GreenleeOrderCancelNotificationServiceAdapter;

public class GreenleeWarehouseProcessingCancelRequestExecutor extends WarehouseProcessingCancelRequestExecutor
{
    private static final Logger LOG = Logger.getLogger(GreenleeWarehouseProcessingCancelRequestExecutor.class.getName());
    
    private GreenleeOrderCancelNotificationServiceAdapter greenleeOrderCancelNotificationServiceAdapter;
    
    /**
     * @return the greenleeOrderCancelNotificationServiceAdapter
     */
    public GreenleeOrderCancelNotificationServiceAdapter getGreenleeOrderCancelNotificationServiceAdapter()
    {
        return greenleeOrderCancelNotificationServiceAdapter;
    }

    /**
     * @param greenleeOrderCancelNotificationServiceAdapter the greenleeOrderCancelNotificationServiceAdapter to set
     */
    public void setGreenleeOrderCancelNotificationServiceAdapter(
            GreenleeOrderCancelNotificationServiceAdapter greenleeOrderCancelNotificationServiceAdapter)
    {
        this.greenleeOrderCancelNotificationServiceAdapter = greenleeOrderCancelNotificationServiceAdapter;
    }

    @Override
    public void processCancelRequest(OrderCancelRequest orderCancelRequest, OrderCancelRecordEntryModel cancelRequestRecordEntry)
    {
      this.getOrderStatusChangeStrategy().changeOrderStatusAfterCancelOperation(cancelRequestRecordEntry, true);
      
      this.getWarehouseAdapter().requestOrderCancel(orderCancelRequest);
      if (this.getGreenleeOrderCancelNotificationServiceAdapter() == null) {
        LOG.info("order: " + orderCancelRequest.getOrder().getCode() + " is being cancelled");
      } else {
        this.getGreenleeOrderCancelNotificationServiceAdapter().sendCancelPendingNotifications(orderCancelRequest, cancelRequestRecordEntry);
      }
    }
}

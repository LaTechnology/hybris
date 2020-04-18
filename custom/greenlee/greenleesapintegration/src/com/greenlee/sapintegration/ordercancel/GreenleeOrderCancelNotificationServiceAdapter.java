package com.greenlee.sapintegration.ordercancel;

import de.hybris.platform.ordercancel.OrderCancelNotificationServiceAdapter;
import de.hybris.platform.ordercancel.OrderCancelRequest;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;

public interface GreenleeOrderCancelNotificationServiceAdapter extends OrderCancelNotificationServiceAdapter
{
    public void sendCancelPendingNotifications(OrderCancelRequest orderCancelRequest, OrderCancelRecordEntryModel cancelRequestRecordEntry);
}

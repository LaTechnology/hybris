package com.greenlee.sapintegration.orderexchange.cancellation;

import de.hybris.platform.basecommerce.enums.OrderCancelState;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordercancel.OrderCancelStateMappingStrategy;

public class DefaultGreenleeOrderCancelStateMappingStrategy implements OrderCancelStateMappingStrategy{

	@Override
	public OrderCancelState getOrderCancelState(OrderModel order) {
		final OrderStatus orderStatus = order.getStatus();
		if ((OrderStatus.CANCELLED.equals(orderStatus)) || (OrderStatus.COMPLETED.equals(orderStatus)))
		{
			return OrderCancelState.CANCELIMPOSSIBLE;
		}
		
		return OrderCancelState.SENTTOWAREHOUSE;
	}

}

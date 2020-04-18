package com.greenlee.datahub.order.publication.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.MethodExecutor;
import org.springframework.expression.TypedValue;

import com.hybris.datahub.saporder.util.OrderItemNumberConverter;

public class DeliveryEntryNumberConverter  implements MethodExecutor{

	private static final Logger LOGGER = LoggerFactory.getLogger((Class)DeliveryEntryNumberConverter.class);
	 
    private OrderItemNumberConverter orderItemNumberConverter;

	public /* varargs */ TypedValue execute(EvaluationContext context, Object target, Object ... arguments) throws AccessException {
		  String erpOrderEntryNumberString = (String)arguments[0];
	      String orderId = arguments.length == 2 ? (String)arguments[1] : null;
	      long erpOrderEntry = Long.parseLong(erpOrderEntryNumberString);
	      long hybrisOrderEntry = this.orderItemNumberConverter.convertFromErpToHybris(erpOrderEntry, orderId);
	      LOGGER.debug("orderId={}, erpOrderEntry={}, hybrisOrderEntry={}", new Object[]{orderId, erpOrderEntry, hybrisOrderEntry});
	      return new TypedValue((Object)Long.toString(hybrisOrderEntry));
	 }

	 public void setOrderItemNumberConverter(OrderItemNumberConverter orderItemNumberConverter) {
	      this.orderItemNumberConverter = orderItemNumberConverter;
	 }
}

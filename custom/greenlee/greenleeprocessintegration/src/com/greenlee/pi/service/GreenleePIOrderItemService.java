/**
 *
 */
package com.greenlee.pi.service;

import com.greenlee.orderhistory.data.PIOrderItem;
import com.greenlee.pi.exception.PIException;


/**
 * @author peter.asirvatham
 *
 */
public interface GreenleePIOrderItemService
{
	PIOrderItem getPIOrderDetails(String orderid) throws PIException;
}

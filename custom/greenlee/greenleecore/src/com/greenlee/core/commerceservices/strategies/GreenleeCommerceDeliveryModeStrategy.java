/**
 * 
 */
package com.greenlee.core.commerceservices.strategies;

import de.hybris.platform.commerceservices.order.impl.DefaultCommerceDeliveryModeStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.core.model.order.CartModel;


/**
 * @author Viji Shetty
 * 
 */
public class GreenleeCommerceDeliveryModeStrategy extends DefaultCommerceDeliveryModeStrategy
{
	@Override
	public boolean removeDeliveryMode(final CommerceCheckoutParameter parameter)
	{
		super.removeDeliveryMode(parameter);
		final CartModel cartModel = parameter.getCart();
		cartModel.setShippedByDate(null);
		cartModel.setShipmentAccountNumber(null);
		getModelService().save(cartModel);
		getModelService().refresh(cartModel);

		return true;
	}

}

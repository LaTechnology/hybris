/**
 *
 */
package com.greenlee.facades.cart;

import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.order.exceptions.CalculationException;

import java.util.Collection;
import java.util.Date;

import com.greenlee.facades.price.data.OrderSimulationData;
import com.greenlee.facades.price.data.PriceDiffDataList;



/**
 * @author nalini.ramarao
 *
 */
public interface GreenLeeCartFacade
{
    PriceDiffDataList getRealTimePricing();

    void setShipDateShipNoteAndEmailAddressesForCart(final String deliveryModeCode, String shippingNote,
            Collection<String> emailAddresses, Date shipByDate, String shipmentAccountNumber);

    DeliveryModeData getDeliveryMethodForCode(String deliveryMethodCode);

    CartModificationData updateCartEntry(long entryNumber, long quantity, String serialNumbers)
            throws CommerceCartModificationException;

    OrderSimulationData getOrderSimulation(boolean isSimulate) throws CalculationException;

    void resetCheckoutAttributes() throws CalculationException;


}

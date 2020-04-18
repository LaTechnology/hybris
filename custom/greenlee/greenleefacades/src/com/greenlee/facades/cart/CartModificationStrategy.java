/**
 *
 */
package com.greenlee.facades.cart;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import com.greenlee.facades.price.data.PriceDiffData;
import com.hybris.urn.xi.pricing_enquiry.DTHybrisPricingResponse.ORDERITEMS.ITEMS;


/**
 * @author raja.santhanam
 *
 */
public interface CartModificationStrategy
{
    PriceDiffData identifyCartModifications(AbstractOrderEntryModel orderEntryModel, ITEMS response);
}

/**
 *
 */
package com.greenlee.facades.cart.impl;

import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import java.math.BigDecimal;

import com.greenlee.facades.cart.CartModificationStrategy;
import com.greenlee.facades.price.data.PriceDiffData;
import com.hybris.urn.xi.pricing_enquiry.DTHybrisPricingResponse.ORDERITEMS.ITEMS;


/**
 * @author raja.santhanam
 *
 */
public class GreenleeCartModificationStrategy implements CartModificationStrategy
{

    private PriceDataFactory priceDataFactory;

    /* (non-Javadoc)
     * @see com.greenlee.facades.cart.CartModificationStrategy#identifyCartModifications(de.hybris.platform.core.model.order.AbstractOrderEntryModel, com.hybris.urn.xi.pricing_enquiry.DTHybrisPricingResponse.ORDERITEMS.ITEMS)
     */
    @Override
    public PriceDiffData identifyCartModifications(final AbstractOrderEntryModel orderEntryModel, final ITEMS response)
    {
        final double total = Double.parseDouble(response.getSubtotal1());
        final double qty = orderEntryModel.getQuantity().doubleValue();
        final Double newPrice = Double.valueOf(total / qty);
        if (!orderEntryModel.getOrder().getSubtotal().equals(newPrice))
        {
            final PriceDiffData data = new PriceDiffData();
            data.setProductName(orderEntryModel.getProduct().getName());
            data.setActualPrice(createPriceData(orderEntryModel.getBasePrice().doubleValue(),
                    orderEntryModel.getOrder().getCurrency().getIsocode()));
            data.setDiscountedPrice(
                    createPriceData(newPrice.doubleValue(), orderEntryModel.getOrder().getCurrency().getIsocode()));
            data.setEntryNumber(orderEntryModel.getEntryNumber());
            return data;
        }
        return null;
    }

    /**
     * @param orderEntryModel
     */
    private PriceData createPriceData(final double value, final String isoCodeCurrency)
    {
        return priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(value), isoCodeCurrency);
    }

    /**
     * @return the priceDataFactory
     */
    public PriceDataFactory getPriceDataFactory()
    {
        return priceDataFactory;
    }

    /**
     * @param priceDataFactory
     *            the priceDataFactory to set
     */
    public void setPriceDataFactory(final PriceDataFactory priceDataFactory)
    {
        this.priceDataFactory = priceDataFactory;
    }

}

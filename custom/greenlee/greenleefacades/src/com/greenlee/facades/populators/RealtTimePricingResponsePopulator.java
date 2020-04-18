/**
 *
 */
package com.greenlee.facades.populators;

import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.greenlee.facades.price.data.PriceDiffData;
import com.greenlee.facades.price.data.PriceDiffDataList;
import com.hybris.urn.xi.pricing_enquiry.DTHybrisPricingResponse;
import com.hybris.urn.xi.pricing_enquiry.DTHybrisPricingResponse.ORDERITEMS.ITEMS;


/**
 * @author nalini.ramarao
 *
 */
public class RealtTimePricingResponsePopulator implements Populator<DTHybrisPricingResponse, PriceDiffDataList>
{
    private static final Logger LOG = Logger.getLogger(RealtTimePricingResponsePopulator.class);

    private CartService      cartService;
    private PriceDataFactory priceDataFactory;

    /* (non-Javadoc)
     * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final DTHybrisPricingResponse source, final PriceDiffDataList target)
    {
        List<PriceDiffData> diffDataLists = new ArrayList<>();
        final CartModel model = getCartService().getSessionCart();
        if ("E".equalsIgnoreCase(source.getRETURN().getType()))
        {
            target.setErrorMsg(source.getRETURN().getMessage());
        }
        final Iterator<AbstractOrderEntryModel> entriesIterator = model.getEntries().iterator();
        while (entriesIterator.hasNext())
        {
            final AbstractOrderEntryModel entryModel = entriesIterator.next();
            if (source.getORDERITEMS() != null)
            {
                diffDataLists = populateItems(source, entryModel, diffDataLists);
            }
        }
        target.setPriceDiffRow(diffDataLists);
    }

    protected List<PriceDiffData> populateItems(final DTHybrisPricingResponse source, final AbstractOrderEntryModel entryModel,
            final List<PriceDiffData> diffDataLists)
    {
        for (final ITEMS cartItem : source.getORDERITEMS().getITEMS())
        {
            if (entryModel.getEntryNumber().compareTo(Integer.valueOf(cartItem.getHybrisItemNo())) == 0)
            {
                final double total = Double.parseDouble(cartItem.getSubtotal1());
                final double qty = entryModel.getQuantity().doubleValue();
                final PriceDiffData data = new PriceDiffData();
                if (entryModel.getBasePrice().compareTo(Double.valueOf(total / qty)) != 0)
                {
                    data.setProductName(entryModel.getProduct().getName());
                    data.setActualPrice(createPriceData(entryModel.getBasePrice().doubleValue(), entryModel.getOrder()
                            .getCurrency().getIsocode()));
                    data.setDiscountedPrice(createPriceData(total / qty, entryModel.getOrder().getCurrency().getIsocode()));
                    data.setEntryNumber(entryModel.getEntryNumber());
                    data.setEstimatedShipDate(cartItem.getGoodsIssueDate());
                    diffDataLists.add(data);
                    break;
                }
                else
                {
                    LOG.error("Estimated Shipping date: " + cartItem.getGoodsIssueDate());
                    data.setEntryNumber(entryModel.getEntryNumber());
                    data.setEstimatedShipDate(cartItem.getGoodsIssueDate());
                    diffDataLists.add(data);
                }
            }
        }
        return diffDataLists;
    }

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


    /**
     * @return the cartService
     */
    public CartService getCartService()
    {
        return cartService;
    }

    /**
     * @param cartService
     *            the cartService to set
     */
    public void setCartService(final CartService cartService)
    {
        this.cartService = cartService;
    }

}

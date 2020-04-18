/**
 *
 */
package com.greenlee.facades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.greenlee.facades.cart.CartModificationStrategy;
import com.greenlee.facades.price.data.OrderSimulationData;
import com.greenlee.facades.price.data.OrderSimulationProductData;
import com.greenlee.facades.price.data.PriceDiffData;
import com.hybris.urn.xi.pricing_enquiry.DTHybrisPricingResponse;
import com.hybris.urn.xi.pricing_enquiry.DTHybrisPricingResponse.ORDERITEMS.ITEMS;


/**
 * @author qili
 */
public class OrderSimulationResponsePopulator implements Populator<DTHybrisPricingResponse, OrderSimulationData>
{
    private static final Logger      LOG = Logger.getLogger(OrderSimulationResponsePopulator.class);
    private CartService              cartService;
    private CartModificationStrategy modificationStrategy;

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

    /* (non-Javadoc)
     * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final DTHybrisPricingResponse source, final OrderSimulationData target) throws ConversionException
    {
        final CartModel cartModel = getCartService().getSessionCart();
        LOG.info("Return type: " + source.getRETURN().getType());

        final List<AbstractOrderEntryModel> abstractOrderEntryModelList = new ArrayList<AbstractOrderEntryModel>(
                cartModel.getEntries());
        LOG.info("AbstractOrderEntryModel : " + abstractOrderEntryModelList.size());
        final List<OrderSimulationProductData> orderSimulationProductDatas = new ArrayList<OrderSimulationProductData>();
        if ("E".equalsIgnoreCase(source.getRETURN().getType()))
        {
            LOG.info("Error Message: " + source.getRETURN().getMessage());
            target.setErrorMsg(source.getRETURN().getMessage());
            return;
        }
        if (source.getORDERITEMS() != null && !source.getORDERITEMS().getITEMS().isEmpty())
        {
            LOG.info("Shipping cost:" + source.getFreight());
            target.setShippingCost(Double.valueOf(source.getFreight()));

            final List<ITEMS> piResponseItems = new ArrayList<ITEMS>(source.getORDERITEMS().getITEMS());
            LOG.info("PI ResponseItems : " + piResponseItems.size());
            for (final ITEMS items : piResponseItems)
            {
                double totalTax = 0.0d;
                AbstractOrderEntryModel orderEntryModel = null;
                if (!abstractOrderEntryModelList.isEmpty())
                {
                    if (StringUtils.isNotBlank(items.getHybrisItemNo()) && StringUtils.isNotEmpty(items.getHybrisItemNo()))
                    {
                        orderEntryModel = abstractOrderEntryModelList.get(Integer.valueOf(items.getHybrisItemNo()).intValue());
                        LOG.info("AbstractOrderEntryModel : " + orderEntryModel.getEntryNumber() + " ItemNumber From PI :: "
                                + items.getHybrisItemNo());
                    }
                }
                final OrderSimulationProductData productData = getOrderSimulationProductData(items, orderEntryModel);
                if (null != productData && productData.getTax() != null)
                {
                    totalTax += productData.getTax().doubleValue();
                    LOG.info("Total Tax cost:" + totalTax);
                    target.setTotalTax(Double.valueOf(totalTax));
                }
                orderSimulationProductDatas.add(productData);
            }
        }

        LOG.info("OrderSimulationProductData :" + orderSimulationProductDatas.size());

        target.setProducts(orderSimulationProductDatas);
    }

    protected OrderSimulationProductData getOrderSimulationProductData(final ITEMS item, final AbstractOrderEntryModel entryModel)
    {
        final OrderSimulationProductData data = new OrderSimulationProductData();
        if (null != item && null != entryModel)
        {
            LOG.info("getOrderSimulationProductData >> AbstractOrderEntryModel : " + entryModel.getEntryNumber()
                    + " ItemNumber From PI :: " + item.getHybrisItemNo());
            final double total = Double.parseDouble(item.getSubtotal1());
            final double qty = Double.parseDouble(item.getRequiredQuantity());

            final Double basePrice = entryModel.getBasePrice();
            final Double totalByQty = Double.valueOf(total / qty);
            if (null != basePrice && null != totalByQty && basePrice.compareTo(totalByQty) != 0)
            {
                final PriceDiffData priceDifferenceData = modificationStrategy.identifyCartModifications(entryModel, item);
                LOG.info("priceDifferenceData is present: " + (priceDifferenceData != null));
                data.setPriceDifference(priceDifferenceData);
            }
            LOG.info("Discounted Price: " + (total));
            data.setDiscountedPrice(Double.valueOf(total));
            LOG.info("EntryNumber: " + entryModel.getEntryNumber());
            data.setEntryNumber(entryModel.getEntryNumber());
            LOG.info("Product Name: " + entryModel.getProduct().getName());
            data.setProductName(entryModel.getProduct().getName());
            if (data.getTax() == null && item.getSubtotal2() != null)
            {
                LOG.info("Tax: " + item.getSubtotal2());
                data.setTax(Double.valueOf(item.getSubtotal2()));
            }
            else
            {
                LOG.info("Tax is : " + item.getSubtotal2());
                data.setTax(new Double(0.0d));
            }
            if (StringUtils.isNotBlank(item.getSubtotal3()))
            {
                LOG.info("Duty: " + item.getSubtotal3());
                data.setDuty(Double.valueOf(item.getSubtotal3()));
            }
            else
            {
                data.setDuty(new Double(0.0d));
                LOG.info("Duty: " + data.getDuty());
            }

            LOG.info("Currency: " + item.getCurrency());
            data.setCurrency(item.getCurrency());
            LOG.info("GoodsIssueDate: " + item.getGoodsIssueDate());
            data.setEstimatedShipDate(item.getGoodsIssueDate());
        }
        return data;
    }

    protected List<OrderSimulationProductData> populateItems(final DTHybrisPricingResponse source,
            final AbstractOrderEntryModel entryModel)
    {
        final List<OrderSimulationProductData> productDataSimulationProductDatas = new ArrayList<OrderSimulationProductData>();
        for (final ITEMS item : source.getORDERITEMS().getITEMS())
        {
            LOG.info("AbstractOrderEntryModel : " + entryModel.getEntryNumber() + " ItemNumber From PI :: "
                    + item.getHybrisItemNo());
            final double total = Double.parseDouble(item.getSubtotal1());
            final double qty = Double.parseDouble(item.getRequiredQuantity());
            final OrderSimulationProductData data = new OrderSimulationProductData();
            final Double basePrice = entryModel.getBasePrice();
            final Double totalByQty = Double.valueOf(total / qty);
            if (null != basePrice && null != totalByQty && basePrice.compareTo(totalByQty) != 0)
            {
                final PriceDiffData priceDifferenceData = modificationStrategy.identifyCartModifications(entryModel, item);
                LOG.info("priceDifferenceData is present: " + (priceDifferenceData != null));
                data.setPriceDifference(priceDifferenceData);
            }
            LOG.info("Discounted Price: " + (total));
            data.setDiscountedPrice(Double.valueOf(total));
            LOG.info("EntryNumber: " + entryModel.getEntryNumber());
            data.setEntryNumber(entryModel.getEntryNumber());
            LOG.info("Product Name: " + entryModel.getProduct().getName());
            data.setProductName(entryModel.getProduct().getName());
            if (data.getTax() == null && item.getSubtotal2() != null)
            {
                LOG.info("Tax: " + item.getSubtotal2());
                data.setTax(Double.valueOf(item.getSubtotal2()));
            }
            else
            {
                LOG.info("Tax is : " + item.getSubtotal2());
                data.setTax(new Double(0.0d));
            }

            if (StringUtils.isNotBlank(item.getSubtotal3()))
            {
                LOG.info("Duty: " + item.getSubtotal3());
                data.setDuty(Double.valueOf(item.getSubtotal3()));
            }
            else
            {
                data.setDuty(new Double(0.0d));
                LOG.info("Duty: " + data.getDuty());
            }

            LOG.info("Currency: " + item.getCurrency());
            data.setCurrency(item.getCurrency());
            LOG.info("GoodsIssueDate: " + item.getGoodsIssueDate());
            data.setEstimatedShipDate(item.getGoodsIssueDate());
            productDataSimulationProductDatas.add(data);

        }
        return productDataSimulationProductDatas;
    }

    /**
     * @return the modificationStrategy
     */
    public CartModificationStrategy getModificationStrategy()
    {
        return modificationStrategy;
    }

    /**
     * @param modificationStrategy
     *            the modificationStrategy to set
     */
    public void setModificationStrategy(final CartModificationStrategy modificationStrategy)
    {
        this.modificationStrategy = modificationStrategy;
    }

}

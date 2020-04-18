package com.greenlee.facades.order.populators;

import de.hybris.platform.b2bacceleratorfacades.order.populators.B2BOrderPopulator;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.math.BigDecimal;


/**
 * Greenlee Order Populator populates {@link de.hybris.platform.commercefacades.order.data.OrderData} with
 * {@link OrderModel}.
 */
public class GreenleeB2BOrderPopulator extends B2BOrderPopulator
{

    private PriceDataFactory  priceDataFactory;
    private CommonI18NService commonI18NService;
    private Converter<DeliveryModeModel, DeliveryModeData> deliveryMethodConverter;
    private DeliveryService                                deliveryService;

    @Override
    public void populate(final OrderModel orderModel, final OrderData orderData) throws ConversionException
    {
        orderData.setShippedByDate(orderModel.getShippedByDate());
        orderData.setDuty(getPriceData(orderModel.getTotalDuty()));
        if (orderModel.getSapPriceAvailability() != null && orderModel.getSapPriceAvailability().booleanValue())
        {
            orderData.setSapPriceAvailability(orderModel.getSapPriceAvailability().booleanValue());
        }
        else
        {
            orderData.setSapPriceAvailability(false);
        }
        if (null != orderModel.getSelectedDeliveryMode())
        {
        orderData.setDeliveryMode(getDeliveryMethodConverter()
                .convert(getDeliveryService().getDeliveryModeForCode(orderModel.getSelectedDeliveryMode())));
        }
        if (null != orderModel.getDeliveryCost())
        {
            orderData.setDeliveryCost(getPriceData(orderModel.getDeliveryCost()));
        }

    }

    /**
     *
     */
    private PriceData getPriceData(final Double value)
    {
        return getPriceDataFactory().create(PriceDataType.BUY, BigDecimal.valueOf(value.doubleValue()),
                getCommonI18NService().getCurrentCurrency());
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
     * @return the commonI18NService
     */
    public CommonI18NService getCommonI18NService()
    {
        return commonI18NService;
    }

    /**
     * @param commonI18NService
     *            the commonI18NService to set
     */
    public void setCommonI18NService(final CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    /**
     * @return the deliveryMethodConverter
     */
    public Converter<DeliveryModeModel, DeliveryModeData> getDeliveryMethodConverter()
    {
        return deliveryMethodConverter;
    }

    /**
     * @param deliveryMethodConverter
     *            the deliveryMethodConverter to set
     */
    public void setDeliveryMethodConverter(final Converter<DeliveryModeModel, DeliveryModeData> deliveryMethodConverter)
    {
        this.deliveryMethodConverter = deliveryMethodConverter;
    }

    /**
     * @return the deliveryService
     */
    public DeliveryService getDeliveryService()
    {
        return deliveryService;
    }

    /**
     * @param deliveryService
     *            the deliveryService to set
     */
    public void setDeliveryService(final DeliveryService deliveryService)
    {
        this.deliveryService = deliveryService;
    }


}

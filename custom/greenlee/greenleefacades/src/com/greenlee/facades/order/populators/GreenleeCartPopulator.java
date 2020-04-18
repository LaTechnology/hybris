package com.greenlee.facades.order.populators;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.greenlee.core.constants.GreenleeCoreConstants;


/**
 * Greenlee Order Populator populates {@link de.hybris.platform.commercefacades.order.data.OrderData} with
 * {@link OrderModel}.
 */
public class GreenleeCartPopulator implements Populator<CartModel, CartData>
{

    private PriceDataFactory                               priceDataFactory;
    private CommonI18NService                              commonI18NService;
    private SessionService                                 sessionService;
    private Converter<AddressModel, AddressData>           addressConverter;
    private Converter<DeliveryModeModel, DeliveryModeData> deliveryMethodConverter;
    private Converter<B2BUnitModel, B2BUnitData>           b2bUnitConverter;
    private DeliveryService                                deliveryService;
    private static final Logger                            LOGGER = Logger.getLogger(GreenleeCartPopulator.class);

    protected Converter<AddressModel, AddressData> getAddressConverter()
    {
        return addressConverter;
    }

    @Required
    public void setAddressConverter(final Converter<AddressModel, AddressData> addressConverter)
    {
        this.addressConverter = addressConverter;
    }

    public DeliveryService getDeliveryService()
    {
        return deliveryService;
    }

    @Required
    public void setDeliveryService(final DeliveryService deliveryService)
    {
        this.deliveryService = deliveryService;
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

    @Override
    public void populate(final CartModel cartModel, final CartData cartData) throws ConversionException
    {
        cartData.setShippedByDate(cartModel.getShippedByDate());
        cartData.setShippingNote(cartModel.getShippingNote());
        cartData.setEmailccAddresses(new ArrayList<String>(cartModel.getEmailCCAddresses()));
        cartData.setDuty(getPriceData(cartModel.getTotalDuty()));
        cartData.setIsErpPrice(cartModel.getIsErpPrice().booleanValue());
        if (cartModel.getLivingstonString() != null)
        {
            cartData.setLivingstonString(cartModel.getLivingstonString());
        }
        /*        if (cartModel.getDeliveryMode()!=null && cartModel.getDeliveryMode().getCode() != null)
                {
                    cartData.setDeliveryMode(getDeliveryMethodConverter().convert(
                            getDeliveryService().getDeliveryModeForCode(cartModel.getDeliveryMode().getCode())));
                }*/
        cartData.setShippingAccountNumber(cartModel.getShipmentAccountNumber());
        cartData.setSapPriceAvailability(cartModel.getSapPriceAvailability().booleanValue());
        if (null != cartModel.getSelectedBillingAddress())
        {
            cartData.setBillingAddress(getAddressConverter().convert(cartModel.getSelectedBillingAddress(), new AddressData()));
        }
        if (null != cartModel.getPreviousDeliveryMode())
        {
            cartData.setDeliveryMode(getDeliveryMethodConverter().convert(
                    getDeliveryService().getDeliveryModeForCode(cartModel.getPreviousDeliveryMode().getCode())));
        }
        if (null != cartModel.getPreviousDeliveryMode())
        {
            cartData.setSelectedDeliveryMode(cartModel.getPreviousDeliveryMode().getCode());
        }
        //157
        if (null != cartModel.getDeliveryAddress())
        {
            LOGGER.info("cartModel to Data :: " + cartModel.getDeliveryAddress().getPk().getLongValueAsString());
            cartData.setDeliveryAddress(getAddressConverter().convert(cartModel.getDeliveryAddress(), new AddressData()));
        }
        /* if (null != cartModel.getErrorInCart())
         {
             cartData.setErrorInCart("ERROR");
         }*/
        final Session currentSession = sessionService.getCurrentSession();
        final B2BUnitModel b2bUnit = currentSession.getAttribute(GreenleeCoreConstants.SESSION_B2BUNIT_KEY);
        if (b2bUnit != null)
        {
            final B2BUnitData b2bUnitData = b2bUnitConverter.convert(b2bUnit);
            cartData.setUnit(b2bUnitData);
        }
    }

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
     * @return the sessionService
     */
    public SessionService getSessionService()
    {
        return sessionService;
    }

    /**
     * @param sessionService
     *            the sessionService to set
     */
    public void setSessionService(final SessionService sessionService)
    {
        this.sessionService = sessionService;
    }

    /**
     * @return the b2bUnitConverter
     */
    public Converter<B2BUnitModel, B2BUnitData> getB2bUnitConverter()
    {
        return b2bUnitConverter;
    }

    /**
     * @param b2bUnitConverter
     *            the b2bUnitConverter to set
     */
    public void setB2bUnitConverter(final Converter<B2BUnitModel, B2BUnitData> b2bUnitConverter)
    {
        this.b2bUnitConverter = b2bUnitConverter;
    }

}

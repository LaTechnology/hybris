/**
 *
 */
package com.greenlee.facades.cart.impl;

import de.hybris.platform.b2bacceleratorfacades.order.impl.DefaultCartFacade;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.TaxValue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.greenlee.core.cart.services.GreenleeCommerceCartService;
import com.greenlee.facades.cart.GreenLeeCartFacade;
import com.greenlee.facades.price.data.OrderSimulationData;
import com.greenlee.facades.price.data.OrderSimulationProductData;
import com.greenlee.facades.price.data.PriceDiffData;
import com.greenlee.facades.price.data.PriceDiffDataList;
import com.greenlee.pi.service.GreenleePIClientService;
import com.hybris.urn.xi.pricing_enquiry.DTHybrisPricingResponse;
import com.hybris.urn.xi.pricing_enquiry.DTHybrisPricingResponse.ORDERITEMS.ITEMS;


/**
 * @author nalini.ramarao
 *
 */
public class DefaultGreenLeeCartFacade extends DefaultCartFacade implements GreenLeeCartFacade
{
    private static final Logger                                     LOG                                        = Logger.getLogger(DefaultGreenLeeCartFacade.class);

    private static final String                                     COULD_NOT_MARSHALL_THE_RESPONSE_INTO_A_XML = "Could not marshall the response into a XML.";

    private Converter<DTHybrisPricingResponse, PriceDiffDataList>   realTimePricingResponseConverter;
    private ModelService                                            modelService;
    private GreenleePIClientService                                 greenleePIClientService;
    private GreenleeCommerceCartService                             greenleeCommerceCartService;
    private DeliveryService                                         deliveryService;
    private Converter<DTHybrisPricingResponse, OrderSimulationData> orderSimulationResponseConverter;
    private Converter<DeliveryModeModel, DeliveryModeData>          deliveryMethodConverter;
    private CalculationService                                      calculationService;
    private PriceDataFactory                                        priceDataFactory;
    private CommonI18NService                                       commonI18NService;


    /* (non-Javadoc)
     * @see com.greenlee.facades.cart.GreenLeeCartFacade#getRealTimePricing()
     */
    @SuppressWarnings("deprecation")
    @Override
    public PriceDiffDataList getRealTimePricing()
    {
        final CartModel cartModel = getCartService().getSessionCart();
        LOG.info("Get real time pricing.." + cartModel.getCode());
        final DTHybrisPricingResponse dtHybrisPricingResponse = getGreenleePIClientService().callPricingEnquiry(cartModel, false);
        LOG.info("Check if repsonse is null: " + (dtHybrisPricingResponse == null));
        if (dtHybrisPricingResponse != null)
        {
            LOG.info("Type: " + dtHybrisPricingResponse.getRETURN().getType());
            LOG.info("Mesage: " + dtHybrisPricingResponse.getRETURN().getMessage());
            if (dtHybrisPricingResponse.getORDERITEMS() != null)
            {
                LOG.info("Response Items size: " + Integer.valueOf(dtHybrisPricingResponse.getORDERITEMS().getITEMS().size()));
            }
            LOG.info("Freight: " + dtHybrisPricingResponse.getFreight());
            final Iterator<AbstractOrderEntryModel> entriesIterator = cartModel.getEntries().iterator();
            while (entriesIterator.hasNext())
            {
                final AbstractOrderEntryModel entryModel = entriesIterator.next();
                if (dtHybrisPricingResponse.getORDERITEMS() != null)
                {
                    for (final ITEMS cartItem : dtHybrisPricingResponse.getORDERITEMS().getITEMS())
                    {
                        LOG.info("entryModel.getEntryNumber(): " + entryModel.getEntryNumber());
                        LOG.info("cartItem.getHybrisItemNo(): " + cartItem.getHybrisItemNo());
                        if (entryModel.getEntryNumber().compareTo(Integer.valueOf(cartItem.getHybrisItemNo())) == 0)
                        {
                            LOG.info("total: " + cartItem.getSubtotal1());
                            LOG.info("qty: " + cartItem.getRequiredQuantity());
                            final double total = Double.parseDouble(cartItem.getSubtotal1());
                            final double qty = Double.parseDouble(cartItem.getRequiredQuantity());
                            LOG.info("entryModel.getBasePrice(): " + entryModel.getBasePrice());
                            if (entryModel.getBasePrice().compareTo(Double.valueOf(total / qty)) != 0)
                            {
                                LOG.info("entryModel.getProduct().getName(): " + entryModel.getProduct().getName());
                                LOG.info("entryModel.getBasePrice(): " + entryModel.getBasePrice());
                                LOG.info("entryModel.getEntryNumber(): " + entryModel.getEntryNumber());
                                final PriceDiffData data = new PriceDiffData();
                                data.setProductName(entryModel.getProduct().getName());
                                data.setActualPrice(createPriceData(entryModel.getBasePrice().doubleValue(), entryModel
                                        .getOrder().getCurrency().getIsocode()));
                                data.setDiscountedPrice(createPriceData(total, entryModel.getOrder().getCurrency().getIsocode()));
                                data.setEntryNumber(entryModel.getEntryNumber());
                                break;
                            }
                        }
                    }
                }
            }
        }
        final PriceDiffDataList dataList = realTimePricingResponseConverter.convert(dtHybrisPricingResponse);
        updateCartModel(dataList, StringUtils.isBlank(dtHybrisPricingResponse.getRETURN().getMessage()));
        return dataList;
    }

    private PriceData createPriceData(final double value, final String isoCodeCurrency)
    {
        return priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(value), isoCodeCurrency);
    }


    /**
     *
     * @param dataList
     * @param sapPriceAvailability
     */
    private void updateCartModel(final PriceDiffDataList dataList, final boolean sapPriceAvailability)
    {
        final CartModel cartModel = getCartService().getSessionCart();
        double totalPrice = 0.0d;
        final List<AbstractOrderEntryModel> abstractOrderEntryModels = cartModel.getEntries();
        for (final PriceDiffData data : dataList.getPriceDiffRow())
        {
            if (data.getEntryNumber() != null)
            {
                final AbstractOrderEntryModel orderEntryModel = abstractOrderEntryModels.get(data.getEntryNumber().intValue());
                if (data.getActualPrice() != null)
                {
                    orderEntryModel.setBasePrice(new Double(data.getDiscountedPrice().getValue().doubleValue()
                            / orderEntryModel.getQuantity().doubleValue()));
                    orderEntryModel.setTotalPrice(Double.valueOf(data.getDiscountedPrice().getValue().doubleValue()));
                    totalPrice += data.getDiscountedPrice().getValue().doubleValue();
                }
                orderEntryModel.setEstimatedShipDate(data.getEstimatedShipDate());
                modelService.save(orderEntryModel);
            }
        }
        cartModel.setTotalPrice(Double.valueOf(totalPrice));
        cartModel.setSapPriceAvailability(new Boolean(sapPriceAvailability));
        cartModel.setIsErpPrice(Boolean.TRUE);
        getCartService().setSessionCart(cartModel);
    }

    /* (non-Javadoc)
     * @see com.greenlee.facades.cart.GreenLeeCartFacade#setShippingNoteAndEmailAddressesForCart(java.lang.String, java.util.Collection)
     */
    @Override
    public void setShipDateShipNoteAndEmailAddressesForCart(final String deliveryModeCode, final String shippingNote,
            final Collection<String> emailAddresses, final Date shipByDate, final String shipmentAccountNumber)
    {
        final CartModel cartModel = getCartService().getSessionCart();
        final DeliveryModeModel deliveryModeModel = getDeliveryService().getDeliveryModeForCode(deliveryModeCode);
        if (deliveryModeModel != null)
        {
            cartModel.setPreviousDeliveryMode(deliveryModeModel);
        }
        cartModel.setSavedBy(getUserService().getCurrentUser()); // card created or saved by user.
        cartModel.setShippingNote(shippingNote);
        cartModel.setShipmentAccountNumber(shipmentAccountNumber);
        cartModel.setEmailCCAddresses(emailAddresses);
        cartModel.setShippedByDate(shipByDate);
        modelService.save(cartModel);
        modelService.refresh(cartModel);
    }

    @Override
    public CartModificationData updateCartEntry(final long entryNumber, final long quantity, final String serialNumbers)
            throws CommerceCartModificationException
    {
        final CartModel cartModel = getCartService().getSessionCart();
        final CommerceCartParameter parameter = new CommerceCartParameter();
        parameter.setEnableHooks(true);
        parameter.setCart(cartModel);
        parameter.setEntryNumber(entryNumber);
        parameter.setQuantity(quantity);
        parameter.setSerialNumbers(serialNumbers);

        final CommerceCartModification modification = getGreenleeCommerceCartService().updateSerialNoForCartEntry(parameter);

        return getCartModificationConverter().convert(modification);
    }

    /**
     * @return the greenleeCommerceCartService
     */
    public GreenleeCommerceCartService getGreenleeCommerceCartService()
    {
        return greenleeCommerceCartService;
    }

    /**
     * @param greenleeCommerceCartService
     *            the greenleeCommerceCartService to set
     */
    @Required
    public void setGreenleeCommerceCartService(final GreenleeCommerceCartService greenleeCommerceCartService)
    {
        this.greenleeCommerceCartService = greenleeCommerceCartService;
    }


    /* (non-Javadoc)
     * @see com.greenlee.facades.cart.GreenLeeCartFacade#getDeliveryMethodForCode(java.lang.String)
     */
    @Override
    public DeliveryModeData getDeliveryMethodForCode(final String deliveryMethodCode)
    {
        return deliveryMethodConverter.convert(deliveryService.getDeliveryModeForCode(deliveryMethodCode));
    }

    /**
     * @return the deliveryService
     */
    @Override
    public DeliveryService getDeliveryService()
    {
        return deliveryService;
    }

    /**
     * @param deliveryService
     *            the deliveryService to set
     */
    @Override
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

    /* (non-Javadoc)
     * @see com.greenlee.facades.cart.GreenLeeCartFacade#getOrderSimulation()
     */
    @SuppressWarnings("deprecation")
    @Override
    public OrderSimulationData getOrderSimulation(final boolean isSimulate) throws CalculationException
    {
        final CartModel cartModel = getCartService().getSessionCart();
        LOG.info("Get order simulation for cart " + cartModel.getCode());
        final DTHybrisPricingResponse dtHybrisPricingResponse = getGreenleePIClientService().callPricingEnquiry(cartModel,
                isSimulate);

        if (!isSimulate)
        {
            cartModel.setIsErpPrice(Boolean.TRUE);
        }
        final OrderSimulationData simulatedData = orderSimulationResponseConverter.convert(dtHybrisPricingResponse);
        if (!StringUtils.equalsIgnoreCase(simulatedData.getErrorMsg(), "null")
                && StringUtils.isNotBlank(simulatedData.getErrorMsg()) && StringUtils.isNotEmpty(simulatedData.getErrorMsg()))
        {
            LOG.error("getOrderSimulation >> ERR_NTFY_SUPPORT_0006 - Real time order simulation failed for customer. Reason: "
                    + simulatedData.getErrorMsg() + " cart number " + cartModel.getCode());
            return simulatedData;
        }
        updateOrderSimulation(simulatedData, cartModel);
        calculationService.calculateTotals(cartModel, true);

        LOG.info("Saving the cart as session cart");
        getCartService().setSessionCart(cartModel);
        LOG.info("Retrieving the cart from session cart");
        final CartModel debugCart = getCartService().getSessionCart();
        for (final AbstractOrderEntryModel entryModel : debugCart.getEntries())
        {
            LOG.info("Debug Cart Base price of entry is " + entryModel.getBasePrice());
            LOG.info("Debug Cart EstimatedShipDate of entry is " + entryModel.getEstimatedShipDate());
            LOG.info("Debug Cart TotalPrice of entry is " + entryModel.getTotalPrice());
        }
        LOG.info("Debug Cart TotalPrice of cart is " + debugCart.getTotalPrice());
        LOG.info("Debug Cart TotalTax of cart is " + debugCart.getTotalTax());
        LOG.info("Debug Cart TotalDuty of cart is " + debugCart.getTotalDuty());
        LOG.info("Debug Cart ShippingCost of cart is " + debugCart.getDeliveryCost());
        return simulatedData;
    }

    /**
     * update unit price, total price, total tax and estimate ship date
     *
     * @param orderSimulationData
     * @param cartModel
     */
    protected void updateOrderSimulation(final OrderSimulationData orderSimulationData, final CartModel cartModel)
    {
        double totalPrice = 0.0d;
        double totalTax = 0.0d;
        double totalDuty = 0.0d;
        final List<AbstractOrderEntryModel> abstractOrderEntryModels = cartModel.getEntries();
        if (orderSimulationData.getProducts() != null && !orderSimulationData.getProducts().isEmpty())
        {
            for (final OrderSimulationProductData productData : orderSimulationData.getProducts())
            {
                if (productData.getEntryNumber() != null)
                {
                    final AbstractOrderEntryModel entryModel = abstractOrderEntryModels.get(productData.getEntryNumber()
                            .intValue());
                    LOG.info("DefaultGreenLeeCartFacade >> AbstractOrderEntryModel : " + entryModel.getEntryNumber()
                            + " ItemNumber From PI :: " + productData.getEntryNumber().intValue());
                    if (!StringUtils.equalsIgnoreCase(cartModel.getCurrency().getIsocode(), productData.getCurrency()))
                    {
                        LOG.error("Currency set in order is " + cartModel.getCurrency().getIsocode()
                                + ", but currency in response from SAP is " + productData.getCurrency());
                        //final CurrencyModel currencyModel = commonI18NService.getCurrency(productData.getCurrency());
                        //cartModel.setCurrency(currencyModel);
                        orderSimulationData.setErrorMsg("Apologies. We could not proceed with checkout due to currency mismatch");
                        LOG.error("ERR_NTFY_SUPPORT_0006 - Real time order simulation failed for customer. Reason: "
                                + orderSimulationData.getErrorMsg() + " cart number " + cartModel.getCode());
                        return;
                    }
                    if (entryModel.getBasePrice() != null)
                    {
                        LOG.info("Base price of entry was"
                                + (entryModel.getBasePrice().doubleValue() / entryModel.getQuantity().longValue()));
                        entryModel.setBasePrice(Double.valueOf(productData.getDiscountedPrice().doubleValue()
                                / entryModel.getQuantity().longValue()));
                    }
                    LOG.info("Base price of entry is " + entryModel.getBasePrice());
                    LOG.info("EstimatedShipDate of entry was " + entryModel.getEstimatedShipDate());
                    entryModel.setEstimatedShipDate(productData.getEstimatedShipDate());
                    LOG.info("EstimatedShipDate of entry is " + entryModel.getEstimatedShipDate());

                    LOG.info("TotalPrice of entry was " + entryModel.getTotalPrice());
                    entryModel.setTotalPrice(Double.valueOf(productData.getDiscountedPrice().doubleValue()));
                    LOG.info("TotalPrice of entry is " + entryModel.getTotalPrice());

                    final Collection<TaxValue> taxValues = new ArrayList<>();
                    final TaxValue sapProvidedTaxValue = new TaxValue("PI Provided for entry " + productData.getEntryNumber(),
                            productData.getTax().doubleValue(), true, productData.getTax().doubleValue(), cartModel.getCurrency()
                                    .getIsocode());
                    taxValues.add(sapProvidedTaxValue);
                    entryModel.setTaxValues(taxValues);

                    LOG.info("TaxValue " + entryModel.getTaxValues());
                    totalPrice += productData.getDiscountedPrice().doubleValue();
                    totalDuty += productData.getDuty().doubleValue();
                    modelService.save(entryModel);
                    LOG.info("Saving the entryModel");
                }
            }
        }
        if (orderSimulationData.getTotalTax() != null && orderSimulationData.getTotalTax().doubleValue() > 0d)
        {
            totalTax = orderSimulationData.getTotalTax().doubleValue();
        }
        LOG.info("TotalPrice of cart was " + cartModel.getTotalPrice());
        cartModel.setTotalPrice(Double.valueOf(totalPrice));
        LOG.info("TotalPrice of cart is " + cartModel.getTotalPrice());

        LOG.info("TotalTax of cart was " + cartModel.getTotalTax());
        cartModel.setTotalTax(Double.valueOf(totalTax));
        LOG.info("TotalTax of cart is " + cartModel.getTotalTax());

        LOG.info("TotalDuty of cart was " + cartModel.getTotalDuty());
        cartModel.setTotalDuty(Double.valueOf(totalDuty));
        LOG.info("TotalDuty of cart is " + cartModel.getTotalDuty());

        LOG.info("ShippingCost of cart was " + cartModel.getDeliveryCost());
        cartModel.setDeliveryCost(orderSimulationData.getShippingCost());
        LOG.info("ShippingCost of cart is " + cartModel.getDeliveryCost());

        if (StringUtils.isBlank(orderSimulationData.getErrorMsg()))
        {
            cartModel.setSapPriceAvailability(Boolean.TRUE);
        }
        modelService.save(cartModel);
        LOG.info("Saving the cartModel");
    }

    /**
     * @return the calculationService
     */
    public CalculationService getCalculationService()
    {
        return calculationService;
    }

    /**
     * @param calculationService
     *            the calculationService to set
     */
    public void setCalculationService(final CalculationService calculationService)
    {
        this.calculationService = calculationService;
    }

    /**
     * @return the priceDataFactory
     */
    @Override
    public PriceDataFactory getPriceDataFactory()
    {
        return priceDataFactory;
    }

    /**
     * @param priceDataFactory
     *            the priceDataFactory to set
     */
    @Override
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
     * @return the orderSimulationResponseConverter
     */
    public Converter<DTHybrisPricingResponse, OrderSimulationData> getOrderSimulationResponseConverter()
    {
        return orderSimulationResponseConverter;
    }

    /**
     * @param orderSimulationResponseConverter
     *            the orderSimulationResponseConverter to set
     */
    public void setOrderSimulationResponseConverter(
            final Converter<DTHybrisPricingResponse, OrderSimulationData> orderSimulationResponseConverter)
    {
        this.orderSimulationResponseConverter = orderSimulationResponseConverter;
    }

    /**
     * @return the greenleePIClientService
     */
    public GreenleePIClientService getGreenleePIClientService()
    {
        return greenleePIClientService;
    }

    /**
     * @param greenleePIClientService
     *            the greenleePIClientService to set
     */
    public void setGreenleePIClientService(final GreenleePIClientService greenleePIClientService)
    {
        this.greenleePIClientService = greenleePIClientService;
    }

    /**
     * @return the modelService
     */
    public ModelService getModelService()
    {
        return modelService;
    }

    /**
     * @param modelService
     *            the modelService to set
     */
    public void setModelService(final ModelService modelService)
    {
        this.modelService = modelService;
    }

    /**
     * @return the realTimePricingResponseConverter
     */
    public Converter<DTHybrisPricingResponse, PriceDiffDataList> getRealTimePricingResponseConverter()
    {
        return realTimePricingResponseConverter;
    }

    /**
     * @param realTimePricingResponseConverter
     *            the realTimePricingResponseConverter to set
     */
    public void setRealTimePricingResponseConverter(
            final Converter<DTHybrisPricingResponse, PriceDiffDataList> realTimePricingResponseConverter)
    {
        this.realTimePricingResponseConverter = realTimePricingResponseConverter;
    }

    public void resetCheckoutAttributes() throws CalculationException
    {
        final CartModel cartModel = getCartService().getSessionCart();
        if (cartModel.getDeliveryAddress() != null)
        {
            cartModel.setDeliveryMode(null);
            cartModel.setShipmentAccountNumber(null);
            cartModel.setDeliveryCost(Double.valueOf(0));
            cartModel.setTotalDuty(Double.valueOf(0));
            cartModel.setTotalTaxValues(Collections.EMPTY_LIST);
            for (final AbstractOrderEntryModel entryModel : cartModel.getEntries())
            {
                entryModel.setTaxValues(Collections.EMPTY_LIST);
                modelService.save(entryModel);
            }

            modelService.save(cartModel);
            calculationService.calculateTotals(cartModel, true);
            getCartService().setSessionCart(cartModel);
        }

    }
}

/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.greenlee.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.NumberTool;
import org.springframework.beans.factory.annotation.Required;

import com.greenlee.facades.constants.GreenleeFacadesConstants;


/**
 * Velocity context for a order cancelled email.
 */
public class OrderCancelledEmailContext extends AbstractEmailContext<OrderProcessModel>
{
    private Converter<OrderModel, OrderData>     orderConverter;
    private Converter<B2BUnitModel, B2BUnitData> b2bUnitConverter;
    private OrderData                            orderData;
    private String                               orderCode;
    private String                               orderGuid;
    private boolean                              guest;
    private String                               storeName;
    // Greenlee customer service mail id changed - GRE-1850
    private static final String                  CUSTOMERSERVICE_EMAIL = Config.getString("website.customer.service.email",
                                                                               "CSCommunications@Greenlee.textron.com");


    @Override
    public void init(final OrderProcessModel orderProcessModel, final EmailPageModel emailPageModel)
    {
        super.init(orderProcessModel, emailPageModel);
        orderData = getOrderConverter().convert(orderProcessModel.getOrder());
        final B2BUnitData b2bUnitData = b2bUnitConverter.convert(orderProcessModel.getOrder().getUnit());
        orderData.setUnit(b2bUnitData);
        orderCode = orderProcessModel.getOrder().getCode();
        orderGuid = orderProcessModel.getOrder().getGuid();
        guest = CustomerType.GUEST.equals(getCustomer(orderProcessModel).getType());
        storeName = orderProcessModel.getOrder().getStore().getName();

        put(GreenleeFacadesConstants.EMAIL_CC_ADDRESSES, orderProcessModel.getOrder().getEmailCCAddresses());
        final Collection<String> additionalToEmails = new ArrayList<String>();
        additionalToEmails.add(CUSTOMERSERVICE_EMAIL);
        //        put(GreenleeFacadesConstants.ADDITIONAL_TO_EMAIL_ADDRESSES, additionalToEmails); //GSM-63

        put(GreenleeFacadesConstants.DATE_TOOL, new DateTool());
        put(GreenleeFacadesConstants.NUMBER_TOOL, new NumberTool());

    }

    @Override
    protected BaseSiteModel getSite(final OrderProcessModel orderProcessModel)
    {
        return orderProcessModel.getOrder().getSite();
    }

    @Override
    protected CustomerModel getCustomer(final OrderProcessModel orderProcessModel)
    {
        return (CustomerModel) orderProcessModel.getOrder().getUser();
    }

    protected Converter<OrderModel, OrderData> getOrderConverter()
    {
        return orderConverter;
    }

    @Required
    public void setOrderConverter(final Converter<OrderModel, OrderData> orderConverter)
    {
        this.orderConverter = orderConverter;
    }

    public OrderData getOrder()
    {
        return orderData;
    }

    @Override
    protected LanguageModel getEmailLanguage(final OrderProcessModel orderProcessModel)
    {
        return orderProcessModel.getOrder().getLanguage();
    }

    public OrderData getOrderData()
    {
        return orderData;
    }

    public void setOrderData(final OrderData orderData)
    {
        this.orderData = orderData;
    }

    public String getOrderCode()
    {
        return orderCode;
    }

    public void setOrderCode(final String orderCode)
    {
        this.orderCode = orderCode;
    }

    public String getOrderGuid()
    {
        return orderGuid;
    }

    public void setOrderGuid(final String orderGuid)
    {
        this.orderGuid = orderGuid;
    }

    public boolean isGuest()
    {
        return guest;
    }

    public void setGuest(final boolean guest)
    {
        this.guest = guest;
    }

    public String getStoreName()
    {
        return storeName;
    }

    public void setStoreName(final String storeName)
    {
        this.storeName = storeName;
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

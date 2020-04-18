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
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.NumberTool;
import org.springframework.beans.factory.annotation.Required;

import com.greenlee.facades.constants.GreenleeFacadesConstants;


/**
 * Velocity context for a order notification email.
 */
public class OrderNotificationEmailContext extends AbstractEmailContext<OrderProcessModel>
{
    private Converter<OrderModel, OrderData>     orderConverter;
    private Converter<B2BUnitModel, B2BUnitData> b2bUnitConverter;
    private OrderData                            orderData;
    public static final String                   USERTYPE              = "usertype";
    public static final String                   COMPANYNAME           = "companyName";
    private static final String                  CUSTOMERSERVICE_EMAIL = Config.getString("website.customer.service.email",
                                                                               "CSCommunications@Greenlee.textron.com");

    @Override
    public void init(final OrderProcessModel orderProcessModel, final EmailPageModel emailPageModel)
    {
        super.init(orderProcessModel, emailPageModel);
        orderData = getOrderConverter().convert(orderProcessModel.getOrder());
        final B2BUnitData b2bUnitData = b2bUnitConverter.convert(orderProcessModel.getOrder().getUnit());
        orderData.setUnit(b2bUnitData);
        put(GreenleeFacadesConstants.EMAIL_CC_ADDRESSES, orderProcessModel.getOrder().getEmailCCAddresses());
        final Collection<String> additionalToEmails = new ArrayList<String>();
        additionalToEmails.add(CUSTOMERSERVICE_EMAIL);
        //put(GreenleeFacadesConstants.ADDITIONAL_TO_EMAIL_ADDRESSES, additionalToEmails); GSM-63
        put(GreenleeFacadesConstants.DATE_TOOL, new DateTool());
        put(GreenleeFacadesConstants.NUMBER_TOOL, new NumberTool());
        put(GreenleeFacadesConstants.STRING_TOOL, new StringUtils());
        put(USERTYPE, (b2bUnitData.getUserType() != null) ? b2bUnitData.getUserType() : "");
        if (StringUtils.isNotEmpty(b2bUnitData.getUserType()) && b2bUnitData.getUserType() != "B2C")
        {
            put(COMPANYNAME, (b2bUnitData.getName() != null) ? b2bUnitData.getName() : "");
        }
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

    /**
     * @param b2bUnitConverter
     *            the b2bUnitConverter to set
     */
    public void setB2bUnitConverter(final Converter<B2BUnitModel, B2BUnitData> b2bUnitConverter)
    {
        this.b2bUnitConverter = b2bUnitConverter;
    }



}

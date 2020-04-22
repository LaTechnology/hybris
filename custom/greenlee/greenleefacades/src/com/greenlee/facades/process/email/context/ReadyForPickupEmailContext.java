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
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.ConsignmentData;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Required;

import com.greenlee.facades.constants.GreenleeFacadesConstants;


/**
 * Velocity context for a Ready For Pickup notification email.
 */
public class ReadyForPickupEmailContext extends AbstractEmailContext<ConsignmentProcessModel>
{
    private Converter<ConsignmentModel, ConsignmentData> consignmentConverter;
    private ConsignmentData                              consignmentData;
    private String                                       orderCode;
    private String                                       orderGuid;
    private boolean                                      guest;
    private static final String                          CUSTOMERSERVICE_EMAIL = Config.getString(
                                                                                       "website.customer.service.email",
                                                                                       "CSCommunications@Greenlee.textron.com");

    @Override
    public void init(final ConsignmentProcessModel consignmentProcessModel, final EmailPageModel emailPageModel)
    {
        super.init(consignmentProcessModel, emailPageModel);
        orderCode = consignmentProcessModel.getConsignment().getOrder().getCode();
        orderGuid = consignmentProcessModel.getConsignment().getOrder().getGuid();
        consignmentData = getConsignmentConverter().convert(consignmentProcessModel.getConsignment());
        put(GreenleeFacadesConstants.EMAIL_CC_ADDRESSES, consignmentProcessModel.getConsignment().getOrder()
                .getEmailCCAddresses());
        final Collection<String> additionalToEmails = new ArrayList<String>();
        additionalToEmails.add(CUSTOMERSERVICE_EMAIL);
        put(GreenleeFacadesConstants.ADDITIONAL_TO_EMAIL_ADDRESSES, additionalToEmails);

        guest = CustomerType.GUEST.equals(getCustomer(consignmentProcessModel).getType());
    }

    @Override
    protected BaseSiteModel getSite(final ConsignmentProcessModel consignmentProcessModel)
    {
        return consignmentProcessModel.getConsignment().getOrder().getSite();
    }

    @Override
    protected CustomerModel getCustomer(final ConsignmentProcessModel consignmentProcessModel)
    {
        return (CustomerModel) consignmentProcessModel.getConsignment().getOrder().getUser();
    }

    protected Converter<ConsignmentModel, ConsignmentData> getConsignmentConverter()
    {
        return consignmentConverter;
    }

    @Required
    public void setConsignmentConverter(final Converter<ConsignmentModel, ConsignmentData> consignmentConverter)
    {
        this.consignmentConverter = consignmentConverter;
    }

    public ConsignmentData getConsignment()
    {
        return consignmentData;
    }

    public String getOrderCode()
    {
        return orderCode;
    }

    public String getOrderGuid()
    {
        return orderGuid;
    }

    public boolean isGuest()
    {
        return guest;
    }

    @Override
    protected LanguageModel getEmailLanguage(final ConsignmentProcessModel consignmentProcessModel)
    {
        if (consignmentProcessModel.getConsignment().getOrder() instanceof OrderModel)
        {
            return ((OrderModel) consignmentProcessModel.getConsignment().getOrder()).getLanguage();
        }

        return null;
    }

}
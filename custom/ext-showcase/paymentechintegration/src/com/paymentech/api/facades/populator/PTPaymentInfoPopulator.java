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
package com.paymentech.api.facades.populator;

import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;


/**
 */
public class PTPaymentInfoPopulator implements Populator<CreditCardPaymentInfoModel, CCPaymentInfoData>
{

	@Override
	public void populate(final CreditCardPaymentInfoModel source, final CCPaymentInfoData target)
	{
		target.setProfileID(source.getCustomerRefNumber());
	}
}

/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.hybris.gold.lt.fulfilmentprocess.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import com.hybris.gold.lt.fulfilmentprocess.constants.GoldFulfilmentProcessConstants;

@SuppressWarnings("PMD")
public class GoldFulfilmentProcessManager extends GeneratedGoldFulfilmentProcessManager
{
	public static final GoldFulfilmentProcessManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (GoldFulfilmentProcessManager) em.getExtension(GoldFulfilmentProcessConstants.EXTENSIONNAME);
	}
	
}

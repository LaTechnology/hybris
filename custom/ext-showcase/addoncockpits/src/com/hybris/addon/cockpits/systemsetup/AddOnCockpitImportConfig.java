/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 * 
 */
package com.hybris.addon.cockpits.systemsetup;

import de.hybris.platform.cockpit.systemsetup.CockpitImportConfig;

import java.util.Map;


/**
 * @author rmcotton
 * 
 */
public class AddOnCockpitImportConfig extends CockpitImportConfig
{
	private Map<String, String> ctxID2FactoryMappings = null;

	@Override
	public void setCtxID2FactoryMappings(final Map<String, String> ctxID2FactoryMappings)
	{
		this.ctxID2FactoryMappings = ctxID2FactoryMappings;
		super.setCtxID2FactoryMappings(ctxID2FactoryMappings);
	}

	public Map<String, String> getCtxID2FactoryMappings()
	{
		return this.ctxID2FactoryMappings;
	}
}

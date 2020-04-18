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
package com.hybris.addon.cockpits.widgets.impl;

import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cockpit.widgets.impl.DefaultWidgetConfig;

import org.springframework.beans.factory.annotation.Required;

import com.hybris.addon.cockpits.widgets.AddOnWidgetConfig;


/**
 * @author rmcotton
 * 
 */
public class DefaultAddOnWidgetConfig<T extends Widget> extends DefaultWidgetConfig implements AddOnWidgetConfig<T>
{
	private String slotId;
	private String cssPath;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.addon.cockpits.widgets.AddOnWidgetConfig#getSlotSclass()
	 */
	@Override
	public String getSlotId()
	{
		return slotId;
	}

	@Required
	public void setSlotId(final String slotId)
	{
		this.slotId = slotId;
	}

	public void setCssPath(final String path)
	{
		this.cssPath = path;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.addon.cockpits.widgets.AddOnWidgetConfig#getCssPath()
	 */
	@Override
	public String getCssPath()
	{
		return this.cssPath;
	}

}
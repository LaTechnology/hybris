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
package com.hybris.addon.cockpits.widgets;

import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cockpit.widgets.WidgetConfig;


/**
 * @author rmcotton
 * 
 */
public interface AddOnWidgetConfig<T extends Widget> extends WidgetConfig
{
	String getSlotId();

	String getCssPath();
}

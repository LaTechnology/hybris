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
package com.hybris.addon.cscockpit.widget.models.impl;

import de.hybris.platform.cockpit.widgets.WidgetConfig;
import de.hybris.platform.cscockpit.widgets.models.impl.CsWidgetBrowserModel;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * @author rmcotton
 * 
 */
public class AddOnCsWidgetBrowserModel extends CsWidgetBrowserModel implements ApplicationContextAware, InitializingBean
{

	private ApplicationContext appContext;
	private List<String> additionalWidgetMapBeanIds;

	public void setAdditionalWidgetMapBeanIds(final List<String> additionalWidgetMapBeanIds)
	{
		this.additionalWidgetMapBeanIds = additionalWidgetMapBeanIds;
	}

	public List<String> getAdditionalWidgetMapBeanIds()
	{
		return this.additionalWidgetMapBeanIds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.
	 * ApplicationContext)
	 */
	@Override
	public void setApplicationContext(final ApplicationContext appContext) throws BeansException
	{
		this.appContext = appContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception
	{
		if (getAdditionalWidgetMapBeanIds() != null)
		{
			final List<String> widgetCodes = new LinkedList<String>(getWidgetFocusOrder());
			final Map<String, WidgetConfig> widgetMap = new LinkedHashMap<String, WidgetConfig>(getWidgetMap());

			for (final String beanId : getAdditionalWidgetMapBeanIds())
			{
				final WidgetMapKey key = appContext.getBean(beanId, WidgetMapKey.class);
				widgetCodes.add(key.getWidgetCode());
				widgetMap.put(key.getWidgetCode(), key.getWidgetConfig());
			}

			setWidgetMap(widgetMap);
			setWidgetFocusOrder(widgetCodes);
		}

	}
}

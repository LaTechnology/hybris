///*
// * [y] hybris Platform
// *
// * Copyright (c) 2000-2013 hybris AG
// * All rights reserved.
// *
// * This software is the confidential and proprietary information of hybris
// * ("Confidential Information"). You shall not disclose such Confidential
// * Information and shall use it only in accordance with the terms of the
// * license agreement you entered into with hybris.
// *
// *
// */
//package com.hybris.addon.cockpits.components.liveedit;
//
//import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//
//
///**
// * @author rmcotton
// *
// */
//public class DefaultCallbackEventHandlerRegistry implements CallbackEventHandlerRegistry<LiveEditView>, InitializingBean,
//		ApplicationContextAware
//{
//	private Map<String, CallbackEventHandler> callbackEventHandlers;
//	private ApplicationContext applicationContext;
//
//	/*
//	 * (non-Javadoc)
//	 *
//	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
//	 */
//	@Override
//	public void afterPropertiesSet() throws Exception
//	{
//		this.callbackEventHandlers = new HashMap<String, CallbackEventHandler>();
//
//		for (final CallbackEventHandler<LiveEditView> handler : this.applicationContext.getBeansOfType(
//				com.hybris.addon.cockpits.components.liveedit.CallbackEventHandler.class).values())
//		{
//			this.callbackEventHandlers.put(handler.getEventId(), handler);
//		}
//	}
//
//	/*
//	 * (non-Javadoc)
//	 *
//	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.
//	 * ApplicationContext)
//	 */
//	@Override
//	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException
//	{
//		this.applicationContext = applicationContext;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 *
//	 * @see com.hybris.addon.cockpits.components.liveedit.CallbackEventHandlerRegistry#getHandlerById(java.lang.String)
//	 */
//	@Override
//	public CallbackEventHandler getHandlerById(final String id)
//	{
//		return this.callbackEventHandlers.get(id);
//	}
//}

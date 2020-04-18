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
//import org.zkoss.zk.ui.Component;
//import org.zkoss.zk.ui.HtmlBasedComponent;
//import org.zkoss.zk.ui.event.Events;
//import org.zkoss.zul.Button;
//import org.zkoss.zul.Caption;
//import org.zkoss.zul.Window;
//
//
///**
// * @author rmcotton
// * 
// */
//public abstract class AbstractLiveEditCaptionButtonHandler implements LiveEditCaptionButtonHandler
//{
//	protected Button createRightCaptionButton(final String label, final String sClass, final HtmlBasedComponent parent,
//			final org.zkoss.zk.ui.event.EventListener listener)
//	{
//		final Button button = new Button(label);
//		button.setSclass(sClass);
//		button.addEventListener(Events.ON_CLICK, listener);
//		parent.appendChild(button);
//		return button;
//	}
//
//	protected Window createPopupWindow(final Component parent)
//	{
//		//popup window
//		final Window popupwindow = new Window();
//		popupwindow.setParent(parent);
//		popupwindow.setSclass("popupwindow");
//		popupwindow.setVisible(false);
//		popupwindow.setWidth("800px");
//		popupwindow.setHeight("800px");
//		popupwindow.setBorder("none");
//		popupwindow.setClosable(true);
//		popupwindow.setMaximizable(false);
//		popupwindow.setSizable(false);
//		popupwindow.setShadow(false);
//		popupwindow.setAction("onhide: anima.fade(#{self}); onshow: anima.appear(#{self});");
//
//		final Caption caption = new Caption();
//		caption.setParent(popupwindow);
//
//		return popupwindow;
//	}
//
//}

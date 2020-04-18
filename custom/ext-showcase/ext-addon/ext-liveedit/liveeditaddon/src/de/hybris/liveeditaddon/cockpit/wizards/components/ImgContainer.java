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
package de.hybris.liveeditaddon.cockpit.wizards.components;

import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;


/**
 */
public class ImgContainer extends Div
{

	private final String IMAGE_CONTAINER_SCLASS = "productImageMain";
	private final String ACTION_CONTAINER_SCLASS = "actionContainer";
	private final String DROP_ID = "PRODUCT_IMAGE_DROP_ID";
	private final String url;

	private Image image;

	public ImgContainer()
	{
		this.url = "";
		init(true);
	}

	public ImgContainer(String url)
	{
		this.url = url;
		init(true);
	}

	public ImgContainer(String url, boolean showRemoveButton)
	{
		this.url = url;
		init(showRemoveButton);
	}

	private void renderRemoveButton()
	{
		RemoveButton removeButton = new RemoveButton();
		removeButton.addEventListener(Events.ON_CLICK, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				image.setStyle("visibility:hidden;");
				ImgContainer.this.setStyle("background-color:#eee;");
			}
		});
		removeButton.setParent(this);
	}

	private void init(boolean showRemoveButton)
	{
		this.setSclass(IMAGE_CONTAINER_SCLASS);
		if (StringUtils.isNotBlank(url))
		{
			image = new Image(url);
			image.setParent(this);
		}
		if (showRemoveButton)
		{
			renderRemoveButton();
		}
		this.setDroppable(DROP_ID);
		this.setDraggable(DROP_ID);
		attachHoverAction();
	}

	private void attachHoverAction()
	{
		this.setAction("onmouseover:var actionDiv=this.getElementsByClassName('actionContainer')[0];"
				+ "actionDiv.style.visibility='visible';"
				+ "onmouseout:var actionDiv=this.getElementsByClassName('actionContainer')[0];"
				+ "actionDiv.style.visibility='hidden';");
	}

}

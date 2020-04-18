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

import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.metainfo.EventHandler;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;


/**
 */
public class MainTile extends Div implements IdSpace
{
	private static final long serialVersionUID = 1L;

	private final String ROOT_SCLASS = "productImageMain";

	public RemoveButton removeButton;

	private EventListener removeClickButtonListener;

	public MainTile()
	{
		init();
	}


	private void init()
	{
		this.setSclass(ROOT_SCLASS);
		this.setDroppable("PRODUCT_IMAGE_DROP_ID");
		this.setDraggable("PRODUCT_IMAGE_DROP_ID");
	}

	public boolean addRemoveEventListener(final String name, final EventListener listener)
	{
		if (removeButton == null)
		{
			return false;
		}

		if (name.equals(Events.ON_CLICK))
		{
			removeClickButtonListener = listener;
			return true;
		}
		else
		{
			return this.removeButton.addEventListener(name, listener);
		}
	}

	public void addRemoveEventHandler(final String name, final EventHandler handler)
	{
		if (removeButton != null)
		{
			this.removeButton.addEventHandler(name, handler);
		}
	}

	public void renderImage(final String pathToImage)
	{

		removeButton = new RemoveButton();
		final Div imgContainer = new Div();
		final Image img = new Image(pathToImage);

		removeButton.addEventListener(Events.ON_CLICK, new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception //NOPMD: ZK specific
			{
				imgContainer.detach();
				img.setStyle("visibility:hidden;");
				removeButton.detach();
				imgContainer.setStyle("background-color:#eee;");
				if (removeClickButtonListener != null)
				{
					removeClickButtonListener.onEvent(event);
				}
			}
		});

		imgContainer.setSclass(ROOT_SCLASS);

		img.setParent(imgContainer);
		removeButton.setParent(imgContainer);
		imgContainer.setParent(this);
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

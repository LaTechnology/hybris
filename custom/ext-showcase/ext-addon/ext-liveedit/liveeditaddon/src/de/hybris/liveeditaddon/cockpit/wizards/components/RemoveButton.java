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

import de.hybris.platform.cockpit.constants.ImageUrls;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;


/**
 */
public class RemoveButton extends Div
{
	private final String ACTION_CONTAINER_SCLASS = "actionContainer";

	private Image removeButtonImage;

	private EventListener clickEventListener;

	public RemoveButton()
	{
		init();
	}

	private void init()
	{
		removeButtonImage = new Image(ImageUrls.REMOVE_BUTTON_IMAGE);

		this.setSclass(ACTION_CONTAINER_SCLASS);
		removeButtonImage.setParent(this);

		removeButtonImage.addEventListener(Events.ON_CLICK, new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				if (clickEventListener != null)
				{
					clickEventListener.onEvent(event);
				}
				handleClickEvent();
			}
		});
	}

	private void handleClickEvent()
	{
		this.detach();
	}

	@Override
	public boolean addEventListener(final String name, final EventListener listener)
	{
		if (name.equals(Events.ON_CLICK))
		{
			clickEventListener = listener;
			return true;
		}
		else
		{
			return this.removeButtonImage.addEventListener(name, listener);
		}
	}
}

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

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.metainfo.EventHandler;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;


/**
 */
public class ImageTile extends Div implements IdSpace
{
	private static final long serialVersionUID = 1L;

	private final String ROOT_SCLASS = "filedrop";
	private final String BUTTON_SCLASS = "message";
	private final String LABEL_KEY_PATTERN = "button.label.format.";
	private final String GALLERY_SCLASS = "productImageGallery";

	public Button button;
	public RemoveButton removeButton;

	private boolean ready;
	private String format;
	private String label;
	private EventListener removeClickButtonListener;


	public ImageTile()
	{
		init();
	}

	public ImageTile(final String label, final String format)
	{
		this.format = format;
		init();
		this.setLabel(label);
	}

	public String getLabel()
	{
		return this.label;
	}

	public void setLabel(final String label)
	{
		final String label_key = LABEL_KEY_PATTERN + label;
		String labelFromProperties = Labels.getLabel(label_key);
		if (labelFromProperties == null || labelFromProperties.isEmpty())
		{
			labelFromProperties = label;
		}
		this.label = labelFromProperties;
		this.setTooltiptext(labelFromProperties);
		this.button.setLabel(labelFromProperties);
	}

	private void init()
	{
		button = new Button();
		button.setParent(this);
		button.setSclass(BUTTON_SCLASS);
		this.setSclass(ROOT_SCLASS);
	}

	public boolean isReady()
	{
		return ready;
	}

	public void setReady(final boolean ready)
	{
		this.ready = ready;
	}

	public String getFormat()
	{
		return format;
	}

	public void setFormat(final String format)
	{
		this.format = format;
	}

	@Override
	public boolean addEventListener(final String name, final EventListener listener)
	{
		return this.button.addEventListener(name, listener);
	}

	@Override
	public void addEventHandler(final String name, final EventHandler handler)
	{
		this.button.addEventHandler(name, handler);
	}

	public void hideTitle()
	{
		this.getFirstChild().setVisible(false);
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

	public void renderPreviewImage(final String pathToImage)
	{
		this.button.setVisible(false);

		removeButton = new RemoveButton();
		final Div imgContainer = new Div();

		//if render for second time-delete old img
		if (this.getChildren().size() > 1)
		{
			this.getLastChild().detach();
		}
		final Image img = new Image(pathToImage);

		removeButton.addEventListener(Events.ON_CLICK, new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception //NOPMD: ZK specific
			{
				ImageTile.this.setReady(false);
				imgContainer.detach();
				ImageTile.this.button.setVisible(true);
				if (removeClickButtonListener != null)
				{
					removeClickButtonListener.onEvent(event);
				}
			}
		});

		imgContainer.setSclass(GALLERY_SCLASS);

		img.setParent(imgContainer);
		removeButton.setParent(imgContainer);
		imgContainer.setParent(this);
		attachHoverAction();
		this.setReady(true);
	}

	private void attachHoverAction()
	{
		this.setAction("onmouseover:var actionDiv=this.getElementsByClassName('actionContainer')[0];"
				+ "actionDiv.style.visibility='visible';"
				+ "onmouseout:var actionDiv=this.getElementsByClassName('actionContainer')[0];"
				+ "actionDiv.style.visibility='hidden';");
	}

	public void reset()
	{
		this.setReady(true);
		this.button.setVisible(true);
	}
}

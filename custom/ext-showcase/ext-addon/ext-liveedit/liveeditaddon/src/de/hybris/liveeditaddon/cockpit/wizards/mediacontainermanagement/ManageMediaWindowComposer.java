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
package de.hybris.liveeditaddon.cockpit.wizards.mediacontainermanagement;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.liveeditaddon.cockpit.wizards.components.ImageTile;
import de.hybris.liveeditaddon.cockpit.wizards.components.ImgContainer;
import de.hybris.liveeditaddon.cockpit.wizards.mediacontainermanagement.service.impl.AbstractManageMediaService;
import de.hybris.liveeditaddon.cockpit.wizards.productimagemanagement.events.CockpiResizeableImageDropEventListener;
import de.hybris.liveeditaddon.cockpit.wizards.productimagemanagement.events.CockpitFormatImageDropEventListener;
import de.hybris.liveeditaddon.cockpit.wizards.productimagemanagement.events.ProductImageModelListener;

import java.util.Iterator;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Window;


/**
 */
public class ManageMediaWindowComposer extends GenericForwardComposer
{
	private final String AUTO_RESIZE_FORMAT = "AutoResizeDrop";
	private final String AUTO_RESIZE_LABEL = "autoResizeDrop";

	private Window window;
	private Component componentWindow;
	private Div mediaFormatContainer;
	private Div autoResizeContainer;
	private Div primaryImage;
	private Component btnDone;

	private AbstractManageMediaService manageMediaService;

	@Override
	public void doAfterCompose(final Component comp) throws Exception
	{
		super.doAfterCompose(comp);

		componentWindow = comp;

		initServices();
		if (manageMediaService.isResizePluginIncluded())
		{
			composeAutoResize();
		}
		refreshMainPicture();
		refresh();
	}

	private void initServices()
	{
		final Map<String, Object> args = componentWindow.getDesktop().getExecution().getArg();
		manageMediaService = (AbstractManageMediaService) args.get(ManageMediaContainerWizard.ARG_MANAGE_MEDIA_SERVICE);
		manageMediaService.addListener(getListener());
	}

	/**
	 * @return
	 */
	private ProductImageModelListener getListener()
	{
		return new ProductImageModelListener()
		{

			@Override
			public void onLoad()
			{
				//
			}

			@Override
			public void onChange()
			{
			}
		};
	}

	private void composeAutoResize()
	{
		autoResizeContainer.setVisible(true);
		final ImageTile autoResizeTile = new ImageTile(AUTO_RESIZE_LABEL, AUTO_RESIZE_FORMAT);
		autoResizeTile.addEventListener(Events.ON_CLICK, new CockpiResizeableImageDropEventListener(manageMediaService)
		{
			@Override
			protected void refresh(final Event event)
			{
				ManageMediaWindowComposer.this.refresh();
			}
		});
		autoResizeTile.setParent(autoResizeContainer);
	}

	private void composeMediaFormatContainer()
	{
		mediaFormatContainer.getChildren().clear();

		final Map<String, String> mediaFormatsMap = manageMediaService.getMediaFormatMap();
		if (!mediaFormatsMap.isEmpty())
		{
			final Iterator it = mediaFormatsMap.entrySet().iterator();
			while (it.hasNext())
			{
				final Map.Entry<String, String> entry = (Map.Entry) it.next();
				final ImageTile imageTile = new ImageTile(entry.getKey(), entry.getKey());
				imageTile.addEventListener(Events.ON_CLICK, new CockpitFormatImageDropEventListener(manageMediaService)
				{
					@Override
					protected void refresh(final Event event)
					{
						ManageMediaWindowComposer.this.refresh();
					}
				});

				final String mediaPath = manageMediaService.getMediaPathForFormat(imageTile.getFormat());
				if (!mediaPath.isEmpty())
				{
					imageTile.renderPreviewImage(mediaPath);
					imageTile.addRemoveEventListener(Events.ON_CLICK, new EventListener()
					{
						@Override
						public void onEvent(final Event event) throws Exception
						{
							manageMediaService.removeMediaByFormat(imageTile.getFormat());
							ManageMediaWindowComposer.this.refresh();
						}
					});
				}

				imageTile.setParent(mediaFormatContainer);
			}
		}
	}

	private void composeMainPicture(final String url)
	{
		final ImgContainer mainPicture = new ImgContainer(url, false);
		mainPicture.setParent(primaryImage);
		mainPicture.addEventListener(Events.ON_DROP, new EventListener()
		{

			@Override
			public void onEvent(final Event event) throws Exception
			{
				if (event instanceof DropEvent)
				{
					refresh();
				}
			}
		});
	}

	private void refreshMainPicture()
	{
		primaryImage.getChildren().clear();
		final MediaContainerModel mediaContainerModel = manageMediaService.getMediaContainerModel();
		if (mediaContainerModel != null)
		{
			final MediaModel picture = manageMediaService.getMasterMedia(mediaContainerModel.getMedias());
			String imgPath = "";
			if (picture != null)
			{
				imgPath = manageMediaService.getServerPath() + picture.getURL();
			}
			composeMainPicture(imgPath);
		}
	}

	public void refresh()
	{
		composeMediaFormatContainer();
		btnDone.setVisible(isReadyForSave());
	}

	public void onClick$btnDone(final ForwardEvent event)
	{
		manageMediaService.save();
		Events.postEvent(new Event("onClose", componentWindow, null));
	}

	public void onClick$btnCancel(final ForwardEvent event)
	{
		Events.postEvent(new Event("onClose", componentWindow, null));
	}

	private boolean isReadyForSave()
	{
		return manageMediaService.isReadyForSave();
	}

}

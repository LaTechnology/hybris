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
package de.hybris.liveeditaddon.cockpit.wizards.productimagemanagement.events;

import de.hybris.liveeditaddon.cockpit.media.MediaHelper;
import de.hybris.liveeditaddon.cockpit.service.CockpitImageMediaService;
import de.hybris.liveeditaddon.cockpit.wizards.components.ImageTile;

import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Messagebox;


/**
 */
public class CockpiResizeableImageDropEventListener implements EventListener
{

	private static final Logger LOG = Logger.getLogger(CockpiResizeableImageDropEventListener.class);

	private final CockpitImageMediaService model;

	private final String LABEL_FILEUPLOAD_TIPS = "uploadmediawizard.fileupload.tips";
	private final String LABEL_WINDOW_MESSAGE = "dialog.fileupload.window.error.message";
	private final String LABEL_WINDOW_TITLE = "dialog.fileupload.window.error.title";

	public CockpiResizeableImageDropEventListener(final CockpitImageMediaService model)
	{
		super();
		this.model = model;
	}

	@Override
	public void onEvent(final Event event) throws Exception
	{
		try
		{
			final ImageTile imageTile = (ImageTile) event.getTarget().getParent();
			final org.zkoss.util.media.Media uploadedMedia = Fileupload.get(Labels.getLabel(LABEL_FILEUPLOAD_TIPS), null);
			// check if file is ZIP
			if (uploadedMedia != null && uploadedMedia.getFormat().equals("zip"))
			{
				final List<byte[]> extractedBytes = MediaHelper.extractBytesFromZipInputStream(LOG, uploadedMedia);
				model.setImagesFromZip(extractedBytes, imageTile);
			}
			// Add check if the uploaded file is actually an image.
			else if (uploadedMedia != null && uploadedMedia instanceof org.zkoss.image.Image)
			{
				final byte[] bytes = MediaHelper.extractMediaBytesFromMedia(LOG, uploadedMedia);
				if (bytes != null)
				{
					model.setImageWithAutoResize(bytes, imageTile);
					refresh(event);
				}
			}
		}
		catch (final InterruptedException e)
		{
			LOG.error(LABEL_WINDOW_MESSAGE, e);
			Messagebox.show(Labels.getLabel(LABEL_WINDOW_MESSAGE), Labels.getLabel(LABEL_WINDOW_TITLE), Messagebox.OK,
					Messagebox.EXCLAMATION);
		}
	}

	protected void refresh(final Event event)
	{
		//
	}
}

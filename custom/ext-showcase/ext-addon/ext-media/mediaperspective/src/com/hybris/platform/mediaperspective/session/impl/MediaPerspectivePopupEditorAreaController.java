package com.hybris.platform.mediaperspective.session.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.PopupEditorAreaController;
import de.hybris.platform.cockpit.session.impl.DefaultEditorAreaController;

public class MediaPerspectivePopupEditorAreaController extends DefaultEditorAreaController implements PopupEditorAreaController
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(MediaPerspectivePopupEditorAreaController.class);

	private BrowserModel contextEditorBrowser = null;
	private final Map<String, Object> attributes = new HashMap<String, Object>();

	public Map<String, Object> getAttributesMap()
	{
		return attributes;
	}

	@Override
	public void setContextEditorBrowser(final BrowserModel model)
	{
		this.contextEditorBrowser = model;
	}

	public BrowserModel getContextEditorBrowserModel()
	{
		return contextEditorBrowser;
	}
}




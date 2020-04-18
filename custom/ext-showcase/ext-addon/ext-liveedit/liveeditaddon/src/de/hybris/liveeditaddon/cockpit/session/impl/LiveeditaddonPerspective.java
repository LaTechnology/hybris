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
package de.hybris.liveeditaddon.cockpit.session.impl;

import de.hybris.platform.cmscockpit.session.impl.LiveEditPerspective;
import de.hybris.platform.cockpit.model.meta.TypedObject;

import java.util.Collections;
import java.util.Map;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Window;


/**
 * 
 */
public class LiveeditaddonPerspective extends LiveEditPerspective
{

	private LiveEditCallbackEditorArea liveEditPopupEditorArea = null;

	public void setLiveEditPopupEditorArea(final LiveEditCallbackEditorArea popupEditorArea)
	{
		if ((this.liveEditPopupEditorArea == null && popupEditorArea != null)
				|| (this.liveEditPopupEditorArea != null && !this.liveEditPopupEditorArea.equals(popupEditorArea)))
		{
			if (this.getLiveEditPopupEditorArea() != null)
			{
				this.getLiveEditPopupEditorArea().setPerspective(null);
				//this.getPopupEditorArea().removeEditorAreaListener(this.getEditorAreaListener());
			}
			this.liveEditPopupEditorArea = popupEditorArea;
			if (this.getLiveEditPopupEditorArea() != null)
			{
				this.getLiveEditPopupEditorArea().setPerspective(this);
				//this.getPopupEditorArea().addEditorAreaListener(this.getEditorAreaListener());
				this.getLiveEditPopupEditorArea().getEditorAreaController().setModel(this.getLiveEditPopupEditorArea());
				addCockpitEventAcceptor(getLiveEditPopupEditorArea());
			}
		}
	}

	public void activateItemInLiveEditPopup(final TypedObject activeItem, final Window window,
			final OnEventCallback onEventCallback, final String editorAreaConfigCode)
	{
		getLiveEditPopupEditorArea().initialize(Collections.<String, Object> singletonMap("item", activeItem));
		getLiveEditPopupEditorArea().getEditorAreaController().initialize();

		getLiveEditPopupEditorArea().reset();
		getLiveEditPopupEditorArea().setCurrentObject(activeItem);
		getLiveEditPopupEditorArea().setOverrideEditorAreaConfigCode(editorAreaConfigCode);
		getLiveEditPopupEditorArea().getEditorAreaController().resetSectionPanelModel();
		window.setTitle(Labels.getLabel("perspective.popupeditor.edit", new String[]
		{ activeItem.getType().getCode() }));
		window.doHighlighted();
		window.setClosable(true);
		window.setVisible(true);
		window.addEventListener(Events.ON_CLOSE, new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				window.setVisible(false);
				event.stopPropagation();
				if (onEventCallback != null)
				{
					onEventCallback.onEvent(event);
				}
			}
		});
		Executions.createComponents(getLiveEditPopupEditorArea().getViewURI(), window, null);
	}

	public static interface OnEventCallback
	{
		void onEvent(final Event event);
	}

	@Override
	public void initialize(final Map<String, Object> params)
	{
		super.initialize(params);
	}

	public LiveEditCallbackEditorArea getLiveEditPopupEditorArea()
	{
		return this.liveEditPopupEditorArea;
	}
}

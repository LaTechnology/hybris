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
package de.hybris.liveeditaddon.cockpit.navigationeditor.wizard;

import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.cmscockpit.components.liveedit.impl.DefaultLiveEditViewModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.util.StopWatch;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Window;

import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationEditorViewModel;
import de.hybris.liveeditaddon.cockpit.services.NavigationEditorService;


/**
 * 
 */
public class NavigationEditorWizard
{
	private final String componentURI = "/cmscockpit/navigationEditor.zul";
	public static final String WIZARD_ARG = "wizardModel";
	public static final String SERVICE_ARG = "wizardService";

	private final NavigationEditorViewModel navigationEditorViewModel;
	private final NavigationEditorService navigationEditorService;

	Window wizardWindow;

	public NavigationEditorWizard(final DefaultLiveEditViewModel model, final String serverPath, final String componentUID,
			final String contentSlotUID, final NavigationEditorService navigationEditorService)
	{
		navigationEditorViewModel = new NavigationEditorViewModel();
		this.navigationEditorService = navigationEditorService;

		final StopWatch timer = new StopWatch("Initializing Navigation Editor Wizard");
		try
		{
			navigationEditorViewModel.initModel(model, serverPath, componentUID, contentSlotUID);
		}
		finally
		{
			timer.stop();
		}
	}

	public void show(final LiveEditView liveEditView)
	{
		wizardWindow = createFrameComponent(liveEditView);

		wizardWindow.setPosition("center");
		wizardWindow.doHighlighted();

		wizardWindow.addEventListener(Events.ON_CLOSE, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea().update();
				liveEditView.getModel().clearPreviewInformation();
				liveEditView.update();
			}
		});
	}

	protected Window createFrameComponent(final LiveEditView liveEditView)
	{
		final Map<String, Object> args = new HashMap<String, Object>();
		args.put(WIZARD_ARG, navigationEditorViewModel);
		args.put(SERVICE_ARG, navigationEditorService);
		final Window ret = (Window) Executions.createComponents(getComponentURI(), null, args);

		ret.applyProperties();
		new AnnotateDataBinder(ret).loadAll();
		ret.setParent(liveEditView.getViewComponent());
		return ret;
	}

	protected String getComponentURI()
	{
		return this.componentURI;
	}
}

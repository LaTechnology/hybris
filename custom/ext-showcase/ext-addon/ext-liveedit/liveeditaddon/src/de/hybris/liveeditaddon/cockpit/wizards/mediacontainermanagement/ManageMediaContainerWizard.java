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

import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.liveeditaddon.cockpit.wizards.mediacontainermanagement.service.ManageMediaService;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Window;


/**
 */
public class ManageMediaContainerWizard
{

	public static final String ARG_MANAGE_MEDIA_SERVICE = "manageMediaService";

	private final static String VIEW_PATH = "/cmscockpit/manageMedia.zul";

	private ManageMediaService manageMediaService;

	public ManageMediaContainerWizard(final ManageMediaService manageMediaService)
	{
		this.setManageMediaService(manageMediaService);
	}

	public void show(final LiveEditView liveEditView)
	{
		final Window wizardWindow = createView(liveEditView);

		wizardWindow.setPosition("center");
		wizardWindow.doHighlighted();
	}

	protected Window createView(final LiveEditView liveEditView)
	{
		final Map<String, Object> args = new HashMap<String, Object>();
		args.put(ARG_MANAGE_MEDIA_SERVICE, manageMediaService);

		final Window ret = (Window) Executions.createComponents(VIEW_PATH, null, args);

		ret.applyProperties();
		new AnnotateDataBinder(ret).loadAll();
		ret.setParent(liveEditView.getViewComponent());

		ret.addEventListener(Events.ON_CLOSE, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				ret.setParent(null);
				liveEditView.update();
			}
		});
		return ret;
	}

	/**
	 * @return the manageMediaService
	 */
	public ManageMediaService getManageMediaService()
	{
		return manageMediaService;
	}

	/**
	 * @param manageMediaService
	 *           the manageMediaService to set
	 */
	public void setManageMediaService(final ManageMediaService manageMediaService)
	{
		this.manageMediaService = manageMediaService;
	}

}

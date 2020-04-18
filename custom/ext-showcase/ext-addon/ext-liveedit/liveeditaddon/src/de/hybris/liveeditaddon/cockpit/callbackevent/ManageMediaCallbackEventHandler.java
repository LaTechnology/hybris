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
package de.hybris.liveeditaddon.cockpit.callbackevent;

import de.hybris.liveeditaddon.cockpit.service.MediaFormatService;
import de.hybris.liveeditaddon.cockpit.wizards.mediacontainermanagement.ManageMediaContainerWizard;
import de.hybris.liveeditaddon.cockpit.wizards.mediacontainermanagement.service.impl.AbstractManageMediaService;
import de.hybris.liveeditaddon.cockpit.wizards.mediacontainermanagement.service.impl.DefaultManageMediaService;

import de.hybris.platform.acceleratorcms.model.components.AbstractMediaContainerComponentModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.core.model.media.MediaContainerModel;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Messagebox;

import javax.annotation.Resource;
import java.util.Map;

/**
 */
public class ManageMediaCallbackEventHandler extends AbstractLiveEditCallbackEventHandler<LiveEditView>
{

	@Resource(name = "mediaFormatService")
	private MediaFormatService mediaFormatService;

	@Override
	public String getEventId()
	{
		return "manageMedia";
	}

    @Override
    public void onCallbackEvent(LiveEditView view, Map<String, Object> attributeMap) throws Exception
	{
		final String componentId = (String)attributeMap.get("item_uid");
		final String serverPath = (String)attributeMap.get("serverPath");
		try
		{
			final AbstractCMSComponentModel component = getComponentForUid(componentId, view);
			if (component instanceof AbstractMediaContainerComponentModel)
			{
				final AbstractMediaContainerComponentModel abstractMediaContainerComponent = (AbstractMediaContainerComponentModel) component;
				final CatalogVersionModel catalogVersion = view.getModel().getCurrentPreviewData().getActiveCatalogVersion();
				final String siteUid = view.getModel().getSite().getUid();

				final MediaContainerModel mediaContainerModel = abstractMediaContainerComponent.getMedia();

				final Map<String, String> mediaFormatsForCurrentSite = mediaFormatService.getMediaFormatsForCurrentSite(siteUid);
				final AbstractManageMediaService manageMediaService = new DefaultManageMediaService(serverPath, siteUid,
						catalogVersion, mediaContainerModel, abstractMediaContainerComponent, mediaFormatsForCurrentSite);

				final ManageMediaContainerWizard wizard = new ManageMediaContainerWizard(manageMediaService);
				wizard.show(view);
			}
			else
			{
				throw new IllegalArgumentException(
						"Current component is not type of AbstractMediaContainerComponentModel. Please provide correct component");
			}
		}
		catch (final IllegalArgumentException e)
		{
			Messagebox.show(Labels.getLabel("dialog.managemedia.nocomponentfound.error.message"),
					Labels.getLabel("dialog.managemedia.nocomponentfound.error.title"), Messagebox.CANCEL, Messagebox.ERROR);
		}
		finally
		{
			view.update();
		}
	}
}

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

import de.hybris.liveeditaddon.cockpit.wizards.LiveEditMediaComponentWizard;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.cmscockpit.session.impl.CmsPageBrowserModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import org.springframework.beans.factory.annotation.Required;

import javax.annotation.Resource;
import java.util.Map;


/**
 * 
 */
public class DropMediaCallbackEventHandler extends AbstractLockingLiveEditCallbackEventHandler<LiveEditView>
{

	@Resource(name = "mediaService")
	private MediaService mediaService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.cmscockpit.components.liveedit.CallbackEventHandler#getEventId()
	 */
	@Override
	public String getEventId()
	{
		return "createMediaComponent";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.cmscockpit.components.liveedit.CallbackEventHandler#onCallbackEvent(de.hybris.platform.cmscockpit.
	 * components.liveedit.LiveEditView, java.lang.String[])
	 */
    @Override
    public void onCallbackEventInternal(LiveEditView view, Map<String, Object> attributeMap) throws Exception
	{
		final String pageUid = (String)attributeMap.get("page_id");
		//final String contentSlotId = passedAttributes[2];
		final String slotPosition = (String)attributeMap.get("position");

		final String mediaFileName = (String)attributeMap.get("mediaFileName");

		// get the Model page
		final AbstractPageModel page = getPageForPreviewCatalogVersions(view, pageUid);
		// convert to a typed object
        final CmsPageBrowserModel pageModel = getApplicationContext().getBean("cmsPageBrowserModel", CmsPageBrowserModel.class);
		final TypedObject pageObject = getCockpitTypeService().wrapItem(page);
		pageModel.setCurrentPageObject(pageObject);
		pageModel.initialize();

		// find the section
		final BrowserSectionModel selectedModel = getBrowserSectionForPosition(slotPosition, pageModel);
		// required for CmsComponentController.done()

		final LiveEditMediaComponentWizard cmsCreateComponentWizard = new LiveEditMediaComponentWizard(selectedModel,
				view.getViewComponent(), pageModel, view);

		cmsCreateComponentWizard.setPosition(slotPosition);
		cmsCreateComponentWizard.setMediaModel(getMediaForPreviewCatalogVersions(view, mediaFileName));
		cmsCreateComponentWizard.start();
	}

	protected AbstractPageModel getPageForPreviewCatalogVersions(final LiveEditView view, final String pageUid)
	{
		return ((AbstractPageModel) getSessionService().executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public Object execute()
			{
				getCatalogVersionService().setSessionCatalogVersions(view.getModel().getCurrentPreviewData().getCatalogVersions());
				AbstractPageModel page;
				try
				{
					page = getCmsPageService().getPageForId(pageUid);
				}
				catch (final CMSItemNotFoundException e)
				{
					return null;
				}
				return page;
			}
		}));
	}

	protected MediaModel getMediaForPreviewCatalogVersions(final LiveEditView view, final String mediaFileName)
	{
		return ((MediaModel) getSessionService().executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public Object execute()
			{
				getCatalogVersionService().setSessionCatalogVersions(view.getModel().getCurrentPreviewData().getCatalogVersions());
				MediaModel media;
				try
				{
					media = getMediaService().getMedia(mediaFileName);
				}
				catch (final UnknownIdentifierException e)
				{
					return null;
				}
				return media;
			}
		}));
	}

	public MediaService getMediaService()
	{
		return mediaService;
	}

	@Required
	public void setMediaService(final MediaService mediaService)
	{
		this.mediaService = mediaService;
	}
}

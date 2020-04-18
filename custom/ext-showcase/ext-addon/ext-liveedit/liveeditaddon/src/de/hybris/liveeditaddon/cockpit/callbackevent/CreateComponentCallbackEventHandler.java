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

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.cmscockpit.session.impl.CmsPageBrowserModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.liveeditaddon.cockpit.wizards.LiveEditCmsComponentWizard;

import java.util.Map;


/**
 * 
 */
public class CreateComponentCallbackEventHandler extends AbstractLockingLiveEditCallbackEventHandler<LiveEditView>
{

	@Override
	public String getEventId()
	{
		return "createComponent";
	}

	@Override
    public void onCallbackEventInternal(LiveEditView view, Map<String, Object> attributeMap) throws Exception
	{
		final String pageUid = (String) attributeMap.get("page_id");
		final String slotPosition = (String) attributeMap.get("position");

		final AbstractPageModel page = getPageForPreviewCatalogVersions(pageUid, view);
		// convert to a typed object
        final CmsPageBrowserModel pageModel = getApplicationContext().getBean("cmsPageBrowserModel", CmsPageBrowserModel.class);
		final TypedObject pageObject = getCockpitTypeService().wrapItem(page);
		pageModel.setCurrentPageObject(pageObject);
		pageModel.initialize();

		// find the section
		final BrowserSectionModel selectedModel = getBrowserSectionForPosition(slotPosition, pageModel);
		// required for CmsComponentController.done()
		final LiveEditCmsComponentWizard cmsCreateComponentWizard = new LiveEditCmsComponentWizard(selectedModel,
				view.getViewComponent(), pageModel, view);
		cmsCreateComponentWizard.setPosition(slotPosition);
		cmsCreateComponentWizard.start();
	}
}

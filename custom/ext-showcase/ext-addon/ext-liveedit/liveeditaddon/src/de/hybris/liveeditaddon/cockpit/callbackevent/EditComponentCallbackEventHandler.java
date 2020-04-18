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

import de.hybris.platform.cmscockpit.components.liveedit.impl.LiveEditPopupEditDialog;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;

import java.util.Map;


/**
 * 
 */
public class EditComponentCallbackEventHandler extends AbstractLockingLiveEditCallbackEventHandler<LiveEditView>
{

	@Override
	public String getEventId()
	{
		return "callback";
	}

    @Override
    public void onCallbackEventInternal(LiveEditView view, Map<String, Object> attributesMap) throws Exception
	{
        final LiveEditPopupEditDialog popupEditorDialog = new LiveEditPopupEditDialog(attributesMap, view.getModel().getCurrentPreviewData().getCatalogVersions(), view);
        view.getViewComponent().appendChild(popupEditorDialog);
    }

}

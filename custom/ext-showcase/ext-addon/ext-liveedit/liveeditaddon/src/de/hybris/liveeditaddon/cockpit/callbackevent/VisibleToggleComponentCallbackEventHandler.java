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

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;

import java.util.Map;


/**
 * 
 */
public class VisibleToggleComponentCallbackEventHandler extends AbstractLockingLiveEditCallbackEventHandler<LiveEditView>
{

	@Override
	public String getEventId()
	{
		return "toggleVisibility";
	}

    @Override
    public void onCallbackEventInternal(final LiveEditView view, Map<String, Object> attributeMap) throws Exception
	{
		final String componentUid = (String)attributeMap.get("cmp_id");
		final AbstractCMSComponentModel component = getComponentForUid(componentUid, view);
		component.setVisible(Boolean.valueOf(!component.getVisible().booleanValue()));
		getModelService().save(component);
		view.update();
	}

}

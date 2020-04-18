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

import de.hybris.liveeditaddon.cockpit.service.CMSLockingService;
import de.hybris.liveeditaddon.constants.LiveeditaddonConstants;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Map;


/**
 * 
 */
public abstract class AbstractLockingLiveEditCallbackEventHandler<V extends LiveEditView> extends
		AbstractLiveEditCallbackEventHandler<V>
{

    @Autowired
    protected CMSLockingService cmsLockingService;

	@Override
    public void onCallbackEvent(final V view, final Map<String, Object> attributeMap) throws Exception
	{
		final String slotId = (String) attributeMap.get(LiveeditaddonConstants.SLOT_ID);

		if (!cmsLockingService.isSectionLocked(view, slotId))
		{
			onCallbackEventInternal(view, attributeMap);
		}
	}

    public abstract void onCallbackEventInternal(final V view, final Map<String, Object> attributeMap) throws Exception;

}

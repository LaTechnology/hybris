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

import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.liveeditaddon.cockpit.session.impl.LiveeditaddonBrowserArea;
import de.hybris.liveeditaddon.constants.LiveeditaddonConstants;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.cmscockpit.session.impl.LiveEditBrowserArea;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;

import java.util.Map;


/**
 * 
 */
public class InspectComponentCallbackEventHandler extends AbstractLiveEditCallbackEventHandler<LiveEditView>
{

	private final String INSPECTOR_CONTAINER_ID = "inspectorContainer";

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.cmscockpit.components.liveedit.CallbackEventHandler#getEventId()
	 */
	@Override
	public String getEventId()
	{
		return "inspectComponent";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.cmscockpit.components.liveedit.CallbackEventHandler#onCallbackEvent(de.hybris.platform.cmscockpit
	 * .components.liveedit.LiveEditView, java.lang.String[])
	 */
    @Override
    public void onCallbackEvent(LiveEditView view, Map<String, Object> attributeMap) throws Exception
	{
		final String componentId = (String)attributeMap.get("cmp_id");
        final String slot_id = (String)attributeMap.get(LiveeditaddonConstants.SLOT_ID);
		final AbstractCMSComponentModel component = getComponentForUid(componentId, view);

		final UICockpitPerspective currentPerspective = UISessionUtils.getCurrentSession().getCurrentPerspective();
		final UIBrowserArea currentBrowserArea = currentPerspective.getBrowserArea();
		final LiveeditaddonBrowserArea browser = (LiveeditaddonBrowserArea) currentBrowserArea;
		browser.openInspectorInDiv(getCockpitTypeService().wrapItem(component), INSPECTOR_CONTAINER_ID, view, slot_id);
	}

}

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
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.liveeditaddon.cockpit.session.impl.LiveeditaddonPerspective;
import de.hybris.liveeditaddon.cockpit.session.impl.LiveeditaddonPerspective.OnEventCallback;

import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;

import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;

import java.util.Map;


/**
 * 
 */
public class EditCMSItemCallbackEventHandler extends AbstractLockingLiveEditCallbackEventHandler<LiveEditView>
{

	private String eventId;
	private String editorAreaConfigCode;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.cmscockpit.components.liveedit.CallbackEventHandler#getEventId()
	 */
	@Override
	public String getEventId()
	{
		return this.eventId;
	}

	@Required
	public void setEventId(final String eventId)
	{
		this.eventId = eventId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.cmscockpit.components.liveedit.CallbackEventHandler#onCallbackEvent(de.hybris.platform.cmscockpit
	 * .components.liveedit.LiveEditView, java.lang.String[])
	 */
    //@Override
    public void onCallbackEventInternal(final LiveEditView view, Map<String, Object> attributeMap) throws Exception

	{
		final PK pk = PK.parse(String.valueOf(attributeMap.get("component_pk")));

		final ItemModel item = getModelService().get(pk);
		final TypedObject wrappedItem = getCockpitTypeService().wrapItem(item);

		((LiveeditaddonPerspective) UISessionUtils.getCurrentSession().getCurrentPerspective()).activateItemInLiveEditPopup(
				wrappedItem, createPopupWindow(view.getViewComponent()), new OnEventCallback()
				{

					@Override
					public void onEvent(final Event event)
					{
						if (event.getName().equals(Events.ON_CLOSE))
						{
							view.update();
						}
					}
				}, getEditorAreaConfigCode());
	}

	public String getEditorAreaConfigCode()
	{
		return editorAreaConfigCode;
	}

	public void setEditorAreaConfigCode(final String editorAreaConfigCode)
	{
		this.editorAreaConfigCode = editorAreaConfigCode;
	}


}

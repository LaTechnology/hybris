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
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Messagebox;


/**
 * 
 */
public class RemoveComponentCallbackEventHandler extends AbstractLockingLiveEditCallbackEventHandler<LiveEditView>
{
	@Override
	public String getEventId()
	{
		return "removeComponent";
	}

    @Override
    public void onCallbackEventInternal(final LiveEditView view, Map<String, Object> attributeMap) throws Exception
	{
		final String componentId = (String)attributeMap.get("cmp_id");
		final String contentSlotId = (String)attributeMap.get("slot_id");
		removeComponent(view, contentSlotId, componentId);
	}

	/**
	 * Remove the component from the content slot
	 * 
	 * @param contentSlotId
	 * @param componentId
	 */
	protected void removeComponent(final LiveEditView view, final String contentSlotId, final String componentId)
	{
		final AbstractCMSComponentModel component = getComponentForUid(componentId, view);
		final ContentSlotModel contentSlot = getContentSlotForPreviewCatalogVersions(contentSlotId, view);
		final TypedObject typedObject = getCockpitTypeService().wrapItem(component);

		final String currentMessage = Labels.getLabel("prompt.confirm_remove_content_element", new String[]
		{ UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel(typedObject) });
		try
		{
			if (Messagebox.show(currentMessage, Labels.getLabel("dialog.confirmRemove.title"), Messagebox.YES | Messagebox.NO,
					Messagebox.QUESTION) == Messagebox.YES)
			{

				if (contentSlot != null)
				{
					final List<AbstractCMSComponentModel> newOrder = new ArrayList(contentSlot.getCmsComponents());
					// remove our component from the list
					newOrder.remove(component);
					contentSlot.setCmsComponents(newOrder);
					getModelService().save(contentSlot);
				}

				view.update();
			}
		}
		catch (final ModelSavingException e)
		{
			throw new IllegalStateException("Could not save content slot [" + contentSlotId + "] after remove ", e);
		}
		catch (final InterruptedException e)
		{
			throw new IllegalStateException("Could not save content slot [" + contentSlotId + "] after remove ", e);
		}
	}


}

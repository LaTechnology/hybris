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
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.model.contents.containers.AbstractCMSComponentContainerModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Messagebox;

import java.util.Map;


/**
 */
public class RemoveContainerCallbackEventHandler extends RemoveComponentCallbackEventHandler
{
	private final static Logger LOG = Logger.getLogger(RemoveContainerCallbackEventHandler.class.getName());

	@Override
	public String getEventId()
	{
		return "removeContainer";
	}

    @Override
    public void onCallbackEventInternal(final LiveEditView view, Map<String, Object> attributeMap) throws Exception
	{
		final String componentId = (String)attributeMap.get("cmp_id");
		final String contentSlotId = (String)attributeMap.get("slot_id");
		try
		{
			if (componentId == null || contentSlotId == null)
			{
				throw new IllegalArgumentException("Please provide component id and slot id. [action: REMOVE_CONTAINER]");
			}

			final AbstractCMSComponentModel component = getComponentForUid(componentId, view);

			if (!(component instanceof SimpleCMSComponentModel))
			{
				throw new IllegalArgumentException("Please provide correct component type. [action: REMOVE_CONTAINER]");
			}

			final SimpleCMSComponentModel sc = (SimpleCMSComponentModel) component;

			if (!sc.getContainers().isEmpty())
			{
				for (final AbstractCMSComponentContainerModel container : sc.getContainers())
				{
					for (final ContentSlotModel slot : container.getSlots())
					{
						if (slot.getUid().equals(contentSlotId))
						{
							removeComponent(view, contentSlotId, container.getUid());
							break;
						}
					}
				}
			}
		}
		catch (final IllegalArgumentException e)
		{
			LOG.error(e);
			Messagebox.show(Labels.getLabel("dialog.removecontainer.error.message"),
					Labels.getLabel("dialog.removecontainer.error.title"), Messagebox.CANCEL, Messagebox.ERROR);
		}
		finally
		{
			view.update();
		}

	}

}

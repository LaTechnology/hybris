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

import java.util.Collection;
import java.util.Collections;
import java.util.Map;


/**
 * 
 */
public class ShowAllComponentsCallbackEventHandler<V extends LiveEditView> extends AbstractLiveEditCallbackEventHandler<V>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.cmscockpit.components.liveedit.CallbackEventHandler#getEventId()
	 */
	@Override
	public String getEventId()
	{
		return "showAllComponents";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.cmscockpit.components.liveedit.CallbackEventHandler#onCallbackEvent(de.hybris.platform.cmscockpit
	 * .components.liveedit.LiveEditView, java.lang.String[])
	 */
    @Override
    public void onCallbackEvent(final V view, Map<String, Object> attributeMap) throws Exception
	{
		final String contentSlotId = (String)attributeMap.get("slot_id");
		final ContentSlotModel contentSlot = getContentSlotForPreviewCatalogVersions(contentSlotId, view);
		if (contentSlot != null)
		{
			final TypedObject wrappedContentSlot = getCockpitTypeService().wrapItem(contentSlot);
			final Collection<TypedObject> wrappedComponents = getCockpitTypeService().wrapItems(contentSlot.getCmsComponents());
			UISessionUtils
					.getCurrentSession()
					.getCurrentPerspective()
					.openReferenceCollectionInBrowserContext(wrappedComponents,
							getCockpitTypeService().getObjectTemplate(AbstractCMSComponentModel._TYPECODE), wrappedContentSlot,
							Collections.<String, Object> emptyMap());
		}

	}
}

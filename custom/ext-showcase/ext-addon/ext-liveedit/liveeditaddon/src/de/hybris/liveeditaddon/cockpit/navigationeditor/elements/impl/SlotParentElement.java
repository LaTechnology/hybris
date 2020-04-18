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
package de.hybris.liveeditaddon.cockpit.navigationeditor.elements.impl;

import de.hybris.liveeditaddon.cockpit.navigationeditor.elements.NavigationParentElement;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;

import java.util.List;


/**
 */
public class SlotParentElement implements NavigationParentElement
{
	private ContentSlotModel element;

	public SlotParentElement(final ContentSlotModel slot)
	{
		element = slot;
	}

	@Override
	public CMSItemModel getElement()
	{
		return element;
	}

	@Override
	public List<AbstractCMSComponentModel> getChildren()
	{
		return element.getCmsComponents();
	}

	@Override
	public ContentSlotModel getSlot()
	{
		return element;
	}
}

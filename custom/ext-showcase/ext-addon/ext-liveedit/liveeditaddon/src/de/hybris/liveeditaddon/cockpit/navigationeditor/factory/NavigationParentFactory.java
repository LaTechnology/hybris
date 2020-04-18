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
package de.hybris.liveeditaddon.cockpit.navigationeditor.factory;

import de.hybris.liveeditaddon.cockpit.navigationeditor.elements.NavigationParentElement;
import de.hybris.liveeditaddon.cockpit.navigationeditor.elements.impl.ComponentParentElement;
import de.hybris.liveeditaddon.cockpit.navigationeditor.elements.impl.SlotParentElement;
import de.hybris.liveeditaddon.cockpit.services.NavigationEditorService;
import de.hybris.platform.acceleratorcms.model.components.NavigationBarCollectionComponentModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditViewModel;


/**
 */
public class NavigationParentFactory
{

	public static NavigationParentElement build(final NavigationEditorService service, final String componentUid,
			final String slotUid, final LiveEditViewModel viewModel)
	{
		final ContentSlotModel contentSlot = service.getNavigationBarContentSlot(slotUid, viewModel);
		final AbstractCMSComponentModel componentModel = service.getComponentForPreviewCatalogVersions(componentUid, viewModel);

		if (contentSlot.getCmsComponents().contains(componentModel))//component can be a direct NavigationBarComponent or direct NavigationBarCollectionComponentModel
		{
            if (componentModel instanceof NavigationBarCollectionComponentModel) {
                return new ComponentParentElement((NavigationBarCollectionComponentModel)componentModel, contentSlot);
            }else{
                return new SlotParentElement(contentSlot);
            }
		}
		else // or can be an indirect NavigationBarComponent
		{
			// New NavigationBarCollectionComponent concept
			for (final AbstractCMSComponentModel component : contentSlot.getCmsComponents())
			{
				if (component instanceof NavigationBarCollectionComponentModel)
				{
					final NavigationBarCollectionComponentModel collectionModel = (NavigationBarCollectionComponentModel) component;
					if (collectionModel.getComponents().contains(componentModel))
					{
						return new ComponentParentElement(collectionModel, contentSlot);
					}
				}
			}
		}
		return null;
	}

}

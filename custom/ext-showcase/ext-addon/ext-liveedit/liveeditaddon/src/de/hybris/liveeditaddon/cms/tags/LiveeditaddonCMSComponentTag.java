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
package de.hybris.liveeditaddon.cms.tags;

import de.hybris.platform.acceleratorcms.component.slot.CMSPageSlotComponentService;
import de.hybris.platform.acceleratorcms.tags2.CMSComponentTag;
import de.hybris.platform.acceleratorservices.util.SpringHelper;
import de.hybris.platform.addonsupport.config.cms.dataattributeproviders.TagDataAttributesProvider;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 */
public class LiveeditaddonCMSComponentTag extends CMSComponentTag
{
	@Override
	protected Map<String, String> getElementAttributes()
	{
		final AbstractCMSComponentModel container = resolveComponent();

		final List<TagDataAttributesProvider> tagDataAttributesProviders = getTagDataAttributesProviders();
		Map<String, String> mergedAttributeMap = new HashMap<String, String>();

		for (final TagDataAttributesProvider dataAttributesProvider : tagDataAttributesProviders)
		{
			mergedAttributeMap = htmlElementHelper.mergeAttributeMaps(mergedAttributeMap,
					dataAttributesProvider.getAttributes(container, currentComponent, currentCmsPageRequestContextData));
		}
		return htmlElementHelper.mergeAttributeMaps(mergedAttributeMap, super.getElementAttributes());
	}

	protected List<TagDataAttributesProvider> getTagDataAttributesProviders()
	{
		return SpringHelper.getSpringBean(pageContext.getRequest(), "tagDataAttributesProviders", List.class, true);
	}

	@Override
	protected CMSPageSlotComponentService lookupCMSPageSlotComponentService()
	{
		return SpringHelper.getSpringBean(pageContext.getRequest(), "componentCMSPageSlotComponentService",
				CMSPageSlotComponentService.class, true);
	}
}

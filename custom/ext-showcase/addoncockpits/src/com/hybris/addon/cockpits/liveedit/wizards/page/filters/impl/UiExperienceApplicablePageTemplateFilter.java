/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */

package com.hybris.addon.cockpits.liveedit.wizards.page.filters.impl;

import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;

import java.util.List;
import java.util.ListIterator;

import com.hybris.addon.cockpits.liveedit.wizards.page.filters.ApplicablePageTemplateFilter;


/**
 * @author rmcotton
 *
 */
public class UiExperienceApplicablePageTemplateFilter implements ApplicablePageTemplateFilter
{

	private final UiExperienceLevel defaultLevel = UiExperienceLevel.DESKTOP;


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.steroidsliveedit.strategies.ApplicablePageTemplateFilter#filter(de.hybris.platform.cms2.model.preview
	 * .PreviewDataModel, java.util.List)
	 */
	@Override
	public void filter(final PreviewDataModel previewData, final List<PageTemplateModel> templatesToFilter)
	{
		if (previewData == null || templatesToFilter == null)
		{
			return;
		}

		final UiExperienceLevel level = (previewData.getUiExperience() == null && defaultLevel != null) ? defaultLevel
				: previewData.getUiExperience();

		if (level == null)
		{
			return;
		}


		for (final ListIterator<PageTemplateModel> li = templatesToFilter.listIterator(); li.hasNext();)
		{
			final PageTemplateModel template = li.next();
			if (template.getUid().startsWith(level.getCode()))
			{
				// retain
				continue;
			}

			boolean foundOther = false;
			for (final UiExperienceLevel level2 : UiExperienceLevel.values())
			{
				if (!level2.equals(level) && template.getUid().startsWith(level2.getCode()))
				{
					foundOther = true;
					break;
				}
			}

			if (!(!foundOther && level.equals(defaultLevel)))
			{
				li.remove();
			}

		}
	}

}

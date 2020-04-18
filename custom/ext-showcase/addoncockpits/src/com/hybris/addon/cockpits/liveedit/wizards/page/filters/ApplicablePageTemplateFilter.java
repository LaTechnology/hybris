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
package com.hybris.addon.cockpits.liveedit.wizards.page.filters;

import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;

import java.util.List;


/**
 * @author rmcotton
 * 
 */
public interface ApplicablePageTemplateFilter
{
	void filter(final PreviewDataModel previewData, final List<PageTemplateModel> templatesToFilter);
}

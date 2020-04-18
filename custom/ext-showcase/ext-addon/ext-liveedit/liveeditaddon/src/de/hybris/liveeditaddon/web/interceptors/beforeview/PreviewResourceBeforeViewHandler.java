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
package de.hybris.liveeditaddon.web.interceptors.beforeview;

import static de.hybris.platform.addonsupport.config.javascript.JavaScriptVariableDataFactory.create;
import static de.hybris.platform.addonsupport.config.javascript.JavaScriptVariableDataFactory.getVariables;

import de.hybris.platform.acceleratorcms.data.CmsPageRequestContextData;
import de.hybris.platform.acceleratorcms.services.CMSPageContextService;
import de.hybris.platform.acceleratorservices.storefront.data.JavaScriptVariableData;
import de.hybris.platform.addonsupport.interceptors.AbstractConditionalResourceBeforeHandler;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;

/**
 * 
 */
public class PreviewResourceBeforeViewHandler extends AbstractConditionalResourceBeforeHandler
{
	@Resource(name = "cmsPageContextService")
	private CMSPageContextService cmsPageContextService;

	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18nService;

	protected String pathPropertyPrefix = "preview";

	@Override
	public boolean isIncludeResource(final HttpServletRequest request, final HttpServletResponse response, final ModelMap model,
			final String viewName)
	{
		final CmsPageRequestContextData cmsPageRequestContextData = cmsPageContextService.getCmsPageRequestContextData(request);
		return (cmsPageRequestContextData.getPage() != null && cmsPageRequestContextData.isPreview());
	}

	@Override
	public String beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelMap model,
			final String viewName) throws Exception
	{
		final String viewNameReturned = super.beforeView(request, response, model, viewName);
		final CmsPageRequestContextData cmsPageRequestContextData = cmsPageContextService.getCmsPageRequestContextData(request);
		if (isIncludeResource(request, response, model, viewName))
		{
			final List<JavaScriptVariableData> variables = getVariables(model);
            variables.add(create("previewCurrentLanguage", commonI18nService.getCurrentLanguage().getIsocode()));
            variables.add(create("previewCurrentPagePk", cmsPageRequestContextData.getPage().getPk()
                    .getLongValueAsString()));
            variables.add(create("previewCurrentPageUid", cmsPageRequestContextData.getPage().getUid()));
            variables.add(create("previewCurrentUserId", cmsPageRequestContextData.getUser().getUid()));
            variables.add(create("previewCurrentJaloSessionId", cmsPageRequestContextData.getSessionId()));
		}
		return viewNameReturned;
	}

	public void setPathPropertyPrefix(final String pathPropertyPrefix)
	{
		this.pathPropertyPrefix = pathPropertyPrefix;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hybris.addon.common.interceptors.beforeview.AbstractConditionalResourceBeforeHandler#getPathPropertyPrefix()
	 */
	@Override
	public String getPathPropertyPrefix()
	{
		return pathPropertyPrefix;
	}
}

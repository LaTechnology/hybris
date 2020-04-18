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
import de.hybris.platform.catalog.model.CatalogVersionModel;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.hybris.platform.cmscockpit.enums.LiveEditVariant;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.ui.ModelMap;



/**
 * 
 */
public class LiveEditResourceBeforeViewHandler extends AbstractConditionalResourceBeforeHandler
{
	private String pathPropertyPrefix = "liveedit";
	private LiveEditVariant liveEditVariant = LiveEditVariant.QUICKEDIT;

	@Resource(name = "cmsPageContextService")
	private CMSPageContextService cmsPageContextService;

	@Override
	public boolean isIncludeResource(final HttpServletRequest request, final HttpServletResponse response, final ModelMap model,
			final String viewName)
	{
		final CmsPageRequestContextData cmsPageRequestContextData = cmsPageContextService.getCmsPageRequestContextData(request);
		final LiveEditVariant previewliveEditVariant = (cmsPageRequestContextData.getPreviewData() != null && cmsPageRequestContextData
				.getPreviewData().getLiveEditVariant() != null) ? cmsPageRequestContextData.getPreviewData().getLiveEditVariant()
				: LiveEditVariant.QUICKEDIT;
		return (cmsPageRequestContextData.getPage() != null && cmsPageRequestContextData.isLiveEdit() && previewliveEditVariant == liveEditVariant);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hybris.addon.common.interceptors.BeforeViewHandlerAdaptee#beforeView(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, org.springframework.ui.ModelMap, java.lang.String)
	 */
	@Override
	public String beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelMap model,
			final String viewName) throws Exception
	{
		final String viewNameReturned = super.beforeView(request, response, model, viewName);
		final CmsPageRequestContextData cmsPageRequestContextData = cmsPageContextService.getCmsPageRequestContextData(request);
		if (cmsPageRequestContextData.getPage() != null && cmsPageRequestContextData.isLiveEdit())
		{
			final List<JavaScriptVariableData> variables = getVariables(model);
			final CatalogVersionModel version = cmsPageRequestContextData.getPage().getCatalogVersion();
			final String uploadUrl = "/liveeditaddon/mediaupload.htm?catalogId=" + version.getCatalog().getId()
					+ "&catalogVersionId=" + version.getVersion();
            variables.add(create("liveeditMediaUploadUrl", uploadUrl));
            variables.add(create("serverPath",
                    UrlUtils.buildFullRequestUrl(request.getScheme(), request.getServerName(), request.getServerPort(), "", null)));
		}



		return viewNameReturned;
	}


	public void setPathPropertyPrefix(final String pathPropertyPrefix)
	{
		this.pathPropertyPrefix = pathPropertyPrefix;
	}

	public CMSPageContextService getCmsPageContextService()
	{
		return cmsPageContextService;
	}

	@Override
	public String getPathPropertyPrefix()
	{
		return pathPropertyPrefix;
	}

	@Override
	protected String getCommonCssPathKey()
	{
		return "addOnLiveEditCommonCssPaths";
	}

	@Override
	protected String getThemeCssPathKey()
	{
		return "addOnLiveEditThemeCssPaths";
	}

	@Override
	protected String getJavaScriptPathsKey()
	{
		return "addOnLiveEditJavaScriptPaths";
	}

	public LiveEditVariant getLiveEditVariant()
	{
		return liveEditVariant;
	}

	public void setLiveEditVariant(final LiveEditVariant liveEditVariant)
	{
		this.liveEditVariant = liveEditVariant;
	}

	public void setLiveEditVariantCode(final String liveEditModeCode)
	{
		this.liveEditVariant = LiveEditVariant.valueOf(liveEditModeCode);
	}

}

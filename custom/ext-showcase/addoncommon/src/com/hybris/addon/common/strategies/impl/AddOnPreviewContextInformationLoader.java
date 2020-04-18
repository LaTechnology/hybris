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
package com.hybris.addon.common.strategies.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.acceleratorcms.preview.strategies.PreviewContextInformationLoaderStrategy;
import com.hybris.addon.common.strategies.SiteContextInformationLoaderStrategy;

import de.hybris.platform.acceleratorcms.context.impl.DefaultContextInformationLoader;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;


/**
 * @author przemyslaw.muzyk, dariusz.malachowski
 * 
 */
public class AddOnPreviewContextInformationLoader extends DefaultContextInformationLoader
{
	private List<SiteContextInformationLoaderStrategy> siteContextLoadStrategies;
	private List<PreviewContextInformationLoaderStrategy> strategies;
	private List<PreviewContextInformationLoaderStrategy> previewRequestStrategies;

	@Override
	public CMSSiteModel initializeSiteFromRequest(String absoluteURL)
	{
		CMSSiteModel site = super.initializeSiteFromRequest(absoluteURL);
		initializeSiteContextStrategies(site, siteContextLoadStrategies);
		return site;
	}

	@Override
	public void loadFakeContextInformation(final HttpServletRequest httpRequest, final PreviewDataModel previewData)
	{
		initializePreviewContextStrategies(strategies, previewData);
		storePreviewTicketIDWithinSession(httpRequest);
	}

	@Override
	public void initializePreviewRequest(final PreviewDataModel previewDataModel)
	{
		initializePreviewContextStrategies(previewRequestStrategies, previewDataModel);
	}

	protected void initializeSiteContextStrategies(CMSSiteModel site, List<SiteContextInformationLoaderStrategy> strategyList)
	{
		for (SiteContextInformationLoaderStrategy strategy : strategyList)
		{
			strategy.loadContextInformation(site);
		}
	}

	protected void initializePreviewContextStrategies(List<PreviewContextInformationLoaderStrategy> strategyList,
			final PreviewDataModel previewData)
	{
		for (final PreviewContextInformationLoaderStrategy strategy : strategyList)
		{
			strategy.initContextFromPreview(previewData);
		}
	}

	public static class LoadUserStrategy extends DefaultContextInformationLoader implements
			PreviewContextInformationLoaderStrategy
	{
		@Override
		public void initContextFromPreview(final PreviewDataModel preview)
		{
			super.loadFakeUser(preview.getUser());

		}
	}

	public static class LoadUserGroupStrategy extends DefaultContextInformationLoader implements
			PreviewContextInformationLoaderStrategy
	{

		@Override
		public void initContextFromPreview(final PreviewDataModel preview)
		{

			super.loadFakeUserGroup(preview);

		}

	}

	public static class LoadLanguageStrategy extends DefaultContextInformationLoader implements
			PreviewContextInformationLoaderStrategy
	{

		@Override
		public void initContextFromPreview(final PreviewDataModel preview)
		{
			if (preview.getLanguage() != null)
			{
				super.loadFakeLanguage(preview.getLanguage());
			}

		}

	}

	public static class LoadDateStrategy extends DefaultContextInformationLoader implements
			PreviewContextInformationLoaderStrategy
	{

		@Override
		public void initContextFromPreview(final PreviewDataModel preview)
		{
			super.loadFakeDate(preview.getTime());
		}

	}

	public List<PreviewContextInformationLoaderStrategy> getStrategies()
	{
		return strategies;
	}

	@Required
	public void setStrategies(List<PreviewContextInformationLoaderStrategy> strategies)
	{
		this.strategies = strategies;
	}

	public List<PreviewContextInformationLoaderStrategy> getPreviewRequestStrategies()
	{
		return previewRequestStrategies;
	}

	@Required
	public void setPreviewRequestStrategies(final List<PreviewContextInformationLoaderStrategy> previewRequestStrategies)
	{
		this.previewRequestStrategies = previewRequestStrategies;
	}

	public List<SiteContextInformationLoaderStrategy> getSiteContextLoadStrategies()
	{
		return siteContextLoadStrategies;
	}

	@Required
	public void setSiteContextLoadStrategies(List<SiteContextInformationLoaderStrategy> siteContextLoadStrategies)
	{
		this.siteContextLoadStrategies = siteContextLoadStrategies;
	}

}

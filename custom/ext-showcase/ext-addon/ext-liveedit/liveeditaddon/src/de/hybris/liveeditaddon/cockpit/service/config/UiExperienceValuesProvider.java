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
package de.hybris.liveeditaddon.cockpit.service.config;

import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.services.config.AvailableValuesProvider;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.site.BaseSiteService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * 
 */
public class UiExperienceValuesProvider implements AvailableValuesProvider
{
	private SiteConfigService siteConfigService;
	private SessionService sessionService;
	private BaseSiteService baseSiteService;
	private CMSAdminSiteService cmsAdminSiteService;

	@Override
	public List<? extends Object> getAvailableValues(final PropertyDescriptor propertyDescriptor)
	{
		return getSessionService().executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public Object execute()
			{
				if (getCmsAdminSiteService().getActiveSite() == null)
				{
					return Collections.EMPTY_LIST;
				}

				getBaseSiteService().setCurrentBaseSite(getCmsAdminSiteService().getActiveSite(), false);
				final String[] levelsAsString = StringUtils.split(
						getSiteConfigService().getProperty("storefront.supportedUiExperienceLevels"), ",");

				if (levelsAsString == null)
				{
					return Collections.EMPTY_LIST;
				}

				final Set<UiExperienceLevel> levels = new LinkedHashSet<UiExperienceLevel>();
				for (int i = 0; i < levelsAsString.length; i++)
				{
					final UiExperienceLevel level = UiExperienceLevel.valueOf(levelsAsString[i]);
					levels.add(level);
				}
				return new ArrayList<UiExperienceLevel>(levels);
			}
		});
	}

	public SiteConfigService getSiteConfigService()
	{
		return siteConfigService;
	}

	@Required
	public void setSiteConfigService(final SiteConfigService siteConfigService)
	{
		this.siteConfigService = siteConfigService;
	}

	public SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	public CMSAdminSiteService getCmsAdminSiteService()
	{
		return cmsAdminSiteService;
	}

	@Required
	public void setCmsAdminSiteService(final CMSAdminSiteService cmsAdminSiteService)
	{
		this.cmsAdminSiteService = cmsAdminSiteService;
	}
}

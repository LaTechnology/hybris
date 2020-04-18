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
package de.hybris.liveeditaddon.admin.facades;

import de.hybris.platform.acceleratorservices.uiexperience.UiExperienceService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.preview.CMSPreviewTicketModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPreviewService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.liveeditaddon.admin.ActionMenuRequestData;
import de.hybris.liveeditaddon.admin.ComponentAdminMenuGroupData;
import de.hybris.liveeditaddon.admin.facades.impl.PreviewTicketInvalidException;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Required;


/**
 * 
 */
public abstract class AbstractCMSAdminFacade
{
	private SessionService sessionService;
	private CatalogVersionService catalogVersionService;
	private CMSPreviewService cmsPreviewService;
	private BaseSiteService baseSiteService;
	private UiExperienceService uiExperienceService;

	protected ComponentAdminMenuGroupData getAdminMenu(final ActionMenuRequestData request) throws PreviewTicketInvalidException
	{
		final Object result = getSessionService().executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public Object execute()
			{

				final CMSPreviewTicketModel ticket = getCmsPreviewService().getPreviewTicket(request.getPreviewTicket());
				if (ticket == null)
				{
					return new PreviewTicketInvalidException(request.getPreviewTicket() + " is not a valid preview ticket.");
				}
				initializePreviewRequest(ticket.getPreviewData());
				return getAdminMenuGroupConverter().convert(request);
			}
		});
		if (result instanceof PreviewTicketInvalidException)
		{
			throw (PreviewTicketInvalidException) result;
		}
		return (ComponentAdminMenuGroupData) result;
	}

	protected void initializePreviewRequest(final PreviewDataModel previewDataModel)
	{
		loadActiveBaseSite(previewDataModel);
		loadCatalogVersions(previewDataModel.getCatalogVersions());
		loadUiExperienceLevel(previewDataModel);
	}

	protected void loadActiveBaseSite(final PreviewDataModel previewDataModel)
	{
		final BaseSiteService baseSiteService = getBaseSiteService();
		if (previewDataModel.getActiveSite() != null)
		{
			baseSiteService.setCurrentBaseSite(previewDataModel.getActiveSite(), true);
		}
	}

	public void loadCatalogVersions(final Collection<CatalogVersionModel> catalogVersions)
	{
		getCatalogVersionService().setSessionCatalogVersions(catalogVersions);
	}

	public void loadUiExperienceLevel(final PreviewDataModel previewDataModel)
	{
		getUiExperienceService().setDetectedUiExperienceLevel(previewDataModel.getUiExperience());
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

	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	public CMSPreviewService getCmsPreviewService()
	{
		return cmsPreviewService;
	}

	@Required
	public void setCmsPreviewService(final CMSPreviewService cmsPreviewService)
	{
		this.cmsPreviewService = cmsPreviewService;
	}

	public UiExperienceService getUiExperienceService()
	{
		return uiExperienceService;
	}

	@Required
	public void setUiExperienceService(final UiExperienceService uiExperienceService)
	{
		this.uiExperienceService = uiExperienceService;
	}

	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	protected abstract Converter getAdminMenuGroupConverter();
}

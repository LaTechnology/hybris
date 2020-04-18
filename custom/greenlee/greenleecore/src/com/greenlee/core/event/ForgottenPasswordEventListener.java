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
package com.greenlee.core.event;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.commerceservices.model.process.ForgottenPasswordProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import org.springframework.beans.factory.annotation.Required;


/**
 * Listener for "forgotten password" functionality event.
 */
public class ForgottenPasswordEventListener extends AbstractSiteEventListener<GreenleeForgottenPwdEvent>
{

	private ModelService modelService;
	private BusinessProcessService businessProcessService;

	@Override
	protected void onSiteEvent(final GreenleeForgottenPwdEvent event)
	{
		final ForgottenPasswordProcessModel forgottenPasswordProcessModel = (ForgottenPasswordProcessModel) getBusinessProcessService()
				.createProcess("forgottenPassword-" + event.getCustomer().getUid() + "-" + System.currentTimeMillis(),
						"forgottenPasswordEmailProcess");
		forgottenPasswordProcessModel.setSite(event.getSite());
		forgottenPasswordProcessModel.setCustomer(event.getCustomer());
		forgottenPasswordProcessModel.setToken(event.getToken());
		forgottenPasswordProcessModel.setLanguage(event.getLanguage());
		forgottenPasswordProcessModel.setCurrency(event.getCurrency());
		forgottenPasswordProcessModel.setStore(event.getBaseStore());
		forgottenPasswordProcessModel.setUserAddedByAdmin(event.getUserCreatedByAdmin());
		getModelService().save(forgottenPasswordProcessModel);
		getBusinessProcessService().startProcess(forgottenPasswordProcessModel);
	}

	@Override
	protected boolean shouldHandleEvent(final GreenleeForgottenPwdEvent event)
	{
		final BaseSiteModel site = event.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage("event.site", site);
		return SiteChannel.B2B.equals(site.getChannel());
	}

	protected BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	@Required
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

	/**
	 * @return the modelService
	 */
	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

}

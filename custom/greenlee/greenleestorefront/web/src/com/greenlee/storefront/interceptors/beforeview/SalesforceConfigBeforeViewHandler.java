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
package com.greenlee.storefront.interceptors.beforeview;

import de.hybris.platform.acceleratorservices.config.SiteConfigService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;

import com.greenlee.storefront.interceptors.BeforeViewHandler;


/**
 */
public class SalesforceConfigBeforeViewHandler implements BeforeViewHandler
{
	private SiteConfigService siteConfigService;

	protected SiteConfigService getSiteConfigService()
	{
		return siteConfigService;
	}

	@Required
	public void setSiteConfigService(final SiteConfigService siteConfigService)
	{
		this.siteConfigService = siteConfigService;
	}

	@Override
	public void beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelAndView modelAndView)
			throws Exception
	{
		modelAndView.addObject("salesforce_form_action", getSiteConfigService().getString("salesforce.action", "NA"));
		modelAndView.addObject("salesforce_form_orgid", getSiteConfigService().getString("salesforce.orgid", "NA"));
		modelAndView.addObject("salesforce_form_returl", getSiteConfigService().getString("salesforce.returl", "NA"));
		modelAndView.addObject("salesforce_form_recordtype", getSiteConfigService().getString("salesforce.recordtype", "NA"));
	}
}

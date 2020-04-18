///*
// * [y] hybris Platform
// *
// * Copyright (c) 2000-2013 hybris AG
// * All rights reserved.
// *
// * This software is the confidential and proprietary information of hybris
// * ("Confidential Information"). You shall not disclose such Confidential
// * Information and shall use it only in accordance with the terms of the
// * license agreement you entered into with hybris.
// *
// *
// */
//package com.hybris.addon.cockpits.session.liveedit.impl;
//
//import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminComponentService;
//import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
//import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
//import de.hybris.platform.cmscockpit.services.CmsCockpitService;
//import de.hybris.platform.servicelayer.model.ModelService;
//import de.hybris.platform.yacceleratorcockpits.cmscockpit.components.contentbrowser.DefaultCmsPageMainAreaPreviewComponentFactory;
//import de.hybris.platform.yacceleratorcockpits.cmscockpit.session.impl.DefaultCmsPageBrowserModel;
//
//import com.hybris.addon.cockpits.components.cms.contentbrowser.AddOnCmsPageMainAreaPreviewComponentFactory;
//
//
///**
// * @author rmcotton
// *
// */
//public class AddOnCmsPageBrowserModel extends DefaultCmsPageBrowserModel
//{
//
//	/**
//	 * @param cmsAdminSiteService
//	 * @param cmsCockpitService
//	 * @param modelService
//	 * @param cmsAdminComponentService
//	 * @param cmsAdminContentSlotService
//	 */
//	public AddOnCmsPageBrowserModel(final CMSAdminSiteService cmsAdminSiteService, final CmsCockpitService cmsCockpitService,
//			final ModelService modelService, final CMSAdminComponentService cmsAdminComponentService,
//			final CMSAdminContentSlotService cmsAdminContentSlotService)
//	{
//		super(cmsAdminSiteService, cmsCockpitService, modelService, cmsAdminComponentService, cmsAdminContentSlotService);
//		// YTODO Auto-generated constructor stub
//	}
//
//	@Override
//	protected DefaultCmsPageMainAreaPreviewComponentFactory newDefaultCmsPageMainAreaPreviewComponentFactory()
//	{
//		return new AddOnCmsPageMainAreaPreviewComponentFactory(getCurrentPageObject());
//	}
//}

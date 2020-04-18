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
package de.hybris.liveeditaddon.cockpit.navigationbargenerator.impl;

import de.hybris.liveeditaddon.cockpit.navigationbargenerator.CatalogDataService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.site.BaseSiteService;
import org.apache.log4j.Logger;

import java.util.List;


public class DefaultCatalogDataService implements CatalogDataService
{
	private CMSAdminSiteService cmsAdminSiteService;
    private BaseSiteService baseSiteService;

	private static final Logger LOG = Logger.getLogger(DefaultCatalogDataService.class);

	@Override
	public CatalogModel getProductCatalogVersions()
	{
        final List<CatalogModel> productCatalogs = baseSiteService.getProductCatalogs(cmsAdminSiteService.getActiveSite());
        if ((null == productCatalogs) || (productCatalogs.isEmpty())) {
            return null;
        }
		return productCatalogs.get(0);
	}

    //GETTERS AND SETTERS
    public CMSAdminSiteService getCmsAdminSiteService() {
        return cmsAdminSiteService;
    }

    public void setCmsAdminSiteService(CMSAdminSiteService cmsAdminSiteService) {
        this.cmsAdminSiteService = cmsAdminSiteService;
    }

    public BaseSiteService getBaseSiteService() {
        return baseSiteService;
    }

    public void setBaseSiteService(BaseSiteService baseSiteService) {
        this.baseSiteService = baseSiteService;
    }
}

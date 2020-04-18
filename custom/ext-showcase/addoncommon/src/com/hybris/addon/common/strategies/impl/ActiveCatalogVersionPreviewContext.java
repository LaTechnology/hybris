package com.hybris.addon.common.strategies.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.acceleratorcms.preview.strategies.PreviewContextInformationLoaderStrategy;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.site.BaseSiteService;


/**
 * ActiveCatalogVersionPreviewContext takes care of activating the catalog versions and base site at runtime base on
 * preview context
 *
 * @author a.andone
 *
 */
public class ActiveCatalogVersionPreviewContext implements PreviewContextInformationLoaderStrategy
{
	private BaseSiteService baseSiteService;
	private CatalogVersionService catalogVersionService;

	private static final Logger LOG = Logger.getLogger(ActiveCatalogVersionPreviewContext.class);

	public void initContextFromPreview(PreviewDataModel preview)
	{
		Collection<CatalogVersionModel> previewCatalogVersions = preview.getCatalogVersions();
		Collection<CatalogVersionModel> sessionCatalogVersions = Collections.unmodifiableCollection(catalogVersionService
				.getSessionCatalogVersions());

		setBaseSite(preview);
		setActiveCatalogs(preview, previewCatalogVersions, sessionCatalogVersions);
	}

	private void setActiveCatalogs(PreviewDataModel preview, Collection<CatalogVersionModel> previewCatalogVersions,
			Collection<CatalogVersionModel> sessionCatalogVersions)
	{
		CatalogVersionModel sessionContentCatalogVersion = null;
		CatalogVersionModel previewContentCatalogVersion = null;

		for (CatalogVersionModel catalogVersionModel : sessionCatalogVersions)
		{
			if (catalogVersionModel.getCatalog() instanceof ContentCatalogModel)
			{
				sessionContentCatalogVersion = catalogVersionModel;
			}
		}

		for (CatalogVersionModel catalogVersionModel : previewCatalogVersions)
		{
			if (catalogVersionModel.getCatalog() instanceof ContentCatalogModel)
			{
				previewContentCatalogVersion = catalogVersionModel;
			}
		}

		//check if a different content catalog is selected from navigation
		boolean contentChanged = sessionContentCatalogVersion != null && previewContentCatalogVersion != null
				&& !previewContentCatalogVersion.getVersion().equals(sessionContentCatalogVersion.getVersion());

		//if content catalog from session is null, it is after first login
		//or content catalog has been changed
		//or product catalog version has not been changed from preview context
		if (sessionContentCatalogVersion == null || previewContentCatalogVersion == null || contentChanged
				)
		{
			CatalogVersionModel currentCatalogVersion = previewContentCatalogVersion;
			final Set<CatalogVersionModel> applicableCatalogVersions = getApplicableCatalogVersions(preview, currentCatalogVersion);

			applyCatalogVersions(preview, currentCatalogVersion, applicableCatalogVersions);

		}
		else
		{
			//set session catalogs selected from preview content
			catalogVersionService.setSessionCatalogVersions(preview.getCatalogVersions());
		}
	}

	private void applyCatalogVersions(PreviewDataModel preview, CatalogVersionModel currentCatalogVersion,
			Set<CatalogVersionModel> applicableCatalogVersions)
	{
		//set the new catalogs on session
		catalogVersionService.setSessionCatalogVersions(applicableCatalogVersions);

		//set the new catalogs on preview context
		preview.setCatalogVersions(applicableCatalogVersions);
		preview.setActiveCatalogVersion(currentCatalogVersion);
	}

	private Set<CatalogVersionModel> getApplicableCatalogVersions(PreviewDataModel preview,
			CatalogVersionModel currentCatalogVersion)
	{
		final String versionStringToMatch = currentCatalogVersion.getVersion();
		final List<CatalogModel> productCatalogs = baseSiteService.getProductCatalogs(preview.getActiveSite());

		final Set<CatalogVersionModel> applicableCatalogVersions = new LinkedHashSet<CatalogVersionModel>();

		//first add the content catalog
		applicableCatalogVersions.add(currentCatalogVersion);

		//add the product catalog having the same version with content
		for (final CatalogModel catalog : productCatalogs)
		{
			for (final CatalogVersionModel catalogVersion : catalog.getCatalogVersions())
			{
				if (catalogVersion.getVersion().equals(versionStringToMatch))
				{
					applicableCatalogVersions.add(catalogVersion);
				}
			}
		}

		if (CollectionUtils.isEmpty(applicableCatalogVersions))
		{
			for (final CatalogModel catalog : productCatalogs)
			{
				applicableCatalogVersions.add(catalog.getActiveCatalogVersion());
			}
		}
		return applicableCatalogVersions;
	}

	private void setBaseSite(PreviewDataModel preview)
	{
		final BaseSiteService baseSiteService = getBaseSiteService();

		if (preview.getActiveSite() == null)
		{
			LOG.warn("Could not set active site. Reason: No active site was selected!");
		}
		else
		{
			baseSiteService.setCurrentBaseSite(preview.getActiveSite(), false);
		}
	}

	/**
	 * @return the baseSiteService
	 */
	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	/**
	 * @param baseSiteService
	 *           the baseSiteService to set
	 */
	@Required
	public void setBaseSiteService(BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	/**
	 * @return the catalogVersionService
	 */
	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	/**
	 * @param catalogVersionService
	 *           the catalogVersionService to set
	 */
	@Required
	public void setCatalogVersionService(CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

}

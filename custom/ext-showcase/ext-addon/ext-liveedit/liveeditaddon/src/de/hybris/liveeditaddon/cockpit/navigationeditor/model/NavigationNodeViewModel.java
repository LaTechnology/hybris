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
package de.hybris.liveeditaddon.cockpit.navigationeditor.model;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditViewModel;
import de.hybris.platform.core.model.media.MediaModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import de.hybris.liveeditaddon.cockpit.util.NavigationPackHelper;


/**
 * 
 */
public class NavigationNodeViewModel
{
	private Map<String, String> names = NavigationPackHelper.getDefaultLangMap();
	private String uid;
	private String bannerLinkURL;
	private Collection<NavigationColumnViewModel> navigationNodeColumns = new ArrayList<NavigationColumnViewModel>();
	private String styleClass;
	private NavigationLinkViewModel navBarLink;
	private Integer wrapAfter;
	private Collection<AbstractRestrictionModel> restrictions = new ArrayList<AbstractRestrictionModel>();
	private final LiveEditViewModel liveEditView;
	private CatalogVersionModel catalogVersion;
	private final ContentSlotModel contentSlot;

	public NavigationNodeViewModel(final LiveEditViewModel liveEditView, final ContentSlotModel contentSlot)
	{
		this.liveEditView = liveEditView;
		this.contentSlot = contentSlot;
	}


	public void setNames(final Map<String, String> names)
	{
		this.names = names;
	}

	public void setName(final String name)
	{
		this.names.put(liveEditView.getCurrentPreviewData().getLanguage().getIsocode(), name);
	}

	public void setUid(final String uid)
	{
		this.uid = uid;
	}

	public String getName()
	{
		return names.get(liveEditView.getCurrentPreviewData().getLanguage().getIsocode());
	}

	public Map<String, String> getNames()
	{
		return names;
	}


	public void setBannerLinkURL(final String bannerLinkURL)
	{
		this.bannerLinkURL = bannerLinkURL;
	}


	public String getUid()
	{
		return uid;
	}

	public String getBannerLinkURL()
	{
		return bannerLinkURL;
	}

	public void setBanner(final MediaModel object)
	{
		// YTODO add media model for banner

	}

	public Collection<NavigationColumnViewModel> getNavigationNodeColumns()
	{
		return navigationNodeColumns;
	}

	public void setNavigationNodeColumns(final Collection<NavigationColumnViewModel> cols)
	{
		this.navigationNodeColumns = cols;
	}

	public NavigationLinkViewModel getNavBarLink()
	{
		return navBarLink;
	}

	public void setNavBarLink(final NavigationLinkViewModel navBarLink)
	{
		this.navBarLink = navBarLink;
	}

	public String getStyleClass()
	{
		return styleClass;
	}

	public void setStyleClass(final String styleClass)
	{
		this.styleClass = styleClass;
	}

	public Integer getWrapAfter()
	{
		return wrapAfter;
	}

	public void setWrapAfter(final Integer wrapAfter)
	{
		this.wrapAfter = wrapAfter;
	}

	public Collection<AbstractRestrictionModel> getRestrictions()
	{
		return restrictions;
	}

	public void setRestrictions(final Collection<AbstractRestrictionModel> restrictions)
	{
		this.restrictions = restrictions;
	}

	public void setCatalogVersion(final CatalogVersionModel catalogVersion)
	{
		this.catalogVersion = catalogVersion;
	}

	public CatalogVersionModel getCatalogVersion()
	{
		return this.catalogVersion;
	}

	public LiveEditViewModel getLiveEditView()
	{
		return this.liveEditView;
	}

	public ContentSlotModel getContentSlot()
	{
		return this.contentSlot;
	}
}

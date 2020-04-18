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

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.hybris.liveeditaddon.enums.CMSMenuItemType;
import de.hybris.liveeditaddon.cockpit.util.NavigationPackHelper;


/**
 * 
 */
public class NavigationLinkViewModel
{
	private String url;
	private Map<String, String> names = new HashMap<String, String>();
	private CMSMenuItemType menuItemType;
	private ProductModel product;
	private CategoryModel category;
	private ContentPageModel page;
	private NavigationColumnViewModel column;
	private boolean deleted = false;
	private CMSLinkComponentModel model;
	private Collection<AbstractRestrictionModel> restrictions = new ArrayList<AbstractRestrictionModel>();
	private final String defaultLanguage;

	public NavigationLinkViewModel(final String defaultLanguageIso)
	{
		this.defaultLanguage = defaultLanguageIso;
	}

	public void setName(final String lang, final String name)
	{
		this.names.put(lang, name);
	}

	public void setNames(final Map<String, String> names)
	{
		if (names != null)
		{
			this.names = new HashMap<String, String>(names);
		}
		else
		{
			this.names = new HashMap<String, String>();
		}
	}

	public void setURL(final String label)
	{
		this.url = label;
	}

	public void setMenuItemType(final CMSMenuItemType menuItemType)
	{
		this.menuItemType = menuItemType;
	}

	public String getName()
	{
		if (names.get(getCurrentLanguageIsoCode()) == null)
		{
			return "";
		}
		return names.get(getCurrentLanguageIsoCode());
	}

	private String getCurrentLanguageIsoCode()
	{
		if (StringUtils.isNotBlank(defaultLanguage))
		{
			return defaultLanguage;
		}
		//works only for links in columns
		if (column == null || column.getParentNavigationNode() == null)
		{
			return null;
		}
		return column.getParentNavigationNode().getLiveEditView().getCurrentPreviewData().getLanguage().getIsocode();
	}

	public Map<String, String> getNames()
	{
		return names;
	}

	public String getURL()
	{
		return url;
	}

	public CMSMenuItemType getMenuItemType()
	{
		return menuItemType;
	}

	public ProductModel getProduct()
	{
		return product;
	}

	public void setProduct(final ProductModel product)
	{
		this.product = product;
	}

	public CategoryModel getCategory()
	{
		return category;
	}

	public void setCategory(final CategoryModel category)
	{
		this.category = category;
	}

	public ContentPageModel getPage()
	{
		return page;
	}

	public void setPage(final ContentPageModel page)
	{
		this.page = page;
	}

	public NavigationColumnViewModel getColumn()
	{
		return column;
	}

	public void setColumn(final NavigationColumnViewModel column)
	{
		this.column = column;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if ((obj == null) || (obj.getClass() != this.getClass()))
		{
			return false;
		}

		final NavigationLinkViewModel link = (NavigationLinkViewModel) obj;

		return NavigationPackHelper.cleanFacetValueText(this.getName()).equals(
				NavigationPackHelper.cleanFacetValueText(link.getName()));
	}

	public boolean isDeleted()
	{
		return deleted;
	}

	public void delete()
	{
		this.deleted = true;
	}

	public void setModel(final CMSLinkComponentModel linkModel)
	{
		model = linkModel;
	}

	public CMSLinkComponentModel getModel()
	{
		return this.model;
	}

	public Collection<AbstractRestrictionModel> getRestrictions()
	{
		return restrictions;
	}

	public void setRestrictions(final Collection<AbstractRestrictionModel> restrictions)
	{
		this.restrictions = restrictions;
	}
}

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
package de.hybris.liveeditaddon.cockpit.navigationbargenerator.data;

import java.util.*;


/**
 * 
 * Created: Jun 15, 2012
 * 
 * 
 */
public class CategoryData
{
	private String code;
	private String name;
	private Map<String, String> names = new HashMap<String, String>();
	private String url;
	private boolean products = false;
	private CategoryData supercategory;
	private Collection<CategoryData> subcategories = new ArrayList<CategoryData>();
	private boolean main = false;

	public String getCode()
	{
		return code;
	}

	public void setCode(final String code)
	{
		this.code = code;
	}

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public Map<String, String> getNames()
	{
		return names;
	}

	public void setNames(final Map<String, String> names)
	{
		this.names = names;
	}

	public void addName(final String languageIsoCode, final String value)
	{
		names.put(languageIsoCode, value);
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(final String url)
	{
		this.url = url;
	}

	public boolean hasProducts()
	{
		return products;
	}

	public void hasProducts(final boolean products)
	{
		this.products = products;
	}

	public CategoryData getSupercategory()
	{
		return supercategory;
	}

	public void setSupercategory(final CategoryData supercategory)
	{
		this.supercategory = supercategory;
	}

	public Collection<CategoryData> getSubcategories()
	{
		return subcategories;
	}

	public void setSubcategories(final Collection<CategoryData> subcategories)
	{
		this.subcategories = subcategories;
	}

	public boolean isMain()
	{
		return main;
	}

	public void setMain(final boolean main)
	{
		this.main = main;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		final StringBuilder out = new StringBuilder();
		final String supCat = supercategory == null ? "" : supercategory.getCode();
		out.append("Category code=[").append(code);
		out.append("], name=[").append(names);
		out.append("], url=[").append(url);
		out.append("], products? [").append(products);
		out.append("], supCat=[").append(supCat);
		final List<String> subCodes = new ArrayList<String>(subcategories.size());
		for (final CategoryData subCat : subcategories)
		{
			subCodes.add(subCat.getCode());
		}
		out.append("], subCat=[(").append(subCodes);
		out.append(")]");
		return out.toString();
	}

	/**
	 * @return
	 */
	public boolean hasLonelySubcategories()
	{
		for (final CategoryData subcategory : subcategories)
		{
			if (subcategory.getSubcategories().isEmpty())
			{
				return true;
			}
		}
		return false;
	}

}

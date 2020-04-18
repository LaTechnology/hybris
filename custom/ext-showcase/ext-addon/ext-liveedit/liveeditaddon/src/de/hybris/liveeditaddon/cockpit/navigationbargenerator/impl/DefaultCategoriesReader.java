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

import com.google.common.base.Preconditions;
import de.hybris.liveeditaddon.cockpit.navigationbargenerator.CategoriesReader;
import de.hybris.liveeditaddon.cockpit.navigationbargenerator.converters.CategoryConverter;
import de.hybris.liveeditaddon.cockpit.navigationbargenerator.data.CategoryData;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import org.springframework.beans.factory.annotation.Required;

import java.util.*;


/**
 * Created: Jun 13, 2012
 * 
 * 
 */
public class DefaultCategoriesReader implements CategoriesReader
{
	private CatalogVersionService catalogVersionService;
	private CategoryService categoryService;
	private CategoryConverter categoryConverter;

	@Override
	public Collection<CategoryModel> getRootCategories(final CatalogVersionModel catalogVersionModel)
	{
		Preconditions.checkNotNull(catalogVersionModel);
		return categoryService.getRootCategoriesForCatalogVersion(catalogVersionModel);
	}

	@Override
	public Collection<CategoryModel> getRootCategories(final String catalogId, final String catalogVersionName)
	{
		Preconditions.checkNotNull(catalogId);
		Preconditions.checkNotNull(catalogVersionName);
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(catalogId, catalogVersionName);
		return getRootCategories(catalogVersion);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CategoryData findCategoriesForCatalog(final String catalogId, final String catalogVersionName, final Integer level,
			final boolean shouldHaveProducts, final Set<String> localizations)
	{
		final CategoryData target = new CategoryData();
		target.setCode("0_0");
		target.setNames(convertLocalizationsToRootMap(localizations));
		target.hasProducts(shouldHaveProducts);
		target.setUrl("");

		final Collection<CategoryModel> rootCategories = getRootCategories(catalogId, catalogVersionName);
		final Collection rootCategoriesData = new ArrayList<CategoryData>(rootCategories.size());
		for (final CategoryModel rootCategory : rootCategories)
		{
			final CategoryData category = findCategoriesForCategory(rootCategory, level);
			category.setSupercategory(target);
			category.setMain(true);
			rootCategoriesData.add(category);
		}
		target.setSubcategories(rootCategoriesData);
		return target;
	}

	@Override
	public CategoryData findCategoriesForCatalog(final String catalogId, final String catalogVersionName,
			final String[] rootCategoryCodes, final Integer level, final boolean shouldHaveProducts, final Set<String> localizations)
	{
		final CategoryData target = new CategoryData();
		target.setCode("0_0");
		target.setNames(convertLocalizationsToRootMap(localizations));
		target.hasProducts(shouldHaveProducts);
		target.setUrl("");

		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(catalogId, catalogVersionName);

		final Collection<CategoryModel> rootCategories = new ArrayList<CategoryModel>();
		for (final String rootCategoryCode : rootCategoryCodes)
		{
			rootCategories.add(categoryService.getCategoryForCode(catalogVersion, rootCategoryCode));
		}
		final Collection rootCategoriesData = new ArrayList<CategoryData>(rootCategories.size());
		for (final CategoryModel rootCategory : rootCategories)
		{
			final CategoryData category = findCategoriesForCategory(rootCategory, level);
			category.setSupercategory(target);
			category.setMain(true);
			rootCategoriesData.add(category);
		}
		target.setSubcategories(rootCategoriesData);
		return target;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CategoryData findCategoriesForCategory(final String catalogId, final String catalogVersionName,
			final String rootCategory, final Integer level)
	{
		final CategoryData target = new CategoryData();
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(catalogId, catalogVersionName);

		final CategoryModel category = categoryService.getCategoryForCode(catalogVersion, rootCategory);
		categoryConverter.populate(category, target);

		// sort categories by name
		final List<CategoryData> subcategories = new ArrayList(target.getSubcategories());
		if (!subcategories.isEmpty())
		{
			Collections.sort(subcategories, new Comparator<CategoryData>()
			{
				@Override
				public int compare(final CategoryData o1, final CategoryData o2)
				{
					return o2.getName().compareTo(o1.getName());
				}
			});
		}

		for (final CategoryData subCat : subcategories)
		{
			subCat.setMain(true);
		}
		return target;
	}

	private CategoryData findCategoriesForCategory(final CategoryModel category, final Integer level)
	{
		final CategoryData target = new CategoryData();
		categoryConverter.setSubCategoriesLevel(level.intValue());
		categoryConverter.populate(category, target);
		return target;
	}

	Map<String, String> convertLocalizationsToRootMap(Set<String> localizations)
	{
		Preconditions.checkNotNull(localizations);
		Preconditions.checkArgument(!localizations.isEmpty(), "Localization set shouldn't be null");

		Map<String, String> result = new HashMap<String, String>();
		for (String localization : localizations)
		{
			result.put(localization, "root");
		}
		return result;
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

	public CategoryService getCategoryService()
	{
		return categoryService;
	}

	@Required
	public void setCategoryService(final CategoryService categoryService)
	{
		this.categoryService = categoryService;
	}

	public CategoryConverter getCategoryConverter()
	{
		return categoryConverter;
	}

	@Required
	public void setCategoryConverter(final CategoryConverter categoryConverter)
	{
		this.categoryConverter = categoryConverter;
	}
}

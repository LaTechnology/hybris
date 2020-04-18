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
package de.hybris.liveeditaddon.cockpit.navigationbargenerator.converters;

import de.hybris.liveeditaddon.cockpit.navigationbargenerator.data.CategoryData;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import java.util.*;


/**
 * Converter implementation for {@link de.hybris.platform.category.model.CategoryModel} as source and {@link CategoryData} as target type.
 */
public class CategoryConverter
{
	private I18NService i18nService;
	private UrlResolver<CategoryModel> categoryModelUrlResolver;
	private int subCategoriesLevel = 1;
	private static Set<Locale> locales;

	private Set<Locale> getLocales()
	{
		if (locales == null || locales.isEmpty())
		{
			locales = i18nService.getSupportedLocales();
		}
		return locales;
	}

	protected int getSubCategoriesLevel()
	{
		return subCategoriesLevel;
	}

	public void setSubCategoriesLevel(final int subCategoriesLevel)
	{
		this.subCategoriesLevel = subCategoriesLevel;
	}

	public void populate(final CategoryModel source, final CategoryData target)
	{
		populateWithSubCategories(source, target, subCategoriesLevel);
	}

	protected void populateWithSubCategories(final CategoryModel source, final CategoryData target, final int subLevel)
	{
		populateBasic(source, target);
		final String catalogId = source.getCatalogVersion().getCatalog().getId();
		final List<CategoryModel> subCategories = source.getCategories();
		final List<CategoryData> subCategoriesData = new ArrayList<CategoryData>(subCategories.size());
		if (subLevel > 0)
		{
			final Collection<CategoryModel> filteredCategories = filterCategories(subCategories, catalogId);
			for (final CategoryModel subCategory : filteredCategories)
			{
				final CategoryData subCategoryData = createTarget();
				subCategoryData.setSupercategory(target);
				populateWithSubCategories(subCategory, subCategoryData, subLevel - 1);
				subCategoriesData.add(subCategoryData);
			}
		}
		target.setSubcategories(subCategoriesData);
	}

	protected void populateBasic(final CategoryModel source, final CategoryData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");


		target.setCode(source.getCode());
		target.setName(source.getName());
		for (final Locale locale : getLocales())
		{
			final String name = source.getName(locale);
			if (name != null && !name.isEmpty())
			{
				target.addName(locale.getLanguage(), name);
			}
		}
		final List<ProductModel> products = source.getProducts();
		target.hasProducts(products != null && !products.isEmpty());
		target.setUrl(categoryModelUrlResolver.resolve(source));
	}

	private CategoryData createTarget()
	{
		return new CategoryData();
	}

	protected Collection<CategoryModel> filterCategories(final Collection<CategoryModel> categories, final String catalogId)
	{
		final List<CategoryModel> result = new ArrayList<CategoryModel>();
		for (final CategoryModel categoryModel : categories)
		{
			if (toBeConverted(categoryModel, catalogId))
			{
				result.add(categoryModel);
			}
		}
		return result;
	}

	protected boolean toBeConverted(final CategoryModel categoryModel, final String catalogId)
	{
		return categoryModel.getCatalogVersion().getCatalog().getId().equals(catalogId)
				&& !(categoryModel instanceof ClassificationClassModel);
	}

	protected UrlResolver<CategoryModel> getCategoryModelUrlResolver()
	{
		return categoryModelUrlResolver;
	}

	@Required
	public void setCategoryModelUrlResolver(final UrlResolver<CategoryModel> categoryModelUrlResolver)
	{
		this.categoryModelUrlResolver = categoryModelUrlResolver;
	}

	protected I18NService getI18nService()
	{
		return i18nService;
	}

	@Required
	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
	}
}

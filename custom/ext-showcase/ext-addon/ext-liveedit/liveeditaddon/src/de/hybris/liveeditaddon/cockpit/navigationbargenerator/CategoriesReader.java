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
package de.hybris.liveeditaddon.cockpit.navigationbargenerator;

import de.hybris.liveeditaddon.cockpit.navigationbargenerator.data.CategoryData;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;

import java.util.Collection;
import java.util.Set;


/**
 * Created: Jun 13, 2012
 * 
 * 
 */
public interface CategoriesReader
{
	/**
	 * 
	 * @param catalogId
	 *           product catalog to get categories from
	 * @param catalogVersionName
	 *           product catalog's version
	 * @param level
	 *           level of subcategories
	 * @return CategoryData (fake root object) with all categories
	 */
	CategoryData findCategoriesForCatalog(String catalogId, String catalogVersionName, Integer level, boolean shouldHaveProducts,
                                          Set<String> localizations);

	/**
	 * 
	 * @param catalogId
	 *           product catalog to get categories from
	 * @param catalogVersionName
	 *           product catalog's version
	 * @param rootCategories
	 *           root categories used in navigation
	 * @param level
	 *           level of subcategories
	 * @return CategoryData (fake root object) with all categories
	 */
	CategoryData findCategoriesForCatalog(String catalogId, String catalogVersionName, String[] rootCategories, Integer level,
                                          boolean shouldHaveProducts, Set<String> localizations);

	/**
	 * 
	 * @param catalogId
	 *           product catalog to get categories from
	 * @param catalogVersionName
	 *           product catalog's version
	 * @param rootCategory
	 *           root category code
	 * @param level
	 *           level of subcategories
	 * @return CategoryData
	 */
	CategoryData findCategoriesForCategory(String catalogId, String catalogVersionName, String rootCategory, Integer level);

	/**
	 * 
	 * @param catalogVersionModel
	 * @return
	 */
	public Collection<CategoryModel> getRootCategories(final CatalogVersionModel catalogVersionModel);

	/**
	 * 
	 * @param catalogId
	 * @param catalogVersionName
	 * @return
	 */
	public Collection<CategoryModel> getRootCategories(final String catalogId, final String catalogVersionName);
}

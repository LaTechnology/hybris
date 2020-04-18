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
package de.hybris.liveeditaddon.admin.converters.populator;

import de.hybris.platform.acceleratorservices.urldecoder.FrontendUrlDecoder;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.liveeditaddon.admin.ActionMenuRequestData;

import org.springframework.beans.factory.annotation.Required;



/**
 * 
 */
public class CategoryAndProductPopulator implements Populator<ActionMenuRequestData, Object>
{

	private FrontendUrlDecoder<ProductModel> productUrlDecoder;
	private FrontendUrlDecoder<CategoryModel> categoryUrlDecoder;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final ActionMenuRequestData source, final Object target) throws ConversionException
	{
		if (source.getProductCode() == null)
		{
			final ProductModel decodedProduct = getProductUrlDecoder().decode(source.getUrl());
			if (decodedProduct != null)
			{
				source.setProductCode(decodedProduct.getCode());
			}
		}
		if (source.getProductCode() == null && source.getCategoryCode() == null)
		{
			final CategoryModel decodedCategory = getCategoryUrlDecoder().decode(source.getUrl());
			if (decodedCategory != null)
			{
				source.setCategoryCode(decodedCategory.getCode());
			}
		}

	}

	public FrontendUrlDecoder<ProductModel> getProductUrlDecoder()
	{
		return productUrlDecoder;
	}

	@Required
	public void setProductUrlDecoder(final FrontendUrlDecoder<ProductModel> productUrlDecoder)
	{
		this.productUrlDecoder = productUrlDecoder;
	}

	public FrontendUrlDecoder<CategoryModel> getCategoryUrlDecoder()
	{
		return categoryUrlDecoder;
	}

	@Required
	public void setCategoryUrlDecoder(final FrontendUrlDecoder<CategoryModel> categoryUrlDecoder)
	{
		this.categoryUrlDecoder = categoryUrlDecoder;
	}


}

package com.greenlee.core.interceptor;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.CollectionUtils;


/**
 * This class validates product reference added to cart entry model and ensures if it must be a warranty product
 * 
 * @author dipankan sahoo
 * 
 */
public class GreenleeWarrantyProductValidateInterceptor implements ValidateInterceptor<ProductReferenceModel>
{

	private static final String WARRANTY_CATEGORY = "warranty";
	private static final String CATALOG_ID = "greenleeProductCatalog";
	private static final String CATALOG_VERSION_NAME = "Staged";
	private static final String FOLLOWUP = "FOLLOWUP";
	private ModelService modelService;
	private CategoryService categoryService;
	private CatalogVersionService catalogVersionService;


	/**
	 * Validates whether the product reference of type 'FOLLOWUP' belongs to Warranty Category
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void onValidate(final ProductReferenceModel reference, final InterceptorContext ctx) throws InterceptorException
	{
		if (reference.getReferenceType().getCode().equals(FOLLOWUP) && reference.getTarget() != null
				&& (!CollectionUtils.isEmpty(reference.getTarget().getSupercategories()) && (!hasParentWarrantyCategory(reference))))
		{
			throw new InterceptorException("Product reference can not be created with target product [" + reference.getTarget()
					+ "] as the parent category is not warranty type");
		}

	}

	/**
	 * This method determines whether a target product from product reference has super category as Warranty
	 * 
	 * @param reference
	 * @return isWarrantyCategory
	 */
	private boolean hasParentWarrantyCategory(final ProductReferenceModel reference)
	{
		boolean isWarrantyCategory = false;
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, CATALOG_VERSION_NAME);

		final CategoryModel catModel = categoryService.getCategoryForCode(catalogVersion, WARRANTY_CATEGORY);

		for (final CategoryModel superCategory : reference.getTarget().getSupercategories())
		{
			if (catModel != null && StringUtils.equalsIgnoreCase(catModel.getCode(), superCategory.getCode()))
			{
				isWarrantyCategory = true;
				break;
			}
		}

		return isWarrantyCategory;
	}


	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the categoryService
	 */
	public CategoryService getCategoryService()
	{
		return categoryService;
	}

	/**
	 * @param categoryService
	 *           the categoryService to set
	 */
	@Required
	public void setCategoryService(final CategoryService categoryService)
	{
		this.categoryService = categoryService;
	}

	protected CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}
}

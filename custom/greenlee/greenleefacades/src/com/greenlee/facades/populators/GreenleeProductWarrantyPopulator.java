/**
 *
 */
package com.greenlee.facades.populators;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import com.greenlee.core.model.GreenleeProductModel;


/**
 * @author raja.santhanam
 *
 */
public class GreenleeProductWarrantyPopulator implements Populator<GreenleeProductModel, ProductData>
{

    public static final String WARRANTY_CATEGORY_CODE = "warranty";

    private CategoryService    categoryService;


    /* (non-Javadoc)
     * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final GreenleeProductModel source, final ProductData target) throws ConversionException
    {
        final CategoryModel catModel = categoryService.getCategoryForCode(WARRANTY_CATEGORY_CODE);
        if (source.getSupercategories().contains(catModel))
        {
            target.setWarrantyProduct(Boolean.TRUE);
        }
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
     *            the categoryService to set
     */
    public void setCategoryService(final CategoryService categoryService)
    {
        this.categoryService = categoryService;
    }

}

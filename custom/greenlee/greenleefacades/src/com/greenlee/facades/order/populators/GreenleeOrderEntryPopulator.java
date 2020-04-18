package com.greenlee.facades.order.populators;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.order.converters.populator.OrderEntryPopulator;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.i18n.I18NService;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


/**
 * Converter for converting order / cart entries
 */
public class GreenleeOrderEntryPopulator extends OrderEntryPopulator
{
    private static final String WARRANTY_CATEGORY = "warranty";
    private CategoryService     categoryService;
    private I18NService         i18NService;

    @Override
    public void populate(final AbstractOrderEntryModel source, final OrderEntryData target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");

        target.setIsWarrantyProduct(checkParentCategory(source));
        target.setWarrantySerialNumbers(source.getWarrantySerialNumbers(i18NService.getCurrentLocale()));

    }

    private boolean checkParentCategory(final AbstractOrderEntryModel entry)
    {
        final CategoryModel catModel = categoryService.getCategoryForCode(WARRANTY_CATEGORY);
        return entry.getProduct().getSupercategories().contains(catModel);
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
    @Required
    public void setCategoryService(final CategoryService categoryService)
    {
        this.categoryService = categoryService;
    }

    /**
     * @return the i18NService
     */
    public I18NService getI18NService()
    {
        return i18NService;
    }

    /**
     * @param i18nService
     *            the i18NService to set
     */
    public void setI18NService(final I18NService i18nService)
    {
        i18NService = i18nService;
    }
}

/**
 *
 */
package com.greenlee.facades.populators;

import de.hybris.platform.commercefacades.product.converters.populator.ProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.user.UserService;

import com.greenlee.core.model.GreenleeProductModel;


/**
 * @author aruna
 *
 */
public class GreenleeProductPopulator extends ProductPopulator
{
    private UserService userService;

    private I18NService i18NService;

    @Override
    public void populate(final ProductModel source, final ProductData target)
    {
        super.populate(source, target);
        if (source instanceof GreenleeProductModel)
        {
            final GreenleeProductModel product = (GreenleeProductModel) source;
            target.setB2bProduct(product.getB2bProduct());
            target.setB2cProduct(product.getB2cProduct());
            target.setB2eProduct(product.getB2eProduct());
            if (product.getCatalogNumber(i18NService.getCurrentLocale()) != null)
            {
                target.setCatalogNumber(product.getCatalogNumber(i18NService.getCurrentLocale()));
            }
            target.setSapEan(product.getSapEAN());
        }

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

    /**
     * @return the userService
     */
    public UserService getUserService()
    {
        return userService;
    }

    /**
     * @param userService
     *            the userService to set
     */
    public void setUserService(final UserService userService)
    {
        this.userService = userService;
    }


}

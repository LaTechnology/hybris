/**
 *
 */
package com.greenlee.facades.populators;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.converters.populator.SearchResultProductPopulator;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

import com.greenlee.core.enums.UserTypes;
import com.greenlee.core.model.GreenleeB2BCustomerModel;


/**
 * @author peter.asirvatham
 * 
 */
public class GreenleeSearchResultProductPopulator extends SearchResultProductPopulator
{
    private UserService userService;

    @Override
    public void populate(final SearchResultValueData source, final ProductData target)
    {
        target.setShowAddToCart(Boolean.FALSE);
        B2BUnitModel unit = null;
        final UserModel user = getUserService().getCurrentUser();
        if (user instanceof B2BCustomerModel)
        {
            unit = ((GreenleeB2BCustomerModel) user).getSessionB2BUnit();
        }
        final Boolean b2bProduct = getValue(source, "b2bProduct");
        final Boolean b2cProduct = getValue(source, "b2cProduct");
        final Boolean b2eProduct = getValue(source, "b2eProduct");

        target.setB2bProduct(b2bProduct);
        target.setB2cProduct(b2cProduct);
        target.setB2eProduct(b2eProduct);

        if (unit != null && unit.getUserType() != null)
        {

            if ((UserTypes.B2B.getCode().equals(unit.getUserType().getCode())) && b2bProduct != null && b2bProduct.booleanValue())
            {
                target.setShowAddToCart(Boolean.TRUE);
            }
            if ((UserTypes.B2E.getCode().equals(unit.getUserType().getCode())) && b2eProduct != null && b2eProduct.booleanValue())
            {
                target.setShowAddToCart(Boolean.TRUE);
            }
            if ((UserTypes.B2C.getCode().equals(unit.getUserType().getCode())) && b2cProduct != null && b2cProduct.booleanValue())
            {
                target.setShowAddToCart(Boolean.TRUE);
            }

            if ((UserTypes.B2C.getCode().equals(unit.getUserType().getCode()))
                    || (UserTypes.B2E.getCode().equals(unit.getUserType().getCode())))
            {

                if ((b2cProduct.booleanValue() || b2eProduct.booleanValue())
                        && this.<Double> getValue(source, "priceValue") != null)
                {
                    target.setShowPrice(true);
                }
                else
                {
                    target.setShowPrice(false);
                }
            }

            if (UserTypes.B2B.getCode().equals(unit.getUserType().getCode())
                    && this.<Double> getValue(source, "priceValue") != null)
            {
                target.setShowPrice(true);
            }

        }
        else
        {

            if (target.getB2cProduct() != null && target.getB2cProduct().booleanValue())
            {
                target.setShowAddToCart(Boolean.TRUE);
            }

            if (b2cProduct.booleanValue() && this.<Double> getValue(source, "priceValue") != null)
            {
                target.setShowPrice(true);
            }
            else
            {
                target.setShowPrice(false);
            }
        }

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

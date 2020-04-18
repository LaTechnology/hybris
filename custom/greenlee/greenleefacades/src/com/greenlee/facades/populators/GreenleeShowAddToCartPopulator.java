/**
 *
 */
package com.greenlee.facades.populators;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.user.UserService;

import com.greenlee.core.enums.UserTypes;
import com.greenlee.core.model.GreenleeB2BCustomerModel;
import com.greenlee.core.model.GreenleeProductModel;


/**
 * @author peter.asirvatham
 * 
 */
public class GreenleeShowAddToCartPopulator implements Populator<GreenleeProductModel, ProductData>
{

    private GreenleeAccountTypeProductPopulator greenleeAccountTypeProductPopulator;
    private UserService                         userService;

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

    @Override
    public void populate(final GreenleeProductModel greenleeProduct, final ProductData productData) throws ConversionException
    {
        getGreenleeAccountTypeProductPopulator().populate(greenleeProduct, productData);
        productData.setShowAddToCart(Boolean.FALSE);
        B2BUnitModel unit = null;
        final UserModel user = getUserService().getCurrentUser();
        if (user instanceof B2BCustomerModel)
        {
            unit = ((GreenleeB2BCustomerModel) user).getSessionB2BUnit();
        }
        if (unit != null && unit.getUserType() != null)
        {
            if ((UserTypes.B2B.getCode().equals(unit.getUserType().getCode())) && greenleeProduct.getB2bProduct() != null
                    && greenleeProduct.getB2bProduct().booleanValue())
            {
                productData.setShowAddToCart(Boolean.TRUE);
            }
            if ((UserTypes.B2E.getCode().equals(unit.getUserType().getCode())) && greenleeProduct.getB2eProduct() != null
                    && greenleeProduct.getB2eProduct().booleanValue())
            {
                productData.setShowAddToCart(Boolean.TRUE);
            }
            if ((UserTypes.B2C.getCode().equals(unit.getUserType().getCode())) && greenleeProduct.getB2cProduct() != null
                    && greenleeProduct.getB2cProduct().booleanValue())
            {
                productData.setShowAddToCart(Boolean.TRUE);
            }
        }
        else
        {
            if (productData.getB2cProduct() != null && productData.getB2cProduct().booleanValue())
            {
                productData.setShowAddToCart(Boolean.TRUE);

            }
        }

    }

    /**
     * @return the greenleeAccountTypeProductPopulator
     */
    public GreenleeAccountTypeProductPopulator getGreenleeAccountTypeProductPopulator()
    {
        return greenleeAccountTypeProductPopulator;
    }

    /**
     * @param greenleeAccountTypeProductPopulator
     *            the greenleeAccountTypeProductPopulator to set
     */
    public void setGreenleeAccountTypeProductPopulator(
            final GreenleeAccountTypeProductPopulator greenleeAccountTypeProductPopulator)
    {
        this.greenleeAccountTypeProductPopulator = greenleeAccountTypeProductPopulator;
    }


}

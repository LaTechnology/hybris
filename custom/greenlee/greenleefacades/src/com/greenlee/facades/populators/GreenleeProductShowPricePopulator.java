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
 *
 */
public class GreenleeProductShowPricePopulator implements Populator<GreenleeProductModel, ProductData>
{
    private UserService userService;

    /* (non-Javadoc)
     * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final GreenleeProductModel source, final ProductData target) throws ConversionException
    {
        final UserModel userModel = getUserService().getCurrentUser();
        B2BUnitModel unit = null;

        if (userModel instanceof B2BCustomerModel)
        {
            unit = ((GreenleeB2BCustomerModel) userModel).getSessionB2BUnit();
        }
        if (unit != null && unit.getUserType() != null)
        {

            if ((UserTypes.B2C.getCode().equals(unit.getUserType().getCode())))
            {
                if (source.getB2cProduct().booleanValue())
                {
                    target.setShowPrice(true);
                }
                else
                {
                    target.setShowPrice(false);
                }
            }
            else if ((UserTypes.B2E.getCode().equals(unit.getUserType().getCode())))
            {
                if (source.getB2eProduct().booleanValue())
                {
                    target.setShowPrice(true);
                }
                else
                {
                    target.setShowPrice(false);
                }
            }
            else if ((UserTypes.B2B.getCode().equals(unit.getUserType().getCode())))
            {
                target.setShowPrice(true);
            }
        }
        else
        {
            if (source.getB2cProduct().booleanValue())
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

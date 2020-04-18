/**
 *
 */
package com.greenlee.facades.populators;

import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import com.greenlee.core.model.GreenleeB2BCustomerModel;


/**
 *
 */
public class CustomerEmailPopulator implements Populator<CustomerModel, CustomerData>
{

    /* (non-Javadoc)
     * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final CustomerModel source, final CustomerData target) throws ConversionException
    {
        if (source instanceof GreenleeB2BCustomerModel)
        {
            final GreenleeB2BCustomerModel b2bCustomerModel = (GreenleeB2BCustomerModel) source;
            target.setEmail(b2bCustomerModel.getEmail());
        }
    }

}

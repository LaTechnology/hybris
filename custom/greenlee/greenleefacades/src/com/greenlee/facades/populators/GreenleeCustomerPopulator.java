/**
 *
 */
package com.greenlee.facades.populators;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.commercefacades.user.converters.populator.CustomerPopulator;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import com.greenlee.core.model.GreenleeB2BCustomerModel;


/**
 * @author raja.santhanam
 *
 */
public class GreenleeCustomerPopulator extends CustomerPopulator
{
    private Converter<B2BUnitModel, B2BUnitData> b2bUnitConverter;

    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final CustomerModel source, final CustomerData target)
    {
        super.populate(source, target);
        if (source instanceof GreenleeB2BCustomerModel)
        {
            final GreenleeB2BCustomerModel customer = (GreenleeB2BCustomerModel) source;
            final B2BUnitModel sessionB2BUnit = customer.getSessionB2BUnit();
            if (sessionB2BUnit != null)
            {
                final B2BUnitData b2bUnitData = b2bUnitConverter.convert(sessionB2BUnit);
                target.setSessionB2BUnit(b2bUnitData);
            }
            target.setSapConsumerID(customer.getSapConsumerID());
            target.setCompanyName(customer.getCompanyName());
            target.setAccountInformation(customer.getAccountInformation());

        }
    }

    /**
     * @return the b2bUnitConverter
     */
    public Converter<B2BUnitModel, B2BUnitData> getB2bUnitConverter()
    {
        return b2bUnitConverter;
    }

    /**
     * @param b2bUnitConverter
     *            the b2bUnitConverter to set
     */
    public void setB2bUnitConverter(final Converter<B2BUnitModel, B2BUnitData> b2bUnitConverter)
    {
        this.b2bUnitConverter = b2bUnitConverter;
    }

}

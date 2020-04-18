/**
 *
 */
package com.greenlee.facades.populators;

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * @author raja.santhanam
 *
 */
public class GreenleeRegionAsDistrictAddressPopulator implements Populator<AddressModel, AddressData>
{
    @Override
    public void populate(final AddressModel source, final AddressData target) throws ConversionException
    {
        target.setDistrict(source.getDistrict());
        target.setSelectedBillingAddressId(source.getSelectedBillingAddressId());
        if (source.getDefaultBillingAddress() != null)
        {
            target.setDefaultBillingAddress(source.getDefaultBillingAddress());
        }
        if (source.getDefaultShippingAddress() != null)
        {
            target.setDefaultShippingAddress(source.getDefaultShippingAddress());
        }
        if (source.getPrimaryAddress() != null)
        {
            target.setPrimaryAddress(source.getPrimaryAddress());
        }
        if (source.getLivingstonString() != null)
        {
            target.setLivingstonString(source.getLivingstonString());
        }
    }

}

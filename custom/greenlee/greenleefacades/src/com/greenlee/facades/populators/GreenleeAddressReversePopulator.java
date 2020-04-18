/**
 *
 */
package com.greenlee.facades.populators;

import de.hybris.platform.commercefacades.user.converters.populator.AddressReversePopulator;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * @author aruna
 *
 */
public class GreenleeAddressReversePopulator extends AddressReversePopulator
{

    @Override
    public void populate(final AddressData addressData, final AddressModel addressModel) throws ConversionException
    {
        //address model
        super.populate(addressData, addressModel);
        if (addressData.getDistrict() != null)
        {
            addressModel.setDistrict(addressData.getDistrict());
        }
        if (addressData.getSelectedBillingAddressId() != null)
        {
            addressModel.setSelectedBillingAddressId(addressData.getSelectedBillingAddressId());
        }
        if (addressData.getPrimaryAddress() != null)
        {
            addressModel.setPrimaryAddress(addressData.getPrimaryAddress());
        }
        if (addressData.getLivingstonString() != null)
        {
            addressModel.setLivingstonString(addressData.getLivingstonString());
        }
    }
}

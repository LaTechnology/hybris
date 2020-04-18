/**
 *
 */
package com.greenlee.facades.populators;

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.apache.commons.lang.StringUtils;


/**
 * @author raja.santhanam
 *
 */
public class GreenleeSapSourcedAddressPopulator implements Populator<AddressModel, AddressData>
{

    /* (non-Javadoc)
     * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final AddressModel source, final AddressData target) throws ConversionException
    {
        target.setIsSapSourcedAddress(StringUtils.isNotBlank(source.getSapCustomerID()));
        target.setSelectedBillingAddressId(source.getSelectedBillingAddressId());
        if (source.getLivingstonString() != null)
        {
            target.setLivingstonString(source.getLivingstonString());
        }
    }

}

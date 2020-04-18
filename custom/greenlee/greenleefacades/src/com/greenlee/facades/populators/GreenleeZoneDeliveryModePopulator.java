/**
 *
 */
package com.greenlee.facades.populators;

import de.hybris.platform.commercefacades.order.converters.populator.ZoneDeliveryModePopulator;
import de.hybris.platform.commercefacades.order.data.ZoneDeliveryModeData;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;


/**
 * @author raja.santhanam
 * 
 */
public class GreenleeZoneDeliveryModePopulator extends ZoneDeliveryModePopulator
{
    @Override
    public void populate(final ZoneDeliveryModeModel source, final ZoneDeliveryModeData target)
    {
        super.populate(source, target);
        target.setExpeditedShipping(source.getIsExpeditedShipping().booleanValue());
        target.setAccountNumberMaxLength(source.getAccountNumberMaxLength().intValue());
        target.setAcctNumberMandatory(source.getIsAcctNumberMandatory().booleanValue());
    }
}

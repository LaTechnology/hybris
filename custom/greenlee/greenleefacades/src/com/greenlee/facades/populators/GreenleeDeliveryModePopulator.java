/**
 *
 */
package com.greenlee.facades.populators;

import de.hybris.platform.commercefacades.order.converters.populator.AbstractDeliveryModePopulator;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;


/**
 * @author raja.santhanam
 * 
 */
public class GreenleeDeliveryModePopulator extends AbstractDeliveryModePopulator<ZoneDeliveryModeModel, DeliveryModeData>
{
    @Override
    public void populate(final ZoneDeliveryModeModel source, final DeliveryModeData target)
    {
        super.populate(source, target);
        target.setExpeditedShipping(source.getIsExpeditedShipping().booleanValue());
        target.setAccountNumberMaxLength(source.getAccountNumberMaxLength().intValue());
        target.setAcctNumberMandatory(source.getIsAcctNumberMandatory().booleanValue());
    }
}

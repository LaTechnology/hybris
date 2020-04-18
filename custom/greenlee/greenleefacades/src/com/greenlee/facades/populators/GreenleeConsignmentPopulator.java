/**
 *
 */
package com.greenlee.facades.populators;

import de.hybris.platform.commercefacades.order.converters.populator.ConsignmentPopulator;
import de.hybris.platform.commercefacades.order.data.ConsignmentData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

import java.math.BigDecimal;

import javax.annotation.Resource;


/**
 * @author Viji Shetty
 *
 *         Populated the totals required in the consignment for the delivery sent email.
 *
 */
public class GreenleeConsignmentPopulator extends ConsignmentPopulator
{
    @Resource(name = "priceDataFactory")
    private PriceDataFactory priceDataFactory;

    @Override
    public void populate(final ConsignmentModel source, final ConsignmentData target)
    {
        super.populate(source, target);
        if (source.getConsignmentValue() != null)
        {
            target.setSubTotal(createPrice(
                    source,
                    Double.valueOf(source.getConsignmentValue().doubleValue() - source.getDutyBreakUp().doubleValue()
                            - source.getFreightBreakUp().doubleValue() - source.getTaxBreakUp().doubleValue()
                            + source.getPromotionBreakUp().doubleValue())));
        }
        if (source.getFreightBreakUp() != null)
        {
            target.setDeliveryCost(createPrice(source, source.getFreightBreakUp()));
        }
        if (source.getTaxBreakUp() != null)
        {
            target.setTotalTax(createPrice(source, source.getTaxBreakUp()));
        }
        if (source.getPromotionBreakUp() != null)
        {
            target.setTotalDiscounts(createPrice(source, source.getPromotionBreakUp()));
        }
        if (source.getDutyBreakUp() != null)
        {
            target.setDuty(createPrice(source, source.getDutyBreakUp()));
        }
        if (source.getConsignmentValue() != null && source.getTaxBreakUp() != null)
        {
            /*source.getConsignmentValue().doubleValue() + source.getDutyBreakUp().doubleValue()
            + source.getFreightBreakUp().doubleValue() + source.getTaxBreakUp().doubleValue()
            - source.getPromotionBreakUp().doubleValue()*/
            target.setTotalPriceWithTax(createPrice(source, Double.valueOf(source.getConsignmentValue().doubleValue())));
        }
    }

    protected PriceData createPrice(final ConsignmentModel source, final Double val)
    {
        return priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(val.doubleValue()), source.getOrder().getCurrency());
    }
}

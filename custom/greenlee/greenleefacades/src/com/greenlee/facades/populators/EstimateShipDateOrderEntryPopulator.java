/**
 *
 */
package com.greenlee.facades.populators;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.i18n.I18NService;

import org.springframework.util.Assert;


/**
 * @author qili
 */
public class EstimateShipDateOrderEntryPopulator implements Populator<AbstractOrderEntryModel, OrderEntryData>
{

    private I18NService i18NService;

    /**
     * @return the i18NService
     */
    public I18NService getI18NService()
    {
        return i18NService;
    }

    /**
     * @param i18nService
     *            the i18NService to set
     */
    public void setI18NService(final I18NService i18nService)
    {
        i18NService = i18nService;
    }



    /* (non-Javadoc)
     * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final AbstractOrderEntryModel source, final OrderEntryData target) throws ConversionException
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        if (source.getEstimatedShipDate(i18NService.getCurrentLocale()) != null)
        {
            target.setEstimatedShipDate(source.getEstimatedShipDate(i18NService.getCurrentLocale()));
        }
    }

}

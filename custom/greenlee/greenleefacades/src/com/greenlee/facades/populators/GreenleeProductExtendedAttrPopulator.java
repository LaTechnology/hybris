/**
 *
 */
package com.greenlee.facades.populators;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;

import com.greenlee.core.model.GreenleeProductModel;


/**
 * @author raja.santhanam
 * 
 */
public class GreenleeProductExtendedAttrPopulator implements Populator<GreenleeProductModel, ProductData>
{

    /* (non-Javadoc)
     * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final GreenleeProductModel source, final ProductData target)
    {
        target.setUnspscNumber(source.getUnspscNumber());
        target.setLongUPC(source.getLongUPC());
        target.setSmallUPC(source.getSmallUPC());
        target.setCatalogNumber(source.getCatalogNumber());
        target.setSapSegmentID(source.getSapSegmentID());
        target.setSapEan(source.getSapEAN());
    }

}

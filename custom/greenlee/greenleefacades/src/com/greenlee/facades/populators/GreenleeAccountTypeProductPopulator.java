/**
 *
 */
package com.greenlee.facades.populators;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import com.greenlee.core.model.GreenleeProductModel;


/**
 * @author peter.asirvatham
 *
 */
public class GreenleeAccountTypeProductPopulator
{

    public void populate(final GreenleeProductModel greenleeProduct, final ProductData productData) throws ConversionException
    {
        productData.setWeightUnits(greenleeProduct.getWeightUnits());
        productData.setB2bProduct(greenleeProduct.getB2bProduct());
        productData.setB2cProduct(greenleeProduct.getB2cProduct());
        productData.setB2eProduct(greenleeProduct.getB2eProduct());
    }

}

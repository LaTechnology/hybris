/**
 * 
 */
package com.greenlee.facades.populators;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import com.greenlee.core.model.GreenleeProductModel;



/**
 * @author midhun.bose
 * 
 */
public class GreenleeProductVideolinkPopulator implements Populator<GreenleeProductModel, ProductData>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final GreenleeProductModel source, final ProductData target) throws ConversionException
	{
		target.setVideolink(source.getVideolink());
	}

}
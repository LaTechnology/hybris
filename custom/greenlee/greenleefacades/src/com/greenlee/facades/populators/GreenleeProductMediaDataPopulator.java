package com.greenlee.facades.populators;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;

import java.util.ArrayList;
import java.util.List;

import com.greenlee.core.model.GreenleeProductModel;
import com.greenlee.greenleecore.model.media.GreenleeProductMediaModel;
import com.greenlee.greenleefacades.greenleeproduct.data.GreenleeProductMediaData;


/**
 * @author xiaochen bian
 *
 */
public class GreenleeProductMediaDataPopulator implements Populator<GreenleeProductModel, ProductData>
{

    @Override
    public void populate(final GreenleeProductModel source, final ProductData target)
    {
        if (source.getProductArtifacts() != null)
        {
            final List productArtifacts = new ArrayList<GreenleeProductMediaData>();
            for (final GreenleeProductMediaModel greenleeProductMedia : source.getProductArtifacts())
            {
                final GreenleeProductMediaData greenleeProductMediaData = new GreenleeProductMediaData();
                greenleeProductMediaData.setUrl(greenleeProductMedia.getURL());
                greenleeProductMediaData.setAltText(greenleeProductMedia.getCode());
                greenleeProductMediaData.setGeneralPriority(greenleeProductMedia.getGeneralSortOrderPriority());
                productArtifacts.add(greenleeProductMediaData);
            }
            target.setProductArtifacts(productArtifacts);
        }
    }
}

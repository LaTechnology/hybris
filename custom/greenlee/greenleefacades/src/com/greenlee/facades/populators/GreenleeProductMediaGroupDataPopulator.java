/**
 *
 */
package com.greenlee.facades.populators;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.List;

import com.greenlee.core.model.GreenleeProductMediaGroupModel;
import com.greenlee.core.model.GreenleeProductModel;
import com.greenlee.greenleecore.model.media.GreenleeProductMediaModel;
import com.greenlee.greenleefacades.greenleeproduct.data.GreenleeProductMediaData;
import com.greenlee.greenleefacades.greenleeproduct.data.GreenleeProductMediaGroupData;


/**
 * @author qili
 */
public class GreenleeProductMediaGroupDataPopulator implements Populator<GreenleeProductModel, ProductData>
{

    /* (non-Javadoc)
     * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final GreenleeProductModel source, final ProductData target) throws ConversionException
    {
        if (source.getProductArtifactsGroups() != null)
        {
            final List<GreenleeProductMediaGroupData> mediaGroups = new ArrayList<>();
            for (final GreenleeProductMediaGroupModel mediaGroupModel : source.getProductArtifactsGroups())
            {
                final GreenleeProductMediaGroupData mediaGroup = new GreenleeProductMediaGroupData();
                mediaGroup.setGroupCode(mediaGroupModel.getGroupCode());
                mediaGroup.setGroupName(mediaGroupModel.getGroupName());

                final List productArtifacts = new ArrayList<GreenleeProductMediaData>();
                for (final GreenleeProductMediaModel mediaModel : mediaGroupModel.getProductMedia())
                {
                    final GreenleeProductMediaData mediaData = new GreenleeProductMediaData();
                    mediaData.setAltText(mediaModel.getCode());
                    mediaData.setGroupPriority(mediaModel.getGroupSortOrderPriority());
                    mediaData.setUrl(mediaModel.getURL());
                    productArtifacts.add(mediaData);
                }
                mediaGroup.setProductMedia(productArtifacts);
                mediaGroups.add(mediaGroup);
            }
            target.setProductArtifactsGroups(mediaGroups);
        }
    }

}

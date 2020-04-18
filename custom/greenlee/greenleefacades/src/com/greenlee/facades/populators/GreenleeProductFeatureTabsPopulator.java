/**
 *
 */
package com.greenlee.facades.populators;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.greenlee.core.model.GreenleeProductFeatureTabModel;
import com.greenlee.core.model.GreenleeProductModel;
import com.greenlee.facades.product.data.GreenleeProductFeatureTabData;





/**
 * @author kaushik.ganguly
 * 
 */
public class GreenleeProductFeatureTabsPopulator implements Populator<GreenleeProductModel, ProductData>
{


    private CommerceCommonI18NService i18Service;

    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final GreenleeProductModel source, final ProductData target) throws ConversionException
    {
        if (source.getProductFeatureTabs() != null)
        {
            final LanguageModel currentLanguage = i18Service.getCurrentLanguage();
            final Locale localeInSession = i18Service.getLocaleForLanguage(currentLanguage);
            final List featureTabs = new ArrayList<GreenleeProductFeatureTabData>();
            for (final GreenleeProductFeatureTabModel tab : source.getProductFeatureTabs())
            {
                final GreenleeProductFeatureTabData tabData = new GreenleeProductFeatureTabData();
                tabData.setName(tab.getTabName(localeInSession));
                tabData.setDescription(tab.getTabDescription(localeInSession));
                featureTabs.add(tabData);
            }
            target.setFeatureTabs(featureTabs);
            target.setFeatures(source.getProductFeatures(localeInSession));
        }
    }

    /**
     * @return the i18Service
     */
    public CommerceCommonI18NService getI18Service()
    {
        return i18Service;
    }

    /**
     * @param i18Service
     *            the i18Service to set
     */
    public void setI18Service(final CommerceCommonI18NService i18Service)
    {
        this.i18Service = i18Service;
    }




}

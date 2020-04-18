/**
 *
 */
package com.greenlee.facades.populators;

import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.converters.populator.SearchResultVariantProductPopulator;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * @author qili Populate showAddToCart and purchasable attributes to productData
 */
public class GreenleeSearchResultVariantProductPopulator extends SearchResultVariantProductPopulator
{
    private static final Logger LOG = Logger.getLogger(GreenleeSearchResultVariantProductPopulator.class);

    private ProductFacade       productFacade;

    /**
     * @return the productFacade
     */
    public ProductFacade getProductFacade()
    {
        return productFacade;
    }

    /**
     * @param productFacade
     *            the productFacade to set
     */
    public void setProductFacade(final ProductFacade productFacade)
    {
        this.productFacade = productFacade;
    }

    /* (non-Javadoc)
     * @see de.hybris.platform.commercefacades.search.converters.populator.SearchResultVariantProductPopulator#populate(de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData, de.hybris.platform.commercefacades.product.data.ProductData)
     */
    @Override
    public void populate(final SearchResultValueData source, final ProductData target)
    {
        super.populate(source, target);
        populateShowAddTocartAndPurchasable(target);

        target.setCatalogNumber(this.<String> getValue(source, "catalogNumber"));
    }

    protected void populateShowAddTocartAndPurchasable(final ProductData target)
    {
        try
        {
            final ProductModel productModel = getProductService().getProductForCode(target.getCode());
            if (productModel != null)
            {
                final List<ProductOption> options = new ArrayList<>(
                        Arrays.asList(ProductOption.BASIC, ProductOption.SHOWADDTOCART));
                final ProductData productData = productFacade.getProductForOptions(productModel, options);
                target.setShowAddToCart(productData.getShowAddToCart());
                target.setPurchasable(productData.getPurchasable());
            }
        }
        catch (final UnknownIdentifierException e)
        {
            LOG.error("Product with code " + target.getCode() + " not found! " + e);
        }
    }

}

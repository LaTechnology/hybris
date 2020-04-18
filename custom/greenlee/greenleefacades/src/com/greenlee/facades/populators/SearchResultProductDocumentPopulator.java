/**
 *
 */
package com.greenlee.facades.populators;

import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.util.Assert;

import com.greenlee.search.data.ProductDocumentData;


/**
 * @author aruna
 *
 */
public class SearchResultProductDocumentPopulator implements Populator<SearchResultValueData, ProductDocumentData>
{

    /**
     * 
     */
    private static final String CATALOG_VERSION = "catalogVersion";
    /**
     * 
     */
    private static final String ONLINE = "Online";
    /**
     * 
     */
    private static final String URL = "URL";
    /**
     * 
     */
    private static final String DOCUMENT_PRODUCT_REFERENCE = "documentProductReference";
    /**
     * 
     */
    private static final String DOCUMENT_SIZE = "documentSize";
    /**
     * 
     */
    private static final String DOCUMENT_MIME = "documentMime";
    /**
     * 
     */
    private static final String DOCUMENT_NAME = "documentName";
    /**
     * 
     */
    private static final String DOCUMENT_TYPE = "documentType";

    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final SearchResultValueData source, final ProductDocumentData target) throws ConversionException
    {
        // YTODO Auto-generated method stub
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        if (this.<String> getValue(source, CATALOG_VERSION).equalsIgnoreCase(ONLINE))
        {
            // Pull the values directly from the SearchResult object
            target.setURL(this.<String> getValue(source, URL));
            target.setDocumentType(this.<String> getValue(source, DOCUMENT_TYPE));
            target.setDocumentName(this.<String> getValue(source, DOCUMENT_NAME));
            target.setDocumentMime(this.<String> getValue(source, DOCUMENT_MIME));
            target.setDocumentSize(this.<String> getValue(source, DOCUMENT_SIZE));
            target.setDocumentProductReference(this.<String> getValue(source, DOCUMENT_PRODUCT_REFERENCE));
        }

    }

    protected <T> T getValue(final SearchResultValueData source, final String propertyName)
    {
        if (source.getValues() == null)
        {
            return null;
        }

        // DO NOT REMOVE the cast (T) below, while it should be unnecessary it is required by the javac compiler
        return (T) source.getValues().get(propertyName);
    }
}

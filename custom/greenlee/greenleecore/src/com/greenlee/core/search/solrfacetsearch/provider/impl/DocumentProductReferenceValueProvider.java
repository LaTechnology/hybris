/**
 *
 */
package com.greenlee.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.greenlee.core.model.GreenleeProductModel;
import com.greenlee.core.model.ProductDocumentModel;
import com.greenlee.greenleecore.model.media.GreenleeProductMediaModel;


/**
 * @author aruna
 *
 */
public class DocumentProductReferenceValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider
{
	private FieldNameProvider fieldNameProvider;

	/**
	 * @return the fieldNameProvider
	 */
	public FieldNameProvider getFieldNameProvider()
	{
		return fieldNameProvider;
	}

	/**
	 * @param fieldNameProvider
	 *           the fieldNameProvider to set
	 */
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.solrfacetsearch.provider.FieldValueProvider#getFieldValues(de.hybris.platform.solrfacetsearch
	 * .config.IndexConfig, de.hybris.platform.solrfacetsearch.config.IndexedProperty, java.lang.Object)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig paramIndexConfig, final IndexedProperty indexedProperty,
			final Object object) throws FieldValueProviderException
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		if (object instanceof ProductDocumentModel)
		{
			final GreenleeProductMediaModel productDocument = (GreenleeProductMediaModel) object;
			final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, null);
			for (final String fieldName : fieldNames)
			{
				fieldValues.addAll(getFieldValuesForDocument(fieldName, productDocument));
			}
		}
		// YTODO Auto-generated method stub
		return fieldValues;
	}

	private List<FieldValue> getFieldValuesForDocument(final String fieldName, final GreenleeProductMediaModel productDocument)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		for (final GreenleeProductModel product : productDocument.getGreenleeProduct())
		{
			new FieldValue(fieldName, product.getCode());
		}
		return fieldValues;
	}

}

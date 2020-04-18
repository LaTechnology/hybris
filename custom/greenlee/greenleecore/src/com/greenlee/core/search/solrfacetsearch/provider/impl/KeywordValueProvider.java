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

import com.greenlee.greenleecore.model.media.GreenleeProductMediaModel;


/**
 * @author aruna
 *
 */
public class KeywordValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider
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
	public Collection<FieldValue> getFieldValues(final IndexConfig arg0, final IndexedProperty indexedProperty,
			final Object object) throws FieldValueProviderException
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		if (object instanceof GreenleeProductMediaModel)
		{
			final GreenleeProductMediaModel productDocument = (GreenleeProductMediaModel) object;
			final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, null);
			for (final String fieldName : fieldNames)
			{
				fieldValues.add(new FieldValue(fieldName, productDocument.getKeyword()));
			}
		}
		// YTODO Auto-generated method stub
		return fieldValues;
	}


}

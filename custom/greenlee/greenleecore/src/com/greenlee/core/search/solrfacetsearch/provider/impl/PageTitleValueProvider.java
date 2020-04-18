/**
 *
 */
package com.greenlee.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * @author aruna
 *
 */
public class PageTitleValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{
	private FieldNameProvider fieldNameProvider;


	protected FieldNameProvider getFieldNameProvider()
	{
		return fieldNameProvider;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}


	@SuppressWarnings("deprecation")
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{

		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();

		if (model instanceof ContentPageModel)
		{
			final ContentPageModel contentModel = (ContentPageModel) model;
			String title = null;
			if (contentModel.getTitle() == null)
			{
				title = "Title not found";
			}
			else
			{
				title = contentModel.getTitle();
			}
			final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, null);
			for (final String fieldName : fieldNames)
			{
				fieldValues.add(new FieldValue(fieldName, title));
			}
			return fieldValues;
		}
		else
		{
			throw new FieldValueProviderException("Cannot get stock of non-product item");
		}
	}

}

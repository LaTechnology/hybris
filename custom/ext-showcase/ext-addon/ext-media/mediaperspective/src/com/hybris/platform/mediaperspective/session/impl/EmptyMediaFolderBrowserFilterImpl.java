package com.hybris.platform.mediaperspective.session.impl;

import java.util.ArrayList;
import java.util.List;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.impl.ItemAttributePropertyDescriptor;
import de.hybris.platform.cockpit.model.search.Operator;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.model.search.SearchParameterValue;
import de.hybris.platform.cockpit.services.search.impl.ItemAttributeSearchDescriptor;
import de.hybris.platform.cockpit.session.BrowserFilter;
import de.hybris.platform.cockpit.session.UISessionUtils;

class EmptyMediaFolderBrowserFilterImpl implements BrowserFilter
{

	private final String label;
	private final List<String> emptyFolderMediaCodes;

	EmptyMediaFolderBrowserFilterImpl(final String label, final List<String> emptyFolderMediaCodes)
	{
		this.label = label;
		this.emptyFolderMediaCodes = emptyFolderMediaCodes;
	}

	@Override
	public String getLabel()
	{
		return label;
	}

	@Override
	public boolean exclude(final Object item)
	{

		return false;
	}

	@Override
	public void filterQuery(final Query query)
	{
		final PropertyDescriptor propertyDescriptor = UISessionUtils.getCurrentSession().getTypeService()
				.getPropertyDescriptor("media.folder");
		final ItemAttributeSearchDescriptor searchDescriptor = new ItemAttributeSearchDescriptor(
				(ItemAttributePropertyDescriptor) propertyDescriptor);
		List<SearchParameterValue> searchParameterValues = new ArrayList<>();
		for (String code : emptyFolderMediaCodes) {
			searchParameterValues.add(new SearchParameterValue(
					searchDescriptor, code, Operator.EQUALS));
		}
		query.addParameterOrValues(searchParameterValues);
	}
}
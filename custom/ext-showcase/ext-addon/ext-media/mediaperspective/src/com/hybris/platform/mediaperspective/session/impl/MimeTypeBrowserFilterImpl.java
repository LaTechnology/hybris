package com.hybris.platform.mediaperspective.session.impl;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.impl.ItemAttributePropertyDescriptor;
import de.hybris.platform.cockpit.model.search.Operator;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.model.search.SearchParameterValue;
import de.hybris.platform.cockpit.services.search.impl.ItemAttributeSearchDescriptor;
import de.hybris.platform.cockpit.session.BrowserFilter;
import de.hybris.platform.cockpit.session.UISessionUtils;

class MimeTypeBrowserFilterImpl implements BrowserFilter
{

	private final String label;
	private final String identifier;

	MimeTypeBrowserFilterImpl(final String label, final String identifier)
	{
		this.label = label;
		this.identifier = identifier;
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
				.getPropertyDescriptor("abstractMedia.mime");
		final ItemAttributeSearchDescriptor searchDescriptor = new ItemAttributeSearchDescriptor(
				(ItemAttributePropertyDescriptor) propertyDescriptor);
		query.addParameterValue(new SearchParameterValue(searchDescriptor, identifier, Operator.EQUALS));
	}
}
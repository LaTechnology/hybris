package com.hybris.addon.common.converters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.util.Assert;

public final class ConfigurableConverters {
	
	private ConfigurableConverters()
	{
		// Private constructor to prevent instance creation
	}

	/**
	 * Convert all the items in the source list using a converter.
	 * 
	 * @param sourceList
	 *           The source list of objects to convert.
	 * @param converter
	 *           The converter to use to convert the source objects.
	 * @param <SOURCE>
	 *           The type of the source objects.
	 * @param <TARGET>
	 *           The type of the converted objects.
	 * @return The list of converted objects.
	 */
	public static <SOURCE, TARGET, OPTION> List<TARGET> convertAll(final Collection<? extends SOURCE> sourceList,
			final ConfigurableConverter<SOURCE, TARGET, OPTION> converter, Collection<OPTION> options)
	{
		Assert.notNull(converter);

		if (sourceList == null || sourceList.isEmpty())
		{
			return Collections.emptyList();
		}

		final List<TARGET> result = new ArrayList<TARGET>(sourceList.size());

		for (final SOURCE source : sourceList)
		{
			result.add(converter.convert(source, options));
		}

		return result;
	}
}

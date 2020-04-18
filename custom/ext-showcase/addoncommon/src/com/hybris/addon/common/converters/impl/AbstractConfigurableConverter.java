package com.hybris.addon.common.converters.impl;

import java.util.Collection;

import com.hybris.addon.common.converters.ConfigurableConverter;

import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

/**
 * @author dariusz.malachowski
 *
 * @param <SOURCE>
 * @param <TARGET>
 * @param <OPTION>
 */
public abstract class AbstractConfigurableConverter<SOURCE, TARGET, OPTION> implements ConfigurableConverter<SOURCE, TARGET, OPTION>, ConfigurablePopulator<SOURCE, TARGET, OPTION>
{

	@Override
	public TARGET convert(final SOURCE source) throws ConversionException
	{
		final TARGET target = createTarget();
		populate(source, target, null);
		return target;
	}

	@Override
	public TARGET convert(final SOURCE source, final TARGET prototype) throws ConversionException
	{
		populate(source, prototype, null);
		return prototype;
	}

	@Override
	public TARGET convert(final SOURCE source, final Collection<OPTION> options) throws ConversionException
	{
		final TARGET target = createTarget();
		populate(source, target, options);
		return target;
	}

	@Override
	public TARGET convert(final SOURCE source, final TARGET target, final Collection<OPTION> options) throws ConversionException
	{
		populate(source, target, options);
		return target;
	}

	protected abstract TARGET createTarget();
	
	@Override
	public abstract void populate(final SOURCE source, final TARGET target, Collection<OPTION> options);
	
}

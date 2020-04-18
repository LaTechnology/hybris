package com.hybris.addon.common.converters.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

/**
 * Adapts the regular Non-Configurable Populator in the Configurable Populator interface.
 */
public class ConfigurablePopulatorAdapter<SOURCE,TARGET,OPTION> implements ConfigurablePopulator<SOURCE,TARGET,OPTION>, Populator<SOURCE,TARGET> {

	private Populator<SOURCE,TARGET> populator;
	
	@Override
	public void populate(SOURCE source, TARGET target, Collection<OPTION> options) throws ConversionException {
		populate(source, target);
	}
	
	@Override
	public void populate(SOURCE source, TARGET target) throws ConversionException {
		getPopulator().populate(source, target);
	}
	
	@Required
	public void setPopulator(Populator<SOURCE, TARGET> populator)
	{
		this.populator = populator;
	}
	
	public Populator<SOURCE, TARGET> getPopulator()
	{
		return this.populator;
	}
	
}
package com.hybris.addon.common.converters;

import java.util.List;

import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;

/**
 * 
 * @author dariusz.malachowski
 *
 * @param <SOURCE>
 * @param <TARGET>
 * @param <OPTION>
 */
public interface ConfigurablePopulatorList<SOURCE, TARGET, OPTION>
{
	/**
	 * Get the list of populators.
	 * 
	 * @return the populators.
	 */
	List<ConfigurablePopulator<SOURCE, TARGET, OPTION>> getPopulators();

	/**
	 * Set the list of populators.
	 * 
	 * @param populators
	 *           the populators
	 */
	void setPopulators(List<ConfigurablePopulator<SOURCE, TARGET, OPTION>> populators);
}

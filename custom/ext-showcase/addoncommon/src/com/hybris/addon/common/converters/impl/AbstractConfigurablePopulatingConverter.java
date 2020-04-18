package com.hybris.addon.common.converters.impl;

import java.util.Collection;
import java.util.List;

import com.hybris.addon.common.converters.ConfigurablePopulatorList;
import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;

/**
 * 
 * @author dariusz.malachowski
 *
 * @param <SOURCE>
 * @param <TARGET>
 * @param <OPTION>
 */
public abstract class AbstractConfigurablePopulatingConverter<SOURCE, TARGET, OPTION> extends AbstractConfigurableConverter<SOURCE, TARGET, OPTION> implements ConfigurablePopulatorList<SOURCE, TARGET, OPTION>
{

	private List<ConfigurablePopulator<SOURCE, TARGET, OPTION>> populators;	

	@Override
	public List<ConfigurablePopulator<SOURCE, TARGET, OPTION>> getPopulators()
	{
		return populators;
	}

	@Override
	public void setPopulators(final List<ConfigurablePopulator<SOURCE, TARGET, OPTION>> populators)
	{
		this.populators = populators;		
	}
	
	@Override
	public void populate(final SOURCE source, final TARGET target, final Collection<OPTION> options)
	{
		final List<ConfigurablePopulator<SOURCE, TARGET, OPTION>> populatorList = getPopulators();		
		if(populatorList != null && !populatorList.isEmpty()) {
			
			for(final ConfigurablePopulator<SOURCE, TARGET, OPTION> populator : populatorList) {
				if (populator != null)
				{
					populator.populate(source, target, options);
				}
			}
		}		
	}

}

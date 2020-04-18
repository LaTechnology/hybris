/**
 * 
 */
package com.hybris.addon.common.converters;

import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Collection;


/**
 * @author dominik.strzyzewski
 * @param <SOURCE> 
 * @param <TARGET> 
 * @param <OPTION> 
 * 
 */
public interface ConfigurableConverter<SOURCE, TARGET, OPTION> extends Converter<SOURCE, TARGET>
{
	/**
	 * @param source
	 * @param options
	 * @return
	 * @throws ConversionException
	 */
	TARGET convert(SOURCE source, Collection<OPTION> options) throws ConversionException;

	/**
	 * @param source
	 * @param target
	 * @param options
	 * @return
	 * @throws ConversionException
	 */
	TARGET convert(SOURCE source, TARGET target, Collection<OPTION> options) throws ConversionException;
}

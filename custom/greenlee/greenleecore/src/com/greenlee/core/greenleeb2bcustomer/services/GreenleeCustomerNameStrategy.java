/**
 *
 */
package com.greenlee.core.greenleeb2bcustomer.services;

import de.hybris.platform.commerceservices.strategies.impl.DefaultCustomerNameStrategy;

import org.apache.commons.lang.StringUtils;


/**
 * @author peter.asirvatham
 *
 */
public class GreenleeCustomerNameStrategy extends DefaultCustomerNameStrategy
{
	private static final String SEPARATOR_SPACE = " ";

	@Override
	public String[] splitName(final String name)
	{
		final String trimmedName = StringUtils.trimToNull(name);
		if (trimmedName.indexOf(SEPARATOR_SPACE) > 0)
		{
			return new String[]
			{ trimmedName.substring(0, trimmedName.indexOf(SEPARATOR_SPACE)),
					trimmedName.substring(trimmedName.indexOf(SEPARATOR_SPACE) + 1) };
		}
		else
		{
			return new String[]
			{ name, "" };
		}
	}
}

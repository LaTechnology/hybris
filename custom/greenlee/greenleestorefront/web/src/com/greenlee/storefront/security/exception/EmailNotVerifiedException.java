/**
 *
 */
package com.greenlee.storefront.security.exception;

import org.springframework.security.authentication.DisabledException;


/**
 * @author dipankan
 *
 */
public class EmailNotVerifiedException extends DisabledException
{
	public EmailNotVerifiedException(final String msg)
	{
		super(msg);
	}

	public EmailNotVerifiedException(final String msg, final Throwable t)
	{
		super(msg, t);
	}
}

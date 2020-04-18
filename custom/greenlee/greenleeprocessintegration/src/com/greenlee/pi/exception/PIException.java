/**
 *
 */
package com.greenlee.pi.exception;

/**
 * @author peter.asirvatham
 * 
 */
public class PIException extends Exception
{
	public PIException()
	{
		//
	}

	public PIException(final String message)
	{
		super(message);
	}

	public PIException(final Throwable cause)
	{
		super(cause);
	}

	public PIException(final String message, final Throwable cause)
	{
		super(message, cause);
	}
}

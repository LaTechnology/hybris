/**
 *
 */
package com.greenlee.core.event;

import de.hybris.platform.commerceservices.event.ForgottenPwdEvent;


/**
 * @author kaushik
 *
 */
public class GreenleeForgottenPwdEvent extends ForgottenPwdEvent
{
	private Boolean userCreatedByAdmin;

	/**
	 *
	 */
	public GreenleeForgottenPwdEvent()
	{
		super();
		// YTODO Auto-generated constructor stub
	}

	public GreenleeForgottenPwdEvent(final Boolean userCreatedByAdmin, final String token)
	{
		super(token);
		this.userCreatedByAdmin = userCreatedByAdmin;
		// YTODO Auto-generated constructor stub
	}


	/**
	 * @return the userCreatedByAdmin
	 */
	public Boolean getUserCreatedByAdmin()
	{
		return userCreatedByAdmin;
	}

	/**
	 * @param userCreatedByAdmin
	 *           the userCreatedByAdmin to set
	 */
	public void setUserCreatedByAdmin(final Boolean userCreatedByAdmin)
	{
		this.userCreatedByAdmin = userCreatedByAdmin;
	}


}

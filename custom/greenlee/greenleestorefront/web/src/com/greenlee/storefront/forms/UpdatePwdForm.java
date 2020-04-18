/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.greenlee.storefront.forms;

/**
 * Form object for updating the password.
 */

public class UpdatePwdForm
{
	private String pwd;
	private String checkPwd;
	private String token;
	private boolean agreeToPrivacyPolicy;
	private boolean requestForInfo;
	private boolean updatedByAdmin;

	/**
	 * @return the agreeToPrivacyPolicy
	 */
	public boolean isAgreeToPrivacyPolicy()
	{
		return agreeToPrivacyPolicy;
	}

	/**
	 * @param agreeToPrivacyPolicy
	 *           the agreeToPrivacyPolicy to set
	 */
	public void setAgreeToPrivacyPolicy(final boolean agreeToPrivacyPolicy)
	{
		this.agreeToPrivacyPolicy = agreeToPrivacyPolicy;
	}

	/**
	 * @return the requestForInfo
	 */
	public boolean isRequestForInfo()
	{
		return requestForInfo;
	}

	/**
	 * @param requestForInfo
	 *           the requestForInfo to set
	 */
	public void setRequestForInfo(final boolean requestForInfo)
	{
		this.requestForInfo = requestForInfo;
	}

	/**
	 * @return the pwd
	 */
	public String getPwd()
	{
		return pwd;
	}

	/**
	 * @param pwd
	 *           the pwd to set
	 */
	public void setPwd(final String pwd)
	{
		this.pwd = pwd;
	}

	/**
	 * @return the checkPwd
	 */
	public String getCheckPwd()
	{
		return checkPwd;
	}

	/**
	 * @param checkPwd
	 *           the checkPwd to set
	 */
	public void setCheckPwd(final String checkPwd)
	{
		this.checkPwd = checkPwd;
	}

	/**
	 * @return the token
	 */
	public String getToken()
	{
		return token;
	}

	/**
	 * @param token
	 *           the token to set
	 */
	public void setToken(final String token)
	{
		this.token = token;
	}

	/**
	 * @return the updatedByAdmin
	 */
	public boolean isUpdatedByAdmin()
	{
		return updatedByAdmin;
	}

	/**
	 * @param updatedByAdmin
	 *           the updatedByAdmin to set
	 */
	public void setUpdatedByAdmin(final boolean updatedByAdmin)
	{
		this.updatedByAdmin = updatedByAdmin;
	}
}

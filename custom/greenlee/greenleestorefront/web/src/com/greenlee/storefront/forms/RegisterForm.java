/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hubris.
 *
 *
 */
package com.greenlee.storefront.forms;


/**
 * Form object for registration
 */
public class RegisterForm
{

	private String titleCode;
	private String firstName;
	private String lastName;
	private String email;
	private String pwd;
	private String checkPwd;
	private String captcha;
	private String mobileNumber;
	private String userType;
	private B2CUserForm b2cUser;

	/**
	 * @return the b2cUser
	 */
	public B2CUserForm getB2cUser()
	{
		return b2cUser;
	}

	/**
	 * @param b2cUser
	 *           the b2cUser to set
	 */
	public void setB2cUser(final B2CUserForm b2cUser)
	{
		this.b2cUser = b2cUser;
	}

	/**
	 * @return the userType
	 */
	public String getUserType()
	{
		return userType;
	}

	/**
	 * @param userType
	 *           the userType to set
	 */
	public void setUserType(final String userType)
	{
		this.userType = userType;
	}

	/**
	 * @return the titleCode
	 */
	public String getTitleCode()
	{
		return titleCode;
	}

	/**
	 * @param titleCode
	 *           the titleCode to set
	 */
	public void setTitleCode(final String titleCode)
	{
		this.titleCode = titleCode;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName()
	{
		return firstName;
	}

	/**
	 * @param firstName
	 *           the firstName to set
	 */
	public void setFirstName(final String firstName)
	{
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName()
	{
		return lastName;
	}

	/**
	 * @param lastName
	 *           the lastName to set
	 */
	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}

	/**
	 * @return the email
	 */
	public String getEmail()
	{
		return email;
	}

	/**
	 * @param email
	 *           the email to set
	 */
	public void setEmail(final String email)
	{
		this.email = email;
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

	public String getCaptcha()
	{
		return captcha;
	}

	public void setCaptcha(final String captcha)
	{
		this.captcha = captcha;
	}

	public String getMobileNumber()
	{
		return mobileNumber;
	}

	public void setMobileNumber(final String mobileNumber)
	{
		this.mobileNumber = mobileNumber;
	}
}

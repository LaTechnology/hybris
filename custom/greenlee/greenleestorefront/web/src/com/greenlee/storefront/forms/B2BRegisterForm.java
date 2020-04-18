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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * Form object for registration
 */
public class B2BRegisterForm
{

	private String titleCode;
	private String firstName;
	private String lastName;
	private String email;
	private String pwd;
	private String checkPwd;
	private String captcha;
	private String mobileNumber;
	private String userSelection;
	private String userType;
	private String password;
	private String addressLane1;
	private String addressLane2;
	private String city;
	private String state;
	private String zipCode;
	private String country;
	private String companyName;
	private String accountInformation;
	private String accountInformationNumber;
	private String enteredState;
	private Boolean hasExistingGreenleeAccount;
	private Boolean agreeToPrivacyPolicy;
	private Boolean requestForInfo;


	/**
	 * @return the hasExistingGreenleeAccount
	 */
	public Boolean getHasExistingGreenleeAccount()
	{
		return hasExistingGreenleeAccount;
	}

	/**
	 * @param hasExistingGreenleeAccount
	 *           the hasExistingGreenleeAccount to set
	 */
	public void setHasExistingGreenleeAccount(final Boolean hasExistingGreenleeAccount)
	{
		this.hasExistingGreenleeAccount = hasExistingGreenleeAccount;
	}

	/**
	 * @return the enteredState
	 */
	public String getEnteredState()
	{
		return enteredState;
	}

	/**
	 * @param enteredState
	 *           the enteredState to set
	 */
	public void setEnteredState(final String enteredState)
	{
		this.enteredState = enteredState;
	}

	/**
	 * @return the accountInformationNumber
	 */
	public String getAccountInformationNumber()
	{
		return accountInformationNumber;
	}

	/**
	 * @param accountInformationNumber
	 *           the accountInformationNumber to set
	 */
	public void setAccountInformationNumber(final String accountInformationNumber)
	{
		this.accountInformationNumber = accountInformationNumber;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName()
	{
		return companyName;
	}

	/**
	 * @param companyName
	 *           the companyName to set
	 */
	public void setCompanyName(final String companyName)
	{
		this.companyName = companyName;
	}


	/**
	 * @return the accountInformation
	 */
	public String getAccountInformation()
	{
		return accountInformation;
	}

	/**
	 * @param accountInformation
	 *           the accountInformation to set
	 */
	public void setAccountInformation(final String accountInformation)
	{
		this.accountInformation = accountInformation;
	}

	/**
	 * @return the userSelection
	 */
	public String getUserSelection()
	{
		return userSelection;
	}

	/**
	 * @param userSelection
	 *           the userSelection to set
	 */
	public void setUserSelection(final String userSelection)
	{
		this.userSelection = userSelection;
	}

	/**
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @param password
	 *           the password to set
	 */
	public void setPassword(final String password)
	{
		this.password = password;
	}

	/**
	 * @return the addressLane1
	 */
	@Size(min = 0, max = 60, message = "{address.streetname.length.validation}")
	public String getAddressLane1()
	{
		return addressLane1;
	}

	/**
	 * @param addressLane1
	 *           the addressLane1 to set
	 */
	public void setAddressLane1(final String addressLane1)
	{
		this.addressLane1 = addressLane1;
	}

	/**
	 * @return the addressLane2
	 */
	@Size(min = 0, max = 10, message = "{address.streetno.length.validation}")
	public String getAddressLane2()
	{
		return addressLane2;
	}

	/**
	 * @param addressLane2
	 *           the addressLane2 to set
	 */
	public void setAddressLane2(final String addressLane2)
	{
		this.addressLane2 = addressLane2;
	}

	/**
	 * @return the city
	 */
	@NotNull(message = "{address.city.length.empty}")
	@Size(min = 1, max = 40, message = "{address.city.length.validation}")
	public String getCity()
	{
		return city;
	}

	/**
	 * @param city
	 *           the city to set
	 */
	public void setCity(final String city)
	{
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState()
	{
		return state;
	}

	/**
	 * @param state
	 *           the state to set
	 */
	public void setState(final String state)
	{
		this.state = state;
	}

	/**
	 * @return the zipCode
	 */
	@NotNull(message = "{address.postal.length.validation}")
	@Size(min = 1, max = 10, message = "{address.postal.length.validation}")
	public String getZipCode()
	{
		return zipCode;
	}

	/**
	 * @param zipCode
	 *           the zipCode to set
	 */
	public void setZipCode(final String zipCode)
	{
		this.zipCode = zipCode;
	}

	/**
	 * @return the country
	 */
	public String getCountry()
	{
		return country;
	}

	/**
	 * @param country
	 *           the country to set
	 */
	public void setCountry(final String country)
	{
		this.country = country;
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
	@Size(min = 1, max = 40, message = "{address.firstname.length.validation}")
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
	@Size(min = 1, max = 40, message = "{address.lastname.length.validation}")
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

	public Boolean getAgreeToPrivacyPolicy()
	{
		return agreeToPrivacyPolicy;
	}

	public void setAgreeToPrivacyPolicy(final Boolean agreeToPrivacyPolicy)
	{
		this.agreeToPrivacyPolicy = agreeToPrivacyPolicy;
	}

	public Boolean getRequestForInfo()
	{
		return requestForInfo;
	}

	public void setRequestForInfo(final Boolean requestForInfo)
	{
		this.requestForInfo = requestForInfo;
	}
}

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
package com.greenlee.storefront.security.impl;

import de.hybris.platform.acceleratorstorefrontcommons.security.impl.DefaultBruteForceAttackCounter;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Default implementation of {@link BruteForceAttackCounter}
 */

/**
 * @author aruna
 *
 */
public class DefaultGreenLeeBruteForceAttackCounter extends DefaultBruteForceAttackCounter
{
	public DefaultGreenLeeBruteForceAttackCounter(final Integer maxFailedLogins, final Integer cacheExpiration,
			final Integer cacheSizeLimit)
	{
		super(maxFailedLogins, cacheExpiration, cacheSizeLimit);
	}


	private static final Logger LOG = Logger.getLogger(DefaultBruteForceAttackCounter.class);//NOPMD
	@Autowired private UserService userService;
	@Autowired private ModelService modelService;

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}


	@Override
	public void registerLoginFailure(final String userUid)
	{
		super.registerLoginFailure(userUid);
		UserModel userModel = getUserService().getUserForUID(userUid);
		if (!userModel.isLoginDisabled())
		{
			if (isAttack(userUid))
			{
				userModel.setLoginDisabled(true);
				getModelService().save(userModel);
				resetUserCounter(userUid);
			}
		}
	}




	@Override
	public int getUserFailedLogins(final String userUid)
	{
		if (StringUtils.isNotEmpty(userUid))
		{
			return get(prepareUserUid(userUid), Integer.valueOf(0)).getCounter();
		}
		else
		{
			return Integer.valueOf(0);
		}
	}


	protected String prepareUserUid(final String userUid)
	{
		return StringUtils.lowerCase(userUid);
	}


	/**
	 *
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}


}

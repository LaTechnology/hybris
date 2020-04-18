/**
 *
 */
package com.greenlee.core.unit.services.impl;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.List;

import com.greenlee.core.unit.dao.GreenleeUnitDao;
import com.greenlee.core.unit.services.GreenleeUnitService;


/**
 * Default implementation of GreenleeUnitService
 *
 * @author raja.santhanam
 *
 */
public class DefaultGreenleeUnitService implements GreenleeUnitService
{

	private SessionService sessionService;
	private UserService userService;
	private GreenleeUnitDao unitDao;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.greenlee.core.unit.services.GreenleeUnitService#getAllSecondaryB2BUnits()
	 */
	@Override
	public List<B2BUnitModel> getAllSecondaryB2BUnits()
	{
		final List<B2BUnitModel> allB2BUnits = getAllB2BUnits();

		final List<B2BUnitModel> filteredCollection = new ArrayList<>();
		for (final B2BUnitModel unit : allB2BUnits)
		{
			if (unit.getGroups() != null && !unit.getGroups().isEmpty())
			{
				filteredCollection.add(unit);
			}
		}
		return filteredCollection;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.greenlee.core.unit.services.GreenleeUnitService#getAllPrimaryB2BUnits()
	 */
	@Override
	public List<B2BUnitModel> getAllPrimaryB2BUnits()
	{
		final List<B2BUnitModel> allB2BUnits = getAllB2BUnits();
		final List<B2BUnitModel> filteredCollection = new ArrayList<>();
		for (final B2BUnitModel unit : allB2BUnits)
		{
			if (unit.getGroups() == null || unit.getGroups().isEmpty())
			{
				filteredCollection.add(unit);
			}
		}
		return filteredCollection;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.greenlee.core.unit.services.GreenleeUnitService#getAllB2BUnits()
	 */
	@Override
	public List<B2BUnitModel> getAllB2BUnits()
	{
		return unitDao.getAllB2BUnits();
	}

	/**
	 * @return the unitDao
	 */
	public GreenleeUnitDao getUnitDao()
	{
		return unitDao;
	}

	/**
	 * @param unitDao
	 *           the unitDao to set
	 */
	public void setUnitDao(final GreenleeUnitDao unitDao)
	{
		this.unitDao = unitDao;
	}

	/**
	 * @return the sessionService
	 */
	public SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

}


/**
 *
 */
package com.greenlee.core.servicelayer.interceptor;

import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.user.daos.UserGroupDao;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;

import com.greenlee.core.model.GreenleeB2BCustomerModel;


/**
 * @author aruna
 *
 */
public class GreenleeB2BCustomerPriceInterceptor implements PrepareInterceptor<GreenleeB2BCustomerModel>
{
	/**
	 *
	 */
	private static final String B2B = "b2b";


	private static final String CUSTOMER = "b2bcustomergroup";


	private UserGroupDao userGroupDao;


	/**
	 * @return the userGroupDao
	 */
	public UserGroupDao getUserGroupDao()
	{
		return userGroupDao;
	}


	/**
	 * @param userGroupDao
	 *           the userGroupDao to set
	 */
	@Required
	public void setUserGroupDao(final UserGroupDao userGroupDao)
	{
		this.userGroupDao = userGroupDao;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.interceptor.PrepareInterceptor#onPrepare(java.lang.Object,
	 * de.hybris.platform.servicelayer.interceptor.InterceptorContext)
	 */
	@Override
	public void onPrepare(final GreenleeB2BCustomerModel greenleeB2BCustomerModel, final InterceptorContext arg1)
			throws InterceptorException
	{

		final UserGroupModel hybrids = userGroupDao.findUserGroupByUid(CUSTOMER);
		if (B2B.equalsIgnoreCase(greenleeB2BCustomerModel.getUserType()))
		{
			if (!greenleeB2BCustomerModel.getGroups().contains(CUSTOMER))
			{
				final Set<PrincipalGroupModel> newGroups = new HashSet<PrincipalGroupModel>(greenleeB2BCustomerModel.getGroups());
				newGroups.add(hybrids);
				greenleeB2BCustomerModel.setGroups(newGroups);
			}
			else
			{
				if (greenleeB2BCustomerModel.getGroups().contains(hybrids))
				{
					final Set<PrincipalGroupModel> newGroups = new HashSet<PrincipalGroupModel>(greenleeB2BCustomerModel.getGroups());
					newGroups.remove(hybrids);
					greenleeB2BCustomerModel.setGroups(newGroups);
				}
			}
		}


	}

}

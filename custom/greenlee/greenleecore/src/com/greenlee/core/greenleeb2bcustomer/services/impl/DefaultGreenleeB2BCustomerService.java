package com.greenlee.core.greenleeb2bcustomer.services.impl;

import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;

import java.util.List;

import com.greenlee.core.greenleeb2bcustomer.dao.GreenleeB2BCustomerDao;
import com.greenlee.core.greenleeb2bcustomer.services.GreenleeB2BCustomerService;
import com.greenlee.core.model.GreenleeB2BCustomerModel;


/**
 * @author xiaochen bian
 *
 */
public class DefaultGreenleeB2BCustomerService extends AbstractBusinessService implements GreenleeB2BCustomerService
{
	private GreenleeB2BCustomerDao greenleeB2BCustomerDao;

	public GreenleeB2BCustomerDao getGreenleeB2BCustomerDao()
	{
		return greenleeB2BCustomerDao;
	}

	public void setGreenleeB2BCustomerDao(final GreenleeB2BCustomerDao greenleeB2BCustomerDao)
	{
		this.greenleeB2BCustomerDao = greenleeB2BCustomerDao;
	}

	@Override
	public List<GreenleeB2BCustomerModel> getGreenleeB2BCustomersById(final String id)
	{
		return getGreenleeB2BCustomerDao().findGreenleeB2BCustomerById(id);
	}

}

package com.greenlee.core.greenleeb2bcustomer.dao;

import de.hybris.platform.servicelayer.internal.dao.Dao;

import java.util.List;

import com.greenlee.core.model.GreenleeB2BCustomerModel;


/**
 * @author xiaochen bian
 *
 */
public interface GreenleeB2BCustomerDao extends Dao
{
	List<GreenleeB2BCustomerModel> findGreenleeB2BCustomerById(String id);
}

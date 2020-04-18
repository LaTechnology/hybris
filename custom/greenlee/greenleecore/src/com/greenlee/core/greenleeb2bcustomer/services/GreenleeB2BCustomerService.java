package com.greenlee.core.greenleeb2bcustomer.services;

import java.util.List;

import com.greenlee.core.model.GreenleeB2BCustomerModel;


/**
 * @author xiaochen bian
 *
 */
public interface GreenleeB2BCustomerService
{
	List<GreenleeB2BCustomerModel> getGreenleeB2BCustomersById(String id);
}

/**
 *
 */
package com.greenlee.core.salesforce.services;

import com.greenlee.core.model.GreenleeB2BCustomerModel;


/**
 * @author xiaochen bian
 *
 */
public interface SalesforceService
{
	void updateSapIdToSalesforce(GreenleeB2BCustomerModel customer);

	void postUserToSaleforce(GreenleeB2BCustomerModel customer);

}

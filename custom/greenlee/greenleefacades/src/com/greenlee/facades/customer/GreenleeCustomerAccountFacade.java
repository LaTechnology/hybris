/**
 *
 */
package com.greenlee.facades.customer;

import de.hybris.platform.commerceservices.customer.DuplicateUidException;

import com.greenlee.core.model.GreenleeB2BCustomerModel;
import com.greenlee.facades.customer.data.B2CRegisterData;


/**
 * @author aruna
 *
 */
public interface GreenleeCustomerAccountFacade
{

    GreenleeB2BCustomerModel saveCustomer(final B2CRegisterData registerData) throws DuplicateUidException;

    void logRegistrationData(B2CRegisterData registerData);
}

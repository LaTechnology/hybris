package com.greenlee.facades.greenleeb2bcustomer.facades.impl;

import de.hybris.platform.b2bacceleratorfacades.customer.exception.InvalidPasswordException;
import de.hybris.platform.b2bacceleratorfacades.customer.impl.DefaultB2BCustomerFacade;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;

import com.greenlee.core.customer.GreenleeCustomerAccountService;


/**
 * @author dipankan
 *
 *         B2B implementation for CustomerFacade.
 */
public class GreenleeB2BCustomerFacade extends DefaultB2BCustomerFacade
{
    private GreenleeCustomerAccountService greenleeCustomerAccountService;

    @Override
    public void updatePassword(final String token, final String newPassword) throws TokenInvalidatedException,
            InvalidPasswordException
    {
        validatePassword(newPassword);
        //        super.updatePassword(token, newPassword);

        getGreenleeCustomerAccountService().updatePassword(token, newPassword);
    }


    /**
     * @return the greenleeCustomerAccountService
     */
    public GreenleeCustomerAccountService getGreenleeCustomerAccountService()
    {
        return greenleeCustomerAccountService;
    }


    /**
     * @param greenleeCustomerAccountService
     *            the greenleeCustomerAccountService to set
     */
    public void setGreenleeCustomerAccountService(final GreenleeCustomerAccountService greenleeCustomerAccountService)
    {
        this.greenleeCustomerAccountService = greenleeCustomerAccountService;
    }



}

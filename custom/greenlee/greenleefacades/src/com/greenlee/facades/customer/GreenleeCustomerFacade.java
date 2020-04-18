/**
 *
 */
package com.greenlee.facades.customer;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;


/**
 * Facade to manipulate the current B2BUnit for a given user.
 *
 * @author raja.santhanam
 *
 */
public interface GreenleeCustomerFacade extends CustomerFacade
{
    /**
     * Updates the selected B2BUnit for the given user as the users sessionB2BUnit.
     *
     * @param useruid
     * @param b2bUnit
     */
    public B2BUnitData updateSelectedB2BUnit(String useruid, String b2bUnit);

    /**
     * Checks whether the customer account has been email verified
     *
     * @param token
     */
    public void verifyUserEmailAddress(String token) throws TokenInvalidatedException;

    /**
     * @see GRE-2153.
     *
     *      Privacy Policy and Sales and Marketing Checkbox.
     *
     * @param privacy
     *            the privacy to set
     * @param requestInfo
     *            the requestInfo to set
     */
    public void updateUserDetailsToSaleforceByPostCall(final B2BCustomerModel customer);
}

package com.greenlee.core.customer;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.core.model.user.CustomerModel;

import com.greenlee.facades.customer.data.B2CRegisterData;


/**
 *
 */

/**
 * @author aruna
 *
 */
public interface GreenleeCustomerAccountService
{
	/**
	 * @param customerModel
	 * @param registerData
	 * @throws DuplicateUidException
	 */
	void saveB2BUser(B2BCustomerModel customerModel, B2CRegisterData registerData) throws DuplicateUidException;

	/**
	 * Update the password for the user by decrypting and validating the token.
	 *
	 * @param token
	 *           the token to identify the the customer to reset the password for.
	 * @param newPassword
	 *           the new password to set
	 * @throws IllegalArgumentException
	 *            If the new password is empty or the token is invalid or expired
	 * @throws TokenInvalidatedException
	 *            if the token was already used or there is a newer token
	 */
	void updatePassword(String token, String newPassword) throws TokenInvalidatedException;

	/**
	 * @see GRE-2153.
	 *
	 *      Privacy Policy and Sales and Marketing Checkbox.
	 *
	 * @param privacy
	 *           the privacy to set
	 * @param requestInfo
	 *           the requestInfo to set
	 */
	public void updatePrivacyMarketingInfo(final CustomerModel customer, final boolean privacy, final boolean requestInfo);

}

/**
 *
 */
package com.greenlee.core.customer.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commerceservices.security.SecureToken;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sap.hybris.sapcustomerb2c.outbound.DefaultB2CSapCustomerAccountService;


/**
 * @author kaushik
 *
 */
public class GreenleeDefaultCustomerAccountService extends DefaultB2CSapCustomerAccountService
{
	@Override
	public void forgottenPassword(final CustomerModel customerModel)
	{
		validateParameterNotNullStandardMessage("customerModel", customerModel);
		final long timeStamp = getTokenValiditySeconds() > 0L ? new Date().getTime() : 0L;
		final SecureToken data = new SecureToken(customerModel.getUid(), timeStamp);
		final String token = getSecureTokenService().encryptData(data);
		customerModel.setToken(token);
		getModelService().save(customerModel);

	}

	@Override
	public List<AddressModel> getAddressBookDeliveryEntries(final CustomerModel customerModel)
	{
		validateParameterNotNull(customerModel, "Customer model cannot be null");
		final List<AddressModel> addressModels = new ArrayList<AddressModel>();

		for (final AddressModel address : customerModel.getAddresses())
		{
			if ((Boolean.TRUE.equals(address.getShippingAddress()) || Boolean.TRUE.equals(address.getBillingAddress()))
					&& Boolean.TRUE.equals(address.getVisibleInAddressBook()))
			{
				addressModels.add(address);
			}
		}

		return addressModels;
	}

}

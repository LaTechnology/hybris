/**
 *
 */
package com.greenlee.core.livingston.services;

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;


/**
 * @author xiaochen bian
 *
 */
public interface LivingstonService
{

	String getAddressScreenMatchCode(final CustomerData customer, final AddressData address);

	boolean isEligibleForScreen();
}

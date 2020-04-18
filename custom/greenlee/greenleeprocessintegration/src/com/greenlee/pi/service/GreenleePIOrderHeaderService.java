/**
 *
 */
package com.greenlee.pi.service;

import de.hybris.platform.commercefacades.user.data.CustomerData;

import java.util.List;

import com.greenlee.orderhistory.data.PIHeaderDetails;
import com.greenlee.pi.exception.PIException;


/**
 * @author peter.asirvatham
 *
 */
public interface GreenleePIOrderHeaderService
{
	List<PIHeaderDetails> getPIOrderHeaderDetails(CustomerData customerData) throws PIException;
}

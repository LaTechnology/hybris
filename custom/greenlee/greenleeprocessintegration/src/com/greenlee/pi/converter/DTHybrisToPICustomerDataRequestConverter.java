/**
 *
 */
package com.greenlee.pi.converter;

import de.hybris.platform.converters.impl.AbstractConverter;

import com.greenlee.pi.data.PIAddress;
import com.greenlee.pi.data.PICustomerData;
import com.hybris.urn.xi.ecc_real_time_customer_creation.DTHybrisToPICustomerDataRequest;
import com.hybris.urn.xi.ecc_real_time_customer_creation.DTHybrisToPICustomerDataRequest.BillingAddress;
import com.hybris.urn.xi.ecc_real_time_customer_creation.DTHybrisToPICustomerDataRequest.ShippingAddress;


/**
 * @author peter.asirvatham
 *
 */
public class DTHybrisToPICustomerDataRequestConverter extends AbstractConverter<PICustomerData, DTHybrisToPICustomerDataRequest>
{

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.converters.impl.AbstractConverter#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final PICustomerData customerData, final DTHybrisToPICustomerDataRequest request)
	{
		request.setCustomerName(customerData.getCustomerName());
		request.setEmailId(customerData.getEmailId());
		request.setBaseReference(customerData.getBaseReference());
		if (customerData.getBillingAddress() != null)
		{
			request.setBillingAddress(populateBillingAddress(customerData.getBillingAddress()));
		}
		if (customerData.getShippingAddress() != null)
		{
			request.setShippingAddress(populateShippingAddress(customerData.getShippingAddress()));
		}
	}

	public BillingAddress populateBillingAddress(final PIAddress piAddress)
	{
		final BillingAddress address = new BillingAddress();
		address.setStreetName(piAddress.getStreetName());
		address.setStreetNumber(piAddress.getStreetNumber());
		address.setPostalCode(piAddress.getPostalCode());
		address.setTown(piAddress.getTown());
		address.setCountry(piAddress.getCountry());
		address.setGender(piAddress.getGender());
		address.setDOB(piAddress.getDob());
		address.setCompany(piAddress.getCompany());
		address.setPOBox(piAddress.getPoBox());
		address.setPhone1(piAddress.getPhone1());
		address.setPhone2(piAddress.getPhone2());
		address.setMobilePhone(piAddress.getMobilePhone());
		address.setFax(piAddress.getFax());
		address.setEmail(piAddress.getEmail());
		address.setTitle(piAddress.getTitle());
		address.setFirstName(piAddress.getFirstName());
		address.setMiddleName(piAddress.getMiddleName());
		address.setMiddleName2(piAddress.getMiddleName2());
		address.setLastName(piAddress.getLastName());
		address.setDepartment(piAddress.getDepartment());
		address.setBuilding(piAddress.getBuilding());
		address.setApartment(piAddress.getApartment());
		address.setRegion(piAddress.getRegion());
		address.setDistrict(piAddress.getDistrict());
		return address;
	}

	public ShippingAddress populateShippingAddress(final PIAddress piAddress)
	{
		final ShippingAddress address = new ShippingAddress();
		address.setStreetName(piAddress.getStreetName());
		address.setStreetNumber(piAddress.getStreetNumber());
		address.setPostalCode(piAddress.getPostalCode());
		address.setTown(piAddress.getTown());
		address.setCountry(piAddress.getCountry());
		address.setGender(piAddress.getGender());
		address.setDOB(piAddress.getDob());
		address.setCompany(piAddress.getCompany());
		address.setPOBox(piAddress.getPoBox());
		address.setPhone1(piAddress.getPhone1());
		address.setPhone2(piAddress.getPhone2());
		address.setMobilePhone(piAddress.getMobilePhone());
		address.setFax(piAddress.getFax());
		address.setEmail(piAddress.getEmail());
		address.setTitle(piAddress.getTitle());
		address.setFirstName(piAddress.getFirstName());
		address.setMiddleName(piAddress.getMiddleName());
		address.setMiddleName2(piAddress.getMiddleName2());
		address.setLastName(piAddress.getLastName());
		address.setDepartment(piAddress.getDepartment());
		address.setBuilding(piAddress.getBuilding());
		address.setApartment(piAddress.getApartment());
		address.setRegion(piAddress.getRegion());
		address.setDistrict(piAddress.getDistrict());
		return address;
	}
}

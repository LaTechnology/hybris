/**
 *
 */
package com.greenlee.pi.converter;

import de.hybris.platform.converters.impl.AbstractConverter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.sap.sapmodel.model.SAPDeliveryModeModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.greenlee.core.model.GreenleeB2BCustomerModel;
import com.greenlee.core.util.GreenleeUtils;
import com.greenlee.pi.builder.impl.DefaultGreenleePIRequestBuilder;
import com.hybris.urn.xi.pricing_enquiry.DTHybrisPricingRequest;
import com.hybris.urn.xi.pricing_enquiry.DTHybrisPricingRequest.DROPSHIPADDRESS;
import com.hybris.urn.xi.pricing_enquiry.DTHybrisPricingRequest.ORDERHEADER;
import com.hybris.urn.xi.pricing_enquiry.DTHybrisPricingRequest.ORDERITEMS;


/**
 *
 */
public class DTHybrisPricingRequestConverter extends AbstractConverter<AbstractOrderModel, DTHybrisPricingRequest>
{
	private UserService userService;
	public static final String DOCTYPE = "TA";
	private static final Logger LOG = Logger.getLogger(DTHybrisPricingRequestConverter.class);

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}


	private String orderWeight;

	/**
	 * @return the orderWeight
	 */
	public String getOrderWeight()
	{
		return orderWeight;
	}

	/**
	 * @param orderWeight
	 *           the orderWeight to set
	 */
	public void setOrderWeight(final String orderWeight)
	{
		this.orderWeight = orderWeight;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.impl.AbstractConverter#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final AbstractOrderModel orderModel, final DTHybrisPricingRequest request)
	{
		request.setORDERHEADER(getORDERHEADER(orderModel, request));
		request.setORDERITEMS(getORDERITEMS(orderModel, request));
		if (orderModel.getDeliveryAddress() != null && StringUtils.isBlank(orderModel.getDeliveryAddress().getSapCustomerID()))
		{
			request.setDROPSHIPADDRESS(getDROPSHIPADDRESS(orderModel, request));
		}
	}

	protected ORDERHEADER getORDERHEADER(final AbstractOrderModel orderModel, final DTHybrisPricingRequest request)
	{
		ORDERHEADER orderHeader;
		if (request.getORDERHEADER() == null)
		{
			orderHeader = new ORDERHEADER();
		}
		else
		{
			orderHeader = request.getORDERHEADER();
		}
		final GreenleeB2BCustomerModel customer = (GreenleeB2BCustomerModel) orderModel.getUser();
		if (GreenleeUtils.isUserB2COrB2E(customer))
		{
			customer.setSessionB2BUnit(GreenleeUtils.getB2BUnitForB2COrB2EUser(customer));
		}
		orderHeader.setCUSTOMER(GreenleeUtils.getAccNoFromB2BUnit(customer.getSessionB2BUnit()));
		final String salesOrg = GreenleeUtils.getSalesOrgFromB2BUnit(customer.getSessionB2BUnit());
		orderHeader.setDOCTYPE(GreenleeUtils.getSAPOrderType(salesOrg));
		orderHeader.setSALESORG(salesOrg);

		if (StringUtils.isNotBlank(orderModel.getShipmentAccountNumber()))
		{
			orderHeader.setINCOTERMS2(orderModel.getShipmentAccountNumber());
			orderHeader.setINCOTERMS1(Config.getString("greenleeorderexchange.incoterms.code", "ZX2"));
			LOG.info("INCOTERMS2 "+orderHeader.getINCOTERMS2() +"INCOTERMS2  "+orderHeader.getINCOTERMS1() );
		}

		if (orderModel.getDeliveryMode() != null)
		{
			final ZoneDeliveryModeModel deliveryMode = (ZoneDeliveryModeModel) orderModel.getDeliveryMode();
			for (final SAPDeliveryModeModel sapDeliveryMode : orderModel.getStore().getSAPConfiguration().getSapDeliveryModes())
			{
				if (StringUtils.equalsIgnoreCase(sapDeliveryMode.getDeliveryMode().getCode(), deliveryMode.getCode()))
				{
					orderHeader.setSHIPCOND(sapDeliveryMode.getDeliveryValue());
				}
				LOG.info("setSHIPCOND  "+orderHeader.getSHIPCOND() );
			}
			orderHeader.setROUTE(deliveryMode.getCode());
			LOG.info("setROUTE  "+orderHeader.getROUTE() );
		}
		return orderHeader;
	}

	protected ORDERITEMS getORDERITEMS(final AbstractOrderModel orderModel, final DTHybrisPricingRequest request)
	{
		final ORDERITEMS orderItems;
		if (request.getORDERITEMS() == null)
		{
			orderItems = new ORDERITEMS();
		}
		else
		{
			orderItems = request.getORDERITEMS();
		}
		final List<AbstractOrderEntryModel> entries = orderModel.getEntries();
		for (final AbstractOrderEntryModel entry : entries)
		{
			final DTHybrisPricingRequest.ORDERITEMS.ITEMS item = new DTHybrisPricingRequest.ORDERITEMS.ITEMS();
			item.setMATERIAL(entry.getProduct().getCode());
			item.setREFITEM(String.valueOf(entry.getEntryNumber()));
			item.setREQQTY(String.valueOf(entry.getQuantity()));
			orderItems.getITEMS().add(item);
		}
		return orderItems;
	}


	protected DROPSHIPADDRESS getDROPSHIPADDRESS(final AbstractOrderModel orderModel, final DTHybrisPricingRequest request)
	{
		DROPSHIPADDRESS dropshipAddress;
		if (request.getDROPSHIPADDRESS() == null)
		{
			dropshipAddress = new DROPSHIPADDRESS();
		}
		else
		{
			dropshipAddress = request.getDROPSHIPADDRESS();
		}
		dropshipAddress.setCITY(orderModel.getDeliveryAddress().getTown());
		final GreenleeB2BCustomerModel customer = (GreenleeB2BCustomerModel) orderModel.getUser();
		dropshipAddress.setCOMPANYNAME(customer.getSessionB2BUnit().getUid());
		dropshipAddress.setCOUNTRY(orderModel.getDeliveryAddress().getCountry().getIsocode());
		dropshipAddress.setNAME1(orderModel.getDeliveryAddress().getFirstname());
		dropshipAddress.setNAME2(orderModel.getDeliveryAddress().getLastname());
		dropshipAddress.setPOBXPCD(orderModel.getDeliveryAddress().getPostalcode());

		if (orderModel.getDeliveryAddress().getRegion() != null)
		{
			dropshipAddress.setREGION(orderModel.getDeliveryAddress().getRegion().getIsocodeShort());
		}
		else
		{
			dropshipAddress.setREGION(orderModel.getDeliveryAddress().getDistrict());
		}

		dropshipAddress.setSTREET(orderModel.getDeliveryAddress().getStreetname());
		dropshipAddress.setSTREET2(orderModel.getDeliveryAddress().getAddressLine2());
		return dropshipAddress;
	}
}

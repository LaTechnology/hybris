package com.greenlee.sapintegration.orderexchange.outbound.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greenlee.core.model.GreenleeB2BCustomerModel;
import com.greenlee.sapintegration.orderexchange.constants.GreenleeOrderCsvColumns;
import com.greenlee.sapintegration.orderexchange.constants.GreenleePartnerCsvColumns;

import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.sap.orderexchange.constants.OrderCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.PartnerCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.PartnerRoles;
import de.hybris.platform.sap.orderexchange.constants.SaporderexchangeConstants;
import de.hybris.platform.sap.orderexchangeb2b.outbound.impl.DefaultB2BPartnerContributor;

public class DefaultGreenleeB2BPartnerContributor extends
		DefaultB2BPartnerContributor {
	private Logger logger = LoggerFactory.getLogger(DefaultGreenleeB2BPartnerContributor.class);

	@Override
	public java.util.Set<String> getColumns() {

		return new HashSet<>(Arrays.asList(OrderCsvColumns.ORDER_ID,
				PartnerCsvColumns.PARTNER_ROLE_CODE,
				PartnerCsvColumns.PARTNER_CODE,
				PartnerCsvColumns.DOCUMENT_ADDRESS_ID,
				PartnerCsvColumns.FIRST_NAME, PartnerCsvColumns.LAST_NAME,
				PartnerCsvColumns.STREET, PartnerCsvColumns.CITY,
				PartnerCsvColumns.TEL_NUMBER, PartnerCsvColumns.HOUSE_NUMBER,
				PartnerCsvColumns.POSTAL_CODE,
				PartnerCsvColumns.REGION_ISO_CODE,
				PartnerCsvColumns.COUNTRY_ISO_CODE, PartnerCsvColumns.EMAIL,
				PartnerCsvColumns.LANGUAGE_ISO_CODE,
				PartnerCsvColumns.MIDDLE_NAME, PartnerCsvColumns.MIDDLE_NAME2,
				PartnerCsvColumns.DISTRICT, PartnerCsvColumns.BUILDING,
				PartnerCsvColumns.APPARTMENT, PartnerCsvColumns.POBOX,
				PartnerCsvColumns.FAX, PartnerCsvColumns.TITLE,
				GreenleeOrderCsvColumns.ADDRNOTES));
	}
	

	@Override
	public List<Map<String, Object>> createRows(final OrderModel order) {
		if (order.getUser() instanceof GreenleeB2BCustomerModel) {
			final GreenleeB2BCustomerModel greenleeUser = (GreenleeB2BCustomerModel) order.getUser();
			logger.error("Creating rows for the channel "+order.getSite().getChannel()+" Customer "+greenleeUser.getUid() +" User Type "+ greenleeUser.getUserType());
			if (greenleeUser.getUserType().contains(GreenleeOrderCsvColumns.B2C)
					&& order.getPaymentAddress()!=null) {
				logger.error("Creating rows for "+GreenleeOrderCsvColumns.B2C+" Customer "+greenleeUser.getUid());
				return createRowsForB2CAndB2E(order);
			}
			if ((order.getSite().getChannel() == SiteChannel.B2B) && greenleeUser.getUserType().contains(GreenleeOrderCsvColumns.B2E) 
					&& order.getPaymentAddress() == null) 
			{
				//override the channel to B2C from B2B in order to create a bill to party (RE) for B2E customer - 
				//GSD-91
				
 				if(order.getSelectedBillingAddress()!=null)
				{
					logger.error("order.getPaymentAddress() null for the "+GreenleeOrderCsvColumns.B2E+" Customer "+greenleeUser.getUid()+" getSelectedBillingAddress "+order.getSelectedBillingAddress().getPk().getLongValue());
					AddressModel selectedBillingAddressModel=order.getSelectedBillingAddress();
					order.setPaymentAddress(selectedBillingAddressModel);
					logger.error("order.getPaymentAddress() null for the "+GreenleeOrderCsvColumns.B2E+" Customer "+greenleeUser.getUid()+" getPaymentAddress "+order.getPaymentAddress().getPk().getLongValue());
				}
				return createRowsForB2CAndB2E(order);
			}
			if ((order.getSite().getChannel() == SiteChannel.B2B) && greenleeUser.getUserType().contains(GreenleeOrderCsvColumns.B2B)) { // for B2B
				logger.error("Creating rows for "+GreenleeOrderCsvColumns.B2B+" Customer "+greenleeUser.getUid());
				return super.createRows(order);
			}
		}
		return null; 
	}

	protected List<Map<String, Object>> createRowsForB2CAndB2E(final OrderModel order) {
		final Map<String, Object> row1 = createPartnerRow(order,	
				PartnerRoles.SOLD_TO, soldToFromOrder(order));

		final Map<String, Object> row2 = createPartnerRow(order,
				PartnerRoles.CONTACT, contactFromOrder(order));

		final Map<String, Object> row3 = this.createAddressRow(order,
				PartnerRoles.SHIP_TO, SaporderexchangeConstants.ADDRESS_ONE);

		final Map<String, Object> row4 = this.createAddressRow(order,
				PartnerRoles.BILL_TO, SaporderexchangeConstants.ADDRESS_TWO);
		logger.error("enhancePartnerRows calling ");
		enhancePartnerRows(order, row4);
		logger.error("End of enhancePartnerRows");

		final List<Map<String, Object>> result = (row4 == null) ? Arrays
				.asList(row1, row2, row3) : Arrays.asList(row1, row2, row3,
				row4);
		return result;
	}

	protected void enhancePartnerRows(final OrderModel model,
			final Map<String, Object> row) {
		if (model.getPaymentInfo() instanceof CreditCardPaymentInfoModel) {
			logger.error("Creating ADDRNOTES : " );
			CreditCardPaymentInfoModel infoModel = (CreditCardPaymentInfoModel) model
					.getPaymentInfo();
			String adrNote=infoModel.getType().name()
					+ GreenleePartnerCsvColumns.ENDING
					+ infoModel.getNumber();
			row.put(GreenleeOrderCsvColumns.ADDRNOTES, adrNote);
			logger.error("Creating ADDRNOTES : " +adrNote);
		}

	}
	protected String getAddrNotes(final OrderModel model) {
		String addrNotes=null;
		final StringBuffer buffer=new StringBuffer();
		if (model.getPaymentInfo() instanceof CreditCardPaymentInfoModel) {
			CreditCardPaymentInfoModel ccpModel = (CreditCardPaymentInfoModel) model.getPaymentInfo();
			addrNotes=buffer.append(ccpModel.getType().name()).append(GreenleePartnerCsvColumns.ENDING).append(ccpModel.getNumber()).toString();
		}
		return addrNotes;

	}
	/**
	 *This method help to create the bill to party (RE) for the B2E Customer.
	 *
	 *GSD-91
	 *
	 **/
	public Map<String, Object> createAddressRow(final OrderModel order,
			final PartnerRoles partnerRole, final String addressNumber)
	{
		final AddressModel paymentAddress = order.getPaymentAddress();
		AddressModel deliveryAddress = order.getDeliveryAddress();
		final String partnerCode=soldToFromOrder(order);
		if (deliveryAddress != null && partnerRole.getCode().equals(PartnerRoles.SHIP_TO.getCode()))
		{
			Map<String, Object> row  = mapAddressData(order, deliveryAddress);
			logger.error("PartnerRoles.SHIP_TO.getCode() "+PartnerRoles.SHIP_TO.getCode() +" Address Code "+deliveryAddress.getPk().getLongValueAsString());
			row.put(OrderCsvColumns.ORDER_ID, order.getCode()); //added
			row.put(PartnerCsvColumns.PARTNER_ROLE_CODE, partnerRole.getCode());
			row.put(PartnerCsvColumns.PARTNER_CODE, partnerCode);
			row.put(PartnerCsvColumns.DOCUMENT_ADDRESS_ID, addressNumber);
			logger.error("SHIP_TO added");
			return row;
		}
		if (paymentAddress != null && partnerRole.getCode().equals(PartnerRoles.BILL_TO.getCode()))
		{
			Map<String, Object> row  = mapAddressData(order, paymentAddress);
			logger.error("PartnerRoles.BILL_TO.getCode() "+PartnerRoles.BILL_TO.getCode() +" Address Code "+paymentAddress.getPk().getLongValueAsString());
			row.put(OrderCsvColumns.ORDER_ID, order.getCode()); //added
			row.put(PartnerCsvColumns.PARTNER_ROLE_CODE, partnerRole.getCode());
			row.put(PartnerCsvColumns.PARTNER_CODE, partnerCode);
			row.put(PartnerCsvColumns.DOCUMENT_ADDRESS_ID, addressNumber);
			logger.error("BILL_TO added");
			return row;
		}
		return null;
		
	} 
}

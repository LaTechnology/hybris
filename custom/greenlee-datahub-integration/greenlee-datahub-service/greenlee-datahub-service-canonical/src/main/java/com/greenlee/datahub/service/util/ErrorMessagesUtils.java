package com.greenlee.datahub.service.util;

import java.util.HashMap;
import java.util.Map;

import com.hybris.datahub.model.CanonicalItem;

public class ErrorMessagesUtils {

	
	private static Map<String, String> failureMessages = null;
	private static final String REFERENCE = " - Reference ID - ";

	static
	{
		failureMessages = new HashMap<String, String>();
		failureMessages.put("CanonicalOrder", "ERR_NTFY_SUPPORT_00013 - Sales order xml post failure for reason: ");
		failureMessages.put("CanonicalBaseProduct", "ERR_NTFY_SUPPORT_00014 - Material master failure for reason: ");
		failureMessages.put("CanonicalSalesProduct", "ERR_NTFY_SUPPORT_00014 - Material master failure for reason: ");
		failureMessages.put("CanonicalPrice", "ERR_NTFY_SUPPORT_00015 - Price xml failure for reason: ");
		failureMessages.put("CanonicalPartySales", "ERR_NTFY_SUPPORT_00016 - Customer master failure for reason: ");
		failureMessages.put("CanonicalParty", "ERR_NTFY_SUPPORT_00016 - Customer master failure for reason: ");
		failureMessages.put("CanonicalPartyAddress", "ERR_NTFY_SUPPORT_00017 - Address failure for reason: ");
		failureMessages.put("CanonicalDeliveryCreationNotification", "ERR_NTFY_SUPPORT_00018 - Delivery creation (Non PGI) failure for reason: ");
		failureMessages.put("CanonicalGoodsIssueNotification", "ERR_NTFY_SUPPORT_00018 - Delivery creation (PGI) failure for reason: ");
		failureMessages.put("CanonicalOrderItemCancelNotification", "ERR_NTFY_SUPPORT_00019 - Order item cancellation failure for reason: ");
		failureMessages.put("CanonicalOrderCreationNotification", "ERR_NTFY_SUPPORT_00019 - Order creation failure for reason: ");
		failureMessages.put("CanonicalOrderItemCreationNotification", "ERR_NTFY_SUPPORT_00019 - Order item creation failure for reason: ");
		failureMessages.put("CanonicalOrderCancelNotification", "ERR_NTFY_SUPPORT_00019 - Order cancellation failure for reason: ");
	}

	
	
	private static String getErrorMessageWithCode(String canonicalType){
		return failureMessages.get(canonicalType);
	}

	public static String getErrorMessageWithCodeFromCanonical(
			CanonicalItem canonicalItem, String reason) {
		String errorMessage = getErrorMessageWithCode(canonicalItem.getType());
		if(errorMessage == null){
			return null;
		}
		return errorMessage + reason + REFERENCE + getPrimaryKey(canonicalItem);
	}
	
	private static String getPrimaryKey(CanonicalItem canonicalItem){
		String canonicalType = canonicalItem.getType();
		if(canonicalType.equals("CanonicalBaseProduct") || canonicalType.equals("CanonicalSalesProduct") || canonicalType.equals("CanonicalPrice")){
			return (String) canonicalItem.getField("productID");
		}else if(canonicalType.equals("CanonicalParty")){
			return (String) canonicalItem.getField("externalPartyId");
		}else if(canonicalType.equals("CanonicalPartySales")){
			return ((String) canonicalItem.getField("partyId") + (String) canonicalItem.getField("salesKey"));
		}else if(canonicalType.equals("CanonicalPartyAddress")){
			return (String) canonicalItem.getField("addressId");
		}
		return (String) canonicalItem.getField("orderId");
	}
}

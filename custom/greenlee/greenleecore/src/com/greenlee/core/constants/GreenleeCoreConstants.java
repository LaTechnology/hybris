/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.greenlee.core.constants;

/**
 * Global class for all GreenleeCore constants. You can add global constants for your extension into this class.
 */
public final class GreenleeCoreConstants extends GeneratedGreenleeCoreConstants
{
	public static final String EXTENSIONNAME = "greenleecore";

	public static final String SESSION_B2BUNIT_KEY = "sessionB2BUnit";
	public static final String SWITCHABLE_SESSION_B2BUNIT_KEY = "switchableB2BUnits";
	public static final String SWTCHACCT_LIMIT_TOSHOW_SEARCHBOX = "limitToShowSearchbox";

	public static final String CONFKEY_SWTCHACCT_LIMIT_TOSHOW_SEARCHBOX = "greenlee.switch.account.limitToShowSearchbox";

	public static final String DOMESTIC_SALESORG_KEY = "sap.domestic.salesorg.key";
	public static final String INTERNATIONAL_SALESORG_KEY = "sap.international.salesorg.key";

	public static final String DOMESTIC_SALESORG_VALUE = "sap.domestic.salesorg.value";
	public static final String INTERNATIONAL_SALESORG_VALUE = "sap.international.salesorg.value";

	public static final String ERROR_MSG = "Error retrieving autocomplete suggestions";

	public static final String CART_REMOVAL_MESSAGE = "cartRemovalAlertMsg";


	private GreenleeCoreConstants()
	{
		//empty
	}

	// implement here constants used by this extension
	public interface WORKFLOW
	{
		public static final String AUTO_PRODUCT_ATTRIBUTION_SUCCESS = "Auto_Product_Attribution_SUCCESS";
		public static final String AUTO_PRODUCT_ATTRIBUTION_FAILURE = "Auto_Product_Attribution_FAILURE";

		public static final String AUTO_PRODUCT_PUSH_TO_PRODUCTION = "Auto_Product_Push_To_Production_SUCCESS";

		public static final String AUTO_PRODUCT_PUSH_TO_PRODUCTION_SYNC_JOB_CODE = "sync greenleeProductCatalog:Staged->Online";
	}
}
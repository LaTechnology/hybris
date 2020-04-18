/**
 *
 */
package com.greenlee.core.util;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.util.Config;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greenlee.core.constants.GreenleeCoreConstants;
import com.greenlee.core.enums.UserTypes;
import com.greenlee.core.model.GreenleeB2BCustomerModel;


/**
 * @author nalini.ramarao
 *
 */
public class GreenleeUtils
{
	private static Map<String, String> sapOrderTypes = null;
	private final static Logger logger = LoggerFactory.getLogger(GreenleeUtils.class);

	private GreenleeUtils()
	{
		//
	}

	static
	{
		sapOrderTypes = new HashMap<String, String>();
		sapOrderTypes.put(Config.getString(GreenleeCoreConstants.DOMESTIC_SALESORG_KEY, "1300"),
				Config.getString(GreenleeCoreConstants.DOMESTIC_SALESORG_VALUE, "ZWOR"));
		sapOrderTypes.put(Config.getString(GreenleeCoreConstants.INTERNATIONAL_SALESORG_KEY, "1310"),
				Config.getString(GreenleeCoreConstants.INTERNATIONAL_SALESORG_VALUE, "ZWEX"));
	}


	public static String getSalesOrgFromB2BUnit(final B2BUnitModel b2bUnitModel)
	{
		final String[] b2bUnitKeyFields = b2bUnitModel.getUid().split("_");
		if (b2bUnitKeyFields.length == 4)
		{
			return b2bUnitKeyFields[1];
		}
		else
		{
			logger.error("Set the default DOMESTIC_SALESORG_KEY " + GreenleeCoreConstants.DOMESTIC_SALESORG_KEY);
			return Config.getString(GreenleeCoreConstants.DOMESTIC_SALESORG_KEY, "1300");
		}
	}

	public static String getAccNoFromB2BUnit(final B2BUnitModel b2bUnitModel)
	{
		final String[] b2bUnitKeyFields = b2bUnitModel.getUid().split("_");
		if (b2bUnitKeyFields.length == 4)
		{
			return b2bUnitKeyFields[0];
		}
		return null;
	}

	public static String getAccNoFromB2BUnitId(final String b2bUnitModel)
	{
		final String[] b2bUnitKeyFields = b2bUnitModel.split("_");
		if (b2bUnitKeyFields.length > 0)
		{
			return b2bUnitKeyFields[0];
		}
		return b2bUnitModel;
	}

	public static String getSAPOrderType(final String salesOrg)
	{
		return sapOrderTypes.get(salesOrg);
	}


	public static boolean isUserB2COrB2E(final GreenleeB2BCustomerModel greenleeB2BCustomerModel)
	{
		boolean isB2COrB2E = false;
		for (final PrincipalGroupModel unitData : greenleeB2BCustomerModel.getGroups())
		{
			if (unitData instanceof B2BUnitModel
					&& (StringUtils.equalsIgnoreCase(((B2BUnitModel) unitData).getUserType().getCode(), UserTypes.B2E.getCode()) || StringUtils
							.equalsIgnoreCase(((B2BUnitModel) unitData).getUserType().getCode(), UserTypes.B2C.getCode())))
			{
				isB2COrB2E = true;
			}
		}
		return isB2COrB2E;
	}

	public static B2BUnitModel getB2BUnitForB2COrB2EUser(final GreenleeB2BCustomerModel greenleeB2BCustomerModel)
	{
		B2BUnitModel b2COrB2E = null;
		for (final PrincipalGroupModel unitData : greenleeB2BCustomerModel.getGroups())
		{
			if (unitData instanceof B2BUnitModel
					&& (StringUtils.equalsIgnoreCase(((B2BUnitModel) unitData).getUserType().getCode(), UserTypes.B2E.getCode()) || StringUtils
							.equalsIgnoreCase(((B2BUnitModel) unitData).getUserType().getCode(), UserTypes.B2C.getCode())))
			{
				b2COrB2E = (B2BUnitModel) unitData;
			}
		}
		return b2COrB2E;
	}




}

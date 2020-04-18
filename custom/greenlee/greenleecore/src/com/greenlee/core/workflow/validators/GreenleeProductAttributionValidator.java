/**
 *
 */
package com.greenlee.core.workflow.validators;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.greenlee.core.model.GreenleeProductModel;


/**
 * @author midhun.bose
 * 
 */
public class GreenleeProductAttributionValidator
{
	private GreenleeProductAttributionValidator()
	{
		//
	}

	public static boolean validate(final GreenleeProductModel product)
	{
		if (StringUtils.isEmpty(product.getCatalogNumber()) || StringUtils.isEmpty(product.getSmallUPC())
				|| StringUtils.isEmpty(product.getLongUPC()) || StringUtils.isEmpty(product.getUnspscNumber()))
		{
			return false;
		}

		if (StringUtils.isEmpty(product.getCode()) || StringUtils.isEmpty(product.getSummary())
				|| CollectionUtils.isEmpty(product.getKeywords()))
		{
			return false;
		}

		if (product.getSupercategories() == null || product.getSupercategories().size() <= 0)
		{
			return false;
		}

		if (product.getReturnable() == null || product.getObsolete() == null)
		{
			return false;
		}

		if (product.getWeight() == null || StringUtils.isEmpty(product.getWeightUnits()))
		{
			return false;
		}

		if (StringUtils.isEmpty(product.getSapSegmentID()) || StringUtils.isEmpty(product.getSapFamilyID())
				|| StringUtils.isEmpty(product.getSapGroupID()) || StringUtils.isEmpty(product.getSapSubGroupID()))
		{
			return false;
		}

		if (StringUtils.isEmpty(product.getCountryOfOrigin()) || StringUtils.isEmpty(product.getCommissionGroupCode()))
		{
			return false;
		}

		if (StringUtils.isEmpty(product.getHtsCode()) || StringUtils.isEmpty(product.getRohsIndicator())
				|| StringUtils.isEmpty(product.getMsdsFlag()))
		{
			return false;
		}

		return true;

	}

}

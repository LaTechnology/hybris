/**
 *
 */
package com.greenlee.facades.costcenter;

import de.hybris.platform.b2bcommercefacades.company.data.B2BCostCenterData;

/**
 * @author savarimuthu.s
 *
 */
public interface GreenleeB2BCostCenterFacade
{
    /**
     * This service to provide the cost center data based on the B2BUnit Uid.
     * 
     * @param cardId
     *            ,
     * @param b2bUnitId
     * @throws Exception
     *             return B2BCostCenterData
     */
    public B2BCostCenterData getGreenleeB2BUnitCostCenters(final String cartId, final String b2bUnitId) throws Exception;
}

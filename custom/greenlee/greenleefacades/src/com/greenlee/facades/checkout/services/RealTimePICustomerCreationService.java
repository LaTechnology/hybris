/**
 *
 */
package com.greenlee.facades.checkout.services;

import de.hybris.platform.util.Config;


/**
 * @author peter.asirvatham
 *
 */
public interface RealTimePICustomerCreationService
{
    public static final String DUMMY_UNIT_B2B       = Config.getString("greenlee.account.dummy.distributor.uid",
                                                            "dummydistributor");
    public static final String DUMMY_UNIT_B2E       = Config.getString("greenlee.account.dummy.distributor.b2e.uid", "0010000014");
    public static final String DUMMY_UNIT_B2C       = Config.getString("greenlee.account.dummy.distributor.b2c.uid", "0010000012");
    public static final String DATE_FORMAT          = "yyyyMMdd";
    public static final String SEPARATOR_UNDERSCORE = "_";

    public static final String DUMMY_UNIT_B2B_CAD   = Config.getString("greenlee.account.dummy.distributor.uid.cad",
                                                            "dummydistributor_CAD");
    public static final String DUMMY_UNIT_B2E_CAD   = Config.getString("greenlee.account.dummy.distributor.b2e.uid.cad",
                                                            "0010000014");
    public static final String DUMMY_UNIT_B2C_CAD   = Config.getString("greenlee.account.dummy.distributor.b2c.uid.cad",
                                                            "0010000012");


    public abstract boolean isCustomerCreated(final StringBuilder errorMessage, String livingstonScreenStatus);
}

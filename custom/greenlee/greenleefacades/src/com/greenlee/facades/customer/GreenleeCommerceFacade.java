/**
 *
 */
package com.greenlee.facades.customer;

import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.commercefacades.user.data.PrincipalData;

import java.util.List;
import java.util.Set;

import com.greenlee.core.model.GreenleeB2BCustomerModel;


/**
 * Facade to access B2BUnit related functionalities
 *
 * @author raja.santhanam
 *
 */
public interface GreenleeCommerceFacade
{
    /**
     * Fethes the all the secondary(Level2) B2BUnits configured in the system.
     *
     * @return a list of all secondary(Level2) B2BUnits
     */
    List<B2BUnitData> getAllSecondaryUnitsOfOrganization();

    /**
     * Fethes the all the secondary(Level2) B2BUnits, eligible for switching for the current user
     *
     * @return a list of switchable secondary(Level2) B2BUnits
     */
    List<B2BUnitData> getSwitchableUnitsOfOrganization(final GreenleeB2BCustomerModel customer);

    Set<PrincipalData> getMembersToBeEmailed(final GreenleeB2BCustomerModel customer);
}

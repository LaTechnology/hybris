/**
 *
 */
package com.greenlee.core.unit.services;

import de.hybris.platform.b2b.model.B2BUnitModel;

import java.util.List;


/**
 * Service to access B2BUnit configured in the system.
 *
 * @author raja.santhanam
 *
 */
public interface GreenleeUnitService
{
	/**
	 * Fethes the all the secondary(Level2) B2BUnits configured in the system.
	 *
	 * @return a list of all secondary(Level2) B2BUnits
	 */
	public List<B2BUnitModel> getAllSecondaryB2BUnits();

	/**
	 * Fetches all the B2BUnits configured in the system.
	 *
	 * @return a list of all B2BUnits
	 */
	public List<B2BUnitModel> getAllB2BUnits();

	/**
	 * Fethes the all the primary(Level1) B2BUnits configured in the system.
	 *
	 * @return a list of all primary(Level1) B2BUnits
	 */
	public List<B2BUnitModel> getAllPrimaryB2BUnits();
}

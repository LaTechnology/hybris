/**
 *
 */
package com.greenlee.core.unit.dao;

import de.hybris.platform.b2b.model.B2BUnitModel;

import java.util.List;


/**
 * Dao to access B2BUnit.
 *
 * @author raja.santhanam
 *
 */
public interface GreenleeUnitDao
{
	/**
	 * Fetches all the B2BUnits configured on the system.
	 *
	 * @return - List of B2BUnits
	 */
	public List<B2BUnitModel> getAllB2BUnits();
}

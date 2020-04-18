/**
 *
 */
package com.hybris.addon.cockpits.liveedit.generic.handlers;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.wizards.Wizard;


/**
 * @author aandone
 */
public interface BeforeSaveWizardActionHandler
{
	/**
	 * Actions executed before saving the item
	 *
	 * @param wizard
	 *           current wizard
	 * @param item
	 *           current item object
	 * @return true, if should be saved
	 */
	public boolean beforeSave(final Wizard wizard, final TypedObject item);
}

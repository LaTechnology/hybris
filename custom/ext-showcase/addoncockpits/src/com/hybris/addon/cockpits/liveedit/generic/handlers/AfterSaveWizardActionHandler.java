package com.hybris.addon.cockpits.liveedit.generic.handlers;

import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;


/**
 * @author aandone
 */
public interface AfterSaveWizardActionHandler
{
	/**
	 * Actions executed after saving the items
	 * 
	 * @param wizard
	 *           current wizard
	 * @param page
	 *           current page
	 */
	public void afterSave(final Wizard wizard, final WizardPage page);
}

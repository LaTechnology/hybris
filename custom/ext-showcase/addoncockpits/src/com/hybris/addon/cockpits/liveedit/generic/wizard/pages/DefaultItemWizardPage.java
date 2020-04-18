package com.hybris.addon.cockpits.liveedit.generic.wizard.pages;

import de.hybris.platform.cockpit.wizards.generic.DefaultGenericItemPage;

import org.springframework.beans.factory.annotation.Required;


/**
 * @author aandone - Generic item page with string item type as parameter
 */
public class DefaultItemWizardPage extends DefaultGenericItemPage
{
	protected String currentTypeString;

	public String getCurrentTypeString()
	{
		return currentTypeString;
	}

	@Required
	public void setCurrentTypeString(final String currentTypeString)
	{
		this.currentTypeString = currentTypeString;
	}


}

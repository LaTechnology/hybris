package com.hybris.addon.cockpits.liveedit.generic.wizard.pages;

import de.hybris.platform.core.model.ItemModel;

import java.util.Map;


/**
 * @author aandone
 */
public interface WizardItemMandatoryPage<T extends ItemModel>
{
	public Map<String, Object> fillPredefinedValues(T model);
}

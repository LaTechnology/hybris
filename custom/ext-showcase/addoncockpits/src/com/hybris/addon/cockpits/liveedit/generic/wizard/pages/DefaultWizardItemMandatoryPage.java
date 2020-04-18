package com.hybris.addon.cockpits.liveedit.generic.wizard.pages;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.session.UISession;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.wizards.generic.GenericItemMandatoryPage;
import de.hybris.platform.core.model.ItemModel;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;

import com.hybris.addon.cockpits.liveedit.generic.wizard.DefaultGenericItemWizard;


/**
 * Generic Item Mandatory Page for multitype wizard
 *
 * @author aandone
 * @param <T>
 *           item model
 */
public class DefaultWizardItemMandatoryPage<T extends ItemModel> extends GenericItemMandatoryPage implements
		WizardItemMandatoryPage<T>
{

	protected String currentTypeString;

	@Override
	public Component createRepresentationItself()
	{
		if (StringUtils.isNotBlank(currentTypeString))
		{
			T model = null;
			final ObjectType currentType = UISessionUtils.getCurrentSession().getTypeService().getObjectType(currentTypeString);
			final UISession session = UISessionUtils.getCurrentSession();
			final DefaultGenericItemWizard itemWizard = (DefaultGenericItemWizard) getWizard();
			itemWizard.setCurrentType(currentType);

			if (itemWizard.getItem() == null)
			{
				model = session.getModelService().create(currentType.getCode());
			}
			else
			{
				model = (T) itemWizard.getItem().getObject();
			}

			final Map<String, Object> additionalValues = fillPredefinedValues(model);
			itemWizard.handleAdditionalValues(additionalValues, model.getItemtype(), itemWizard.getObjectValueContainer());

			itemWizard.setItem(session.getTypeService().wrapItem(model));
		}

		return super.createRepresentationItself();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hybris.addon.cockpits.liveedit.generic.wizard.pages.WizardItemMandatoryPage#fillPredefinedValues(de.hybris
	 * .platform.core.model.ItemModel)
	 */
	@Override
	public Map<String, Object> fillPredefinedValues(final T model)
	{
		return new HashMap<String, Object>();
	}

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

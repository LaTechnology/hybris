package com.hybris.addon.cockpits.liveedit.generic.wizard.pages;

import de.hybris.platform.cockpit.wizards.generic.DecisionPage;

import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;


/**
 * Generic page used to create decision pages on multitype wizard
 * 
 * @author aandone
 */
public class DefaultWizardItemDecisionPage extends DecisionPage
{

	private Map<String, String> decisionsMap;

	@Override
	public Component createRepresentationItself()
	{
		if (CollectionUtils.isEmpty(decisions))
		{
			createDecisions();
		}
		return super.createRepresentationItself();
	}

	protected void createDecisions()
	{
		for (final Map.Entry<String, String> entry : decisionsMap.entrySet())
		{
			final String decisionLabel = entry.getKey();
			final String nextWizardPageId = entry.getValue();

			decisions.add(new Decision(nextWizardPageId, Labels.getLabel(decisionLabel), DEFAULT_ELEMENT_IMAGE));
		}
	}

	public Map<String, String> getDecisionsMap()
	{
		return decisionsMap;
	}

	@Required
	public void setDecisionsMap(final Map<String, String> decisionsMap)
	{
		this.decisionsMap = decisionsMap;
	}

}

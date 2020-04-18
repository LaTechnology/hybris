/**
 *
 */
package com.greenlee.core.workflow.jobs;

import de.hybris.platform.workflow.jobs.AutomatedWorkflowTemplateJob;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;


/**
 * @author midhun.bose
 *
 */
public class CreativeConfirmationActionJob implements AutomatedWorkflowTemplateJob
{

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.workflow.jobs.AutomatedWorkflowTemplateJob#perform(de.hybris.platform.workflow.model.
	 * WorkflowActionModel)
	 */
	@Override
	public WorkflowDecisionModel perform(final WorkflowActionModel action)
	{
		for (final WorkflowDecisionModel decision : action.getDecisions())
		{
			return decision;
		}
		return null;
	}

}

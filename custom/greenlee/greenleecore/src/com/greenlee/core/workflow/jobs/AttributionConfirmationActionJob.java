/**
 *
 */
package com.greenlee.core.workflow.jobs;

import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.greenlee.core.constants.GreenleeCoreConstants;
import com.greenlee.core.model.GreenleeProductModel;
import com.greenlee.core.workflow.validators.GreenleeProductAttributionValidator;


/**
 * @author midhun.bose
 * 
 */
public class AttributionConfirmationActionJob extends AbstractConfirmationActionJob
{

	@Autowired
	ModelService modelService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.workflow.jobs.AutomatedWorkflowTemplateJob#perform(de.hybris.platform.workflow.model.
	 * WorkflowActionModel)
	 */
	@Override
	public WorkflowDecisionModel perform(final WorkflowActionModel action)
	{

		final GreenleeProductModel product = getAttachedGreenleeProduct(action);
		final Map<String, WorkflowDecisionModel> decisions = new HashMap<String, WorkflowDecisionModel>();
		for (final WorkflowDecisionModel decision : action.getDecisions())
		{
			decisions.put(decision.getCode(), decision);
		}


		final boolean isAttributionComplete = GreenleeProductAttributionValidator.validate(product);


		if (isAttributionComplete)
		{
			return decisions.get(GreenleeCoreConstants.WORKFLOW.AUTO_PRODUCT_ATTRIBUTION_SUCCESS);
		}
		else
		{
			final List<CommentModel> comments = new ArrayList<CommentModel>();
			final CommentModel comment = new CommentModel();
			comment.setText("Please update all the mandatory attributes");
			comment.setSubject("Please update all the mandatory attributes");
			action.setComments(comments);
			action.setComment("Please update all the mandatory attributes");
			modelService.save(action);
			return decisions.get(GreenleeCoreConstants.WORKFLOW.AUTO_PRODUCT_ATTRIBUTION_FAILURE);
		}

	}

}

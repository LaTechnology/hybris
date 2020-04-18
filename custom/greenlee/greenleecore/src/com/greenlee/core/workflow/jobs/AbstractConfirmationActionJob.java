/**
 *
 */
package com.greenlee.core.workflow.jobs;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.workflow.jobs.AutomatedWorkflowTemplateJob;
import de.hybris.platform.workflow.model.WorkflowActionModel;

import java.util.List;

import com.greenlee.core.model.GreenleeProductModel;


/**
 * @author midhun.bose
 *
 */
public abstract class AbstractConfirmationActionJob implements AutomatedWorkflowTemplateJob
{
	protected GreenleeProductModel getAttachedGreenleeProduct(final WorkflowActionModel action)
	{
		final List<ItemModel> attachments = action.getAttachmentItems();
		if (attachments != null)
		{
			for (final ItemModel item : attachments)
			{
				if (item instanceof GreenleeProductModel)
				{
					return (GreenleeProductModel) item;
				}
			}
		}
		return null;
	}
}

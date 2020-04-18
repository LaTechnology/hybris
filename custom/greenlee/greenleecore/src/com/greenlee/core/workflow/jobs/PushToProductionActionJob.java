/**
 *
 */
package com.greenlee.core.workflow.jobs;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncCronJobModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.jobs.AutomatedWorkflowTemplateJob;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.greenlee.core.constants.GreenleeCoreConstants;


/**
 * @author kaushik.ganguly
 *
 */
public class PushToProductionActionJob implements AutomatedWorkflowTemplateJob
{

	@Autowired
	ModelService modelService;

	@Autowired
	UserService userService;

	@Autowired
	CronJobService cronJobService;

	@Autowired
	CatalogVersionService catalogVersionService;

	@Autowired
	ConfigurationService configurationService;

	private static final Logger LOG = Logger.getLogger(PushToProductionActionJob.class);

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.workflow.jobs.AutomatedWorkflowTemplateJob#perform(de.hybris.platform.workflow.model.
	 * WorkflowActionModel)
	 */
	@Override
	public WorkflowDecisionModel perform(final WorkflowActionModel action)
	{
		final Date now = new Date();

		final Map<String, WorkflowDecisionModel> decisions = new HashMap<String, WorkflowDecisionModel>();
		for (final WorkflowDecisionModel decision : action.getDecisions())
		{
			decisions.put(decision.getCode(), decision);
		}


		final CatalogVersionSyncCronJobModel catalogVersionCronSyncJob = modelService.create(CatalogVersionSyncCronJobModel.class);
		final CatalogVersionSyncJobModel catalogVersionSyncJob = modelService.create(CatalogVersionSyncJobModel.class);
		catalogVersionSyncJob.setCode(GreenleeCoreConstants.WORKFLOW.AUTO_PRODUCT_PUSH_TO_PRODUCTION_SYNC_JOB_CODE + now.getTime());
		final String catalogName = configurationService.getConfiguration().getString("greenlee.product.workflow.catalogname");
		catalogVersionSyncJob.setSourceVersion(catalogVersionService.getCatalogVersion(catalogName, "Staged"));
		catalogVersionSyncJob.setTargetVersion(catalogVersionService.getCatalogVersion(catalogName, "Online"));
		catalogVersionCronSyncJob.setJob(catalogVersionSyncJob);

		modelService.save(catalogVersionCronSyncJob);
		cronJobService.performCronJob(catalogVersionCronSyncJob);


		LOG.info("Invoked the catalog sync job: Started ");


		return decisions.get(GreenleeCoreConstants.WORKFLOW.AUTO_PRODUCT_PUSH_TO_PRODUCTION);
	}

}

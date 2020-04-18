/**
 *
 */
package com.greenlee.core.event;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import com.greenlee.core.model.ShareProductProcessModel;



/**
 *
 */
public class ShareProductEventListener extends AbstractSiteEventListener<ShareProductEvent>
{
	private ModelService modelService;
	private BusinessProcessService businessProcessService;

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the businessProcessService
	 */
	public BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	/**
	 * @param businessProcessService
	 *           the businessProcessService to set
	 */
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.commerceservices.event.AbstractSiteEventListener#onSiteEvent(de.hybris.platform.servicelayer.
	 * event.events.AbstractEvent)
	 */
	@Override
	protected void onSiteEvent(final ShareProductEvent event)
	{
		final ShareProductProcessModel shareProductProcessModel = (ShareProductProcessModel) getBusinessProcessService()
				.createProcess("shareProductEmailProcess-" + event.getGreenleeProductModel().getCode() + "-" + event.getToAddress()
						+ "-" + event.getFromAddress() + "-" + System.currentTimeMillis(), "shareProductEmailProcess");
		shareProductProcessModel.setSite(event.getSite());
		shareProductProcessModel.setStore(event.getBaseStore());
		shareProductProcessModel.setToAddress(event.getToAddress());
		shareProductProcessModel.setFromAddress(event.getFromAddress());
		shareProductProcessModel.setGreenleeProductModel(event.getGreenleeProductModel());
		shareProductProcessModel.setMessage(event.getMessage());
		getModelService().save(shareProductProcessModel);
		getBusinessProcessService().startProcess(shareProductProcessModel);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.commerceservices.event.AbstractSiteEventListener#shouldHandleEvent(de.hybris.platform.
	 * servicelayer.event.events.AbstractEvent)
	 */
	@Override
	protected boolean shouldHandleEvent(final ShareProductEvent event)
	{
		final BaseSiteModel site = event.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage("event.site", site);
		return SiteChannel.B2B.equals(site.getChannel());
	}

}

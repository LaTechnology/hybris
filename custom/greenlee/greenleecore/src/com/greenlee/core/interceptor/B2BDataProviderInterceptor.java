/**
 *
 */
package com.greenlee.core.interceptor;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.log4j.Logger;

import com.greenlee.core.util.GreenleeDummyDataProvider;


/**
 * @author peter.asirvatham
 * 
 */
public class B2BDataProviderInterceptor implements PrepareInterceptor<B2BUnitModel>
{

	private static final Logger LOG = Logger.getLogger(B2BDataProviderInterceptor.class);
	private ModelService modelService;
	private GreenleeDummyDataProvider greenleeDummyDataProvider;


	/**
	 * @return the greenleeDummyDataProvider
	 */
	public GreenleeDummyDataProvider getGreenleeDummyDataProvider()
	{
		return greenleeDummyDataProvider;
	}

	/**
	 * @param greenleeDummyDataProvider
	 *           the greenleeDummyDataProvider to set
	 */
	public void setGreenleeDummyDataProvider(final GreenleeDummyDataProvider greenleeDummyDataProvider)
	{
		this.greenleeDummyDataProvider = greenleeDummyDataProvider;
	}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.servicelayer.interceptor.PrepareInterceptor#onPrepare(java.lang.Object,
	 * de.hybris.platform.servicelayer.interceptor.InterceptorContext)
	 */

	@Override
	public void onPrepare(final B2BUnitModel b2bUnitModel, final InterceptorContext context) throws InterceptorException
	{
		if (modelService.isNew(b2bUnitModel))
		{
			greenleeDummyDataProvider.createDummyBudgetAndCost(b2bUnitModel);
			LOG.info("Dummy record created for :" + b2bUnitModel.getDisplayName());
		}

	}

}

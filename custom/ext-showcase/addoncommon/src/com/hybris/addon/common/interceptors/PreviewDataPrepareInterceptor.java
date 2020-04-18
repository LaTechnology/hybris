/**
 *
 */
package com.hybris.addon.common.interceptors;

import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

/**
 * PreviewData PrepareInterceptor
 * @author a.andone
 *
 */
public class PreviewDataPrepareInterceptor implements PrepareInterceptor<PreviewDataModel>
{
	@Override
	public void onPrepare(PreviewDataModel model, InterceptorContext ctx) throws InterceptorException
	{
		//if a different product catalog is selected from preview context
		if(!ctx.isModified(model, PreviewDataModel.ACTIVECATALOGVERSION) && ctx.isModified(model, PreviewDataModel.CATALOGVERSIONS))
		{
			//model.setCatalogVersionChanged(true);
			ctx.registerElement(model, getModelSource(ctx, model));
		}

	}

	private Object getModelSource(final InterceptorContext ctx, final ItemModel model)
	{
		if (ctx.isNew(model))
		{
			return null;
		}

		return ctx.getModelService().getSource(model);
	}
}

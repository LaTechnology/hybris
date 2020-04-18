/**
 *
 */
package com.greenlee.core.interceptor;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;

import com.greenlee.core.model.GreenleeB2BCustomerModel;
import com.greenlee.core.permission.services.GreenleeB2BPermissionService;


/**
 * @author kaushik ganguly this interceptor creates the three B2Bpermissions automatically as the customer model is
 *         created or modified.
 * 
 */
public class B2BPermissionsCreationInterceptor implements PrepareInterceptor<GreenleeB2BCustomerModel>
{

	private ModelService modelService;
	private GreenleeB2BPermissionService greenleeB2BPermissionService;

	@Override
	public void onPrepare(final GreenleeB2BCustomerModel greenleeB2BCustomerModel, final InterceptorContext context)
			throws InterceptorException
	{
		if (isCustomerNewOrModified(greenleeB2BCustomerModel)
				&& !greenleeB2BPermissionService.hasB2BPermissions(greenleeB2BCustomerModel))
		{
			greenleeB2BPermissionService.createB2BPermissionsForCustomer(greenleeB2BCustomerModel);
		}

	}

	private Boolean isCustomerNewOrModified(final GreenleeB2BCustomerModel greenleeB2BCustomerModel)
	{
		return modelService.isNew(greenleeB2BCustomerModel) || modelService.isModified(greenleeB2BCustomerModel);
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

	/**
	 * @return the greenleeB2BPermissionService
	 */
	public GreenleeB2BPermissionService getGreenleeB2BPermissionService()
	{
		return greenleeB2BPermissionService;
	}

	/**
	 * @param greenleeB2BPermissionService
	 *           the greenleeB2BPermissionService to set
	 */
	public void setGreenleeB2BPermissionService(final GreenleeB2BPermissionService greenleeB2BPermissionService)
	{
		this.greenleeB2BPermissionService = greenleeB2BPermissionService;
	}

}

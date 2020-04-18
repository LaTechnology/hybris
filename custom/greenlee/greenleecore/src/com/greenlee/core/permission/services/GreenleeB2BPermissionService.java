/**
 *
 */
package com.greenlee.core.permission.services;

import de.hybris.platform.b2b.model.B2BPermissionModel;
import de.hybris.platform.b2b.model.B2BUnitModel;

import java.util.List;
import java.util.Set;

import com.greenlee.core.model.GreenleeB2BCustomerModel;
import com.greenlee.core.permission.services.impl.GreenleeB2BPermissions;


/**
 * @author kaushik
 *
 */
public interface GreenleeB2BPermissionService
{
	public void createB2BPermissionsForCustomer(final GreenleeB2BCustomerModel greenleeB2BCustomerModel);

	public void createB2BPermissionsForSalesRepresentatives(final GreenleeB2BCustomerModel greenleeB2BCustomerModel);

	public void createB2BPermissionForCustomer(final GreenleeB2BCustomerModel greenleeB2BCustomerModel,
			final B2BUnitModel b2bUnitModel, final GreenleeB2BPermissions permissionType);

	public boolean hasB2BPermissions(final GreenleeB2BCustomerModel greenleeB2BCustomerModel);

	public List<B2BPermissionModel> getAllPermissionsForB2BUnit(final GreenleeB2BCustomerModel greenleeB2BCustomerModel,
			final B2BUnitModel b2bUnitModel);

	public B2BPermissionModel getB2BPermissionModel(final GreenleeB2BCustomerModel greenleeB2BCustomerModel,
			final B2BUnitModel b2bUnitModel, final GreenleeB2BPermissions permissionType);

	public String getPermissionNameForUser(final GreenleeB2BCustomerModel greenleeB2BCustomerModel,
			final B2BUnitModel b2bUnitModel, final GreenleeB2BPermissions permissionType);

	public Set<B2BPermissionModel> getAllPermissionsAsSet(final GreenleeB2BCustomerModel greenleeB2BCustomerModel);


}

/**
 *
 */
package com.greenlee.core.permission.services.impl;

/**
 * @author kaushik
 *
 */
public enum GreenleeB2BPermissions
{
	TIMESPANPERMISSION("_timepermission_"), ORDERTHRESHOLD("_orderpermission_"), BUDGETEXCEEDEDPERMISSION("_budgetpermission_");
	private String permissionName;

	private GreenleeB2BPermissions(final String permission)
	{
		this.permissionName = permission;
	}

	public String getPermissionName()
	{
		return this.permissionName;
	}

}

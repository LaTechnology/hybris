/**
 *
 */
package com.greenlee.core.permission.services.impl;

import de.hybris.platform.b2b.enums.B2BPeriodRange;
import de.hybris.platform.b2b.model.B2BBudgetExceededPermissionModel;
import de.hybris.platform.b2b.model.B2BOrderThresholdPermissionModel;
import de.hybris.platform.b2b.model.B2BOrderThresholdTimespanPermissionModel;
import de.hybris.platform.b2b.model.B2BPermissionModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.greenlee.core.model.GreenleeB2BCustomerModel;
import com.greenlee.core.permission.services.GreenleeB2BPermissionService;


/**
 * @author kaushik
 *
 */
public class DefaultGreenleeB2BPermissionService implements GreenleeB2BPermissionService
{

	private ConfigurationService configurationService;

	private ModelService modelService;

	private static final Logger LOG = Logger.getLogger(DefaultGreenleeB2BPermissionService.class);



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
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}


	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}


	@Override
	public void createB2BPermissionsForCustomer(final GreenleeB2BCustomerModel greenleeB2BCustomerModel)
	{
		if (greenleeB2BCustomerModel != null && greenleeB2BCustomerModel.getGroups() != null)
		{
			for (final PrincipalGroupModel b2bUnit : greenleeB2BCustomerModel.getGroups())
			{
				if (b2bUnit instanceof B2BUnitModel)
				{
					final List<B2BPermissionModel> permissions = getAllPermissionsForB2BUnit(greenleeB2BCustomerModel,
							(B2BUnitModel) b2bUnit);
					if (permissions != null && !permissions.isEmpty())
					{
						findOrCreatePermission(permissions, greenleeB2BCustomerModel, (B2BUnitModel) b2bUnit);
					}
					else
					{

						createB2BPermissionForCustomer(greenleeB2BCustomerModel, (B2BUnitModel) b2bUnit,
								GreenleeB2BPermissions.BUDGETEXCEEDEDPERMISSION);
						createB2BPermissionForCustomer(greenleeB2BCustomerModel, (B2BUnitModel) b2bUnit,
								GreenleeB2BPermissions.ORDERTHRESHOLD);
						createB2BPermissionForCustomer(greenleeB2BCustomerModel, (B2BUnitModel) b2bUnit,
								GreenleeB2BPermissions.TIMESPANPERMISSION);
					}

				}
			}
		}
	}

	private void findOrCreatePermission(final List<B2BPermissionModel> permissions,
			final GreenleeB2BCustomerModel greenleeB2BCustomerModel, final B2BUnitModel b2bUnitModel)
	{
		for (final GreenleeB2BPermissions s : GreenleeB2BPermissions.values())
		{
			final boolean permissionMissing = findPermission(permissions, greenleeB2BCustomerModel, b2bUnitModel, s);

			if (permissionMissing)
			{
				createB2BPermissionForCustomer(greenleeB2BCustomerModel, b2bUnitModel, s);
			}
		}
	}

	private boolean findPermission(final List<B2BPermissionModel> permissions,
			final GreenleeB2BCustomerModel greenleeB2BCustomerModel, final B2BUnitModel b2bUnitModel,
			final GreenleeB2BPermissions permissionType)
	{
		boolean permissionMissing = true;
		for (final B2BPermissionModel permission : permissions)
		{
			if (permission.getCode().equals(getPermissionNameForUser(greenleeB2BCustomerModel, b2bUnitModel, permissionType)))
			{
				permissionMissing = false;
				break;
			}
		}

		return permissionMissing;
	}


	@Override
	public void createB2BPermissionsForSalesRepresentatives(final GreenleeB2BCustomerModel greenleeB2BCustomerModel)
	{
		if (greenleeB2BCustomerModel != null && greenleeB2BCustomerModel.getAllowedAccounts() != null)
		{
			for (final B2BUnitModel b2bUnit : greenleeB2BCustomerModel.getAllowedAccounts())
			{

				final List<B2BPermissionModel> permissions = getAllPermissionsForB2BUnit(greenleeB2BCustomerModel, b2bUnit);
				if (permissions != null && !permissions.isEmpty())
				{
					findOrCreatePermission(permissions, greenleeB2BCustomerModel, b2bUnit);
				}
				else
				{
					createB2BPermissionForCustomer(greenleeB2BCustomerModel, b2bUnit, GreenleeB2BPermissions.BUDGETEXCEEDEDPERMISSION);
					createB2BPermissionForCustomer(greenleeB2BCustomerModel, b2bUnit, GreenleeB2BPermissions.ORDERTHRESHOLD);
					createB2BPermissionForCustomer(greenleeB2BCustomerModel, b2bUnit, GreenleeB2BPermissions.TIMESPANPERMISSION);
				}


			}
		}
	}



	@Override
	public void createB2BPermissionForCustomer(final GreenleeB2BCustomerModel greenleeB2BCustomerModel,
			final B2BUnitModel b2bUnitModel, final GreenleeB2BPermissions permissionType)
	{
		switch (permissionType)
		{
			case TIMESPANPERMISSION:
				createDummyTimelinePermission(greenleeB2BCustomerModel, b2bUnitModel, permissionType);
				break;
			case ORDERTHRESHOLD:
				createDummyOrderThresholdPermission(greenleeB2BCustomerModel, b2bUnitModel, permissionType);
				break;
			case BUDGETEXCEEDEDPERMISSION:
				createDummyBudgetExceededPermission(greenleeB2BCustomerModel, b2bUnitModel, permissionType);
				break;
			default:
				LOG.info("none of the permissions matched");
				break;
		}
	}

	private void createDummyTimelinePermission(final GreenleeB2BCustomerModel greenleeB2BCustomerModel,
			final B2BUnitModel b2bUnitModel, final GreenleeB2BPermissions permissionType)
	{
		final B2BOrderThresholdTimespanPermissionModel orderThresholdTimeSpanPermission = modelService
				.create(B2BOrderThresholdTimespanPermissionModel.class);
		orderThresholdTimeSpanPermission.setActive(true);
		orderThresholdTimeSpanPermission.setCode(getPermissionNameForUser(greenleeB2BCustomerModel, b2bUnitModel, permissionType));
		orderThresholdTimeSpanPermission.setUnit(b2bUnitModel);
		orderThresholdTimeSpanPermission.setCurrency(b2bUnitModel.getCurrencyPreference());
		orderThresholdTimeSpanPermission.setThreshold(new Double(configurationService.getConfiguration().getDouble(
				"b2bcustomer.orderthreshold.amount")));
		orderThresholdTimeSpanPermission.setRange(B2BPeriodRange.DAY);
		if (greenleeB2BCustomerModel.getPermissions() != null)
		{
			final Set permissions = getAllPermissionsAsSet(greenleeB2BCustomerModel);
			permissions.add(orderThresholdTimeSpanPermission);
			greenleeB2BCustomerModel.setPermissions(permissions);
		}
		else
		{
			greenleeB2BCustomerModel.setPermissions(new HashSet(Arrays.asList(new B2BPermissionModel[]
			{ orderThresholdTimeSpanPermission })));
		}
	}

	private void createDummyOrderThresholdPermission(final GreenleeB2BCustomerModel greenleeB2BCustomerModel,
			final B2BUnitModel b2bUnitModel, final GreenleeB2BPermissions permissionType)
	{
		final B2BOrderThresholdPermissionModel orderThresholdPermission = modelService
				.create(B2BOrderThresholdPermissionModel.class);
		orderThresholdPermission.setActive(true);
		orderThresholdPermission.setCode(getPermissionNameForUser(greenleeB2BCustomerModel, b2bUnitModel, permissionType));
		orderThresholdPermission.setUnit(b2bUnitModel);
		orderThresholdPermission.setCurrency(b2bUnitModel.getCurrencyPreference());
		orderThresholdPermission.setThreshold(new Double(configurationService.getConfiguration().getDouble(
				"b2bcustomer.orderthreshold.amount")));
		if (greenleeB2BCustomerModel.getPermissions() != null)
		{
			final Set permissions = getAllPermissionsAsSet(greenleeB2BCustomerModel);
			permissions.add(orderThresholdPermission);
			greenleeB2BCustomerModel.setPermissions(permissions);

		}
		else
		{
			greenleeB2BCustomerModel.setPermissions(new HashSet(Arrays.asList(new B2BPermissionModel[]
			{ orderThresholdPermission })));
		}
	}

	private void createDummyBudgetExceededPermission(final GreenleeB2BCustomerModel greenleeB2BCustomerModel,
			final B2BUnitModel b2bUnitModel, final GreenleeB2BPermissions permissionType)
	{
		final B2BBudgetExceededPermissionModel budgetPermission = modelService.create(B2BBudgetExceededPermissionModel.class);
		budgetPermission.setActive(true);
		budgetPermission.setCode(getPermissionNameForUser(greenleeB2BCustomerModel, b2bUnitModel, permissionType));
		budgetPermission.setUnit(b2bUnitModel);
		if (greenleeB2BCustomerModel.getPermissions() != null)
		{
			final Set permissions = getAllPermissionsAsSet(greenleeB2BCustomerModel);
			permissions.add(budgetPermission);
			greenleeB2BCustomerModel.setPermissions(permissions);

		}
		else
		{
			greenleeB2BCustomerModel.setPermissions(new HashSet(Arrays.asList(new B2BPermissionModel[]
			{ budgetPermission })));
		}
	}



	@Override
	public boolean hasB2BPermissions(final GreenleeB2BCustomerModel greenleeB2BCustomerModel)
	{
		//LOG.info("Checking permissions for " + greenleeB2BCustomerModel.getUid());
		if (greenleeB2BCustomerModel.getPermissions() == null || greenleeB2BCustomerModel.getPermissions().isEmpty()
				|| greenleeB2BCustomerModel.getPermissions().size() == 0)
		{
			//LOG.info("No permissions available for " + greenleeB2BCustomerModel.getUid() + " permissions:"
			//+ greenleeB2BCustomerModel.getPermissions());
			return false;
		}
		else if (greenleeB2BCustomerModel.getPermissions().size() > 0)
		{
			//LOG.info(greenleeB2BCustomerModel.getPermissions().size() + " Permissions already available for user"
			//	+ greenleeB2BCustomerModel.getUid());
			return checkPermissionInAllGroups(greenleeB2BCustomerModel);
		}
		return false;
	}

	private boolean checkPermissionInAllGroups(final GreenleeB2BCustomerModel greenleeB2BCustomerModel)
	{
		return checkGroupPermissions(greenleeB2BCustomerModel) || checkAllowedAccountPermissions(greenleeB2BCustomerModel);
	}

	private boolean checkGroupPermissions(final GreenleeB2BCustomerModel greenleeB2BCustomerModel)
	{
		boolean hasB2BUnitPermissions = false;
		if (greenleeB2BCustomerModel != null && greenleeB2BCustomerModel.getAllGroups() != null)
		{
			for (final PrincipalGroupModel b2bUnit : greenleeB2BCustomerModel.getAllGroups())
			{
				if (b2bUnit instanceof B2BUnitModel)
				{
					final List<B2BPermissionModel> permissions = getAllPermissionsForB2BUnit(greenleeB2BCustomerModel,
							(B2BUnitModel) b2bUnit);
					if (permissions != null && !permissions.isEmpty())
					{
						hasB2BUnitPermissions = true;
					}
					else
					{
						hasB2BUnitPermissions = false;
					}
				}
			}
		}
		//	LOG.info("checkGroupPermissions return value:" + hasB2BUnitPermissions);
		return hasB2BUnitPermissions;
	}

	private boolean checkAllowedAccountPermissions(final GreenleeB2BCustomerModel greenleeB2BCustomerModel)
	{
		boolean hasB2BUnitPermissions = false;
		if (greenleeB2BCustomerModel != null && greenleeB2BCustomerModel.getAllowedAccounts() != null)
		{
			for (final PrincipalGroupModel b2bUnit : greenleeB2BCustomerModel.getAllowedAccounts())
			{
				if (b2bUnit instanceof B2BUnitModel)
				{
					final List<B2BPermissionModel> permissions = getAllPermissionsForB2BUnit(greenleeB2BCustomerModel,
							(B2BUnitModel) b2bUnit);
					if (permissions != null && !permissions.isEmpty())
					{
						hasB2BUnitPermissions = true;
					}
					else
					{
						hasB2BUnitPermissions = false;
					}
				}
			}
		}
		//LOG.info("checkAllowedAccountPermissions return value:" + hasB2BUnitPermissions);
		return hasB2BUnitPermissions;
	}

	@Override
	public List<B2BPermissionModel> getAllPermissionsForB2BUnit(final GreenleeB2BCustomerModel greenleeB2BCustomerModel,
			final B2BUnitModel b2bUnitModel)
	{
		final List<B2BPermissionModel> retVal = new ArrayList<>();
		for (final GreenleeB2BPermissions permission : GreenleeB2BPermissions.values())
		{
			final B2BPermissionModel model = getB2BPermissionModel(greenleeB2BCustomerModel, b2bUnitModel, permission);
			if (model != null)
			{
				//	LOG.info("permission exists " + model.getCode() + " of user " + greenleeB2BCustomerModel.getUid());
				retVal.add(model);
			}
		}
		return retVal.isEmpty() ? null : retVal;
	}

	@Override
	public B2BPermissionModel getB2BPermissionModel(final GreenleeB2BCustomerModel greenleeB2BCustomerModel,
			final B2BUnitModel b2bUnitModel, final GreenleeB2BPermissions permissionType)
	{
		if (greenleeB2BCustomerModel.getPermissions() != null)
		{
			for (final B2BPermissionModel permission : greenleeB2BCustomerModel.getPermissions())
			{
				//	LOG.info("Getting permission " + permission.getCode() + " of user " + greenleeB2BCustomerModel.getUid());
				if (permission.getCode().equals(getPermissionNameForUser(greenleeB2BCustomerModel, b2bUnitModel, permissionType)))
				{
					return permission;
				}
			}
		}
		return null;
	}

	@Override
	public String getPermissionNameForUser(final GreenleeB2BCustomerModel greenleeB2BCustomerModel,
			final B2BUnitModel b2bUnitModel, final GreenleeB2BPermissions permissionType)
	{
		return b2bUnitModel.getUid() + permissionType.getPermissionName() + greenleeB2BCustomerModel.getOriginalUid();
	}

	@Override
	public Set<B2BPermissionModel> getAllPermissionsAsSet(final GreenleeB2BCustomerModel greenleeB2BCustomerModel)
	{
		final Set<B2BPermissionModel> permissions = new HashSet<>();
		for (final B2BPermissionModel permission : greenleeB2BCustomerModel.getPermissions())
		{
			permissions.add(permission);
		}
		return permissions;

	}

}

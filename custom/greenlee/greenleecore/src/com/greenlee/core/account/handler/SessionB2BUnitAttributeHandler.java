/**
 *
 */
package com.greenlee.core.account.handler;

import de.hybris.platform.b2b.company.B2BCommerceUnitService;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.model.B2BUserGroupModel;

import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.greenlee.core.constants.GreenleeCoreConstants;
import com.greenlee.core.enums.UserTypes;
import com.greenlee.core.model.GreenleeB2BCustomerModel;
import com.greenlee.core.unit.services.GreenleeUnitService;


/**
 * The SessionB2BUnit dynamic attribute will be accessed from the Jalo session value. It will also be mutated to the
 * Jalo Session value.
 *
 *
 * @author raja.santhanam
 *
 */
public class SessionB2BUnitAttributeHandler implements DynamicAttributeHandler<B2BUnitModel, GreenleeB2BCustomerModel>
{

	private static final String DUMMY_DISTRIBUTOR_B2BUNIT = "greenlee.account.dummy.distributor.uid";
	private static final String SALESCONSULTANT_GROUP = "greenlee.account.group.salesconsultant.uid";
	private static final String SALESEMPLOYEE_GROUP = "greenlee.account.group.salesemployee.uid";

	private SessionService sessionService;
	private ConfigurationService configService;
	private B2BCommerceUnitService b2bUnitService;
	private GreenleeUnitService unitService;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler#get(de.hybris.platform.servicelayer.model.
	 * AbstractItemModel)
	 */
	@Override
	public B2BUnitModel get(final GreenleeB2BCustomerModel item)
	{
		B2BUnitModel b2bUnit = null;
		final Map<String, Object> allAttributes = sessionService.getAllAttributes();
		if (allAttributes != null)
		{
			b2bUnit = (B2BUnitModel) allAttributes.get(GreenleeCoreConstants.SESSION_B2BUNIT_KEY);

			//Check if the b2bunit is assignable to the user before setting the b2bunit.
			// This can happen, when remember me is selected during login and during a second login to access a secured page, if a different user id is entered.
			// The above situation will result in a sessionB2BUnit hijack.
			if (b2bUnit != null && !isSwitchable(item, b2bUnit))
			{
				b2bUnit = null;
			}
		}
		return b2bUnit;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler#set(de.hybris.platform.servicelayer.model.
	 * AbstractItemModel, java.lang.Object)
	 */
	@Override
	public void set(final GreenleeB2BCustomerModel item, final B2BUnitModel value)
	{
		final Session currentSession = sessionService.getCurrentSession();
		if (currentSession != null && value != null && isSwitchable(item, value))
		{
			currentSession.setAttribute(GreenleeCoreConstants.SESSION_B2BUNIT_KEY, value);
		}
		else
		{
			currentSession.removeAttribute(GreenleeCoreConstants.SESSION_B2BUNIT_KEY);
		}
	}

	/**
	 * @return the sessionService
	 */
	public SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}


	private boolean isSwitchable(final GreenleeB2BCustomerModel customer, final B2BUnitModel b2bUnit)
	{
		final List<B2BUnitModel> switchableUnits = getSwitchableB2BUnits(customer);
		for (final B2BUnitModel switchableUnit : switchableUnits)
		{
			if (StringUtils.equalsIgnoreCase(switchableUnit.getUid(), b2bUnit.getUid()))
			{
				return true;
			}
		}
		return false;
	}


	private List<B2BUnitModel> getSwitchableB2BUnits(final GreenleeB2BCustomerModel customer)
	{

		final List<B2BUnitModel> switchableB2BUnits = new ArrayList<>();
		final B2BUnitModel parentUnit = b2bUnitService.getParentUnit();

		//If the user is a B2C/B2E user, then just send him back the parent unit.
		if (UserTypes.B2C.equals(parentUnit.getUserType()) || UserTypes.B2E.equals(parentUnit.getUserType()))
		{
			switchableB2BUnits.add(parentUnit);
		}

		final String dummyDistributorB2BUnitUid = configService.getConfiguration().getString(DUMMY_DISTRIBUTOR_B2BUNIT);

		if (StringUtils.equalsIgnoreCase(dummyDistributorB2BUnitUid, parentUnit.getUid()))
		{
			//The user is either a SalesRep or an Employee.
			if (customer.getAllowedAccounts().isEmpty() && ifUserBelongsToUserGroup(customer, SALESEMPLOYEE_GROUP))
			{
				return getAllSecondaryUnitsOfOrganization();
			}
			else
			{
				//User is a SalesRep
				if (ifUserBelongsToUserGroup(customer, SALESCONSULTANT_GROUP))
				{
					final Collection<B2BUnitModel> allowedAccounts = customer.getAllowedAccounts();
					switchableB2BUnits.addAll(getActiveB2BUnits(allowedAccounts));
				}
			}
		}
		else
		{
			final Collection<B2BUnitModel> relatedAccounts = getActiveB2BUnits(customer.getAllGroups());

			final Collection<B2BUnitModel> completeB2BUnitsAvailableForSwitching = getSecondaryB2BUnitsOnly(relatedAccounts);
			switchableB2BUnits.addAll(completeB2BUnitsAvailableForSwitching);
		}

		return switchableB2BUnits;
	}

	private Collection<B2BUnitModel> getSecondaryB2BUnitsOnly(final Collection<B2BUnitModel> customerAllGroups)
	{
		final Set<B2BUnitModel> uniqueSecondaryLevelB2BUnit = new HashSet<>();
		for (final B2BUnitModel b2bUnitModel : customerAllGroups)
		{
			if (b2bUnitModel.getGroups() == null || b2bUnitModel.getGroups().isEmpty())
			{
				//This indicates this a Level1 Group.
				//Need to filter only B2BUnits from groups of Level1.
				uniqueSecondaryLevelB2BUnit.addAll(getActiveB2BUnits(b2bUnitModel.getMembers()));
			}
			else
			{
				// If not null, it indicates secondary level2 B2Bunit
				uniqueSecondaryLevelB2BUnit.add(b2bUnitModel);
			}
		}
		return uniqueSecondaryLevelB2BUnit;
	}

	public List<B2BUnitModel> getAllSecondaryUnitsOfOrganization()
	{
		final Collection<? extends B2BUnitModel> b2bUnits = unitService.getAllSecondaryB2BUnits();
		final List<B2BUnitModel> activeB2BUnits = getActiveB2BUnits(b2bUnits);
		return activeB2BUnits;
	}

	private List<B2BUnitModel> getActiveB2BUnits(final Collection<? extends PrincipalModel> b2bUnits)
	{
		final List<B2BUnitModel> activeB2BUnits = new ArrayList<>(b2bUnits.size());
		for (final PrincipalModel groupModel : b2bUnits)
		{
			if (groupModel instanceof B2BUnitModel)
			{
				final B2BUnitModel model = (B2BUnitModel) groupModel;
				if (model.getActive().booleanValue())
				{
					activeB2BUnits.add(model);
				}
			}
		}
		return activeB2BUnits;
	}

	private boolean ifUserBelongsToUserGroup(final GreenleeB2BCustomerModel customer, final String userGroupUid)
	{
		for (final PrincipalModel model : customer.getAllGroups())
		{
			if (model instanceof B2BUserGroupModel)
			{
				return StringUtils.equalsIgnoreCase(configService.getConfiguration().getString(userGroupUid), model.getUid());
			}
		}
		return false;
	}

	/**
	 * @return the configService
	 */
	public ConfigurationService getConfigService()
	{
		return configService;
	}

	/**
	 * @param configService
	 *           the configService to set
	 */
	public void setConfigService(final ConfigurationService configService)
	{
		this.configService = configService;
	}

	/**
	 * @return the b2bUnitService
	 */
	public B2BCommerceUnitService getB2bUnitService()
	{
		return b2bUnitService;
	}

	/**
	 * @param b2bUnitService
	 *           the b2bUnitService to set
	 */
	public void setB2bUnitService(final B2BCommerceUnitService b2bUnitService)
	{
		this.b2bUnitService = b2bUnitService;
	}

	/**
	 * @return the unitService
	 */
	public GreenleeUnitService getUnitService()
	{
		return unitService;
	}

	/**
	 * @param unitService
	 *           the unitService to set
	 */
	public void setUnitService(final GreenleeUnitService unitService)
	{
		this.unitService = unitService;
	}

}

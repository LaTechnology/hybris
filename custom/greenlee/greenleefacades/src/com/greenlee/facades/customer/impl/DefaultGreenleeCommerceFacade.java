/**
 *
 */
package com.greenlee.facades.customer.impl;

import de.hybris.platform.b2b.company.B2BCommerceUnitService;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.model.B2BUserGroupModel;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.commercefacades.user.data.PrincipalData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.greenlee.core.constants.GreenleeCoreConstants;
import com.greenlee.core.model.GreenleeB2BCustomerModel;
import com.greenlee.core.unit.services.GreenleeUnitService;
import com.greenlee.core.util.CommonUtils;
import com.greenlee.facades.customer.GreenleeCommerceFacade;


/**
 * Default implementation for GreenleeCommerceFacade
 *
 * @author raja.santhanam
 *
 */
public class DefaultGreenleeCommerceFacade implements GreenleeCommerceFacade
{
    private static final Logger                  LOG                       = Logger.getLogger(DefaultGreenleeCommerceFacade.class);
    private static final String                  DUMMY_DISTRIBUTOR_B2BUNIT = "greenlee.account.dummy.distributor.uid";
    private static final String                  SALESCONSULTANT_GROUP     = "greenlee.account.group.salesconsultant.uid";
    private static final String                  SALESEMPLOYEE_GROUP       = "greenlee.account.group.salesemployee.uid";
    private static final String                  B2ECUST="greenlee.account.dummy.distributor.b2e.uid";
    private static final String                  B2CCUST="greenlee.account.dummy.distributor.b2c.uid";
    private static final String                  B2ECUST_STR               = "B2ECUST";
    private static final String                  B2CCUST_STR               = "B2CCUST";
    private static final String                  dummydistributor          = "dummydistributor";
    private static final String                  cad_dummydistributor      = "dummydistributor_CAD";
    private static final String                  dummydistributor_CAD      = "greenlee.account.dummy.distributor.uid.cad";
    private static final String                  B2ECUST_CAD               = "greenlee.account.dummy.distributor.b2e.uid.cad";
    private static final String                  B2CCUST_CAD               = "greenlee.account.dummy.distributor.b2c.uid.cad";
    private static final String                  B2ECUST_CAD_STR           = "B2ECUST_CAD";
    private static final String                  B2CCUST_CAD_STR           = "B2CCUST_CAD";
    private B2BCommerceUnitService b2bUnitService;
    private Converter<B2BUnitModel, B2BUnitData> b2bUnitConverter;
    private ConfigurationService                 configService;
    private UserService                          userService;
    private GreenleeUnitService                  unitService;
    private FlexibleSearchService                flexibleSearchService;
    private SessionService                       sessionService;

    /*
     * (non-Javadoc)
     *
     * @see com.greenlee.facades.customer.GreenleeCommerceFacade#getAllActiveUnitsOfOrganization()
     */
    @Override
    public List<B2BUnitData> getAllSecondaryUnitsOfOrganization()
    {
        final Collection<? extends B2BUnitModel> b2bUnits = unitService.getAllSecondaryB2BUnits();
        LOG.info(CommonUtils.toStringFormatter("Size of B2BUnits ", " : " + b2bUnits.size()));
        final List<B2BUnitModel> activeB2BUnits = getActiveB2BUnits(b2bUnits);
        return Converters.convertAll(activeB2BUnits, b2bUnitConverter);
    }

    /**
     * @param b2bUnits
     */
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
        LOG.info(CommonUtils.toStringFormatter("Size of active B2BUnits", ":" + activeB2BUnits.size()));
        return activeB2BUnits;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.greenlee.facades.customer.GreenleeCommerceFacade#getSwitchableUnitsOfOrganization(java.lang.String)
     */
    @Override
    public List<B2BUnitData> getSwitchableUnitsOfOrganization(final GreenleeB2BCustomerModel customer)
    {

        final List<B2BUnitModel> switchableB2BUnits = new ArrayList<>();
        final B2BUnitModel parentUnit = b2bUnitService.getParentUnit();
        LOG.info("B2BUnitModel ID :" + parentUnit.getUid());
        final String b2eBaseReference = Config.getString(B2ECUST, B2ECUST_STR);
        final String b2cBaseReference = Config.getString(B2CCUST, B2CCUST_STR);
        final String b2eCadBaseReference = Config.getString(B2ECUST_CAD, B2ECUST_CAD_STR);
        final String b2cCadBaseReference = Config.getString(B2CCUST_CAD, B2CCUST_CAD_STR);
        //GSM-73
        if (StringUtils.equalsIgnoreCase(b2eBaseReference, parentUnit.getUid())
                || StringUtils.equalsIgnoreCase(b2cBaseReference, parentUnit.getUid())
                || StringUtils.equalsIgnoreCase(b2eCadBaseReference, parentUnit.getUid())
                || StringUtils.equalsIgnoreCase(b2cCadBaseReference, parentUnit.getUid()))
        {
            LOG.error("Inside the BaseReference B2BUnitModel ID :" + parentUnit.getUid());
            if (null != customer.getDefaultB2BUnit())
            {
                LOG.error("Inside the BaseReference getDefaultB2BUnit ID :" + customer.getDefaultB2BUnit().getUid());
                switchableB2BUnits.add(customer.getDefaultB2BUnit());
            }
            else
            {
                LOG.error("Else the BaseReference B2BUnitModel ID :" + parentUnit.getUid());
                switchableB2BUnits.add(parentUnit);
            }
            return Converters.convertAll(switchableB2BUnits, b2bUnitConverter);
        }
        /*   GSM-73
         *   if (UserTypes.B2C.equals(parentUnit.getUserType()) || UserTypes.B2E.equals(parentUnit.getUserType()))
             {
                 switchableB2BUnits.add(parentUnit);
                 return Converters.convertAll(switchableB2BUnits, b2bUnitConverter);
             }*/
        final String dummyDistributorB2BUnitUid = Config.getString(DUMMY_DISTRIBUTOR_B2BUNIT, dummydistributor);
        final String dummyDistributorB2BUnitUidCad = Config.getString(dummydistributor_CAD, cad_dummydistributor);

        LOG.error("DUMMY_DISTRIBUTOR_B2BUNIT >> " + dummyDistributorB2BUnitUid + "  ::  "
                + StringUtils.equalsIgnoreCase(dummyDistributorB2BUnitUid, parentUnit.getUid()));
        if (StringUtils.equalsIgnoreCase(dummyDistributorB2BUnitUid, parentUnit.getUid())
                || StringUtils.equalsIgnoreCase(dummyDistributorB2BUnitUidCad, parentUnit.getUid()))
        {
            //The user is either a SalesRep or an Employee.
            if (customer.getAllowedAccounts().isEmpty() && ifUserBelongsToUserGroup(customer, SALESEMPLOYEE_GROUP))
            {
                LOG.error("SALESEMPLOYEE_GROUP >> ");
                return getAllSecondaryUnitsOfOrganization();
            }
            else
            {
                //User is a SalesRep
                if (ifUserBelongsToUserGroup(customer, SALESCONSULTANT_GROUP))
                {
                    LOG.error("SALESCONSULTANT_GROUP >> ");
                    final Collection<B2BUnitModel> allowedAccounts = customer.getAllowedAccounts();
                    switchableB2BUnits.addAll(getActiveB2BUnits(allowedAccounts));
                }
            }
        }
        else
        {
            LOG.error("The Real SAP B2bUnits ");
            final Collection<B2BUnitModel> relatedAccounts = getActiveB2BUnits(customer.getAllGroups());
            for (final B2BUnitModel b2bUnitModel : relatedAccounts)
            {
                LOG.error("The Real SAP B2bUnits Collection<B2BUnitModel> relatedAccounts :" + b2bUnitModel.getUid());
            }
            final Collection<B2BUnitModel> completeB2BUnitsAvailableForSwitching = getSecondaryB2BUnitsOnly(relatedAccounts);
            for (final B2BUnitModel b2bUnitModel : completeB2BUnitsAvailableForSwitching)
            {
                LOG.error("completeB2BUnitsAvailableForSwitching B2BUnitModel ID :" + b2bUnitModel.getUid());
            }
            switchableB2BUnits.addAll(completeB2BUnitsAvailableForSwitching);
        }

        final Session currentSession = sessionService.getCurrentSession();
        if (currentSession != null)
        {
            currentSession.setAttribute(GreenleeCoreConstants.SWITCHABLE_SESSION_B2BUNIT_KEY, switchableB2BUnits);
        }
        LOG.info("switchableB2BUnits " + switchableB2BUnits.size());
        return Converters.convertAll(switchableB2BUnits, b2bUnitConverter);
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
     * @return the b2bUnitService
     */
    public B2BCommerceUnitService getB2bUnitService()
    {
        return b2bUnitService;
    }

    /**
     * @param b2bUnitService
     *            the b2bUnitService to set
     */
    public void setB2bUnitService(final B2BCommerceUnitService b2bUnitService)
    {
        this.b2bUnitService = b2bUnitService;
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
     *            the configService to set
     */
    public void setConfigService(final ConfigurationService configService)
    {
        this.configService = configService;
    }

    /**
     * @return the userService
     */
    public UserService getUserService()
    {
        return userService;
    }

    /**
     * @param userService
     *            the userService to set
     */
    public void setUserService(final UserService userService)
    {
        this.userService = userService;
    }

    /**
     * @return the b2bUnitConverter
     */
    public Converter<B2BUnitModel, B2BUnitData> getB2bUnitConverter()
    {
        return b2bUnitConverter;
    }

    /**
     * @param b2bUnitConverter
     *            the b2bUnitConverter to set
     */
    public void setB2bUnitConverter(final Converter<B2BUnitModel, B2BUnitData> b2bUnitConverter)
    {
        this.b2bUnitConverter = b2bUnitConverter;
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
     *            the unitService to set
     */
    public void setUnitService(final GreenleeUnitService unitService)
    {
        this.unitService = unitService;
    }

    /**
     * @return the flexibleSearchService
     */
    public FlexibleSearchService getFlexibleSearchService()
    {
        return flexibleSearchService;
    }

    /**
     * @param flexibleSearchService
     *            the flexibleSearchService to set
     */
    public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
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
     *            the sessionService to set
     */
    public void setSessionService(final SessionService sessionService)
    {
        this.sessionService = sessionService;
    }

    @Override
    public Set<PrincipalData> getMembersToBeEmailed(final GreenleeB2BCustomerModel customer)
    {
        Set<PrincipalData> uniqueMembers = null;
        final List<B2BUnitData> switchableB2BUnitsList = getSwitchableUnitsOfOrganization(customer);
        final String b2eBaseReference = Config.getString(B2ECUST, "B2ECUST");
        final String b2cBaseReference = Config.getString(B2CCUST, "B2CCUST");
        final String b2eCadBaseReference = Config.getString(B2ECUST_CAD, "B2ECUST_CAD");
        final String b2cCadBaseReference = Config.getString(B2CCUST_CAD, "B2CCUST_CAD");
        for (final B2BUnitData b2bUnit : switchableB2BUnitsList)
        {
            if (StringUtils.equalsIgnoreCase(b2eBaseReference, b2bUnit.getUid())
                    || StringUtils.equalsIgnoreCase(b2cBaseReference, b2bUnit.getUid())
                    || StringUtils.equalsIgnoreCase(b2eCadBaseReference, b2bUnit.getUid())
                    || StringUtils.equalsIgnoreCase(b2cCadBaseReference, b2bUnit.getUid()))
            {
                uniqueMembers = new HashSet<PrincipalData>();
                LOG.info("getMembersToBeEmailed " + uniqueMembers.size());
                LOG.info("GetMembersToBeEmailed for the customer " + customer.getUid() + " is " + uniqueMembers.size());
                return uniqueMembers;
            }
            else
            {
                uniqueMembers = new HashSet<PrincipalData>();
                uniqueMembers.addAll(b2bUnit.getMembers());
                LOG.info("Get All Members other than B2E/B2C Cust that needs to be Emailed for the customer " + customer.getUid()
                        + " is " + uniqueMembers.size());
                return uniqueMembers;
            }
        }
        return uniqueMembers;
    }
}

/**
 *
 */
package com.greenlee.facades.costcenter.impl;

import de.hybris.platform.b2b.model.B2BCostCenterModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCostCenterService;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.b2bcommercefacades.company.data.B2BCostCenterData;
import de.hybris.platform.b2bcommercefacades.company.impl.DefaultB2BCostCenterFacade;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.greenlee.facades.costcenter.GreenleeB2BCostCenterFacade;


/**
 * @author savarimuthu.s
 *
 */
public class DefaultGreenleeB2BCostCenterFacade extends DefaultB2BCostCenterFacade implements GreenleeB2BCostCenterFacade

{
    @Resource(name = "b2bCostCenterService")
    private B2BCostCenterService<B2BCostCenterModel, B2BCustomerModel> b2bCostCenterService;
    @Resource(name = "b2bCostCenterConverter")
    private Converter<B2BCostCenterModel, B2BCostCenterData>           b2bCostCenterConverter;

    private B2BUnitService<B2BUnitModel, B2BCustomerModel>             b2bUnitService;

    protected static final Logger                                      LOG = Logger.getLogger(DefaultGreenleeB2BCostCenterFacade.class);



    /**
     * @return the b2bUnitService
     */
    public B2BUnitService<B2BUnitModel, B2BCustomerModel> getB2bUnitService()
    {
        return b2bUnitService;
    }



    /**
     * @param b2bUnitService
     *            the b2bUnitService to set
     */
    @Required
    public void setB2bUnitService(final B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService)
    {
        this.b2bUnitService = b2bUnitService;
    }



    /*
     * (non-Javadoc)
     *
     * @see com.hybris.platform.facade.GreenleeB2BCostCenterFacade#getGreenleeCostCenter(java.lang.String)
     */
    @Override
    public B2BCostCenterData getGreenleeB2BUnitCostCenters(final String cartId, final String b2bUnitId) throws Exception
    {
        Assert.notNull(b2bUnitId, "B2B Unit [ " + b2bUnitId + " does not have a null value, for the card Id [ " + cartId + " ]");
        final B2BCostCenterData costCenterData = new B2BCostCenterData();
        try
        {
            final B2BUnitModel parentUnit = getB2bUnitService().getUnitForUid(b2bUnitId);
            LOG.error("Loading user B2B Unit [ " + b2bUnitId + " for the card Id [ " + cartId + " ] Total Cost Center ");
            final List<B2BCostCenterModel> b2bCostCenterModels = b2bCostCenterService.getCostCentersForUnitBranch(parentUnit,
                    parentUnit.getCurrencyPreference());
            LOG.error("B2B Unit [ " + b2bUnitId + " for the card Id [ " + cartId + " ] Total Cost Center found "
                    + b2bCostCenterModels.size());
            if (CollectionUtils.isNotEmpty(b2bCostCenterModels))
            {
                List<B2BCostCenterData> costCenterDatas = new ArrayList<B2BCostCenterData>();
                try
                {
                    LOG.error("Choosed best Cost center and Conversion started >> List<B2BCostCenterData> Unit [ " + b2bUnitId
                            + " for the card Id [ " + cartId);
                    costCenterDatas = Converters.convertAll(b2bCostCenterModels, b2bCostCenterConverter);
                }
                catch (final Exception exception)
                {
                    LOG.error("ERR_NTFY_SUPPORT_000600 -  B2B Unit [ " + b2bUnitId
                            + " does not have a cost center. for the card Id [ " + cartId + " ] Total Cost Center was "
                            + b2bCostCenterModels.size() + "Conversion Error " + exception);
                }

                LOG.error("Conversion completed ");
                if (CollectionUtils.isNotEmpty(costCenterDatas))
                {
                    LOG.error("Select the best cost center for the unit >> List<B2BCostCenterData> Unit [ " + b2bUnitId
                            + " for the card Id [ " + cartId + " ]");
                    for (final B2BCostCenterData b2bCostCenterData : costCenterDatas)
                    {
                        LOG.error("Select the best cost center for the unit >> List<B2BCostCenterData> Unit [ " + b2bUnitId
                                + " for the card Id [ " + cartId + " ] and status was :"
                                + StringUtils.equalsIgnoreCase(parentUnit.getUid(), b2bCostCenterData.getUnit().getUid()));
                        if (b2bCostCenterData.getUnit() != null
                                && StringUtils.equalsIgnoreCase(parentUnit.getUid(), b2bCostCenterData.getUnit().getUid()))
                        {
                            LOG.error("Selected Cost center id was " + b2bCostCenterData.getCode() + " Cost Center Name "
                                    + b2bCostCenterData.getName() + " for the unit " + b2bUnitId + " for the card Id [ " + cartId
                                    + " ] and status was :"
                                    + StringUtils.equalsIgnoreCase(parentUnit.getUid(), b2bCostCenterData.getUnit().getUid()));
                            return b2bCostCenterData;
                        }
                    }
                }
            }
            if (CollectionUtils.isEmpty(b2bCostCenterModels))
            {
                LOG.error("ERR_NTFY_SUPPORT_000601 B2B Unit [ " + b2bUnitId + " does not have a cost center. for the card Id [ "
                        + cartId + " ]");
                throw new Exception("B2B Unit [ " + b2bUnitId + " does not have a cost center. for the card Id [ " + cartId
                        + " ]");
            }
        }
        catch (final Exception exception)
        {
            LOG.error("ERR_NTFY_SUPPORT_000602 B2B Unit [ " + b2bUnitId + " does not have a cost center. for the card Id [ "
                    + cartId + " ]");
            throw exception;
        }
        return costCenterData;
    }

}

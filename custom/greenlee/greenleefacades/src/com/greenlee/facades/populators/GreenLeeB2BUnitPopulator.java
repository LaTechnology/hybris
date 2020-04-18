/**
 *
 */
package com.greenlee.facades.populators;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bacceleratorfacades.order.populators.B2BUnitPopulator;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.converters.Converters;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;

import com.greenlee.core.model.GreenleeB2BCustomerModel;


/**
 * @author peter.asirvatham
 *
 */
public class GreenLeeB2BUnitPopulator extends B2BUnitPopulator

{

    @Override
    public void populate(final B2BUnitModel source, final B2BUnitData target)
    {
        target.setName(source.getLocName());
        target.setUid(source.getUid());
        target.setActive(Boolean.TRUE.equals(source.getActive()));
        if (source.getContactAddress() != null)
        {
            target.setContactAddress(getAddressConverter().convert(source.getContactAddress()));
        }
        if (source.getBillingAddress() != null)
        {
            target.setBillingAddress(getAddressConverter().convert(source.getBillingAddress()));
        }

        if (source.getShippingAddress() != null)
        {
            target.setShippingAddress(getAddressConverter().convert(source.getShippingAddress()));
        }

        target.setUserType(source.getUserType().getCode());

        // unit addresses
        if (CollectionUtils.isNotEmpty(source.getAddresses()))
        {
            target.setAddresses(Converters.convertAll(source.getAddresses(), getAddressConverter()));
        }

        target.setMembers(Converters.convertAll(
                CollectionUtils.select(source.getMembers(), PredicateUtils.instanceofPredicate(GreenleeB2BCustomerModel.class)),
                getPrincipalConverter()));
    }

}

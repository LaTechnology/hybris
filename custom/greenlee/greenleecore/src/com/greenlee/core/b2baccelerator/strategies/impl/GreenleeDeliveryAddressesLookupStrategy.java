/**
 *
 */
package com.greenlee.core.b2baccelerator.strategies.impl;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bacceleratorservices.strategies.impl.DefaultB2BDeliveryAddressesLookupStrategy;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.AddressModel;

import java.util.ArrayList;
import java.util.List;


/**
 * @author raja.santhanam
 *
 */
public class GreenleeDeliveryAddressesLookupStrategy extends DefaultB2BDeliveryAddressesLookupStrategy
{

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.commerceservices.strategies.DeliveryAddressesLookupStrategy#getDeliveryAddressesForOrder(de.
	 * hybris.platform.core.model.order.AbstractOrderModel, boolean)
	 */
	@Override
	public List<AddressModel> getDeliveryAddressesForOrder(final AbstractOrderModel abstractOrder,
			final boolean visibleAddressesOnly)
	{
		final List<AddressModel> deliveryAddresses = new ArrayList<>();
		/*
		 * final B2BCostCenterModel costCenter = getCostCenterForOrder(abstractOrder); if (costCenter != null) { final
		 * Set<AddressModel> addresses = collectAddressesForCostCenter(costCenter); if (addresses != null &&
		 * !addresses.isEmpty()) { sortAddresses(addresses); } deliveryAddresses.addAll(addresses); }
		 */

		final B2BUnitModel unitModel = abstractOrder.getUnit();
		deliveryAddresses.addAll(
				getFallbackDeliveryAddressesLookupStrategy().getDeliveryAddressesForOrder(abstractOrder, visibleAddressesOnly));
		if (unitModel != null)
		{
			final List<AddressModel> addressModelList = new ArrayList<AddressModel>(unitModel.getAddresses());
			if (!addressModelList.isEmpty())
			{
				for (final AddressModel b2bUnitBillingAddressModel : addressModelList)
				{
					if (b2bUnitBillingAddressModel.getBillingAddress() != null
							&& b2bUnitBillingAddressModel.getBillingAddress().booleanValue())
					{
						deliveryAddresses.add(b2bUnitBillingAddressModel);
					}
				}
			}

			//deliveryAddresses.addAll(unitModel.getAddresses());
		}
		return deliveryAddresses;
	}

}

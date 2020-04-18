/**
 *
 */
package com.greenlee.core.cart.strategies.impl;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.commerceservices.strategies.impl.DefaultCartValidationStrategy;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.strategies.calculation.FindPriceStrategy;
import de.hybris.platform.util.PriceValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import com.greenlee.core.enums.UserTypes;
import com.greenlee.core.model.GreenleeProductModel;


/**
 * @author raja.santhanam
 *
 */
public class GreenleeCartValidationStrategy extends DefaultCartValidationStrategy
{

	private FindPriceStrategy findPriceStrategy;

	@Override
	protected Long getStockLevel(final CartEntryModel cartEntryModel)
	{
		// Equivalent to force in stock.
		return new Long(Long.MAX_VALUE);
	}

	@Override
	protected void validateDelivery(final CartModel cartModel)
	{
		// Overriding the OOTB behaviour.
		if (cartModel.getDeliveryAddress() != null)
		{
			if (!isGuestUserCart(cartModel) && !isValidAddress(cartModel))
			{
				cartModel.setDeliveryAddress(null);
				getModelService().save(cartModel);
			}
		}
	}

	private boolean isValidAddress(final CartModel cartModel)
	{
		return getUserService().getCurrentUser().equals(cartModel.getDeliveryAddress().getOwner())
				|| cartModel.getUnit().equals(cartModel.getDeliveryAddress().getOwner());
	}

	protected PriceValue findBasePrice(final AbstractOrderEntryModel entry) throws CalculationException
	{
		return findPriceStrategy.findBasePrice(entry);
	}

	@Required
	public void setFindPriceStrategy(final FindPriceStrategy findPriceStrategy)
	{
		this.findPriceStrategy = findPriceStrategy;
	}


	@Override
	protected CommerceCartModification validateCartEntry(final CartModel cartModel, final CartEntryModel cartEntryModel)
	{


		CommerceCartModification modification = checkIfSellableToCustomer(cartModel, cartEntryModel);

		// If no modificaiton from here, then check if super has any modifications.
		if (modification == null)
		{
			modification = checkIfProductHasPrice(cartModel, cartEntryModel);
		}
		if (modification == null)
		{
			modification = super.validateCartEntry(cartModel, cartEntryModel);
		}

		return modification;
	}

	private CommerceCartModification checkIfProductHasPrice(final CartModel cartModel, final CartEntryModel cartEntryModel)
	{
		CommerceCartModification modification = null;
		try
		{
			findBasePrice(cartEntryModel);
		}
		catch (final CalculationException e)
		{
			modification = getCartModification(cartEntryModel, cartModel, CommerceCartModificationStatus.UNAVAILABLE);
		}
		return modification;
	}

	/**
	 * @param cartModel
	 * @param cartEntryModel
	 * @return
	 */
	private CommerceCartModification checkIfSellableToCustomer(final CartModel cartModel, final CartEntryModel cartEntryModel)
	{
		CommerceCartModification modification = null;
		final GreenleeProductModel productModel = (GreenleeProductModel) cartEntryModel.getProduct();
		final B2BUnitModel b2bUnit = cartModel.getUnit();

		if (!productModel.getB2bProduct().booleanValue() && b2bUnit != null)
		{
			if (StringUtils.equals(UserTypes.B2B.getCode(), b2bUnit.getUserType().getCode()))
			{
				modification = getCartModification(cartEntryModel, cartModel, CommerceCartModificationStatus.UNAVAILABLE);
			}
		}
		if (!productModel.getB2eProduct().booleanValue() && b2bUnit != null)
		{
			if (StringUtils.equals(UserTypes.B2E.getCode(), b2bUnit.getUserType().getCode()))
			{
				modification = getCartModification(cartEntryModel, cartModel, CommerceCartModificationStatus.UNAVAILABLE);
			}
		}
		if (!productModel.getB2cProduct().booleanValue() && b2bUnit != null)
		{
			if (StringUtils.equals(UserTypes.B2C.getCode(), b2bUnit.getUserType().getCode()))
			{
				modification = getCartModification(cartEntryModel, cartModel, CommerceCartModificationStatus.UNAVAILABLE);
			}
		}
		return modification;
	}

	private CommerceCartModification getCartModification(final CartEntryModel cartEntryModel, final CartModel cartModel,
			final String modificationStatus)
	{
		final CommerceCartModification modification = new CommerceCartModification();
		//		modification.setStatusCode(CommerceCartModificationStatus.UNAVAILABLE);
		modification.setStatusCode(modificationStatus);
		modification.setQuantityAdded(0);
		modification.setQuantity(0);

		final CartEntryModel entry = new CartEntryModel();
		entry.setProduct(cartEntryModel.getProduct());

		modification.setEntry(entry);

		getModelService().remove(cartEntryModel);
		getModelService().refresh(cartModel);
		normalizeEntryNumbers(cartModel);

		return modification;

	}

	private void normalizeEntryNumbers(final CartModel cartModel)
	{
		final List<AbstractOrderEntryModel> entries = new ArrayList<AbstractOrderEntryModel>(cartModel.getEntries());
		Collections.sort(entries, new BeanComparator(AbstractOrderEntryModel.ENTRYNUMBER, new ComparableComparator()));
		for (int i = 0; i < entries.size(); i++)
		{
			entries.get(i).setEntryNumber(Integer.valueOf(i));
			getModelService().save(entries.get(i));
		}
	}

}

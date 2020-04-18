/**
 *
 */
package com.greenlee.facades.cart.hook.impl;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.hook.CommerceUpdateCartEntryHook;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import com.greenlee.core.enums.UserTypes;
import com.greenlee.core.model.GreenleeB2BCustomerModel;
import com.greenlee.pi.service.GreenleePIClientService;
import com.hybris.urn.xi.pricing_enquiry.DTHybrisPricingResponse;
import com.hybris.urn.xi.pricing_enquiry.DTHybrisPricingResponse.ORDERITEMS.ITEMS;


/**
 * @author nalini.ramarao
 *
 */
public class GreenleeUpdateCartEntryMethodHook implements CommerceUpdateCartEntryHook
{
    private ModelService            modelService;

    private GreenleePIClientService greenleePIClientService;

    public GreenleePIClientService getGreenleePIClientService()
    {
        return greenleePIClientService;
    }

    public void setGreenleePIClientService(final GreenleePIClientService greenleePIClientService)
    {
        this.greenleePIClientService = greenleePIClientService;
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
     *            the modelService to set
     */
    public void setModelService(final ModelService modelService)
    {
        this.modelService = modelService;
    }

    private void updateCartModel(final CartModel model, final DTHybrisPricingResponse dtHybrisPricingResponse)
    {
        final List<AbstractOrderEntryModel> abstractOrderEntryModels = model.getEntries();
        if (dtHybrisPricingResponse.getORDERITEMS() != null)
        {
            for (final ITEMS cartItem : dtHybrisPricingResponse.getORDERITEMS().getITEMS())
            {
                final AbstractOrderEntryModel orderEntryModel = abstractOrderEntryModels
                        .get(Integer.parseInt(cartItem.getHybrisItemNo()));
                final double total = Double.parseDouble(cartItem.getSubtotal1());
                final double qty = Double.parseDouble(cartItem.getRequiredQuantity());
                orderEntryModel.setBasePrice(new Double(total / qty));
                orderEntryModel.setTotalPrice(new Double(total));
                modelService.save(orderEntryModel);
            }
        }
        model.setSapPriceAvailability(Boolean.TRUE);
        modelService.save(model);
    }

    /* (non-Javadoc)
     * @see de.hybris.platform.commerceservices.order.hook.CommerceUpdateCartEntryHook#afterUpdateCartEntry(de.hybris.platform.commerceservices.service.data.CommerceCartParameter, de.hybris.platform.commerceservices.order.CommerceCartModification)
     */
    @Override
    public void afterUpdateCartEntry(final CommerceCartParameter parameter, final CommerceCartModification result)
    {
        final CartModel cart = parameter.getCart();

        final UserModel user = cart.getUser();
        if (user instanceof GreenleeB2BCustomerModel)
        {
            final GreenleeB2BCustomerModel customer = (GreenleeB2BCustomerModel) user;
            if (cart.getIsErpPrice().booleanValue() && !UserTypes.B2C.equals(customer.getSessionB2BUnit().getUserType())
                    && !cart.getEntries().isEmpty())
            {
                final DTHybrisPricingResponse dtHybrisPricingResponse = getGreenleePIClientService().callPricingEnquiry(cart,
                        false);

                updateCartModel(cart, dtHybrisPricingResponse);
            }
            else if (cart.getSapPriceAvailability() == null || cart.getSapPriceAvailability().booleanValue())
            {
                cart.setSapPriceAvailability(Boolean.FALSE);

            }
            if (cart.getEntries().isEmpty())
            {
                cart.setSapPriceAvailability(Boolean.FALSE);
                cart.setIsErpPrice(Boolean.FALSE);
                cart.setTotalDuty(new Double(0.0));
                cart.setDeliveryCost(new Double(0.0));
                cart.setTotalDiscounts(new Double(0.0));

            }

            modelService.save(cart);
        }

    }

    /* (non-Javadoc)
     * @see de.hybris.platform.commerceservices.order.hook.CommerceUpdateCartEntryHook#beforeUpdateCartEntry(de.hybris.platform.commerceservices.service.data.CommerceCartParameter)
     */
    @Override
    public void beforeUpdateCartEntry(final CommerceCartParameter parameter)
    {
        // YTODO Auto-generated method stub

    }


}

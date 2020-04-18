package com.greenlee.facades.checkout.impl;

import static de.hybris.platform.util.localization.Localization.getLocalizedString;

import de.hybris.platform.b2bacceleratorfacades.checkout.data.PlaceOrderData;
import de.hybris.platform.b2bacceleratorfacades.exception.EntityValidationException;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BCommentData;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BReplenishmentRecurrenceEnum;
import de.hybris.platform.b2bacceleratorfacades.order.data.TriggerData;
import de.hybris.platform.b2bacceleratorfacades.order.impl.DefaultB2BCheckoutFacade;
import de.hybris.platform.b2bacceleratorservices.enums.CheckoutPaymentType;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.greenlee.facades.customer.GreenleeUserFacade;


/**
 * A custom implementation of a checkout facade for a b2b accelerator store where payment authorization is not done.
 */
public class DefaultGreenleeB2BCheckoutFacade extends DefaultB2BCheckoutFacade
{
    @SuppressWarnings("unused")
    private static final Logger LOG                                         = Logger.getLogger(DefaultGreenleeB2BCheckoutFacade.class);

    private static final String CART_CHECKOUT_TERM_UNCHECKED                = "cart.term.unchecked";
    private static final String CART_CHECKOUT_NO_QUOTE_DESCRIPTION          = "cart.no.quote.description";
    private static final String CART_CHECKOUT_REPLENISHMENT_NO_STARTDATE    = "cart.replenishment.no.startdate";
    private static final String CART_CHECKOUT_REPLENISHMENT_NO_FREQUENCY    = "cart.replenishment.no.frequency";
    private static final String CART_CHECKOUT_TRANSACTION_NOT_CARD_VERIFIED = "cart.transation.notVerified";

    private SessionService      sessionService;

    @Resource(name = "greenleeUserFacade")
    private GreenleeUserFacade  greenleeUserFacade;

    // GSM : 226
    public void updateDeliveryMode(final CartData cartData)
    {
        final CartModel cartModel = getCart();

        if (cartData.getSelectedDeliveryMode() != null)
        {
            LOG.info("Selected Delivery Mode [ " + cartData.getSelectedDeliveryMode() + " ] ");
            final DeliveryModeModel deliveryModeModel = getDeliveryService().getDeliveryModeForCode(
                    cartData.getSelectedDeliveryMode());
            if (deliveryModeModel != null)
            {
                cartModel.setSelectedDeliveryMode(cartData.getSelectedDeliveryMode());
                cartModel.setDeliveryMode(deliveryModeModel);
            }

            getModelService().save(cartModel);
            getModelService().refresh(cartModel);
        }
    }
    @Override
    public CartData updateCheckoutCart(final CartData cartData)
    {
        final CartModel cartModel = getCart();
        AddressModel billAddressModel = getModelService().create(AddressModel.class);
        if (cartModel != null)
        {
            final CustomerModel customerModel = getCurrentUserForCheckout();

            if (cartData.getLivingstonString() != null && StringUtils.isNotBlank(cartData.getLivingstonString())
                    && StringUtils.isNotEmpty(cartData.getLivingstonString()))
            {
                LOG.error("Livingston value on cart " + cartData.getLivingstonString());
                cartModel.setLivingstonString(cartData.getLivingstonString());
            }
            if (cartData.getSelectedDeliveryMode() != null)
            {
                LOG.info("Selected Delivery Mode [ " + cartData.getSelectedDeliveryMode() + " ] ");
                final DeliveryModeModel deliveryModeModel = getDeliveryService().getDeliveryModeForCode(
                        cartData.getSelectedDeliveryMode());
                if (deliveryModeModel != null)
                {
                    cartModel.setSelectedDeliveryMode(cartData.getSelectedDeliveryMode());
                    cartModel.setDeliveryMode(deliveryModeModel);
                }
            }
            if (cartData.getBillingAddress() != null)
            {
                final String cartIdentifer = cartData.getBillingAddress().getId();
                billAddressModel = this.getCustomerAccountService().getAddressForCode(customerModel, cartIdentifer);
                if (billAddressModel != null)
                {
                    billAddressModel.setSelectedBillingAddressId(cartIdentifer);
                    greenleeUserFacade.setDefaultBillingAddress(customerModel, billAddressModel);
                    cartModel.setSelectedBillingAddress(billAddressModel);
                    LOG.info("Selected Billing Address ID Added to Cart Model[ " + cartIdentifer + " ] Customer ID [ "
                            + customerModel.getUid() + " ]");
                }
            }
            //157
            //reset the persisted delivery address when clicked on change.

            if (cartModel.getDeliveryAddress() != null && cartData.getDeliveryAddress() != null)
            {
                LOG.info("Address Model " + customerModel.getUid() + " ] "
                        + cartModel.getDeliveryAddress().getPk().getLongValueAsString());
                LOG.info("Address Data " + customerModel.getUid() + " ] " + cartData.getDeliveryAddress().getId());
                LOG.info("Address Compare "
                        + cartData.getDeliveryAddress().getId()
                                .equals(cartModel.getDeliveryAddress().getPk().getLongValueAsString()));
                if (!cartData.getDeliveryAddress().getId().equals(cartModel.getDeliveryAddress().getPk().getLongValueAsString()))
                {
                    cartModel.setDeliveryAddress(null); //reset the previous address and persist the new selected address - GSM-157
                    getModelService().save(cartModel);
                    getModelService().refresh(cartModel);
                    //set new selected Address
                    final AddressModel newAddressModel = this.getCustomerAccountService().getAddressForCode(customerModel,
                            cartModel.getDeliveryAddress().getPk().getLongValueAsString());
                    LOG.info("Updated card model for the Customer ID [ " + customerModel.getUid() + " ] new Address ID "
                            + newAddressModel.getPk().getLongValueAsString());
                    cartModel.setDeliveryAddress(newAddressModel);
                }
            }
            //157
            getModelService().save(cartModel);
            getModelService().refresh(cartModel);
        }
        final CartData cartDataReturn = super.updateCheckoutCart(cartData);
        billAddressModel = null;
        return cartDataReturn;
    }

    @Override
    public <T extends AbstractOrderData> T placeOrder(final PlaceOrderData placeOrderData) throws InvalidCartException
    {
        // term must be checked
        if (!placeOrderData.getTermsCheck().equals(Boolean.TRUE))
        {
            throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_TERM_UNCHECKED));
        }

        // for CARD type, transaction must be authorized before placing order
        final boolean isCardtPaymentType = CheckoutPaymentType.CARD.getCode().equals(getCart().getPaymentType().getCode());
        if (isCardtPaymentType)
        {
            final List<PaymentTransactionModel> transactions = getCart().getPaymentTransactions();
            boolean cardVerified = false;
            for (final PaymentTransactionModel transaction : transactions)
            {
                for (final PaymentTransactionEntryModel entry : transaction.getEntries())
                {
                    if (entry.getType().equals(PaymentTransactionType.CARD_VERIFICATION)
                            && TransactionStatus.ACCEPTED.name().equals(entry.getTransactionStatus()))
                    {
                        cardVerified = true;
                        break;
                    }
                }
            }
            if (!cardVerified)
            {
                throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_TRANSACTION_NOT_CARD_VERIFIED));
            }
        }

        if (isValidCheckoutCart(placeOrderData))
        {
            // validate quote negotiation
            if (placeOrderData.getNegotiateQuote() != null && placeOrderData.getNegotiateQuote().equals(Boolean.TRUE))
            {
                if (StringUtils.isBlank(placeOrderData.getQuoteRequestDescription()))
                {
                    throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_NO_QUOTE_DESCRIPTION));
                }
                else
                {
                    final B2BCommentData b2BComment = new B2BCommentData();
                    b2BComment.setComment(placeOrderData.getQuoteRequestDescription());

                    final CartData cartData = new CartData();
                    cartData.setB2BComment(b2BComment);

                    updateCheckoutCart(cartData);
                }
            }

            // validate replenishment
            if (placeOrderData.getReplenishmentOrder() != null && placeOrderData.getReplenishmentOrder().equals(Boolean.TRUE))
            {
                if (placeOrderData.getReplenishmentStartDate() == null)
                {
                    throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_REPLENISHMENT_NO_STARTDATE));
                }

                if (placeOrderData.getReplenishmentRecurrence().equals(B2BReplenishmentRecurrenceEnum.WEEKLY)
                        && CollectionUtils.isEmpty(placeOrderData.getNDaysOfWeek()))
                {
                    throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_REPLENISHMENT_NO_FREQUENCY));
                }

                final TriggerData triggerData = new TriggerData();
                populateTriggerDataFromPlaceOrderData(placeOrderData, triggerData);

                return (T) scheduleOrder(triggerData);
            }

            return (T) super.placeOrder();
        }

        return null;
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


}

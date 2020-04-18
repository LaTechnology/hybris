/**
 *
 */
package com.greenlee.facades.customer;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.user.impl.DefaultUserFacade;
import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.greenlee.core.greenleeb2bcustomer.services.GreenleeB2BCustomerService;
import com.greenlee.core.model.GreenleeB2BCustomerModel;
import com.greenlee.core.util.GreenleeUtils;


/**
 * @author aruna
 *
 */
public class GreenleeUserFacade extends DefaultUserFacade
{
    private GreenleeB2BCustomerService greenleeB2BCustomerService;
    protected static final Logger      LOG = Logger.getLogger(GreenleeUserFacade.class);
    private DeliveryService            deliveryService;

    public void setPrimaryAddress(final GreenleeB2BCustomerModel currentCustomer)
    {
        final AddressModel primaryAddress = getPrimaryAddressFromBookEntries(currentCustomer);

        final CartModel cartModel = getCartService().getSessionCart();

        cartModel.setPrimaryAddress(primaryAddress);

        getModelService().save(cartModel);
    }

    public AddressModel getPrimaryAddressFromBookEntries(final GreenleeB2BCustomerModel currentCustomer)
    {
        final List<AddressModel> addresses = getCustomerAccountService().getAddressBookEntries(currentCustomer);
        LOG.error("Address Books [" + addresses.size() + "]");
        for (final AddressModel addressModel : addresses)
        {
            if (null != addressModel.getPrimaryAddress() && addressModel.getPrimaryAddress().booleanValue())
            {
                final boolean isPrimayAddress = addressModel.getPrimaryAddress().booleanValue();
                LOG.error("Address Data [" + isPrimayAddress + " PK: " + addressModel.getPk() + " Firstname "
                        + addressModel.getFirstname() + " LastName :" + addressModel.getLastname());
                return addressModel;
            }
        }
        return null;
    }
    public void setDefaultBillingAddress(final CustomerModel customerModel, final AddressModel addressModel)
    {
        validateParameterNotNull(addressModel, "Address model cannot be null");
        validateParameterNotNull(customerModel, "CustomerModel model cannot be null");
        for (final AddressModel addressModel2 : customerModel.getAddresses())
        {
            if (addressModel2.getBillingAddress().booleanValue()
                    && addressModel2.getPk().getLongValueAsString().equalsIgnoreCase(addressModel.getPk().getLongValueAsString()))
            {
                LOG.info("setDefaultBillingAddress " + addressModel.getPk().getLongValueAsString());
                customerModel.setDefaultPaymentAddress(addressModel);
            }
            if (addressModel2.getBillingAddress().booleanValue())
            {
                LOG.info("setDefaultBillingAddress >> addressModel2.getBillingAddress().booleanValue() >> "
                        + addressModel.getPk().getLongValueAsString());
                addressModel2.setSelectedBillingAddressId(addressModel.getPk().getLongValueAsString());
            }
            getModelService().save(addressModel2);
            getModelService().refresh(addressModel2);
        }

        getModelService().save(customerModel);
        getModelService().refresh(customerModel);
    }

    public AddressData getDefaultBillingAddress(final String addressCode)
    {
        for (final AddressData addressData : getBillingAddressBook())
        {
            if (addressData.getId() != null && addressData.getSelectedBillingAddressId() != null
                    && addressData.getId().equals(addressCode))
            {
                LOG.info("If getDefaultBillingAddress >> getDefaultBillingAddress >> " + addressCode);
                return addressData;
            }
            else
            {
                LOG.info("Else getDefaultBillingAddress >> getDefaultBillingAddress >> not matching");
                addressData.setSelectedBillingAddressId(null);
                editAddress(addressData);
            }
        }
        return null;
    }

    public AddressData getDefaultPaymentAddressForCheckout()
    {
        AddressData addressData = null;
        try
        {
            final CustomerModel customerModel = (CustomerModel) getUserService().getCurrentUser();
            final AddressModel paymentAddress = customerModel.getDefaultPaymentAddress();
            LOG.error("Default Payment Address for the Customer ID [ " + customerModel.getUid()
                    + " ] & The Selected Billing Address as a Payment Address ID is " + "["
                    + paymentAddress.getPk().getLongValue());
            addressData = getAddressConverter().convert(paymentAddress);
            return addressData;
        }
        catch (final Exception e)
        {
            LOG.error(e);
        }
        return addressData;

    }

    public AddressData getDefaultBillingAddress()
    {
        for (final AddressData addressData : getBillingAddressBook())
        {
            if (addressData.getId() != null && addressData.getSelectedBillingAddressId() != null)
            {
                LOG.info("Selected Billing Address Code [" + addressData.getId() + " ][ "
                        + addressData.getSelectedBillingAddressId() + "]");
                return addressData;
            }
        }
        return null;
    }

    public List<AddressData> getAddressBookEntries()
    {
        final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();
        AddressData addressData = null;
        final List<AddressData> result = new ArrayList<AddressData>();
        final List<AddressModel> addresses = getCustomerAccountService().getAddressBookEntries(currentCustomer);
        for (final AddressModel addressModel : addresses)
        {
            addressData = getAddressConverter().convert(addressModel);
            LOG.info("currentCustomer >> " + currentCustomer.getUid() + " AddressModel " + addressModel.getPk() + " addressData "
                    + addressData.getId());
            if (addressData.getPrimaryAddress() != null && addressData.getDefaultBillingAddress() != null
                    && addressData.getDefaultShippingAddress() != null)
            {
                final boolean isPrimayAddress = addressData.getPrimaryAddress().booleanValue();
                final boolean isDefaultBillingAddress = addressData.getDefaultBillingAddress().booleanValue();
                final boolean isDefaultShippingAddress = addressData.getDefaultShippingAddress().booleanValue();
                final boolean isShippingAddress = addressData.isShippingAddress();
                final boolean isBillingAddress = addressData.isBillingAddress();
                final boolean ruleFlag = (isPrimayAddress || isDefaultBillingAddress || isDefaultShippingAddress
                        || isShippingAddress || isBillingAddress);
                LOG.error("Address Data [" + addressData.getId() + "] ruleFlag [" + ruleFlag + "] isPrimayAddress ["
                        + isPrimayAddress + "] isDefaultBillingAddress [" + isDefaultBillingAddress
                        + "] isDefaultShippingAddress [" + isDefaultShippingAddress + "] isShippingAddress [" + isShippingAddress
                        + "] isBillingAddress " + isBillingAddress);
            }
            result.add(addressData);
        }
        return result;
    }

    public List<AddressData> getBillingAddressBook()
    {
        // Get the current customer's addresses
        final Collection<AddressModel> addresses = getCustomerAccountService().getAddressBookDeliveryEntries(
                (CustomerModel) getUserService().getCurrentUser());

        if (addresses != null && !addresses.isEmpty())
        {
            final List<AddressData> result = new ArrayList<AddressData>();
            final AddressData defaultAddress = getDefaultAddress();
            // Filter for delivery addresses
            for (final AddressModel address : addresses)
            {
                if (address.getCountry() != null)
                {
                    final boolean validForSiteBilling = address.getBillingAddress().booleanValue();
                    // Filter out invalid addresses for billing address.
                    if (validForSiteBilling)
                    {
                        final AddressData addressData = getAddressConverter().convert(address);
                        if (defaultAddress != null && defaultAddress.getId() != null
                                && defaultAddress.getId().equals(addressData.getId()))
                        {
                            addressData.setDefaultAddress(true);
                            result.add(0, addressData);
                        }
                        else
                        {
                            result.add(addressData);
                        }
                    }
                }
            }

            return result;
        }
        return Collections.emptyList();
    }

    public String getSelectableDeliveryModeByCode(final String code)
    {
        String deliveryName = null;
        if (null != code)
        {
            final DeliveryModeModel deliveryModeModel = getDeliveryService().getDeliveryModeForCode(code);
            return deliveryName = deliveryModeModel.getName();
        }
        return deliveryName;
    }

    public void editSelectedBillingAddress(final AddressData addressData)
    {
        //        validateParameterNotNullStandardMessage("addressData", addressData);
        final CustomerModel currentCustomer = getCurrentUserForCheckout();
        final AddressModel addressModel = getCustomerAccountService().getAddressForCode(currentCustomer, addressData.getId());
        //        addressModel.setRegion(null);
        getAddressReversePopulator().populate(addressData, addressModel);
        getCustomerAccountService().saveAddressEntry(currentCustomer, addressModel);
        if (addressData.isDefaultAddress())
        {
            getCustomerAccountService().setDefaultAddressEntry(currentCustomer, addressModel);
        }
        this.getDefaultBillingAddress(addressData.getId());
    }

    public void setFlagForBillingAddress(final AddressData addressData)
    {
        final CustomerModel currentCustomer = getCurrentUserForCheckout();
        final AddressModel addressModel = getCustomerAccountService().getAddressForCode(currentCustomer, addressData.getId());
        getAddressReversePopulator().populate(addressData, addressModel);
        getCustomerAccountService().saveAddressEntry(currentCustomer, addressModel);
    }

    public void addAddress(final AddressData addressData, final CustomerModel customerModel)
    {

        final boolean makeThisAddressTheDefault = addressData.isDefaultAddress()
                || (customerModel.getDefaultShipmentAddress() == null && addressData.isVisibleInAddressBook());

        // Create the new address model
        final AddressModel newAddress = getModelService().create(AddressModel.class);
        getAddressReversePopulator().populate(addressData, newAddress);
        customerModel.setDefaultShipmentAddress(newAddress);//GSM-155
        // Store the address against the user
        getCustomerAccountService().saveAddressEntry(customerModel, newAddress);

        // Update the address ID in the newly created address.
        addressData.setId(newAddress.getPk().toString());

        if (makeThisAddressTheDefault && customerModel.getAddresses() != null && !customerModel.getAddresses().isEmpty())
        {
            for (final AddressModel address : customerModel.getAddresses())
            {
                getCustomerAccountService().setDefaultAddressEntry(customerModel, address);
                break;
            }

        }
    }

    @Override
    public void addAddress(final AddressData addressData)
    {
        //        validateParameterNotNullStandardMessage("addressData", addressData);

        final CustomerModel currentCustomer = getCurrentUserForCheckout();

        final boolean makeThisAddressTheDefault = addressData.isDefaultAddress()
                || (currentCustomer.getDefaultShipmentAddress() == null && addressData.isVisibleInAddressBook());

        // Create the new address model
        final AddressModel newAddress = getModelService().create(AddressModel.class);
        getAddressReversePopulator().populate(addressData, newAddress);

        // Store the address against the user
        getCustomerAccountService().saveAddressEntry(currentCustomer, newAddress);

        // Update the address ID in the newly created address
        addressData.setId(newAddress.getPk().toString());

        if (makeThisAddressTheDefault)
        {
            getCustomerAccountService().setDefaultAddressEntry(currentCustomer, newAddress);
        }
        currentCustomer.setDefaultPaymentAddress(null);
        getModelService().save(currentCustomer);
        getModelService().refresh(currentCustomer);
    }

    public void addBillingAddress(final AddressData addressData)
    {
        //        validateParameterNotNullStandardMessage("addressData", addressData);

        final CustomerModel currentCustomer = getCurrentUserForCheckout();

        final boolean makeThisAddressTheDefault = addressData.isDefaultAddress() || addressData.isVisibleInAddressBook();

        // Create the new address model
        final AddressModel newAddress = getModelService().create(AddressModel.class);
        getAddressReversePopulator().populate(addressData, newAddress);

        // Store the address against the user
        getCustomerAccountService().saveAddressEntry(currentCustomer, newAddress);

        // Update the address ID in the newly created address
        addressData.setId(newAddress.getPk().toString());
        addressData.setSelectedBillingAddressId(newAddress.getPk().toString());
        if (makeThisAddressTheDefault)
        {
            getCustomerAccountService().setDefaultAddressEntry(currentCustomer, newAddress);
            currentCustomer.setDefaultPaymentAddress(newAddress);
        }

        getModelService().save(currentCustomer);
        getModelService().refresh(currentCustomer);
    }

    public String getAccNoFromB2BUnit(final CustomerData currentUser)
    {
        final List<GreenleeB2BCustomerModel> greenleeB2BCustomersById = greenleeB2BCustomerService
                .getGreenleeB2BCustomersById(currentUser.getUid());
        if (greenleeB2BCustomersById != null && !greenleeB2BCustomersById.isEmpty())
        {
            final GreenleeB2BCustomerModel greenleeB2BCustomer = greenleeB2BCustomersById.get(0);
            final B2BUnitModel b2bUnit = greenleeB2BCustomer.getSessionB2BUnit();
            return GreenleeUtils.getAccNoFromB2BUnit(b2bUnit);
        }
        return null;

    }

    public UserModel getUserById(final String userID)
    {
        return getUserService().getUserForUID(StringUtils.lowerCase(userID));
    }

    /**
     * @return the greenleeB2BCustomerService
     */
    public GreenleeB2BCustomerService getGreenleeB2BCustomerService()
    {
        return greenleeB2BCustomerService;
    }

    /**
     * @param greenleeB2BCustomerService
     *            the greenleeB2BCustomerService to set
     */
    @Required
    public void setGreenleeB2BCustomerService(final GreenleeB2BCustomerService greenleeB2BCustomerService)
    {
        this.greenleeB2BCustomerService = greenleeB2BCustomerService;
    }

    protected DeliveryService getDeliveryService()
    {
        return deliveryService;
    }

    @Required
    public void setDeliveryService(final DeliveryService deliveryService)
    {
        this.deliveryService = deliveryService;
    }
}

package com.greenlee.facades.checkout.services.impl;

/**
 *
 */


import de.hybris.platform.acceleratorfacades.order.AcceleratorCheckoutFacade;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bacceleratorfacades.api.cart.CheckoutFacade;
import de.hybris.platform.b2bacceleratorservices.company.impl.DefaultCompanyB2BCommerceService;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.daos.CurrencyDao;
import de.hybris.platform.servicelayer.internal.model.impl.ItemModelCloneCreator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.greenlee.core.enums.UserTypes;
import com.greenlee.core.model.GreenleeB2BCustomerModel;
import com.greenlee.facades.checkout.services.RealTimePICustomerCreationService;
import com.greenlee.facades.customer.GreenleeUserFacade;
import com.greenlee.pi.data.PIAddress;
import com.greenlee.pi.data.PICustomerData;
import com.greenlee.pi.service.GreenleePICustomerService;


/**
 * @author peter.asirvatham
 *
 */
public class DefaultRealTimePICustomerCreationService implements RealTimePICustomerCreationService
{

    private static final Logger                  LOG = Logger.getLogger(DefaultRealTimePICustomerCreationService.class);

    @Resource(name = "userService")
    private UserService                          userService;

    @Resource(name = "itemModelCloneCreator")
    private ItemModelCloneCreator                itemModelCloneCreator;

    @Resource(name = "greenleeUserFacade")
    private GreenleeUserFacade                   greenleeUserFacade;

    @Autowired
    private SessionService                       sessionService;

    @Resource(name = "modelService")
    private ModelService                         modelService;

    @Resource(name = "greenleePICustomerService")
    private GreenleePICustomerService            greenleePICustomerService;

    @Resource(name = "companyB2BCommerceService")
    private DefaultCompanyB2BCommerceService     companyB2BCommerceService;

    @Resource(name = "currencyDao")
    private CurrencyDao                          currencyDao;

    @Resource(name = "commonI18NService")
    private CommonI18NService                    commonI18NService;

    @Resource(name = "userFacade")
    private UserFacade                           userFacade;

    @Resource(name = "storeSessionFacade")
    private StoreSessionFacade                   storeSessionFacade;

    @Resource(name = "acceleratorCheckoutFacade")
    private AcceleratorCheckoutFacade            checkoutFacade;

    @Resource(name = "greenleeB2BCheckoutFacade")
    private CheckoutFacade                       greenleeCheckoutFacade;

    @Resource(name = "cartService")
    private CartService                          cartService;

    private Populator<AddressData, AddressModel> addressReversePopulator;

    protected Populator<AddressData, AddressModel> getAddressReversePopulator()
    {
        return addressReversePopulator;
    }

    @Required
    public void setAddressReversePopulator(final Populator<AddressData, AddressModel> addressReversePopulator)
    {
        this.addressReversePopulator = addressReversePopulator;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.hybris.platform.b2bacceleratoraddon.checkout.services.RealTimePICustomerCreationService#isCustomerCreated(java.
     * lang.StringBuffer)
     */
    @Override
    public boolean isCustomerCreated(final StringBuilder errorMessage, final String livingstonScreenStatus)
    {

        boolean allowedToCheckout = true;
        final Set<B2BUnitModel> b2bUnits = new HashSet();
        final Set<PrincipalGroupModel> newUserGroup = new HashSet();
        final UserModel userModel = userService.getCurrentUser();
        getB2BUnits(b2bUnits, newUserGroup);
        for (final B2BUnitModel dummyB2BUnit : b2bUnits)
        {
            if (UserTypes.B2C.equals(dummyB2BUnit.getUserType()) && (dummyB2BUnit.getUid().equalsIgnoreCase(DUMMY_UNIT_B2C)))
            {
                final PICustomerData piCustomerData = createPICustomerData(userModel, DUMMY_UNIT_B2C, livingstonScreenStatus);
                allowedToCheckout = performRealTimeCustomerCreationinSAP(piCustomerData, errorMessage);
                if (allowedToCheckout)
                {
                    performCheckoutProcess(UserTypes.B2C.getCode(), piCustomerData, dummyB2BUnit, newUserGroup);
                }
            }
            if (UserTypes.B2E.equals(dummyB2BUnit.getUserType()) && (dummyB2BUnit.getUid().equalsIgnoreCase(DUMMY_UNIT_B2E)))
            {
                final PICustomerData piCustomerData = createPICustomerData(userModel, DUMMY_UNIT_B2E, livingstonScreenStatus);
                allowedToCheckout = performRealTimeCustomerCreationinSAP(piCustomerData, errorMessage);
                if (allowedToCheckout)
                {
                    performCheckoutProcess(UserTypes.B2E.getCode(), piCustomerData, dummyB2BUnit, newUserGroup);
                }

            }
            if (UserTypes.B2B.equals(dummyB2BUnit.getUserType()) && (dummyB2BUnit.getUid().equalsIgnoreCase(DUMMY_UNIT_B2B)))
            {
                allowedToCheckout = false;
            }

            // The implementation for CAD Customers replication on SAP PI.

            if (UserTypes.B2C.equals(dummyB2BUnit.getUserType()) && (dummyB2BUnit.getUid().equalsIgnoreCase(DUMMY_UNIT_B2C_CAD)))
            {
                final PICustomerData piCustomerData = createPICustomerData(userModel, DUMMY_UNIT_B2C, livingstonScreenStatus);
                allowedToCheckout = performRealTimeCustomerCreationinSAP(piCustomerData, errorMessage);
                if (allowedToCheckout)
                {
                    performCheckoutProcess(UserTypes.B2C.getCode(), piCustomerData, dummyB2BUnit, newUserGroup);
                }
            }
            if (UserTypes.B2E.equals(dummyB2BUnit.getUserType()) && (dummyB2BUnit.getUid().equalsIgnoreCase(DUMMY_UNIT_B2E_CAD)))
            {
                final PICustomerData piCustomerData = createPICustomerData(userModel, DUMMY_UNIT_B2E, livingstonScreenStatus);
                allowedToCheckout = performRealTimeCustomerCreationinSAP(piCustomerData, errorMessage);
                if (allowedToCheckout)
                {
                    performCheckoutProcess(UserTypes.B2E.getCode(), piCustomerData, dummyB2BUnit, newUserGroup);
                }

            }
            if (UserTypes.B2B.equals(dummyB2BUnit.getUserType()) && (dummyB2BUnit.getUid().equalsIgnoreCase(DUMMY_UNIT_B2B_CAD)))
            {
                allowedToCheckout = false;
            }
        }

        return allowedToCheckout;

    }

    public void getB2BUnits(final Set<B2BUnitModel> b2bUnits, final Set<PrincipalGroupModel> newUserGroup)
    {
        final UserModel userModel = userService.getCurrentUser();
        final Set<PrincipalGroupModel> userGroup = userModel.getGroups();
        for (final PrincipalGroupModel userGroupModel : userGroup)
        {
            if (userGroupModel instanceof B2BUnitModel)
            {
                final B2BUnitModel b2bunit = (B2BUnitModel) userGroupModel;
                b2bUnits.add(b2bunit);
            }
            newUserGroup.add(userGroupModel);
        }
    }

    public boolean performCheckoutProcess(final String userType, final PICustomerData piCustomerData,
            final B2BUnitModel dummyB2BUnit, final Set<PrincipalGroupModel> newUserGroup)
    {
        final UserModel userModel = userService.getCurrentUser();
        final B2BUnitModel unit = postProcessB2BunitCreation(userType, piCustomerData);
        newUserGroup.add(unit);
        newUserGroup.remove(dummyB2BUnit);
        if (userModel instanceof GreenleeB2BCustomerModel)
        {
            final GreenleeB2BCustomerModel greenleeUser = (GreenleeB2BCustomerModel) userModel;
            greenleeUser.setDefaultB2BUnit(unit);
            //Setting sessionB2bUnit to the newly created unit.
            greenleeUser.setSessionB2BUnit(unit);
            greenleeUser.setGroups(newUserGroup);
            //greenleeUser.setAddresses(null);

            if (userType.equals(UserTypes.B2E.getCode()) && unit.getMembers().size() == 1)
            {
                final UserGroupModel adminusergroup = userService.getUserGroupForUID("b2badmingroup");
                final Set<PrincipalGroupModel> groups = new HashSet<PrincipalGroupModel>();
                if (greenleeUser.getGroups() != null && !greenleeUser.getGroups().isEmpty())
                {
                    groups.addAll(greenleeUser.getGroups());
                }
                if (adminusergroup != null)
                {
                    groups.add(adminusergroup);
                    greenleeUser.setGroups(groups);
                }
            }
            //GRE-1601 - The initially set shipping address in the cart is now removed(from user to b2bunit), hence resetting the address in the cart again
            final CartModel cartModel = cartService.getSessionCart();
            cartModel.setUnit(unit);
            if (unit.getBillingAddress() != null && unit.getBillingAddress().getShippingAddress() != null
                    && unit.getBillingAddress().getShippingAddress().booleanValue())
            {
                cartModel.setDeliveryAddress(unit.getBillingAddress());
            }
            modelService.save(cartModel);
            LOG.error("Unit Model Saved for the Customer ID " + unit.getUid() + " the Cart ID: " + cartModel.getCode());
            modelService.saveAll(greenleeUser);
            modelService.refresh(cartModel);
            modelService.refresh(greenleeUser);
            //Start of 1844 - Issue fix - Update cart data to get reflect the newly added unit address in cart delivery address.
            final CartData cartData = checkoutFacade.getCheckoutCart();
            greenleeCheckoutFacade.updateCheckoutCart(cartData);
            if (unit.getBillingAddress() != null && unit.getBillingAddress().getShippingAddress() != null
                    && unit.getBillingAddress().getShippingAddress().booleanValue())
            {
                final AddressData selectedAddressData = checkoutFacade.getDeliveryAddressForCode(unit.getBillingAddress().getPk()
                        .toString());
                cartData.setDeliveryAddress(selectedAddressData);
                greenleeCheckoutFacade.updateCheckoutCart(cartData);
            }
            //End of 1844 - Issue fix - Update cart data to get reflect the newly added unit address in cart delivery address.
        }
        return true;
    }

    public boolean performRealTimeCustomerCreationinSAP(final PICustomerData piCustomerData, final StringBuilder errorMessage)
    {
        boolean allowedToCheckout = true;

        final PICustomerData customerData = greenleePICustomerService.piCreateCustomer(piCustomerData);
        if (customerData != null && customerData.getErrorMessage() != null && !customerData.getErrorMessage().isEmpty())
        {
            allowedToCheckout = false;
            errorMessage.append(customerData.getErrorMessage());
            LOG.error(customerData.getErrorMessage() + " [ " + customerData.getSapCustomerNo() + " ]");
        }
        else if (customerData != null && customerData.getSapCustomerNo() != null)
        {
            piCustomerData.setBillToCustomerNo(customerData.getBillToCustomerNo());
            piCustomerData.setSapCustomerNo(customerData.getSapCustomerNo());
            final B2BUnitModel childB2bUnit = companyB2BCommerceService.getUnitForUid(customerData.getSapCustomerNo());
            if (childB2bUnit != null)
            {
                allowedToCheckout = false;
                LOG.error("The Account cannot be created, Account already exist [ " + customerData.getSapCustomerNo() + " ]");
                //                errorMessage.append("The Account cannot be created, Account already exist");
            }
        }
        else
        {
            allowedToCheckout = false;
        }
        return allowedToCheckout;
    }

    public PICustomerData createPICustomerData(final UserModel userModel, final String baseReference,
            final String livingstonScreenStatus)
    {
        final CartModel cartModel = cartService.getSessionCart();
        final PICustomerData customerData = new PICustomerData();
        if (StringUtils.equals(baseReference, DUMMY_UNIT_B2E))
        {
            customerData.setCustomerName(((GreenleeB2BCustomerModel) userModel).getCompanyName());
        }
        else
        {
            customerData.setCustomerName(userModel.getName());
        }
        customerData.setEmailId(userModel.getUid());
        customerData.setBaseReference(baseReference);

        // set the shipping address for SAP /PI to persist data.
        final AddressModel shippingAddress = cartModel.getDeliveryAddress();
        LOG.info("shippingAddress   >> " + shippingAddress.getPk().getLongValue());
        customerData.setShippingAddress(populateAddress(shippingAddress));
        customerData.getShippingAddress().setApartment(livingstonScreenStatus);

        //send primary address for customer creation in the billing address property to SAP PI form customer creation
        final AddressModel primaryAddress = cartModel.getPrimaryAddress();
        LOG.info("primaryAddress >> " + primaryAddress.getPk().getLongValue());
        customerData.setBillingAddress(populateAddress(primaryAddress));

        return customerData;
    }
    public PIAddress populateAddress(final AddressModel address)
    {
        final PIAddress addressData = new PIAddress();
        addressData.setStreetName(address.getStreetname());
        addressData.setStreetNumber(address.getStreetnumber());
        addressData.setPostalCode(address.getPostalcode());
        addressData.setTown(address.getTown());
        if (address.getCountry() != null)
        {
            addressData.setCountry(address.getCountry().getIsocode());
        }
        if (address.getGender() != null)
        {
            addressData.setGender(address.getGender().getCode());
        }
        if (address.getDateOfBirth() != null)
        {
            final DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
            addressData.setDob(formatter.format(address.getDateOfBirth()));
        }
        addressData.setCompany(address.getCompany());
        addressData.setPoBox(address.getPobox());
        addressData.setPhone1(address.getPhone1());
        addressData.setPhone2(address.getPhone2());
        addressData.setMobilePhone(address.getCellphone());
        addressData.setFax(address.getFax());
        addressData.setEmail(address.getEmail());
        if (address.getTitle() != null)
        {
            addressData.setTitle(address.getTitle().getCode());
        }
        addressData.setFirstName(address.getFirstname());
        addressData.setMiddleName(address.getMiddlename());
        addressData.setMiddleName2(address.getMiddlename2());
        addressData.setLastName(address.getLastname());
        addressData.setDepartment(address.getDepartment());
        addressData.setBuilding(address.getBuilding());
        addressData.setApartment(address.getAppartment());
        if (address.getRegion() != null)
        {
            addressData.setRegion(address.getRegion().getIsocodeShort());
        }
        addressData.setDistrict(address.getDistrict());
        return addressData;
    }

    public B2BUnitModel postProcessB2BunitCreation(final String userType, final PICustomerData piCustomerData)
    {
        CurrencyModel currencyModel = null;
        if (piCustomerData.getShippingAddress() != null && piCustomerData.getShippingAddress().getCountry() != null
                && piCustomerData.getShippingAddress().getCountry().equalsIgnoreCase("CA"))
        {

            currencyModel = currencyDao.findCurrenciesByCode("CAD").get(0);
            storeSessionFacade.setCurrentCurrency("CAD");
        }
        else
        {
            currencyModel = currencyDao.findCurrenciesByCode("USD").get(0);
            storeSessionFacade.setCurrentCurrency("USD");
        }
        final Set<PrincipalGroupModel> b2bGroups = new HashSet();
        B2BUnitModel parentb2bUnit = getB2BUnitForUnitId(piCustomerData.getSapCustomerNo());
        final B2BUnitModel unit = modelService.create(B2BUnitModel.class);
        updateSapCustomerIdInAddresses(piCustomerData, unit);
        if (unit.getShippingAddress() != null)
        {
            LOG.info("Shipping Address SAP Customer No: " + unit.getShippingAddress().getSapCustomerID());
        }
        if (unit.getBillingAddress() != null)
        {
            LOG.info("Billing Address SAP Customer No: " + unit.getBillingAddress().getSapCustomerID());
        }
        final JaloSession session = JaloSession.getCurrentSession();
        final String currentUser = session.getSessionContext().getUser().getUid();
        final UserModel userModel = userService.getCurrentUser();
        if (parentb2bUnit == null)
        {
            parentb2bUnit = modelService.create(B2BUnitModel.class);
            parentb2bUnit.setUid(getBaseUnitId(piCustomerData.getSapCustomerNo()));
            parentb2bUnit.setCurrencyPreference(currencyModel);
            parentb2bUnit.setActive(Boolean.TRUE);
            parentb2bUnit.setUserType(UserTypes.valueOf(userType));
        }
        if (userModel instanceof GreenleeB2BCustomerModel && UserTypes.B2E.getCode().equals(userType))
        {
            final GreenleeB2BCustomerModel greenleeUser = (GreenleeB2BCustomerModel) userModel;
            unit.setLocName(greenleeUser.getCompanyName(), Locale.ENGLISH);
            parentb2bUnit.setLocName(greenleeUser.getCompanyName(), Locale.ENGLISH);
        }
        else
        {
            unit.setLocName(userModel.getName(), Locale.ENGLISH);
            parentb2bUnit.setLocName(userModel.getName(), Locale.ENGLISH);
        }
        session.getSessionContext().setUser(session.getUserManager().getAdminEmployee());
        b2bGroups.add(parentb2bUnit);
        unit.setUid(piCustomerData.getSapCustomerNo());

        unit.setUserType(UserTypes.valueOf(userType));
        unit.setCurrencyPreference(currencyModel);
        unit.setGroups(b2bGroups);
        unit.setActive(Boolean.TRUE);
        modelService.saveAll(unit);
        modelService.refresh(unit);
        session.getSessionContext().setUser(session.getUserManager().getUserByLogin(currentUser));
        commonI18NService.setCurrentCurrency(currencyModel);
        userFacade.syncSessionCurrency();
        return unit;
    }

    public B2BUnitModel getB2BUnitForUnitId(final String sapCustomerNo)
    {
        B2BUnitModel existingb2bUnit = null;
        final String baseUnitId = getBaseUnitId(sapCustomerNo);
        if (baseUnitId != null)
        {
            existingb2bUnit = companyB2BCommerceService.getUnitForUid(baseUnitId);
        }
        return existingb2bUnit;
    }

    public String getBaseUnitId(final String sapCustomerNo)
    {
        String baseUnitId = null;
        if (sapCustomerNo != null && !sapCustomerNo.isEmpty())
        {
            baseUnitId = sapCustomerNo.substring(0, sapCustomerNo.indexOf(SEPARATOR_UNDERSCORE));
        }
        return baseUnitId;
    }

    public void updateSapCustomerIdInAddresses(final PICustomerData customerData, final B2BUnitModel unit)
    {
        final UserModel user = userService.getCurrentUser();
        final Collection<AddressModel> addressess = new ArrayList();
        if (customerData.getSapCustomerNo() != null && !customerData.getSapCustomerNo().isEmpty())
        {
            if (customerData.getBillToCustomerNo() == null || customerData.getBillToCustomerNo().isEmpty())
            {
                customerData.setBillToCustomerNo(customerData.getSapCustomerNo());
            }
        }
        else if (customerData.getBillToCustomerNo() != null && !customerData.getBillToCustomerNo().isEmpty())
        {
            if (customerData.getSapCustomerNo() == null || customerData.getSapCustomerNo().isEmpty())
            {
                customerData.setSapCustomerNo(customerData.getBillToCustomerNo());
            }
        }
        if (user instanceof GreenleeB2BCustomerModel)
        {
            final GreenleeB2BCustomerModel greenleeUser = (GreenleeB2BCustomerModel) user;
            AddressData shippingAddress = null;
            final CartData cartData = checkoutFacade.getCheckoutCart();
            shippingAddress = cartData.getDeliveryAddress();
            //final AddressModel billingAddress = greenleeUser.getDefaultPaymentAddress();

            for (final AddressModel address : greenleeUser.getAddresses())
            {

                if (address.getBillingAddress() != null && address.getBillingAddress().booleanValue())
                {
                    final AddressModel b2bAddress = (AddressModel) itemModelCloneCreator.copy(address);
                    LOG.info("**********************");
                    LOG.info("BillToCustomerNo: " + customerData.getBillToCustomerNo());
                    LOG.info("**********************");
                    b2bAddress.setSapCustomerID(getBaseUnitId(customerData.getBillToCustomerNo()));
                    LOG.info("Saving Billing Address");
                    b2bAddress.setBillingAddress(Boolean.TRUE);
                    b2bAddress.setShippingAddress(Boolean.FALSE);

                    if (shippingAddress != null && shippingAddress.getId().equals(address.getPk().getLongValueAsString()))
                    {
                        b2bAddress.setShippingAddress(Boolean.TRUE);
                    }
                    unit.setBillingAddress(b2bAddress);
                    greenleeUser.setDefaultPaymentAddress(b2bAddress);
                    modelService.saveAll(greenleeUser);
                    modelService.remove(address);
                    b2bAddress.setOwner(unit);
                    addressess.add(b2bAddress);
                }
            }
            unit.setAddresses(addressess);
        }
    }
}

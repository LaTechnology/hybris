/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.greenlee.storefront.controllers;

import de.hybris.platform.acceleratorcms.model.components.CartSuggestionComponentModel;
import de.hybris.platform.acceleratorcms.model.components.CategoryFeatureComponentModel;
import de.hybris.platform.acceleratorcms.model.components.DynamicBannerComponentModel;
import de.hybris.platform.acceleratorcms.model.components.MiniCartComponentModel;
import de.hybris.platform.acceleratorcms.model.components.NavigationBarComponentModel;
import de.hybris.platform.acceleratorcms.model.components.ProductFeatureComponentModel;
import de.hybris.platform.acceleratorcms.model.components.ProductReferencesComponentModel;
import de.hybris.platform.acceleratorcms.model.components.PurchasedCategorySuggestionComponentModel;
import de.hybris.platform.acceleratorcms.model.components.SimpleResponsiveBannerComponentModel;
import de.hybris.platform.acceleratorcms.model.components.SubCategoryListComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2lib.model.components.ProductCarouselComponentModel;
import de.hybris.platform.cms2lib.model.components.ProductListComponentModel;


/**
 */
public interface ControllerConstants
{
	/**
	 * Class with action name constants
	 */
	interface Actions
	{
		interface Cms
		{
			String _Prefix = "/view/";
			String _Suffix = "Controller";

			/**
			 * Default CMS component controller
			 */
			String DefaultCMSComponent = _Prefix + "DefaultCMSComponentController";

			/**
			 * CMS components that have specific handlers
			 */
			String PurchasedCategorySuggestionComponent = _Prefix + PurchasedCategorySuggestionComponentModel._TYPECODE + _Suffix;
			String CartSuggestionComponent = _Prefix + CartSuggestionComponentModel._TYPECODE + _Suffix;
			String ProductReferencesComponent = _Prefix + ProductReferencesComponentModel._TYPECODE + _Suffix;
			String ProductCarouselComponent = _Prefix + ProductCarouselComponentModel._TYPECODE + _Suffix;
			String MiniCartComponent = _Prefix + MiniCartComponentModel._TYPECODE + _Suffix;
			String ProductFeatureComponent = _Prefix + ProductFeatureComponentModel._TYPECODE + _Suffix;
			String CategoryFeatureComponent = _Prefix + CategoryFeatureComponentModel._TYPECODE + _Suffix;
			String NavigationBarComponent = _Prefix + NavigationBarComponentModel._TYPECODE + _Suffix;
			String CMSLinkComponent = _Prefix + CMSLinkComponentModel._TYPECODE + _Suffix;
			String DynamicBannerComponent = _Prefix + DynamicBannerComponentModel._TYPECODE + _Suffix;
			String SubCategoryListComponent = _Prefix + SubCategoryListComponentModel._TYPECODE + _Suffix;
			String SimpleResponsiveBannerComponent = _Prefix + SimpleResponsiveBannerComponentModel._TYPECODE + _Suffix;
			String ProductListComponent = _Prefix + ProductListComponentModel._TYPECODE + _Suffix;
		}
	}

	/**
	 * Class with view name constants
	 */
	interface Views
	{
		interface Cms
		{
			String ComponentPrefix = "cms/";
		}

		interface Pages
		{
			interface Account
			{
				String AccountLoginPage = "pages/account/accountLoginPage";
				String AccountHomePage = "pages/account/accountHomePage";
				String AccountOrderHistoryPage = "pages/account/accountOrderHistoryPage";
				String AccountOrderPage = "pages/account/accountOrderPage";
				String AccountProfilePage = "pages/account/accountProfilePage";
				String AccountProfileEditPage = "pages/account/accountProfileEditPage";
				String AccountProfileEmailEditPage = "pages/account/accountProfileEmailEditPage";
				String AccountChangePasswordPage = "pages/account/accountChangePasswordPage";
				String AccountAddressBookPage = "pages/account/accountAddressBookPage";
				String AccountEditAddressPage = "pages/account/accountEditAddressPage";
				String AccountPaymentInfoPage = "pages/account/accountPaymentInfoPage";
				String AccountRegisterPage = "pages/account/accountRegisterPage";
				String AddAddressFormPage = "pages/account/addAddressFormPage";
			}

			interface Checkout
			{
				String CheckoutRegisterPage = "pages/checkout/checkoutRegisterPage";
				String CheckoutConfirmationPage = "pages/checkout/checkoutConfirmationPage";
				String CheckoutLoginPage = "pages/checkout/checkoutLoginPage";
			}

			interface MultiStepCheckout
			{
				String AddEditDeliveryAddressPage = "pages/checkout/multi/addEditDeliveryAddressPage";
				String ChooseDeliveryMethodPage = "pages/checkout/multi/chooseDeliveryMethodPage";
				String ChoosePickupLocationPage = "pages/checkout/multi/choosePickupLocationPage";
				String AddPaymentMethodPage = "pages/checkout/multi/addPaymentMethodPage";
				String CheckoutSummaryPage = "pages/checkout/multi/checkoutSummaryPage";
				String HostedOrderPageErrorPage = "pages/checkout/multi/hostedOrderPageErrorPage";
				String HostedOrderPostPage = "pages/checkout/multi/hostedOrderPostPage";
				String SilentOrderPostPage = "pages/checkout/multi/silentOrderPostPage";
				String GiftWrapPage = "pages/checkout/multi/giftWrapPage";
				String TermsAndConditionPage = "pages/checkout/multi/termsAndConditionPage";
				String AddEditBillingAddressPage = "pages/checkout/multi/addEditBillingAddressPage";

			}

			interface Password
			{
				String PasswordResetChangePage = "pages/password/passwordResetChangePage";
				String PasswordResetRequest = "pages/password/passwordResetRequestPage";
				String PasswordResetRequestConfirmation = "pages/password/passwordResetRequestConfirmationPage";
			}

			interface Error
			{
				String ErrorNotFoundPage = "pages/error/errorNotFoundPage";
				String ServerErrorPage = "pages/error/serverErrorPage";
			}

			interface Cart
			{
				String CartPage = "pages/cart/cartPage";
			}

			interface HelpMeChoose
			{
				String HelpMeChoosePage = "pages/helpmechoose/helpMeChoosePage";
				String maketherightchoice = "pages/helpmechoose/maketherightchoice";
			}

			interface StoreFinder
			{
				String StoreFinderSearchPage = "pages/storeFinder/storeFinderSearchPage";
				String StoreFinderDetailsPage = "pages/storeFinder/storeFinderDetailsPage";
				String StoreFinderViewMapPage = "pages/storeFinder/storeFinderViewMapPage";
			}

			interface Misc
			{
				String MiscRobotsPage = "pages/misc/miscRobotsPage";
				String MiscSiteMapPage = "pages/misc/miscSiteMapPage";
			}

			interface Guest
			{
				String GuestOrderPage = "pages/guest/guestOrderPage";
				String GuestOrderErrorPage = "pages/guest/guestOrderErrorPage";
			}

			interface Product
			{
				String WriteReview = "pages/product/writeReview";
				String OrderForm = "pages/product/productOrderFormPage";
			}
		}

		interface Fragments
		{
			interface Cart
			{
				String AddToCartPopup = "fragments/cart/addToCartPopup";
				String MiniCartPanel = "fragments/cart/miniCartPanel";
				String MiniCartErrorPanel = "fragments/cart/miniCartErrorPanel";
				String CartPopup = "fragments/cart/cartPopup";
				String ExpandGridInCart = "fragments/cart/expandGridInCart";
			}

			interface Account
			{
				String CountryAddressForm = "fragments/address/countryAddressForm";

			}

			interface Checkout
			{
				String TermsAndConditionsPopup = "fragments/checkout/termsAndConditionsPopup";
				String BillingAddressForm = "fragments/checkout/billingAddressForm";
				String ReadOnlyExpandedOrderForm = "fragments/checkout/readOnlyExpandedOrderForm";
			}

			interface Password
			{
				String PasswordResetRequestPopup = "fragments/password/passwordResetRequestPopup";
				String ForgotPasswordValidationMessage = "fragments/password/forgotPasswordValidationMessage";
				String ForgotPasswordNoEmailMessage = "fragments/password/forgotPasswordNoEmailMessage";
				String ForgotPasswordEmptyEmailMessage = "fragments/password/passwordEmptyCheckPopup";
			}

			interface Product
			{
				String FutureStockPopup = "fragments/product/futureStockPopup";
				String QuickViewPopup = "fragments/product/quickViewPopup";
				String ZoomImagesPopup = "fragments/product/zoomImagesPopup";
				String ReviewsTab = "fragments/product/reviewsTab";
				String ReviewsValidationMessage = "fragments/product/ReviewsValidationMessage";
				String ReviewsPerPage = "fragments/product/reviewsPerPage";
				String StorePickupSearchResults = "fragments/product/storePickupSearchResults";
				String ShareProductPopup = "fragments/product/shareProductPopup";
			}
		}

		interface Integration
		{
			String SALESFORCEWEBTOLEAD = "pages/form/salesforcewebtoleadform";
			String ACCOUNTORDERDETAILITEMS = "pages/account/accountOrderDetailItems";
			String ACCOUNTORDERDETILSPAGE = "pages/account/accountOrderDetailPage";
		}
	}
}

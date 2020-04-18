<%@ attribute name="supportedCountries" required="true" type="java.util.List"%>
<%@ attribute name="regions" required="true" type="java.util.List"%>
<%@ attribute name="country" required="false" type="java.lang.String"%>
<%@ attribute name="cancelUrl" required="false" type="java.lang.String"%>
<%@ attribute name="addressBook" required="false" type="java.lang.String"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/desktop/address"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<c:if test="${not empty deliveryAddresses}">
	<button type="button" class="btn btn-default btn-block js-address-book">
		<spring:theme
			code="checkout.checkout.multi.deliveryAddress.viewAddressBook"
			text="Address Book" />
	</button>
	<br>
</c:if>

<form:form method="post" commandName="addressForm">
	<form:hidden path="addressId" class="add_edit_delivery_address_id"
		status="${not empty suggestedAddresses ? 'hasSuggestedAddresses' : ''}" />
	<input type="hidden" name="bill_state" id="address.billstate" />
	
	
	<c:choose>
	<c:when test="${addAddress}">
	<div id="i18nAddressForm" class="i18nAddressForm">
	<address:addressFormElements regions="${regions}" country="${country}" />
<%-- 	<address:addressFormElements regions="${regions}" country="${country}" /> --%>
	</div>
	</c:when>
	<c:otherwise>
<%-- 	<div id="countrySelector" data-address-code="${addressData.id}"
		data-country-iso-code="${addressData.country.isocode}"
		class="form-group">
		<a href="#" class=""><spring:theme code="checkout.addnew.shippingAddress" text="Add a new address"></spring:theme></a>
	</div> --%>
	<div id="i18nAddressForm" class="i18nAddressForm">
		<c:if test="${not empty country}">
			<address:addressFormElements regions="${regions}"
				country="${country}" />
		</c:if>
		</div>
	</c:otherwise>
	</c:choose>
	
	
	<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
		<div class="checkbox checkbox-group">
			<c:choose>
				<c:when test="${showSaveToAddressBook}">
					<button
						class="positive right change_address_button show_processing_message"
						type="submit">
						<spring:theme code="checkout.multi.saveAddress"
							text="Save address" />
					</button>
				</c:when>
				<c:when test="${not addressBookEmpty && not isDefaultAddress}">
			       <ycommerce:testId code="editAddress_defaultAddress_box">
			           <formElement:formCheckbox idKey="defaultAddress"
			               labelKey="address.default" path="defaultAddress"
			               inputCSS="add-address-left-input"
			               labelCSS="add-address-left-label" mandatory="true" />
			       </ycommerce:testId>
			    </c:when>
			</c:choose>
		</div>
	</sec:authorize>
	<div id="addressform_button_panel" class="form-actions">
		<c:choose>
			<c:when test="${edit eq true && not addressBook}">
				<ycommerce:testId code="multicheckout_saveAddress_button">
					<button
						class="positive right change_address_button show_processing_message"
						type="submit">
						<spring:theme code="checkout.multi.saveAddress"
							text="Save address" />
					</button>
				</ycommerce:testId>
			</c:when>
			<c:when test="${addressBook eq true}">
					
						<ycommerce:testId code="editAddress_saveAddress_button">
						<div class="row form-actions">
						<div class="col-md-3">
							 <button type="submit" class="btn-white btn-login"><spring:theme code="text.button.save"
											  text="Save" /></button>
						</div>
						</div>
						</ycommerce:testId>
					
					<%-- <div class="col-xs-12 col-sm-5 col-md-4 col-lg-3 col-sm-pull-5 col-md-pull-4 col-lg-pull-3">
						<ycommerce:testId code="editAddress_cancelAddress_button">
							<c:url value="${cancelUrl}" var="cancel"/>
							<a class="btn btn-block btn-default" href="${cancel}">
								<spring:theme code="text.button.cancel"
											  text="Cancel" />
							</a>
						</ycommerce:testId>
					</div> --%>
			</c:when>
		</c:choose>
	</div>
</form:form>

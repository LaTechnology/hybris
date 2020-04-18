<%@ attribute name="supportedCountries" required="true"	type="java.util.List"%>
<%@ attribute name="regions" required="true" type="java.util.List"%>
<%@ attribute name="country" required="false" type="java.lang.String"%>
<%@ attribute name="cancelUrl" required="false" type="java.lang.String"%>
<%@ attribute name="addressBook" required="false" type="java.lang.String"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="formElement"	tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/desktop/address"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>


<form:form method="post" commandName="addressForm">
	<input id="saveInAddressBook" name="saveInAddressBook" type="hidden"
		value="true" />
	<form:hidden path="addressId" class="add_edit_delivery_address_id"
		status="${not empty suggestedAddresses ? 'hasSuggestedAddresses' : ''}" />
	<input type="hidden" name="bill_state" id="address.billstate" />
	<div id="i18nAddressForm" class="i18nAddressForm">
		<address:addressFormElementsForCheckout regions="${regions}" country="${country}" />
	</div>
	
	<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
		<div class="checkbox checkbox-group">
			<c:choose>
				<c:when test="${not addressBookEmpty && not isDefaultAddress}">
					<ycommerce:testId code="editAddress_defaultAddress_box">
						<%-- <formElement:formCheckbox idKey="defaultAddress"
							labelKey="address.default.billing" path="defaultAddress"
							inputCSS="add-address-left-input"
							labelCSS="add-address-left-label" mandatory="true" /> --%>
							
							<c:if test="${not empty SHIPPING && SHIPPING eq 'SHIPPING'}">
						<div>
							<formElement:formCheckbox idKey="defaultAddress"
						   labelKey="address.default.shipping" path="defaultAddress"
						   inputCSS="add-address-left-input"
						   labelCSS="add-address-left-label" mandatory="false" />
						   
							<formElement:formCheckbox idKey="billingAddress"
						   labelKey="address.default.billing" path="billingAddress"
						   inputCSS="add-address-left-input"
						   labelCSS="add-address-left-label" mandatory="fasle" />
						</div>
					</c:if>
					<c:if test="${not empty BILLING && BILLING eq 'BILLING'}">
						<div>
							<formElement:formCheckbox idKey="defaultAddress"
						   labelKey="address.default.billing" path="defaultAddress"
						   inputCSS="add-address-left-input"
						   labelCSS="add-address-left-label" mandatory="true" />
						</div>
					</c:if>
					
					</ycommerce:testId>
				</c:when>
			</c:choose>
		</div>
	</sec:authorize>
		
	<div class="row form-actions">
		<div class="col-md-3">
			<button
				class="positive right change_address_button show_processing_message btn-white btn-login"
				type="submit">
				<spring:theme code="checkout.multi.saveAddress" text="Save address" />
			</button>
		</div>
	</div>
</form:form>

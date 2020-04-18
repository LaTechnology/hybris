<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/desktop/address"%>

<spring:url value="/my-account/address-book" var="addressBookUrl"/>

<%-- <button type="button" class="btn btn-default addressBackBtn" data-back-to-addresses="${addressBookUrl}">
	<span class="glyphicon glyphicon-chevron-left"></span><spring:theme code="text.account.addressBook.back.btn" text=" Back"/>
</button> --%>
<div class="add-address">
<div class="account-body">
 <div class="col-md-10">
<c:choose>
	<c:when test="${edit eq true }">
		<h1><div class="account-section-header"><spring:theme code="text.account.addressBook.updateAddress" text="Update Address"/></div></h1>
	</c:when>
	<c:otherwise>
		<h1><div class="account-section-header"><spring:theme code="text.account.addressBook.addAddress" text="New Address"/></div></h1>
	</c:otherwise>
</c:choose>
<!-- <div class="account-section-content	 account-section-content-small"> -->
<div class="row">
<div class="col-md-offset-1 col-md-11">
	<address:addressFormSelector supportedCountries="${countries}" regions="${regions}" cancelUrl="/my-account/address-book" addressBook="true" />
</div>
</div>
</div>
</div>
</div>

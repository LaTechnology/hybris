<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>

<c:url value="${redirectUrl}" var="continueUrl"/>
<template:page pageTitle="${pageTitle}" hideHeaderLinks="true">
<div id="globalMessages">
		<common:globalMessages/>
	</div>

<%-- 	<h2><spring:theme code="checkout.multi.hostedOrderPageError.header"/></h2> --%>
	

	<div class="error-details-section">
		
			<spring:theme code="checkout.multi.hostedOrderPageError.globalError"/><br><br><br><br>
		
		<div class="action">
			<a class="btn btn-primary" href="${continueUrl}"><spring:theme code="checkout.multi.hostedOrderPageError.continue"/></a>
		</div>
	</div>
	

</template:page>

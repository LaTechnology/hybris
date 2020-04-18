<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="idKey" required="true" type="java.lang.String"%>
<%@ attribute name="labelKey" required="true" type="java.lang.String"%>
<%@ attribute name="path" required="true" type="java.lang.String"%>
<%-- <%@ attribute name="items" required="true" type="java.util.Collection" %> --%>
<%@ attribute name="itemValue" required="false" type="java.lang.String"%>
<%@ attribute name="itemLabel" required="false" type="java.lang.String"%>
<%@ attribute name="mandatory" required="false" type="java.lang.Boolean"%>
<%@ attribute name="labelCSS" required="false" type="java.lang.String"%>
<%@ attribute name="selectCSSClass" required="false"
	type="java.lang.String"%>
<%@ attribute name="skipBlank" required="false" type="java.lang.Boolean"%>
<%@ attribute name="skipBlankMessageKey" required="false"
	type="java.lang.String"%>
<%@ attribute name="selectedValue" required="false"
	type="java.lang.String"%>
<%@ attribute name="tabindex" required="false" rtexprvalue="true"%>
<%@ attribute name="disabled" required="false" type="java.lang.Boolean"%>
<%@ attribute name="userTypeData" required="true"
	type="java.util.HashMap"%>
<%@ attribute name="userTypeSelected" required="false"
	type="java.lang.String"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<c:choose>
	<c:when test="${path =='accountInformation' }">
		<label class="control-label ${labelCSS}" for="${idKey}"> <spring:theme
				code="${labelKey}" />
		</label>
		<template:errorSpanField path="${path}">
			<select name="accountInformation" class="custom-select"
				id="accountInformationId">
				<option value="" disabled="disabled" selected>Please Select</option>
				<c:forEach var="accountInformationData"
					items="${accountInformationData}">
					<option value="${accountInformationData.value}">${accountInformationData.key}</option>
				</c:forEach>
			</select>
		</template:errorSpanField>



	</c:when>
	<c:otherwise>
		<template:errorSpanField path="${path}">
			<label class="control-label ${labelCSS}" for="${idKey}"> <spring:theme
					code="${labelKey}" /> <%-- <c:if test="${mandatory != null && mandatory == false}">
				<spring:theme code="login.optional" />
			</c:if> --%>
			</label>
			<select id="greenLeeRegisterUserType" name="userType"
				class="custom-select">
				<option value="" disabled="disabled" selected>Please Select</option>
				<c:forEach var="userTypeData" items="${userTypeData}">

					<c:choose>

						<c:when test="${userTypeSelected == userTypeData.value }">

							<option value="${userTypeData.value}" selected>${userTypeData.key}</option>
						</c:when>
						<c:otherwise>
							<option value="${userTypeData.value}">${userTypeData.key}</option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</select>



		</template:errorSpanField>


	</c:otherwise>
</c:choose>
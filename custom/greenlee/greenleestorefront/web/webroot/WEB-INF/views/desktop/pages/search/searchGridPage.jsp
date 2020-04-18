<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="storepickup"
	tagdir="/WEB-INF/tags/desktop/storepickup"%>
<template:page pageTitle="${pageTitle}">
	<div class="container">
		<div class="cart-switch">
			<cms:pageSlot position="BreadCrumbSlot" var="feature" element="div"
				class="row">
				<cms:component component="${feature}" />
			</cms:pageSlot>
		</div>
		
                        <cms:pageSlot position="ASMComponentSlot" var="feature">
                            <cms:component component="${feature}"/>
                        </cms:pageSlot>
                
		<div class="row">
			<div class="col-md-12 cart-switch">
				<cms:pageSlot position="TopHeaderSlot" var="feature">
					<cms:component component="${feature}" />
				</cms:pageSlot>
			</div>
		</div>
		</div>
		<div class="glproduct-grid searchPrdt-grid pc-fixed">
			<div class="container">
				<c:if test="${not empty searchWord}"><h1 class="hidden-xs hidden-sm">Results for "${searchWord}"</h1></c:if>
				<div class="row">
							
					<cms:pageSlot position="ProductLeftRefinements" var="feature">
						<cms:component component="${feature}" />
					</cms:pageSlot>

				<cms:pageSlot position="SearchResultsGridSlot" var="feature">
					<cms:component component="${feature}" />
				</cms:pageSlot>
			</div>
		</div>
	</div>

	<%-- <storepickup:pickupStorePopup /> --%>

</template:page>

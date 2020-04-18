<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>

<template:page pageTitle="${pageTitle}">
	<div class="pc-fixed">
        <cms:pageSlot position="ASMComponentSlot" var="feature">
            <cms:component component="${feature}"/>
        </cms:pageSlot>              
		<div class="container">
			<div class="row">
				<div class="col-md-12 cart-switch">
					<cms:pageSlot position="TopHeaderSlot" var="feature">
						<cms:component component="${feature}" />
					</cms:pageSlot>
				</div>
			</div>
			<div class="cart-switch row-share">
				<div class="row">
					<cms:pageSlot position="BreadCrumbSlot" var="feature">
						<cms:component component="${feature}" />
					</cms:pageSlot>
					<product:productShare product="${product}" />
				</div>

			</div>
		</div>
		<product:productDetailsPanel />
		<product:productDetailsTab product="${product}" />
		<div class="product-container">
			<cms:pageSlot position="CrossSelling" var="comp" element="div"
				class="productDetailsPageSectionCrossSelling">
				<!-- Use ProductFeatureComponent [FOLLOWUP] -->
				<cms:component component="${comp}" />
			</cms:pageSlot>
		</div>
		<div class="product-container">
			<cms:pageSlot position="UpSelling" var="comp" element="div"
				class="productDetailsPageSectionUpSelling">
				<cms:component component="${comp}" />
				<!-- Use ProductReferencesComponent [SIMILAR]-->
			</cms:pageSlot>
		</div>
		<%-- <product:productPageReviewsTab product="${product}" /> --%>
		<product:productPageReviews product="${product}" />
		<cms:pageSlot position="Compare" var="comp">
			<cms:component component="${comp}" />
		</cms:pageSlot>
	</div>
</template:page>
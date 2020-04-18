<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>

<template:page pageTitle="${pageTitle}">

	<main> <product:productDetailsPanel />
	<div class="container">
		<div class="tabs-container">
			<ul class="tabs js-tabs tabs-responsive">
				<li class="active" data-rel="overview"><span>Overview</span></li>
				<li data-rel="features"><span>Features</span></li>
				<li data-rel="specifications"><span>Specifications</span></li>
				<li class="last" data-rel="downloads"><span>Downloads</span></li>
			</ul>
			<div class="overview">
				<div class="disp-img simple-banner">
					<div class="thumb">
						<product:productPrimaryReferenceImage
							product="${productReference.target}" format="product" />
					</div>
					<div class="details">${product.description }</div>
				</div>
			</div>
		</div>
	</div>
	<cms:pageSlot position="CrossSelling" var="comp" element="div"
		class="productDetailsPageSectionCrossSelling">
		<!-- Use ProductFeatureComponent [SPAREPART] -->
		<cms:component component="${comp}" />
	</cms:pageSlot> <cms:pageSlot position="UpSelling" var="comp" element="div"
		class="productDetailsPageSectionUpSelling">
		<cms:component component="${comp}" />
		<!-- Use ProductReferencesComponent [SIMILAR]-->
	</cms:pageSlot> <product:productPageReviewsTab product="${product }" /> </main>


</template:page>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="storepickup"
	tagdir="/WEB-INF/tags/responsive/storepickup"%>


<template:page pageTitle="${pageTitle}">
	<div class="greenlee-body">
		<div class="glproduct-grid">
			<div class="container">
				<div class="row">
					<div class="col-md-12">
							<ol class="breadcrumb">
								<li><a href="#">Home</a></li>
								<li class="active">shop</li>
							</ol>
					</div>				
					<cms:pageSlot position="ProductLeftRefinements" var="feature">
						<cms:component component="${feature}" />
					</cms:pageSlot>

					<cms:pageSlot position="SearchResultsGridSlot" var="feature">
						<cms:component component="${feature}" />
					</cms:pageSlot>
				</div>
			</div>
		</div>
	</div>
	<%-- <storepickup:pickupStorePopup /> --%>

</template:page>

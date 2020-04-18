<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="storepickup"
	tagdir="/WEB-INF/tags/desktop/storepickup"%>

<template:page pageTitle="${pageTitle}">
	

		<div class="glproduct-grid">
			<div class="container">
				<div class="col-md-12 cart-switch">
					<cms:pageSlot position="BreadCrumbSlot" var="feature">
						<cms:component component="${feature}" />
					</cms:pageSlot>
				</div>
				<div class="container">
                <div class="row">
                    <div class="col-md-12 cart-switch">
                        <cms:pageSlot position="ASMComponentSlot" var="feature">
                            <cms:component component="${feature}"/>
                        </cms:pageSlot>
                    </div>
                </div>
            </div>
				<div class="row">
		<div class="col-md-12 cart-switch">
			<cms:pageSlot position="TopHeaderSlot" var="feature">
				<cms:component component="${feature}" />
			</cms:pageSlot>
		</div>
	</div>
				
				<cms:pageSlot position="Section1" var="feature">
					<cms:component component="${feature}" element="div" />
				</cms:pageSlot>
				
				<div class="row">
					<cms:pageSlot position="ProductLeftRefinements" var="feature">
						<cms:component component="${feature}" />
					</cms:pageSlot>
					<div class="pc-fixed">
					<cms:pageSlot position="ProductGridSlot" var="feature">
						<cms:component component="${feature}" />
					</cms:pageSlot>
					</div>
					<%-- <storepickup:pickupStorePopup /> --%>
				</div>
			</div>
		</div>

	
</template:page>

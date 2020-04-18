<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="storepickup"
	tagdir="/WEB-INF/tags/desktop/storepickup"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<template:page pageTitle="${pageTitle}">
	
		<div class="glproduct-list">
		<div class="container">
                <div class="row">
                    <div class="col-md-12 cart-switch">
                        <cms:pageSlot position="ASMComponentSlot" var="feature">
                            <cms:component component="${feature}"/>
                        </cms:pageSlot>
                    </div>
                </div>
            </div>
			<div class="container pc-fixed">
				<div class="row">
					<div class="col-md-12">
						<cms:pageSlot position="BreadCrumbSlot" var="feature">
							<cms:component component="${feature}" />
						</cms:pageSlot>
					</div>
						
					<cms:pageSlot position="ProductLeftRefinements" var="feature">
						<cms:component component="${feature}" />
					</cms:pageSlot>
			
					<cms:pageSlot position="ProductListSlot" var="feature">
						<cms:component component="${feature}" />
					</cms:pageSlot>
					
	
				</div>
			</div>
		</div>

	

</template:page>

<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<template:page pageTitle="${pageTitle}">

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
	</div>
<div class="container">
		<div class="row">
			<cms:pageSlot position="BreadCrumbSlot" var="feature"  element="div" class="cart-switch">
				<cms:component component="${feature}" />
			</cms:pageSlot>
		</div>
	</div>
	<div class="greenlee-contact">
		
		
		<cms:pageSlot position="Section1" var="feature">
			<cms:component component="${feature}" />
		</cms:pageSlot>
		
	<div class="container">
		<div class="row">
			<div class="col-md-5 col-md-push-7 no-space">
				<cms:pageSlot position="Section2A" var="feature" >
					<cms:component component="${feature}" />
				</cms:pageSlot>
			</div>
			<div class="col-md-6 col-md-pull-5">
			<div class="contact">
				<cms:pageSlot position="Section2B" var="feature" >
					<cms:component component="${feature}" />
				</cms:pageSlot>
			</div>
			</div>
		</div>
	</div>

		<cms:pageSlot position="Section3" var="feature" element="div">
			<cms:component component="${feature}" />
		</cms:pageSlot>
	</div>
</template:page>
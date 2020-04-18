<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<template:page pageTitle="${pageTitle}">
<div class="container">
           
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
	<cms:pageSlot position="Section1" var="feature">
		<cms:component component="${feature}" element="div" class="" />
	</cms:pageSlot>
<div class="container">
	<div class="row">
		<cms:pageSlot position="Section2A" var="feature" >
			<cms:component component="${feature}" element="div"
			class="col-md-12"/>
		</cms:pageSlot>

		<cms:pageSlot position="Section2B" var="feature" element="div"
			class="col-md-12 static-content">
			<cms:component component="${feature}" />
		</cms:pageSlot>
		<cms:pageSlot position="Section3" var="feature" element="div" class="col-md-12 static-content">
			<cms:component component="${feature}" />
		</cms:pageSlot>
	</div>
</div>
	
</template:page>

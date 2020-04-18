<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>

<template:page pageTitle="${pageTitle}">
	<div class="container">
		<div id="globalMessages">
			<common:globalMessages/>
		</div>
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
		<cms:component component="${feature}" element="div" class="span-24 section1 cms_disp-img_slot"/>
	</cms:pageSlot>
	<div class="span-24 section2">
		<cms:pageSlot position="Section2A" var="feature" element="div" class="span-12 zone_a cms_disp-img_slot">
			<cms:component component="${feature}"/>
		</cms:pageSlot>
		<cms:pageSlot position="Section2B" var="feature" element="div" class="span-6 zone_b thumbnail_detail">
			<cms:component component="${feature}"/>
		</cms:pageSlot>
		<cms:pageSlot position="Section2C" var="feature" element="div" class="span-6 zone_c last thumbnail_detail">
			<cms:component component="${feature}"/>
		</cms:pageSlot>
	</div>
	<cms:pageSlot position="Section3" var="feature" element="div" class="span-24 section3 cms_disp-img_slot">
		<cms:component component="${feature}"/>
	</cms:pageSlot>
	<cms:pageSlot position="Section4" var="feature" element="div" class="span-24">
		<cms:component component="${feature}" element="div" class="span-6 section4 small_detail ${(elementPos%4 == 3) ? 'last' : ''}"/>
	</cms:pageSlot>
	<cms:pageSlot position="Section5" var="feature" element="div" class="span-24 section5 cms_disp-img_slot">
		<cms:component component="${feature}"/>
	</cms:pageSlot>
</template:page>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<template:page pageTitle="${pageTitle}">

	<div id="owl-home" class="owl-carousel">
		<div class="item">
			<cms:pageSlot position="Section1" var="feature">
				<cms:component component="${feature}" />
			</cms:pageSlot>
		</div>
	</div>

	<div class="solution-section">
		<div class="container">
			<div class="margin-tp20 solution-list">
				<div class="row">
					<cms:pageSlot position="Section2A" var="feature">
						<cms:component component="${feature}" element="div"
							class="col-md-4" />
					</cms:pageSlot>
					<cms:pageSlot position="Section2B" var="feature">
						<cms:component component="${feature}" element="div"
							class="col-xs-6 col-sm-3" />
					</cms:pageSlot>
					<cms:pageSlot position="Section2C" var="feature" element="div"
						class="landingLayout2PageSection2C">
						<cms:component component="${feature}" element="div" />
					</cms:pageSlot>
				</div>
			</div>
		</div>
	</div>
	<cms:pageSlot position="Section3" var="feature">
		<cms:component component="${feature}" />
	</cms:pageSlot>

	<cms:pageSlot position="Section3A" var="feature">
		<cms:component component="${feature}" />
	</cms:pageSlot>


	<div class="choose-section">
		<div class="clearfix">

			<cms:pageSlot position="Section4" var="feature">
				<cms:component component="${feature}" />
			</cms:pageSlot>

		</div>
	</div>
	<cms:pageSlot position="Section5" var="feature">
		<cms:component component="${feature}" />
	</cms:pageSlot>

	<div class="testimonial-section">
     <div class="container">
	<cms:pageSlot position="Section6" var="feature">
		<cms:component component="${feature}" />
	</cms:pageSlot>
	</div>
	</div>

	<div class="about-section">
		<div class="container">
			<cms:pageSlot position="Section7" var="feature">
				<cms:component component="${feature}" element="div"
					class=" ${isFirstElement ? 'about-container first': 'about-container'}" />
			</cms:pageSlot>
		</div>
	</div>

</template:page>
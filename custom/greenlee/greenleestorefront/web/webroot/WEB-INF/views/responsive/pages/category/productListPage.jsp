<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>
<%@ taglib prefix="storepickup" tagdir="/WEB-INF/tags/responsive/storepickup" %>

<template:page pageTitle="${pageTitle}">

	<div>
		<div id="products">
			<div id="product1">
				<div id="productData1">
				</div>
				<button id="remove1" value="">remove</button>
			</div>
			<div id="product2">
				<div id="productData2">
				</div>
				<button id="remove2">remove</button>
			</div>
			<div id="product3">
				<div id="productData3">
				</div>
				<button id="remove3">remove</button>
			</div>
			<span id="com-message"></span>
		</div>
		<button id="compareBtn">compare</button>
	</div>

	<cms:pageSlot position="Section1" var="feature">
		<cms:component component="${feature}" element="div" />
	</cms:pageSlot>
	
	<div class="row">
		<cms:pageSlot position="ProductLeftRefinements" var="feature">
			<cms:component component="${feature}"/>
		</cms:pageSlot>

		<cms:pageSlot position="ProductListSlot" var="feature">
			<cms:component component="${feature}"/>
		</cms:pageSlot>


	</div>

</template:page>

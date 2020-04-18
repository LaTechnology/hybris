<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<template:page pageTitle="${pageTitle}">
	<cart:cartValidation />
	<cart:cartPickupValidation />
	<div class="container">
                
                        <cms:pageSlot position="ASMComponentSlot" var="feature">
                            <cms:component component="${feature}"/>
                        </cms:pageSlot>
                 
           
	
	
			
		
		<div class="row">	
				<cms:pageSlot position="TopContent" var="feature">
					<cms:component component="${feature}" />
				</cms:pageSlot>
			<cms:pageSlot position="TopContent2" var="feature">
				<cms:component component="${feature}" element="div"
					class="cart-switch" />
			</cms:pageSlot>
		</div>
	
	</div>
	<div class="cart-container">
	
		
		<div class="container">
			<common:globalMessages />
			<c:if test="${not empty cartData.entries}">
				<c:choose>
					<c:when test="${fn:length(cartData.entries) > 1}">
						<h1>
							<spring:theme code="basket.my.shopping.basket"
								text="MY SHOPPING CART " />&nbsp;
							<span><spring:theme
									code="basket.my.shopping.basket.entry.count" text="({0} ITEMS)"
									arguments="${fn:length(cartData.entries)}" /></span>
						</h1>
					</c:when>
					<c:otherwise>
						<h1>
							<spring:theme code="basket.my.shopping.basket"
								text="MY SHOPPING CART " />&nbsp;
							<span><spring:theme
									code="basket.my.shopping.basket.one.entry.count"
									text="({0} ITEM)" arguments="${fn:length(cartData.entries)}" /></span>
						</h1>
					</c:otherwise>
				</c:choose>
				<div class="row">
					<cms:pageSlot position="CenterLeftContentSlot" var="feature">
						<cms:component component="${feature}" />
					</cms:pageSlot>

					<cms:pageSlot position="CenterRightContentSlot" var="feature">
						<cms:component component="${feature}" />
					</cms:pageSlot>
				</div>
			</c:if>
		</div>
		<c:if test="${empty cartData.entries}">
			<cms:pageSlot position="EmptyCartMiddleContent" var="feature"
				element="div" class="emptycart">
				<cms:component component="${feature}" />
			</cms:pageSlot>
		</c:if>
	</div>

	<cms:pageSlot position="BottomContentSlot" var="feature">
		<cms:component component="${feature}" />
	</cms:pageSlot>


</template:page>
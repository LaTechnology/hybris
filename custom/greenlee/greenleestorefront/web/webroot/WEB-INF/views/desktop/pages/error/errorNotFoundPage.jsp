<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>

<template:page pageTitle="${pageTitle}">
	
	<c:url value="/" var="homePageUrl" />


	<cms:pageSlot position="MiddleContent" var="comp" >
		<cms:component component="${comp}"/>
	</cms:pageSlot>
	<cms:pageSlot position="BottomContent" var="comp" element="div" class="errorNotFoundPageBottom">
		<cms:component component="${comp}"/>
	</cms:pageSlot>
	<cms:pageSlot position="SideContent" var="feature" element="div" class="errorNotFoundPageSide">
		<cms:component component="${feature}"/>
	</cms:pageSlot>
	<div class="greenlee-account">
		<div class="container">
			<div class="yCmsContentSlot emptycart emptysearch">
				<div class="content">
					<div class="error-page">
						<h2>
							<a href="${homePageUrl}"> <spring:theme text="Page Not Found"
									code="system.error.page.not.found" />
							</a>
						</h2>
					</div>
				</div>
			</div>
		</div>
	</div>

</template:page>
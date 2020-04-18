<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<template:page pageTitle="${pageTitle}">
	<cms:pageSlot position="ASMComponentSlot" var="feature">
		<cms:component component="${feature}" />
	</cms:pageSlot>
	<div class="video-wrp">
		<div id="globalMessages">
			<c:if test="${not empty accThankMsgs}">
				<div class="global-alerts">
					<c:forEach items="${accThankMsgs}" var="msg">
						<div class="alert alert-thanks alert-dismissable text-center">
							<div class="container">
								<spring:theme code="${msg.code}" arguments="${msg.attributes}" />
								<i class="gl gl-remove"></i>
							</div>
						</div>
					</c:forEach>
				</div>
			</c:if>

			<div class="container">
				<common:globalMessages />
			</div>
		</div>




		<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
			<div class="switch-account--user">
				<div class="container">
					<div class="row">
						<div class="col-md-12 cart-switch">
							<cms:pageSlot position="TopHeaderSlot" var="feature">
								<cms:component component="${feature}" />
							</cms:pageSlot>
						</div>
					</div>
				</div>
			</div>
		</sec:authorize>
		<div id="owl-home" class="owl-carousel">
			<div class="item">
				<cms:pageSlot position="Section1" var="feature">
					<cms:component component="${feature}" />
				</cms:pageSlot>
			</div>
		</div>
		<div class="solution-section">
			<div class="container">
				<!--  <div class="margin-tp20 solution-list"> GSM215-->
				<div class="homepage-bottom-header-slot">
					<div class="row">
						<cms:pageSlot position="Section2A" var="feature">
							<%-- <cms:component component="${feature}" element="div"
								class="layout-img col-md-4  ${(isLastElement) ? 'col-sm-12' : 'col-sm-6'}" /> --%>
								 <cms:component component="${feature}" element="div"
								class="col-md-4  ${isFirstElement ? 'col-sm-6 layout-img1': ''} ${not isLastElement and not isFirstElement ? 'col-sm-6 layout-img2': ''} ${isLastElement ? 'col-sm-12 layout-img3': ''}" />
						</cms:pageSlot>
						<cms:pageSlot position="Section2B" var="feature">
							<cms:component component="${feature}" element="div"
								class="col-xs-6 col-sm-3" />
						</cms:pageSlot>
						<cms:pageSlot position="Section2C" var="feature">
							<cms:component component="${feature}" />
						</cms:pageSlot>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="video__container">
		<div class="video">
			<cms:pageSlot position="Section3" var="feature" element="div"
				class="feature">
				<cms:component component="${feature}" element="div"/>
				<%-- <c:if test="${isFirstElement}">
					<i class="gl gl-down hidden-xs"></i>
				</c:if> --%>
			</cms:pageSlot>
		</div>
	</div>
	<div class="video__container">
		<div class="video">
			<cms:pageSlot position="Section3A" var="feature">
				<cms:component component="${feature}" />
			</cms:pageSlot>
		</div>
	</div>
	<div class="video-wrp">
		<div class="choose-section-new">
			<div class="clearfix">
				<cms:pageSlot position="Section4" var="feature">
					<cms:component component="${feature}" />
				</cms:pageSlot>
			</div>
		</div>
		<div class="product-container">
			<cms:pageSlot position="Section5" var="feature">
				<cms:component component="${feature}" />
			</cms:pageSlot>
		</div>
		<div class="testimonial-section">
			<div class="container">
				<cms:pageSlot position="Section6" var="feature">
					<cms:component component="${feature}" />
				</cms:pageSlot>
			</div>
		</div>
		<div class="about-section">
			<div class="container table-fix">
				<cms:pageSlot position="Section7" var="feature">
					<cms:component component="${feature}" element="div"
						class="${isFirstElement ? 'about-container first': 'about-container'} ${isLastElement ? 'last': ''}" />
				</cms:pageSlot>
			</div>
		</div>
	</div>
</template:page>

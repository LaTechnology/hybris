<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>

<template:page pageTitle="${pageTitle}">
	<div class="container">
		<div class="row">
			<div class="col-md-12 cart-switch">
				<cms:pageSlot position="ASMComponentSlot" var="feature">
					<cms:component component="${feature}" />
				</cms:pageSlot>
			</div>
		</div>
	</div>
	<div class="container">
		<div class="row">
			<div class="col-md-12 cart-switch">
				<cms:pageSlot position="TopHeaderSlot" var="feature">
					<cms:component component="${feature}" />
				</cms:pageSlot>
			</div>
		</div>
	</div>
	<div class="category-landing residential-layout">
		<div class="banner">
			<cms:pageSlot position="Section1" var="feature">
				<cms:component component="${feature}" />
			</cms:pageSlot>
		</div>
		<div class="category-menu">
			<div class="inner">
				<div class="container">
					<label>
						<span>Overview</span>
						<i class="fa fa-angle-down"></i>
					</label>
					<ul class="flexi-menu">
						<cms:pageSlot position="Section2" var="feature">
							<c:choose>
								<c:when test="${isFirstElement}">
									<li class="active"><cms:component component="${feature}" />
									</li>
								</c:when>
								<c:otherwise>
									<li><cms:component component="${feature}" /></li>
								</c:otherwise>
							</c:choose>
						</cms:pageSlot>
					</ul>
					<cms:pageSlot position="Section2b" var="feature">
						<cms:component component="${feature}" />
					</cms:pageSlot>
				</div>
			</div>
		</div>

		<cms:pageSlot position="Section3" var="feature">
			<c:if test="${not empty feature.content}">
				<div id="overview" class="overview">
					<div class="story-wrp">
						<div class="container">
							<cms:component component="${feature}" />
						</div>
					</div>
				</div>
			</c:if>
		</cms:pageSlot>
		<div id="headthemap4" class="testimonial-section">
			<!--  div class="container"-->
			<div class="row">
				<cms:pageSlot position="Section4" var="feature">
					<cms:component component="${feature}" element="div"
						class="row simpleimagecomponent pcp-banner col-xs-12" />
				</cms:pageSlot>
			</div>
			<!-- /div -->
		</div>

		<div id="features" class="benefits">
			<div class="container">
				<div class="row">
					<cms:pageSlot position="Section5" var="feature">
						<cms:component component="${feature}" element="div"/>
					</cms:pageSlot>
				</div>
				<div class="row business-content">
					<cms:pageSlot position="Section5a" var="feature">
						<cms:component component="${feature}" element="div"
							class="col-xs-6 col-sm-6 col-md-3 col-lg-3 business-item" />
					</cms:pageSlot>
				</div>
			</div>
		</div>

		<div id="testimonials" class="testimonial-section">
			<!--  div class="container"-->
			<div class="row">
				<cms:pageSlot position="Section6" var="feature">
					<cms:component component="${feature}" element="div"
						class="row simpleimagecomponent pcp-banner col-xs-12" />
				</cms:pageSlot>
			</div>
			<!-- /div -->
		</div>
		<div id="models" class="product-list models">
			<div class="container">
				<div class="title-row">
					<!-- This section needs to be replaced with Compare component -->
					<cms:pageSlot position="Section7a" var="feature" element="div"
						class="model-cat">
						<cms:component component="${feature}" />
					</cms:pageSlot>
				</div>
				<div class="row product">
					<cms:pageSlot position="Section7" var="feature">
						<cms:component component="${feature}" />
					</cms:pageSlot>
					<div class="col-xs-12 col-md-6 pull-right">
						<div class="choose-section full-width">
							<cms:pageSlot position="Section7b" var="feature">
								<cms:component component="${feature}" />
							</cms:pageSlot>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template:page>

<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav" %>
<c:set var="searchUrl" value="/en/USD/search?q=%3Arelevance&text=#"/>

<template:page pageTitle="${pageTitle}">
<div class="container">
                <div class="row">
                    <div class="col-md-12 cart-switch">
                        <cms:pageSlot position="ASMComponentSlot" var="feature">
                            <cms:component component="${feature}"/>
                        </cms:pageSlot>
                    </div>
                </div>
            </div>
	<div class="container">
		<div class="row">
			<div class="col-md-11 cart-switch">
				<cms:pageSlot position="BreadCrumbSlot" var="feature">
					<cms:component component="${feature}" />
				</cms:pageSlot>
			</div>
		</div>
	</div>
	<c:url value="/" var="homePageUrl" />
	<div class="greenlee-account">
		<div class="container">
			<div class="yCmsContentSlot emptycart emptysearch">
				<div class="item_container">
				<nav:searchSpellingSuggestion spellingSuggestion="${searchPageData.spellingSuggestion}" />
			</div>
				<div class="content">
					<h2>
						<spring:theme code="search.no.results" arguments="${searchWord}"
							text="No Results found for ${searchWord}" />
					</h2>
					<a class="btn btn-default js-shopping-button" href="${searchUrl}"><spring:theme
							code="search.no.results.message" text="Browse Products" /></a>
				</div>
			</div>
		</div>
	</div>
	
	<%-- <cms:pageSlot position="MiddleContent" var="feature">
					<cms:component component="${feature}" />
	</cms:pageSlot> --%>
	
</template:page>
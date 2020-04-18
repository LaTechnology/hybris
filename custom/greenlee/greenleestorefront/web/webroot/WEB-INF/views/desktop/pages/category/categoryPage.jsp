<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav" %>

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
    <div class="row product-category-page">
		<div class="row">
			<div class="col-md-12 cart-switch">
				<cms:pageSlot position="TopHeaderSlot" var="feature">
					<cms:component component="${feature}" />
				</cms:pageSlot>
			</div>
		</div>
        <div class="col-md-3 col-lg-2 facetNavigation hidden-sm hidden-xs">
            <nav:categoryNav pageData="${searchPageData}"/>
            <cms:pageSlot position="Section4" var="feature">
                <cms:component component="${feature}" element="div" class="section4 small_detail"/>
            </cms:pageSlot>
        </div>

        <div class="col-md-9 col-lg-10 product-categories">
            <div class="row simpleimagecomponent">
                <cms:pageSlot position="Section1" var="feature">
                    <cms:component component="${feature}" />
                </cms:pageSlot>
            </div>
            <div class="row hidden-md hidden-lg refine-bar">
                <button class="btn btn-default pull-right js-show-facets"><spring:theme code="search.nav.refine.button" text="Refine"/></button>
            </div>

            <div class="row">
                <cms:pageSlot position="Section2" var="feature">
                    <cms:component component="${feature}" element="div" class="simpleimagecomponent pcp-prod col-xs-6 col-sm-4"/>
                </cms:pageSlot>
            </div>

            <cms:pageSlot position="Section3" var="feature" >
                <cms:component component="${feature}" element="div" class="row simpleimagecomponent pcp-banner col-xs-12"/>
            </cms:pageSlot>

        </div>
    </div>


</template:page>
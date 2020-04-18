<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<template:page pageTitle="${pageTitle}">
    <div class="aboutuspage">
        <div class="video-wrp">
            <div class="container">
                <div class="row">
                    <cms:pageSlot position="BreadCrumbSlot" var="feature" element="div" class="cart-switch">
                        <cms:component component="${feature}"/>
                    </cms:pageSlot>
                </div>
                <div id="globalMessages">
                    <common:globalMessages/>
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
                <cms:pageSlot position="Section1" var="feature">
                    <cms:component component="${feature}"/>
                </cms:pageSlot>
            </div>
            <div class="text--middle-wrp">
                <cms:pageSlot position="Section2" var="feature">
                    <cms:component component="${feature}"/>
                </cms:pageSlot>
                <cms:pageSlot position="Section3" var="feature" element="div" class="text-middle">
                    <cms:component component="${feature}"/>
                </cms:pageSlot>
            </div>
            <div class="container">
                <div class="story-wrp">
                    <cms:pageSlot position="Section4" var="feature" element="div" class="story-cnt">
                        <cms:component component="${feature}"/>
                    </cms:pageSlot>
                </div>
            </div>
            <div class="store-wrp">
                <cms:pageSlot position="Section5" var="feature" element="div" class="container">
                    <cms:component component="${feature}"/>
                </cms:pageSlot>
            </div>
        </div>
        <cms:pageSlot position="Section6" var="feature" >
	        <c:if test="${not empty feature}">
		        <div class="video__container">
		            <div class="video">
		                    <cms:component component="${feature}" />
		            </div>
		        </div>
	        </c:if>
        </cms:pageSlot>
    </div>
</template:page>

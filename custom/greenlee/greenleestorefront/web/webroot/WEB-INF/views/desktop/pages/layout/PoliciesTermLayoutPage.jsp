<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>

<template:page pageTitle="${pageTitle}">
    <div class="container">
        <div class="row">
        	<div class="col-md-12 cart-switch">
            <cms:pageSlot position="BreadCrumbSlot" var="feature">
                <cms:component component="${feature}"/>
            </cms:pageSlot>
            </div>
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
        <div class="terms-wrp">
            <cms:pageSlot position="Section1" var="feature">
                <cms:component component="${feature}"/>
            </cms:pageSlot>
            <div class="row terms-content">
                <div class="col-md-3">
                    <cms:pageSlot position="Section2" var="feature" element="ul">
                        <cms:component component="${feature}" element="li" id="link-${elementPos}" class="${(isFirstElement) ? 'link-nav active' : 'link-nav'}" data-nav="linker-${elementPos}"/>
                    </cms:pageSlot>
                </div>
                <div class="col-md-9">
                    <cms:pageSlot position="Section3" var="feature">
                        <cms:component component="${feature}" element="div" id="linker-${elementPos}" data-link="link-${elementPos}"  class="tab-area"/>
                    </cms:pageSlot>
                </div>
            </div>
        </div>
        </div>
    </template:page>

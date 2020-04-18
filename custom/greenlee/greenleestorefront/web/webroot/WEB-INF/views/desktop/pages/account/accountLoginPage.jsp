<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

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
			<cms:pageSlot position="LeftContentSlot" var="feature">
				<cms:component component="${feature}" />
			</cms:pageSlot>
		
			<cms:pageSlot position="RightContentSlot" var="feature">
				<cms:component component="${feature}" />
			</cms:pageSlot>
		
</template:page>
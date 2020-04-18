<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/desktop/user"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>


<spring:theme code="resetPwd.title" var="pageTitle" />

<template:page pageTitle="${pageTitle}">
	<div class="container">
		<div class="row">
			<div class="col-md-11">
				<cms:pageSlot position="BreadCrumbSlot" var="feature">
					<cms:component component="${feature}" />
				</cms:pageSlot>
			</div>
		</div>
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
	<user:updatePwd />
</template:page>

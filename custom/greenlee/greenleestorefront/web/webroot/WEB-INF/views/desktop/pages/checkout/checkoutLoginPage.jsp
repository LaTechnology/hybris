<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<template:page pageTitle="${pageTitle}">
<div class="login-section">
    <cms:pageSlot position="LeftContentSlot" var="feature">
        <cms:component component="${feature}"/>
    </cms:pageSlot>
    <cms:pageSlot position="RightContentSlot" var="feature">
        <cms:component component="${feature}"/>
    </cms:pageSlot>
    </div>
</template:page>

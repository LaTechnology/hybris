<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>


<spring:theme code="updatePwd.title" var="title"/>
<template:page pageTitle="${pageTitle}">
	<div class="global-alerts">
		<div class="alert alert-danger alert-dismissable">
			<spring:theme code="text.page.message.underconstruction" text="It seems this page has failed. Please close your browser and re-open a new session.<br />If this error persists, please contact Greenlee Communications Customer Support at cscommunications@greenlee.textron.com or 1-800-642-2155."/>
		</div>
	</div>
	
</template:page>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>




<template:page pageTitle="${pageTitle}">
	<div class="greenlee-contact">
		<cms:pageSlot position="Section1" var="feature">
			<cms:component component="${feature}" />
		</cms:pageSlot>

<div class="container">
	<div>
		<c:import url="/compare/list"/>
	</div>
</div>

	</div>


</template:page>

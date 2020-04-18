<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true"%>
<%@ attribute name="pageTitle" required="false" rtexprvalue="true"%>
<%@ attribute name="pageCss" required="false" fragment="true"%>
<%@ attribute name="pageScripts" required="false" fragment="true"%>
<%@ attribute name="hideHeaderLinks" required="false"%>

<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="header"
	tagdir="/WEB-INF/tags/desktop/common/header"%>
<%@ taglib prefix="footer"
	tagdir="/WEB-INF/tags/desktop/common/footer"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart" %>

<template:master pageTitle="${pageTitle}">


	<jsp:attribute name="pageCss">
		<jsp:invoke fragment="pageCss" />
	</jsp:attribute>

	<jsp:attribute name="pageScripts">
		<jsp:invoke fragment="pageScripts" />
	</jsp:attribute>

	<jsp:body>
		<main data-currency-iso-code="${currentCurrency.isocode}">
			<spring:theme code="text.skipToContent" var="skipToContent" />
			<a href="#skip-to-content" class="skiptocontent sr-only" data-role="none">${skipToContent}</a>
			<spring:theme code="text.skipToNavigation" var="skipToNavigation" />
			<a href="#skiptonavigation" class="skiptonavigation sr-only" data-role="none">${skipToNavigation}</a>


			<header:header hideHeaderLinks="${hideHeaderLinks}" />


			
			
			<a id="skip-to-content"></a>
		
			
				<%-- 
					Do not uncomment the below globalMessages tag.
					If you need the error messages to be rendered, please add the below tag to the respective pages.
					This is done in order keep the error/warn/info messages display to match the approved UI designs.
				 --%>
				<%-- <common:globalMessages /> --%>
				<%-- <cart:cartRestoration /> --%>
				
				<div class="greenlee-body">
					
				<jsp:doBody />
				</div>
			

			<footer:footer />
		</main>

	</jsp:body>

</template:master>

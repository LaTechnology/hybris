<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav" %>


	
 				<div class="col-md-3">
                 <div class="alert-wrp">
                 	<h2>Alerts</h2>
                 	<div class="jumbotron-alert">
		<ul>
			<c:forEach items="${promoLinks}" var="link">
			<li>
				<c:url value="${link.url}" var="encodedUrl" />
		<a href="${encodedUrl}" title="${link.linkName}" ${link.target == null || link.target == 'SAMEWINDOW' ? '' : 'target="_blank"'}>${link.linkName}</a>
		</li>
			</c:forEach>
		</ul>
		</div>
         </div>
         </div>
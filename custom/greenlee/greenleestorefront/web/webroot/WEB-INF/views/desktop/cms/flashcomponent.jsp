<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- <c:url value="${not empty page ? page.label : urlLink}" var="encodedUrl" />
	<c:choose>
		<c:when test="${empty encodedUrl || encodedUrl eq '#'}">
			<div class="crt-parallaxvideo">
				<img title="${headline}" alt="${media.altText}" src="${media.url}">
				 <iframe id="video" src="${encodedUrl}" frameborder="0" allowfullscreen> </iframe>
				</div>
			<!-- <div class="details">
			</div> -->
		</c:when>
		<c:otherwise>
				<div class="thumb">
					<img title="${headline}" alt="${media.altText}" src="${media.url}">
				</div>
				<div class="crt-parallaxvideo"> ${encodedUrl}
				<img title="${headline}" alt="${media.altText}" src="${media.url}">
				 <iframe id="video" src="${encodedUrl}" frameborder="0" allowfullscreen> </iframe>
				</div>
			
				
		</c:otherwise>
	</c:choose> --%>
	
	
	<c:if test="${not empty urlLink}">
	<c:url value="${urlLink}" var="encodedUrl"/>
	<iframe id="video" src="${encodedUrl}" frameborder="0" allowfullscreen> </iframe>
	</c:if>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:forEach items="${medias}" var="media" varStatus="status">
			<c:choose>
				<c:when test="${fn:length(medias) ge 2}">
					<c:if test="${empty imagerData1 && media.width le 768}">
						<c:set var="imagerData1">
							<source srcset="${media.url}">
						</c:set>
					</c:if>
					<c:if test="${empty imagerData2 && media.width ge 769}">
						<c:set var="imagerData2">
							 <source media="(min-width: 768px)" srcset="${media.url}">
						</c:set>
						<c:set var="mediaUrl">
							${media.url}
						</c:set>
					</c:if>
				</c:when>
				<c:otherwise>
					<c:set var="imagerData3"><img src="${media.url}" alt="${altText}" /></c:set>
				</c:otherwise>
			</c:choose>
	<c:if test="${empty altText}">
		<c:set var="altText" value="${media.altText}"/>
	</c:if>
</c:forEach>

<c:url value="${urlLink}" var="encodedUrl" />
<div class="simple-responsive-banner-component">
	<c:choose>
		<c:when test="${empty encodedUrl || encodedUrl eq '#'}">
			<c:choose>
				<c:when test="${not empty imagerData1 && not empty imagerData2}">
					<picture>
					   ${imagerData2}
					   ${imagerData1}
					   <img src="${mediaUrl}" alt="${altText}" />
					</picture>
				</c:when>
				<c:otherwise>
					  ${imagerData3}
				</c:otherwise>
				</c:choose>
			</c:when>
		<c:otherwise>
				<a href="${encodedUrl}">
					<c:choose>
				<c:when test="${not empty imagerData1 && not empty imagerData2}">
					<picture>
					   ${imagerData2}
					   ${imagerData1}
					   <img src="${mediaUrl}" alt="${altText}" />
					</picture>
				</c:when>
				<c:otherwise>
					  ${imagerData3}
				</c:otherwise>
				</c:choose>
				</a>
		</c:otherwise>
	</c:choose>
</div>

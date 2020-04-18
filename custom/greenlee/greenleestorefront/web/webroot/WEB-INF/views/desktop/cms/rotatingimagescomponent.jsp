<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<div class="slider_component simple-banner">

	
			
     		<%--   <h3>${banner.headline}</h3> --%>
     		  <div class="owl-carousel owl-theme">
			<c:forEach items="${banners}" var="banner" varStatus="status">	
     		
         	 <div class="item">
				<c:if test="${ycommerce:evaluateRestrictions(banner)}">
				<div class="media">
				<c:url value="${banner.urlLink}" var="encodedUrl" />
					<a tabindex="-1" href="${encodedUrl}"<c:if test="${banner.external}"> target="_blank"</c:if>><img src="${banner.media.url}" alt="${not empty banner.headline ? banner.headline : banner.media.altText}" title="${not empty banner.headline ? banner.headline : banner.media.altText}"/></a>
					
					</div>
				<div class="mediacontent">
					${banner.content}
				</div>
				</c:if>
			</div>
			
			</c:forEach>
			</div>

	</div>

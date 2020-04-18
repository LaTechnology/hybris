<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ attribute name="galleryImages" required="true" type="java.util.List"%>

<c:set var="productVideolink" value="${product.videolink}"/>
<div class="carousel gallery-carousel js-gallery-carousel owl-carousel owl-theme">
	<c:forEach items="${galleryImages}" var="container" varStatus="varStatus">
		<a href="#" class="item"> 
			<img src="${container.cartIcon.url}" alt="${container.thumbnail.altText}">
		</a>
	</c:forEach>
	<c:if test="${not empty  productVideolink}">
		<a href="#" class="item">  
			<%-- <img src="https://img.youtube.com/vi/${fn:substringAfter(productVideolink,"?v=")}/0.jpg" alt=""> --%>
			<c:set var="text" value="${productVideolink}" />
			<c:set var="splittext" value="${fn:split(text,'/')}" />
					
			<c:if test="${fn:contains(productVideolink, '?')}">
   					<img src="https://img.youtube.com/vi/${fn:substring(productVideolink,fn:indexOf(productVideolink, splittext[fn:length(splittext)-1]),fn:indexOf(productVideolink,'?'))}/0.jpg" alt="">
			</c:if>
			<c:if test="${not fn:contains(productVideolink, '?')}">
   					<img src="https://img.youtube.com/vi/${splittext[fn:length(splittext)-1]}/0.jpg" alt="">
			</c:if>
			
		</a>
	</c:if>
</div>
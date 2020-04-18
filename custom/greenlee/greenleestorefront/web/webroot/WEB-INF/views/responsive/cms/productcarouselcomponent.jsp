<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component" %>


	<c:choose>
	<c:when test="${not empty productData}">
		<div class="product-carousel product-list">
			<div class="container">
        	<h2>${title}</h2>
        	<div id="owl-carousel" class="orange-carousel product">

			<c:choose>
				<c:when test="${component.popup}">
					<!-- <div class="carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference"> -->
						<div id="quickViewTitle" class="quickView-header" style="display:none">
							<div class="headline">		
								<span class="headline-text"><spring:theme code="popup.quick.view.select"/></span>
							</div>
						</div>
						<c:forEach items="${productData}" var="product">
							<div class="item">
							<c:url value="${product.url}/quickView" var="productQuickViewUrl"/>
							<product:productPrimaryImage product="${product}" format="product"/>
								<a href="${productQuickViewUrl}" class="js-reference-item">
								<product:productPrimaryReferenceImage product="${product}" format="product"/>
								 <h3>${product.name}</h3>
								</a>
								<span class="price"><format:fromPrice priceData="${product.price}"/></span>
								<p>${product.description}</p>
							</div>
				</c:forEach>
				</c:when>
					<c:otherwise>
					
					<c:forEach items="${productData}" var="product">

							<c:url value="${product.url}/quickView" var="productQuickViewUrl"/>
							<div class="item">
							<product:productPrimaryImage product="${product}" format="product"/>
								<a href="${productQuickViewUrl}" class="js-reference-item">
								<product:productPrimaryReferenceImage product="${product}" format="product"/>
								 <h3> ${product.name}</h3>
								</a>
								<span class="price"><format:fromPrice priceData="${product.price}"/></span>
								<p>${product.description}</p>
							</div>						
				</c:forEach>
					
				</c:otherwise>
				</c:choose>
				</div>
				</div>
				</div>
	</c:when>

	<c:otherwise>
		<component:emptyComponent/>
	</c:otherwise>
</c:choose>


<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<div id="addToCartTitle" style="display: none">
	<spring:theme code="basket.added.to.basket" />
</div>
<div id="quickViewTitle" class="quickView-header" style="display: none">
	<spring:theme code="popup.quick.view.select" />
</div>

<c:choose>
	<c:when
		test="${not empty suggestions and component.maximumNumberProducts > 0}">
		<div class="product-carousel product-list">
			<div class="container">
				<div class="headline">
					<h2>${component.title}</h2>
				</div>
				<div id="prd-carousel" class="orange-carousel product owl-carousel owl-theme cart-carousel">
					
							<c:forEach end="${component.maximumNumberProducts}"
								items="${suggestions}" var="suggestion">
								<c:url value="${suggestion.url}"
									var="productQuickViewUrl" />
								
									<div class="item">
										<a href="${productQuickViewUrl}">
										
											
											<product:productPrimaryReferenceImage product="${suggestion}"
												format="product" /> <c:if
												test="${component.displayProductTitles}">
												<%-- <div class="item-name">${suggestion.name}</div> --%>
												<h3>${suggestion.name}</h3>
											</c:if> <c:if test="${component.displayProductPrices}">
												<%-- <div class="item-price"><format:fromPrice priceData="${suggestion.price}"/></div> --%>
												<span class="price"> <format:fromPrice
														priceData="${suggestion.price}" /></span>
											</c:if> <c:if test="${component.displayProductTitles}">
												<%-- <div class="item-name">${suggestion.description}</div> --%>
												<div class="summary">${suggestion.description}</div>
											</c:if>
										</a>
									</div>
							
							</c:forEach>
						

				</div>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<component:emptyComponent />
	</c:otherwise>
</c:choose>

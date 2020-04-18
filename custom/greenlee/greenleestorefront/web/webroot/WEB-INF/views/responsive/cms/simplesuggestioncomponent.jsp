<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>

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
				<div id="owl-carousel"
					class="carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference orange-carousel product owl-carousel owl-theme owl-loaded">
					<div class="owl-stage-outer">
						<div class="owl-stage"
							style="transform: translate3d(0px, 0px, 0px); transition: 0s; width: 2790px;">
							<c:forEach end="${component.maximumNumberProducts}"
								items="${suggestions}" var="suggestion">
								<c:url value="${suggestion.url}/quickView"
									var="productQuickViewUrl" />
								<div class="owl-item active"
									style="width: 310px; margin-right: 0px;">
									<div class="item">
										<a href="${productQuickViewUrl}" class="js-reference-item">
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
												<p>${suggestion.description}</p>
											</c:if>
										</a>
									</div>
								</div>
							</c:forEach>
						</div>
					</div>

				</div>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<component:emptyComponent />
	</c:otherwise>
</c:choose>

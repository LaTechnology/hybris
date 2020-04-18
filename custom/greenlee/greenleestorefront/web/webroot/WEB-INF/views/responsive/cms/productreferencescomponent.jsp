<%@ page trimDirectiveWhitespaces="true"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>


<c:choose>
	<c:when
		test="${not empty productReferences and component.maximumNumberProducts > 0}">

		<c:choose>
			<c:when test="${component.productReferenceTypes eq '[SPAREPART]' }">
				<div class="maintenence-product">
					<div class="container">
						<h2>Maintenance Upgrades</h2>
						<h4>for AirScout 306 Wifi Testing Kit</h4>
						<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.
							Aenean euismod bibendum laoreet.</p>

						<div class="product">
							<c:forEach end="${component.maximumNumberProducts}"
								items="${productReferences}" var="productReference">
								<div class="item">
									<a href="${productReference.target.url}"><product:productPrimaryReferenceImage
											product="${productReference.target}" format="product" />
										<h3>${productReference.target.name}</h3></a>
									<!-- <span class="price"> $ 5,325</span> -->
									<format:fromPrice priceData="${productReference.target.price}" />
									<p>${productReference.target.description}</p>
									<form method="get" id="addToCartForm2"
										class="add_to_cart_form" action="${addToCartUrl}">
										<input type="hidden" maxlength="3" size="1" id="qty"
											name="qty" class="qty js-qty-selector-input" value="1">
										<input type="hidden" name="productCodePost"
											value="${product.code}" />
										<input type="hidden" name="CSRFToken"
											value="${CSRFToken}" />	
										<button id="addToCartButton2" type="submit"
											class="btn btn-primary btn-block js-add-to-cart">
											<i class="fa fa-shopping-cart hidden-xs"></i>Add to cart
										</button>
									</form>
								</div>
							</c:forEach>
						</div>
					</div>
				</div>
			</c:when>
			<c:when test="${component.productReferenceTypes eq '[SIMILAR]' }">
				<div class="product-carousel product-list">
					<div class="container">
						<h2>Related products and accessories</h2>
						<div id="owl-carousel" class="orange-carousel product">
							<c:forEach end="${component.maximumNumberProducts}"
								items="${productReferences}" var="productReference">
								<div class="item">
									<a href="${productReference.target.url}"><product:productPrimaryReferenceImage
											product="${productReference.target}" format="product" />
										<h3>${productReference.target.name}</h3></a>

									<!-- <span class="price"> $ 5,325</span> -->
									<format:fromPrice priceData="${productReference.target.price}" />
									<p>${productReference.target.description}</p>
								</div>
							</c:forEach>
						</div>
					</div>
				</div>
			</c:when>
		</c:choose>
	</c:when>

	<c:otherwise>
		<component:emptyComponent />
	</c:otherwise>
</c:choose>
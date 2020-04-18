<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>

<c:if test="${not empty cartData.entries}">

	<c:url value="/cart/checkout" var="checkoutUrl" scope="session" />
	<c:url value="/cart/realTimePriceCheck" var="pricingUrl" scope="session" />
	<c:url value="${continueUrl}" var="continueShoppingUrl" scope="session" />

	<c:set var="showTax" value="false" />
		<div class="col-md-9">
			<div class="cart-wrp">
				<div class="cart-data">
					<span class="cartId cart"> <spring:theme
							code="basket.page.cartId" />&nbsp;<span class="cartIdNr">#
							${cartData.code}</span>
					</span>
					<div class="cart-btn-group">
					<span>
					<a href="${continueShoppingUrl}"><spring:theme
								text="Continue Shopping" code="cart.page.continue" /></a>
					</span>

					<c:if
						test="${not cartData.isErpPrice and (user.sessionB2BUnit.userType == 'B2B')}">
						<span class="cart-distributor hidden-lg hidden-md">
						<ycommerce:testId code="checkoutButton">
							<button
								class="btn-white btn btn-primary btn-block checkoutButton "
								data-checkout-url="${pricingUrl}">
								<spring:theme code="checkout.distributorprice" />
							</button>
						</ycommerce:testId>
						</div>
					</c:if>
					</div>

					<c:if
						test="${not cartData.isErpPrice and (user.sessionB2BUnit.userType == 'B2B')}">
						<div class="cart-distributor hidden-xs hidden-sm">
						<ycommerce:testId code="checkoutButton">
							<button
								class="btn-white btn btn-primary btn-block checkoutButton"
								data-checkout-url="${pricingUrl}">
								<spring:theme code="checkout.distributorprice" />
							</button>
						</ycommerce:testId>
						</div>
					</c:if>
				</div>
				</div>
		</div>
		<div class="col-md-9">
			<cart:cartItems cartData="${cartData}" />
		</div>
</c:if>

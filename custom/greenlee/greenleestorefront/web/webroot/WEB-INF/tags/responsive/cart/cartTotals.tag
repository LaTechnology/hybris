<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="cartData" required="true"
	type="de.hybris.platform.commercefacades.order.data.CartData"%>
<%@ attribute name="showTax" required="false" type="java.lang.Boolean"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:url value="/cart/checkout" var="checkoutUrl" scope="session" />
<div class="col-md-3">
	<div class="cart-block">
		<div class="cart-title">Order Summary</div>
		<table class="cart-summary">
			<tbody>
				<tr class="subtotal">
					<td><spring:theme code="basket.page.totals.subtotal" /></td>
					<td class="subtotal text-right"><format:price
							priceData="${cartData.subTotal}" /></td>
				</tr>
				<tr>
					<td><spring:theme code="basket.page.totals.savings" /></td>
					<td class="text-right"><format:price
							priceData="${cartData.totalDiscounts}" /></td>
				</tr>
			</tbody>
			<tfoot>
				<tr>
					<th class="ct-total"><spring:theme
							code="basket.page.totals.total" /></th>
					<th class="checkout-total"><format:price
							priceData="${cartData.totalPrice}" /></th>
				</tr>
			</tfoot>
		</table>
		<a href="${checkoutUrl}" class="btn"><spring:theme
				code="checkout.checkout" /></a>
	</div>
	<div class="ct-helpblock">
		<div class="cart-help">
			<div class="help-info">
				<h5>
					<i class="fa fa-question-circle"></i>Any questions?
				</h5>
				<a class="support" href="tel:1-800-198-9384">1-800-198-9384</a> <a
					href="mailto:support@airscoutwifi.com">support@airscoutwifi.com</a>
			</div>
		</div>
	</div>
</div>

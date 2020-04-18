<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData"%>
<%@ attribute name="showTax" required="false" type="java.lang.Boolean"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="multi-checkout-b2b"	tagdir="/WEB-INF/tags/addons/b2bacceleratoraddon/desktop/checkout/multi"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ attribute name="afterSimulate" required="false" type="java.lang.Boolean"%>

<c:url value="/cart/checkout" var="checkoutUrl" scope="session" />
<c:url value="/cart" var="cartUrl" />

<div class="cart-block">
	<div class="cart-title">
		<spring:theme code="checkout.multi.order.summary.title" /><a class="pencil-link" href="${cartUrl}"><i
			class="fa fa-pencil-square-o"></i></a>
	</div>
	<div class="check-count">(${fn:length(cartData.entries)} items)</div>
	<table class="cart-summary">
		<tbody>
			<tr class="subtotal">
				<td><spring:theme code="basket.page.totals.subtotal" /></td>
				<td class="subtotal text-right"><format:price
						priceData="${cartData.subTotal}" /></td>
			</tr>
			<tr>
				<td><spring:theme code="basket.page.totals.delivery"/></td>
				<td class="text-right">				
					<ycommerce:testId code="Order_Totals_Delivery">
						<format:orderSummaryPrice priceData="${cartData.deliveryCost}" sapPriceAvailability="${cartData.sapPriceAvailability}" deliveryAddress="${cartData.deliveryAddress}"/>
					</ycommerce:testId>
				</td>
			</tr>
		<%-- 		
			<c:if test="${cartData.net && cartData.totalTax.value > 0 && showTax}">
		 --%>			
 			<c:if test="${cartData.totalTax.value > 0}">
				<tr>
					<td><spring:theme code="basket.page.totals.netTax"/></td>
					<td class="text-right"><format:price priceData="${cartData.totalTax}"/></td>
				</tr>
			</c:if>
			<c:if test="${cartData.totalDiscounts.value > 0}">					
				<tr>
					<td><spring:theme code="basket.page.totals.savings" /></td>
					<td class="text-right"><format:price
							priceData="${cartData.totalDiscounts}" /></td>
				</tr>
			</c:if>
			
			<c:if test="${afterSimulate and not empty cartData.duty and cartData.duty.value > 0 and cartData.sapPriceAvailability}">
				<tr>
					<td><spring:theme code="basket.page.totals.duty" /></td>
					<td class="text-right"><format:price
							priceData="${cartData.duty}" /></td>
				</tr>
			</c:if>
		</tbody>
		<tfoot>
			<tr>
				<th class="ct-total"><spring:theme
						code="basket.page.totals.total" /></th>
				<th class="checkout-total">
				<format:priceCurrency priceData="${cartData.totalPrice}"/>
				<format:price priceData="${cartData.totalPrice}" /></th>
			</tr>
		</tfoot>
	</table>
</div>
<multi-checkout-b2b:deliveryCartItems cartData="${cartData}" />
<div class="ct-helpblock">
	<div class="cart-help">
		<div class="help-info">
			<h5>
				<i class="fa fa-question-circle"></i><spring:theme
						code="text.anyquestions" />
			</h5>
			<a class="support" href="tel:<spring:theme code='productDetailPage.supportTel' />"><spring:theme
						code="productDetailPage.supportTel" /></a> <a
				href="mailto:<spring:theme code='productDetailPage.supportEmail' />"><spring:theme
						code="productDetailPage.supportEmail" /></a>
		</div>
	</div>
</div>


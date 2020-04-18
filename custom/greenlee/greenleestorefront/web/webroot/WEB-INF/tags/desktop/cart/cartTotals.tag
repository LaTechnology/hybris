<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="cartData" required="true"
	type="de.hybris.platform.commercefacades.order.data.CartData"%>
	<%@ attribute name="vouchersInCart" required="false"
	type="java.util.List"%>
<%@ attribute name="showTax" required="false" type="java.lang.Boolean"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart"%>

<c:url value="/cart/checkout" var="checkoutUrl" scope="session" />
<div class="col-md-3">
	<div class="cart-block">
		<div class="cart-title"><spring:theme code="cart.text.ordersummary" /></div>
		
		
		
		
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
	
				
				<tr>
		<c:if test="${user.sessionB2BUnit.userType ne 'B2B'}">
		<td colspan="2" class="voucher-cart">	
		<h5><spring:theme code="vouchers.form.txt.codeheading" /></h5>		
				<cart:vouchers vouchersInCart="${vouchersInCart}"/>
			
		</td>
		</c:if>
		</tr>
			</tbody>
			<tfoot>
				<tr>
					<th class="ct-total"><spring:theme
							code="basket.page.totals.total" /></th>
					<th class="checkout-total">
					<format:priceCurrency priceData="${cartData.subTotalWithDiscounts}"/>
					<format:price priceData="${cartData.subTotalWithDiscounts}"/>
					</th>
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
					<i class="fa fa-question-circle"></i><spring:theme code="cart.text.anyquestions" />
				</h5>
				<a class="support" href="tel:<spring:theme code="cart.text.phonenumber" />"><spring:theme code="cart.text.phonenumber" /></a> <a
					href="mailto:<spring:theme code="cart.text.email" />"><spring:theme code="cart.text.email" /></a>
			</div>
		</div>
	</div>
</div>

<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<spring:theme code="text.addToCart" var="addToCartText"/>
<spring:theme code="text.popupCartTitle" var="popupCartTitleText"/>
<c:url value="/cart" var="cartUrl"/>
<c:url value="/cart/checkout" var="checkoutUrl"/>

 
<div class="mini-cart--header">
	<div class="row">
		<div class="col-xs-6">
			<h5><spring:theme code="text.cart" arguments="${numberItemsInCart}"/></h5>
		</div>
		<div class="col-xs-6 text-right">
			<a href="${cartUrl}"><spring:theme code="popup.cart.viewall"/></a>
		</div>
	</div>
</div>
<div class="mini-cart--body">
	<ol class="mini-cart-list">
	<c:forEach items="${entries}" var="entry" end="${numberShowing - 1}">
								<c:url value="${entry.product.url}" var="entryProductUrl"/>
		<li class="mini-cart-item">
			<div class="thumb">
				<a href="${entryProductUrl}">
					<product:productPrimaryImage product="${entry.product}" format="cartIcon"/>
				</a>
			</div>
			<div class="details">
				<a class="name" href="${entryProductUrl}">${entry.product.name}</a>
				<div class="qty"><spring:theme code="popup.cart.quantity"/>: ${entry.quantity}</div>
	           	<div class="price"><format:price priceData="${entry.basePrice}"/></div>
			</div>				
				 <a href="#" class="remove-entry-button" id="removeEntry_0">
	                <i class="fa fa-trash"></i>
				</a>
		</li>
		</c:forEach>
	</ol>	
</div>
<div class="mini-cart--footer">
	<div class="mini-cart-totals">
							<span class="key"><spring:theme code="popup.cart.total"/></span>
							<span class="value"><format:price priceData="${cartData.totalPrice}"/></span>
	</div>
	<div class="mini-cart-check">
							<a href="${cartUrl}" class="btn btn-primary btn-block mini-cart-checkout-button">
								<spring:theme code="checkout.checkout" /></a>
	</div>
						

</div>
 


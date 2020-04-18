<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="order" required="true"
	type="de.hybris.platform.commercefacades.order.data.OrderData"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>

<%@ attribute name="containerCSS" required="false"
	type="java.lang.String"%>

<!--  <div class="account-orderdetail-orderPromotion-section text-right">
	<c:if test="${not empty order.appliedOrderPromotions}">
	    <ycommerce:testId code="order_recievedPromotions_label">
	        <c:forEach items="${order.appliedOrderPromotions}" var="promotion">
	            <div class="orderPromotion">
	            	<ycommerce:testId code="orderDetails_orderPromotion_label">
	            		${promotion.description}
	            	</ycommerce:testId>
	            </div>
	        </c:forEach>
	    </ycommerce:testId>
	</c:if>
</div>-->

<h3>
	<spring:theme code="text.account.order.orderTotals" text="Order Total" />
</h3>
<ul class="table">
	<li>
		<p>
			<spring:theme code="text.account.order.subtotal" text="Subtotal:" />
		</p>
		<p>
			<ycommerce:testId code="orderTotal_subTotalWithDiscounts_label">
				<format:price priceData="${order.subTotal}" />
			</ycommerce:testId>
		</p>
	</li>

	<li>
		<p>
			<spring:theme code="text.account.order.savings" text="Savings:" />
		</p>
		<p>
			<format:price priceData="${order.totalDiscounts}" />
		</p>
	</li>

	<li>
		<p>
			<spring:theme code="text.account.order.shipping" text="Shipping:" />
		</p>
		<p>
			<ycommerce:testId code="orderTotal_devlieryCost_label">
				<format:orderSummaryPrice priceData="${order.deliveryCost}"
					sapPriceAvailability="${order.sapPriceAvailability}"
					deliveryAddress="${order.deliveryAddress}" />
				<%-- <format:price priceData="${order.deliveryCost}" displayFreeForZero="true"/> --%>
			</ycommerce:testId>
		</p>
	</li>

	<li>
		<p>
			<spring:theme code="text.account.order.netTax" text="Tax:" />
		</p>
		<p>
			<format:price priceData="${order.totalTax}" />
		</p>
	</li>
	<li>
		<p>
			<spring:theme code="text.account.order.duty" text="Duty:" />
		</p>
		<p>
			<format:price priceData="${order.duty}" />
		</p>
	</li>
	<li class="total">
		<p>
			<spring:theme code="text.account.order.total" text="Total:" />
		</p>
		<p>
			<c:choose>
				<c:when test="${order.net}">
					<format:price priceData="${order.totalPriceWithTax}" />
				</c:when>
				<c:otherwise>
					<ycommerce:testId code="orderTotal_totalPrice_label">
						<format:priceCurrency priceData="${order.totalPrice}" />
						<format:price priceData="${order.totalPrice}" />
					</ycommerce:testId>
				</c:otherwise>
			</c:choose>
		</p>
	</li>
	<%-- <li class="promocode-msg"><spring:theme
			code="order.onfirmation.with.promo.codes"
			text="If you applied a promo code" /></li> --%>
	<!-- Commented as per GRE-940 -->
	<%-- <li>
		<c:if test="${order.totalDiscounts.value > 0}">
		    <div class="account-orderdetail-orderTotalDiscount-section">
		        <c:if test="${not order.net}">
		            <div class="order-total-taxes">
		                <ycommerce:testId code="orderTotal_includesTax_label">
		                    <spring:theme code="text.account.order.total.includesTax" arguments="${order.totalTax.formattedValue}"/>
		                </ycommerce:testId>
		            </div>
		        </c:if>
				<br />
		        <ycommerce:testId code="order_totalDiscount_label">
		            <div class="order-total-savings">
		                <spring:theme code="text.account.order.totalSavings" arguments="${order.totalDiscounts.formattedValue}"/>
		            </div>
		        </ycommerce:testId>
		    </div>
		</c:if>
	</li> --%>
</ul>

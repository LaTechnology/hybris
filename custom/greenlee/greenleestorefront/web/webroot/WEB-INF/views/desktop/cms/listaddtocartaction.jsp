<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:url value="${url}" var="addToCartUrl"/>
<form:form id="addToCartForm${product.code}" action="${addToCartUrl}" method="post" class="add_to_cart_form">
	
	<ycommerce:testId code="addToCartButton">
		<input type="hidden" name="productCodePost" value="${product.code}"/>
		<input type="hidden" name="productNamePost" value="${product.name}"/>
		<input type="hidden" name="productPostPrice" value="${product.price.value}"/>
		
		<c:if test="${empty showAddToCart ? true : showAddToCart}">
			<c:set var="buttonType">submit</c:set>

			<c:if test="${product.purchasable and product.stock.stockLevelStatus.code ne 'outOfStock' }">
				<c:set var="buttonType">submit</c:set>
			</c:if>
		
			 <c:if test="${not empty product.price and product.price.currencyIso eq currentCurrency.isocode}">
				 <c:set var="yes2AddToCart" value= "${true}"/>
			 </c:if>		
		<c:choose>
		<c:when test="${yes2AddToCart and product.showAddToCart}">
		<c:choose>
			<c:when test="${fn:contains(buttonType, 'submit')}">
				<button id="addToCartButton" type="${buttonType}" class="btn btn-primary btn-block js-add-to-cart" disabled="disabled">
					<spring:theme code="basket.add.to.basket"/><i class="fa fa-shopping-cart"></i>
				</button>
			</c:when>
			<c:otherwise>
				
				<a href='<c:url value="/contactus"></c:url>' title="Where to Buy" class="btn btn-primary btn-block"><spring:theme code="home.wheretobuy"/></a>
			</c:otherwise>
		</c:choose>
		</c:when>
		<c:otherwise>
			<a href='<c:url value="/contactus"></c:url>' title="Where to Buy" class="btn btn-primary btn-block"><spring:theme code="home.wheretobuy"/></a>
		</c:otherwise>
		</c:choose>		
	</c:if>
	</ycommerce:testId>
</form:form>


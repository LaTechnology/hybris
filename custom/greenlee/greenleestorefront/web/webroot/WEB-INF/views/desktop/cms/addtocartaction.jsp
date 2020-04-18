<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<c:url value="${url}" var="addToCartUrl"/>
<form:form method="post" id="addToCartForm" class="add_to_cart_form span-5" action="${addToCartUrl}">
	<c:if test="${product.purchasable}">
		<input type="hidden" maxlength="3" size="1" id="qty" name="qty" class="js-qty-selector-input" value="1">
	</c:if>
	<input type="hidden" name="productCodePost" value="${product.code}"/>

	<c:choose>
		<c:when test="${product.showAddToCart and product.purchasable}">
			<button id="addToCartButton" type="${buttonType}" class="btn btn-primary btn-block js-add-to-cart" disabled="disabled">
				<spring:theme code="basket.add.to.basket"/><i class="fa fa-shopping-cart"></i>
			</button>
		</c:when>
		<c:otherwise>
			<a href='<c:url value="/contactus"></c:url>' title="Where to Buy" class="btn btn-primary btn-block"><spring:theme code="home.wheretobuy"/></a>
		</c:otherwise>
	</c:choose>		
	
</form:form>

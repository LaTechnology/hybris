<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/responsive/action" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>


<spring:theme code="text.addToCart" var="addToCartText"/>
<c:url value="${product.url}" var="productUrl"/>
<c:set value="${not empty product.potentialPromotions}" var="hasPromotion"/>
<div class="item">
	<div class="glist-image">
		<a class="thumb" href="${productUrl}" title="${product.name}">
			<product:productPrimaryImage product="${product}" format="thumbnail"/>
		</a>
	</div>
	<div class="glist-thumb">
		<h3>
		<a class="thumb" href="${productUrl}" title="${product.name}">
			${product.name}
		</a>
		</h3>
		<div class="prdt-desc">
			<p>${product.summary}</p>
		</div>
		
	</div>
	<div class="glist-details">
		<span class="price"> <format:price priceData="${product.price}"/></span>
	
		<c:set var="product" value="${product}" scope="request"/>
		<c:set var="addToCartText" value="${addToCartText}" scope="request"/>
		<c:set var="addToCartUrl" value="${addToCartUrl}" scope="request"/>
		<action:actions element="div" parentComponent="${component}"  />	
	</div>
</div>
<%-- <li class="product-item">
	<ycommerce:testId code="test_searchPage_wholeProduct">
		
		
		<ycommerce:testId code="searchPage_productName_link_${product.code}">
			<a class="name" href="${productUrl}">${product.name}</a>
		</ycommerce:testId>

		<div class="price-panel">
			<c:if test="${not empty product.potentialPromotions}">
				<div class="promo">
					<c:forEach items="${product.potentialPromotions}" var="promotion">
						${promotion.description}
					</c:forEach>
				</div>
			</c:if>

			<ycommerce:testId code="searchPage_price_label_${product.code}">
				<div class="price"><format:price priceData="${product.price}"/></div>
			</ycommerce:testId>
		</div>

		<c:if test="${not empty product.summary}">
			<div class="description">${product.summary}</div>
		</c:if>



		<c:set var="product" value="${product}" scope="request"/>
		<c:set var="addToCartText" value="${addToCartText}" scope="request"/>
		<c:set var="addToCartUrl" value="${addToCartUrl}" scope="request"/>

		<div class="addtocart">
			<div id="actions-container-for-${component.uid}" class="row">
				<action:actions element="div" parentComponent="${component}"  />
			</div>
		</div>

	</ycommerce:testId>
</li>

 --%>






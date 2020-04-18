<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/desktop/action" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
 
<c:if test="${data ne 'content'}">
<spring:theme code="text.addToCart" var="addToCartText"/>
<c:url value="${product.url}" var="productUrl"/>
<c:set value="${not empty product.potentialPromotions}" var="hasPromotion"/>
<div class="item">
	<div class="glist-image">
		<a class="thumb" href="${productUrl}" title="${product.name}">
			<product:productPrimaryImage product="${product}" format="product"/>
		</a>
	</div>
	<div class="glist-thumb">
		<div class="checkbox checkbox-group glcheckbox">
				<spring:theme code="productcomparison.button.compare_items" var="compareBtnLabel" />
				<c:set var="comparable" value="${not empty pcCodes[product.code]}" />
				<c:url value="/compare" var="pcUrl" />
				<input class="remember" data-product="${product.code}" type="checkbox" ${comparable ? 'checked=checked' : ''} id="compare-${product.code}"  onclick="pcUpdateComparableState('${product.code}','${pcUrl}', this, '${compareBtnLabel}')">
				<label for="compare-${product.code}"><spring:theme code="productcomparison.checkbox.compare" /></label>
		  </div>
		<h3>
		<a class="thumb" href="${productUrl}" title="${product.name}">
			${product.name}
			<br>
			${product.catalogNumber}
		</a>
		
		</h3>
		<div class="prdt-desc">
			<p>${product.summary}</p>
		</div>
		
	</div>
	<div class="glist-details">
		<c:if test="${product.showPrice == 'true'}">
		<span class="price"> <format:price priceData="${product.price}"/></span>
		</c:if>
		<c:set var="product" value="${product}" scope="request"/>
		<c:set var="addToCartText" value="${addToCartText}" scope="request"/>
		<c:set var="addToCartUrl" value="${addToCartUrl}" scope="request"/>
		<action:actions element="div" parentComponent="${component}"  />	
	</div>
</div>
</c:if>


<c:if test="${data=='content'}">
 		<c:forEach items="${contentSearchPageData.results}" var="content">
				<div>
				<h3><a href="${content.url}">${content.title}</a></h3>
				<p>${content.text}</p>
				</div>
		</c:forEach>	
</c:if>
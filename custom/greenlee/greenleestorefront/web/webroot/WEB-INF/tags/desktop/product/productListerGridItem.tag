<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/desktop/action" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>

<spring:theme code="text.addToCart" var="addToCartText"/>
<c:url value="${product.url}" var="productUrl"/>
<c:set value="${not empty product.potentialPromotions}" var="hasPromotion"/>


	<div class="item">
		<a class="thumb" href="${productUrl}" title="${product.name}">
			<product:productPrimaryImage product="${product}" format="product"/>
		</a>	
		<c:set var="product" value="${product}" scope="request"></c:set>
		<c:set var="addToCartText" value="${addToCartText}" scope="request"/>
		<c:set var="addToCartUrl" value="${addToCartUrl}" scope="request"/>
		<c:set var="isGrid" value="true" scope="request"/>

			<div class="actions-container-for-${component.uid}">
				<action:actions element="div" parentComponent="${component}"/>
			</div>
			<h3><a class="name" href="${productUrl}">${product.name} <br> ${product.catalogNumber}</a></h3>
			<span class="price"> <format:price priceData="${product.price}"/></span>
			<p>${product.summary}</p>
			<div class="checkbox checkbox-group glcheckbox">
					<spring:theme code="productcomparison.button.compare_items" var="compareBtnLabel" />
					<c:set var="comparable" value="${not empty pcCodes[product.code]}" />
					<c:url value="/compare" var="pcUrl" />
					<input class="remember" data-product="${product.code}" type="checkbox" ${comparable ? 'checked=checked' : ''} id="compare-${product.code}"  onclick="pcUpdateComparableState('${product.code}','${pcUrl}', this, '${compareBtnLabel}')">
					<label for="compare-${product.code}"><spring:theme code="productcomparison.checkbox.compare" /></label>
					
				  </div>
	</div>


<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
	
	<c:choose>
	<c:when test="${not empty productData}">
		<div class="product-carousel product-list ssdds">
			<div class="container">
        	<h2>${title}</h2>
        	<div id="prd-carousel" class="orange-carousel product owl-carousel owl-theme">

			<c:choose>
				<c:when test="${component.popup}">
						<div id="quickViewTitle" class="quickView-header" style="display:none">
							<div class="headline">		
								<span class="headline-text"><spring:theme code="popup.quick.view.select"/></span>
							</div>
						</div>
						<c:forEach items="${productData}" var="product">
							<div class="item">
							
							<c:set var="comparable" value="${not empty pcCodes[product.code]}" />
							<c:url value="${product.url}" var="productQuickViewUrl"/>
								<a href="${productQuickViewUrl}" class="js-reference-item">
								<product:productPrimaryReferenceImage product="${product}" format="pLPImage"/>
								 <h3>${product.name}</h3>
								</a>
								<span class="price"><format:fromPrice priceData="${product.price}"/></span>
								<p>${product.summary}</p>
								<div class="checkbox checkbox-group glcheckbox">
										<c:url value="/compare" var="pcUrl" />
			<spring:theme code="productcomparison.button.compare_items" var="compareBtnLabel" />
			<input type="checkbox" id="compare-${product.code}"  onclick="pcUpdateComparableState('${product.code}','${pcUrl}', this, '${compareBtnLabel}')" data-product="${product.code}">
			<label for="compare-${product.code}"><spring:theme code="productcomparison.checkbox.compare" /></label>
								</div>
							</div>
				</c:forEach>
				</c:when>
					<c:otherwise>
					
					<c:forEach items="${productData}" var="product">
							<c:set var="comparable" value="${not empty pcCodes[product.code]}" />
							<c:url value="${product.url}" var="productQuickViewUrl"/>
							<div class="item">
								
							<%-- <product:productPrimaryImage product="${product}" format="product"/> --%>
								<a href="${productQuickViewUrl}" class="js-reference-item">
								<product:productPrimaryReferenceImage product="${product}" format="pLPImage"/>
								 <h3> ${product.name}</h3>
								</a>
								<span class="price"><format:fromPrice priceData="${product.price}"/></span>
								<p>${product.summary}</p>
								<div class="checkbox checkbox-group glcheckbox">
															<c:url value="/compare" var="pcUrl" />
			<spring:theme code="productcomparison.button.compare_items" var="compareBtnLabel" />
			<input type="checkbox" id="compare-${product.code}" ${comparable ? 'checked=checked' : ''} onclick="pcUpdateComparableState('${product.code}','${pcUrl}', this, '${compareBtnLabel}')" data-product="${product.code}">
			<label for="compare-${product.code}"><spring:theme code="productcomparison.checkbox.compare" /></label>
								</div>
							</div>						
				</c:forEach>
					
				</c:otherwise>
				</c:choose>
				</div>
				</div>
				</div>
	</c:when>

	<c:otherwise>
		<component:emptyComponent/>
	</c:otherwise>
</c:choose>


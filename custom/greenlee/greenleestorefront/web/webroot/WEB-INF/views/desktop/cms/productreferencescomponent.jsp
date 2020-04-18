<%@ page trimDirectiveWhitespaces="true"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<spring:theme code="productcomparison.button.compare_items" var="compareBtnLabel" />
<c:url value="/compare" var="pcUrl" />

<c:url value="/cart/add" var="addToCartUrl" />
<c:choose>
	<c:when
		test="${not empty productReferences and component.maximumNumberProducts > 0}">

		<c:choose>
			<c:when test="${component.productReferenceTypes eq '[FOLLOWUP]' }">
				<div class="maintenence-product">
					<div class="container">
						<spring:theme text='${component.title}' arguments="${product.name}"/>
						<div class="product">
							<c:forEach end="${component.maximumNumberProducts}"
								items="${productReferences}" var="productReference">
								<div class="item">
									<a href="${productReference.target.url}"><product:productPrimaryReferenceImage
											product="${productReference.target}" format="pLPImage" />
											<div class="checkbox checkbox-group glcheckbox">
													<c:set var="comparable" value="${not empty pcCodes[productReference.target.code]}" />
													
													<input class="remember" type="checkbox" ${comparable ? 'checked=checked' : ''} id="compare-${productReference.target.code}"  onclick="pcUpdateComparableState('${productReference.target.code}','${pcUrl}', this, '${compareBtnLabel}')">
													<label for="compare-${productReference.target.code}"><spring:theme code="productcomparison.checkbox.compare" /></label>
											  </div>
									
										<h3>${productReference.target.name} </h3></a>
									<format:fromPrice priceData="${productReference.target.price}" />
									<p>${productReference.target.description}</p>
									<form method="post" id="addToCartForm2"
										class="add_to_cart_form" action="${addToCartUrl}">
										<input type="hidden" maxlength="3" size="1" id="qty"
											name="qty" class="qty js-qty-selector-input" value="1">
										<input type="hidden" name="productCodePost"
											value="${productReference.target.code}" />
										<input type="hidden" name="CSRFToken"
											value="${CSRFToken}" />	
										<button id="addToCartButton2" type="submit"
											class="btn btn-primary btn-block js-add-to-cart">
											<i class="fa fa-shopping-cart hidden-xs"></i>Add to cart
										</button>
									</form>
								</div>
							</c:forEach>
						</div>
					</div>
				</div>
			</c:when>
			<c:when test="${component.productReferenceTypes eq '[SIMILAR]' }">
				<div class="product-carousel product-list">
					<div class="container">
						<h2>${component.title}</h2>
						<div id="prd-carousel" class="orange-carousel product owl-carousel owl-theme">
							<c:forEach end="${component.maximumNumberProducts}"
								items="${productReferences}" var="productReference">
								<div class="item">
									<a href="${productReference.target.url}"><product:productPrimaryReferenceImage
											product="${productReference.target}" format="pLPImage" />
											<div class="checkbox checkbox-group glcheckbox">
													<c:set var="comparable" value="${not empty pcCodes[productReference.target.code]}" />
													<input class="remember" type="checkbox" ${comparable ? 'checked=checked' : ''} id="compare-${productReference.target.code}"  onclick="pcUpdateComparableState('${productReference.target.code}','${pcUrl}', this, '${compareBtnLabel}')">
													<label for="compare-${productReference.target.code}"><spring:theme code="productcomparison.checkbox.compare" /></label>
											  </div>
										<h3>${productReference.target.name}</h3></a>

									<!-- <span class="price"> $ 5,325</span> -->
									<format:fromPrice priceData="${productReference.target.price}" />
									<p>${productReference.target.description}</p>
								</div>
							</c:forEach>
						</div>
					</div>
				</div>
			</c:when>
		</c:choose>
	</c:when>

	<c:otherwise>
		<component:emptyComponent />
	</c:otherwise>
</c:choose>
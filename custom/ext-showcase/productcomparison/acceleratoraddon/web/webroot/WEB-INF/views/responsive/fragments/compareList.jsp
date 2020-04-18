<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<c:url value="/cart/add" var="addToCartUrl"/>
<c:set value="${productList.size() + 1}" var="colSpan"/>

<div class="resizeableColorbox">
	<div class="product_compare">
		<h1>
			<spring:theme code="productcomparison.header.compare_items" />
			${component.openPopup} {fn:length(productList}}
		</h1>
		<c:if test="${empty productList || fn:length(productList} <= 1}">
			<p class="empty">
				<spring:theme code="productcomparison.label.empty_list" />
			</p>
		</c:if>
		<c:if test="${not empty productList &&  fn:length(productList} > 1}">
			<a href="<c:url value="/"/>" class="btn btn-link pull-right js-productcomparison-clear" data-popup="${pcPopup}">
				<spring:theme code="productcomparison.button.clear_items"/>
			</a>
			<div class="table-responsive">
				<table>
					<tbody>
						<tr class="desc">
							<th id="product"><spring:theme
									code="productcomparison.label.product" /></th>
							<c:forEach items="${productList}" var="product" varStatus="row">
								<td class="title">
									${product.name}
								</td>
							</c:forEach>
						<tr>
						<tr class="pic">
							<th id="brand"><spring:theme
									code="productcomparison.label.image" /></th>
							<c:forEach items="${productList}" var="product" varStatus="row">
								<c:set value="${ycommerce:productImage(product, 'product')}"
									var="primaryImage" />
								<td headers="brand"><a
									href="<c:url value="${product.url}"/>" title="#" alt=""><c:choose>
											<c:when test="${not empty primaryImage}">
												<c:choose>
													<c:when test="${not empty primaryImage.altText}">
														<img src="${primaryImage.url}"
															alt="${fn:escapeXml(primaryImage.altText)}"
															title="${fn:escapeXml(primaryImage.altText)}" />
													</c:when>
													<c:otherwise>
														<img src="${primaryImage.url}"
															alt="${fn:escapeXml(product.name)}"
															title="${fn:escapeXml(product.name)}" />
													</c:otherwise>
												</c:choose></a> </c:when> <c:otherwise>
										<theme:image code="img.missingProductImage.${format}"
											alt="${fn:escapeXml(product.name)}"
											title="${fn:escapeXml(product.name)}" />
									</c:otherwise> </c:choose> <%-- <product:productPrimaryImage product="${product}" format="thumbnail" /> --%>
								</td>
							</c:forEach>
						</tr>
						<tr>
							<th id="reference"><spring:theme
									code="productcomparison.label.product_code" /></th>
							<c:forEach items="${productList}" var="product" varStatus="row">
								<td headers="reference">${product.code}</td>
							</c:forEach>
						</tr>
						<tr class="costs">
							<th id="price"><spring:theme
									code="productcomparison.label.price" /></th>
							<c:forEach items="${productList}" var="product" varStatus="row">
								<td headers="price" class="price">
									<format:fromPrice priceData="${product.price}" />
								</td>
							</c:forEach>
						</tr>
						<tr>
							<th id="image"><spring:theme
									code="productcomparison.label.brand" /></th>
							<c:forEach items="${productList}" var="product" varStatus="row">
								<td headers="image">${product.manufacturer} <!-- <img src="http://placehold.it/162x75"> -->
								</td>
							</c:forEach>
						</tr>
					<!--/top-->
					<!--middle-->
					<c:set value="${(not empty variantMap ? fn:length(variantMap) : 0) + fn:length(productList[0].classifications) }" var="countOfRows"/>
					<c:if test="${countOfRows > 0}">
						<c:if test="${not empty variantMap}">
							<td colspan="${colSpan}" class="headline">
								<spring:theme code="productcomparison.label.options" />
							</td>
							<c:forEach items="${variantMap}" var="variant" varStatus="variantStatus">
								<tr class="middle">
									<th>${variant.name}</th>
									<c:forEach items="${productList}" var="product" varStatus="row">

										<td headers="detail02 detail03">
										${variant.productAttrValueMap[product.code]}
											</td>
									</c:forEach>
								</tr>
							</c:forEach>
						</c:if>

						<c:forEach items="${productList[0].classifications}" var="classification" varStatus="classStatus">
							<td colspan="${colSpan}" class="headline">
								${classification.name}
							</td>
							<c:forEach items="${classification.features}" var="feature" varStatus="featureStatus">
								<tr class="middle">
									<th>${feature.name}</th>
									<c:forEach items="${productList}" var="product" varStatus="row">

										<td headers="detail02 detail03"><c:forEach
												items="${product.classifications[classStatus.index].features[featureStatus.index].featureValues}"
												var="featureValue" varStatus="fvStatus">
												<c:set value="${product.classifications[classStatus.index].features[featureStatus.index]}" var="specFeature"/>
										${featureValue.value}
										<c:choose>
									 		<c:when test="${specFeature.range}">
												${not fvStatus.last ? '-' : specFeature.featureUnit.symbol}
											</c:when>
											<c:otherwise>
												${specFeature.featureUnit.symbol}
												${not fvStatus.last ? '<br/>' : ''}
											</c:otherwise>
												</c:choose>
											</c:forEach></td>
									</c:forEach>
								</tr>
							</c:forEach>
						</c:forEach>
					</c:if>
					<!--/middle-->
					<!--bottom-->
					<tr>
						<th id="rating"><spring:theme code="productcomparison.label.rating"/>:</th>
						<c:forEach items="${productList}" var="product" varStatus="row">
							<td headers="rating"><c:if
									test="${not empty product.averageRating}">
									<div class="prod_review">
										<span class="stars large" style="display: inherit;"> <span
											style="width: <fmt:formatNumber maxFractionDigits="0" value="${product.averageRating * 24}" />px;"></span>
										</span>
										<p class="average-rating">
											<fmt:formatNumber maxFractionDigits="1"
												value="${product.averageRating}" />/5
										</p>
									</div>
									<!--/prod_review-->
								</c:if></td>
						</c:forEach>
					</tr>
					<tr>
						<th id="availability"><spring:theme code="productcomparison.label.availability"/>:</th>
						<c:forEach items="${productList}" var="product" varStatus="row">
							<td headers="availability"><spring:theme
									code="productcomparison.label.${product.stock.stockLevelStatus.code}" />
							</td>
						</c:forEach>
					</tr>
					<tr>
						<th id="add">&nbsp;</th>
						<c:forEach items="${productList}" var="product" varStatus="row">
							<td headers="add">
								<div class="prod_add_to_cart">
									<form:form method="post" id="addToCartForm" class="add_to_cart_form form-inline" action="${addToCartUrl}">
										<div class="form-group">
											<c:set var="buttonType">button</c:set>

											<c:if test="${product.purchasable and product.stock.stockLevelStatus.code ne 'outOfStock' }">
												<c:set var="buttonType">submit</c:set>
											</c:if>

											<c:if test="${product.purchasable}">
												<label for="qty"><spring:theme code="productcomparison.label.quantity" /></label>
												<input type="text" maxlength="3" size="1" id="qty_${product.code}" name="qty"
													class="qty form-control" value="1"  ${buttonType eq 'submit' ? '' : 'disabled=disabled'}>
											</c:if>
											<input type="hidden" name="productCodePost" value="${product.code}" />
										</div>

										<c:set var="allowAddToCart" value="true" />
										<c:if test="${allowAddToCart}">
											<c:choose>
												<c:when test="${fn:contains(buttonType, 'button')}">
													<button type="${buttonType}"
														class="btn btn-primary out-of-stock js-add-to-cartcd" disabled="true">
														<spring:theme code="productcomparison.label.outOfStock" />
													</button>
												</c:when>

												<c:otherwise>
													<button id="addToCartButton" type="submit" class="btn btn-primary js-add-to-cart">
														<spring:theme code="basket.add.to.basket" />
													</button>
												</c:otherwise>
											</c:choose>
										</c:if>
									</form:form>


									<!-- pickup in store -->
									<c:if test="${cmsSite.channel == 'B2C'}">
									<c:url var="pickUpInStoreFormAction" value="/store-pickup/${product.code}/pointOfServices"/>

									<div class="collect_from_store clear_fix">
									<c:choose>
									<c:when test="${cartPage}">
										<a href="javascript:void(0)" class="click_pickupInStore_Button" id="product_${product.code}${entryNumber}" data-productcart='<p><strong>${product.price.formattedValue}</strong></p><c:if test="${not empty product.baseOptions[0].selected.variantOptionQualifiers}"><c:forEach var="variant" items="${product.baseOptions[0].selected.variantOptionQualifiers}"><c:if test="${not empty variant.value}"><p><spring:theme code="basket.pickup.product.variant" arguments="${variant.name},${variant.value}" /></p></c:if></c:forEach></c:if>' data-img='<product:productPrimaryImage product="${product}" format="thumbnail"/>' data-productname="${product.name}" data-cartpage="${cartPage}" data-entryNumber="${entryNumber}"  data-actionurl="${pickUpInStoreFormAction}" data-value="${quantity}" >
										<c:choose>
										<c:when test="${not empty deliveryPointOfService}">
											<spring:theme code="basket.page.shipping.change.store"/>
										</c:when>
										<c:otherwise>
											<spring:theme code="basket.page.shipping.find.store" text="Find A Store"/>
										</c:otherwise>
									</c:choose>
									</a>
									</c:when>
									<c:when test="${searchResultsPage}">
									<button class="btn btn-secondary click_pickupInStore_Button" id="product_${product.code}${entryNumber}" type="submit"  data-productcart='<p><strong>${product.price.formattedValue}</strong></p><c:if test="${not empty product.baseOptions[0].selected.variantOptionQualifiers}"><c:forEach var="variant" items="${product.baseOptions[0].selected.variantOptionQualifiers}"><c:if test="${not empty variant.value}"><p><spring:theme code="basket.pickup.product.variant" arguments="${variant.name},${variant.value}" /></p></c:if></c:forEach></c:if>'  data-img='<product:productPrimaryImage product="${product}" format="thumbnail"/>' data-productname="${product.name}" data-cartpage="false" data-entryNumber="0"  data-actionurl="${pickUpInStoreFormAction}" data-value="1">
										<spring:theme code="pickup.in.store"/>
									</button>
									</c:when>
									<c:otherwise>

									<button class="btn btn-secondary click_pickupInStore_Button" id="product_${product.code}${entryNumber}" type="submit" data-productcart='<p><strong>${product.price.formattedValue}</strong></p><c:if test="${not empty product.baseOptions[0].selected.variantOptionQualifiers}"><c:forEach var="variant" items="${product.baseOptions[0].selected.variantOptionQualifiers}"><c:if test="${not empty variant.value}"><p><spring:theme code="basket.pickup.product.variant" arguments="${variant.name},${variant.value}" /></p></c:if></c:forEach></c:if>' data-img='<product:productPrimaryImage product="${product}" format="thumbnail"/>' data-productname="${product.name}" data-cartpage="false" data-entryNumber="0"  data-actionurl="${pickUpInStoreFormAction}" data-value="1">
										<spring:theme code="pickup.in.store"/>
									</button>
									</c:otherwise>
									</c:choose>
									</div>

									<div id="popup_store_pickup_form" class="pickup_store_search clearfix" style="display: none">
										<div class="item_container_holder clearfix">
											<div class="title_holder">
												<h2>
													<spring:theme code="pickup.product.availability" />
												</h2>
											</div>
										</div>

										<div class="prod_grid span-4">
											<span class="thumb"> </span> <strong class="details"></strong>
											<div class="cart">
												<p>
													<strong></strong>
												</p>
												<c:if test="${not empty product.baseOptions[0].selected.variantOptionQualifiers}">
													<c:forEach var="variant"
														items="${product.baseOptions[0].selected.variantOptionQualifiers}">
														<c:if test="${not empty variant.value}">
															<p>
																<spring:theme code="basket.pickup.product.variant"
																	arguments="${variant.name},${variant.value}" />
															</p>

														</c:if>
													</c:forEach>
												</c:if>
											</div>
											<div class="quantity pickup_store_search-quantity">
												<label data-for="pickupQty"><spring:theme
														code="basket.page.quantity" /></label> <input type="text" size="1"
													maxlength="3" data-id="pickupQty" name="qty" class="qty" />
											</div>
										</div>

										<div class="span-17 last">
											<div class="pickup_store_search-form">
												<form name="pickupInStoreForm" action="#" method="post"
													class="form_field-input">
													<label for="locationForSearch" class="nostyle"><spring:theme
															code="pickup.search.message" /></label> <input type="text"
														name="locationQuery" data-id="locationForSearch" /> <input
														type="hidden" name="cartPage" data-id="atCartPage"
														value="${cartPage}" /> <input type="hidden"
														name="entryNumber" value="${entryNumber}"
														class="entryNumber" />
													<button type="submit" class="form"
														data-id="pickupstore_search_button">
														<spring:theme code="pickup.search.button" />
													</button>
													<button type="submit" class="form"
														data-id="find_pickupStoresNearMe_button">
														<spring:theme code="storeFinder.findStoresNearMe" />
													</button>
												</form>
											</div>
											<div data-id="pickup_store_results"
												class="pickup_store_results"></div>
										</div>
									</div>
									</c:if>
								</div>
								<c:set var="itemRemoveIcon">
									<span class="glyphicon glyphicon-trash icon"></span><spring:theme code="text.remove" />
								</c:set>
								<c:choose>
									<c:when test="${pcPopup != null}">
										<a href='<c:url value="/"/>' class="btn btn-info js-productcomparison-remove" data-product-code="${product.code}" data-popup="${pcPopup}">
										   ${itemRemoveIcon}</a>
									</c:when>
									<c:otherwise>
										<a class="btn btn-info" href='<c:url value="/compare/remove?code=${product.code}"/>'>
											${itemRemoveIcon}</a>
									</c:otherwise>
								</c:choose>
							</td>
						</c:forEach>
					</tr>
				</table>
				<!--/bottom-->
			</div>
		</c:if>
	</div>
	<!--/product_compare-->
</div>
<script type="text/javascript">
	document.addEventListener('DOMContentLoaded', function load() {
		pcUpdateDiffRows();
		document.removeEventListener('DOMContentLoaded', load, false);
	}, false);
</script>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="storepickup"
	tagdir="/WEB-INF/tags/desktop/storepickup"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:url var="searchUrl" value="/search?q=%3Arelevance&text=#" />
<input type="hidden" id="cmsSiteChannel" value="${cmsSite.channel}" />
<div class="compare-heading clearfix">

	<h1>
		<spring:theme code="productcomparison.header.compare_items" />
		${component.openPopup}
	</h1>
	<c:if test="${not empty productList && fn:length(productList) gt 1}">
		<button type="button" class="neutral btn-white" id="pcClearBtnId"
			onclick="pcClearList('<c:url value="/"/>', '${pcPopup}')">
			<spring:theme code="productcomparison.button.clear_items" />
		</button>
	</c:if>
	<a href="${searchUrl}">View all models</a>


</div>
<!--/compare-heading-->
<c:if test="${empty productList || fn:length(productList) lt 2}">

	<div class="alert alert-info alert-dismissable">
		<i class="fa fa-exclamation-triangle fa-m"></i>
		<spring:theme code="productcomparison.label.empty_list" />

	</div>
</c:if>

<c:if test="${not empty productList && fn:length(productList) gt 1}">



	<div class="row product product-compare-list">
		<div class="col-md-offset-3 col-xs-offset-6">
			<div class="compare-carousel owl-carousel owl-theme"
				style="display: block">

				<c:forEach items="${productList}" var="product" varStatus="row">

					<div class="item">
						<div class="col-xs-12 col-md-12 prd">
							<div class="compare-prd">

								<c:set value="${ycommerce:productImage(product, 'pLPImage')}"
									var="primaryImage" />
								<a class="thumb" href="<c:url value="${product.url}"/>"
									title="${product.name}" alt=""> <c:choose>
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
											</c:choose></a>
								</c:when>
								<c:otherwise>
									<theme:image code="img.missingProductImage.${format}"
										alt="${fn:escapeXml(product.name)}"
										title="${fn:escapeXml(product.name)}" />
								</c:otherwise>
								</c:choose>

								<p>${product.catalogNumber}</p>

								<h3 class="thumb">${fn:escapeXml(product.name)}</h3>

								<c:choose>
									<c:when test="${pcPopup != null}">
										<a href='#'	onclick="pcRemoveProduct('${product.code}','<c:url value="/"/>', '${pcPopup}')"
											style="position: absolute; width: 25px; height: 25px; top: 10px; right: 12px; display: block;">
											<i class="gl gl-remove" style="position: relative; top: 0; right: 0; margin:0;"></i></a>
									</c:when>
									<c:otherwise>
										<a href='<c:url value="/compare/remove?code=${product.code}"/>'
											style="position: absolute; width: 25px; height: 25px; top: 10px; right: 12px; display: block;"><i
											class="gl gl-remove" style="position: relative; top: 0; right: 0; margin:0;"></i></a>
									</c:otherwise>
								</c:choose>

								<span class="price"> <format:fromPrice
										priceData="${product.price}" /></span>
								<p class="summary">
									<c:if test="${not empty product.summary}">
												${product.summary}
												</c:if>
								</p>


								<form id="addToCartForm1" class="add_to_cart_form"
									action="<c:url value="/cart/add"/>" method="post">
									<c:if test="${product.purchasable}">
										<input type="hidden" maxlength="3" size="1" id="qty"
											name="qty" class="js-qty-selector-input" value="1">
									</c:if>
									<input type="hidden" name="productCodePost"
										value="${product.code}" />

									<c:choose>
										<c:when
											test="${product.showAddToCart and product.purchasable}">
											<button id="addToCartButton" type="${buttonType}"
												class="btn btn-primary btn-block js-add-to-cart"
												disabled="disabled">
												<spring:theme code="basket.add.to.basket" />
												<i class="fa fa-shopping-cart"></i>
											</button>
										</c:when>
										<c:otherwise>
											<a href='<c:url value="/contactus"></c:url>'
												title="Where to Buy" class="btn btn-primary btn-block"><spring:theme
													code="home.wheretobuy" /></a>
										</c:otherwise>
									</c:choose>
								</form>

							</div>
						</div>
					</div>
				</c:forEach>

			</div>
			<!--/compare-carousel owl-carousel owl-theme-->
		</div>
		<!--/col-md-offset-3 col-xs-offset-6-->
	</div>
	<!--/row product product-compare-list-->
	<!--  requirement to list all products and features regardless of being comparable or not lead to this customization -->
	<!-- Variable to store the classification categories as a comma separated list-->
	<c:set var="classificationkeys" value="" scope="request" />
	<c:forEach items="${productList}" var="product" varStatus="row">

		<!-- Iterate through a custom list from back end which is a collection of product to class cat to features -->
		<c:forEach items="${classifications[product.code]}"
			var="classification" varStatus="classStatus">

			<!--  If condition to eliminate listing the same
    		category if it has already come, the feature values for loop down below would have already listed it for all products-->
			<c:if
				test="${(empty classificationkeys) or (!fn:contains(classificationkeys, classification.key))}">
				<div class="feature-compare-list">
					<div class="feature-compare-title">${classification.key}</div>
					<c:set var="classificationkeys"
						value="${classificationkeys},${classification.key}" />

					<table class="compare-table full-width">
						<c:forEach items="${classification.value}" var="feature"
							varStatus="featureStatus">
							<tr>
								<th class="text-left feature-list--title">${feature.name}</th>

								<!--  iterate thru the products again to display the feature values for each products -->
								<c:forEach items="${productList}" var="product2" varStatus="row">
									<c:set var="productClassification"
										value="${classifications[product2.code]}" />
									<c:set var="productFeature"
										value="${productClassification[classification.key]}" />

									<!--  if the product doesn't even have the category, skip it -->
									<c:if test="${empty productClassification[classification.key]}">
										<td
											class="td-compare feature-${row.index} ${row.index < 3 ? ' active':' inactive'} ${row.index == 0 ? ' mactive':''}">-</td>
									</c:if>
									<c:forEach items="${productFeature}" var="specFeature"
										begin="${featureStatus.index }" end="${featureStatus.index }">
										<c:forEach items="${specFeature.featureValues}"
											var="featureValue" varStatus="fvStatus">
											<td
												class="td-compare feature-${row.index} ${row.index < 3 ? ' active':' inactive'} ${row.index == 0 ? ' mactive':''}">
												${featureValue.value} <c:choose>
													<c:when test="${specFeature.range}">
														${not fvStatus.last ? '-' : specFeature.featureUnit.symbol}
													</c:when>
													<c:otherwise>
														${specFeature.featureUnit.symbol}
														${not fvStatus.last ? '<br/>' : ''}
													</c:otherwise>
												</c:choose>
											</td>
										</c:forEach>
									</c:forEach>
								</c:forEach>
							</tr>
						</c:forEach>
					</table>
				</div>
			</c:if>
		</c:forEach>
	</c:forEach>

</c:if>

<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>

<div class="pdp-details">
	<div class="container mcontainer">
		<div class="row mrow">
			<div class="col-md-5 padd-r0 mpadd-20 equal-col">
				<product:productImagePanel galleryImages="${galleryImages}"
					product="${product}" />
			</div>
			<div class="col-md-4 padd-r0 padd-l0 equal-col prd-grey">
				<div class="product-wrp mpadd-20">
					<h2 class="product-name">${product.name}</h2>
					<div class="prod-info">
						<div class="info-item">
							<label><spring:theme code='productDetailPage.catNo' /></label> ${product.catalogNumber}
						</div>
						<c:if test="${not empty product.sapEan}">
						<div class="info-item last">
							<label><spring:theme code='productDetailPage.upc' /></label> ${product.sapEan}
						</div>
						</c:if>
					</div>
					<product:productReviewSummary product="${product}" showLinks="true" />


					<p class="description">${product.summary}</p>
					<c:if test="${product.showPrice == 'true'}">
					<div class="price">
						<format:fromPrice priceData="${product.price}" />
					</div>
					</c:if>
					<cms:pageSlot position="AddToCart" var="component">
						<cms:component component="${component}" />
					</cms:pageSlot>

				</div>
			</div>
			<div class="col-md-3 padd-r0 padd-l0 equal-col">
				<div class="clearfix pdp-links">
					<div class="pdp-list first">
						<h4>
							<spring:theme code='productDetailPage.needHelp' /> <i class="fa fa-angle-down hidden-lg hidden-md"></i>
						</h4>
						<ul>
							<li class="yCmsComponent"><a
								href="mailto:<spring:theme code='productDetailPage.supportEmail' />"><i
									class="fa fa-envelope"></i><spring:theme code='productDetailPage.supportEmail' /></a></li>
							<li class="yCmsComponent"><a href="tel:<spring:theme code='productDetailPage.supportTel' />"><i
									class="fa fa-phone"></i><spring:theme code='productDetailPage.supportTel' /></a></li>
							<c:if test="${product.requestToDemo}">
								<li class="yCmsComponent"><a class="js-request-demo  text-uppercase"
									href="<c:url value='/salesforwebtolead' />"
									data-cbox-title="<spring:theme code='request.to.demo'/>"> <i
										class="fa fa-question-circle"></i>
									<spring:theme code='request.to.demo' />
								</a></li>
							</c:if>
							<c:if test="${not empty productReferences}">
							<c:set var="followUp" value="false" />
								<c:forEach items="${productReferences}" var="followUpItem">
									<c:if test="${followUpItem.referenceType eq 'FOLLOWUP' and followUp eq 'false'}">
									 <c:set var="followUp" value="true" />
										<li class="yCmsComponent maintence-link"><a href="#" class="text-uppercase"><i
												class="fa fa-arrow-circle-up"></i> <spring:theme code='productDetailPage.maintenceUpgrades' /></a></li>
									</c:if>
								</c:forEach>
							</c:if>
						</ul>
					</div>
					<div class="pdp-list last">
						<c:if test="${product.productArtifacts.size() > 0}">
							<h4>
								<spring:theme code='productDetailPage.download.downloads' /> <i class="fa fa-angle-down hidden-lg hidden-md"></i>
							</h4>
						</c:if>
						<ul>
							<c:forEach var="i" begin="0" end="3"
								items="${product.productArtifacts}">
								<li class="yCmsComponent"><a href="${i.url}" target="_blank">${i.altText}<i class="fa fa-file-pdf-o"></i></a></li>
							</c:forEach>
							<c:if test="${product.productArtifacts.size() > 3}">
								<li class="yCmsComponent"><a id="downloadMore"><spring:theme code='productDetailPage.download.more' /></a></li>
							</c:if>

						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>


<%-- <div class="product-details">
	<ycommerce:testId
		code="productDetails_productNamePrice_label_${product.code}">
		<div class="name">${product.name}
			<span class="sku">ID ${product.code}</span>
		</div>
	</ycommerce:testId>


</div>
<div class="row">
	<div class="col-md-6 col-lg-4"></div>
	<div class="col-md-6 col-lg-8">
		<div class="row">
			<div class="col-lg-6">
				<div class="product-details">
					<product:productPromotionSection product="${product}" />

					<ycommerce:testId
						code="productDetails_productNamePrice_label_${product.code}">
						<product:productPricePanel product="${product}" />
					</ycommerce:testId>

					<div class="description">${product.summary}</div>
					<!--  TODO Change this to image, dependant on UI -->
					<div class="description">${product.videolink}</div>
				</div>
			</div>

			<div class="col-lg-6">

				<cms:pageSlot position="VariantSelector" var="component">
					<cms:component component="${component}" />
				</cms:pageSlot>

				<cms:pageSlot position="AddToCart" var="component">
					<cms:component component="${component}" />
				</cms:pageSlot>

			</div>
		</div>
	</div>
</div> --%>

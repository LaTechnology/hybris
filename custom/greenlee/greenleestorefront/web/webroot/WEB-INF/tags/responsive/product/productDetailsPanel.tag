<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<div class="pdp-details">
	<div class="container mcontainer">
		<div class="row mrow">
			<div class="col-md-5 padd-r0 mpadd-20">
				<product:productImagePanel galleryImages="${galleryImages}"
					product="${product}" />
				<!-- 	<div class="text-center primary-image">
					<a href=""> <img src="/image/pdp/product.png" />
					</a>
				</div>
				<div class="pdp-carousel text-center">
					<div class="item">
						<img src="/image/pdp/p1.png" />
					</div>
					<div class="item">
						<img src="/image/pdp/p2.png" />
					</div>
					<div class="item">
						<img src="/image/pdp/p3.png" />
					</div>
					<div class="item">
						<img src="/image/pdp/p4.png" />
					</div>
					<div class="item">
						<img src="/image/pdp/p5.png" />
					</div>
				</div> -->
			</div>
			<div class="col-md-4 padd-r0 padd-l0">
				<div class="product-wrp mpadd-20">
					<h2 class="product-name">${product.name}</h2>
					<div class="prod-info">
						<div class="info-item">
							<label>Cat No.</label> ASK306-ENT
						</div>
						<div class="info-item">
							<label>UPC</label> ${product.code}
						</div>
						<div class="info-item last">
							<label>UNPSC</label> 88429876
						</div>
					</div>

					<product:productReviewSummary product="${product}" showLinks="true" />


					<p class="description">${product.summary}</p>
					<div class="price">
						<format:fromPrice priceData="${product.price}" />
					</div>

					<cms:pageSlot position="AddToCart" var="component">
						<cms:component component="${component}" />
					</cms:pageSlot>

				</div>
			</div>
			<div class="col-md-3 padd-r0 padd-l0">
				<div class="clearfix pdp-links">
					<div class="pdp-list first">
						<h4>
							NEED HELP? <i class="fa fa-angle-down"></i>
						</h4>
						<ul>
							<li class="yCmsComponent"><a
								href="mailto:support@airscoutwifi.com"><i
									class="fa fa-envelope"></i>support@airscoutwifi.com</a></li>
							<li class="yCmsComponent"><a href="tel:1-800-198-9384"><i
									class="fa fa-phone"></i>1-800-198-9384</a></li>
							<li class="yCmsComponent"><a href="#"><i
									class="fa fa-question-circle"></i>Request demo</a></li>
							<li class="yCmsComponent"><a href="#"><i
									class="fa fa-arrow-circle-up"></i> Maintenence upgrades</a></li>
						</ul>
					</div>
					<div class="pdp-list last">
						<h4>
							Downloads <i class="fa fa-angle-down"></i>
						</h4>
						<ul>
							<c:forEach var="i" begin="0" end="4"
								items="${product.productArtifacts}">
								<li class="yCmsComponent"><a href="${i.getUrl()}"><i
										class="fa fa-file-pdf-o"></i>${i.getAltText()}</a></li>
							</c:forEach>
							<li class="yCmsComponent"><a href="">More</a></li>

						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>


<product:productFeatureTabs product="${product}" />

<product:productSendEmailToFriend product="${product}" />


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


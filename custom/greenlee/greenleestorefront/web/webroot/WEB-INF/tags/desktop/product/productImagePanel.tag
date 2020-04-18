<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ attribute name="galleryImages" required="true" type="java.util.List"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>

<div class="image-gallery js-gallery">
	<c:choose>
		<c:when test="${galleryImages == null || galleryImages.size() == 0}">
			<div class="pdp-main-image">
				<div
					class="carousel gallery-image js-gallery-image owl-carousel owl-theme">
					<div class="item">
						<div class="thumb">
							<spring:theme code="img.missingProductImage.responsive.product"
								text="/" var="imagePath" />
							<c:choose>
								<c:when test="${originalContextPath ne null}">
									<c:url value="${imagePath}" var="imageUrl"
										context="${originalContextPath}" />
								</c:when>
								<c:otherwise>
									<c:url value="${imagePath}" var="imageUrl" />
								</c:otherwise>
							</c:choose>
							<img class="owl-lazy" data-src="${imageUrl}" />
						</div>
					</div>
				</div>
			</div>
		</c:when>
		<c:otherwise>
			<div class="pdp-main-image">
				<div
					class="carousel gallery-image js-gallery-image owl-carousel owl-theme">
					<c:forEach items="${galleryImages}" var="container"
						varStatus="varStatus">
						<div class="item indexed-div" data-index=${varStatus.index}>
							<div class="thumb">
								<img class="owl-lazy dd" data-src="${container.product.url}"
									data-zoom-image="${container.superZoom.url}"
									alt="${container.thumbnail.altText}" >
							</div>
						</div>
					</c:forEach>
					<c:if test="${not empty  product.videolink}">
						<div class="item-video indexed-div" data-index=${galleryImages.size()}>
							<a class="owl-video" href="${product.videolink}">
							</a>
						</div>
					</c:if>
				</div>
				<c:url var="zoomurl" value="/p/${product.code}/zoomImages"></c:url>
				<a class="productImageZoomLink js-zoom-link hidden-sm hidden-xs"
					id="zoomLink" href="${zoomurl }" target="_blank"
					title="Enlarged view of picture"> <i class="fa fa-search-plus"></i>
				</a>
			</div>
			<product:productGalleryThumbnail galleryImages="${galleryImages}" />
		</c:otherwise>
	</c:choose>
</div>

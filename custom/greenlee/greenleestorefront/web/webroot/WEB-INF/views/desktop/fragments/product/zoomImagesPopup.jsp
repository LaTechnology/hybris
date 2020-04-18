<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="popup-gallery">
	<div class="product-wrp">
		<h2 class="product-name">${product.name }</h2>
		<div class="prod-info">
			<div class="info-item">
				<label><spring:theme code='productDetailPage.catNo' /></label>
				${product.catalogNumber}
			</div>
			<c:if test="${not empty product.sapEan}">
			<div class="info-item last">
				<label><spring:theme code='productDetailPage.upc' /></label>
				${product.sapEan}
			</div>
			</c:if>
		</div>
	</div>

	<div class="image-gallery clearfix">
		<div class="pdp-main-image">
			<div class="zoom-vertical">
				<c:forEach items="${galleryImages}" var="container"
					varStatus="status">
					<a data-index=${status.count - 1 } href="#" class="item active">
						<img src="${container.thumbnail.url}"
						data-zoomurl="${container.zoom.url}" alt="${product.name}"
						title="${product.name}">
					</a>
					<c:set var="statusvalue">${status.count}</c:set>
				</c:forEach>
				<c:if test="${not empty  product.videolink}">
					<c:set var="text" value="${product.videolink}" />
					<c:set var="productVideolink" value="${product.videolink}" />
					<c:set var="splittext" value="${fn:split(text,'/')}" />
					<c:if test="${fn:contains(productVideolink, '?')}">
	   					<a data-index=${statusvalue} href="#" class="item">
							<img src="https://img.youtube.com/vi/${fn:substring(productVideolink,fn:indexOf(productVideolink, splittext[fn:length(splittext)-1]),fn:indexOf(productVideolink,'?'))}/0.jpg" data-zoomurl="${product.videolink}" alt="${product.name}" title="${product.name}">
						</a>
					</c:if>
					<c:if test="${not fn:contains(productVideolink, '?')}">
						<a data-index=${statusvalue} href="#" class="item">
								<img src="https://img.youtube.com/vi/${splittext[fn:length(splittext)-1]}/0.jpg" data-zoomurl="${product.videolink}" alt="${product.name}" title="${product.name}">
						</a>
					</c:if>
				</c:if>
			</div>
			<div class="zoom-gallery">
				<c:if test="${empty zoomImageUrl}">
					<c:set value="${ycommerce:productImage(product, 'zoom').url}"
						var="zoomImageUrl" />
				</c:if>
				<div class="zoom-gallery-image owl-carousel owl-theme">
					<c:forEach items="${galleryImages}" var="container">
						<c:set var="zoomImageUrl">${container.zoom.url}</c:set>
						<div class="item">
							<div class="thumb">
								<img class="owl-lazy" data-src="${zoomImageUrl}"
									data-zoom-image="" alt="${product.name }">
							</div>
						</div>
					</c:forEach>
					<c:if test="${not empty  product.videolink}">
						<div class="item-video">
							<a class="owl-video"
								href="${product.videolink}"></a>
						</div>
					</c:if>
				</div>

			</div>
		</div>
	</div>
</div>





<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<c:url value="${product.url}/openWriteReviewPopup" var="getWriteReviewPopup"/>
<c:url value="${product.url}/reviewhtml/4" var="getPageOfReviewsUrl"/>
<c:url value="${product.url}/reviewdatahtml" var="getPaginatedReviewsUrl"/>
<c:if test="${not empty product.reviews}">
		<div class="product-reviews">
			<div class="container">
			<input type="hidden" value="${fn:length(product.reviews)}" name="totalreview" id="totalreview">
				<div class="reviews-heading">
					<h3><spring:theme code="review.reviewsandratings"/></h3>
					<c:if test="${fn:length(product.reviews) > 0}">
					<div class="rating js-ratingCalc " data-rating='{"rating":${product.averageRating},"total":5}'>
						<div class="rating-stars">
							<i class="fa fa-star-o js-ratingIcon"></i>
						</div>
						<span class="average-rating">

						<spring:theme code="review.average.rating" arguments="${product.averageRating}"/>
						</span>
						<span class="no-rating"></span>
					</div>
					<p><spring:theme code="review.overall.ratings" arguments="${reviewsTotal}"/> </p>
					</c:if>
					<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
					<a href="${getWriteReviewPopup}" class="btn btn-white js-write-review" data-cbox-title="WRITE A REVIEW" ><spring:theme code="review.write.title"/> </a>
					</sec:authorize>
					<sec:authorize access="isAnonymous()">

					<c:url var="LoginUrl" value="/login" />
					<div><a href='${LoginUrl}'><spring:theme code="review.login.required"/></a></div>
					</sec:authorize>
				</div>
				<div class="review-wrp">
				<div class="review-controls clearfix">
					<div class="reviews-details">
						<%-- <spring:theme code="review.range"/> ${product.reviews.size()}  <spring:theme code="review.reviews"/> --%>
						<c:choose>
					<c:when test="${empty product.reviews}">
						<spring:theme code="review.based.on"
						arguments="${fn:length(product.reviews)}" />
						</c:when>
						<c:otherwise>

						<c:choose>
						<c:when test="${fn:length(product.reviews) > 4}">
						<div id="tobehidden">
						<c:set var="startValue" value="1"/>
						<c:set var="endValue" value="4"/>
						<spring:theme code="review.range" arguments="${startValue},${endValue},${fn:length(product.reviews)}"/>
						</div>
						</c:when>
						<c:otherwise>
						<c:set var="startValue" value="1"/>
						<c:set var="maxRange" value="${fn:length(product.reviews)}"/>
						<spring:theme code="review.range" arguments="${startValue},${maxRange},${maxRange}"/>
						</c:otherwise>
						</c:choose>
						</c:otherwise>
						</c:choose>
					</div>
					<div class="reviews-nav hidden-xs hidden-sm">
      				<input type="hidden" value="${fn:length(product.reviews)}" name="totalreview" id="totalreview">
    				  <a href="${getPaginatedReviewsUrl}" class="prev"><i class="fa fa-chevron-left"></i></a>
      				<a href="${getPaginatedReviewsUrl}" class="next"><i class="fa fa-chevron-right"></i></a>
     			</div>

				</div>
				<c:if test="${not empty product.reviews}">
				<ul id="reviews" class="review-list" data-reviews="${getPaginatedReviewsUrl}">
				<c:forEach items="${product.reviews}" var="review" varStatus="status">
				 <c:if test="${status.index < 4}">
					<li class="review-entry">
						<div class="autor">
							${review.principal.name}
						</div>
						<div class="date hidden-md hidden-lg"><span class="date"> <fmt:formatDate value="${review.date}" pattern="MMMM dd, yyyy" /></span></div>
						<div class="rating js-ratingCalc" data-rating='{"rating":${review.rating},"total":5}'>
							<div class="rating-stars">
								<i class="fa fa-star-o js-ratingIcon"></i>
							</div>
						</div>
						<div class="title">${review.headline}</div>
						<div class="date hidden-xs hidden-sm"><span class="date"> <fmt:formatDate value="${review.date}" pattern="MMMM dd, yyyy" /></span></div>

						<div class="content">${review.comment}</div>

					</li>
					</c:if>
					</c:forEach>
					</ul>

					</c:if>
				</div>
				<c:if test="${not empty product.reviews && fn:length(product.reviews) gt 4}">
					<a href="#" class="btn btn-white hidden-md hidden-lg">more</a>
				</c:if>
			</div>
		</div>
</c:if>

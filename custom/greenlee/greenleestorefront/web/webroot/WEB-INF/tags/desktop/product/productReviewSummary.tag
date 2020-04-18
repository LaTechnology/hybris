<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ attribute name="showLinks" required="false" type="java.lang.Boolean"%>
<%@ attribute name="starsClass" required="false" type="java.lang.String"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>

<c:url value="${product.url}/openWriteReviewPopup" var="getWriteReviewPopup"/>

<sec:authorize ifNotGranted="ROLE_ANONYMOUS" var="loggedIn"/>



	<c:choose>
		<c:when test="${not empty product.reviews}">
			<div class="rating js-ratingCalc ${starsClass}"
	data-rating='{"rating":${product.averageRating},"total":5}'>
	<div class="rating-stars">
		<i class="fa fa-star-o js-ratingIcon"></i>
	</div>
			<span class="average-rating"> ${product.averageRating}</span>
			<span class="no-rating"> <c:choose>
					<c:when test="${fn:length(product.reviews) > 1}">
						<spring:theme code="review.based.on"
										arguments="${fn:length(product.reviews)}" />
					</c:when>
					<c:otherwise>
						<spring:theme code="review.based.on.one"
										arguments="${fn:length(product.reviews)}" />
					</c:otherwise>
				</c:choose>
			</span>
			</div>
		</c:when>
		<c:otherwise>
			<c:if test="${loggedIn}">
				<a href="${getWriteReviewPopup}"  class="js-write-review" data-cbox-title="WRITE A REVIEW">
						<spring:theme code="review.no.reviews"/>
				</a>
			</c:if>
		</c:otherwise>
	</c:choose>
	<%-- <c:choose>
		<c:when test="${showLinks}">
			<c:if test="${not empty product.reviews}">
				<a href="#tabreview" class="js-openTab"><spring:theme
						code="review.see.reviews" /></a>
			</c:if>
			<a href="#tabreview" class="js-writeReviewTab"><spring:theme
					code="review.write.title" /></a>
		</c:when>
		<c:otherwise>
			<spring:theme code="review.reviews" />
		</c:otherwise>
	</c:choose> --%>


<%-- <div class="rating js-ratingCalc "
						data-rating='{"rating":2.3333333333333335,"total":5}'>
						<div class="rating-stars">
							<i class="fa fa-star js-ratingIcon"></i> <i
								class="fa fa-star js-ratingIcon"></i> <i
								class="fa fa-star js-ratingIcon"></i> <i
								class="fa fa-star js-ratingIcon"></i> <i
								class="fa fa-star js-ratingIcon"></i>
						</div>
						<span class="average-rating"> ${product.averageRating }</span> <span
							class="no-rating"> ${product.numberOfReviews } Reviews</span>
					</div> --%>
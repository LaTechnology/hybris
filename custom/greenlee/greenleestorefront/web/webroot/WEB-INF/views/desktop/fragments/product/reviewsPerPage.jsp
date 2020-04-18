<%@ page trimDirectiveWhitespaces="true"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:url value="${product.url}/reviewhtml/all" var="getAllReviewsUrl"/>
<c:url value="${product.url}/review" var="productReviewActionUrl"/>

<c:url value="${product.url}/reviewdatahtml" var="getPaginatedReviewsUrl"/>
<%-- <c:param name="page" value="${page}"/> --%>

<%-- <product:productPageReviews product="${product}" /> --%>

			<div class="review-controls clearfix">
			<div class="reviews-details">
						<c:set var="startValue" value="${offset+1}"/>
						<c:set var="endValue" value="${maxLimit}"/>
						<spring:theme code="review.range" arguments="${startValue},${endValue},${totalCount}"/>
						</div>
						<div class="reviews-nav hidden-xs hidden-sm">
						<input type="hidden" value="${totalCount}" name="totalreview" id="totalreview">
    				 <a href="${getPaginatedReviewsUrl}" class="prev"><i class="fa fa-chevron-left"></i></a>
      				<a href="${getPaginatedReviewsUrl}" class="next"><i class="fa fa-chevron-right"></i></a> 
      				</div>
      				</div>
				<c:if test="${not empty reviews}">
				<ul id="reviews" class="review-list" data-reviews="${getPaginatedReviewsUrl}">
				<c:forEach items="${reviews}" var="review" varStatus="status">
					<li class="review-entry">
						<div class="autor">
							${review.principal.name}
						</div>
						<div class="date hidden-md hidden-lg"><span class="date"><fmt:formatDate value="${review.date}" pattern="dd/MM/yyyy" /></span></div>
						<div class="rating js-ratingCalc" data-rating='{"rating":${review.rating},"total":5}'>
							<div class="rating-stars">
								<i class="fa fa-star-o js-ratingIcon"></i>
							</div>
						</div>
						<div class="title">${review.headline}</div>
						<div class="date hidden-xs hidden-sm"><span class="date"><fmt:formatDate value="${review.date}" pattern="dd/MM/yyyy" /></span></div>
						<div class="content">${review.comment}</div>
					</li>
					</c:forEach>
					</ul>
					</c:if>
				

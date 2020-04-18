<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%-- <c:url value="${product.url}/reviewhtml/3" var="getPageOfReviewsUrl"/> --%>
<c:url value="${product.url}/reviewhtml/all" var="getAllReviewsUrl"/>
<c:url value="${product.url}/review" var="productReviewActionUrl"/>

<c:url value="${product.url}/reviewhtml" var="getPageOfReviewsUrl">
<c:param name="page" value="${page}"/>
</c:url>

<div class="write-review js-review-write">
	<form:form method="post" action="${productReviewActionUrl}" commandName="greenleeReviewForm">
			<div class="review-data">
			<template:errorSpanField path="rating">
			<div class="help-text"><spring:theme code="review.help.text"/></div>
			<div class="rating rating-set js-ratingCalcSet" data-rating='{"total":5}'>
				<div class="rating-stars">
					<span class="js-ratingIcon js-rationIconSet fa fa-star-o"></span>
				</div>
			</div>
			</template:errorSpanField>

			<form:input cssClass=" form-control sr-only js-ratingSetInput" id="review.rating" path="rating"
					tabindex="${tabindex}" autocomplete="${autocomplete}"/>




				<%-- <formElement:formInputBox idKey="review.headline" labelKey="review.headline" path="headline" inputCSS="form-control" mandatory="true" labelCSS="form-control"/> --%>
				<template:errorSpanField path="headline">

					<label class="control-label form-control" for="review.headline">
						<spring:theme code="review.headline" />
					</label>
					<div class="help-text"><spring:theme code="review.example"/></div>
					<form:input cssClass="form-control" id="review.headline" path="headline"
							tabindex="${tabindex}" autocomplete="${autocomplete}"/>

			</template:errorSpanField>

				<formElement:formTextArea idKey="review.comment" labelKey="review.comment" path="comment" areaCSS="form-control" mandatory="true" labelCSS="control-label "/>





				<template:errorSpanField path="agree">
					<spring:theme code="agree" var="themeIdKey"/>
					   	<div class="checkbox checkbox-group">
					   		<form:checkbox cssClass="form-control" id="${themeIdKey}" path="agree" tabindex="${tabindex}"/>
					   		<label class="text-none" for="${themeIdKey}">
					   			<spring:theme code="review.agree"/>&nbsp;<a href="<c:url value="/policies-and-terms"/>"><spring:theme code="review.terms"/></a>
					   		</label>
					   	</div>
				</template:errorSpanField>


			</div>
			<div class="form-group">
			<button type="submit" class="btn btn-white btn-popup " value="<spring:theme code="review.submit"/>"><spring:theme code="review.submit"/></button>
			<button class="btn btn-white btn-popup btn-border btn-close last" type="button">
					<spring:theme code="review.close"/></button>
			</div>

	</form:form>
</div>

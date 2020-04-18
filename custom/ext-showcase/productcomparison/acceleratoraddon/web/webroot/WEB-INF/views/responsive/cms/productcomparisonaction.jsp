<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:set var="toggleCompareButtonAddLabel"><spring:theme code="productcomparison.button.add" /></c:set>
<c:set var="toggleCompareButtonRemoveLabel"><span class='icon glyphicon glyphicon-trash pull-left'></span><spring:theme code="productcomparison.button.remove" /></c:set>
<c:set var="showCompareTitle"><spring:theme code="productcomparison.header.compare_items" /></c:set>
<c:set var="pcCodesSize" value="${fn:length(pcCodes)}" />

<spring:theme code="productcomparison.button.compare_items" var="compareBtnLabel" />
<c:set var="comparable" value="${not empty pcCodes[product.code]}" />
<c:url value="/compare" var="pcUrl" />

<spring:theme code="${comparable ? toggleCompareButtonRemoveLabel : toggleCompareButtonAddLabel}" var="toggleBtnLabel" />
<div class="productcomparisonaction btn-group btn-group-justified" role="group">
    <a role="button" href="${pcUrl}" class="btn btn-info js-productcomparison-toggle" aria-selected="${comparable ? 'true' : 'false'}" data-product-code="${product.code}" data-label-add="${toggleCompareButtonAddLabel}" data-label-remove="${toggleCompareButtonRemoveLabel}">
        ${toggleBtnLabel}
    </a>
    <c:if test="${component.showCompareButton}">
        <a role="button" class="btn btn-default js-productcomparison-show" ${pcCodesSize lt 2 ? 'disabled=disabled' : ''}
                href="<c:url value="/"/>" data-popup="${component.openPopup}" data-popup-title="${showCompareTitle}">
                <span class="sr-only">${compareBtnLabel}</span><span class="glyphicon glyphicon-list"></span> <span class="js-productcomparison-count">${pcCodesSize}</span>
        </a>
    </c:if>
</div>

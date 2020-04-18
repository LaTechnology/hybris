<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>

<div class="productComparePanel">
	<c:if test="${component.showCompareButton}">
		<spring:theme code="productcomparison.button.compare_items" var="compareBtnLabel" />
		<input type="hidden" id="compareBtnLabel" value="${compareBtnLabel}"/>
		<button type="button" class="compareBtn positive btn" ${fn:length(pcCodes) lt 2 ? 'disabled=disabled' : ''}  
		onclick="pcShowComparePage('<c:url value="/"/>',${component.openPopup},${component.closePopupAfterAddToCart})" >
			${compareBtnLabel} (${fn:length(pcCodes)})
		</button>
		
		<script>
		var prdCompare = [];
		<c:if test="${not empty pcCodes}">	
			
					  <c:forEach items="${pcCodes}" var="code">
					 
					  prdCompare.push("${code.value}");
					 
					  </c:forEach>
					
			</c:if>

		</script>
		
	</c:if>
	<c:if test="${component.showCheckbox}">
		<c:set var="comparable" value="${not empty pcCodes[product.code]}" />
		
		<c:url value="/compare" var="pcUrl" />
		<input type="checkbox" ${comparable ? 'checked=checked' : ''} onclick="pcUpdateComparableState('${product.code}','${pcUrl}', this, '${compareBtnLabel}')">
		<spring:theme code="productcomparison.checkbox.compare" />
	</c:if>

</div>
<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags"%>


	
    	<c:if test="${not empty validationData}">
    	<div id="globalMessages"> 
    		<div class="global-alerts">
			<c:forEach items="${validationData}" var="modification">
					<div class="alert alert-thanks alert-dismissable text-center">
						<div class="container">				
							<c:url value="${modification.entry.product.url}" var="entryUrl"/>
				<spring:theme code="basket.validation.${modification.statusCode}"
					arguments="${modification.entry.product.name}###${entryUrl}###${modification.quantity}###
							${modification.quantityAdded}###${productLinkValidationTextDecoration}" argumentSeparator="###"/>
						 	<i class="gl gl-remove"></i>
						</div>
					</div>				
			</c:forEach>
			</div>
				</div>
		</c:if>
    
	


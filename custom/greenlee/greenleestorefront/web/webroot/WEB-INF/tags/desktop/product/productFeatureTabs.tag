<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>

<c:if test="${not empty product.featureTabs}">
	<div class="container">
		<div class="tabs-container">
		
			<ul class="tabs js-tabs tabs-responsive">
					<c:forEach items="${product.featureTabs}" var="featureTab" varStatus="tabNameLoop">
						<li <c:if test="${tabNameLoop.index == 0}">class="active"</c:if> data-rel="${featureTab.name}">
							<span>${featureTab.name}</span>
						</li>					
					</c:forEach>
				</ul>
		
		
		<c:forEach items="${product.featureTabs}" var="featureTab">
			<div class="${featureTab.name}">
				${featureTab.description}
			</div>
		</c:forEach>
		</div>
	</div>
</c:if>

<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<div class="container">
	<div class="tabs-container">
		<ul class="tabs js-tabs tabs-responsive">
			<li class="active" data-rel="overview"><span><spring:theme
						code='overview' /></span></li>
<%-- 			<li data-rel="features"><span><spring:theme
						code='productDetailPage.features' /></span></li> --%>
			<c:forEach items="${product.featureTabs}" var="featureTab" varStatus="tabNameLoop">
				<c:if test="${not empty featureTab.description}">
					<li data-rel="${fn:replace(featureTab.name,' ','-')}">
						<span>${featureTab.name}</span>
					</li>
				</c:if>						
			</c:forEach>
			<c:if test="${not empty product.classifications}">
				<li data-rel="specifications"><span><spring:theme
							code='productDetailPage.specifications' /></span></li>
			</c:if>						
			<li class="last" data-rel="downloads"><span id="downloadJump"
				name="downloadJump"><spring:theme
						code='productDetailPage.download.downloads' /></span></li>
		</ul>
		
<%-- 		<product:productFeatureTabs product="${product}" />
 --%>		<div class="overview tab-content active">
						<div class="disp-img simple-banner">
							<div class="thumb">
							
								<img title="overview" alt="overview" src="${product.images[0].url }">
							</div>
							<div class="details">
								${product.description}
							</div>
						</div>
					</div>
<%-- 		<div class="features tab-content">
			<div class="feature-wrp">
				${product.features}
			</div>
		</div> --%>
		<c:forEach items="${product.featureTabs}" var="featureTab">
			<c:if test="${not empty featureTab.description}">
				<div class="${fn:replace(featureTab.name,' ','-')} tab-content">
					<div class="feature-wrp">${featureTab.description}</div>
				</div>
			</c:if>	
		</c:forEach>
		<div class="specifications tab-content">
			<product:productDetailsClassifications product="${product}" />
		</div>
		<div class="downloads tab-content">
		<c:if test="${!empty product.productArtifacts}">
			<div class="pdp-list">
				<h4>
					<spring:theme
						code='productDetailPage.download.manualsDocumentation' />
				</h4>
				<ul>
					<c:forEach var="i" items="${product.productArtifacts}">
						<li class="yCmsComponent"><a href="${i.url}"
							target="_blank"><i class="fa fa-file-pdf-o"></i>${i.altText}</a></li>
					</c:forEach>
				</ul>
			</div>
			</c:if>
			<c:if test="${!empty product.productArtifactsGroups}">
			<div class="pdp-list">
			<c:forEach var="group" items="${product.productArtifactsGroups }">
				<c:if test="${!empty group.productMedia}">
				<h4><spring:theme text="${group.groupName }" /></h4>
				<ul>
					<c:forEach var="doc" items="${group.productMedia }">
					<li class="yCmsComponent">
						<a href="${doc.url }" target="_blank"><i class="fa fa-file-pdf-o"></i>${doc.altText }</a>
					</li>
					</c:forEach>
				</ul>
				</c:if>
			</c:forEach>		
			</div>
			</c:if>
			<%--
			<div class="pdp-list last">
				<div class="content">
					<h4>
						<spring:theme code='productDetailPage.download.apps' />
					</h4>
					<p>
						<spring:theme code='productDetailPage.download.apps.message' />
					</p>
				</div>
				 <div class="simple-banner-component">
					<a href="#"> <img title="${media.altText}"
						alt="${media.altText}" src="${themeResourcePath}/image/pdp/apple.png">
					</a>
				</div>
				<div class="simple-banner-component">
					<a href="#"> <img title="${media.altText}"
						alt="${media.altText}" src="${themeResourcePath}/image/pdp/android.png">
					</a>
				</div> 
			</div>
			--%>
		</div>
	</div>
</div>
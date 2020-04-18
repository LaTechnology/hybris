<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="pageData" required="true"
	type="de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<!-- contentSearchPageData.pagination.totalNumberOfResults -->

<c:url var="productList" value="/search"/>
<c:url var="contentList" value="/search?text=${searchWord}&data=content&viewAs=${viewAs}"/>
<!-- =${searchWord}&data=content&viewAs=${viewAs} ?text=${searchWord}&data=product&viewAs=${viewAs}-->
<c:choose>
<c:when test="${data == 'product'}">
<c:set var="productsColor" value="#ff8d03"/>
<c:set var="contentColor" value=""/>

</c:when>
<c:otherwise>
<c:set var="productsColor" value=""/>
<c:set var="contentColor" value="#ff8d03"/>
</c:otherwise>
</c:choose>


<div class="col-md-3 facets-list">
	<div class="facet">
		<div class="facet-name hidden-xs hidden-sm"><spring:theme code="refine.by"/></div>
		<div class="facet-values">
			<ul class="facet-list">
				<c:if test="${not empty searchWord}">
				<li class="active">
				
    <a href="javascript:document.productsForm.submit()"><font color="${productsColor}"><spring:theme code="search.facetProducts" text="Products"/>(${searchPageData.pagination.totalNumberOfResults})</font></a>
    <form name="productsForm" action="${productList}">
     <input type="hidden" name="data" value="product"/>
     <c:if test="${searchPageData.pagination.totalNumberOfResults == 0}">
     	<input type="hidden" name="text" value="${searchText}"/>
     </c:if>
      <c:if test="${searchPageData.pagination.totalNumberOfResults != 0}">
     	<input type="hidden" name="q" value="${searchText}"/>
     </c:if>
 
     <c:if test="${viewAs != null}">
     <input type="hidden" name="viewAs" value="${viewAs}"/>
     </c:if>
     <c:if test="${sortCode != null}">
     <input type="hidden" name="sortCode" value="${sortCode}"/>
     </c:if>
    </form>
    </li>
				
				
<%-- 				<li class="active"> <a href="${productList}"><font color="${productsColor}"><spring:theme code="search.facetProducts" text="Products"/>(${searchPageData.pagination.totalNumberOfResults})</font></a> </li> --%>
                  	<li> <a href="${contentList}"><font color="${contentColor}"><spring:theme code="search.facetContent" text="Content Pages"/> (${contentSearchPageData.pagination.totalNumberOfResults})</font></a> </li>
                  	</c:if>
                  	<c:if test="${empty searchWord}">
				<c:forEach items="${pageData.facets}" var="facet">
					<li>
						<c:choose>
							<c:when test="${facet.code eq 'availableInStores'}">
								<nav:facetNavRefinementStoresFacet facetData="${facet}"
									userLocation="${userLocation}" />
							</c:when>
							<c:otherwise>
								<nav:facetNavRefinementFacet facetData="${facet}" />
							</c:otherwise>
						</c:choose>
					</li>
				</c:forEach>
				</c:if>
			</ul>
		</div>
	</div>
</div>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>


<div class="col-md-9">
	<%-- <div class="results">
		<h1><spring:theme code="search.page.searchText" arguments="${searchPageData.freeTextSearch}"/></h1>
	</div> --%>

	<nav:searchSpellingSuggestion spellingSuggestion="${searchPageData.spellingSuggestion}" />

	<div class="clearfix facets-details">
		<nav:pagination top="true"  supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="${searchPageData.currentQuery.url}"  numberPagesShown="${numberPagesShown}"/>
	</div>
	
	<div class="row product">
		<ul class="product-listing product-list">
				<c:forEach items="${searchPageData.results}" var="product">
					<div class="col-xs-12">
						<product:productListerItem product="${product}"/>
					</div>
				</c:forEach>
		</ul>
	</div>

	<div id="addToCartTitle" style="display:none">
		<div class="add-to-cart-header">
			<div class="headline">
				<span class="headline-text"><spring:theme code="basket.added.to.basket"/></span>
			</div>
		</div>
	</div>
	<div class="load-more">
		<button class="btn btn-white">more</button>
	</div>
	<%-- <nav:pagination top="false"  supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="${searchPageData.currentQuery.url}"  numberPagesShown="${numberPagesShown}"/> --%>
</div>
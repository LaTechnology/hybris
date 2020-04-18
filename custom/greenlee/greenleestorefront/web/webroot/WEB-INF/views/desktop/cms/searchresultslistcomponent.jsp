<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="pagination"
	tagdir="/WEB-INF/tags/desktop/nav/pagination"%>

<div class="col-md-9">
	<%-- <div class="results">
		<h1><spring:theme code="search.page.searchText" arguments="${searchPageData.freeTextSearch}"/></h1>
	</div> --%>

	<nav:searchSpellingSuggestion
 		spellingSuggestion="${searchPageData.spellingSuggestion}" /> 

	<div class="clearfix facets-details">
		<nav:pagination top="true" supportShowPaged="${isShowPageAllowed}"
			supportShowAll="${isShowAllAllowed}"
			searchPageData="${searchPageData}"
			searchUrl="${searchPageData.currentQuery.url}"
			numberPagesShown="${numberPagesShown}" />
	</div>

	<div class="row product">
	

			<c:if test="${data=='product'}">

				<c:choose>
					<c:when test="${viewAs =='grid' }">
						<c:forEach items="${searchPageData.results}" var="product"
							varStatus="status">
							<div class="col-xs-6 col-md-4">
								<product:productListerGridItem product="${product}" />
							</div>
						</c:forEach>

					</c:when>
					<c:otherwise>
						<c:forEach items="${searchPageData.results}" var="product">
							<div class="col-xs-12">
								<product:productListerItem product="${product}" />
							</div>
						</c:forEach>
					</c:otherwise>
				</c:choose>

			</c:if>

	</div>
	<c:if test="${data=='product'}">
		<div class="load-more">
			<c:if test="${(searchPageData.pagination.numberOfPages > 1)}">

				<c:if
					test="${searchPageData.pagination.currentPage < searchPageData.pagination.numberOfPages-1}">
					<input type="hidden" name="loadTotalNoOfPages"
						value="${searchPageData.pagination.numberOfPages}" />
					<input type="hidden" name="loadCurrentPage"
						value="${searchPageData.pagination.currentPage}" />
					<button class="btn btn-white">more</button>
				</c:if>

			</c:if>
		</div>
	</c:if>

	<div class="col-md-9">
		<div class="contentSearch-output">
			<!-- <h2>6 Items</h2> -->
			<c:if test="${data=='content'}">
				<div class="col-xs-12">
					<c:forEach items="${contentSearchPageData.results}" var="content">
						<div>
							<h3>
								<a href="${content.url}">${content.title}</a>
							</h3>
							<p>${content.text}</p>
						</div>
					</c:forEach>
				</div>
			</c:if>
		</div>
		<div id="addToCartTitle" style="display: none">
			<div class="add-to-cart-header">
				<div class="headline">
					<span class="headline-text"><spring:theme
							code="basket.added.to.basket" /></span>
				</div>
			</div>
		</div>

		<c:if test="${data=='content'}">
			<div class="load-more">
				<c:if test="${(contentSearchPageData.pagination.numberOfPages > 1)}">
					<form action="/store/greenlee/en/USD/search" method="get">
						<input type="hidden" name="q" value="${searchText}" /> <input
							type="hidden" name="data" value="${data}" /> <input
							type="hidden" name="page"
							value="${searchPageData.pagination.currentPage+1}" />
						<c:if
							test="${searchPageData.pagination.currentPage < searchPageData.pagination.numberOfPages-1}">
							<button class="btn btn-white">more</button>
						</c:if>
					</form>
				</c:if>
			</div>
		</c:if>
		<%-- <nav:pagination top="false"  supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="${searchPageData.currentQuery.url}"  numberPagesShown="${numberPagesShown}"/> --%>
	</div>
</div>
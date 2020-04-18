<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav" %>
<%@ taglib prefix="storepickup" tagdir="/WEB-INF/tags/desktop/storepickup" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="col-md-9">

<div class="clearfix facets-details">
	<nav:pagination top="true" supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="${searchPageData.currentQuery.url}"  numberPagesShown="${numberPagesShown}"/>
</div>

	<ul class="product-listing product-list">
		<c:forEach items="${searchPageData.results}" var="product" varStatus="status">
			<product:productListerItem product="${product}"/>
		</c:forEach>
	</ul>

	<c:if test="${(searchPageData.pagination.numberOfPages > 1)}">
		<div class="load-more">


			<c:if
				test="${searchPageData.pagination.currentPage < searchPageData.pagination.numberOfPages-1}">
				<input type="hidden" name="loadTotalNoOfPages"
					value="${searchPageData.pagination.numberOfPages}" />
				<input type="hidden" name="loadCurrentPage"
					value="${searchPageData.pagination.currentPage}" />
				<button class="btn btn-white">more</button>
			</c:if>


		</div>
	</c:if>

</div>



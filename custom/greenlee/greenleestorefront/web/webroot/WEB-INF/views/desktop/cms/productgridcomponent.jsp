<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="col-md-9">
<div class="clearfix facets-details">
	<nav:pagination top="true" supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="${searchPageData.currentQuery.url}" numberPagesShown="${numberPagesShown}"/>
	</div>

<div class="row product">
		<c:forEach items="${searchPageData.results}" var="product" varStatus="status">
			<div class="col-xs-6 col-md-4 item-box">
				<product:productListerGridItem product="${product}"/>
			</div>
		</c:forEach>


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

	


</div>
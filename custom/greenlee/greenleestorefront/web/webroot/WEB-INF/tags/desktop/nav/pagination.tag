<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="searchUrl" required="true" %>
<%@ attribute name="searchPageData" required="true" type="de.hybris.platform.commerceservices.search.pagedata.SearchPageData" %>
<%@ attribute name="top" required="true" type="java.lang.Boolean" %>
<%@ attribute name="supportShowAll" required="true" type="java.lang.Boolean" %>
<%@ attribute name="supportShowPaged" required="true" type="java.lang.Boolean" %>
<%@ attribute name="msgKey" required="false" %>
<%@ attribute name="showCurrentPageInfo" required="false" type="java.lang.Boolean"%>
<%@ attribute name="hideRefineButton" required="false" type="java.lang.Boolean"%>
<%@ attribute name="numberPagesShown" required="false" type="java.lang.Integer" %>
<%@ taglib prefix="pagination" tagdir="/WEB-INF/tags/desktop/nav/pagination" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="themeMsgKey" value="${not empty msgKey ? msgKey : 'search.page'}"/>
<c:set var="showCurrPage" value="${not empty showCurrentPageInfo ? showCurrentPageInfo : false}"/>
<c:set var="hideRefBtn" value="${hideRefineButton ? true : false}"/>
<div class="left-facets hidden-xs hidden-sm">
	<c:if test="${data ne'product' and data ne 'content'}">
		<span class="count hidden-xs hidden-sm"><spring:theme code="${themeMsgKey}.totalResults" arguments="${searchPageData.pagination.totalNumberOfResults}"/></span>	</c:if>
	<c:if test="${data=='product'}">
		<span class="count hidden-xs hidden-sm"><spring:theme code="${themeMsgKey}.totalResults" arguments="${searchPageData.pagination.totalNumberOfResults}"/></span>	</c:if>
	<c:if test="${data=='content'}">
		<span class="count hidden-xs hidden-sm"><spring:theme code="${themeMsgKey}.totalResults" arguments="${contentSearchPageData.pagination.totalNumberOfResults}"/></span>
	</c:if>
	<div class="facet-name hidden-md hidden-lg">
		refine by
	</div>
</div>
<div class="right-facets">
	<div class="refinement">
		<div class="sort-refine-bar">
			<div class="form-group mrefine hidden-md hidden-lg">
					<label><spring:theme code="search.page.refineTitle"/>
					<i class="fa fa-angle-down hidden-md hidden-lg" aria-hidden="true"></i>
					</label>
			</div>
			<div class="form-group">
			<c:if test="${data ne'product' and data ne 'content'}">
					<c:if test="${not empty searchPageData.sorts}">

					<form id="sortForm1" name="sortForm1" method="get" action="#">
						<c:if test="${not empty sortQueryParams}">
							<c:forEach var="queryParam" items="${fn:split(sortQueryParams, '&')}">
								<c:set var="keyValue" value="${fn:split(queryParam, '=')}"/>
								<c:if test="${not empty keyValue[1]}">
									<input type="hidden" name="${fn:escapeXml(keyValue[0])}" value="${fn:escapeXml(keyValue[1])}"/>
								</c:if>
							</c:forEach>
						</c:if>
						<label>
							<spring:theme code="search.page.sortTitle"/>
						</label>

						<select id="sortOptions1" name="sort" class="form-control custom-select">
							<option disabled><spring:theme code="${themeMsgKey}.sortTitle"/></option>
							<c:forEach items="${searchPageData.sorts}" var="sort">
								<option value="${sort.code}" ${sort.selected? 'selected="selected"' : ''}>
									<c:choose>
										<c:when test="${not empty sort.name}">
											${sort.name}
										</c:when>
										<c:otherwise>
											<spring:theme code="${themeMsgKey}.sort.${sort.code}"/>
										</c:otherwise>
									</c:choose>
								</option>
							</c:forEach>
						</select>

						<c:catch var="errorException">
							<spring:eval expression="searchPageData.currentQuery.query" var="dummyVar"/><%-- This will throw an exception is it is not supported --%>
							<input type="hidden" name="q" value="${(searchPageData.currentQuery.query.value)}"/>
						</c:catch>
						<input type="hidden" name="viewAs" value="${param.viewAs }" />
						<c:if test="${supportShowAll}">
							<ycommerce:testId code="searchResults_showAll_link">
								<input type="hidden" name="show" value="Page"/>
							</ycommerce:testId>
						</c:if>
						<c:if test="${supportShowPaged}">
							<ycommerce:testId code="searchResults_showPage_link">
								<input type="hidden" name="show" value="All"/>
							</ycommerce:testId>
						</c:if>
					</form>
				</c:if>
				</c:if>
				<c:if test="${data=='product'}">
					<c:if test="${not empty searchPageData.sorts}">

					<form id="sortForm1" name="sortForm1" method="get" action="#">
						<c:if test="${not empty sortQueryParams}">
							<c:forEach var="queryParam" items="${fn:split(sortQueryParams, '&')}">
								<c:set var="keyValue" value="${fn:split(queryParam, '=')}"/>
								<c:if test="${not empty keyValue[1]}">
									<input type="hidden" name="${fn:escapeXml(keyValue[0])}" value="${fn:escapeXml(keyValue[1])}"/>
								</c:if>
							</c:forEach>
						</c:if>
						<label>SORT BY:</label>

						<select id="sortOptions1" name="sort" class="form-control custom-select">
							<option disabled><spring:theme code="${themeMsgKey}.sortTitle"/></option>
							<c:forEach items="${searchPageData.sorts}" var="sort">
								<option value="${sort.code}" ${sort.selected? 'selected="selected"' : ''}>
									<c:choose>
										<c:when test="${not empty sort.name}">
											${sort.name}
										</c:when>
										<c:otherwise>
											<spring:theme code="${themeMsgKey}.sort.${sort.code}"/>
										</c:otherwise>
									</c:choose>
								</option>
							</c:forEach>
						</select>

						<c:catch var="errorException">
							<spring:eval expression="searchPageData.currentQuery.query" var="dummyVar"/><%-- This will throw an exception is it is not supported --%>
							<input type="hidden" name="q" value="${(searchPageData.currentQuery.query.value)}"/>
						</c:catch>
						<input type="hidden" name="viewAs" value="${param.viewAs }" />
						<c:if test="${supportShowAll}">
							<ycommerce:testId code="searchResults_showAll_link">
								<input type="hidden" name="show" value="Page"/>
							</ycommerce:testId>
						</c:if>
						<c:if test="${supportShowPaged}">
							<ycommerce:testId code="searchResults_showPage_link">
								<input type="hidden" name="show" value="All"/>
							</ycommerce:testId>
						</c:if>
					</form>
				</c:if>
				</c:if>
			</div>
		</div>
	</div>
	<c:if test="${data=='product'}">
	<div class="view-components">
	<span class="hidden-xs hidden-sm">view:</span>

	<ul class="views">
		<li>
			<c:set value="${fn:contains(param.viewAs,'grid') ? 'active':'' }" var="selected" />
			<a class="${selected}" href="<c:url value='${viewUrl}&viewAs=grid' />">
				<i class="fa fa-th"></i>
			</a>
		</li>
		<li>
			<c:set value="${fn:contains(param.viewAs,'grid') ? '':'active' }" var="listed" />
			<a class="${listed}" href="<c:url value='${viewUrl}&viewAs=list' />">
				<i class="fa fa-list"></i>
			</a>
		</li>
	</ul>
	</div>
	</c:if>
	<c:if test="${data ne 'product' and data ne 'content'}">
	<c:url value="" var="viewListingTypeURL"/>
	<div class="view-components">
	<span class="hidden-xs hidden-sm">view:</span>

	<ul class="views">
		<li>
			<c:set value="${fn:contains(param.viewAs,'grid') ? 'active':'' }" var="selected" />
			<a class="${selected}" href="${viewListingTypeURL}?q=${(searchPageData.currentQuery.query.value)}&viewAs=">
				<i class="fa fa-th"></i>
			</a>
		</li>
		<li>
			<c:set value="${fn:contains(param.viewAs,'grid') ? '':'active' }" var="listed" />
			<a class="${listed}" href="${viewListingTypeURL}?q=${(searchPageData.currentQuery.query.value)}&viewAs=list">
				<i class="fa fa-list"></i>
			</a>
		</li>
	</ul>
	</div>
	</c:if>
</div>

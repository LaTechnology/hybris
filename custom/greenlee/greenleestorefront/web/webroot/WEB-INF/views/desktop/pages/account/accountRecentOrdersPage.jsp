<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="gl" uri="http://hybris.com/tld/greenlee" %>


<spring:url value="/my-account/order/" var="orderDetailsUrl"/>
<c:set var="searchUrl" value="/my-account/orders?sort=${searchPageData.pagination.sort}"/>

<div class="col-md-7">
<c:if test="${empty searchPageData.results}">
	<div class="orders-filter">
		<ycommerce:testId code="orderHistory_noOrders_label">
			<spring:theme code="text.account.orderHistory.noOrders" />
		</ycommerce:testId>
	</div>
</c:if>

<c:if test="${not empty searchPageData.results}">

<%-- 			 <h1><spring:theme code="text.account.orderHistory.recentorders"/><span> <spring:theme code="text.account.orderHistory.last2months" /></span></h1>
			 <spring:theme code="text.account.orderHistory.sapcustomerid"/><span>&nbsp;&nbsp;${customerData.sessionB2BUnit.uid} </span> --%>
			 <div class="border-bottom">
                 <div class="inline-block width60">
                    <h1 class="hidden-xs hidden-sm"><spring:theme code="text.account.orderHistory.recentorders"/><span> <spring:theme code="text.account.orderHistory.last2months" /></span></h1>
                 </div>
                 <div class="inline-block width40">
                    <span><spring:theme code="text.account.orderHistory.sapcustomerid"/>&nbsp;&nbsp;<gl:b2bunitid id="${customerData.sessionB2BUnit.uid}"/> </span> 
                    <span><c:if test = "${customerData.sessionB2BUnit.userType ne 'B2C'}">${customerData.sessionB2BUnit.name}</c:if> </span>
                 </div>
             </div>
                  <ul class="orders recent-list">
                  <c:forEach items="${searchPageData.results}" var="piHeaderDetails" varStatus="orderHistoryIndex">
                    <li class="${orderHistoryIndex.count % 2 == 0 ? 'even' : 'odd'}">
	                    <div class="order-header clearfix">
	                      <div class="half-wd-l">
	                        <div class="order-date">${piHeaderDetails.recordCreatedDateShort}</div>
	                        <div class="order-no"><spring:theme code="text.account.orderHistory.orderIdRecentOrders" text="ORDER ID: "/>${piHeaderDetails.salesOrderNo}</div>
	                      </div>
	                      <div class="half-wd-r order-right">
	                      	<div class="order-detail-wrp">
	                        	<div class="order-status">${piHeaderDetails.status}</div>
	                      	</div>
	                        <i class="fa fa-angle-up"></i>
	                      </div>
	                    </div>
	                    <div class="full-width order-body">
	                      	<ul class="order-links">
	                      	<%-- <li><a href="javascript:;"><label><spring:theme code="text.account.orderHistory.trackingnumber" /></label></a></li> --%>
	                      	<li><a href="<c:url value='/my-account/piOrderDetails?orderid=${piHeaderDetails.salesOrderNo}&page=recent' />"><spring:theme code="text.account.orderHistory.orderDetails" /></a></li>
	                      	<c:if test="${showInvoice== true}">
	                      		<li class="last"><a href="${orderinvoiceurl}">Invoice</a></li>
	                      	</c:if>
	                      	</ul>
	                    </div>
                    </li>
                     </c:forEach>
                  </ul>
			</c:if>
  </div>

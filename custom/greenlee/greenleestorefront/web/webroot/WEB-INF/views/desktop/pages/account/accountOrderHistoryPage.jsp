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
<div class="col-md-10 your-order">
<div id="order-sort">

<div class="border-bottom">
    <div class="inline-block width60">
       <h1 class="hidden-xs hidden-sm"><spring:theme code="text.account.orderHistory.yourorders" /></h1>
    </div>
    <c:if test="${not empty searchPageData.results}">
    <div class="inline-block width40">
       <span><spring:theme code="text.account.orderHistory.sapcustomerid"/>&nbsp;&nbsp;<gl:b2bunitid id="${customerData.sessionB2BUnit.uid}"/></span>
       <span><c:if test = "${customerData.sessionB2BUnit.userType ne 'B2C'}"> ${customerData.sessionB2BUnit.name}</c:if> </span>
    </div>
    </c:if>
</div>

<c:if test="${empty searchPageData.results}">
	<div class="orders-filter">
		<ycommerce:testId code="orderHistory_noOrders_label">
			<spring:theme code="text.account.orderHistory.noOrders" />
		</ycommerce:testId>
	</div>
</c:if>

<c:if test="${not empty searchPageData.results}">
		<div class="orders-filter">
			<div class="row">
				<div class="col-md-6">
					<p><span>${fn:length(searchPageData.results)}&nbsp;<spring:theme code="text.account.orderHistory.orders" /></span>&nbsp;<spring:theme code="text.account.orderHistory.placed" /></p>
					
				</div>
				</div>
		</div>
<ul class="orders list">
		<c:forEach items="${searchPageData.results}" var="piHeaderDetails" varStatus="orderHistoryIndex">
		
		 <c:choose>
		 	 <c:when test="${orderHistoryIndex.count % 2 == 0}">
		         <li class="even">
		     </c:when>
		     <c:otherwise>
		       <li class="odd">
		     </c:otherwise>
		  </c:choose>
			 <div class="order-header clearfix">
			 	<div class="row">
					<div class="col-md-2">
						<label><spring:theme code="text.account.orderHistory.orderId" /></label>
							<h3 class="order-id">${piHeaderDetails.salesOrderNo}</h3>
					</div>
					<c:if test="${showInvoice== true}">
						<div class="col-md-2">
							<label><spring:theme code="text.account.orderHistory.purchaseOrderNo" /></label>
								<h3 class="order-id">${piHeaderDetails.customerPurchaseOrderNo}</h3>
						</div>
					</c:if>
					<div class="col-md-4">
						<label><spring:theme code="text.account.orderHistory.orderdate" /></label>
						<h3 class="order-date">${piHeaderDetails.recordCreatedDate}</h3>
					</div>
					<div class="col-md-2">
						<label><spring:theme code="text.account.orderHistory.ordertotal" /></label>
						<h3 class="order-total">${piHeaderDetails.documentCurrency} $ ${piHeaderDetails.netValueInDocumentCurrency}</h3>
					</div>
					<div class="col-md-2">
						<label><spring:theme code="text.account.orderHistory.status" /></label>
						<h3 class="order-status">${piHeaderDetails.status}</h3>
					</div>
					<%-- <div class="col-md-2">
						<label><spring:theme code="text.account.orderHistory.trackingnumber" /></label>
						<h3 class="tracking-no">${piHeaderDetails.customerPurchaseOrderNo}</h3>
					</div> --%>
				  </div>
                  </div>
	                    <div class="full-width order-body">
							<a class="btn btn-white visible-xs visible-sm" href="<c:url value='/my-account/piOrderDetails?orderid=${piHeaderDetails.salesOrderNo}&page=history' />"><spring:theme code="text.account.orderHistory.orderDetails" /></a>
	                      	<ul class="order-links visible-md visible-lg">
								<li><a href="<c:url value='/my-account/piOrderDetails?orderid=${piHeaderDetails.salesOrderNo}&page=history&currency=${piHeaderDetails.documentCurrency}' />"><spring:theme code="text.account.orderHistory.orderDetails" /></a></li>
							<c:if test="${showInvoice== true}">
								<li class="last"><a href="${orderinvoiceurl}"><spring:theme code="text.account.orderHistory.invoice" /></a></li>
							</c:if>
	                      	</ul>
	                    </div>  
			</li>
		</c:forEach>
</c:if>
</ul>
</div>
</div>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<template:page pageTitle="${pageTitle}">
<div class="greenlee-body">
	<div class="cart-switch">
	    <div class="container">
		     	<cms:pageSlot position="BreadCrumbSlot" var="feature">
					<cms:component component="${feature}" />
				</cms:pageSlot>
			  <div id="globalMessages">
				<common:globalMessages/>
			</div>
		</div>
	</div>
	
	
<c:set var="orderCurrency" value=" "></c:set>	

<c:if test="${not empty param['currency']}">
	<c:set var="orderCurrency" value="${param['currency']}"></c:set>	
</c:if>



<div class="order-details-container">
	<div class="container">
		<h1><spring:theme code="account.orderdetail"/></h1>
		<div class="row odr-dtls-top">
			<div class="col-md-4">
				<div class="row">
					<label class="col-md-6"><spring:theme code="account.orderdetail.accountname"/></label>
					<h3 class="col-md-6">${orderData.name1}</h3>
				</div>
				<div class="row">
					<label class="col-md-6"><spring:theme code="account.orderdetail.transactionno"/></label>
					<h3 class="col-md-6">${orderData.salesOrderNo}</h3>
				</div>
				<div class="row">
					<label class="col-md-6"><spring:theme code="account.orderdetail.transactiontype"/></label>
					<h3 class="col-md-6">${orderData.description}</h3>
				</div>
				<div class="row">
					<label class="col-md-6"><spring:theme code="account.orderdetail.status"/></label>
					<h3 class="col-md-6"> ${orderData.status}</h3>
				</div>
			</div>
			<div class="col-md-4">
				<div class="row">
					<label class="col-md-6"><spring:theme code="account.orderdetail.iosuser"/></label>
					<h3 class="col-md-6">${user.email}</h3>
				</div>
				<c:if test="${showInvoice== true}">
						<div class="row">
							<label class="col-md-6"><spring:theme code="account.orderdetail.ponumber"/></label>
							<h3 class="col-md-6">${orderData.customerPurchaseOrderNo}</h3>
						</div>
				</c:if>
				
				<div class="row">
					<label class="col-md-6"><spring:theme code="account.orderdetail.terms"/></label>
					<h3 class="col-md-6">${orderData.paymentTerms}</h3>
				</div>
				<div class="row">
					<label class="col-md-6"><spring:theme code="account.orderdetail.trnsdate"/></label>
					<h3 class="col-md-6">${orderData.recordCreatedDate}</h3>
				</div>
			</div>
		</div>
		<div class="odr-dtls-boxes">
			<div class="left">
				<div class="column">
					<div>
						<label><spring:theme code="account.orderdetail.soldto"/>:&nbsp;${orderData.soldToDetails.partyNumber}</label>
						<!-- B2E -->
						<c:if test="${userType=='B2E' && not empty orderData.soldToDetails.name1 && not empty orderData.soldToDetails.name2}">
							<h3>${orderData.soldToDetails.name1}</h3>
							<h3>${orderData.soldToDetails.name2}</h3>
						</c:if>
						<c:if test="${userType=='B2E' && not empty orderData.soldToDetails.name1 && empty orderData.soldToDetails.name2}">
							<h3>${orderData.soldToDetails.name1}</h3>
						</c:if>
						<c:if test="${userType=='B2E' &&  empty orderData.soldToDetails.name1 && not empty orderData.soldToDetails.name2}">
							<h3>${orderData.soldToDetails.name2}</h3>
						</c:if>
						<!-- B2B -->
						<c:if test="${userType=='B2B' && not empty orderData.soldToDetails.name1 && not empty orderData.soldToDetails.name2}">
							<h3>${orderData.soldToDetails.name1}</h3>
							<h3>${orderData.soldToDetails.name2}</h3>
						</c:if>
						<c:if test="${userType=='B2B' && not empty orderData.soldToDetails.name1 && empty orderData.soldToDetails.name2}">
							<h3>${orderData.soldToDetails.name1}</h3>
						</c:if>
						<c:if test="${userType=='B2B' &&  empty orderData.soldToDetails.name1 && not empty orderData.soldToDetails.name2}">
							<h3>${orderData.soldToDetails.name2}</h3>
						</c:if>
						<!-- B2C -->
						<c:if test="${userType=='B2C' && not empty orderData.soldToDetails.name1 && empty orderData.soldToDetails.name2}">
							<h3>${orderData.soldToDetails.name2}</h3>
						</c:if>
						<c:if test="${userType=='B2C' && not empty orderData.soldToDetails.name1 && empty orderData.soldToDetails.name2}">
							<h3>${orderData.soldToDetails.name1}</h3>
						</c:if>
						<c:if test="${userType=='B2C' &&  empty orderData.soldToDetails.name1 && not empty orderData.soldToDetails.name2}">
							<h3>${orderData.soldToDetails.name2}</h3>
						</c:if>
						<p>
							${orderData.soldToDetails.houseNumberAndStreet}</br>
							${orderData.soldToDetails.city},
							${orderData.soldToDetails.region} </br> 
							${orderData.soldToDetails.countryKey}</br>
							${orderData.soldToDetails.postalCode}
						</p>
					</div>
				</div>
				<div class="column">
					<div>
						<label><spring:theme code="account.orderdetail.billto"/>:&nbsp;${orderData.billToDetails.partyNumber}</label>
						<!-- GRE-2111 -->
						<!-- B2E -->
						<c:if test="${userType=='B2E' && not empty orderData.billToDetails.name1 && not empty orderData.billToDetails.name2}">
							<h3>${orderData.billToDetails.name1}</h3>
							<h3>${orderData.billToDetails.name2}</h3>
						</c:if>
						<c:if test="${userType=='B2E' && not empty orderData.billToDetails.name1 && empty orderData.billToDetails.name2}">
							<h3>${orderData.billToDetails.name1}</h3>
						</c:if>
						<c:if test="${userType=='B2E' &&  empty orderData.billToDetails.name1 && not empty orderData.billToDetails.name2}">
							<h3>${orderData.billToDetails.name2}</h3>
						</c:if>
						
						<!-- B2B -->
						<c:if test="${userType=='B2B' && not empty orderData.billToDetails.name1 && not empty orderData.billToDetails.name2}">
							<h3>${orderData.billToDetails.name1}</h3>
							<h3>${orderData.billToDetails.name2}</h3>
						</c:if>
						<c:if test="${userType=='B2B' && not empty orderData.billToDetails.name1 && empty orderData.billToDetails.name2}">
							<h3>${orderData.billToDetails.name1}</h3>
						</c:if>
						<c:if test="${userType=='B2B' &&  empty orderData.billToDetails.name1 && not empty orderData.billToDetails.name2}">
							<h3>${orderData.billToDetails.name2}</h3>
						</c:if>
						<!-- B2C -->
						<c:if test="${userType=='B2C' && not empty orderData.billToDetails.name1 && not empty orderData.billToDetails.name2}">
							<h3>${orderData.billToDetails.name2}</h3>
						</c:if>
						<c:if test="${userType=='B2C' && not empty orderData.billToDetails.name1 && empty orderData.billToDetails.name2}">
							<h3>${orderData.billToDetails.name1}</h3>
						</c:if>
						<c:if test="${userType=='B2C' &&  empty orderData.billToDetails.name1 && not empty orderData.billToDetails.name2}">
							<h3>${orderData.billToDetails.name2}</h3>
						</c:if>
						<p>
							${orderData.billToDetails.houseNumberAndStreet}</br>
							${orderData.billToDetails.city},
							${orderData.billToDetails.region} </br> 
							${orderData.billToDetails.countryKey}</br>
							${orderData.billToDetails.postalCode}
						</p>
					</div>
				</div>
				<div class="column">
					<div>
						<label><spring:theme code="account.orderdetail.shipto"/>:&nbsp;${orderData.shipToDetails.partyNumber}</label>
						<!-- GRE-2111 -->
						<!-- B2E-->
						<c:if test="${userType=='B2E' && not empty orderData.shipToDetails.name1 && not empty orderData.shipToDetails.name2}">
							<h3>${orderData.shipToDetails.name1}</h3>
							<h3>${orderData.shipToDetails.name2}</h3>
						</c:if>
						<c:if test="${userType=='B2E' && not empty orderData.shipToDetails.name1 && empty orderData.shipToDetails.name2}">
							<h3>${orderData.shipToDetails.name1}</h3>
						</c:if>
						<c:if test="${userType=='B2E' && empty orderData.shipToDetails.name1 && not empty orderData.shipToDetails.name2}">
							<h3>${orderData.shipToDetails.name2}</h3>
						</c:if>
						<!-- B2B -->
						<c:if test="${userType=='B2B' && not empty orderData.shipToDetails.name1 && not empty orderData.shipToDetails.name2}">
							<h3>${orderData.shipToDetails.name1}</h3>
							<h3>${orderData.shipToDetails.name2}</h3>
						</c:if>
						<c:if test="${userType=='B2B' && not empty orderData.shipToDetails.name1 && empty orderData.shipToDetails.name2}">
							<h3>${orderData.shipToDetails.name1}</h3>
						</c:if>
						<c:if test="${userType=='B2B' && empty orderData.shipToDetails.name1 && not empty orderData.shipToDetails.name2}">
							<h3>${orderData.shipToDetails.name2}</h3>
						</c:if>
						<!-- B2C -->
						<c:if test="${userType=='B2C' && not empty orderData.shipToDetails.name1 && not empty orderData.shipToDetails.name2}">
							<h3>${orderData.shipToDetails.name1}</h3>
							<h3>${orderData.shipToDetails.name2}</h3>
						</c:if>
						<c:if test="${userType=='B2C' && not empty orderData.shipToDetails.name1 && empty orderData.shipToDetails.name2}">
							<h3>${orderData.shipToDetails.name1}</h3>
						</c:if>
						<c:if test="${userType=='B2C' && empty orderData.shipToDetails.name1 && not empty orderData.shipToDetails.name2}">
							<h3>${orderData.shipToDetails.name2}</h3>
						</c:if>
						<%-- 
						<c:if test="${not empty orderData.shipToDetails.name1 && not empty orderData.shipToDetails.name2}">
							<h3>${orderData.shipToDetails.name1}</h3>
							<h3>${orderData.shipToDetails.name2}</h3>
						</c:if> --%>
						<p>
							${orderData.shipToDetails.houseNumberAndStreet}</br>
							${orderData.shipToDetails.city},
							${orderData.shipToDetails.region} </br> 
							${orderData.shipToDetails.countryKey}</br>
							${orderData.shipToDetails.postalCode}
						</p>
					</div>
				</div>
			</div>
			<div class="manage-order">
				<h2><spring:theme code="account.orderdetail.manageorder"/></h2>
				<ul>
					<li><a href="#" onClick="window.print()"><i class="fa fa-print fa-m"></i><spring:theme code="account.orderdetail.print"/></a></li>
					<c:if test="${showInvoice== true}">
						<li><a href="${orderinvoiceurl}"><i class="fa fa-file fa-m"></i><spring:theme code="account.orderdetail.invoice"/></a></li>
					</c:if>
				</ul>
			</div>
		</div>
		<div class="row">
			<div class="col-md-9">
				<div class="order-items">
					<%-- <h2><spring:theme code="account.orderdetail.items"/>&nbsp;${fn:length(orderData.orderDetailLine)}</h2> --%>
					<div class="clearfix cart-heading hidden-xs hidden-sm">
						<div class="col line">
							<span class="heading"><spring:theme code="account.orderdetail.line"/></span>
						</div>
						<div class="col material">
							<span class="heading"><spring:theme code="account.orderdetail.material"/></span>
						</div>
						<div class="col qty">
							<span class="heading"><spring:theme code="account.orderdetail.quantity"/></span>
						</div>
						<div class="col uom">
							<span class="heading"><spring:theme code="account.orderdetail.uom"/></span>
						</div>
						<div class="col ship-qty">
							<span class="heading"><spring:theme code="account.orderdetail.shipqty"/></span>
						</div>
						<div class="col open-qty">
							<span class="heading"><spring:theme code="account.orderdetail.openQty"/></span>
						</div>
						<div class="col unit-price">
							<span class="heading"><spring:theme code="account.orderdetail.unitprice"/></span>
						</div>
						<div class="col exit-price">
							<span class="heading"><spring:theme code="account.orderdetail.exitprice"/></span>
						</div>
					</div>
					<ul class="cart-list">
					<c:forEach items="${orderData.orderDetailLine}" var="PIItems" varStatus="piItemIndex">
					 <c:choose>
						 <c:when test="${piItemIndex.count % 2 == 0}">
					          <li class="product-item odd">
					        </c:when>
					        <c:otherwise>
					           <li class="product-item">
					        </c:otherwise>
					  </c:choose>
						<div class="clearfix">
							<div class="col line"><label class="hidden-md hidden-lg"><spring:theme code="account.orderdetail.line"/></label><span>${PIItems.salesOrderItemNo}</span></div>
							<div class="col material"><label class="hidden-md hidden-lg"><spring:theme code="account.orderdetail.material"/></label><span>
								<c:set var="numberAsString">${fn:trim(PIItems.catalogNumber)}</c:set>
								<c:choose>
									<c:when test="${numberAsString.matches('[0-9]+')}">
							    		<fmt:formatNumber value="${PIItems.catalogNumber}" type="number" pattern="#"></fmt:formatNumber>
									</c:when>
									<c:otherwise>${PIItems.catalogNumber}
									</c:otherwise>
								</c:choose>
							</span></div>
							<div class="col qty"><label class="hidden-md hidden-lg"><spring:theme code="account.orderdetail.quantity"/></label><span><fmt:formatNumber value="${fn:trim(PIItems.orderQuantity)}" type="number" pattern="#"></fmt:formatNumber></span></div>
							<div class="col uom"><label class="hidden-md hidden-lg"><spring:theme code="account.orderdetail.uom"/></label><span>${PIItems.unitOfMeasure}</span></div>
							<div class="col ship-qty"><label class="hidden-md hidden-lg"><spring:theme code="account.orderdetail.shipqty"/></label><span><fmt:formatNumber value="${fn:trim(PIItems.shippedQuantity)}" type="number" pattern="#"></fmt:formatNumber></span></div>
							<div class="col open-qty"><label class="hidden-md hidden-lg"><spring:theme code="account.orderdetail.openQty"/></label><span><fmt:formatNumber value="${fn:trim(PIItems.openQuantity)}" type="number" pattern="#"></fmt:formatNumber></span></div>
							<div class="col unit-price"><label class="hidden-md hidden-lg"><spring:theme code="account.orderdetail.unitprice" text="Unit price *"/></label><span>$${PIItems.unitrice}</span></div>
							<div class="col exit-price"><label class="hidden-md hidden-lg"><spring:theme code="account.orderdetail.exitprice" text="Extended Price *"/></label><span>$${PIItems.extPrice}</span></div>
							<div class="col prdt-img"><%-- <product:productGalleryThumbnail product="${product}" format="product"/> --%>
							<product:productPrimaryImage product="${PIItems.product}" format="cartIcon"/></div>
							<div class="col catalog">
								<%-- <label><spring:theme code="account.orderdetail.catalogno"/>${PIItems.catalogNumber}</label>  --%>
								<label><span><spring:theme code="account.orderdetail.upc"/></span>${PIItems.upcCode}</label> 
								<label><span><spring:theme code="account.orderdetail.materialno"/> </span>
								<c:set var="numberAsString" value="${fn:trim(PIItems.materialNumber)}"></c:set>
									<c:choose>										
										<c:when test="${numberAsString.matches('[0-9]+')}">										
								    		<fmt:formatNumber value="${fn:trim(numberAsString)}" type="number" pattern="#"></fmt:formatNumber>
										</c:when>
										<c:otherwise>${numberAsString}
										</c:otherwise>
									</c:choose>
								</label>
							</div>
							<div class="col desc">
								<label><span><spring:theme code="account.orderdetail.description"/></span></label>
								<p>${PIItems.materialDescription}</p>
							</div>
							<div class="col ship-date">
							<c:if test="${not empty PIItems.status}">
								<label><span><spring:theme code="account.orderdetail.linestatus" text="Status:"/></span>&nbsp;${PIItems.status}</label>
							</c:if>
							<c:if test="${empty PIItems.status}">
								<label><span><spring:theme code="account.orderdetail.status" text="Status:"/></span>&nbsp;<spring:theme code="account.orderdetail.notavailable" text="N/A"/> </label>
							</c:if>
							
							<c:if test="${not empty PIItems.scheduleLineDate}">
								<label><span><spring:theme code="account.orderdetail.shipment" text="Shipment:"/></span>&nbsp;${PIItems.scheduleLineDate}</label> 
							</c:if>
							<c:if test="${empty PIItems.scheduleLineDate}">
								<label><span><spring:theme code="account.orderdetail.shipment" text="Shipment:"/></span>&nbsp;<spring:theme code="account.orderdetail.notavailable" text="N/A"/></label> 
							</c:if>
									
							<c:if test="${not empty PIItems.scheduleLineDate}">
								<c:if test="${not empty PIItems.trackingNoDetails}">
									<c:forEach items="${PIItems.trackingNoDetails}" var="PITrackingItems">
										<c:if test="${not empty PITrackingItems.route}">
											<label><span><spring:theme code="account.orderdetail.route" text="Ship Via: "/></span>&nbsp;${PITrackingItems.route}</label>
										</c:if>
										<a href="${PITrackingItems.carrierURL}" target="_blank">
										<label><span><spring:theme code="account.orderdetail.trackingno"/></span>&nbsp;${PITrackingItems.trackingNo}</label> </a>
									</c:forEach>
								</c:if>
							</c:if>
							<c:if test="${not empty PIItems.scheduleLineDate}">
								<c:if test="${not empty PIItems.serialNoDetails}">
									<label><span style="float:left;"><spring:theme code="account.orderdetail.serialnum"/></span>
									<div style="display:inline-block;">
										<c:forEach items="${PIItems.serialNoDetails}" var="PISerialNoDetails">
											<c:if test="${not empty PISerialNoDetails.serialNumber}">
													${PISerialNoDetails.serialNumber}<br/>
											</c:if>
										</c:forEach>
									</div>	
									</label>
								</c:if>
							</c:if>
							<c:if test="${empty PIItems.scheduleLineDate}">
								<label><span><spring:theme code="account.orderdetail.trackingno"/></span>&nbsp;<spring:theme code="account.orderdetail.notavailable" text="N/A"/></label> 
								<label><span><spring:theme code="account.orderdetail.serialnum"/></span>&nbsp;<spring:theme code="account.orderdetail.serialnumnotavailable" text="N/A"/></label> 
							</c:if>
							</div>
						</div>
						</li>
						</c:forEach>
					</ul>
					 <div class="net-val clearfix">
					 	<div class="net-val-table">
						 	<table>
						 		<tbody>
									<tr><td class="td-spacing"><spring:theme code="account.orderdetail.subTotal"/></td> <td>$&nbsp; ${orderData.subTotal}</td> </tr>
									<tr><td class="td-spacing"><spring:theme code="account.orderdetail.duty"/></td> <td>$&nbsp; ${orderData.duty}</td> </tr>
 									<c:choose>
										<c:when test="${fn:contains(orderData.freight, 'advise')}">
											<tr><td class="td-spacing"><spring:theme code="account.orderdetail.freight"/></td><td>$&nbsp; ${orderData.freight}</td> </tr>
										</c:when>
										<c:otherwise>
											<tr><td class="td-spacing"><spring:theme code="account.orderdetail.freight"/></td><td>$&nbsp; ${orderData.freight}</td></tr>
										</c:otherwise>
									</c:choose>

									<tr><td class="td-spacing last"><spring:theme code="account.orderdetail.tax"/></td> <td>$&nbsp; ${orderData.tax}</td> </tr>
								</tbody>
								<tfoot>
									<tr>
										<th class="td-spacing"><spring:theme code="account.orderdetail.netvalue"/></th><th>${orderCurrency}&nbsp; $${orderData.netValueinDocumentCurrency}</th>
									</tr>
								</tfoot>
							</table>
						</div>
					</div>
					<label><spring:theme code="account.orderdetail.applicablepromo" text="* Includes applicable Promo code discounts."/></label>
				</div>
			</div>
			<div class="col-md-3">
				<div class="notes">
					<h2>Notes and Shipping Instructions</h2>
					<div class="box">${orderData.orderNote} ${orderData.shipInstLongText}</div>
				</div>
			</div>
		</div>
	</div>
</div>
</div>
</template:page>
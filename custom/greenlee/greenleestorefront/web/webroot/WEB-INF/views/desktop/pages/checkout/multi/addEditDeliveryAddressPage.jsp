<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="multi-checkout"
	tagdir="/WEB-INF/tags/desktop/checkout/multi"%>
<%@ taglib prefix="b2b-multi-checkout"
	tagdir="/WEB-INF/tags/addons/b2bacceleratoraddon/desktop/checkout/multi"%>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/desktop/address"%>

<template:page pageTitle="${pageTitle}" hideHeaderLinks="true">
	<div class="checkout-wrap">
		<div class="container">
			<div class="row">
				<div class="col-md-9">
					<div class="checkout-accordion">
						<multi-checkout:checkoutProgressBar steps="${checkoutSteps}" progressBarId="${progressBarId}">
							<div id="globalMessages">
								<common:globalMessages/>
							</div>
							<div class="checkout-content active" id="step1">
								<div id="countrySelector" data-address-code="${addressData.id}" data-country-iso-code="${addressData.country.isocode}" class="row label-bottom">
									<div class="col-md-6">
										<label><spring:theme code="checkout.multi.deliveryAddress.select.savedAddress" text="Select from your saved address"/></label>
									</div>
									<div class="col-md-6 checkout-address">
										<a href="#" class="">
											<spring:theme code="checkout.addnew.shippingAddress" text="Add a new address"></spring:theme>
										</a>
									</div>
								</div>
								<div id="addressbook" class="clearfix owl-carousel owl-theme ${activeAddressBook} address-checkout">
									<c:if test="${not empty deliveryAddresses}">
										<c:forEach var="deliveryAddress" items="${deliveryAddresses}" varStatus="status">
											<c:set var="isNotEditable" value="${deliveryAddress.isSapSourcedAddress}"/>
											<div class="addressEntry item">
												<div class="address-item">
													<c:choose>
														<c:when test="${deliveryAddress.id eq cartData.deliveryAddress.id}">
															<form action="${request.contextPath}/checkout/multi/delivery-address/select" method="GET" class="delivery-form">
																<input type="hidden" name="selectedAddressCode" value="${deliveryAddress.id}"/>
																<input type="hidden" name="addressCode" value="${deliveryAddress.id}"/>
																<input type="hidden" name="editAddressCode" value="${deliveryAddress.id}"/>
																<div class="checkbox checkbox-circle">
																	<div class="round">
																		<span class="checked"></span>
																	</div>
																	<h5>${fn:escapeXml(deliveryAddress.title)}&nbsp; ${fn:escapeXml(deliveryAddress.firstName)}&nbsp; ${fn:escapeXml(deliveryAddress.lastName)}</h5>
																	<address>
																		${fn:escapeXml(deliveryAddress.line1)}<br/>
																		${fn:escapeXml(deliveryAddress.line2)}<br/>
																		${fn:escapeXml(deliveryAddress.town)}&nbsp; ${fn:escapeXml(deliveryAddress.postalCode)}<br/>
																		${fn:escapeXml(deliveryAddress.country.name)}
																		<c:choose>
																			<c:when test="${not empty deliveryAddress.region.name}">
																				&nbsp; ${fn:escapeXml(deliveryAddress.region.name)}
																			</c:when>
																			<c:when test="${not empty deliveryAddress.district}">
																				&nbsp; ${fn:escapeXml(deliveryAddress.district)}
																			</c:when>
																		</c:choose>
																	</address>

																	<spring:theme code="address.country.disclaimer"/>
																</div>
															</form>
															<div class="address-links clearfix">
																<c:if test="${not isNotEditable}">
																	<ul>
																		<li class="first">
																			<form:form action="${request.contextPath}/checkout/multi/delivery-address/remove" method="POST" >
																				<input type="hidden" name="addressCode" value="${deliveryAddress.id}"/>
																				<button type="submit" class="btn-trans remove">
																					<spring:theme code="checkout.multi.deliveryAddress.remove" text="Remove"/>
																				</button>
																			</form:form>

																		</li>
																		<li>
																			<form:form action="${request.contextPath}/checkout/multi/delivery-address/edit" method="POST">
																				<input type="hidden" name="addressCode" value="${deliveryAddress.id}"/>
																				<button type="submit" class="btn-trans edit"><spring:theme code="checkout.multi.deliveryAddress.edit" text="Edit"/></button>
																			</form:form>
																		</li>
																	</ul>
																</c:if>
															</div>
															<button type="submit" class="btn btn-primary btn-block btn-delivery">
																<spring:theme code="checkout.multi.deliveryAddress.continue.button" text="Continue"/>
															</button>
														</c:when>
														<c:otherwise>
															<form action="${request.contextPath}/checkout/multi/delivery-address/select" method="GET" class="delivery-form">
																<input type="hidden" name="selectedAddressCode" value="${deliveryAddress.id}"/>
																<input type="hidden" name="addressCode" value="${deliveryAddress.id}"/>
																<input type="hidden" name="editAddressCode" value="${deliveryAddress.id}"/>
																<div class="checkbox checkbox-circle">
																	<div class="round"></div>
																	<h5>${fn:escapeXml(deliveryAddress.title)}&nbsp; ${fn:escapeXml(deliveryAddress.firstName)}&nbsp; ${fn:escapeXml(deliveryAddress.lastName)}</h5>
																	<address>
																		${fn:escapeXml(deliveryAddress.line1)}<br/>
																		${fn:escapeXml(deliveryAddress.line2)}<br/>
																		${fn:escapeXml(deliveryAddress.town)}&nbsp; ${fn:escapeXml(deliveryAddress.postalCode)}<br/>
																		${fn:escapeXml(deliveryAddress.country.name)}
																		<c:choose>
																			<c:when test="${not empty deliveryAddress.region.name}">
																				&nbsp; ${fn:escapeXml(deliveryAddress.region.name)}
																			</c:when>
																			<c:when test="${not empty deliveryAddress.district}">
																				&nbsp; ${fn:escapeXml(deliveryAddress.district)}
																			</c:when>
																		</c:choose>
																	</address>
																</div>
															</form>
															<div class="address-links clearfix">
																<c:if test="${not isNotEditable}">
																	<ul>
																		<li class="first">
																			<form:form action="${request.contextPath}/checkout/multi/delivery-address/remove" method="POST">
																				<input type="hidden" name="addressCode" value="${deliveryAddress.id}"/>
																				<button type="submit" class="btn-trans remove">
																					<spring:theme code="checkout.multi.deliveryAddress.remove" text="Remove"/>
																				</button>
																			</form:form>

																		</li>
																		<li>
																			<form:form action="${request.contextPath}/checkout/multi/delivery-address/edit" method="POST">
																				<input type="hidden" name="addressCode" value="${deliveryAddress.id}"/>
																				<button type="submit" class="btn-trans edit"><spring:theme code="checkout.multi.deliveryAddress.edit" text="Edit"/></button>
																			</form:form>
																		</li>
																	</ul>
																</c:if>
															</div>
															<button type="submit" class="btn-white btn-border btn-delivery">
																<spring:theme code="checkout.multi.deliveryAddress.select.button" text="Select this Address"/>
															</button>
														</c:otherwise>
													</c:choose>
												</div>
											</div>
										</c:forEach>
									</c:if>
								</div>
								<div class="add-address address-checkout ${activeAddAddress}">
									<address:addressFormSelectorForCheckout supportedCountries="${countries}" regions="${regions}" cancelUrl="${currentStepUrl}" country="${country}"/>
								</div>
							</div>
							<address:suggestedAddresses selectedAddressUrl="/checkout/multi/delivery-address/select"/>
						</multi-checkout:checkoutProgressBar>
					</div>
				</div>
				<b2b-multi-checkout:checkoutOrderDetails cartData="${cartData}" showShipDeliveryEntries="true" showPickupDeliveryEntries="true" showTax="false"/>
			</div>
		</div>
	</div>
</template:page>

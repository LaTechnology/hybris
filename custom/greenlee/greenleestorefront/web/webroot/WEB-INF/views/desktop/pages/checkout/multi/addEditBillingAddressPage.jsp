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

	<cms:pageSlot position="ASMComponentSlot" var="feature">
		<cms:component component="${feature}" />
	</cms:pageSlot>

	<div class="checkout-wrap">
		<div class="container">
			<div class="row">
				<div class="col-md-9">
					<div class="checkout-accordion">
						<multi-checkout:checkoutProgressBar steps="${checkoutSteps}"
							progressBarId="${progressBarId}">
							<div id="globalMessages">
								<common:globalMessages />
							</div>
							<div class="checkout-content active" id="step1">
								<c:if test="${activeAddressBook eq 'active'}">
									<div id="countrySelector" data-address-code="${addressData.id}"
										data-country-iso-code="${addressData.country.isocode}"
										class="row label-bottom">
										<div class="col-md-6">
											<label><spring:theme
													code="checkout.multi.deliveryAddress.select.savedAddress"
													text="Select from your saved address" /></label>
											<p>
												<spring:theme code="address.country.disclaimer" />
											</p>
										</div>
										<div class="col-md-6 checkout-address">
											<a href="#" class=""> <spring:theme
													code="checkout.addnew.shippingAddress"
													text="Add a new address"></spring:theme>
											</a>
										</div>
									</div>
								</c:if>
								<c:if test="${activeAddAddress eq 'active'}">
									<div id="countrySelector" data-address-code="${addressData.id}"
										data-country-iso-code="${addressData.country.isocode}"
										class="row label-bottom">
										<div class="col-md-6">
											<label><spring:theme
													code="checkout.edit.shippingAddress"
													text="Edit a new address" /></label>
										</div>
										<div class="col-md-6 checkout-address">
											<a href="#" class=""> <spring:theme
													code="checkout.multi.deliveryAddress.select.savedAddress"
													text="Select from your saved address" />
											</a>
										</div>
									</div>
								</c:if>
								<div id="addressbook"
									class="clearfix owl-carousel owl-theme ${activeAddressBook} address-checkout">
									<c:if test="${not empty deliveryAddresses}">
										<c:forEach var="deliveryAddress" items="${deliveryAddresses}"
											varStatus="status">
											<c:set var="isNotEditable"
												value="${deliveryAddress.isSapSourcedAddress}" />
											<c:choose>
												<c:when
													test="${deliveryAddress.id eq cartData.deliveryAddress.id}">
													<div class="addressEntry item">
														<div class="address-item">
															<form
																action="${request.contextPath}/checkout/multi/billing-address/select"
																method="GET" class="billing-form">
																<input type="hidden" name="selectedAddressCode"
																	value="${deliveryAddress.id}" /> <input type="hidden"
																	name="addressCode" value="${deliveryAddress.id}" /> <input
																	type="hidden" name="editAddressCode"
																	value="${deliveryAddress.id}" />
																<div class="checkbox checkbox-circle">
																	<div class="round">
																		<span class="checked"></span>
																	</div>
																	<!-- Company -->
																	<h5>${fn:escapeXml(deliveryAddress.firstName)}&nbsp;
																		${fn:escapeXml(deliveryAddress.lastName)}</h5>
																	<address>
																		<c:if test="${not empty deliveryAddress.line2}">									
																			${fn:escapeXml(deliveryAddress.line1)}<br>
																			${fn:escapeXml(deliveryAddress.line2)}																	
																		</c:if>
																		<c:if test="${empty deliveryAddress.line2}">									
																			${fn:escapeXml(deliveryAddress.line1)}																
																		</c:if>
																		<br />

																		<c:if test="${not empty deliveryAddress.region.name}">																			
																			${fn:escapeXml(deliveryAddress.town)},&nbsp;${fn:escapeXml(deliveryAddress.region.name)}&nbsp;
																			${fn:escapeXml(deliveryAddress.postalCode)}
																			
																		</c:if>
																		<c:if test="${empty deliveryAddress.region.name}">																			
																			${fn:escapeXml(deliveryAddress.town)},&nbsp;${fn:escapeXml(deliveryAddress.postalCode)}
																		</c:if>
																		<br /> ${fn:escapeXml(deliveryAddress.country.name)}
																	</address>
																</div>
															</form>
															<div class="address-links clearfix">
																<c:set var="isDefaultRemove" value="false" />
																<c:if test="${not isNotEditable}">
																	<ul>
																		<c:if
																			test="${defaultAddressId ne deliveryAddress.id and not deliveryAddress.billingAddress}">
																			<li class="first"><form:form
																					action="${request.contextPath}/checkout/multi/billing-address/remove"
																					method="POST">
																					<input type="hidden" name="addressCode"
																						value="${deliveryAddress.id}" />
																					<button type="submit" class="btn-trans remove">
																						<spring:theme
																							code="checkout.multi.deliveryAddress.remove"
																							text="Remove" />
																					</button>
																				</form:form></li>
																			<c:set var="isDefaultRemove" value="true" />
																		</c:if>
																		<li><c:if
																				test="${not deliveryAddress.billingAddress}">
																				<form:form
																					action="${request.contextPath}/checkout/multi/billing-address/edit"
																					method="GET">
																					<input type="hidden" name="editAddressCode"
																						value="${deliveryAddress.id}" />
																					<button type="submit" class="btn-trans edit">
																						<spring:theme
																							code="checkout.multi.deliveryAddress.edit"
																							text="Edit" />
																					</button>
																				</form:form>
																			</c:if></li>

																	</ul>
																</c:if>
																<c:if
																	test="${isNotEditable or deliveryAddress.billingAddress}">
																	<ul>
																		<li><spring:theme
																				code="billing.address.to.change"
																				text="contact Greenlee Customer Support" /></li>
																	</ul>
																</c:if>
																<c:if
																	test="${isDefaultRemove eq 'false' and not isNotEditable and not deliveryAddress.billingAddress}">
																	<ul>
																		<li><spring:theme code="shipping.address.to.edit"
																				text="Default user address, To remove, please go to YOUR ACCOUNT/ADDRESS BOOK to modify Default address selection" />
																		</li>
																	</ul>
																</c:if>
															</div>
															<button type="submit"
																class="btn btn-primary btn-block btn-delivery">
																<spring:theme
																	code="checkout.multi.deliveryAddress.continue.button"
																	text="Continue" />
															</button>
														</div>
													</div>
												</c:when>
											</c:choose>
										</c:forEach>
										<c:forEach var="deliveryAddress" items="${deliveryAddresses}"
											varStatus="status">
											<c:set var="isNotEditable"
												value="${deliveryAddress.isSapSourcedAddress}" />
											<c:choose>
												<c:when
													test="${deliveryAddress.id != cartData.deliveryAddress.id}">
													<div class="addressEntry item">
														<div class="address-item">
															<form
																action="${request.contextPath}/checkout/multi/billing-address/select"
																method="GET" class="billing-form">
																<input type="hidden" name="selectedAddressCode"
																	value="${deliveryAddress.id}" /> <input type="hidden"
																	name="addressCode" value="${deliveryAddress.id}" /> <input
																	type="hidden" name="editAddressCode"
																	value="${deliveryAddress.id}" />
																<div class="checkbox checkbox-circle">
																	<div class="round"></div>
																	<c:if
																		test="${isNotEditable && not empty userType && userType=='B2E'}">
																		<h3>${fn:escapeXml(companyName)}</h3>
																	</c:if>

																	<h5>
																		<%-- ${fn:escapeXml(deliveryAddress.title)}&nbsp;  --%>${fn:escapeXml(deliveryAddress.firstName)}&nbsp;
																		${fn:escapeXml(deliveryAddress.lastName)}
																	</h5>
																	<address>
																		<c:if test="${not empty deliveryAddress.line2}">									
																		${fn:escapeXml(deliveryAddress.line1)}<br>
																		${fn:escapeXml(deliveryAddress.line2)}																	
																		</c:if>
																		<c:if test="${empty deliveryAddress.line2}">									
																		${fn:escapeXml(deliveryAddress.line1)}																
																		</c:if>
																		<br />

																		<c:if test="${not empty deliveryAddress.region.name}">																			
																			${fn:escapeXml(deliveryAddress.town)},&nbsp;${fn:escapeXml(deliveryAddress.region.name)}&nbsp;
																			${fn:escapeXml(deliveryAddress.postalCode)}
																			
																		</c:if>
																		<c:if test="${empty deliveryAddress.region.name}">																			
																			${fn:escapeXml(deliveryAddress.town)},&nbsp;${fn:escapeXml(deliveryAddress.postalCode)}
																		</c:if>
																		<br /> ${fn:escapeXml(deliveryAddress.country.name)}
																	</address>
																</div>
															</form>
															<div class="address-links clearfix">
																<c:set var="isRemove" value="false" />
																<c:if test="${not isNotEditable}">
																	<ul>

																		<c:if
																			test="${defaultAddressId ne deliveryAddress.id and not deliveryAddress.billingAddress}">
																			<li class="first"><form:form
																					action="${request.contextPath}/checkout/multi/billing-address/remove"
																					method="POST">
																					<input type="hidden" name="addressCode"
																						value="${deliveryAddress.id}" />
																					<button type="submit" class="btn-trans remove">
																						<spring:theme
																							code="checkout.multi.deliveryAddress.remove"
																							text="Remove" />
																					</button>
																				</form:form></li>
																			<c:set var="isRemove" value="true" />
																		</c:if>
																		<li><c:if
																				test="${not deliveryAddress.billingAddress}">
																				<form:form
																					action="${request.contextPath}/checkout/multi/billing-address/edit"
																					method="GET">
																					<input type="hidden" name="editAddressCode"
																						value="${deliveryAddress.id}" />
																					<button type="submit" class="btn-trans edit">
																						<spring:theme
																							code="checkout.multi.deliveryAddress.edit"
																							text="Edit" />
																					</button>
																				</form:form>
																			</c:if></li>
																	</ul>
																</c:if>
																<c:if
																	test="${isNotEditable or deliveryAddress.billingAddress}">
																	<ul>
																		<li><spring:theme
																				code="billing.address.to.change"
																				text="contact Greenlee Customer Support" /></li>
																	</ul>
																</c:if>
																<c:if
																	test="${isRemove eq 'false' and not isNotEditable and not deliveryAddress.billingAddress}">
																	<ul>
																		<li><spring:theme code="shipping.address.to.edit"
																				text="Default user address, To remove, please go to YOUR ACCOUNT/ADDRESS BOOK to modify Default address selection" />
																		</li>
																	</ul>
																</c:if>
															</div>
															<button type="submit"
																class="btn-white btn-border btn-delivery">
																<spring:theme
																	code="checkout.multi.deliveryAddress.select.button"
																	text="Select this Address" />
															</button>
														</div>
													</div>
												</c:when>
											</c:choose>
										</c:forEach>
									</c:if>
								</div>
								<div class="add-address address-checkout ${activeAddAddress}">
									<%-- 									<spring:theme  code="address.state.disclaimer"/>
 --%>
									<address:addressFormSelectorForCheckout
										supportedCountries="${countries}" regions="${regions}"
										cancelUrl="${currentStepUrl}" country="${country}" />
								</div>
							</div>
							<address:suggestedAddresses
								selectedAddressUrl="/checkout/multi/billing-address/select" />
						</multi-checkout:checkoutProgressBar>
					</div>
				</div>
				<b2b-multi-checkout:checkoutOrderDetails cartData="${cartData}"
					showShipDeliveryEntries="true" showPickupDeliveryEntries="true"
					showTax="false" />
			</div>
		</div>
	</div>
</template:page>
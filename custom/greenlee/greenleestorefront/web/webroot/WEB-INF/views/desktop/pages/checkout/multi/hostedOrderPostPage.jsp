<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="multi-checkout"
	tagdir="/WEB-INF/tags/desktop/checkout/multi"%>
<%@ taglib prefix="b2b-multi-checkout"
	tagdir="/WEB-INF/tags/addons/b2bacceleratoraddon/desktop/checkout/multi"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>

<spring:url value="/checkout/multi/payment-method/choose"
	var="placeOrderUrl" />
<template:page pageTitle="${pageTitle}" hideHeaderLinks="true">
	<script type="text/javascript">
	<!--
		window.completeCREPayment = function(responseObject) {
			orderNumber = responseObject.orderNumber;
			window.location = window.location.origin
					+ '/checkout/orderConfirmation/' + orderNumber;
		};

		window.creHandleErrors = function(responseObject) {
			errorCode = responseObject.errorCode;
			window.location = window.location.origin
					+ '/checkout/multi/payment-method/hop-error?errorCode='
					+ errorCode;
		};

		window.cancelCREPayment = function() {
			window.location = window.location.origin
					+ '/checkout/multi/delivery-method/choose';
		};

		window.whatCVV2 = function() {
			var dualScreenLeft = window.screenLeft != undefined ? window.screenLeft
					: screen.left;
			var dualScreenTop = window.screenTop != undefined ? window.screenTop
					: screen.top;

			var width = window.innerWidth ? window.innerWidth
					: document.documentElement.clientWidth ? document.documentElement.clientWidth
							: screen.width;
			var height = window.innerHeight ? window.innerHeight
					: document.documentElement.clientHeight ? document.documentElement.clientHeight
							: screen.height;

			var left = ((width / 2) - (600 / 2)) + dualScreenLeft;
			var top = ((height / 2) - (300 / 2)) + dualScreenTop;
			var newWindow = window.open(window.location.origin
					+ '/_ui/desktop/theme-greenlee/whatsthis-cvv-popup.html',
					'CVV', 'scrollbars=yes, width=' + 600 + ', height=' + 300
							+ ', top=' + top + ', left=' + left);

			// Puts focus on the newWindow
			if (window.focus) {
				newWindow.focus();
			}

			return false;

		};
	//-->
	</script>

	<input type="hidden" id="hopDebugMode"
		data-hop-debug-mode="${hopDebugMode}" />
	<spring:url value="/_ui/common/images/spinner.gif" var="spinnerUrl" />

	<!-- default payment -->

	<div class="greenlee-body">
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
								<div class="checkout-content active" id="step3">
									<div id="globalMessages">
										<common:globalMessages />
										<c:if test="${errorCode == 'A5566'}">
											<div class="col-md-10">
												<label><spring:theme
														code="checkout.multi.hostedOrderPageError.A5566" /></label>
											</div>
										</c:if>
									</div>
									<c:if test="${cartData.paymentType.code eq 'CARD'}">
										<c:choose>
											<c:when test="${not empty paymentInfos}">
												<div class="row label-bottom">
													<c:if test="${errorCode != 'A5566'}">
														<div class="col-md-6">
															<label><spring:theme
																	code="checkout.summary.paymentMethod.savedCards.selectSavedCardOrEnterNew" /></label>
														</div>

														<div class="col-md-6 checkout-address">
															<a href=""><spring:theme
																	code="checkout.summary.paymentMethod.savedCards.enterNewPaymentDetails" /></a>
														</div>
													</c:if>
												</div>
											</c:when>
											<c:otherwise>
												<div class="row label-bottom">
													<c:if test="${errorCode != 'A5566'}">
														<div class="col-md-6">
															<label><spring:theme
																	code="checkout.summary.paymentMethod.savedCards.enterNewPaymentDetails" /></label>
														</div>
													</c:if>
												</div>

											</c:otherwise>
										</c:choose>
										<%-- 		                        		<c:choose> --%>
										<%-- 										<c:when test="${not empty src}"> --%>

										<%--            							    </c:when> --%>
										<%-- 							   			<c:otherwise> --%>
										<c:if test="${not empty paymentInfos}">
											<div id="paymentmethod"
												class="clearfix owl-carousel owl-theme active address-checkout saved-cards">
												<c:forEach items="${paymentInfos}" var="paymentInfo"
													varStatus="status">
													<div class="addressEntry item">
														<div class="address-item">

															<input type="hidden" name="termsCheck" value="true" />
															<%-- 														<input type="hidden" name="selectedPaymentMethodId" 
															value="${paymentInfo.id}"/> --%>
															<c:choose>
																<c:when test="${paymentInfo.defaultPaymentInfo}">
																	<div class="checkbox checkbox-circle">
																		<input type="hidden" name="selectedPaymentMethodId"
																			value="${paymentInfo.id}" />
																		<div class="round">
																			<span class="checked"></span>
																		</div>
																		<h5>
																			<spring:theme
																				code="checkout.multi.paymentMethod.paymentDetails.cardDetails"
																				arguments="${fn:escapeXml(paymentInfo.cardType)},
																						${fn:substring(paymentInfo.cardNumber, fn:length(paymentInfo.cardNumber)-4, fn:length(paymentInfo.cardNumber))}" />
																		</h5>
																		<p>
																			<spring:theme
																				code="checkout.multi.paymentMethod.paymentDetails.expires"
																				arguments="${fn:escapeXml(paymentInfo.expiryMonth)},${fn:escapeXml(paymentInfo.expiryYear)}" />
																		</p>
																		<b2b-multi-checkout:paymentCardType
																			cardType="${paymentInfo.cardTypeData}" />
																		<c:if
																			test="${not empty paymentInfo.billingAddress.firstName}">
																			<p class="label-address">
																				<spring:theme
																					code="paymentMethod.billingAddress.header" />
																			</p>
																			<p class="pay-title">
																				<c:if
																					test="${not empty fn:escapeXml(paymentInfo.billingAddress.title)}">
																						${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.title))}&nbsp;
																						</c:if>
																				${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.firstName))}&nbsp;
																				${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.lastName))}
																			</p>
																			<address>
																				<%-- 	<c:if test="${empty fn:escapeXml(paymentInfo.billingAddress.line2)}">
																							${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.line1))}<br>
																						</c:if>
																						<c:if test="${not empty fn:escapeXml(paymentInfo.billingAddress.line2)}">
																							${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.line2))}<br>${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.line1))}<br>
																						</c:if> --%>
																				<c:if
																					test="${not empty fn:escapeXml(paymentInfo.billingAddress.line2)}">
																							${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.line2))}&nbsp;<br>
																				</c:if>
																				<c:if
																					test="${not empty fn:escapeXml(paymentInfo.billingAddress.line1)}">
																							${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.line1))}&nbsp;<br>
																				</c:if>
																				${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.town))},&nbsp;
																				${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.region.name))}&nbsp;${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.postalCode))}<br>
																				${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.country.name))}&nbsp;

																			</address>
																		</c:if>

																	</div>
																</c:when>
																<c:otherwise>
																	<div class="checkbox checkbox-circle">
																		<input type="hidden" name="selectedPaymentMethodId"
																			value="${paymentInfo.id}" />
																		<div class="round">
																			<!-- <span class="checked"></span> -->
																		</div>
																		<h5>
																			<spring:theme
																				code="checkout.multi.paymentMethod.paymentDetails.cardDetails"
																				arguments="${fn:escapeXml(paymentInfo.cardType)},${fn:substring(paymentInfo.cardNumber, 
																					fn:length(paymentInfo.cardNumber)-4, fn:length(paymentInfo.cardNumber))}" />
																		</h5>
																		<p>
																			<spring:theme
																				code="checkout.multi.paymentMethod.paymentDetails.expires"
																				arguments="${fn:escapeXml(paymentInfo.expiryMonth)},${fn:escapeXml(paymentInfo.expiryYear)}" />
																		</p>
																		<b2b-multi-checkout:paymentCardType
																			cardType="${paymentInfo.cardTypeData}" />
																		<c:if
																			test="${not empty paymentInfo.billingAddress.firstName}">
																			<p class="label-address">
																				<spring:theme
																					code="paymentMethod.billingAddress.header" />
																			</p>
																			<p class="pay-title">
																				<c:if
																					test="${not empty fn:escapeXml(paymentInfo.billingAddress.title)}">
																						${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.title))}&nbsp;
																					</c:if>
																				${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.firstName))}&nbsp;
																				${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.lastName))}
																			</p>
																			<address>
																				<%-- 	<c:if test="${empty fn:escapeXml(paymentInfo.billingAddress.line2)}">
																							${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.line1))}<br>
																						</c:if>
																						<c:if test="${not empty fn:escapeXml(paymentInfo.billingAddress.line2)}">
																							${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.line2))}<br>${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.line1))}<br>
																						</c:if> --%>
																				<c:if
																					test="${not empty fn:escapeXml(paymentInfo.billingAddress.line2)}">
																							${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.line2))}&nbsp;<br>
																				</c:if>
																				<c:if
																					test="${not empty fn:escapeXml(paymentInfo.billingAddress.line1)}">
																							${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.line1))}<br>
																				</c:if>
																				${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.town))},&nbsp;
																				${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.region.name))}&nbsp;${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.postalCode))}<br>
																				${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.country.name))}&nbsp;
																			</address>
																		</c:if>

																	</div>
																	<button class="btn-white btn-border btn-saved">
																		<spring:theme
																			code="checkout.multi.sop.useThisPaymentInfo"
																			text="Select this card" />
																	</button>
																</c:otherwise>
															</c:choose>
															<%-- 																	</form> --%>

															<%-- 																<form:form action="${request.contextPath}/checkout/multi/payment-method/remove" method="POST">
																				<input type="hidden" name="paymentInfoId" value="${paymentInfo.id}"/>
																				<button type="submit" class="negative remove-payment-item right" tabindex="${status.count + 22}">
																					<spring:theme code="checkout.multi.sop.remove" text="Remove"/>
																				</button>
																			</form:form> --%>

														</div>
													</div>
												</c:forEach>

											</div>
										</c:if>
										<!-- Payment Gateway Integration with Iframe -->

										<div
											class="item_container iframe-scroll-wrapper payment-iframe ${not empty paymentInfos ? ' address-checkout':' active'}">
											<%-- GSM-35, this has been commented.
									               <div id="address1-desc"><spring:theme code="payment.gateway.integration.iframe.addressline1" text="Billing Address Apt Or Suite No. Only"/></div>
									               <div id="address2-desc"><spring:theme code="payment.gateway.integration.iframe.addressline2" text="Billing Street Address"/></div>
									                --%>
											<c:if test="${errorCode != 'A5566'}">
												<iframe id="stPayment" src="${src}" width="100%"
													height="1025px" scrolling="yes" style="border: 0px;">
												</iframe>
											</c:if>
										</div>

										<!-- Payment Gateway Integration with Iframe -->
										<%--                      			  </c:otherwise> --%>
										<%--                             </c:choose> --%>
										<c:if test="${not empty paymentInfos}">
											<form:form action="${placeOrderUrl}" id="placeOrderForm1"
												commandName="placeOrderForm" method="GET">
												<input type="hidden" name="selectedPaymentMethodId"
													id="bindPaymentCardTypeSelect" value="" />
												<input type="hidden" name="termsCheck" value="true" />
												<div class="form-group">
													<button type="submit" class="btn btn-primary btn-order">
														<spring:theme code="checkout.summary.placeOrder" />
													</button>
												</div>
											</form:form>
										</c:if>
									</c:if>
								</div>
							</multi-checkout:checkoutProgressBar>
						</div>
					</div>
					<b2b-multi-checkout:checkoutOrderDetails cartData="${cartData}"
						showShipDeliveryEntries="true" showPickupDeliveryEntries="true"
						showTax="true" afterSimulate="true" />
				</div>
			</div>
		</div>
	</div>

	<!-- default payment -->

	<%-- 	<div id="item_container_holder">

		<div class="item_container">
			<div>
				<h3>
					<spring:theme code="checkout.multi.hostedOrderPostPage.header.wait"/>
					<img src="${commonResourcePath}/images/spinner.gif" />
				</h3>
				<hr/>
			</div>
		</div>

		<c:if test="${hopDebugMode}">
			<div class="item_container">
				<div id="debugWelcome">
					<h3>
						<spring:theme code="checkout.multi.hostedOrderPostPage.header.debug"/>
					</h3>
					<hr/>
				</div>
			</div>
		</c:if>

		<div class="item_container">
			<form:form id="hostedOrderPagePostForm" name="hostedOrderPagePostForm" action="${hostedOrderPageData.postUrl}" method="POST">
				<div id="postFormItems">
					<dl>
						<c:forEach items="${hostedOrderPageData.parameters}" var="entry" varStatus="status">
							<c:choose>
								<c:when test="${hopDebugMode}">
									<dt><label for="${entry.key}" class="required"><c:out value="${entry.key}"/></label></dt>
									<dd><input type="text" id="${entry.key}" name="${entry.key}" value="${entry.value}" tabindex="${status.count + 1}"/></dd>
								</c:when>
								<c:otherwise>
									<input type="hidden" id="${entry.key}" name="${entry.key}" value="${entry.value}" />
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</dl>
				</div>

			</form:form>
		</div>
	</div> --%>
</template:page>

<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>	

	<!-- Remove button code for reference -->
			<%-- 		<c:forEach items="${paymentInfoData}" var="paymentInfo">
						<div style="display:none">
							<div id="popup_confirm_payment_removal_${paymentInfo.id}">
								<spring:theme code="text.account.paymentDetails.delete.following" text="The following payment method will be deleted"/>	
								<br />
								<strong>
									<br>${fn:escapeXml(paymentInfo.accountHolderName)}
									<br>${fn:escapeXml(paymentInfo.cardTypeData.name)}
									<br>${fn:escapeXml(paymentInfo.cardNumber)}
									<br><c:if test="${paymentInfo.expiryMonth lt 10}">0</c:if>${fn:escapeXml(paymentInfo.expiryMonth)}&nbsp;/&nbsp;${fn:escapeXml(paymentInfo.expiryYear)}
									
									<c:if test="${paymentInfo.billingAddress ne null}">
										<br>${fn:escapeXml(paymentInfo.billingAddress.line1)}
										<br>${fn:escapeXml(paymentInfo.billingAddress.town)}&nbsp;${fn:escapeXml(paymentInfo.billingAddress.region.isocodeShort)}
										<br>${fn:escapeXml(paymentInfo.billingAddress.country.name)}&nbsp;${fn:escapeXml(paymentInfo.billingAddress.postalCode)}
									</c:if>
								</strong>
								<c:url value="/my-account/remove-payment-method" var="removePaymentActionUrl"/>
								<form:form id="removePaymentDetails${paymentInfo.id}" action="${removePaymentActionUrl}" method="post">
									<input type="hidden" name="paymentInfoId" value="${paymentInfo.id}"/>
									<br />
									<div class="container paymentsDeleteActions">
										<ycommerce:testId code="paymentDetailsDelete_delete_button" >
											<button type="submit" class="btn btn-default btn-primary col-xs-12 paymentsDeleteBtn">
												<spring:theme code="text.account.paymentDetails.delete" text="Delete"/>
											</button>
										</ycommerce:testId>
										<a class="btn btn-default closeColorBox col-xs-12 paymentsDeleteBtn" data-payment-id="${paymentInfo.id}">
											<spring:theme code="text.button.cancel" text="Cancel"/>
										</a>
									</div>
								</form:form>
							</div>
						</div>
					</c:forEach> --%>
		       		
	        		<div class="col-md-10 account-body">				
		<h1><spring:theme code="myaccount.savedcards.heading" text="saved credit cards" />&nbsp;&nbsp;<spring:theme code="myaccount.savedcards.info.title" text="saved credit cards info" /><i class="fa fa-angle-down hidden-md hidden-lg"></i></h1>
		&nbsp;
		<c:choose>
			<c:when test="${not empty paymentInfoData}">
	        	<div class="saved-cards">
	        		<ul class="address-items clearfix">
	        		<c:forEach items="${paymentInfoData}" var="paymentInfo">
	        				<li>
		        				<div class="address-item shipping-address">
			        			<form action="/checkout/multi/delivery-address/select" method="GET">
                              <h5><br>${fn:escapeXml(paymentInfo.cardTypeData.name)}&nbsp;<spring:theme code="myaccount.savedcards.ending" text="ending **** " /> ${fn:substring(paymentInfo.cardNumber, fn:length(paymentInfo.cardNumber)-4, fn:length(paymentInfo.cardNumber))}</h5>
                              <p><spring:theme code="myaccount.savedcards.expires" text="Expires on" />&nbsp;<%-- <c:if test="${paymentInfo.expiryMonth lt 10}">0</c:if> --%>
                              ${fn:escapeXml(paymentInfo.expiryMonth)}&nbsp;/&nbsp;${fn:escapeXml(paymentInfo.expiryYear)}</p>
                            <c:choose>
                            <c:when test="${not empty paymentInfo.cardTypeData and fn:containsIgnoreCase(paymentInfo.cardTypeData.name, 'Visa')}">
                            <theme:image code="img.missingProductImage.responsive.visa" alt="VISA" title="VISA"/>
                            </c:when>
                            <c:when test="${not empty paymentInfo.cardTypeData and fn:containsIgnoreCase(paymentInfo.cardTypeData.name, 'Mastercard')}">
                            <theme:image code="img.missingProductImage.responsive.mastercard" alt="Mastercard" title="Mastercard"/>
                            </c:when>
                            <c:when test="${not empty paymentInfo.cardTypeData and fn:containsIgnoreCase(paymentInfo.cardTypeData.name, 'Maestro')}">
                            <theme:image code="img.missingProductImage.responsive.maestro" alt="Maestro" title="Maestro"/>
                            </c:when>
                            <c:when test="${not empty paymentInfo.cardTypeData and fn:containsIgnoreCase(paymentInfo.cardTypeData.name, 'American Express')}">
                            <theme:image code="img.missingProductImage.responsive.americanexpress" alt="American Express" title="American Express"/>
                            </c:when>
                           	 <c:when test="${not empty paymentInfo.cardTypeData and fn:containsIgnoreCase(paymentInfo.cardTypeData.name, 'Discover')}">
								<theme:image code="img.missingProductImage.responsive.discover" alt="Discover" title="Discover"/>
							 </c:when>
                            </c:choose>
                            <c:if test="${not empty paymentInfo.billingAddress.firstName}">
                              <p class="label-address"><spring:theme code="myaccount.savedcards.address.heading" text="BILLING ADDRESS" /></p>
                              <p class="pay-title">${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.title))} ${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.firstName))}&nbsp;${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.lastName))}</p>
                           		<address>
									<c:if test="${not empty fn:escapeXml(paymentInfo.billingAddress.line1) and not empty fn:escapeXml(paymentInfo.billingAddress.line2)}">
										${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.line2))}<br>${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.line1))}<br>
									</c:if>
									<c:if test="${not empty fn:escapeXml(paymentInfo.billingAddress.line1) and empty fn:escapeXml(paymentInfo.billingAddress.line2)}">
										${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.line1))}<br>
									</c:if>
									${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.town))},&nbsp;
									${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.region.name))}&nbsp;${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.postalCode))}<br>
									${fn:toUpperCase(fn:escapeXml(paymentInfo.billingAddress.country.name))}&nbsp;
								</address>
                             </c:if>
                        </form>
			        		
								<c:url value="/my-account/remove-payment-method" var="removePaymentActionUrl"/>
								<form:form id="removePaymentDetails${paymentInfo.id}" action="${removePaymentActionUrl}" method="post">
									<input type="hidden" name="paymentInfoId" value="${paymentInfo.id}"/>
									<br />
									<div class="address-links clearfix paymentsDeleteActions">
									<ul>
									<li>
										<ycommerce:testId code="paymentDetailsDelete_delete_button" >
											<button type="submit" class="btn-trans pay-remove">
												<spring:theme code="text.account.paymentDetails.remove" text="remove"/>
											</button>
										</ycommerce:testId>
										</li>
										</ul>
									
								</form:form>
							</div>
			        				<div class="change-address default-address">
			        					<a href="#" ><c:if test="${paymentInfo.defaultPaymentInfo}" >&nbsp;<i class="fa fa-credit-card"></i><spring:theme code="text.default" text="Default Payment Method" /></c:if></a>
			        				</div>
									<div class="actions">
								<c:if test="${not paymentInfo.defaultPaymentInfo}" >
									<c:url value="/my-account/set-default-payment-details" var="setDefaultPaymentActionUrl"/>
									<form:form id="setDefaultPaymentDetails${paymentInfo.id}" action="${setDefaultPaymentActionUrl}" method="post">
										<input type="hidden" name="paymentInfoId" value="${paymentInfo.id}"/>
										<ycommerce:testId code="paymentDetails_setAsDefault_button" >
										<div class="change-address default-address">
											<button type="submit" class="btn-trans default-card">
												<i class="fa fa-credit-card"></i><spring:theme code="text.setDefault" text="Set as Default" />
											</button>
											</div>
										</ycommerce:testId>
									</form:form>
								</c:if>
							</div>
			        			</div>
	        				</li>
	        				</c:forEach>
	        			</ul>
	        			</div>
	        			</c:when>
	        			<c:otherwise>
						<p><spring:theme code="text.account.paymentDetails.noPaymentInformation" text="No Saved Card Details"/></p>
						</c:otherwise>
	        			</c:choose>
					</div>
				
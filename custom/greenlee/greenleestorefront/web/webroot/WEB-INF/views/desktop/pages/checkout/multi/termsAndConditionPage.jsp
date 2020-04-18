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


<template:page pageTitle="${pageTitle}" hideHeaderLinks="true">

	<spring:url value="/checkout/multi/terms-condition/add" var="tandCUrl" />
	<spring:url value="/_ui/common/images/spinner.gif" var="spinnerUrl" />

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
									</div>

									<form:form action="${tandCUrl}" id="greenleeTAndCForm"
										commandName="greenleeTAndCForm" method="POST">

										<div class="checkbox checkbox-group">
											<div class="form-group">
												<div class="checkbox checkbox-group terms-and-condition">
													<formElement:formCheckbox idKey="termsAndCondition"
														labelKey="checkout.termsandcondition" path="termsAndCondition"
														mandatory="true" inputCSS="terms-and-condition" tabindex="1" />
												</div>
											</div>
										</div>

										<div class="form-group">
											<c:if test="${not hasErrorInCart}">
												<button type="submit" class="btn btn-primary btn-order">
													<spring:theme
															code="checkout.multi.termsandcondition.continue.button"
															text="Next" />
												</button>
											</c:if>
										</div>
									</form:form>
								</div>
							</multi-checkout:checkoutProgressBar>
						</div>
					</div>
					<b2b-multi-checkout:checkoutOrderDetails cartData="${cartData}"
						showShipDeliveryEntries="true" showPickupDeliveryEntries="false"
						showTax="false" />
				</div>
			</div>
		</div>
	</div>

</template:page>

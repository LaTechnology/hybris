<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<spring:url value="/my-account/update-profile" var="updateProfileUrl"/>
<spring:url value="/my-account/update-password" var="updatePasswordUrl"/>
<spring:url value="/my-account/update-email" var="updateEmailUrl"/>
<spring:url value="/my-account/address-book" var="addressBookUrl"/>
<spring:url value="/my-account/payment-details" var="paymentDetailsUrl"/>
<spring:url value="/my-account/orders" var="ordersUrl"/>

<template:page pageTitle="${pageTitle}">


<div class="container">
			
			<div class="row">
				<div class="cart-switch">
					<cms:pageSlot position="BreadCrumbSlot" var="feature">
						<cms:component component="${feature}" />
					</cms:pageSlot>
				</div>
			</div>
			<div id="globalMessages">
				<common:globalMessages/>
			</div>
	</div>
	
	<div class="my-gl-account">

                        <cms:pageSlot position="ASMComponentSlot" var="feature">
                            <cms:component component="${feature}"/>
                        </cms:pageSlot>
                
		<div class="container">
			<div class="row">
				 <div class="col-md-2 sidebar">
				 		<h4 class="account-head hidden-md hidden-lg">your account<i class="fa-angle-down fa"></i></h4>
	        			<div class="account-sidebar">
	        				<div class="sidebar-body">
								<cms:pageSlot position="SideContent" var="feature" class="accountPageSideContent">
								<c:if test="${feature.uid ne 'MyCompanyNavigationComponent'}"> 
									<cms:component component="${feature}" />
								</c:if>
								<c:if test="${feature.uid eq'MyCompanyNavigationComponent'}"> 
									<sec:authorize access="hasAnyRole('ROLE_B2BADMINGROUP')">
										<cms:component component="${feature}" />
									</sec:authorize> 
								</c:if>
								</cms:pageSlot>
							</div>
						</div>
					</div>
					<div class="account-body greenlee-quote">
				<cms:pageSlot position="BodyContent" var="feature" element="div" class="accountPageBodyContent">
					<cms:component component="${feature}" />
				</cms:pageSlot>
				</div>
			
			<cms:pageSlot position="BottomContent" var="feature" element="div" class="accountPageBottomContent">
				<cms:component component="${feature}" />
			</cms:pageSlot>
		</div>
		</div>
	</div>	
</template:page>
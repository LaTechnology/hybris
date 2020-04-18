<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/desktop/address"%>

<template:page pageTitle="${pageTitle}">

	<c:url var="action" value="/login/register"/>
	<c:url var="regionsUrl" value="/login/regions"/>
	<p id="regionsUrl" style="display: none">${regionsUrl}</p>
	<div class="greenlee-account">
		<cms:pageSlot position="ASMComponentSlot" var="feature">
			<cms:component component="${feature}"/>
		</cms:pageSlot>
		<div class="container">
			<div class="row">
				<div class="col-md-12 cart-switch">
					<cms:pageSlot position="TopHeaderSlot" var="feature">
						<cms:component component="${feature}"/>
					</cms:pageSlot>
				</div>
				<cms:pageSlot position="BreadCrumbSlot" var="feature">
					<cms:component component="${feature}"/>
				</cms:pageSlot>
			</div>
			<div id="globalMessages">
				<common:globalMessages />
			</div>
			<div class="row account-wpr">
				<div class="col-md-offset-2 col-md-6">
					<div class="headline">
						<spring:theme code="register.newUser" text="New user? Register to order online"/>
					</div>

					<div class="register">
						<form:form method="post" commandName="registerForm" action="${action}" id="registerForm">
							<div class="form-adjust default-show">

								<formElement:formSelectBox
									idKey="register.title"
									labelKey="register.title"
									path="titleCode"
									mandatory="false"
									skipBlank="true"
									items="${titleData}"
									selectCSSClass="form-control custom-select"
									divCSSClass="title-select isBorder"
									/>

								<formElement:formInputBox idKey="register.firstName" labelKey="register.firstName" path="firstName" inputCSS="form-control" placeholder=""/>

								<formElement:formInputBox idKey="register.lastName" labelKey="register.lastName" path="lastName" inputCSS="form-control" placeholder=""/>

								<formElement:formInputBox idKey="register.email" labelKey="register.email" path="email" inputCSS="form-control" placeholder=""/>

								<formElement:formInputBox idKey="register.mobileNumber" labelKey="register.phoneNumber" path="mobileNumber" inputCSS="form-control" placeholder=""/>

								<template:errorSpanField path="userType">

									<label class="control-label" for="register.usertype"><spring:theme code="register.userType" text="User Type"/></label>
									<div class="controls isBorder usertype-selector">

										<select id="greenLeeRegisterUserType" name="userType" class="custom-select">
											<option value="" disabled="disabled" selected>Please Select</option>
											<c:forEach var="userTypeData" items="${userTypeData}">

												<c:choose>
													<c:when test="${userTypeSelected == userTypeData.value }">
														<option value="${userTypeData.value}" selected>${userTypeData.key}</option>
													</c:when>
													<c:otherwise>
														<option value="${userTypeData.value}">${userTypeData.key}</option>
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</select>

									</div>

								</template:errorSpanField>
							
								
								<div class="distributor-fields">
									<div class="existing-checkbox">
										<div class="checkbox checkbox-group">
										 	<formElement:formCheckbox idKey="hasExistingGreenleeAccount" labelKey="register.hasExistingGreenleeAccount" path="hasExistingGreenleeAccount" mandatory="false" inputCSS="remember" tabindex="${tabindex}"/>
										
										</div>
									</div>
									<formElement:formInputBox idKey="register.companyName" labelKey="register.companyName" path="companyName" inputCSS="form-control" placeholder=""/>
									<div class="existing-check">
										<template:errorSpanField path="accountInformation">
											<label class="control-label " for="register.accountInformation"><spring:theme code="register.accountInformation" text="ACCOUNT INFORMATION *"/></label>
											<div class="controls isBorder">
												<select name="accountInformation" class="custom-select" id="accountInformationId">
													<option value="" disabled="disabled" <c:if test="${empty selectedAccountInformation}">selected</c:if>>Please Select</option>
													<c:forEach var="accountInformationData" items="${accountInformationData}">
													
													<c:choose>
													<c:when test="${selectedAccountInformation eq accountInformationData.value}">
														<option value="${accountInformationData.value}" selected>${accountInformationData.key}</option>
													</c:when>
													<c:otherwise>
															<option value="${accountInformationData.value}">${accountInformationData.key}</option>
													</c:otherwise>
												</c:choose>
													
													

													</c:forEach>
												</select>

											</div>
										</template:errorSpanField>
										<c:choose>
										<c:when test="${not empty selectedAccountInformation}">
										<div id="accountInformationNumberId">
											<formElement:formInputBox idKey="register.accountInformationNumber" labelKey="${selectedAccountInformation}" path="accountInformationNumber" inputCSS="form-control" placeholder=""/>
										</div>
										</c:when>
										<c:otherwise>
										<div style="display: none;" id="accountInformationNumberId">
											<formElement:formInputBox idKey="register.accountInformationNumber" labelKey="" path="accountInformationNumber" inputCSS="form-control" placeholder=""/>
										</div>
										</c:otherwise>
										</c:choose>

										
									</div>

								</div>

								<div class="password-fields">

									<formElement:formPasswordBox idKey="password" labelKey="register.pwd" path="pwd" inputCSS="form-control password-strength" mandatory="true"/>

									<formElement:formPasswordBox idKey="register.checkPwd" labelKey="register.checkPwd" path="checkPwd" inputCSS="form-control" mandatory="true"/>

								</div>
							</div>
							<div class="online-fields clearfix">
								<div class="headline" id="mailling">
									<spring:theme code="register.mailingAddress" text="mailing address"/>
								</div>
								<div class="headline" id="company">
									<spring:theme code="register.companyAddress" text="company address"/>
								</div>
								<div class="row">
									<div class="col-md-12">
										<formElement:formSelectBox
											idKey="country"
											labelKey="register.country"
											path="country"
											mandatory="true"
											skipBlank="false"
											skipBlankMessageKey="form.select.empty"
											items="${countryData}"
											itemValue="isocode"
											selectedValue=""
											selectCSSClass="form-control custom-select"
											divCSSClass="isBorder"
											/>
									</div>

								
								</div>

								<div class="row">
									<div class="col-md-6">
										<formElement:formInputBox idKey="register.addressLane1" labelKey="register.addressLineOne" path="addressLane1" inputCSS="form-control" placeholder=""/>
									</div>
									<div class="col-md-6">
										<formElement:formInputBox idKey="register.addressLane2" labelKey="register.addressLineTwo" path="addressLane2" inputCSS="form-control" placeholder=""/>
									</div>
								</div>
								<div class="row">

									<div class="col-md-6">
										<formElement:formInputBox idKey="register.city" labelKey="register.city" path="city" inputCSS="form-control" placeholder=""/>

									</div>
										<div class="col-md-6">

										<div class="hide" id="regionsDiv">
										<formElement:formSelectBox
											idKey="state"
											labelKey="register.state"
											path="state"
											mandatory="true"
											skipBlank="false"
											skipBlankMessageKey="form.select.empty"
											items="${regionsForSelectedCountry}"
											itemValue="isocode"
											selectCSSClass="form-control custom-select"
											divCSSClass="isBorder"
											/>
											<input type="hidden" id="hiddenState" path="state" value="${selectedStateIsoCode}"/>
										</div>
										<div id="stateTextBox">
											<formElement:formInputBox idKey="register.state" labelKey="register.state" path="enteredState" inputCSS="form-control" placeholder=""/>
										</div>
									</div>

								</div>
								<div class="row">

									<div class="col-md-6">
										<formElement:formInputBox idKey="register.zipCode" labelKey="register.zip" path="zipCode" inputCSS="form-control" placeholder=""/>
										<%-- <input type="hidden" name="CSRFToken" value="${CSRFToken}"> --%>
									</div>

								</div>
							</div>
							
							<div class="form-group">
							<div class="checkbox checkbox-group">
						   			<formElement:formCheckbox idKey="agreeToPrivacyPolicy" labelKey="register.policy" path="agreeToPrivacyPolicy" mandatory="true" inputCSS="remember" tabindex="${tabindex}"/>
						   	</div>
						
							<div class="checkbox checkbox-group">
						   		<formElement:formCheckbox idKey="requestForInfo" labelKey="register.request.info" path="requestForInfo" mandatory="false" inputCSS="remember" tabindex="${tabindex}"/>
						   	</div>
						</div>
							
							<div class="form-actions clearfix">
								<button type="submit" class="btn btn-white"><spring:theme code="button.register" text="REGISTER"/></button>
							</div>
						</form:form>
					</div>
				</div>
				<c:url var="loginUrl" value="/login"/>
				<div class="account-sider">
					<div class="jumbotron">
						<h3>
							<spring:theme code="register.alreadyRegistered" text="Already registered"/>
						</h3>
						<p>
							<spring:theme code="register.signinText" text="Sign in here to purchase online"/>
						</p>
						<a href="${loginUrl }" class="btn-white btn-sign"><spring:theme code="register.sign" text="Sign in txt"/></a>
					</div>
				</div>
			</div>
		</div>
	</div>

</template:page>

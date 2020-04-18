
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<div class="account-body">
<div class="col-md-10">
	<h1 class="hidden-xs hidden-sm">
		<spring:theme code="text.account.profile.updatePersonalDetails"/>
	</h1>

<div class="row">
<div class="col-md-offset-1 col-md-11">
	<form:form action="update-profile" method="post" commandName="updateProfileForm">
		<div class="row">
			<div class="col-md-4">
		<formElement:formSelectBox idKey="profile.title" labelKey="profile.title" path="titleCode" mandatory="false" skipBlank="true" items="${titleData}" selectCSSClass="countrySelector form-control custom-select"
						divCSSClass="usertype-selector isBorder"/>
			</div>
		</div>
		<div class="row">
		<div class="col-md-4">
		<formElement:formInputBox idKey="profile.firstName" labelKey="profile.firstName" path="firstName" inputCSS="text" mandatory="true"/>
		</div>
		</div>
		<div class="row">
		<div class="col-md-4">
		<formElement:formInputBox idKey="profile.lastName" labelKey="profile.lastName" path="lastName" inputCSS="text" mandatory="true"/>	
		</div>
		</div>					
		<div class="row">
		<div class="col-md-4">
		<%-- <formElement:formInputBox idKey="profile.email" labelKey="profile.email" path="email" inputCSS="text" readonly="true"/>	 --%>
		<div class="form-group">
				<label class="control-label" >
				<spring:theme code="text.account.profile.email" text="Email"/>
				</label>
				<label class="control-label">${updateProfileForm.email}</label>
				<label><spring:theme code="account.profile.email.edit" text="Please contact Greenlee Web Support at webrequest@greenlee.textron.com, if you need to update your email address."/></label>
		</div>
		<spring:theme code="text.profile.email.note"/>
		<input id="profile.email" name="email" type="hidden" value="${updateProfileForm.email}">
		</div>
		</div>	
		<div class="form-group">
			
				<ycommerce:testId code="personalDetails_savePersonalDetails_button">
					<button type="submit" class="btn btn-white btn-popup">
						<spring:theme code="text.account.profile.saveUpdates" text="Save Updates"/>
					</button>
				</ycommerce:testId>
			
	
				<ycommerce:testId code="personalDetails_cancelPersonalDetails_button">
					<c:url value="profile" var="profileURL"/>
					<a href="${profileURL }"><button type="button" class="btn btn-white btn-popup btn-border last backToHome" >
						<spring:theme code="text.account.profile.cancel" text="Cancel"/>
					</button></a>
				</ycommerce:testId>
	
		</div>
	
	</form:form>
	</div>
</div>
</div>
</div>


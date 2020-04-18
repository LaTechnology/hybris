<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<div class="forgotten-password">
	<div class="description"><b><spring:theme code="forgottenPwd.description"/></b></div>
	<form:form method="post" commandName="forgottenPwdForm">
		<div class="control-group">
			<ycommerce:testId code="login_forgotPasswordEmail_input">
				<formElement:formInputBox idKey="forgottenPwd.email" labelKey="forgottenPwd.email" path="email" mandatory="true"/>
			</ycommerce:testId>
			<ycommerce:testId code="login_forgotPasswordSubmit_button">
				<button class="btn btn-primary btn-block btn-white btn-login" type="submit">
					<spring:theme code="forgottenPwd.submit"/>
				</button>
			</ycommerce:testId>
		</div>
	</form:form>
</div>

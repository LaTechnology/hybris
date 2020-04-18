<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/desktop/formElement"%>

<%-- <div class="account-section-header">
	<spring:theme code="text.account.profile.updatePasswordForm" />
</div>
<div class="account-section-content	 account-section-content-small ">
	<form:form action="${action}" method="post"
		commandName="updatePasswordForm">

		<formElement:formPasswordBox idKey="currentPassword"
			labelKey="profile.currentPassword" path="currentPassword"
			inputCSS="form-control" mandatory="true" />
		<formElement:formPasswordBox idKey="newPassword"
			labelKey="profile.newPassword" path="newPassword"
			inputCSS="form-control" mandatory="true" />
		<formElement:formPasswordBox idKey="checkNewPassword"
			labelKey="profile.checkNewPassword" path="checkNewPassword"
			inputCSS="form-control" mandatory="true" />

		<div class="container accountActions">
			<div class="col-sm-6 col-sm-push-6 accountButtons">
				<button type="submit" class="btn btn-primary btn-block">
					<spring:theme code="updatePwd.submit" text="Update Password" />
				</button>
			</div>
			<div class="col-sm-6 col-sm-pull-6 accountButtons">
				<button type="button" class="btn btn-default btn-block backToHome">
					<spring:theme code="text.button.cancel" text="Cancel" />
				</button>
			</div>
		</div>
	</form:form>
</div> --%>

<div class="greenlee-account">
	<div class="container">
		<div class="row">
			<div class="col-md-offset-2 col-md-3">
				<div class="headline">
					<spring:theme code="text.account.profile.updatePasswordForm" />
				</div>
				<div class="reset">
					<form:form action="${action}" method="post"
						commandName="updatePasswordForm">
						<div class="form-adjust">
							<div class="form-group">
								<formElement:formPasswordBox idKey="currentPassword"
									labelKey="profile.currentPassword" path="currentPassword"
									inputCSS="form-control" mandatory="true" />
							</div>
							<div class="form-group">
								<formElement:formPasswordBox idKey="newPassword"
									labelKey="profile.newPassword" path="newPassword"
									inputCSS="form-control" mandatory="true" />
							</div>
							<div class="form-group">
								<formElement:formPasswordBox idKey="checkNewPassword"
									labelKey="profile.checkNewPassword" path="checkNewPassword"
									inputCSS="form-control" mandatory="true" />
							</div>

							<div class="form-actions">
								<button type="submit" class="btn-white btn-login">
									<spring:theme code="text.account.profile.updatePasswordForm" />
								</button>
							</div>
						</div>
					</form:form>
				</div>
			</div>

		</div>
	</div>
</div>

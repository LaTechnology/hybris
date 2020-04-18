<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<div class="greenlee-account">
	<div class="container">
		<div class="row">
			<div class="col-md-offset-2 col-md-5 pwdreset-main">
				<div class="headline">
					<spring:theme code="text.account.profile.resetPassword" />
				</div>
				<div class="reset">
					<form:form method="post" commandName="updatePwdForm" id="resetForm">
						<div class="form-adjust">
							<div class="form-group">
								<formElement:formPasswordBox idKey="updatePwd.pwd"
									labelKey="updatePwd.pwd" path="pwd" inputCSS="form-control"
									mandatory="true" />
							</div>
							<div class="form-group">
								<formElement:formPasswordBox idKey="updatePwd.checkPwd"
									labelKey="updatePwd.checkPwd" path="checkPwd" mandatory="true" />
							</div>
							<c:if test="${not empty USER_ADDED_BY_ADMIN && USER_ADDED_BY_ADMIN==true}">
									<div class="checkbox checkbox-group pwdreset">
										<formElement:formCheckbox idKey="agreeToPrivacyPolicy"
											labelKey="register.policy" path="agreeToPrivacyPolicy"
											inputCSS="form-control" mandatory="true" />
									</div>
		
									<div class="checkbox checkbox-group pwdreset">
										<formElement:formCheckbox idKey="requestForInfo"
											labelKey="register.request.info" path="requestForInfo"
											inputCSS="form-control" mandatory="false" />
									</div>
							</c:if>
							<div class="form-actions">
								<button type="submit" class="btn-white btn-login">
									<spring:theme code="text.account.profile.resetPassword"
										text="Reset Password" />
								</button>
							</div>

						</div>
					</form:form>
				</div>
			</div>
		</div>
	</div>
</div>


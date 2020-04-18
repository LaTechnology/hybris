<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="actionNameKey" required="true"
	type="java.lang.String"%>
<%@ attribute name="action" required="true" type="java.lang.String"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>

<c:set var="hideDescription" value="checkout.login.loginAndCheckout" />

<%-- <div class="headline">
	<spring:theme code="login.title" />
</div>

<c:if test="${actionNameKey ne hideDescription}">
	<p>
		<spring:theme code="login.description" />
	</p>
</c:if>
 --%>


<div class="greenlee-account">

		<div class="container">
		<div id="globalMessages">
			<common:globalMessages />
		</div>
		
		<div class="row">
			<div class="cot-offset-2 col-md-3">
				<div class="headline">
					<spring:theme code="login.signIn" text="SIGN IN TO YOUR ACCOUNT" />
				</div>

				<div class="login">
					<form:form method="post" commandName="loginForm" action="${action}"
						id="loginForm">
						<c:if test="${not empty message}">
							<span class="has-error"> <spring:theme code="${message}" />
							</span>
						</c:if>
						<c:if test="${loginDisabled eq true}">
							<span class="has-error">Login disabled</span>
						</c:if>
						<div class="form-adjust default-show">

							<formElement:formInputBox idKey="login.j_username"
								labelKey="login.email" path="j_username" inputCSS="form-control"
								mandatory="true" />
							<formElement:formPasswordBox idKey="login.j_password"
								labelKey="login.password" path="j_password"
								inputCSS="form-control" mandatory="true" />

							<div class="checkbox checkbox-group">
								<div class="hidden-xs hidden-sm">
								<input id="checkbox1" class="remember" type="checkbox"
									name="_spring_security_remember_me" /> <label for="checkbox1"><spring:theme
										code="login.rememberMe" text="Remember me" /></label>
								</div>
							</div>
							<div class="form-actions">
								<button type="submit" class="btn-white btn-login">
									<spring:theme code="login.login" text="Login" />
								</button>
							</div>
							<c:url var="forgotpassword" value="/login/pw/request" />
							<div class="form-password">
								<a href="${forgotpassword}" class="js-password-forgotten"
									data-cbox-title="Forgot Your Password"> <spring:theme
										code="login.forgotPassword" text="FORGOT PASSWORD" />
								</a>
							</div>
						</div>
					</form:form>
				</div>
			</div>
			
			<c:choose>
				<c:when test="${checkoutRegistration}">
				<c:url var="registerUrl" value="registerPage" />
				</c:when>
				<c:otherwise>
					<c:url var="registerUrl" value="login/registerPage" />
				</c:otherwise>
			</c:choose>
			<%-- <c:url var="registerUrl" value="login/registerPage" /> --%>
			<div class="col-md-offset-1 col-md-5">
				<div class="jumbotron">
					<h3>
						<spring:theme code="login.newUser"
							text="New user?" />
							<span><spring:theme code="login.order.online"
							text="Register to order online" /></span>
					</h3>
					<p class="hidden-xs hidden-sm">
						<spring:theme code="login.Lorem"
							text="Register here to purchase online" />
					</p>
					<a href="${registerUrl}" class="btn-white btn-sign"> <spring:theme
							code="login.create" text="Create an account" />
					</a>
				</div>
			</div>
		</div>
	</div>
</div>


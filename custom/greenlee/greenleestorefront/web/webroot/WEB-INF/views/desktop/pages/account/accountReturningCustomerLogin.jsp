<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/desktop/user"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<c:url value="/j_spring_security_check" var="loginActionUrl" />
<div class="container">
	<div class="row">
		<div class="col-md-11">
			<cms:pageSlot position="BreadCrumbSlot" var="feature">
				<cms:component component="${feature}" />
			</cms:pageSlot>
		</div>
	</div>
	<div class="row">
					<div class="col-md-12 cart-switch">
						<cms:pageSlot position="TopHeaderSlot" var="feature">
							<cms:component component="${feature}" />
						</cms:pageSlot>
					</div>
				</div>
</div>
<div class="login-section">
	<user:login actionNameKey="login.login" action="${loginActionUrl}" />
</div>

<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="account-body">
<div class="col-md-10">
	<h1 class="book-heading hidden-xs hidden-sm">
		<spring:theme code="text.account.profile" text="Personal Information" />
	</h1>
	<div class="row">
		<div class="col-md-offset-1 col-md-5 personal-info">
			<table class="account-profile-data user-info">
			<c:if test="${not empty fn:escapeXml(title.name)}">
				<tr>
					<td><label><spring:theme code="profile.title" text="Title" />:</label></td>
					<td class="end">${fn:escapeXml(title.name)}</td>
				</tr>
			</c:if>
				<tr>
					<td><label><spring:theme code="profile.firstName" text="First name" />:</label>
					</td>
					<td class="end">${fn:escapeXml(customerData.firstName)}</td>
				</tr>
				<tr>
					<td><label><spring:theme code="profile.lastName" text="Last name" />:</label>
					</td>
					<td class="end">${fn:escapeXml(customerData.lastName)}</td>
				</tr>
				<tr>
					<td><label><spring:theme code="profile.email" text="E-mail" />:</label></td>
					<td class="end">${fn:escapeXml(customerData.email)}</td>
				</tr>
				<tr>
					<td><label><spring:theme code="profile.password" text="password" />:</label></td>
					<td class="end"><span class="pwd-reset">*******</span>(<a href="update-password" class="text-underline"><spring:theme
					code="text.account.profile.changePassword" text="Change password" /></a>)</td>
				</tr>
			</table>
			<%-- <a class="button" href="update-password"><spring:theme
					code="text.account.profile.changePassword" text="Change password" /></a> --%>
			<a class="btn btn-white btn-edit" href="update-profile"><spring:theme
					code="text.account.profile.updatePersonalDetails.edit"
					text="Edit" /></a>
					<%--  <a class="button"
				href="update-email"><spring:theme
					code="text.account.profile.updateEmail" text="Update email" /></a> --%>
		</div>
	</div>
</div>
</div>
<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags"%>

<c:if test="${(not empty accConfMsgs) || (not empty accInfoMsgs) || (not empty accErrorMsgs)}">
	<div class="global-alerts">
		
		<%-- Information (confirmation) messages --%>
		<c:if test="${not empty accConfMsgs}">
			<c:forEach items="${accConfMsgs}" var="msg">
				<div class="alert alert-success alert-dismissable ${msg.code}">
					<spring:theme code="${msg.code}" arguments="${msg.attributes}"/>
					<i class="gl gl-remove"></i>
				</div>
			</c:forEach>
		</c:if>

		<%-- Warning messages --%>
		<c:if test="${not empty accInfoMsgs}">
			<c:forEach items="${accInfoMsgs}" var="msg">
				<div class="alert alert-info alert-dismissable ${msg.code == 'registration.confirmation.message.title' ? 'text-center':''}">
					<i class="fa fa-exclamation-triangle fa-m"></i>
						<spring:theme code="${msg.code}" arguments="${msg.attributes}"/>
						<i class="gl gl-remove"></i>
				</div>
			</c:forEach>
		</c:if>

		<%-- Error messages (includes spring validation messages)--%>
		<c:if test="${not empty accErrorMsgs}">
			<c:forEach items="${accErrorMsgs}" var="msg">
				<div class="alert alert-danger alert-dismissable">
					<spring:theme code="${msg.code}" arguments="${msg.attributes}"/>
					<i class="gl gl-remove"></i>
				</div>
			</c:forEach>
		</c:if>
		<spring:theme code="${msg.code}" arguments="${msg.attributes}"/>
		<%-- Thanks messages --%>
		

	</div>
</c:if>
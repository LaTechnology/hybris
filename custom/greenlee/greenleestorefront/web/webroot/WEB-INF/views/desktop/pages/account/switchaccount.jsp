<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="gl" uri="http://hybris.com/tld/greenlee" %>

<c:url value="/switch-account/setDefaultAccount" var="encodedUrl" />
<sec:authorize ifNotGranted="ROLE_ANONYMOUS" var="loggedIn"/>
<c:if test="${loggedIn}">
<c:if test="${fn:length(switchableB2BUnits) ge 2}">
	<div class="col-as-3 account-switch ">
		<div class="dropdown">
			<div class="dd-head">
				<c:if test="${not empty sessionB2BUnit}">
					<spring:theme code="${sessionB2BUnit.name}" />
					<span> (<gl:b2bunitid id="${sessionB2BUnit.uid}" />)
					</span>
					<i class="fa fa-angle-down"></i>
				</c:if>
			</div>
			<div class="dd-body" id="switch-user">
				<c:if test="${fn:length(switchableB2BUnits) ge limitToShowSearchbox}">
					<div class="dd-search">
						<input class="search" placeholder="Search" type="text">
						<button type="submit" class="switchSubmit">
							<i class="fa fa-search fa-m"></i>
						</button>
					</div>
				</c:if>
				<form:form action="${encodedUrl}" method="POST">
					<input type="hidden" name="user" value="${user.uid}" />
					<input type="hidden" name="requestURI"
						value="${requestScope['javax.servlet.forward.servlet_path']}" />
					<input type="hidden" name="redirectTo"
						value="${param['redirectTo']}" />
					<ul class="list">
						<c:forEach items="${switchableB2BUnits}" var="b2bUnit">
							<c:choose>
								<c:when test="${b2bUnit.uid==sessionB2BUnit.uid}">
									<li class="active"><div class="list-details">
											<div class="name">
												<spring:theme code="${b2bUnit.name}" />
											</div>
											<c:if test="${not empty switchAccountDisplayAddress}">
												<address>
													<spring:theme
														code="${b2bUnit.contactAddress.formattedAddress}" />
												</address>
											</c:if>
										</div>
										<div class="list-code">
                                            <label for="unit_${b2bUnit.uid}"><gl:b2bunitid id="${b2bUnit.uid}"/></label>
										</div> <i class="fa fa-check"></i></li>
								</c:when>
								<c:otherwise>
								</c:otherwise>
							</c:choose>
						</c:forEach>
						<c:forEach items="${switchableB2BUnits}" var="b2bUnit">
							<c:choose>
								<c:when test="${b2bUnit.uid==sessionB2BUnit.uid}">
								</c:when>
								<c:otherwise>
									<li><div class="list-details">
											<div class="name">
												<spring:theme code="${b2bUnit.name}" />
											</div>
											<c:if test="${not empty switchAccountDisplayAddress}">
												<address>
													<spring:theme
														code="${b2bUnit.contactAddress.formattedAddress}" />
												</address>
											</c:if>
										</div>
										<div class="list-code">
											<label for="unit_${b2bUnit.uid}"><gl:b2bunitid id="${b2bUnit.uid}"/></label>
											<input type="hidden" name="unitId" value="${b2bUnit.uid}" />
										</div></li>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</ul>
					<input type="hidden" name="unit" id="unit" value="" />
					<input type="hidden" name="selectedUnit" value="${selectedUnit}"
						id="selectedUnit" />
					<input type="hidden" name="uid" value="${sessionB2BUnit.uid }"
						id="uid">
				</form:form>
			</div>
		</div>
	</div>
</c:if>
</c:if>
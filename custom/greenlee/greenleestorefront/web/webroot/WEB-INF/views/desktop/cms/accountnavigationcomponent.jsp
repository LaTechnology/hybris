<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%-- <c:if test="${navigationNode.visible}">
	<div class="accountNav">
		<c:if test="${empty navigationNode.title}">
			<div class="headline">${navigationNode.title}</div>
		</c:if>
		<c:if test="${navigationNode.title eq 'ORDERS'}">
			<div class="headline">
				<h4>
					<i class="fa fa-list"></i>${navigationNode.title}</h4>
			</div>
		</c:if>
		<c:if test="${navigationNode.title eq 'QUOTES'}">
			<div class="headline">
				<h4>
					<i class="fa fa-file-o"></i>${navigationNode.title}</h4>
			</div>
		</c:if>
		<c:if test="${navigationNode.title eq 'PROFILE'}">
			<div class="headline">
				<h4>
					<i class="fa fa-user"></i>${navigationNode.title}</h4>
			</div>
		</c:if>
		<c:if test="${navigationNode.title eq 'MYCOMPANY'}">
			<div class="headline">
				<h4>
					<i class="fa fa-user"></i>${navigationNode.title}</h4>
			</div>
		</c:if>
		<c:forEach items="${navigationNode.links}" var="link">
			<c:choose>
				<c:when test="${navigationNode.uid eq 'AccountLeftNavNode'}">
					<h3 class="active">
						<div class="sidebar-youraccount">
							<c:set
								value="${ requestScope['javax.servlet.forward.servlet_path'] == link.url ? 'active':'' }"
								var="selected" />
							<cms:component component="${link}" evaluateRestriction="true"
								element="li" class=" ${selected}" />
						</div>
					</h3>
				</c:when>

				<c:otherwise>
					<div class="side-header">
						<ul>
							<li><c:set
									value="${ requestScope['javax.servlet.forward.servlet_path'] == link.url ? 'active':'' }"
									var="selected" /> <cms:component component="${link}"
									evaluateRestriction="true" element="li" class=" ${selected}" />
							</li>
						</ul>
					</div>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</div>
</c:if> --%>

<c:if test="${navigationNode.visible}">
	<c:choose>
		<c:when test="${navigationNode.title eq 'My Account'}">
			<c:forEach items="${navigationNode.links}" var="link">
				<c:set value="${ requestScope['javax.servlet.forward.servlet_path'] eq link.url ? 'active':'' }" var="selected" />
				<h3 class="${selected}">
					<span class="sidebar-youraccount"> 
					<cms:component component="${link}" evaluateRestriction="true" />
					</span>
				</h3>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<div class="side-header">
				<c:if test="${navigationNode.title eq 'Orders'}">
					<h4>
						<i class="fa fa-list"></i>${navigationNode.title}</h4>
				</c:if>
				<c:if test="${navigationNode.title eq 'Profile'}">
					<h4>
						<i class="fa fa-user"></i>${navigationNode.title}</h4>
				</c:if>
				<sec:authorize access="hasAnyRole('ROLE_B2BADMINGROUP')">
					<c:if test="${navigationNode.title eq 'MY COMPANY'}">
						<c:if test = "${not fn:contains(user.sessionB2BUnit.uid,'B2ECUST')}" > 
							<h4>
								<i class="fa fa-user"></i>${navigationNode.title}</h4>
						</c:if>
					</c:if>
				</sec:authorize>
				<ul>
					<c:forEach items="${navigationNode.links}" var="link">
						
						<c:if test ="${fn:contains(link.url,'/my-account/address-book')}">
						<c:choose>
						<c:when test ="${fn:contains(requestScope['javax.servlet.forward.servlet_path'],'address')}">
						<cms:component component="${link}" evaluateRestriction="true" element="li" class="active" />	
						</c:when> 
						<c:otherwise>
						<cms:component component="${link}" evaluateRestriction="true" element="li" class="" />	
						</c:otherwise>
						</c:choose>
						
						</c:if>
						
					  <c:if test ="${link.uid ne 'CompanyUsersLink' and link.uid ne 'AccountAddressBookLink'}">  
						<c:if test ="${user.sessionB2BUnit.userType eq 'B2C' || (link.uid ne 'AccountManagePaymentDetailsLink' && user.sessionB2BUnit.userType ne 'B2C')}">
						<c:set value="${ requestScope['javax.servlet.forward.servlet_path'] == link.url ? 'active':'' }" var="selected" />
						<cms:component component="${link}" evaluateRestriction="true" element="li" class=" ${selected}" />
						</c:if>
					 </c:if> 
					 <sec:authorize access="hasAnyRole('ROLE_B2BADMINGROUP')"> 
		 			 <c:if test ="${link.uid eq 'CompanyUsersLink'}">
			 			  <c:if test = "${not fn:contains(user.sessionB2BUnit.uid,'B2ECUST')}" > 
							 <c:set value="${fn:contains(requestScope['javax.servlet.forward.servlet_path'], link.url) ? 'active':'' }" var="selected" />
							<cms:component component="${link}" evaluateRestriction="true" element="li" class=" ${selected}" />
						 </c:if> 
					  </c:if>
					 </sec:authorize>
					</c:forEach>
				</ul>
			
			</div>
			</c:otherwise>
		
	</c:choose>
</c:if>
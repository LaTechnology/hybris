<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<%-- <c:if test="${component.visible}">
	<div class="container">
		<nav class="main-navigation js-enquire-offcanvas-navigation"
			role="navigation">
			<ul class="nav nav-pills">
				<li class="hidden-md hidden-lg">
					<a class="sm-back js-toggle-sm-navigation" href="#">Back</a>
				</li>
				<c:forEach items="${components}" var="component">
					<c:if test="${component.navigationNode.visible}">
						<cms:component component="${component}" evaluateRestriction="true"
							navigationType="offcanvas" />
					</c:if>
				</c:forEach>
			</ul>
		</nav>
	</div>
</c:if> --%>


<c:if test="${component.visible}">
	<nav class="pri-nav">
		<ul class="main-menu">
			<%-- <c:forEach items="${components}" var="component">
					<c:if test="${component.navigationNode.visible}">
						<cms:component component="${component}" evaluateRestriction="true"
							navigationType="offcanvas" />
					</c:if>
				</c:forEach> --%>
			<c:forEach items="${components}" var="component" varStatus="status">
				<c:if test="${component.navigationNode.visible}">
					<%-- <cms:component component="${component}"
									evaluateRestriction="true" navigationType="offcanvas" /> --%>

					<li class="menuitem ${status.first || status.last ? '' : 'mobile-hide'} ${status.first ? 'first' : ''}">
					<c:url value="${component.link.url}" var="urlLink"/>
							<a title="${component.link.linkName}" href="${status.first ? 'javascript:void(0);' : urlLink}">${component.link.linkName}
											<c:if test="${status.first}">
												<i class="fa fa-chevron-down"></i>
											</c:if>
										</a>
						<c:if test="${status.first}">

						<div class="sub-menu">
							<div class="sub-menu-wrp">
								<div class="container homepage-bottom-header-slot">
									<cms:pageSlot position="BottomHeaderSlot" var="component1"
										element="div" class="row">
										<cms:component component="${component1}" element="div"
											class="col-md-4 col-sm-4 col-xs-4  ${isFirstElement ? 'layout-img1': ''} ${not isLastElement and not isFirstElement ? 'layout-img2': ''} ${isLastElement ? 'layout-img3': ''}" />
									</cms:pageSlot>

								</div>
							</div>
						</div>
						</c:if>
					</li>

				</c:if>
			</c:forEach>
		</ul>
	</nav>
</c:if>


<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="hideHeaderLinks" required="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>

	<header>		<!-- header -->
    <div class="navbar navbar-default navbar-fixed-top navbar-burger">
      <div class="container">
		  <c:if test="${not hideHeaderLinks}">
        <button type="button" class="navbar-toggle visible-xs-inline-block visible-sm-inline-block" data-toggle="collapse" data-target=".navbar-collapse">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar bar1"></span>
        <span class="icon-bar bar2"></span>
        <span class="icon-bar bar3"></span>
        </button>
	</c:if>
        <cms:pageSlot position="SiteLogo" var="logo" limit="1" element="div" class="sitelogo ${hideHeaderLinks ? 'static-logo':''}">
			<cms:component component="${logo}" />
		</cms:pageSlot>

		<c:if test="${not hideHeaderLinks}">


       	 <nav:topNavigation />
        <div class="msitesearch">
          <a href="#"><i class="fa fa-search fa-m"></i></a>
        </div>
        <!-- <div class="siteSearch">
          <form action="en/search/" method="get" name="search_form_SearchBox">
            <div class="control-group">
              <label for="input_SearchBox" class="control-label skip hidden">text.search</label>
              <div class="controls">
                <input type="text" data-options="{&quot;autocompleteUrl&quot; : &quot;/mamasnpapasstorefront/mamasnpapas-uk/en/search/autocomplete/SearchBox&quot;,&quot;minCharactersBeforeRequest&quot; : &quot;3&quot;,&quot;waitTimeBeforeRequest&quot; : &quot;500&quot;,&quot;displayProductImages&quot; : true}" placeholder="search" maxlength="100" value="" name="text" class="siteSearchInput " id="input_SearchBox" autocomplete="off" role="textbox" aria-autocomplete="list" aria-haspopup="true">
                <button type="submit" class="siteSearchSubmit"><i class="fa fa-search fa-m"></i></button>
              </div>
            </div>
          </form>
        </div> -->
        <cms:pageSlot position="SearchBox" var="component">
        	<cms:component component="${component}" />
        </cms:pageSlot>
        <nav class="sec-nav">
          <ul>
            <li class="user-menu">
            	<%-- <sec:authorize ifNotGranted="ROLE_ANONYMOUS">
								<c:set var="maxNumberChars" value="25" />
								<c:if test="${fn:length(user.firstName) gt maxNumberChars}">
									<c:set target="${user}" property="firstName"
										value="${fn:substring(user.firstName, 0, maxNumberChars)}..." />
								</c:if>


									<ycommerce:testId code="header_LoggedUser">
										<spring:theme code="header.welcome" arguments="${user.firstName},${user.lastName}" htmlEscape="true" />
									</ycommerce:testId>

							</sec:authorize> --%>
            <!-- <a href="" title=""><i class="fa fa-user fa-m"></i><span class="hidden-xs hidden-sm">sign in / Register</span></a> -->
            <%-- <sec:authorize ifAnyGranted="ROLE_ANONYMOUS">

									<ycommerce:testId code="header_Login_link">
										<a href="<c:url value="/login"/>">
											<i class="fa fa-user fa-m"></i><span class="hidden-xs hidden-sm"><spring:theme code="header.link.login" /></span></a>
										</a>
									</ycommerce:testId>

							</sec:authorize> --%>
				<sec:authorize ifAnyGranted="ROLE_ANONYMOUS">
					<ycommerce:testId code="header_Login_link"><div class="profile-menu"><a href="<c:url value="/login"/>"><i class="fa fa-user fa-m"></i><span class="hidden-xs hidden-sm"><spring:theme code="header.link.login.text"/></span></a></div></ycommerce:testId>
				</sec:authorize>
				<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
				<ycommerce:testId code="header_myAccount">
				<div class="profile-menu logged"> <a href="#" class="logged-in"><i class="fa fa-user fa-m"></i><span class="hidden-xs hidden-sm"><spring:theme code="account.hello"/>${user.name}</span><i class="fa fa-angle-down fa-m hidden-xs hidden-sm"></i><i class="fa fa-angle-up fa-m hidden-xs hidden-sm"></i></a>
					<ul>
					<li class="${myAccountLink}"><a href="<c:url value="/my-account"/>"><spring:theme code="account.youraccount"/></a></li>
                	<li class="${ordersLink}"><a href="<c:url value="/my-account/orders"/>"><spring:theme code="account.yourorders"/></a></li>
                	<li class="${profileLink}"><a href="<c:url value="/my-account/profile"/>"><spring:theme code="account.personalinformation"/></a></li>
                	<li class="${addressBookLink}"><a href="<c:url value="/my-account/address-book"/>"><spring:theme code="account.addressbook"/></a></li>
                	<c:if test="${empty user.sessionB2BUnit || user.sessionB2BUnit.userType eq 'B2C'}">
                		<li class="${paymentLink}"><a href="<c:url value="/my-account/payment-details"/>"><spring:theme code="account.savedcreditcards"/></a></li>
                	</c:if>
                	<li class="${logoutLink}"><a href="<c:url value="/logout"/>"><spring:theme code="account.signout"/></a></li>
					</ul>
					</div>
					</ycommerce:testId>
				</sec:authorize>
            </li>
            <li class="cart-menu">
          <!--   <a href="" title=""><i class="fa fa-shopping-cart fa-m"></i></a> -->
            	 <cms:pageSlot position="MiniCart" var="cart" limit="1">
                        <cms:component component="${cart}" />
                    </cms:pageSlot>
            </li>
          </ul>
        </nav>
        </c:if>
        <c:if test="${hideHeaderLinks}">
        <div class="pull-right secure">
					<span>
			        	<spring:theme code="header.securetransaction"/>
					</span>
				</div>
        </c:if>
      </div>
    </div>
    </header>

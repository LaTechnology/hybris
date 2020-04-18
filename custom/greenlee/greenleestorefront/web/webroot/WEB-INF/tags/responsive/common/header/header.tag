
<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="hideHeaderLinks" required="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>

	<header>		<!-- header -->
    <div class="navbar navbar-default navbar-fixed-top navbar-burger">
      <div class="container">
        <button type="button" class="navbar-toggle visible-xs-inline-block visible-sm-inline-block" data-toggle="collapse" data-target=".navbar-collapse">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar bar1"></span>
        <span class="icon-bar bar2"></span>
        <span class="icon-bar bar3"></span>
        </button>
        <cms:pageSlot position="SiteLogo" var="logo" limit="1" element="div" class="sitelogo">
			<cms:component component="${logo}" />
		</cms:pageSlot>
       	 <nav:topNavigation />
        <div class="msitesearch">
          <i class="fa fa-search fa-m"></i>
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
					<ycommerce:testId code="header_Login_link"><a href="<c:url value="/login"/>"><spring:theme code="header.link.login.text"/></a></ycommerce:testId>
				</sec:authorize>
				<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
				<ycommerce:testId code="header_myAccount"><a href="<c:url value="/my-account"/>"><spring:theme code="header.link.account"/></a></ycommerce:testId>
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
      </div>
    </div>
    </header>
 
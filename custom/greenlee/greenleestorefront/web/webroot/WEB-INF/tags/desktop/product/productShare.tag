<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<!-- Lockerz Share BEGIN -->
<%-- <div class="a2a_kit a2a_default_style LoveShare span-10 last clearfix">
	<a class="a2a_dd share" href="https://www.addtoany.com/share_save" title="<spring:theme code="product.share.viewMoreServices"/>" >
		<spring:theme code="product.share.share" text="Share" />
	</a>
	<a class="a2a_button_linkedin"></a><a class="a2a_button_twitter"></a><a class="a2a_button_facebook"></a>
</div> --%>
<c:set var="test" scope="session" value="false"/>
<sec:authorize ifNotGranted="ROLE_ANONYMOUS" var="loggedIn"/>
<c:if test="${loggedIn}">
	<c:if test="${empty user.sessionB2BUnit or user.sessionB2BUnit.userType eq 'B2B'}">
		<c:set var="switchFlag" scope="session" value="true"/>
	</c:if>
</c:if>

<script type="text/javascript"
	src="https://static.addtoany.com/menu/page.js"></script>
<!-- Lockerz Share END -->

<c:url value="${product.url}/shareProductPopup" var="getShareProductPopup"/>
<div class="a2a_kit a2a_default_style LoveShare pdp-social ${switchFlag ? 'has-switch' : 'no-switch'}">
	<ul>
		<li><a href="${getShareProductPopup }" class="js-email"
			data-cbox-title="Share with friends"><i class="fa fa-envelope"></i></a></li>
		<li class="social-share"><i class="fa fa-share-alt"></i>
			<div class="tooltip">
				<a class="a2a_button_linkedin"><i class="fa fa-linkedin"></i></a>
				<a class="a2a_button_twitter"><i class="fa fa-twitter"></i></a>
				<a class="a2a_button_facebook"><i class="fa fa-facebook"></i></a>
			</div>
	</ul>
</div>

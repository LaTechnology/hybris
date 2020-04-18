<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="false" rtexprvalue="true" %>
<%@ attribute name="metaDescription" required="false" %>
<%@ attribute name="metaKeywords" required="false" %>
<%@ attribute name="pageCss" required="false" fragment="true" %>
<%@ attribute name="pageScripts" required="false" fragment="true" %>

<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="analytics" tagdir="/WEB-INF/tags/shared/analytics" %>
<%@ taglib prefix="generatedVariables" tagdir="/WEB-INF/tags/shared/variables" %>
<%@ taglib prefix="debug" tagdir="/WEB-INF/tags/shared/debug" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="htmlmeta" uri="http://hybris.com/tld/htmlmeta"%>

<!DOCTYPE html>
<html lang="${currentLanguage.isocode}">
    <head>
		<!-- Google Tag Manager -->
		<script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push(
		{'gtm.start': new Date().getTime(),event:'gtm.js'}
		);var f=d.getElementsByTagName(s)[0],
		j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
		'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
		})(window,document,'script','dataLayer','GTM-5Q3NPKJ');</script>
		<!-- End Google Tag Manager -->
		
        <title>
            ${not empty pageTitle ? pageTitle : not empty cmsPage.title ? cmsPage.title : 'Accelerator Title'}
        </title>

        <%-- Meta Content --%>
        <meta charset="utf-8">
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

			<meta content='IE=edge,chrome=1' http-equiv='X-UA-Compatible'>
			<meta content='on' http-equiv='cleartype'>


        <!-- <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"> -->

        <%-- Additional meta tags --%>
        <htmlmeta:meta items="${metatags}"/>

        <%-- Favourite Icon --%>
        <spring:theme code="img.favIcon" text="/" var="favIconPath"/>
        <link rel="shortcut icon" type="image/x-icon" media="all" href="${favIconPath}"/>

        <%-- CSS Files Are Loaded First as they can be downloaded in parallel --%>
        <template:styleSheets/>

        <%-- Inject any additional CSS required by the page --%>
        <jsp:invoke fragment="pageCss"/>
        <analytics:analytics/>
        <generatedVariables:generatedVariables/>
         <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
        <script async="" type="text/javascript" src="${commonResourcePath}/js/picturefill.min.js"></script>
        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
          <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
          <!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
            <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
          <![endif]-->
    </head>

    <body class="${pageBodyCssClasses} ${cmsPageRequestContextData.liveEdit ? ' yCmsLiveEdit' : ''} language-${currentLanguage.isocode}">

		<!-- Google Tag Manager (noscript) -->
		<noscript><iframe src="https://www.googletagmanager.com/ns.html?id=GTM-5Q3NPKJ"
		height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
		<!-- End Google Tag Manager (noscript) -->

        <%-- Inject the page body here --%>
        <jsp:doBody/>

        <form name="accessiblityForm">
            <input type="hidden" id="accesibility_refreshScreenReaderBufferField" name="accesibility_refreshScreenReaderBufferField" value=""/>
        </form>
        <div id="ariaStatusMsg" class="skip" role="status" aria-relevant="text" aria-live="polite"></div>

        <%-- Load JavaScript required by the site --%>
        <template:javaScript/>

        <%-- Inject any additional JavaScript required by the page --%>
        <jsp:invoke fragment="pageScripts"/>

    </body>

    <debug:debugFooter/>

</html>

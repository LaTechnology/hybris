<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>



<%--  AddOn Common CSS files --%>
<%-- <c:forEach items="${addOnCommonCssPaths}" var="addOnCommonCss">
	<link rel="stylesheet" type="text/css" media="all" href="${addOnCommonCss}"/>
</c:forEach> --%>





<%-- Theme CSS files --%>
<link rel="stylesheet" type="text/css" media="all" href="${themeResourcePath}/css/style.css"/>
<link rel='stylesheet' id='greenlee-print-css'  href='${themeResourcePath}/css/print.css' type='text/css' media='print' />
<%--  AddOn Theme CSS files --%>
<%-- <c:forEach items="${addOnThemeCssPaths}" var="addOnThemeCss">
	<link rel="stylesheet" type="text/css" media="all" href="${addOnThemeCss}"/>
</c:forEach> --%>

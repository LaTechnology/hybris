<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url value="${not empty page ? page.label : urlLink}" var="encodedUrl"/>
<div class="disp-img simple-banner">
    <c:choose>
        <c:when test="${empty encodedUrl || encodedUrl eq '#'}">
            <!-- <div class="title"> -->
            <c:if test="${not empty headline}">
                <h2>${headline}</h2>
            </c:if>
            <!-- </div> -->
            <c:if test="${not empty media}">
                <div class="thumb">

                    <img title="${headline}" alt="${media.altText}" src="${media.url}"></div>
                </c:if>
                <div class="details">
                    <c:if test="${not empty content}">
                        ${content}
												<i class="gl gl-down"></i>
                    </c:if>


                </div>
                <!-- 			<div class="action"> -->
                <%-- 				<theme:image code="img.iconArrowCategoryTile" alt="${media.altText}"/> --%>
                <!-- 			</div> -->
            </c:when>
            <c:otherwise>
                <a href="${encodedUrl}">
                    <c:if test="${not empty headline}">
                        <div class="title">
                            <h3>${headline}</h3>
                        </div>
                    </c:if>
                    <c:if test="${not empty media}">
                        <div class="thumb">
                            <img title="${headline}" alt="${media.altText}" src="${media.url}"></div>
                        </c:if>
                        <c:if test="${not empty content}">
                            <div class="details">
                                ${content}
																	<i class="gl gl-down"></i>
                            </div>

                        </c:if>
                        <%-- <span class="action">
					<theme:image code="img.iconArrowCategoryTile" alt="${media.altText}"/>
				</span> --%>

                    </a>
                </c:otherwise>
            </c:choose>
        </div>

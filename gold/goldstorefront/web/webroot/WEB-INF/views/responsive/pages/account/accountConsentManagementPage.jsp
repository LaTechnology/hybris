<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<spring:htmlEscape defaultHtmlEscape="true"/>

<div class="account-section-header consent-section-header">
    <div class="row">
        <div class="container-lg col-md-6">
            <spring:theme code="text.account.consent.consentManagement"/>
        </div>
    </div>
</div>
<div class="row">
    <div class="container-lg col-md-6">
        <div class="account-section-content">
            <div class="account-section-form consent-section-form">
                <div id="consent-management-form" data-consent-management-url="${consentManagementUrl}">
                    <c:if test="${not empty consentTemplateDataList}">
                        <ul class="account-section-form__toggle-list">
                            <c:forEach items="${consentTemplateDataList}" var="consentTemplateData">
                                <li>
                                    <c:set var="consentTemplateId" value="${fn:escapeXml(consentTemplateData.id)}"/>

                                    <label for="${consentTemplateId}" class="control-label">${fn:escapeXml(consentTemplateData.name)}</label>
                                    <label class="toggle-switch">
                                        <c:choose>
                                            <c:when test="${not empty consentTemplateData.consentData && empty consentTemplateData.consentData.consentWithdrawnDate}">
                                                <input class="toggle-switch__input" id="${consentTemplateId}"
                                                       type="checkbox" checked/>
                                                <span class="toggle-switch__slider toggle-switch__slider--round"></span>
                                            </c:when>
                                            <c:otherwise>
                                                <input class="toggle-switch__input" id="${consentTemplateId}"
                                                       type="checkbox">
                                                <span class="toggle-switch__slider toggle-switch__slider--round"></span>
                                            </c:otherwise>
                                        </c:choose>
                                    </label>

                                    <spring:url value="/my-account/consents/give/{/consentTemplateId}/{/version}"
                                                var="giveConsentUrl" htmlEscape="false">
                                        <spring:param name="consentTemplateId" value="${consentTemplateData.id}"/>
                                        <spring:param name="version" value="${consentTemplateData.version}"/>
                                    </spring:url>
                                    <form:form action="${giveConsentUrl}" method="POST">
                                        <button hidden type="submit" id="give-consent-button-${consentTemplateId}"></button>
                                    </form:form>

                                    <spring:url value="/my-account/consents/withdraw/{/consentCode}" var="withdrawConsentUrl"
                                                htmlEscape="false">
                                        <spring:param name="consentCode" value="${consentTemplateData.consentData.code}"/>
                                    </spring:url>
                                    <form:form action="${withdrawConsentUrl}" method="POST">
                                        <button hidden type="submit" id="withdraw-consent-button-${consentTemplateId}"></button>
                                    </form:form>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>

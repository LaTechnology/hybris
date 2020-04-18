<%-- <%@ tag body-content="empty" trimDirectiveWhitespaces="true" %> --%>
<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true"%>
<%@ attribute name="steps" required="true" type="java.util.List" %>
<%@ attribute name="progressBarId" required="true" type="java.lang.String" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


    <c:forEach items="${steps}" var="checkoutStep" varStatus="status">
        <c:url value="${checkoutStep.url}" var="stepUrl"/>
        <c:choose>
            <c:when test="${progressBarId eq checkoutStep.progressBarId}">
                <c:set scope="page" var="currentStepActive" value="${checkoutStep.stepNumber}"/>
                <div class="accordion-step">
                    <a href="${stepUrl}">
                        <div class="checkout-step clearfix active" data-rel="step${status.index}">

                            <div class="pull-left">
                                <div class="step-num">
                                    <span>${status.index + 1}</span>
                                </div>
                                <div class="title">
                                    <h3><spring:theme code="checkout.multi.${checkoutStep.progressBarId}"/></h3>
                                </div>
                            </div>

                        </div>
                    </a>
                    <jsp:doBody/>
                </div>
            </c:when>
            <c:when test="${checkoutStep.stepNumber > currentStepActive }">
                <div class="accordion-step">
                    <%-- <a href="${stepUrl}"> --%>
                        <div class="checkout-step" data-rel="step${status.index}">
                            <div class="step-num">
                                <span>${status.index + 1}</span>
                            </div>
                            <div class="title">
                                <h3><spring:theme code="checkout.multi.${checkoutStep.progressBarId}"/></h3>
                            </div>
                        </div>
                    <!-- </a> -->
                </div>
            </c:when>
            <c:otherwise>
                <div class="accordion-step">
                    <div class="checkout-step clearfix success" data-rel="step${status.index}">
                        <a href="${stepUrl}">
                            <div class="pull-left">
                                <div class="step-num">
                                    <span>
                                        <i class="fa fa-check"></i>
                                    </span>
                                </div>
                                <div class="title">
                                    <h3><spring:theme code="checkout.multi.${checkoutStep.progressBarId}"/></h3>
                                </div>
                            </div>
                        </a>
						<c:if test="${status.index eq 0}">
                            <div class="step-right">
                                <c:choose>
                                    <c:when test="${not empty cartData.deliveryAddress.firstName && not empty cartData.deliveryAddress.lastName}">
                                        <span>${fn:escapeXml(cartData.deliveryAddress.title)}&nbsp;${fn:escapeXml(cartData.deliveryAddress.firstName)}&nbsp; ${fn:escapeXml(cartData.deliveryAddress.lastName)}</span>
                                        <a href="${request.contextPath}/checkout/multi/delivery-address/add">Change</a>
                                    </c:when>
                                    <c:otherwise>
                                        <span><spring:theme code="multi.checkout.title.${user.titleCode}.name" text=""/>&nbsp; ${fn:escapeXml(user.firstName)}&nbsp; ${fn:escapeXml(user.lastName)}</span>
                                        <a href="${request.contextPath}/checkout/multi/delivery-address/add">Change</a>
                                    </c:otherwise>
                                </c:choose>
                            </div>
						</c:if>
						<c:if test="${status.index eq 2}">
                            <div class="step-right">
                                <c:choose>
                                    <c:when test="${not empty cartData.billingAddress.firstName && not empty cartData.billingAddress.lastName}">
                                        <span>${fn:escapeXml(cartData.billingAddress.title)}&nbsp;${fn:escapeXml(cartData.billingAddress.firstName)}&nbsp; ${fn:escapeXml(cartData.billingAddress.lastName)}</span>
                                        <a href="${request.contextPath}/checkout/multi/billing-address/add">Change</a>
                                    </c:when>
                                    <c:otherwise>
                                        <span><spring:theme code="multi.checkout.title.${user.titleCode}.name" text=""/>&nbsp; ${fn:escapeXml(user.firstName)}&nbsp; ${fn:escapeXml(user.lastName)}</span>
                                        <a href="${request.contextPath}/checkout/multi/billing-address/add">Change</a>
                                    </c:otherwise>
                                </c:choose>
                            </div>
						</c:if>${deliveryModeName}
						<c:if test="${status.index eq 1}">
                            <div class="step-right">
                                <span>${cartData.deliveryMode.name}</span><a href="${request.contextPath}/checkout/multi/delivery-method/choose">Change</a>
                            </div>
						</c:if>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </c:forEach>

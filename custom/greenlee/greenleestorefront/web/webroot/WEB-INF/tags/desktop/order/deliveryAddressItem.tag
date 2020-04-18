<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.AbstractOrderData" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="hasShippedItems" value="${order.deliveryItemsQuantity > 0}" />
<div>
	<label><spring:theme code="text.deliveryAddress" text="Delivery Address"/></label>
	<c:if test="${not hasShippedItems}">
		<spring:theme code="checkout.pickup.no.delivery.required"/>
	</c:if>
	<c:if test="${hasShippedItems}">
		<c:set var="isNotEditable" value="${order.deliveryAddress.isSapSourcedAddress}"/>
		<c:if test="${isNotEditable && not empty companyName && not empty userType && userType=='B2E'}">
			<h3>${fn:escapeXml(companyName)}</h3>
		</c:if>
		<h3><%-- ${fn:escapeXml(order.deliveryAddress.title)} &nbsp;--%>${fn:escapeXml(order.deliveryAddress.firstName)}&nbsp;${fn:escapeXml(order.deliveryAddress.lastName)}</h3>
		<p>
			<c:if test="${not empty order.deliveryAddress.line1}">
				${fn:escapeXml(order.deliveryAddress.line1)}&nbsp;<br/>
			</c:if>
			<c:if test="${not empty order.deliveryAddress.line2}">
				${fn:escapeXml(order.deliveryAddress.line2)}&nbsp;<br/>
			</c:if>
			<c:if test="${not empty order.deliveryAddress.town}">
				${fn:escapeXml(order.deliveryAddress.town)},
			</c:if>
			<c:if test="${not empty order.deliveryAddress.region.name}">
				${fn:escapeXml(order.deliveryAddress.region.name)}
			</c:if>
			&nbsp;${fn:escapeXml(order.deliveryAddress.postalCode)}<br/>${fn:escapeXml(order.deliveryAddress.country.name)}
		</p>
	</c:if>
</div>

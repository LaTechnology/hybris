<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.OrderData" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

	
<h3><spring:theme code="checkout.multi.paymentMethod.paymentDetails.cardDetails" arguments="${fn:escapeXml(order.paymentInfo.cardTypeData.name)},${fn:substring(paymentInfo.cardNumber, fn:length(paymentInfo.cardNumber)-4, fn:length(paymentInfo.cardNumber))}"/></h3>
<p><spring:theme code="checkout.multi.paymentMethod.paymentDetails.expires" arguments="${fn:escapeXml(paymentInfo.expiryMonth)},${fn:escapeXml(paymentInfo.expiryYear)}"/></p>
	
 <c:choose>
	 <c:when test="${not empty order.paymentInfo.cardTypeData and fn:containsIgnoreCase(order.paymentInfo.cardTypeData.name, 'Visa')}">
			<theme:image code="img.missingProductImage.responsive.visa" alt="VISA" title="VISA"/>
	 </c:when>
	 <c:when test="${not empty order.paymentInfo.cardTypeData and fn:containsIgnoreCase(order.paymentInfo.cardTypeData.name, 'Mastercard')}">
	 	<theme:image code="img.missingProductImage.responsive.mastercard" alt="Mastercard" title="Mastercard"/>
	 </c:when>
	 <c:when test="${not empty order.paymentInfo.cardTypeData and fn:containsIgnoreCase(order.paymentInfo.cardTypeData.name, 'Maestro')}">
			 <theme:image code="img.missingProductImage.responsive.maestro" alt="Maestro" title="Maestro"/>
	 </c:when>
	 <c:when test="${not empty order.paymentInfo.cardTypeData and fn:containsIgnoreCase(order.paymentInfo.cardTypeData.name, 'American Express')}">
		<theme:image code="img.missingProductImage.responsive.americanexpress" alt="American Express" title="American Express"/>
	 </c:when>
	 <c:when test="${not empty order.paymentInfo.cardTypeData and fn:containsIgnoreCase(order.paymentInfo.cardTypeData.name, 'Discover')}">
		<theme:image code="img.missingProductImage.responsive.discover" alt="Discover" title="Discover"/>
	 </c:when>
 </c:choose>
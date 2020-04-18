<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="deliveryMethods" required="true" type="java.util.List" %>
<%@ attribute name="selectedDeliveryMethodId" required="false" type="java.lang.String" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/desktop/checkout/multi" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<select id="delivery_method" name="selectedDeliveryMethod" class="custom-select">
	<c:forEach items="${deliveryMethods}" var="deliveryMethod">
		<c:set scope="page"  var="currentDeliveryMethod"  value="${deliveryMethod}"/>
		<multi-checkout:deliveryMethodDetails deliveryMethod="${deliveryMethod}" isSelected="${deliveryMethod.code eq selectedDeliveryMethodId}"/>
	</c:forEach>
</select>

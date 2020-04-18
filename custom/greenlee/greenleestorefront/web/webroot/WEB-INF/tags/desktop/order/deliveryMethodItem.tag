<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="deliveryMode" required="true"
	type="de.hybris.platform.commercefacades.order.data.DeliveryModeData"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>


<div>
	<label> <spring:theme code="text.shippingMethod"
			text="Shipping Method" /></label> 
		<h3>${deliveryMode.name}</h3>
		<p>${deliveryMode.description}</p>
</div>
<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="priceData" required="true" type="de.hybris.platform.commercefacades.product.data.PriceData"%>
<%@ attribute name="deliveryAddress" required="true" type="de.hybris.platform.commercefacades.user.data.AddressData"%>
<%@ attribute name="sapPriceAvailability" required="false" type="java.lang.Boolean"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%--
 Tag to render a currency formatted price.
 Includes the currency symbol for the specific currency.
--%>



<c:choose>
	<c:when test="${sapPriceAvailability}">
		<c:choose>
			<c:when test="${deliveryAddress.country.isocode ne 'US' and deliveryAddress.country.isocode ne 'CA'}">
				<spring:theme code="shipping.price.WillAdviseLater" text="Will Advise Later" />
			</c:when>
			<c:otherwise>
				<c:if test="${empty priceData.formattedValue}">
					$0.0
				</c:if>
				<c:if test="${not empty priceData.formattedValue}">
					${priceData.formattedValue}
				</c:if>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<c:choose>
			<c:when test="${not empty deliveryAddress}">
				<spring:theme code="shipping.price.WillAdviseLater" text="Will Advise Later" />
			</c:when>
			<c:otherwise>
			<c:if test="${not empty priceData.formattedValue}">
				${priceData.formattedValue}
			</c:if>		
			<c:if test="${empty priceData.formattedValue}">
				$0.0
			</c:if>	
		</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>
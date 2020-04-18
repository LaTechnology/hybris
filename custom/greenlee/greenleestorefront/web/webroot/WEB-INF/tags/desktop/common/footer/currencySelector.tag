<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>

<%@ attribute name="currencies" required="true"
	type="java.util.Collection"%>
<%@ attribute name="currentCurrency" required="true"
	type="de.hybris.platform.commercefacades.storesession.data.CurrencyData"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<c:if test="${disableCurrency}">
	<c:set var="disabled" value="disabled"/>
</c:if>
<c:if test="${fn:length(currencies) > 1}">
	<c:url value="/_s/currency" var="setCurrencyActionUrl" />
	<div class="currency-selector">
		<form:form action="${setCurrencyActionUrl}" method="post"
			id="currency-form" class="container">
			<div class="inner">
				<div class="form-group">
					<label class="control-label"
						for="currency_selector_${component.uid}"><spring:theme
							code="text.currency" /></label>
					<div class="controls usertype-selector  logged-in">
							<c:choose>
							<c:when test="${disableCurrency}">
								<c:forEach items="${currencies}" var="curr">
								<c:if test="${curr.isocode == currentCurrency.isocode}">
								<span class="select-wrapper not-down">
									<span class="holder"><spring:theme code="text.defaultCountry.${curr.isocode}" arguments="${curr.symbol} - ${curr.isocode}"></spring:theme></span>
								</span>
								</c:if>								
								</c:forEach>
							</c:when>
							<c:otherwise>
								<select name="code" id="currency-selector" 
									class="form-control custom-select" ${disabled}>
									<c:forEach items="${currencies}" var="curr">
										<option value="${curr.isocode}"	${curr.isocode == currentCurrency.isocode ? 'selected="selected"' : ''}>
											<spring:theme code="text.defaultCountry.${curr.isocode}" arguments="${curr.symbol} - ${curr.isocode}"></spring:theme>
										</option>
										<c:if test="${curr.isocode eq currentCurrency.isocode}">
											<c:set var="selectedCurrency" value="${curr.isocode}"/>
											<c:set var="currencySymbol" value="${curr.symbol}"/>
										</c:if>
									</c:forEach>
								</select>
							</c:otherwise>
							</c:choose>
							
					</div>
				</div>
				<i class="fa fa-globe fa-m"></i>
			</div>
		</form:form>
	</div>
</c:if>

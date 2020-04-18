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


<c:if test="${fn:length(currencies) > 1}">
	<c:url value="/_s/currency" var="setCurrencyActionUrl" />
	<div class="currency-selector">
		<form:form action="${setCurrencyActionUrl}" method="post"
			id="currency_form_${component.uid}" class="currency-form container">
			<div class="inner">
				<div class="form-group">
					<label class="control-label"
						for="currency_selector_${component.uid}"><spring:theme
							code="text.currency" /></label>
					<div class="controls usertype-selector logged-in">
						<span class="select-wrapper">
							<select name="code" id="currency_selector_${component.uid}"
								class="custom-select currency-selector">
								<c:forEach items="${currencies}" var="curr">
									<option value="${curr.isocode}"	${curr.isocode == currentCurrency.isocode ? 'selected="selected"' : ''}>
										<spring:theme code="text.defaultCountry.${curr.isocode}" arguments="${curr.symbol}"></spring:theme>
									</option>
									<c:if test="${curr.isocode eq currentCurrency.isocode}">
										<c:set var="selectedCurrency" value="${curr.isocode}"/>
										<c:set var="currencySymbol" value="${curr.symbol}"/>
									</c:if>
								</c:forEach>
							</select>
									<span class="holder"><spring:theme code="text.defaultCountry.${selectedCurrency}" arguments="${currencySymbol}"></spring:theme></span>
						</span>
					</div>
				</div>
				<i class="fa fa-globe fa-m"></i>
			</div>
		</form:form>
	</div>
</c:if>

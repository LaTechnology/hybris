<%@ page trimDirectiveWhitespaces="true"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<c:url value="/search/" var="searchUrl" />
<c:url value="/search/autocomplete/${component.uid}"
	var="autocompleteUrl" />

<div class="site-search ui-front">
	<form name="search_form_${component.uid}" method="get"
		action="${searchUrl}">
		<div class="input-group control-group">
			<spring:theme code="search.placeholder" var="searchPlaceholder" />
			<div class="controls">
			<ycommerce:testId code="header_search_input">
				
				<input type="text" id="js-site-search-input"
					class="form-control js-site-search-input" name="text" value=""
					maxlength="100" placeholder="${searchPlaceholder}"
					data-options='{"autocompleteUrl" : "${autocompleteUrl}","minCharactersBeforeRequest" : "${component.minCharactersBeforeRequest}","waitTimeBeforeRequest" : "${component.waitTimeBeforeRequest}","displayProductImages" : ${component.displayProductImages}}'
					required="required">
				
			</ycommerce:testId>

			<span class="input-group-btn"> <ycommerce:testId
					code="header_search_button">
					<button class="siteSearchSubmit" type="submit">
						<!-- <span class="glyphicon glyphicon-search"></span> -->
						<i class="fa fa-search fa-m"></i>
					</button>
				</ycommerce:testId>
			</span>
			</div>
		</div>
	</form>

</div>

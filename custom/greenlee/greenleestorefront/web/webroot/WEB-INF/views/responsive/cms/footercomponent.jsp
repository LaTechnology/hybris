<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="footer" tagdir="/WEB-INF/tags/responsive/common/footer"  %>

<!-- <div class="container"> -->

	
    <div class="ft-top">
    <div class="container container-footer">
	<div class="row">
	
		<c:forEach items="${navigationNodes}" var="node" varStatus="j">
			<c:if test="${node.visible}">
			<div class="${j.count > 3 ? (j.last ? 'col-md-3 last' : 'col-md-3') : 'col-md-2'}">
			<h4>${node.title}</h4>
						<div class="links">
						<ul>
				<c:forEach items="${node.links}" step="${component.wrapAfter}"
					varStatus="i">

						
								
									<c:forEach items="${node.links}" var="childlink"
										begin="${i.index}" end="${i.index + component.wrapAfter - 1}">
										
												<cms:component
												component="${childlink}" evaluateRestriction="true"
												element="li" />
											
										
									</c:forEach>
								
							
						</c:forEach>
						</ul>
						 <c:if test="${j.last}">
						 	<cms:pageSlot position="socialslot" var="feature">
								<cms:component component="${feature}" element="div" class="ft-social" />
							</cms:pageSlot>
				 		</c:if>
						</div>
				 </div>
				
			</c:if>
		</c:forEach>
	</div>
	</div>
	</div>

	<c:if test="${showLanguageCurrency}">
		<div class="pull-right">
			<footer:languageSelector languages="${languages}"
				currentLanguage="${currentLanguage}" />
			<footer:currencySelector currencies="${currencies}"
				currentCurrency="${currentCurrency}" />
		</div>
	</c:if>


<%-- <div class="copyright">
	<div class="container">${notice}</div>
</div> --%>

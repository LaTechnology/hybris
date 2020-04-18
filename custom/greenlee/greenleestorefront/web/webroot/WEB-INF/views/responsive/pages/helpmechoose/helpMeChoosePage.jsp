<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<div class="helpme-popup">
	<h2><spring:theme code="text.helpmechoose.title"/></h2>
	<form:form method="post" action="${actionURL}" name="helpmechooseform" id="helpmechooseform" >
	<input type="hidden" name="questionCode" value="${question.code}">
	<input type="hidden" name="previousQuestionCode" value="${previousQuestionCode}">
		<ul id="progressbar">
			<li class="active first">
				<span class="before"></span>	
			</li>
			<li>
				<span class="before"></span>
				<span class="after"></span>
			</li>
			<li>
				<span class="before"></span>
				<span class="after"></span>
			</li>
		</ul>
		<p>${question.question}</p>
		<div class="form-group">
			<c:forEach var="answer" items="${question.answers}">
				<div class="radio-group">
					<div class="radio">
						<input type="radio" class="remember" name="answerCode" value="${answer.code}"/>
						<label for="radio1"><span>${answer.answer}</span></label>
					 </div>
    			</div>
			</c:forEach>
		</div>
		<c:if test="${not empty hasPrevious}">
			<button class="btn btn-white btn-popup" type="button"><spring:theme code="text.helpmechoose.button.back"/></button>
		</c:if>
					
		<c:if test="${not empty hasNext}">
			<button class="btn" type="button"><spring:theme code="text.helpmechoose.button.next"/></button>
		</c:if>
		
		<c:if test="${empty hasNext}">
			<button class="btn" type="button"><spring:theme code="text.helpmechoose.button.findchoice"/></button>
		</c:if>
	</form:form>
</div>


<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<div class="helpme-popup">
	<h2> <spring:theme code="text.helpmechoose.title" />  </h2>
	<p>	<spring:theme code="text.helpmechoose.subtitle" /> </p>
	<form:form method="post" action="${actionURL}" name="makethechoice"
		id="makethechoice" class="initial-state">
		<input type="hidden" name="questionCode" value="${question.code}">
		<input type="hidden" name="depth" value="${depth}">
		<input type="hidden" name="currentPosInDepth"
			value="${currentPosInDepth}">
		<input type="hidden" name="previousQuestionCode"
			value="${previousQuestionCode}">
		<c:set var="class4First" value="first" />
		<ul id="progressbar">
			<c:forEach begin="1" end="${depth}" var="currentPos">

				<c:choose>
					<c:when test="${not empty class4First}">
						<li
							class="${currentPos <= currentPosInDepth ? 'active ':'' } ${class4First}">
							<span class="before"></span>
						</li>
						<c:set var="class4First" value="" />
					</c:when>
					<c:otherwise>
						<li class="${currentPos <= currentPosInDepth ? 'active ':'' }">
							<span class="before"></span> <span class="after"></span>
						</li>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</ul>
		<p>${question.question}</p>
		<div class="form-group">
			<c:forEach var="answer" items="${question.answers}"
				varStatus="status">
				<div class="radio-group">
					<div class="radio"
						data-target="<c:url value="${answer.targetURL}"/>">
						<input type="radio" class="remember" name="answerCode"
							value="${answer.code}" id="help${status.count}" /> <label
							for="help${status.count}">${answer.answer}</label>
					</div>
				</div>
			</c:forEach>
		</div>
		<div
			class="help-btn-group clearfix ${currentPosInDepth eq 1 ? 'first':'other' }">
			<c:if test="${not empty hasPrevious}">
				<a id="prevQues" class="btn btn-white btn-popup" type="submit"><spring:theme
						code="text.helpmechoose.button.back" /></a>
			</c:if>

			<c:if test="${not empty hasNext}">
				<button id="nextQues" class="btn" type="submit">
					<spring:theme code="text.helpmechoose.button.next" />
				</button>
			</c:if>

			<c:if test="${empty hasNext}">
				<button id="findResult" class="btn" type="submit">
					<spring:theme code="text.helpmechoose.button.findchoice" />
				</button>
			</c:if>
		</div>
	</form:form>
</div>


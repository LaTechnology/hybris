<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="vouchersInCart" required="false" type="java.util.List"%>
<%@ attribute name="showTax" required="false" type="java.lang.Boolean"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
	

		<form method="post" class="vouchers_form clearfix" action='${request.contextPath}<spring:theme code="vouchers.form.add.action" />'>
		<c:url value="/cart" var="cartURL"/>
			<input type="hidden" name="vbackURL" value="/cart" />
			<input type="text" placeholder="<spring:theme code="vouchers.form.txt.codeplaceholder" />" name='<spring:theme code="vouchers.form.txt.name" />' class="vouchers_txt col-xxs-12" />
			<input type="hidden" name="CSRFToken" value="${CSRFToken}" />
			<input type="submit" value='<spring:theme code="vouchers.form.button.label" />' class="btn" />
		</form>
		
		
		<c:if test="${not empty vouchersInCart}">
		<h5><spring:theme code="vouchers.applied.title" /></h5>
		<ul class="voucherApplied">
			
			<c:forEach var="voucher" items="${vouchersInCart}" >
				<c:if test="${voucher['class'].name ne 'java.lang.String'}">
						<li>
							
							<form method="post" class="vouchers_form clearfix" action='${request.contextPath}<spring:theme code="vouchers.form.del.action" />'>
							<input type="hidden" name="vbackURL" value="/cart" />
							<input type="hidden" name='<spring:theme code="vouchers.form.txt.name" />' value="${voucher.voucherCode}" />
							<input type="hidden" name="CSRFToken" value="${CSRFToken}" />
							<button type="submit" class="remove-voucher btn-trans" >${voucher.voucherCode}<i class="gl gl-remove"></i></button>
							</form>
						</li>
					</c:if>
			</c:forEach>
		</ul>
	</c:if>
		
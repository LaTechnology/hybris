<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:forEach items="${productData}" var="product" varStatus="status">
	<div class="col-xs-6 col-md-3 item-box">
		<product:productListerGridItem product="${product}" /> 
	</div>	
</c:forEach>

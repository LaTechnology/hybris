<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/desktop/order" %>

<div class="account-orderdetail">
	<div class="account-orderdetail-item-section-header">
		<div>
			<order:orderTotalsItem order="${orderData}"/>
		</div>
	</div>
</div>
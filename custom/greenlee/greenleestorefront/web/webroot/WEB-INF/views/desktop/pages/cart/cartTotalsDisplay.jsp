<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart" %>

<!-- Verified that there's a pre-existing bug regarding the setting of showTax; created issue  -->

<cart:cartTotals cartData="${cartData}" vouchersInCart="${vouchersInCart}" showTax="false"/>

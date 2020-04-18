<%@ taglib prefix="address" tagdir="/WEB-INF/tags/desktop/address"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement"	tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<form:form commandName="addressForm">
	<input id="saveInAddressBook" name="saveInAddressBook" type="hidden"
		value="true" />
	<address:addressFormElementsForCheckout regions="${regions}" country="${country}" />
</form:form>


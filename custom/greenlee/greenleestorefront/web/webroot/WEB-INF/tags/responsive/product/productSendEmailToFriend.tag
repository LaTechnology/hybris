<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
	
<div class="sendEmailToFriendPopup">
		<input name="toAddress" id="toAddress" type="text" placeholder="Recipients" />
		<input name="fromAddress" id="fromAddress" type="text" placeholder="Sender" />
		<textarea name="message" id="message" type="text" placeholder="Message"></textarea> 
		<input type="hidden" id="productCodeForShare" value="${product.code}"/>
		<p>
			<button class="sendProductAsEmailButton" value="Share">Share</button>
		</p>
		<div id="info"></div>
	</div>
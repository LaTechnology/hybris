<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>


<footer class="footer">
	<cms:pageSlot position="Footer" var="feature">
		<cms:component component="${feature}"/>
	</cms:pageSlot>

<div class="ft-bot">
   <div class="container container-footer">
     <div class="row">
	<cms:pageSlot position="Section8" var="feature">
	
  
		<cms:component component="${feature}" element="div" class="footer-items ${feature.uid}"/>
	
	
	</cms:pageSlot>
	</div>
	</div>
</div>
</footer>
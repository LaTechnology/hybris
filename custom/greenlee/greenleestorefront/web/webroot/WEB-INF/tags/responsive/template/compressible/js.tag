<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- jquery --%>

<script type="text/javascript" src="${themeResourcePath}/js/home.js"></script>
<%-- plugins --%>
<%-- <script type="text/javascript" src="${commonResourcePath}/js/enquire.min.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/Imager.min.js"></script> --%>
<script type="text/javascript" src="${commonResourcePath}/js/jquery.blockUI-2.66.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/jquery.hoverIntent.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/jquery.pstrength.custom-1.2.0.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/jquery.syncheight.custom.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/jquery.tabs.custom.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/jquery-ui-1.11.2.custom.min.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/jquery.zoom.custom.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/underscore-min.js"></script>


<%-- Custom ACC JS --%>

<script type="text/javascript" src="${commonResourcePath}/js/acc.address.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.autocomplete.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.cart.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.cartitem.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.checkout.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.checkoutaddress.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.checkoutsteps.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.common.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.global.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.hopdebug.js"></script>
<%-- <script type="text/javascript" src="${commonResourcePath}/js/acc.imagegallery.js"></script> --%>
<script type="text/javascript" src="${commonResourcePath}/js/acc.langcurrencyselector.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.minicart.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.order.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.paginationsort.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.payment.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.paymentDetails.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.pickupinstore.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.product.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.productDetail.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.quickview.js"></script>
<%--<script type="text/javascript" src="${commonResourcePath}/js/acc.ratingstars.js"></script>--%>
<script type="text/javascript" src="${commonResourcePath}/js/acc.refinements.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.silentorderpost.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.tabs.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.termsandconditions.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.track.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.storefinder.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.productlist.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/_autoload.js"></script>

<%-- Cms Action JavaScript files --%>
<c:forEach items="${cmsActionsJsFiles}" var="actionJsFile">
    <script type="text/javascript" src="${commonResourcePath}/js/cms/${actionJsFile}"></script>
</c:forEach>

<%-- AddOn JavaScript files --%>
<c:forEach items="${addOnJavaScriptPaths}" var="addOnJavaScript">
    <script type="text/javascript" src="${addOnJavaScript}"></script>
</c:forEach>

 <script type="text/template" id="productListtmpl">
 	{{ _.each( items, function( listItem){ }}
 		<div class="col-xs-12">
			<div class="item">			
				{{ _.each( listItem.images, function(images){ }}
					
					{{ if (images.format !== "thumbnail") { }}				
					<div class="glist-image">				
						<a class="thumb" href="{{= listItem.url}}" title="{{= listItem.name }}">
							<img src="{{= images.url }}" alt="{{= listItem.name }}">
						</a>
					</div>
					{{ } }}
				
				 {{ }); }}
				<div class="glist-thumb">
					<h3><a class="thumb" href="{{= listItem.url}}" title="{{= listItem.name }}">{{= listItem.name }}</a></h3>
					<div class="prdt-desc"><p>{{= listItem.summary }}</p></div>
				</div>
				<div class="glist-details">
				<span class="price">{{= listItem.price }}</span>
				<form method="post" id="addToCartForm{{= listItem.code }}" class="add_to_cart_form" action="${addToCartUrl}">
					<input type="hidden" maxlength="3" size="1" id="qty" name="qty" class="qty js-qty-selector-input" value="1">
					<input type="hidden" name="productCodePost" value="{{= listItem.code }}">
					<button id="addToCartButton{{= listItem.code }}" type="submit" class="btn btn-primary btn-block js-add-to-cart">
						<i class="fa fa-shopping-cart"></i>Add to cart
					</button>
				</form>
				</div>
			</div>
		</div>
 	  {{ }); }}
  </script>
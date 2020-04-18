<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%-- jquery --%>

<script type="text/javascript" src="${themeResourcePath}/js/home.js"></script>
<%-- plugins --%>

<%--<script type="text/javascript" src="${commonResourcePath}/js/jquery.blockUI-2.66.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/jquery.hoverIntent.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/jquery.pstrength.custom-1.2.0.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/jquery.syncheight.custom.js"></script>--%>
<%--<script type="text/javascript" src="${commonResourcePath}/js/jquery.tabs.custom.js"></script>--%>
<%--<script type="text/javascript" src="${commonResourcePath}/js/jquery.zoom.custom.js"></script>--%>


<%-- Custom ACC JS --%>

<script type="text/javascript" src="${commonResourcePath}/js/acc.address.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.autocomplete.js"></script>
<%--<script type="text/javascript" src="${commonResourcePath}/js/acc.cart.js"></script>--%>
<%--<script type="text/javascript" src="${commonResourcePath}/js/acc.cartitem.js"></script>--%>
<%--<script type="text/javascript" src="${commonResourcePath}/js/acc.checkout.js"></script> --%>
<%--<script type="text/javascript" src="${commonResourcePath}/js/acc.checkoutaddress.js"></script> --%>
<%--<script type="text/javascript" src="${commonResourcePath}/js/acc.checkoutsteps.js"></script> --%>
<script type="text/javascript" src="${commonResourcePath}/js/acc.common.js"></script>
<%--<script type="text/javascript" src="${commonResourcePath}/js/acc.global.js"></script> --%>
<%--<script type="text/javascript" src="${commonResourcePath}/js/acc.hopdebug.js"></script>--%>
<%-- <script type="text/javascript" src="${commonResourcePath}/js/acc.imagegallery.js"></script> --%>
<%--<script type="text/javascript" src="${commonResourcePath}/js/acc.langcurrencyselector.js"></script>--%>
<%--<script type="text/javascript" src="${commonResourcePath}/js/acc.minicart.js"></script>--%>
<%--<script type="text/javascript" src="${commonResourcePath}/js/acc.order.js"></script> --%>
<%--<script type="text/javascript" src="${commonResourcePath}/js/acc.paginationsort.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.payment.js"></script>--%>
<%--<script type="text/javascript" src="${commonResourcePath}/js/acc.paymentDetails.js"></script>--%>
<%--<script type="text/javascript" src="${commonResourcePath}/js/acc.pickupinstore.js"></script>--%>
<script type="text/javascript" src="${commonResourcePath}/js/acc.productDetail.js"></script>


<%--<script type="text/javascript" src="${commonResourcePath}/js/acc.refinements.js"></script>--%>
<%--<script type="text/javascript" src="${commonResourcePath}/js/acc.silentorderpost.js"></script>--%>
<%--<script type="text/javascript" src="${commonResourcePath}/js/acc.tabs.js"></script>--%>
<%--<script type="text/javascript" src="${commonResourcePath}/js/acc.termsandconditions.js"></script>--%>
<script type="text/javascript" src="${commonResourcePath}/js/acc.track.js"></script>
<%--<script type="text/javascript" src="${commonResourcePath}/js/acc.storefinder.js"></script>--%>

<%--<script type="text/javascript" src="${commonResourcePath}/js/acc.warrantyproduct.js"></script>--%>
<script type="text/javascript" src="${commonResourcePath}/js/_autoload.js"></script>
<%--<script type="text/javascript" src="${commonResourcePath}/js/registerRegions.js"></script>--%>
<%--<script type="text/javascript" src="${commonResourcePath}/js/acc.addressRegions.js"></script>--%>


<%-- Cms Action JavaScript files --%>
<c:forEach items="${cmsActionsJsFiles}" var="actionJsFile">
    <script type="text/javascript" src="${commonResourcePath}/js/cms/${actionJsFile}"></script>
</c:forEach>

<%-- AddOn JavaScript files --%>
<c:forEach items="${addOnJavaScriptPaths}" var="addOnJavaScript">
    <script type="text/javascript" src="${addOnJavaScript}"></script>
</c:forEach>

<script type="text/javascript">
function addASMFormHandler() {
    if ($) {
        if ($(".asmForm").length) {
            $(".asmForm").each(function () {
                $(this).submit(function() {

                    $(this).find('[placeholder]').each(function() {
                        var input = $(this);
                        if (input.val() == input.attr('placeholder')) {
                            input.val('');
                        }
                    })

                    $.ajax({
                        type: "POST",
                        url: $(this).attr('action'),
                        data: $(this).serialize(),
                        success: function(data) {
                        	var wrapper= document.createElement('div');
                        	wrapper.innerHTML= data;
                        	if($(wrapper).find('title').length != 0 ){
                        		window.location.reload();
                        	} else {
                        		$("#_asm").replaceWith(data);
                        	}
                            addASMHandlers();
                        }
                    });
                    return false;
                });
            });
        }
    }
}
</script>
 <script type="text/template" id="productListtmpl">
 	{{ _.each( items, function( listItem){ }}

 		<div class="col-xs-12">
			<div class="item">
				<div class="glist-image">
				{{ if (listItem.images !== null) { }}
				{{ _.each( listItem.images, function(images){ }}
					{{ if (images.format !== "thumbnail") { }}

						<a class="thumb" href="{{=ACC.config.contextPath}}{{= listItem.url}}" title="{{= listItem.name }}">
							<img src="{{= images.url }}" alt="{{= listItem.name }}">
						</a>

					{{ } }}
				 {{ }); }}
				{{ }  else { }}
						<a class="thumb" href="{{=ACC.config.contextPath}}{{= listItem.url}}" title="{{= listItem.name }}">
					<img src="/_ui/desktop/theme-greenlee/images/missing_product_300x300.jpg" alt="AirScout missing image" title="AirScout missing image">
						</a>
				{{ } }}
				</div>
				<div class="glist-thumb">
					<div class="checkbox checkbox-group glcheckbox">
					<input id="compare-{{= listItem.code }}" class="remember" type="checkbox" data-code={{= listItem.code }} onclick="pcUpdateComparableState('{{= listItem.code }}','{{=ACC.config.encodedContextPath}}/compare', this, 'Compare')">
					<label for="compare-{{= listItem.code }}">Compare</label>
				  </div>
					<h3><a class="thumb" href="{{=ACC.config.contextPath}}{{= listItem.url}}" title="{{= listItem.name }}">{{= listItem.name }}<br>{{= listItem.catalogNumber }}</a></h3>
					<div class="prdt-desc"><p>{{= listItem.summary }}</p></div>
				</div>
				<div class="glist-details">
        {{ if ( listItem.showAddToCart && listItem.price) { }}
				<span class="price">{{= listItem.price.formattedValue}}</span>
        {{ } }}
				<form method="post" id="addToCartForm{{= listItem.code }}" class="add_to_cart_form" action="{{=items.cartUrl}}">
					<input type="hidden" maxlength="3" size="1" id="qty" name="qty" class="qty js-qty-selector-input" value="1">
					<input type="hidden" name="productCodePost" value="{{= listItem.code }}">
					{{ if (listItem.showAddToCart && listItem.price) { }}
						{{ if (listItem.purchasable && listItem.stock.stockLevelStatus.code !== "outOfStock") { }}
					<button id="addToCartButton{{= listItem.code }}" type="submit" class="btn btn-primary btn-block js-add-to-cart" disabled="disabled">
						<i class="fa fa-shopping-cart"></i>Add to cart
					</button>
						{{ }else { }}
						<button type="button" class="addToCartButton outOfStock" disabled="disabled">
						<spring:theme code="product.variants.out.of.stock"/>
						</button>
						{{ } }}
					{{ }else { }}
					<a href='<c:url value="/contactus"></c:url>' title="Where to Buy" class="btn btn-primary btn-block"><spring:theme code="home.wheretobuy"/></a>
					{{ } }}
				</form>
				</div>
			</div>
		</div>
 	  {{ }); }}
  </script>

  <script type="text/template" id="productGridtmpl">
 	{{ _.each( items, function( listItem){ }}
 		<div class="col-xs-6 col-md-4 item-box">
			<div class="item">
				{{ if (listItem.images !== null) { }}
				{{ _.each( listItem.images, function(images){ }}
					{{ if (images.format !== "thumbnail") { }}

						<a class="thumb" href="{{=ACC.config.contextPath}}{{= listItem.url}}" title="{{= listItem.name }}">
							<img src="{{= images.url }}" alt="{{= listItem.name }}">
						</a>

					{{ } }}
				 {{ }); }}
				{{ }  else { }}
						<a class="thumb" href="{{=ACC.config.contextPath}}{{= listItem.url}}" title="{{= listItem.name }}">
					<img src="/_ui/desktop/theme-greenlee/images/missing_product_300x300.jpg" alt="AirScout missing image" title="AirScout missing image">
						</a>
				{{ } }}
				<form method="post" id="addToCartForm{{= listItem.code }}" class="add_to_cart_form" action="{{=items.cartUrl}}">
					<input type="hidden" maxlength="3" size="1" id="qty" name="qty" class="qty js-qty-selector-input" value="1">
					<input type="hidden" name="productCodePost" value="{{= listItem.code }}">
					{{ if (listItem.showAddToCart && listItem.price) { }}
						{{ if (listItem.purchasable && listItem.stock.stockLevelStatus.code !== "outOfStock") { }}
					<button id="addToCartButton{{= listItem.code }}" type="submit" class="btn btn-primary btn-block js-add-to-cart" disabled="disabled">
						<i class="fa fa-shopping-cart"></i>Add to cart
					</button>
						{{ }else { }}
						<button type="button" class="addToCartButton outOfStock" disabled="disabled">
						<spring:theme code="product.variants.out.of.stock"/>
						</button>
						{{ } }}
					{{ }else { }}
					<a href='<c:url value="/contactus"></c:url>' title="Where to Buy" class="btn btn-primary btn-block"><spring:theme code="home.wheretobuy"/></a>
					{{ } }}
				</form>
				<h3><a class="thumb" href="{{=ACC.config.contextPath}}{{= listItem.url}}" title="{{= listItem.name }}">{{= listItem.name }}</a></h3>
        {{ if ( listItem.showAddToCart && listItem.price) { }}
				<span class="price">{{= listItem.price.formattedValue}}</span>
        {{ } }}
				<p>{{= listItem.summary }}</p>
				<div class="checkbox checkbox-group glcheckbox">
					<input id="compare-{{= listItem.code }}" class="remember" type="checkbox" data-code={{= listItem.code }} onclick="pcUpdateComparableState('{{= listItem.code }}','{{=ACC.config.encodedContextPath}}/compare', this, 'Compare')">
					<label for="compare-{{= listItem.code }}">Compare</label>
				  </div>
				</div>
			</div>
		</div>
 	  {{ }); }}
  </script>


  <script type="text/javascript" src="${commonResourcePath}/js/acc.liveedithandler.js"></script>

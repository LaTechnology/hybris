ACC.minicart = {


	updateMiniCartDisplay: function(){
		var miniCartRefreshUrl = $(".js-mini-cart-link").data("miniCartRefreshUrl");
		$.get(miniCartRefreshUrl,{ "_": $.now() },function(data){
			var data = $.parseJSON(data);
			$(".js-mini-cart-link .js-mini-cart-count").html(data.miniCartCount)
			$(".js-mini-cart-link .js-mini-cart-price").html(data.miniCartPrice)
		})
	}

};
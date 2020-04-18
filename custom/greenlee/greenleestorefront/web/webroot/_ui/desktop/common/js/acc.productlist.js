_.templateSettings = {
    evaluate:    /\{\{(.+?)\}\}/g,
    interpolate: /\{\{=(.+?)\}\}/g,
    escape:      /\{\{-(.+?)\}\}/g
};


ACC.productlisting = {
	currentPage:             0,
	numberOfPages:           Number.MAX_VALUE,
	_autoload: [
		"triggerLoadMoreResults"
	],
	triggerLoadMoreResults: function() {
		this.numberOfPages =  parseInt($('input[name=loadTotalNoOfPages]').val(),10);
		this.currentPage = parseInt($('input[name=loadCurrentPage]').val(),10);
		$('.load-more').on('click','.btn',function(e){	
				e.preventDefault();
				ACC.productlisting.currentPage=parseInt(ACC.productlisting.currentPage,10) + 1;
			if (ACC.productlisting.currentPage < ACC.productlisting.numberOfPages) {				
				ACC.productlisting.loadMoreResults(parseInt(ACC.productlisting.currentPage,10));
			} 
		});
	},
	getParameterByName:function (name) {
	    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
	    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
	        results = regex.exec(location.search);
	    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
	},
	loadMoreResults: function(page) {
		var that = this;
		var query=that.getParameterByName("q") || that.getParameterByName("text");
		$.ajax({
			url: location.pathname+'/results',
			async: true,
			data:{'q':query,
				  'page':that.currentPage,
				  'show':'Page',
				  'loadMore':'true',
				  'sort':this.getParameterByName("sort"),
				  'viewAs':this.getParameterByName("viewAs"),
				  'text':this.getParameterByName("text")},
			}).done(function (data) {
					
					var listview = $(".product .product-listing");
					var gridview = $(".glproduct-grid .product");
					//$('input[name=loadTotalNoOfPages]').remove();
					//$('input[name=loadCurrentPage]').remove();
					if(that.numberOfPages == that.currentPage + 1) {
						$('.load-more').remove();
					}
					data.results.cartUrl = $('.add_to_cart_form').attr('action');
					//if(that.getParameterByName("viewAs") == 'list'){
						//$("#productListtmpl").tmpl(productData).appendTo( ".product .product-listing");
						if(listview.length != 0){
							var template = _.template($( "#productListtmpl" ).html());
	  						listview.append(template({'items':data.results}));
  						} else {
  							var template = _.template($( "#productGridtmpl" ).html());
	  						gridview.append(template({'items':data.results}));
  						}
  						GreenleeProduct.enableAddToCartButton();
						GreenleeProduct.bindToAddToCartForm();
					//} else {
						//$("#productGridtmpl").tmpl(productData).appendTo( ".productlist .product-append > div.row");
					//}

				});
	}
};


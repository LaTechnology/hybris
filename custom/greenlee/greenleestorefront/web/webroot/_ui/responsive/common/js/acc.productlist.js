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
		$('.load-more').on('click','.btn',function(e){
			if (ACC.productlisting.currentPage < ACC.productlisting.numberOfPages) {
				ACC.productlisting.currentPage=parseInt(ACC.productlisting.currentPage,10) + 1;
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
					console.log(data);
					//$(data).appendTo( ".productlist .product-append > div.row");
					that.numberOfPages =  parseInt($('input[name=loadTotalNoOfPages]').val(),10);
					that.currentPage = parseInt($('input[name=loadCurrentPage]').val(),10);
					
					$('input[name=loadTotalNoOfPages]').remove();
					$('input[name=loadCurrentPage]').remove();
					if(that.numberOfPages == that.currentPage + 1) {
						$('.showPagination').hide();
					}
									
					//if(that.getParameterByName("viewAs") == 'list'){
						//$("#productListtmpl").tmpl(productData).appendTo( ".product .product-listing");
						
						var template = _.template(
					            $( "#productListtmpl" ).html()
					        );
  						$(".product .product-listing").append(template({'items':data.results}));	
					//} else {
						//$("#productGridtmpl").tmpl(productData).appendTo( ".productlist .product-append > div.row");
					//}

				});
	}
};


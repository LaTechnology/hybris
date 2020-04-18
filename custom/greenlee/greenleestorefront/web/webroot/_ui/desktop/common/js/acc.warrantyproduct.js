ACC.warrantyproduct = {

	bindAll : function() {
		with (ACC.warrantyproduct) {
			saveWarranty();
		}
	},

	saveWarranty : function() {
		$('.addEditKey').on(
				"focusout",
				function(e) {
					var that = $(this);
					var entryNumber = that.attr("data-product");
					var quantity = that.attr("data-quantity");
					var urlPost = ACC.config.encodedContextPath
							+ "/warranty/addSerailNo";
					if (that.val()) {
						$.ajax({
							url : urlPost,
							method : 'POST',
							data : {
								entryNumber : entryNumber,
								entryQuantity : quantity,
								serialNumbers : that.val()
							},
							success : function(data) {

								if (data == 'success') {
									$('.keysadded_response').empty();
									that.parents('.addkey_text').find(
											'.keysadded_response').append(
											'Saved...');

								}
							}
						});

					}
					else
						{
							$('.keysadded_response').empty();
						}

				})
	}
};
$(document).ready(function() {
	ACC.warrantyproduct.bindAll();
});
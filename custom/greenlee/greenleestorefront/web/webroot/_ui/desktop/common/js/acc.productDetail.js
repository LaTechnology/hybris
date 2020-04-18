ACC.productDetail = {

    _autoload: [
        "initPageEvents",
        "bindVariantOptions"
    ],


    checkQtySelector: function(self, mode) {
        var input = $(self).parents(".js-qty-selector").find(".js-qty-selector-input");
        
        var inputVal = parseInt(input.val());
        var max = input.data("max");


        var minusBtn = $(self).parents(".js-qty-selector").find(".js-qty-selector-minus");
        var plusBtn = $(self).parents(".js-qty-selector").find(".js-qty-selector-plus");


        $(self).parents(".js-qty-selector").find(".btn").removeAttr("disabled");
        if (mode == "minus") {

            if (inputVal != 1) {
                ACC.productDetail.updateQtyValue(self, inputVal - 1);
                plusBtn.removeAttr("disabled");
                if (inputVal - 1 == 1) {
                    minusBtn.attr("disabled", "disabled");
                }

            } else {
                minusBtn.attr("disabled", "disabled");
            }
        } else if (mode == "reset") {
            ACC.productDetail.updateQtyValue(self, 1)

        } else if (mode == "plus") {
            if (inputVal != max) {
                ACC.productDetail.updateQtyValue(self, inputVal + 1);
                minusBtn.removeAttr("disabled");
                if (inputVal + 1 == max) {
                    plusBtn.attr("disabled", "disabled");
                }
            } else {
                plusBtn.attr("disabled", "disabled");
            }
        } else if (mode == "input") {
            if (inputVal == 1) {
                $(self).parents(".js-qty-selector").find(".js-qty-selector-minus").attr("disabled", "disabled")
            } else if (inputVal == max) {
                $(self).parents(".js-qty-selector").find(".js-qty-selector-plus").attr("disabled", "disabled");
                minusBtn.removeAttr("disabled");
            } else if (inputVal < 1) {
                ACC.productDetail.updateQtyValue(self, 1)
                $(self).parents(".js-qty-selector").find(".js-qty-selector-minus").attr("disabled", "disabled")
            } else if (inputVal > max) {
                ACC.productDetail.updateQtyValue(self, max)
                $(self).parents(".js-qty-selector").find(".js-qty-selector-plus").attr("disabled", "disabled")
            }
        }

    },
    updateQtyValue: function(self, value) {
        var input = $(self).parents(".js-qty-selector").find(".js-qty-selector-input");
        var addtocartQty = $(self).parents(".product-wrp").find("#addToCartForm").find(".js-qty-selector-input");
        input.val(value);
        addtocartQty.val(value);
        if ($('.cart-qty').length != 0) {
            var entryNumber = $(self).parents('.cart-qty').find('.update-entry-quantity-input').attr('id').split("_");
            var form = $(self).parents('.cart-qty').find('form');
            var productCode = form.find('input[name=productCode]').val();
            var initialCartQuantity = form.find('input[name=initialQuantity]').val();
            var newCartQuantity = form.find('input[name=quantity]').val();

            if (initialCartQuantity != newCartQuantity) {
                ACC.track.trackUpdateCart(productCode, initialCartQuantity, newCartQuantity);
                form.submit();
            }
        }
    },
    initPageEvents: function() {
    	var clicky;

        $(document).mousedown(function(e) {
            // The latest element clicked
            clicky = $(e.target);
        });

        // when 'clicky == null' on blur, we know it was not caused by a click
        // but maybe by pressing the tab key
        $(document).mouseup(function(e) {
            clicky = null;
        });
        
    	  $('.js-qty-selector .js-qty-selector-input').on("blur", function(e) {
    		  var input = $(this).parents(".js-qty-selector").find(".js-qty-selector-input");
    		  var addtocartQty = $(this).parents(".product-wrp").find("#addToCartForm").find(".js-qty-selector-input");
    		  if ($(this).val().length == 0){
    			  if(!clicky.hasClass('js-qty-selector-plus')){    	 
    				  var inputVal = 01;       		
    				 
    			  } else {
    				  var inputVal = 0;  
    			  }
    			  input.val(inputVal);
				  addtocartQty.val(inputVal);
    
         	 }
         });

        $(document).on("click", '.js-qty-selector .js-qty-selector-minus', function() {
            ACC.productDetail.checkQtySelector(this, "minus");
        })


        $(document).on("click", '.js-qty-selector .js-qty-selector-plus', function() {
            ACC.productDetail.checkQtySelector(this, "plus");
        })

        $(document).on("keydown", '.js-qty-selector .js-qty-selector-input', function(e) {
            if (($(this).val() != " " && ((e.which >= 48 && e.which <= 57) || (e.which >= 96 && e.which <= 105))) || e.which == 8 || e.which == 46 || e.which == 37 || e.which == 39 || e.which == 9) {} else if (e.which == 38) {
                ACC.productDetail.checkQtySelector(this, "plus");
            } else if (e.which == 40) {
                ACC.productDetail.checkQtySelector(this, "minus");
            } else {
                e.preventDefault();
            }
        });
      

        $(document).on("keyup", '.js-qty-selector .js-qty-selector-input', function(e) {
            ACC.productDetail.checkQtySelector(this, "input");
            ACC.productDetail.updateQtyValue(this, $(this).val());

        })


        $("#Size").change(function() {
            var url = "";
            var selectedIndex = 0;
            $("#Size option:selected").each(function() {
                url = $(this).attr('value');
                selectedIndex = $(this).attr("index");
            });
            if (selectedIndex != 0) {
                window.location.href = url;
            }
        });

        $("#variant").change(function() {
            var url = "";
            var selectedIndex = 0;
            $("#variant option:selected").each(function() {
                url = $(this).attr('value');
                selectedIndex = $(this).attr("index");
            });
            if (selectedIndex != 0) {
                window.location.href = url;
            }
        });

    },

    bindVariantOptions: function() {
        ACC.productDetail.bindCurrentStyle();
        ACC.productDetail.bindCurrentSize();
        ACC.productDetail.bindCurrentType();
    },

    bindCurrentStyle: function() {
        var currentStyle = $("#currentStyleValue").data("styleValue");
        var styleSpan = $(".styleName");
        if (currentStyle != null) {
            styleSpan.text(": " + currentStyle);
        }

    },

    bindCurrentSize: function() {
        var currentSize = $("#currentSizeValue").data("sizeValue");
        var sizeSpan = $(".sizeName");
        if (currentSize != null) {
            sizeSpan.text(": " + currentSize);
        }

    },

    bindCurrentType: function() {
        var currentSize = $("#currentTypeValue").data("typeValue");
        var sizeSpan = $(".typeName");
        if (currentSize != null) {
            sizeSpan.text(": " + currentSize);
        }

    }


};
$(document).ready(function() {


    $(document).on("click", "#cboxLoadingGraphic", function() {

        if ($(".sendEmailToFriendPopup").length > 0) {
            $("#cboxLoadingGraphic, #cboxLoadingOverlay").css({
                'display': 'none'
            });
            $("#toAddress").focus();
        }

    })
    $(document).on("click", ".sendProductAsEmailButton", null, shareProduct);
    $(".printProductDetailsPage").click(printPDP);


    $(document).on("click", "#downloadMore", null, function() {
        $(document).scrollTop($("#downloadJump").position().top);
        $("li[data-rel='downloads']").addClass("active").click();
    });

});

function shareProduct() {
    var json = {
        "toAddress": $('#toAddress').val(),
        "fromAddress": $('#fromAddress').val(),
        "message": $('#message').val(),
        "productCode": $('#productCodeForShare').val(),
        "CSRFToken": $('#CSRFToken').val()
    };

    $.ajax({
        url: ((location.href.indexOf("#") == -1) ? location.href : location.href.substring(0, location.href.length - 1)) + '/send-to-friend',
        type: 'POST',
        data: {
            'toAddress': json.toAddress.replace(/\s+/g, ''),
            'fromAddress': json.fromAddress.replace(/\s+/g, ''),
            'message': json['message'],
            'CSRFToken': json['CSRFToken']
        },
        dataType: 'json',
        success: function(response) {
            if (response.successMessage) {
                $('.share-form').hide();
                $('.sendProductAsEmailButton').hide();
                var message = "<div id='globalMessages'><div class='global-alerts'><div class='alert alert-success alert-dismissable'>" + response.successMessage + "<i class='gl gl-remove'></i></div></div></div>";
                $('#share-form-message').html(message);
            } else {
                if (response.toAddressMessage) {
                    $('#share-form-toAddress').html(response.toAddressMessage);
                    $('#share-form-toAddress').parents('.form-group').addClass('has-error');
                }
                if (response.fromAddressMessage) {
                    $('#share-form-fromAddress').html(response.fromAddressMessage);
                    $('#share-form-fromAddress').parents('.form-group').addClass('has-error');
                }

            }
            colorbox.resize();
            $('#globalMessages .gl-remove').on('click', function() {
                $('#globalMessages').remove();
            });
        },
    });
}

/*adding a function to print the PDP page*/
function printPDP() {
    window.print();
}
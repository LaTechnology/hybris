module.exports =
    GreenleeProduct = {

        enableAddToCartButton: function() {
            $('.js-add-to-cart').removeAttr('disabled');
        },
        bindToAddToCartForm: function() {
            var that = this;
            var addToCartForm = $('.add_to_cart_form');

            addToCartForm.ajaxForm({
                beforeSubmit: that.loadingAddtoCart,
                cache: false,
                success: that.displayAddToCartPopup
            });
        },
        loadingAddtoCart: function(arr, $form, options) {
            //clearTimeout(GreenleeProduct.dismissttl);
            //clearTimeout(GreenleeProduct.dismissCart);
            $form.find('.js-add-to-cart').attr('disabled', 'disabled');
            $form.find('.js-add-to-cart').html('<i class="fa fa-spinner fa-spin"></i>Adding');

        },
        displayAddToCartPopup: function(cartResult, statusText, xhr, formElement) {
            $('#addToCartLayer').remove();

            if (typeof minicart.updateMiniCartDisplay == 'function') {
                minicart.updateMiniCartDisplay();
            }

            var titleHeader = $('#addToCartTitle').html();


            /*colorbox.open(titleHeader,{
            	html: cartResult.addToCartLayer,
            	width:"320px",
            });*/
            //
            $('.cart-menu').append(cartResult.addToCartLayer);
            $('.navbar-fixed-top').removeClass('fixedAtTop');
            GreenleeProduct.dismissttl = setTimeout(function() {
                $('#addToCartLayer').addClass('active');
                GreenleeProduct.enableAddToCartButton();
                formElement.find('.js-add-to-cart').html('<i class="fa fa-shopping-cart"></i>Add to cart');
            }, 700);
            GreenleeProduct.dismissCart = setTimeout(function() {
                $('#addToCartLayer').remove();
            }, 3500);
            var productCode = $('[name=productCodePost]', formElement).val();
            var quantityField = $('[name=qty]', formElement).val();

            var quantity = 1;
            if (quantityField != undefined) {
                quantity = quantityField;
            }


            ACC.track.trackAddToCart(productCode, quantity, cartResult.cartData);
        }
    };

GreenleeProduct.enableAddToCartButton();
GreenleeProduct.bindToAddToCartForm();

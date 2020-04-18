module.exports =
    minicart = {
        bindAll: function() {
            this.cartRemoveFn();
            this.minicartHover();
        },
        cartRemoveFn: function() {
            var that = this;
            $('body').off('click').on('click', '.submitRemoveProduct', function(e) {
                e.preventDefault();
                var $that = $(this),
                    entryNumber = $that.attr('data-remove');
                $.ajax({
                    url: $that.attr('data-url'),
                    cache: false,
                    data: {
                        entryNumber: entryNumber,
                        uid: 'MiniCart'
                    }
                }).done(function(data) {
                    $('.checkout__order-wrp').html(data);
                    that.updateMiniCartDisplay();
                });
            });
        },
        updateMiniCartDisplay: function() {
            var miniCartRefreshUrl = $(".js-mini-cart-link").data("miniCartRefreshUrl");
            $.get(miniCartRefreshUrl, {
                "_": $.now()
            }, function(data) {
                var data = $.parseJSON(data);
                $(".js-mini-cart-link .js-mini-cart-count").html(data.miniCartCount)
                $(".js-mini-cart-link .js-mini-cart-price").html(data.miniCartPrice)
            })
        },
        minicartHover: function() {
            var that = this;
            $(document).on('mouseenter', '.cart-menu .js-mini-cart-link', function(e) {
                $.ajax({
                    url: $(this).attr('data-mini-cart-url'),
                    cache: false
                }).done(function(data) {
                    $('.checkout__order-wrp').html(data);
                    that.cartRemoveFn();
                    $('.cart-switch .dd-head').removeClass('active');
                    $('.cart-switch .dd-body').hide();
                    $('.cart-menu').addClass('checkout--active');
                });

            });
            $(document).on('mouseleave', '.cart-menu', function(e) {
                if ($('.cart-menu').hasClass('checkout--active')) {
                    $('.cart-menu').removeClass('checkout--active');

                }
            });
            if (/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent)) {
                $('.mini-cart-link').on('click', function(event) {
                    event.preventDefault();
                    if ($(this).parent('.cart-menu').hasClass('checkout--active')) {
                        $('.cart-menu').removeClass('checkout--active');
                    } else {
                        $('.cart-menu').addClass('checkout--active');
                    }
                });
            }
        }
    };
minicart.bindAll();

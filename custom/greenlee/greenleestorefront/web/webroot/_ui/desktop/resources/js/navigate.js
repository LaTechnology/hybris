module.exports =
    navigate = {
        scrollToElement: function(target) {
            if (target.length) {
                $('html, body').animate({
                    scrollTop: target.offset().top
                }, 1000);
            }
        },
        scrollSection: function() {
            /*Start Home page srcoll*/
            $('.feature .gl-down').on('click', function() {
                var scrollElement = $('.video__container');
                navigate.scrollToElement(scrollElement);
            });
            $('.choose-section .gl-down').on('click', function(e) {
                e.stopPropagation();
                e.preventDefault();
                var scrollElement = $('.product-container');
                navigate.scrollToElement(scrollElement);
            });
            $('.cloud .gl-down').on('click', function(e) {
                e.stopPropagation();
                e.preventDefault();
                var scrollElement = $('#testimonials');
                if (scrollElement.length) {
                    $('html, body').animate({
                        scrollTop: scrollElement.offset().top - $('.category-menu').outerHeight(true)
                    }, 1000);
                }
            });
            /*End Home page srcoll*/
            $('#applications .gl-down').on('click', function() {
                var scrollElement = $('#benefits');
                if (scrollElement.length) {
                    $('html, body').animate({
                        scrollTop: scrollElement.offset().top - $('.category-menu').outerHeight(true)
                    }, 1000);
                }
            });
            $('.prd-grey .js-ratingCalc').click(function() {
                var scrollElement = $('.product-reviews');
                navigate.scrollToElement(scrollElement);
            });
            $('.maintence-link a').click(function(e) {
                e.preventDefault();
                var scrollElement = $('.maintenence-product');
                navigate.scrollToElement(scrollElement);
            });
        }
    };

navigate.scrollSection();

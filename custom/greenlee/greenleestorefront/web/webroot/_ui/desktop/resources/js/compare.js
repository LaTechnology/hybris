module.exports =
    comparePage = {
        bindAll: function() {
            'use strict';
            $('.compare-carousel .compare-prd h3').dotdotdot({
                height: 54,
            });
            $('.compare-carousel .summary').dotdotdot({
                height: 84,
            });
            this.carouselInvoke();
            $('.compare-carousel .compare-prd').syncHeight();
        },
        carouselInvoke: function() {
            'use strict';
            var compareCarousel = $('.compare-carousel'),
                flag = false,
                duration = 300;
            if (compareCarousel.length != 0) {
                if (compareCarousel.children().length > 0) {
                    compareCarousel.owlCarousel({
                        responsiveClass: true,
                        responsive: {
                            0: {
                                items: 1
                            },
                            992: {
                                items: 3,
                                mouseDrag: false,
                                nav: true,
                                navText: ['<i class="gl gl-left"></i>', '<i class="gl gl-right"></i>'],
                            }
                        },
                    }).on('changed.owl.carousel', function(e) {
                        if (e.namespace && e.property.name === 'position' && !flag) {
                            flag = true;
                            var pageShow = e.page.size + e.item.index;
                            $('.td-compare').removeClass('active inactive mactive').addClass('inactive');
                            if (e.page.size == 3) {
                                for (var i = e.item.index; i < pageShow; i++) {
                                    $('.feature-' + i).removeClass('inactive').addClass('active');
                                }
                            } else {
                            	
                                $('.feature-' + e.item.index).removeClass('inactive').addClass('mactive');
                            }
                            flag = false;
                        }
                    }).on('resized.owl.carousel', function(e) {
                        if (e.namespace && !flag) {
                            flag = true;
                            var pageShow = e.page.size + e.item.index;
                            $('.td-compare').removeClass('active inactive mactive').addClass('inactive');
                            if (e.page.size == 3) {
                                for (var i = e.item.index; i < pageShow; i++) {
                                    $('.feature-' + i).removeClass('inactive').addClass('active');
                                }
                            } else {
                                $('.feature-' + e.item.index).removeClass('inactive').addClass('mactive');
                            }
                            flag = false;
                        }

                    });
                } else {
                    compareCarousel.show();
                }
            }
        }
    };
comparePage.bindAll();

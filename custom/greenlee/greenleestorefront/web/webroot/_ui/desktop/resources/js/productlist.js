module.exports =
    _.templateSettings = {
        evaluate: /\{\{(.+?)\}\}/g,
        interpolate: /\{\{=(.+?)\}\}/g,
        escape: /\{\{-(.+?)\}\}/g
    };

module.exports =

    productlisting = {
        currentPage: 0,
        numberOfPages: Number.MAX_VALUE,
        refinementsFilter: function() {
            $(document).on('change', '.js-facet-checkbox', function() {
                $(this).parents('form').submit();
            });
            $(document).on('change', '#sortForm1 select', function() {
                $(this).parents('form').submit();
            });
        },
        triggerLoadMoreResults: function() {
            this.numberOfPages = parseInt($('input[name=loadTotalNoOfPages]').val(), 10);
            this.currentPage = parseInt($('input[name=loadCurrentPage]').val(), 10);
            $('.load-more').on('click', '.btn', function(e) {
                e.preventDefault();
                productlisting.currentPage = parseInt(productlisting.currentPage, 10) + 1;
                if (productlisting.currentPage < productlisting.numberOfPages) {
                    productlisting.loadMoreResults(parseInt(productlisting.currentPage, 10));
                }

            });
        },
        getParameterByName: function(name) {
            name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
            var regex = new RegExp('[\\?&]' + name + '=([^&#]*)'),
                results = regex.exec(location.search);
            return results == null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
        },
        loadMoreResults: function(page) {
            var that = this;
            var query = that.getParameterByName('q') || that.getParameterByName('text');
            $.ajax({
                url: location.pathname + '/results',
                async: true,
                data: {
                    q: query,
                    page: that.currentPage,
                    show: 'Page',
                    loadMore: 'true',
                    sort: this.getParameterByName('sort'),
                    viewAs: this.getParameterByName('viewAs'),
                    text: this.getParameterByName('text')
                },
            }).done(function(data) {

                var listview = $('.glproduct-list .product');
                var gridview = $('.glproduct-grid .product');
                //$('input[name=loadTotalNoOfPages]').remove();
                //$('input[name=loadCurrentPage]').remove();
                if (that.numberOfPages == that.currentPage + 1) {
                    $('.load-more').remove();
                }
                data.results.cartUrl = $('.add_to_cart_form').attr('action');
                //if(that.getParameterByName("viewAs") == 'list'){
                //$("#productListtmpl").tmpl(productData).appendTo( ".product .product-listing");
                if (listview.length != 0) {
                    var template = _.template($('#productListtmpl').html());
                    listview.append(template({
                        items: data.results
                    }));
                } else {
                    var template = _.template($('#productGridtmpl').html());
                    gridview.append(template({
                        items: data.results
                    }));
                }
                GreenleeProduct.enableAddToCartButton();
                GreenleeProduct.bindToAddToCartForm();
                productlisting.compareButtonPos();
                //} else {
                //$("#productGridtmpl").tmpl(productData).appendTo( ".productlist .product-append > div.row");
                //}

            });
        },
        compareButtonPos: function(scroll) {
            var footerPos = $('footer').offset().top
            var pagePos = $(window).scrollTop();
            var footerH = $('footer').outerHeight(true);
            var buffer = 20;
            $('.pc-fixed .productComparePanel').css({
                opacity: 1
            });

            if (footerPos <= $(window).innerHeight() + pagePos) {
                $('.pc-fixed .productComparePanel').addClass('sticky');
                $('.pc-fixed .productComparePanel').css({
                    top: 'auto'
                });
                enquire.register('screen and (min-width:992px)', {
                    match: function() {
                        if ($(document).innerHeight() === $(window).innerHeight()) {
                            $('.pc-fixed .productComparePanel').css({
                                top: footerH + buffer
                            });
                        }
                    }
                });
            } else {
                $('.pc-fixed .productComparePanel').removeClass('sticky');
                $('.pc-fixed .productComparePanel').css({
                    top: 'auto'
                });
            }
        },
        mobileRefine: function() {
            $('.mrefine').on('click', function() {
                if ($(this).hasClass('active')) {
                    $('.facets-list').slideUp();
                    $(this).removeClass('active');
                } else {
                    $('.facets-list').slideDown();
                    $(this).addClass('active');
                }
            });
            $('.pdp-links .pdp-list h4').on('click', function() {
                var that = $(this);
                enquire.register('screen and (max-width:800px)', {
                    match: function() {
                        if (that.hasClass('active')) {
                            that.parents('.pdp-list').find('ul').slideUp();
                            that.removeClass('active');
                        } else {
                            $('.pdp-list').find('ul').slideUp()
                            $('.pdp-list h4').removeClass('active');
                            that.parents('.pdp-list').find('ul').slideDown();
                            that.addClass('active');
                        }
                    }
                });
            });
        },
        responsivefacet: function() {
            enquire.register('screen and (max-width:992px)', {
                unmatch: function() {
                    $('.facets-list').css({
                        display: 'block'
                    });
                    $('.pdp-list').find('ul').css({
                        display: 'block'
                    });
                },
                match: function() {
                    $('.facets-list').css({
                        display: 'none'
                    });
                    $('.pdp-list').find('ul').css({
                        display: 'none'
                    });
                }
            });
        }
    };

productlisting.triggerLoadMoreResults();
productlisting.compareButtonPos(false);
productlisting.mobileRefine();
productlisting.responsivefacet();
productlisting.refinementsFilter();
$(window).on('scroll resize', function() {
    productlisting.compareButtonPos(true);
});

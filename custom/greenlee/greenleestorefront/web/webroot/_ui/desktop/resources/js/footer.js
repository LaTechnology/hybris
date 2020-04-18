module.exports =
    Greenleefooter = {
        bindAll: function() {
            this.bindLangCurrencySelector();
            this.footerAtBottom();
            this.mobileFooterAccordion();
        },
        bindLangCurrencySelector: function() {
            $('#lang-selector').change(function() {
                $('#lang-form').submit();
            });

            $('#currency-selector').change(function() {
                $('#currency-form').submit();
            });
        },
        footerAtBottom: function() {
            setTimeout(function() {
                var footerHt = $('footer.footer').height();
                $('body').css('padding-bottom', footerHt);
            }, 1000);

            $(window).resize(_.debounce(function() {
                var footerHt = $('footer.footer').height();
                $('body').css('padding-bottom', footerHt);
            }, 500));
        },
        mobileFooterAccordion: function() {
            enquire.register('screen and (max-width:992px)', {
                unmatch: function() {

                    $('.ft-top').find('ul').css({
                        height: '',
                        display: 'block'
                    });
                    $('.ft-top h4').removeClass('active');
                    $('.ft-top').find('ul').removeClass('active');
                },
                match: function() {

                    $('.ft-top').find('ul').css({
                        display: 'none'
                    });
                }
            });
            $('.ft-top').off('click').on('click', 'h4', function() {
                var that = $(this)
                enquire.register('screen and (max-width:768px)', function() {

                    if (!that.hasClass('active')) {
                        var bodyPad = parseInt($('body').css('padding-bottom').replace('px', ''), 10);
                        var linkHeight = $('.ft-top').find('ul.active').outerHeight(true);
                        var totalHeight = bodyPad - linkHeight;
                        $('body').css({
                            'padding-bottom': totalHeight + 'px'
                        });
                        var bodyPad = parseInt($('body').css('padding-bottom').replace('px', ''), 10);
                        var linkHeight = that.parent().find('ul').outerHeight(true);
                        var totalHeight = bodyPad + linkHeight;
                        $('body').css({
                            'padding-bottom': totalHeight + 'px'
                        });
                        $('.ft-top').find('ul').stop(true, false).slideUp({
                            duration: 350,
                            easing: 'easeOutQuart',
                            queue: false,
                            complete: function() {
                                that.css('height', '');
                                $('.ft-top h4').removeClass('active');
                                $('.ft-top').find('ul').removeClass('active');


                            }
                        });
                        that.parent().find('ul').stop(true, false).slideDown({
                            duration: 350,
                            easing: 'easeOutQuart',
                            queue: false,
                            complete: function() {
                                that.css('height', '');
                                that.addClass('active');
                                that.parent().find('ul').addClass('active');

                            }
                        });
                    }
                });
            });
        }
    };
Greenleefooter.bindAll();

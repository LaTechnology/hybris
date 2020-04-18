module.exports =
    categoryLanding = {
        bindAll: function() {
            'use strict';
            this.pageSelector();
            this.handleMenuClick();
            this.mobileMenuClick();
            this.MenuClickSelection();
            this.stickyMenu();
            this.MenuSelectionUpdate();
        },
        pageSelector: function() {
            this.flexiMenu = selectors.getElement.get('.flexi-menu');
            this.categoryMenu = selectors.getElement.get('.category-menu');
            this.menuInner = selectors.getElement.get('.category-menu .inner');
            this.navFixed = selectors.getElement.get('.navbar-fixed-top');
            this.categoryMenuSpan = selectors.getElement.get('.category-menu label span');
        },
        handleMenuClick: function() {
            'use strict';
            var that = this;
            $('.scrollTrigger').click(function(e) {
                e.preventDefault();
                var $that = $(this);
                if (!$that.closest('li').hasClass('active')) {
                    that.flexiMenu.find('li').removeClass('active');
                    $that.closest('li').addClass('active');
                    var scrollTo = $that.attr('href');
                    $('html,body').animate({
                        scrollTop: $(scrollTo).offset().top - that.menuInner.outerHeight()
                    }, 1000);
                }
            });
        },
        mobileMenuClick: function() {
            'use strict';
            var that = this;
            that.categoryMenu.find('label').click(function() {
                that.flexiMenu.toggleClass('active');
                that.flexiMenu.slideToggle();
            });
        },
        MenuClickSelection: function() {
            'use strict';
            var that = this;
            that.flexiMenu.find('a').click(function() {
                if (that.flexiMenu.hasClass('active')) {
                    var labelText = $(this).text();
                    that.categoryMenuSpan.html(labelText);
                    that.flexiMenu.removeClass('active');
                    that.flexiMenu.slideUp();
                }
            });
        },
        stickyMenu: function() {
            //Sticky category menu
            var that = this;
            if ($('.category-landing').length != 0) {
                $('.category-menu').css('height', that.menuInner.outerHeight());
                $(window).on('load scroll resize', function() {
                    var menuPos = $('.category-menu').offset().top;
                    var pagePos = $(window).scrollTop();
                    if (pagePos >= menuPos) {
                        that.menuInner.addClass('sticky');
                        that.navFixed.addClass('sticky');
                    } else {
                        that.menuInner.removeClass('sticky');
                        that.navFixed.removeClass('sticky');
                    }
                });
            }
        },
        MenuSelectionUpdate: function() {
            var that = this;
            // Cache selectors
            var topMenu = that.flexiMenu,
                topMenuHeight = topMenu.outerHeight(),
                // All list items
                menuItems = topMenu.find('a'),
                // Anchors corresponding to menu items
                scrollItems = menuItems.map(function() {
                    var item = $($(this).attr('href'));
                    if (item.length) {
                        return item;
                    }
                });

            // Bind to scroll
            $(window).scroll(function() {
                // Get container scroll position
                var fromTop = $(this).scrollTop() + that.menuInner.outerHeight();
                // Get id of current scroll item
                var cur = scrollItems.map(function() {

                    if ($(this).offset().top <= fromTop + 1) {
                        return this;
                    }

                });

                // Get the id of the current element
                cur = cur[cur.length - 1];
                var id = cur && cur.length ? cur[0].id : '';
                // Set/remove active class
                if (scrollItems.length != 0) {
                    if (scrollItems[0].offset().top <= fromTop) {
                        menuItems.parent().removeClass('active')
                            .end().filter('[href=\'#' + id + '\']').parent().addClass('active');
                        var labelText = topMenu.find('[href=\'#' + id + '\']').text();
                        that.categoryMenuSpan.html(labelText);
                    } else {
                        topMenu.find('li').removeClass('active');
                        topMenu.find('li').eq(0).addClass('active');
                        var firstText = topMenu.find('li').eq(0).find('a').text();
                        that.categoryMenuSpan.html(firstText);
                    }
                }

            });
        }
    };
categoryLanding.bindAll();

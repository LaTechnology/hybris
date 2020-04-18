module.exports =
    GreenleeHeader = {
        bindAll: function () {
            this.pageSelector();
            this.hamburgerMenu();
            this.ASMmodulefunction();
            this.handleLoggedInHover();
            this.closeOutsideClick();
            this.solutionHover();
            this.siteSearchBox();
        },
        pageSelector: function () {
            this.priNav = selectors.getElement.get('.pri-nav');
            this.userMenu = selectors.getElement.get('.user-menu');
            this.profileMenu = selectors.getElement.get('.profile-menu');
            this.firstMenuItem = selectors.getElement.get('.menuitem.first');
            this.mobileHide = selectors.getElement.get('.mobile-hide');
            this.navbar = selectors.getElement.get('.navbar-toggle');
            this.siteSearch = selectors.getElement.get('.site-search');
        },
        hamburgerMenu: function () {
            'use strict';
            var that = this;
            that.navbar.click(function (event) {
                event.preventDefault();
                var $that = $(this);
                that.siteSearch.slideUp('fast');
                if (that.priNav.hasClass('in')) {
                    $that.removeClass('active').trigger('blur');
                    that.priNav.removeClass('in');
                } else {
                    $that.addClass('active');
                    that.priNav.addClass('in');
                }
            });
        },
        ASMmodulefunction: function () {
            'use strict';
            $('input[name=\'cartId\']').attr('readonly', true);
            $('.ASM_session_andor_text').text('and');
        },
        handleLoggedInHover: function () {
            'use strict';
            var that = this;
            that.userMenu.on('mouseenter touchstart', '.profile-menu.logged > a', function (e) {
                e.preventDefault();
                if ('ontouchstart' in window) {
                    if (that.profileMenu.hasClass('open')) {
                        that.profileMenu.removeClass('open');
                        that.profileMenu.find('ul').stop(true, false).slideUp('slow');
                    } else {

                        that.profileMenu.addClass('open');
                        that.profileMenu.find('ul').stop(true, false).slideDown('slow');
                    }
                } else {
                    that.profileMenu.addClass('open');
                    that.profileMenu.find('ul').stop(true, false).slideDown('slow');
                }

            });
            that.userMenu.on('mouseleave', '.profile-menu', function () {
                that.profileMenu.removeClass('open');
                that.profileMenu.find('ul').stop(true, false).slideUp('slow');
            });
        },
        closeOutsideClick: function () {
            'use strict';
            var that = this;
            $(document).click(function (e) {
                //if the click has happend inside the mobile-nav-menu or mobile-nav-toggle then ignore it
                if (!$(e.target).closest('.user-menu, .profile-menu > a').length) {
                    that.profileMenu.removeClass('open');
                    that.profileMenu.find('ul').stop(true, false).slideUp('slow');
                }
                if (!$(e.target).closest('.menuitem.first').length) {
                    that.firstMenuItem.removeClass('hover');
                }
            });
        },
        solutionHover: function () {
            'use strict';
            var that = this;
            enquire.register('screen and (min-width:769px)', {
                match: function () {
                    that.firstMenuItem.on('click', ' > a', function (e) {
                        'use strict'; //satisfy code inspectors
                        event.preventDefault();
                        var link = $(this).parent(); //preselect the link
                        if (link.hasClass('hover')) {
                            link.removeClass('hover');
                        } else {
                            link.addClass('hover');
                        }

                    });
                },
                unmatch: function () {
                    that.mobileHide.css({
                        display: 'none'
                    });
                    that.firstMenuItem.removeClass('hover');
                }
            });

            that.firstMenuItem.removeClass('hover');
            that.firstMenuItem.off('click').on('click', 'a', function (e) {
                var $that = $(this)
                enquire.register('screen and (max-width:768px)', {
                    match: function () {
                        $that.toggleClass('active');
                        that.mobileHide.stop(true, false).slideToggle('slow');
                    },
                    unmatch: function () {
                        that.mobileHide.css({
                            display: 'none'
                        });
                        that.firstMenuItem.find('a').removeClass('active');
                    }
                });

            });
        },
        siteSearchBox: function () {
            'use strict';
            var that = this;
            $('.msitesearch > a').click(function () {
                that.siteSearch.slideToggle('fast');
                that.priNav.removeClass('in');
                that.navbar.removeClass('active');
            });

            enquire.register('screen and (min-width:992px)', function () {
                that.siteSearch.slideDown('fast');
            });
        }
};
GreenleeHeader.bindAll();

module.exports =
    accountSwitch = {
        bindAll: function () {
            'use strict';
            this.ListMatchItems();
            this.HandleItemsClick();
            this.HandleSwitchToggle();
        },
        /**
         * Fuzzy Search Results for Switch Account user intenally uses List.js Library.
         * @return List with results matching the text in the search box
         */
        ListMatchItems: function () {
            'use strict';
            if ($('#switch-user input[type="text"]').length != 0) {
                var options = {
                    valueNames: ['name', 'list-code'],
                    page: 10
                };
                var userList = new List('switch-user', options);
                var noItems = $('<li id="no-items-found">No search results found</li>');
                userList.on('updated', function (list) {
                    if (list.matchingItems.length == 0) {
                        $(list.list).append(noItems);
                    } else {
                        noItems.detach();
                    }
                });
            }
        },
        /**
         * Click on the List items to submit the form and add a check icon.
         */
        HandleItemsClick: function () {
            'use strict';
            $('.account-switch').on('click', 'li', function () {
                var that = $(this),
                    code;
                if (that.find('.fa').length === 0) {
                    $('.account-switch li .fa').remove();
                    that.addClass('active');
                    code = that.find('.list-code input[name="unitId"]').val();
                    $('#unit').val(code);
                    that.append('<i class="fa fa-check"></i>');
                    that.parents('form').submit();
                }
            });
        },
        /**
         * Slide the switch account section on click on the switch account header.
         */
        HandleSwitchToggle: function () {
            'use strict';
            $('.cart-switch').on('click', '.dd-head', function () {
                var that = $(this),
                    ddBody = $('.dd-body');
                if (that.hasClass('active')) {
                    that.removeClass('active');
                    ddBody.slideUp(500);
                } else {
                    that.addClass('active');
                    ddBody.slideDown(500);
                }
            });
            $(document).click(function (e) {
                //if the click has happend inside the mobile-nav-menu or mobile-nav-toggle then ignore it
                if (!$(e.target).closest('.cart-switch .dd-head').length) {
                    $('.cart-switch .dd-head').removeClass('active');
                    $('.cart-switch .dd-body').stop(true, false).slideUp('slow');
                }
            });
        }
};
accountSwitch.bindAll();

module.exports =
    GreenLeecheckout = {
        bindAll: function() {
            this.bindEmailTagging();
            this.bindcheckboxItems();
            this.heightFixQty();
            this.printRefPage();
            this.syncAddressHeight();
            this.bindCheckedItem();
            this.bindDeliveryMsg();
            this.bindPaymentSelection();
            this.bindDefaultPayment();
            this.bindNewExisitingToggle();
            this.bindCartToggle();
            this.bindDatepicker();

            this.bindShipEmailAccordion();
            this.bindNextEmailTagging();
            this.bindCloseNote();
            this.bindVerticalCarousel();
            this.removeGlobalMsg();
        },
        bindcheckboxItems: function() {
            $('.checkout-content').on('click', '.item', function() {
                var checkbox = $(this).find('input[type="checkbox"]');
                if (checkbox.is(':checked')) {
                    checkbox.prop('checked', false);
                } else {
                    $('.item').find('input[type="checkbox"]').prop('checked', false);
                    checkbox.prop('checked', true);
                }
            });
        },
        heightFixQty: function() {
            if ($('body').hasClass('page-cartPage')) {
                $('.product-item').each(function() {
                    var height = $(this).outerHeight(true);
                    $(this).find('.cart-qty.hidden-xs').css('height', height);
                });
            }
        },
        printRefPage: function() {
            $('#printableRef').click(function() {
                window.print();
            });
        },
        syncAddressHeight: function() {
            $('.address-checkout .address-item').syncHeight();
            $(window).resize(_.debounce(function() {
                //$('.account-body ul.address-items > li').css('height','');
                $('.address-checkout .address-item').css('height', '');
                //$('.account-body ul.address-items > li').syncHeight();
                $('.address-checkout .address-item').syncHeight();
                enquire.register('screen and (min-width:992px)', function() {
                    $('.cart-qty.hidden-xs').height('');
                    $('.product-item').each(function() {
                        var height = $(this).outerHeight(true) - 40;
                        $(this).find('.cart-qty.hidden-xs').height(height);
                    });
                });
            }, 500));
            $(window).bind('load resize', function() {
                var windowWidth = $(window).width();
                if (windowWidth > 768) {
                    $('.account-body ul.address-items > li:odd').addClass('odd');
                    $('.account-body ul.address-items > li:even').addClass('even');
                    $('.account-body ul.address-items li .address-item').css('height', 'auto');
                    $('.account-body ul.address-items li.odd').each(function() {
                        var max_ht = Math.max($(this).height(), $(this).next('li').height());
                        $(this).find('.address-item').css('height', max_ht);
                        $(this).next('li').find('.address-item').css('height', max_ht);
                    });
                } else {
                    $('.account-body ul.address-items li .address-item').css('height', 'auto');
                }
            });
        },
        bindCheckedItem: function() {
            $('#addressbook .btn-delivery').click(function() {
                $('.checked').remove();
                $(this).parents('.address-item').find('.round').html('<span class="checked"></span>');
                $(this).parents('.address-item').find('.delivery-form').submit();
            });
            $('#addressbook .delivery-form .round').click(function(e) {
                $('.checked').remove();
                $(this).parents('.address-item').find('.round').html('<span class="checked"></span>');
                $(this).parents('.address-item').find('.delivery-form').submit();
            });
        },
        bindDeliveryMsg: function() {
            $('#delivery_method').on('change', function() {
                var expedite = $('#delivery_method option:selected').attr('data-expedite');
                if (expedite === 'true') {
                    $('#expediteMsg').show();
                } else {
                    $('#expediteMsg').hide();
                }
                $('.accordion-step').last().find('a').attr('href', 'javascript:void(0)');
            });
        },
        bindPaymentSelection: function() {
            $('#paymentmethod .round').click(function(e) {
                e.preventDefault();
                if ($(this).find('.checked').length == 0) {
                    $('.checked').remove();
                    $(this).parents('.address-item').find('.round').html('<span class="checked"></span>');
                    var val = $(this).parents('.address-item').find('input[name="selectedPaymentMethodId"]').val();
                    var button = $(this).parents('.address-item').find('.btn-saved').detach();
                    $('.address-item').find('.round').not(':has(".checked")').parents('.address-item').each(function() {
                        if ($(this).find('.btn-saved').length == 0) {
                            $(this).append(button);
                        }
                    });
                    $('#bindPaymentCardTypeSelect').val(val);
                }
            });
            var selectedCard = $('#paymentmethod .round').find('.checked');
            if (selectedCard.length != 0 && $('#paymentmethod:visible').length != 0) {
                var selectedVal = selectedCard.parents('.address-item').find('input[name="selectedPaymentMethodId"]').val();
                $('#bindPaymentCardTypeSelect').val(selectedVal);
            }
        },
        bindDefaultPayment: function() {
            $('#paymentmethod .btn-saved').click(function(e) {
                e.preventDefault();
                $('.checked').remove();
                $(this).parents('.address-item').find('.round').html('<span class="checked"></span>');
                var val = $(this).parents('.address-item').find('input[name="selectedPaymentMethodId"]').val();
                var button = $(this).detach();
                $('.address-item').find('.round').not(':has(".checked")').parents('.address-item').each(function() {
                    if ($(this).find('.btn-saved').length == 0) {
                        $(this).append(button);
                    }
                });
                $('#bindPaymentCardTypeSelect').val(val);
            });
        },
        bindNewExisitingToggle: function() {
            $('.address-checkout.active').slideDown(100);
            $('.address-checkout:not(".active")').slideUp(100);
            $('.checkout-address').on('click', 'a', function(e) {
                e.preventDefault();
                var addressBook = $('.address-checkout.active');
                var nonActive = $('.address-checkout:not(".active")');
                var that = $(this);
                var labelText = $(this).parents('.checkout-content').find('.label-bottom').find('label').text();
                var hrefText = $(this).text()
                $(this).text(labelText);
                $(this).parents('.checkout-content').find('.label-bottom').find('label').text(hrefText);
                if (addressBook.hasClass('active')) {
                    addressBook.stop(true, false).slideUp({
                        duration: 200,
                        easing: 'swing',
                        queue: false,
                        complete: function() {
                            addressBook.css('height', '');
                            addressBook.removeClass('active');
                            nonActive.addClass('active');
                        }
                    });
                    nonActive.stop(true, false).slideDown({
                        duration: 200,
                        easing: 'swing',
                        queue: false,
                        complete: function() {
                            nonActive.css('height', '');
                            addressBook.removeClass('active');
                            nonActive.addClass('active');
                            if (that.parents('.checkout-content').find('.payment-iframe').length != 0) {
                                if (that.parents('.checkout-content').find('.payment-iframe').hasClass('active')) {
                                    $('#placeOrderForm1').hide();
                                } else {
                                    $('#placeOrderForm1').show();
                                }
                            }
                        }
                    });
                }
            });
        },
        bindCartToggle: function() {
            $('.checkout-items').on('click', '> a', function(e) {
                e.preventDefault();
                if (!$('.checkout-listitems').hasClass('active')) {
                    $(this).addClass('active');
                    if ($('.checkout-listitems').children().length > 1) {
                        $(this).find('span').text('Hide items');
                    } else {
                        $(this).find('span').text('Hide item');
                    }
                } else {
                    $(this).removeClass('active');
                    if ($('.checkout-listitems').children().length > 1) {
                        $(this).find('span').text('Show items');
                    } else {
                        $(this).find('span').text('Show item');
                    }
                }
                $('.checkout-listitems').stop(true, false).slideToggle({
                    duration: 350,
                    easing: 'swing',
                    queue: false,
                    complete: function() {
                        $('.checkout-listitems').css('height', '');
                        $('.checkout-listitems').toggleClass('active');
                    }
                });
            });
        },
        bindDatepicker: function() {
            $('#shipByDate').datepicker({
                dateFormat: 'mm/dd/yy',
                showOn: 'both',
                buttonText: '<i class=\'fa fa-calendar-o\'></i>',
                nextText: '',
                prevText: '',
                minDate: '0',
                onSelect: function(dateText) {
                    $(this).attr('value', dateText);
                }
            });
            $('#shipByDate').attr('readonly', true);
        },
        bindShipEmailAccordion: function() {
            var that = this;
            $('.email-po-wrp').each(function() {
                if ($(this).find('.has-error').length != 0) {
                    $(this).find('.content-tab').show();
                    if ($(this).find('#email-tagging').length != 0) {
                        var tagged = $('#shippingCCEmailAddress').val();
                        $('#shippingCCEmailAddress').val('');
                        if (tagged.length != 0) {
                            var tagSplit = tagged.split(',');
                            $('#email-tagging').textext()[0].tags().addTags(tagSplit);
                        }
                    }
                    if ($(this).find('#emailCCAddresseshidden').length != 0) {
                        var emailtagged = $('#emailCCAddresseshidden').val();
                        $('#emailCCAddresses').val('');
                        if (emailtagged.length != 0) {
                            var emailtagSplit = emailtagged.split(',');
                            $('#emailCCAddresses').textext()[0].tags().addTags(emailtagSplit);
                        }
                    }
                } else {

                    $(this).find('.content-tab').hide();
                }
            });
            $('.email-po-wrp').on('click', 'a', function(e) {
                e.preventDefault();
                if (!$(this).hasClass('open')) {
                    $(this).addClass('open');
                    $(this).parent().find('.content-tab').stop(true, false).slideDown({
                        duration: 350,
                        easing: 'swing',
                        queue: false,
                        done: function() {
                            if ($(this).parent().find('#carouselId li').length >= 6) {
                                $('#carouselId').carousel({
                                    vertical: true,
                                    scrollNext: '.next',
                                    scrollPrev: '.prev',
                                    scrollNum: 6,
                                    circular: false,
                                    itemHeight: 37,
                                    itemWidth: $('#carouselId li').outerWidth(true),
                                    scrollVisible: 5,
                                });
                            }
                            if ($(this).parent().find('.content-tab').find('#email-tagging').length != 0) {
                                that.bindInitialEmailTag('#email-tagging', '#shippingCCEmailAddress');
                            }
                            if ($(this).parent().find('.content-tab').find('#emailCCAddresses').length != 0) {
                                that.bindInitialEmailTag('#emailCCAddresses', '#emailCCAddresseshidden');
                            }
                        }
                    });
                } else {
                    $(this).parent().find('.content-tab').stop(true, false).slideUp({
                        duration: 350,
                        easing: 'swing',
                        queue: false
                    });
                    $(this).removeClass('open');
                }
            });
        },
        bindInitialEmailTag: function(textbox, hidden) {
            if ($(textbox).length != 0) {
                var emailtagged = $(textbox).val();
                $(textbox).val('');
                if (emailtagged.length != 0) {
                    var emailtagSplit = emailtagged.split(',');
                    $(textbox).textext()[0].tags().addTags(emailtagSplit);
                }
            }
        },
        bindNextEmailTagging: function() {
            var that = this;
            $('#chooseDeliveryMethod_continue_button').on('click', function(e) {
                if ($('.po-email--content textarea').val().length != 0) {
                    e.preventDefault();
                    that.bindInitialEmailTag('#email-tagging', '#shippingCCEmailAddress');
                    that.bindInitialEmailTag('#emailCCAddresses', '#emailCCAddresseshidden');
                    $(this).parents('#shippingMethodForm').submit();
                }

            });
        },
        bindCloseNote: function() {
            $('.close-note').on('click', function() {
                var parent = $(this).parents('.email-po-wrp');
                parent.find('a').removeClass('open');
                parent.find('.content-tab').stop(true, false).slideUp({
                    duration: 350,
                    easing: 'swing',
                    queue: false
                });
            });
        },
        bindEmailTagging: function() {
            $('.po-email--content').css({
                opacity: '1'
            }).show();
            $('#email-tagging, #emailCCAddresses').textext({
                plugins: 'tags',
                ext: {
                    tags: {
                        removeTag: function(tags) {
                            var tagText = $(tags).text().replace(/\s+/g, '');
                            $('.vertical-carousel-list li').each(function(key, value) {
                                var listText = $(this).text().replace(/\s+/g, '');

                                if (listText == tagText) {
                                    $(this).find('input[type="checkbox"]').prop('checked', false);
                                }
                            });
                            $.fn.textext.TextExtTags.prototype.removeTag.apply(this, arguments);
                        },
                        addTags: function(tags) {
                            if (tags != null) {

                                if (tags.length != 0) {
                                    var tagged = tags[0].split(/\s*,\s*/);
                                    var checkEmail = /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))$/i.test(tagged);
                                    if (checkEmail) {
                                        if (tagged.length > 1) {
                                            $('#email-tagging').textext()[0].tags().addTags(tagged);
                                        } else {
                                            $.fn.textext.TextExtTags.prototype.addTags.apply(this, arguments);
                                        }

                                    }
                                }
                            }
                            //
                        },
                        renderTag: function(tag) {
                            return $.fn.textext.TextExtTags.prototype.renderTag
                                .call(this, tag).data("id", tag.id);
                        },
                    }
                }
            }).bind('keyup', function(e) {
                if (e.keyCode == 32 || e.keyCode == 188) {
                    var space_comma_enter = $(this).val().replace(/\s/g, '').replace(',', '');
                    $(this).textext()[0].tags().addTags([space_comma_enter]);
                    $(this).val('');
                }
            }).bind('isTagAllowed', function(e, data) {
                var formData = $(e.target).textext()[0].tags()._formData,
                    list = eval(formData);
                // duplicate checking
                if (formData.length && list.indexOf(data.tag) >= 0) {
                    data.result = false;
                }
            });
            $('.po-email--content').hide();
        },
        bindVerticalCarousel: function() {
            $('.vertical-carousel-list input[type=checkbox]').on('change', function(e) {
                if ($(this).is(':checked')) {
                    $('#email-tagging').textext()[0].tags().addTags([$(this).parent().text().replace(/\s+/g, '')]);
                    $('#tagname').val('');
                } else {

                    var emailInput = $(this).parent().text().replace(/\s+/g, '');
                    $('.text-tag').each(function() {
                        var tagText = $(this).find('.text-label').text();
                        if (emailInput == tagText) {
                            $('#email-tagging').textext()[0].tags().removeTag($(this));

                        }
                    });
                }
            });
        },
        removeGlobalMsg: function() {
            $('.alert .gl-remove').on('click', function() {
                $('.global-alerts').remove();
            });
        },
    };
GreenLeecheckout.bindAll();

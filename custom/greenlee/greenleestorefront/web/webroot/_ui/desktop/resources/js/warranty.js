module.exports =
    warrantyProduct = {

        applySerialNo: function() {
            $('.js-serialno').css({
                'pointer-events': 'auto'
            });
            $(document).off('click').on('click', '.js-serialno', function(e) {
                var $that = $(this);
                e.preventDefault();
                colorbox.open(
                    $(this).data('cboxTitle'), {
                        width: '575px',
                        html: $that.parent().find('.addkey_text'),
                        onCleanup: function() {
                            var serialText = $('.cart-item .details').find('.addkey_text');
                            if (serialText.length == 0) {
                                $('#cboxLoadedContent').find('.addkey_text input').val('');
                                $that.parents('.cart-item .details').append($('#cboxLoadedContent').find('.addkey_text').addClass('hide'));
                            }
                        },
                        onComplete: function() {
                            $('#cboxLoadedContent').find('.addkey_text').removeClass('hide');
                            var serialNo = $that.attr('data-serial');
                            $('#cboxLoadedContent').find('.addkey_text input').val(serialNo);
                            colorbox.resize();
                            $('.addkey_text button[type="submit"]').off('click').on('click', function(e) {
                                e.preventDefault();
                                var that = $('#cboxLoadedContent .addEditKey');
                                var entryNumber = that.attr('data-product');
                                var quantity = that.attr('data-quantity');
                                var urlPost = ACC.config.encodedContextPath +
                                    '/warranty/addSerailNo';
                                if (that.val()) {
                                    $.ajax({
                                        url: urlPost,
                                        method: 'POST',
                                        data: {
                                            entryNumber: entryNumber,
                                            entryQuantity: quantity,
                                            serialNumbers: that.val()
                                        },
                                        success: function(data) {

                                            if (data == 'success') {
                                                var inputHTML = $('#cboxLoadedContent').find('.addkey_text').addClass('hide');
                                                $that.parent().append(inputHTML);
                                                $that.html('Serial Number : ' + that.val());
                                                $that.attr('data-serial', that.val());
                                                colorbox.close();
                                            }
                                        }
                                    });

                                } else {
                                    $('.keysadded_response').empty();
                                }
                            });

                            $('.btn-close').on('click', function() {
                                colorbox.close();
                            });

                        }
                    }
                );
            });
        },

    };

warrantyProduct.applySerialNo();

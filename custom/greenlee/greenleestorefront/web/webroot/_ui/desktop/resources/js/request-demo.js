module.exports =
    GreenleePopup = {
        bindAll: function() {
            this.homePageVideoPopup();
            this.requestDemoPopup();
            this.helpmeFormFn();
            this.helpChooseMePopup();
        },
        homePageVideoPopup: function() {
            $('#owl-home a').off('click').on('click', function(e) {
                e.preventDefault();
                var that = $(this);
                colorbox.open(
                    $(this).data('cboxTitle'), {
                        href: $(this).attr('href'),
                        width: '816px',
                        onComplete: function() {
                            $('#cboxLoadedContent').html('<iframe src=' + that.attr('href') + ' width="560" height="315" frameborder="0" allowfullscreen></iframe>');
                            colorbox.resize();
                        }
                    }
                );
            });
            $('.video__container a').off('click').on('click', function(e) {
                e.preventDefault();
                var that = $(this);
                colorbox.open(
                    $(this).data('cboxTitle'), {
                        href: $(this).attr('href'),
                        width: '816px',
                        onComplete: function() {
                            $('#cboxLoadedContent').html('<iframe src=' + that.attr('href') + ' width="560" height="315" frameborder="0" allowfullscreen></iframe>');
                            colorbox.resize();
                        }
                    }
                );
            });
        },
        requestDemoPopup: function() {
            $('.js-request-demo').css({
                'pointer-events': 'auto'
            });
            $('.js-request-demo').off('click').on('click', function(e) {
                e.preventDefault();
                colorbox.open(
                    $(this).data('cboxTitle'), {
                        href: $(this).attr('href'),
                        width: '816px',
                        onOpen: function() {
                            $('#colorbox').addClass('req-demo-colorbox');
                        },
                        onComplete: function() {
                            greenLeeSelectbox.customSelect();
                            greeenleeValidate.requestForDemoForm();
                            colorbox.resize();
                        }
                    }
                );
            });
        },
        helpmeFormFn: function() {
            var that = this;
            $('form#helpmechooseform').off('submit').on('submit', function(e) {
                e.preventDefault();
                var checked = $('input[name="answerCode"]:checked');
                if (checked.length != 0) {
                    if (checked.parents('.radio').attr('data-target')) {
                        window.location.href = checked.parents('.radio').attr('data-target');
                    } else {
                        $.ajax({
                            url: $('form#helpmechooseform').attr('action'),
                            type: 'POST',
                            data: $('form#helpmechooseform').serialize(),
                            success: function(data, textStatus, jqXHR) {
                                var prevData = $('.helpme-popup').detach();
                                $('#cboxLoadedContent').html(data);
                                $(document).on('click', '#prevQues', function(e) {
                                    $('#cboxLoadedContent').html(prevData);
                                    colorbox.resize();
                                    that.helpmeFormFn();
                                });
                                colorbox.resize();
                                that.helpmeFormFn();
                            }
                        });
                    }
                }

            });
        },
        helpChooseMePopup: function() {
            var that = this;
            $('.choose-section a,.login .form-password a').css({
                'pointer-events': 'auto'
            });
            $('.choose-section a').off('click').on('click', function(e) {
                e.preventDefault();
                colorbox.open(
                    $(this).data('cboxTitle'), {
                        href: $(this).attr('href'),
                        width: '1030px',
                        onClosed: function() {
                            $('#cboxWrapper').removeClass('pop-out');
                            $('#cboxClose').removeClass('helpremove');
                        },
                        onComplete: function() {
                            $('#cboxClose').addClass('helpremove');
                            that.helpmeFormFn();
                        }
                    }
                );
            });
        }
    };
GreenleePopup.bindAll();

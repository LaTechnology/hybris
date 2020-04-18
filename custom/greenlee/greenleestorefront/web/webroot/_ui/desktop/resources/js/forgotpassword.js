module.exports =
    forgotPasswordPopup = {
        colorboxInvoke: function() {
            'use strict';
            $('.js-password-forgotten').off('click').on('click', function(e) {
                e.preventDefault();
                colorbox.open(
                    $(this).data('cboxTitle'), {
                        href: $(this).attr('href'),
                        width: '400px',
                        height: false,
                        scrolling: false,
                        onOpen: function() {
                            selectors.colorbox.addClass('frgt-pwd-popup');
                            $('#validEmail').remove();
                        },
                        onComplete: function() {
                            $('form#forgottenPwdForm').validate({
                                errorElement: 'span',

                                highlight: function(element, errorClass, validClass) {
                                    $(element).parents('.form-group').addClass('has-error');
                                },
                                unhighlight: function(element, errorClass, validClass) {
                                    $(element).parents('.form-group').removeClass('has-error');
                                },
                                showErrors: function(errorMap, errorList) {
                                    this.defaultShowErrors();
                                    colorbox.resize();
                                },
                                rules: {
                                    email: {
                                        required: true,
                                        email: true
                                    },

                                },
                                messages: {
                                    email: 'Please enter a valid email address.'
                                }
                            });
                            $('form#forgottenPwdForm').ajaxForm({
                                beforeSubmit: function() {

                                    return $("form#forgottenPwdForm").valid(); // TRUE when form is valid, FALSE will cancel submit
                                },
                                success: function(data) {
                                    if ($(data).closest('#validEmail').length) {
                                        if ($('#validEmail').length === 0) {
                                            $('.forgotten-password').replaceWith(data);
                                            colorbox.resize();
                                        }
                                    } else {
                                        $('#forgottenPwdForm .control-group').replaceWith($(data).find('.control-group'));
                                        colorbox.resize();
                                    }
                                }
                            });
                        }
                    }
                );
            });
        }
    };
forgotPasswordPopup.colorboxInvoke();

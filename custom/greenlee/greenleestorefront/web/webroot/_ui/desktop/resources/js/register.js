module.exports =
    GreenleeRegister = {
        bindAll: function () {
            this.pageSelector();
            this.userTypeSelector();
            this.passwordStrength();
            this.existingSelector();
            this.initialUserTypeState();
            this.accountNoSelector();
            this.countrySelector();
            this.bindChangeCountry();
        },
        pageSelector: function () {
            this.distributor = selectors.getElement.get('.distributor-fields');
            this.online = selectors.getElement.get('.online-fields');
            this.existingcheckbox = selectors.getElement.get('.existing-checkbox');
            this.existingCheck = selectors.getElement.get('.existing-check');
            this.passwordFields = selectors.getElement.get('.password-fields');
            this.passwordStrong = selectors.getElement.get('.password-strength');
        },
        userTypeSelector: function () {
            var that = this;
            $('.usertype-selector select').change(function () {
                var _that = $(this);
                if (_that.val() == 'B2B') {
                    that.existingcheckbox.hide();
                    that.existingCheck.show();
                    that.distributor.slideDown().find('input').val('');
                    that.distributor.find('.custom-select').each(function () {
                        $(this).find('option').eq(0).prop('selected', 'selected');
                        var selectedOption = $(this).find('option').eq(0).text();
                        $(this).next('.holder').text(selectedOption);
                    });
                    that.online.slideUp();
                    that.passwordFields.slideDown().find('input').val('');
                    $('#password_bar,#password_minchar').remove();
                } else
                if (_that.val() == 'B2E') {
                    that.existingcheckbox.show();
                    $("#mailling").hide();
                    $("#company").show();
                    if ($('.existing-checkbox input[type=checkbox]').is(':checked')) {
                        that.online.slideUp();
                        that.existingCheck.show();
                    } else {
                        that.online.slideDown();
                        that.existingCheck.hide();
                    }

                    that.distributor.slideDown().find('input').val('');
                    that.distributor.find('.custom-select').each(function () {
                        $(this).find('option').eq(0).prop('selected', 'selected');
                        var selectedOption = $(this).find('option').eq(0).text();
                        $(this).next('.holder').text(selectedOption);
                    });
                    that.passwordFields.slideDown().find('input').val('');
                    $('#password_bar,#password_minchar').remove();
                } else {
                	 $("#mailling").show();
                	 $("#company").hide();
                    that.existingcheckbox.hide();
                    that.existingCheck.show();
                    that.distributor.slideUp();
                    that.online.slideDown().find('input').val('')
                    that.online.find('.custom-select').each(function () {
                        $(this).find('option').eq(0).prop('selected', 'selected');
                        var selectedOption = $(this).find('option').eq(0).text();
                        $(this).next('.holder').text(selectedOption);
                    });
                    that.passwordFields.slideDown().find('input').val('');
                    $('#password_bar,#password_minchar').remove();
                }

                if ($('#registerForm #country').length != 0) {
                    if ($('#registerForm #country')[0].selectedIndex == 0) {
                        console.log($('#registerForm #country:selected').val());
                        $('#registerForm #country option[value=\'US\']').attr('selected', true);
                        $('#registerForm #country').next('.holder').text($('#registerForm #country option[value=\'US\']').text());
                        $('#registerForm #country').trigger('change');
                    }
                }
            });
        },
        passwordStrength: function () {
            var that = this;
            if (that.passwordStrong.length > 0) {
                that.passwordStrong.pstrength();
            }
        },

        existingSelector: function () {
            var that = this;
            $('.existing-checkbox input[type=checkbox]').on('change', function (e) {
                if ($(this).is(':checked')) {
                    that.online.slideUp();
                    that.existingCheck.show();
                    $(this).val(true);
                } else {
                    that.online.slideDown();
                    that.existingCheck.hide();
                    $(this).val(false);
                }
            });
        },
        initialUserTypeState: function () {
            var that = this;
            var selectedValue = $('#greenLeeRegisterUserType :selected').val();
            if (selectedValue == 'B2B') {
                that.existingcheckbox.hide();
                that.existingCheck.show();
                that.distributor.slideDown();
                that.online.slideUp();
                that.passwordFields.slideDown();
            } else if (selectedValue == 'B2E') {
                that.existingcheckbox.show();
                that.distributor.slideDown();
                /*var hasExisting = $('.existing-checkbox input[type=checkbox]').val();
                if (hasExisting) {
                    $('.existing-checkbox input[type=checkbox]').attr('checked', true);
                }*/
                if ($('.existing-checkbox input[type=checkbox]').is(':checked')) {
                    that.online.slideUp();
                    that.existingCheck.show();
                } else {
                    that.online.slideDown();
                    that.existingCheck.hide();
                }
                that.passwordFields.slideDown();
            } else if (selectedValue == 'B2C') {
                that.existingcheckbox.hide();
                that.existingCheck.show();
                that.distributor.slideUp();
                that.online.slideDown();
                that.passwordFields.slideDown();
            } else {
                that.distributor.slideUp();
                that.online.slideUp();
                that.passwordFields.slideUp();
            }
        },
        accountNoSelector: function () {
            $('#accountInformationId').on('change', function () {
                $('#accountInformationNumberId').show();
                var selectedOption = $(this).find(':selected').text();
                $('#accountInformationNumberId label').text(selectedOption);
            });
        },
        countrySelector: function () {
            var stateIsoCode = $('#hiddenState').val();
            var isocode = $('#country').val();
            var regionsUrl = $('#regionsUrl').text()
            var $select = $('#regionsDiv select');
            var listItems = '';

            if (isocode) {
                $('#stateTextBox').hide();
                $('#regionsDiv').removeClass('hide');
                $.get(regionsUrl + '?isocode=' + isocode, function (data) {
                    $select.html('');
                    var flag = 0;
                    $select.append('<option value="">Please Select </option>');
                    for (var i = 0; i < data.length; i++) {
                        if (stateIsoCode === data[i].isocode) {
                            flag = 1;
                            $select.append('<option value="' + data[i].isocode + '" selected="selected">' + data[i].name + '</option>');
                        } else {
                            $select.append('<option value="' + data[i].isocode + '">' + data[i].name + '</option>');
                        }
                    }
                    if (flag === 0) {
                        $select.find('option').eq(0).prop('selected', 'selected');
                        $select.next('.holder').text('Please Select');
                    }
                });
                $select.append(listItems);
            }
        },
        bindChangeCountry: function () {
            var that = this;
            $('#country').change(function () {
                that.countrySelector();
            });
        }

};
GreenleeRegister.bindAll();

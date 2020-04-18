module.exports =
    $.validator.addMethod('require_from_group', function(value, element, options) {
        var $fields = $(options[1], element.form),
            $fieldsFirst = $fields.eq(0),
            validator = $fieldsFirst.data('valid_req_grp') ? $fieldsFirst.data('valid_req_grp') : $.extend({}, this),
            isValid = $fields.filter(function() {
                return validator.elementValue(this);
            }).length >= options[0];
        // Store the cloned validator for future validation
        $fieldsFirst.data('valid_req_grp', validator);

        // If element isn't being validated, run each require_from_group field's validation rules
        if (!$(element).data('being_validated')) {
            $fields.data('being_validated', true);
            $fields.each(function() {
                validator.element(this);
            });
            $fields.data('being_validated', false);
        }
        return isValid;
    }, $.validator.format('Please fill at least {0} of these fields.'));

$.validator.addMethod('phone', function(phone_number, element) {
    phone_number = phone_number.replace(/\(|\)|\[|\]|\{|\}|\s+|-/g, '');
    return this.optional(element) || phone_number.length < 21 &&
        phone_number.match(/^[\d- \(\)\[\]\+]+$/);
}, 'Please enter a valid phone number');

$.validator.addMethod("maxlengthspace", function(value, element, len) {
    return value == "" || value.length <= len;
});

greeenleeValidate = {
    bindAll: function() {
        this.requestForDemoForm();
        this.accountLogin();
        this.accountRegister();
        this.searchForm();
        this.repairForm();
        this.returnForm();
        this.contactusForm();
        this.shippingNote();
    },
    accountLogin: function() {
        $('#loginForm').validate({
            errorElement: 'span',

            highlight: function(element, errorClass, validClass) {
                $(element).parents('.form-group').addClass('has-error');
            },
            unhighlight: function(element, errorClass, validClass) {
                $(element).parents('.form-group').removeClass('has-error');
            },
            rules: {
                j_username: {
                    required: true,
                    email: true
                },
                j_password: {
                    required: true
                },
            },
            messages: {
                j_username: 'Please enter a valid email address.'
            }
        });
    },
    shippingNote: function() {
        $('#shippingMethodForm').validate({
            errorElement: 'span',

            highlight: function(element, errorClass, validClass) {
                $(element).parents('.form-group').addClass('has-error');
            },
            unhighlight: function(element, errorClass, validClass) {
                $(element).parents('.form-group').removeClass('has-error');
            },
            rules: {
                shippingNote: {
                    required: false,
                    maxlengthspace: 255
                }
            },
            messages: {
                shippingNote: 'Shipping Note is too long.'
            }
        });
    },
    accountRegister: function() {

        $('#registerForm').validate({
            errorElement: 'span',
            errorPlacement: function(error, element) {
                if (element.is('select')) {
                    element.parents('.controls').append(error);
                } else {
                    element.parents('.form-group').append(error);
                }
            },
            highlight: function(element, errorClass, validClass) {
                $(element).parents('.form-group').addClass('has-error');

            },
            unhighlight: function(element, errorClass, validClass) {
                $(element).parents('.form-group').removeClass('has-error');
            },
            rules: {
                firstName: {
                    required: true,
                    minlength: 2
                },
                lastName: {
                    required: true,
                    minlength: 2
                },
                email: {
                    required: true,
                    email: true
                },
                mobileNumber: {
                    required: true,

                },
                userType: {
                    required: true,
                },
                pwd: {
                    required: true,
                    minlength: 6,
                },
                checkPwd: {
                    required: true,
                    equalTo: '#password',
                    minlength: 6,
                }
            },
            messages: {
                firstName: {
                    minlength: 'Your first name must consist of at least 2 characters'
                },
                lastName: {
                    required: 'Please enter a last name',
                    minlength: 'Your last name must consist of at least 2 characters'
                },
                email: 'Please enter a valid email',
                mobileNumber: 'Please enter a valid phone number',
                userType: 'Please select user type',
                pwd: {
                    required: 'Please enter your password',
                    minlength: jQuery.validator.format('Please enter a strong password (at least {0} characters)'),
                },
                checkPwd: {
                    required: 'Please confirm your password',
                    equalTo: 'Password and password confirmation do not match',
                    minlength: jQuery.validator.format('Please enter a strong password (at least {0} characters)'),
                },
            }
        });
    },

    repairForm: function() {
        $('.request-repair form').validate({

            errorPlacement: function(error, element) {
                if (element.is('select')) {
                    element.parents('.controls').append(error);
                } else {
                    element.parents('.form-group').append(error);
                }
            },
            highlight: function(element, errorClass, validClass) {
                $(element).parents('.form-group').addClass('has-error');

            },
            unhighlight: function(element, errorClass, validClass) {
                $(element).parents('.form-group').removeClass('has-error');
            },
            rules: {
                'Company_Name__c': {
                    required: true,
                },
                'First_Name__c': {
                    required: true
                },
                'Last_Name__c': {
                    required: true
                },
                email: {
                    required: true,
                    email: true
                },
                phone: {
                    required: true,

                },
                /*Order Number:*/
                'Order_Number__c': {
                    required: true
                },
                'Enter_Catalog_Number__c': {
                    required: true
                },
                'Quantity__c': {
                    required: true
                },
                'Reason_For_Repair__c': {
                    required: true
                },
                'Privacy_Policy_Consent__c': { /*Privacy policy*/
                    required: true,
                }
            },
            messages: {
                'Company_Name__c': 'Please provide your company name',
                'First_Name__c': 'Please provide your first name',
                'Last_Name__c': 'Please provide your last name',
                email: 'Please provide a valid email address',
                phone: 'Please provide your phone number',
                'Order_Number__c': 'Please provide your Greenlee order number',
                'Quantity__c': 'Please enter in a quantity',
                'Reason_For_Repair__c': 'Please choose a reason for repair'
            }
        });

    },
    returnForm: function() {
        $('.return form').validate({

            errorPlacement: function(error, element) {
                if (element.is('select')) {
                    element.parents('.controls').append(error);
                } else {
                    element.parents('.form-group').append(error);
                }
            },
            highlight: function(element, errorClass, validClass) {
                $(element).parents('.form-group').addClass('has-error');

            },
            unhighlight: function(element, errorClass, validClass) {
                $(element).parents('.form-group').removeClass('has-error');
            },
            rules: {
                'Company_Name__c': {
                    required: true,
                },
                'First_Name__c': {
                    required: true
                },
                'Last_Name__c': {
                    required: true
                },
                email: {
                    required: true,
                    email: true
                },
                phone: {
                    required: true,

                },
                /*Order Number:*/
                'Order_Number__c': {
                    required: true
                },
                'Enter_Catalog_Number__c': {
                    required: true
                },
                'Quantity__c': {
                    required: true
                },
                'Reason_for_Return__c': {
                    required: true
                },
                'Privacy_Policy_Consent__c': { /*Privacy policy*/
                    required: true,
                }
            },
            messages: {
                'Company_Name__c': 'Please provide your company name',
                'First_Name__c': 'Please provide your first name',
                'Last_Name__c': 'Please provide your last name',
                email: 'Please provide a valid email address',
                phone: 'Please provide your phone number',
                'Order_Number__c': 'Please provide your Greenlee order number',
                'Quantity__c': 'Please enter in a quantity',
                'Reason_for_Return__c': 'Please choose a reason for return'
            }
        });

    },
    searchForm: function() {
        $('.site-search form').validate({
            errorPlacement: function(error, element) {},
            highlight: function(element, errorClass, validClass) {
                $(element).parents('.input-group').addClass('has-error');
            },
            unhighlight: function(element, errorClass, validClass) {
                $(element).parents('.input-group').removeClass('has-error');
            },
            rules: {
                text: {
                    required: true
                }
            },

        });
    },
    requestForDemoForm: function() {
        $('.js-demo-Model form').validate({
            errorElement: 'span',
            errorPlacement: function(error, element) {
                if (element.is('select')) {
                    element.parents('.controls').append(error);
                } else {
                    element.parents('.form-group').append(error);
                }
            },
            highlight: function(element, errorClass, validClass) {
                $(element).parents('.form-group').addClass('has-error');

            },
            unhighlight: function(element, errorClass, validClass) {
                $(element).parents('.form-group').removeClass('has-error');
            },
            rules: {
                'First_Name__c': { /*First Name*/
                    required: true,
                },
                'Last_Name__c': { /*Last Name*/
                    required: true
                },
                'Company_Name__c': { /*Company*/
                    required: true
                },
                'Business_Type__c': { /*Business Type*/
                    required: true,
                },
                'Address_1__c': { /*Address 1*/
                    required: true
                },
                'City__c': { /*City*/
                    required: true
                },
                'State_c__c': { /*State / Province / Region*/
                    required: true
                },
                'Postal_Code__c': { /*Postal Code*/
                    required: true,
                },
                'Country__c': { /*Country*/
                    required: true
                },
                email: { /*email*/
                    required: true,
                    email: true
                },
                phone: { /*phone*/
                    required: true,

                },
                'Request_Type__c': { /*Request Type*/
                    required: true,
                },
                'Privacy_Policy_Consent__c': { /*Privacy policy*/
                    required: true,
                }
            },
            messages: {
                'Country__c': 'Please select a country',
                phone: 'Please provide your phone number',
                'Request_Type__c': 'Please select any Request Type'
            },
            showErrors: function(errorMap, errorList) {
                this.defaultShowErrors();
                $.colorbox.resize();
            },
            submitHandler: function(form) {
                form.submit();
                $.colorbox.close();
            }
        });
    },
    contactusForm: function() {
        $('.contact form').validate({

            errorPlacement: function(error, element) {
                if (element.is('select')) {
                    element.parents('.controls').append(error);
                } else {
                    element.parents('.form-group').append(error);
                }
            },
            highlight: function(element, errorClass, validClass) {
                $(element).parents('.form-group').addClass('has-error');

            },
            unhighlight: function(element, errorClass, validClass) {
                $(element).parents('.form-group').removeClass('has-error');
            },
            rules: {
                'First_Name__c': { /*First Name*/
                    required: true,
                },
                'Last_Name__c': { /*Last Name*/
                    required: true
                },
                'Company_Name__c': { /*Company*/
                    required: true
                },
                'Business_Type__c': { /*Business Type*/
                    required: true,
                },
                'Address_1__c': { /*Address 1*/
                    required: true
                },
                'City__c': { /*City*/
                    required: true
                },
                'State_c__c': { /*State / Province / Region*/
                    required: true
                },
                'Postal_Code__c': { /*Postal Code*/
                    required: true,
                },
                'Country__c': { /*Country*/
                    required: true
                },
                email: { /*email*/
                    required: true,
                    email: true
                },
                phone: { /*phone*/
                    required: true,

                },
                'Type_of_Request__c': { /*Request Type*/
                    required: true,
                },
                'Privacy_Policy_Consent__c': { /*Privacy policy*/
                    required: true,
                }

            },
            messages: {
                email: 'please enter a valid email address.'
            }

        });
    },
};
greeenleeValidate.bindAll();

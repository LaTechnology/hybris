ACC.consent = {
    _autoload: [
        ["bindSendConsent", $("#consent-management-form").length != 0]
    ],
    bindSendConsent: function ()
    {
        var consentCheckbox = $('#consent-management-form').find('input.toggle-switch__input');
        consentCheckbox.click(function ()
        {
            var consentId = $(this).prop('id');
            var isConsentGiven = $(this).is(':checked');
            var buttonId = (isConsentGiven ? '#give-consent-button-' : '#withdraw-consent-button-') + consentId;
            $(buttonId).trigger('click');
        });
    }
};

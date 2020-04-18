module.exports =
    greenLeeSelectbox = {
        customSelect: function () {
            $('.custom-select').each(function () {

                if ($(this).parents('.select-wrapper').length == 0) {
                    $(this).wrap('<span class=\'select-wrapper\'></span>');
                    $(this).after('<span class=\'holder\'></span>');
                    var selectedOption = $(this).find(':selected').text();
                    $(this).next('.holder').text(selectedOption);
                }
            });

            function changedSelectField($this) {
                var selectedOption = $this.find(':selected').text();
                $this.next('.holder').text(selectedOption);
            }
            $('.custom-select').change(function () {
                changedSelectField($(this));
            });
            $('.custom-select').on('keyup', function () {
                changedSelectField($(this));
            });
        }
};

greenLeeSelectbox.customSelect();

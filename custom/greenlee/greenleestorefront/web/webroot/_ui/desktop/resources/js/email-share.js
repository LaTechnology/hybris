module.exports =
    comparePage = {
        colorboxInvoke: function () {
            'use strict';
            $('.js-email').css({
                'pointer-events': 'auto'
            });
            $(document).on('click', '.js-email', function (e) {
                e.preventDefault();
                colorbox.open(
                    $(this).data('cboxTitle'), {
                        href: $(this).attr('href'),
                        width: '816px',
                        scrolling: false,
                        onOpen: function () {

                        },
                        onComplete: function () {
                            $('.email-popup-content img').load(function () {
                                colorbox.resize();
                            });
                        }
                    }
                );
            });
        }
};
comparePage.colorboxInvoke();

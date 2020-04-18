module.exports =
    colorbox = {
        config: {
            maxWidth: '100%',
            opacity: 0.7,
            width: 'auto',
            scrolling: 'false',
            transition: 'none',
            close: '<span class="gl gl-remove"></span>',
            title: '<div class="headline"><span class="headline-text">{title}</span></div>',
            onComplete: function () {
                $.colorbox.resize();
                //ACC.common.refreshScreenReaderBuffer();
            },
            onClosed: function () {
                //ACC.common.refreshScreenReaderBuffer();
                $('#cboxWrapper').removeClass('pop-out');
            },
            onCleanup: function () {
                $('#cboxWrapper').addClass('pop-out');
            }
        },

        open: function (title, config) {
            var config = $.extend({}, colorbox.config, config);
            if (title) {
                config.title = config.title.replace(/{title}/g, title);
            } else {
                config.title = '';
            }
            return $.colorbox(config);
        },

        resize: function () {
            $.colorbox.resize();
        },

        close: function () {
            $.colorbox.close();
            $.colorbox.remove();
        }
};

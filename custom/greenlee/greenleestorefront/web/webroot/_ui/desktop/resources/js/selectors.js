module.exports =
    selectors = {
        Selector_Cache: function () {
            var collection = {};

            function get_from_cache(selector) {
                if (undefined === collection[selector]) {
                    collection[selector] = $(selector);
                }
                return collection[selector];
            }
            return {
                get: get_from_cache
            };
        }
};
selectors.getElement = new selectors.Selector_Cache();

/*General Selector*/
selectors.html = selectors.getElement.get('html');
selectors.body = selectors.getElement.get('body');
selectors.colorbox = selectors.getElement.get('#colorbox');

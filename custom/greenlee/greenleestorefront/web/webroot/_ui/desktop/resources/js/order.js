module.exports = accountOrder = {
    bindOrder: function() {
        if ($('#order-sort').length != 0) {
            var options = {
                valueNames: ['order-id', 'order-date', 'order-total', 'order-status', 'tracking-no']
            };
            var featureList = new List('order-sort', options);
            if (typeof(featureList.sort) != 'undefined') {
                /*featureList.sort('order-id', {
                    order: 'desc'
                });
                featureList.sort('order-date', {
                    order: 'desc'
                });*/
            }
            $('#sort-order').change(function() {
                var selection = $(this).find('option:selected').attr('data-sort');
                var orderType = $(this).find('option:selected').attr('data-order');
                if (selection) {
                    featureList.sort(selection, {
                        order: orderType
                    });
                } else {
                    return false;
                }
            });
        }
    }
};

accountOrder.bindOrder();

if (typeof(ACC.product) != "undefined" && typeof(ACC.product.$addToCartButton) != "undefined")
{
    ACC.productListingFix = {
        INTERVALFIXCALL:500,

        bindDisabledButtons: function() {
            setInterval(function() {
                $(ACC.product.$addToCartButton.selector).removeAttr("disabled");
            },this.INTERVALFIXCALL);
        }
    };

    $(document).ready(function() {
        ACC.productListingFix.bindDisabledButtons();
    });
}
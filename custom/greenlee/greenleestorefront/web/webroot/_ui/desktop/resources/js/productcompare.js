module.exports = greenleeCompare = {
    addProducts: function(value) {
        var products = [];
        products = this.getProducts();
        if (products === null) {
            var products = [];
            products.push(value);

            this.saveProducts(products)
            this.showProducts(products);
        } else {
            if (this.checkProduct(value)) {
                document.getElementById('com-message').innerHTML = "<h4 style='color: red'>Selected product is already in list<h4>";
            } else {
                products.push(value);
                this.saveProducts(products);
                this.showProducts(products);
            }
        }
    },
    checkProduct: function(code) {
        var products = [];
        products = this.getProducts();
        if (products.length != 0) {
            for (i = 0; i < products.length; i++) {
                if (products[i] === code) {
                    return true;
                }
            }
        }
        return false;
    },
    saveProducts: function(products) {
        localStorage.setItem("products", JSON.stringify(products));
    },
    getProducts: function() {
        return JSON.parse(localStorage.getItem("products")) || [];
    },
    showProducts: function(productsData) {
        if (productsData.length >= 0) {
            for (i = 0; i < productsData.length; i++) {
                $("#productData" + (i + 1)).text(productsData[i]);
                $("#product" + (i + 1)).show();
            }
        }
        if (productsData.length >= 2) {
            $("#compareBtn").show();
        }
    },
    removeProduct: function(code) {
        var products = [];
        products = this.getProducts();

        var productsList = [];
        if (products.length >= 0) {
            for (i = 0; i < products.length; i++) {
                if (products[i] == code) {
                    //
                } else {
                    productsList.push(products[i]);
                }
            }
            this.saveProducts(productsList);
            this.showProducts(productsList);
        }
    }
}

$('body').off('change', '.glcheckbox input[type="checkbox"]').on('change', '.glcheckbox input[type="checkbox"]', function() {
    var that = $(this);
    var compareVal = parseInt(that.val(), 10);
    if (that.is(':checked')) {
        greenleeCompare.addProducts(compareVal);
    } else {
        greenleeCompare.removeProduct(compareVal);;
    }
});

localStorage.clear();
$("#compareBtn").hide();
$("#product1").hide();
$("#product2").hide();
$("#product3").hide();
var productData = [];
productsData = greenleeCompare.getProducts();

if (productsData === null) {
    $("#products").empty();
} else {
    greenleeCompare.showProducts(productsData);
}


$("#remove1").click(function() {
    var code = parseInt($("#productData1").text(), 10);
    greenleeCompare.removeProduct(code);
});
$("#remove2").click(function() {
    var code = parseInt($("#productData2").text(), 10);
    greenleeCompare.removeProduct(code);
});

$("#remove3").click(function() {
    var code = parseInt($("#productData3").text(), 10);;
    greenleeCompare.removeProduct(code);
});

$("#compareBtn").click(function() {
    var compareUrl = $("#compareUrl").text();
    window.location.assign(compareUrl + "=" + localStorage.getItem("products"));
});
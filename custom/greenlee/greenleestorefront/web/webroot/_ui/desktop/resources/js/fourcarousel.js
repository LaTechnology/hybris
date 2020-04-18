var owl = $('#prd-carousel');
var aowl = $('#addressbook').length != 0 ? $('#addressbook') : $('#paymentmethod');
if (owl.children().length > 0) {
    owl.owlCarousel({
        dots: true,
        responsive: {
            0: {
                items: 1,
                center: true
            },
            764: {
                items: 3
            },
            992: {
                items: 4
            }
        }
    });
} else {
    owl.show();
}

if (aowl.children().length > 0) {
    aowl.owlCarousel({
        loop: false,
        responsive: {
            0: {
                items: 1
            },
            992: {
                items: 3,
                mouseDrag: false,
                nav: true,
                navText: ['<i class="fa fa-angle-left"></i>', '<i class="fa fa-angle-right"></i>']
            }
        },
        margin: 20, 
        dots: false
    });
} else {
    aowl.show();
}

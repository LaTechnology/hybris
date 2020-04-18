var howl = $('#owl-home');
var towl = $('.testimonial-section .owl-carousel');
var feowl = $('.feature .owl-carousel');
if (howl.children().length > 1) {
    howl.owlCarousel({
        loop: true,
        items: 1
    });
} else {
    howl.show();
}
if (towl.children().length > 1) {
    towl.owlCarousel({
        loop: true,
        items: 1,
        dots: false,
        nav: true,
        navText: ['<i class="gl gl-left"></i>', '<i class="gl gl-right"></i>'],
        center: true,
    });
} else {
    towl.show();
}
if (feowl.children().length > 1) {
    feowl.owlCarousel({
        dots: true,
        items: 1,
    });
} else {
    feowl.show();
}

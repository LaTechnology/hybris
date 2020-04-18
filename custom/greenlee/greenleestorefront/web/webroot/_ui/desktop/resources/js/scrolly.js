var body = $('body'),
    timer;
$(function scrolly() {
    //Keep track of last scroll
    var lastScroll = 66;
    $(window).scroll(function (event) {
        //Sets the current scroll position
        var st = $(this).scrollTop();
        //Determines up-or-down scrolling
        if (st < 150) {
            $('.video').addClass('fixed');
        } else {
            $('.video').removeClass('fixed');
        }
        $('.cart-menu').removeClass('checkout--active');
        if (st > lastScroll && lastScroll > 66) {
            //Replace this with your function call for downward-scrolling
            //Maybe some timeout?
        	
            // setTimeout( function () {
            $('.navbar-fixed-top').addClass('fixedAtTop');
            // }, 800);
        } else {
            //Replace this with your function call for upward-scrolling

            $('.navbar-fixed-top').removeClass('fixedAtTop');

        }
        //Updates scroll position
        lastScroll = st;
    });

});



$(window).scroll(function (event) {
    clearTimeout(timer);
    if (!body.hasClass('disable-hover')) {
        body.addClass('disable-hover')
    }

    timer = setTimeout(function () {
        body.removeClass('disable-hover')
    }, 100);
});

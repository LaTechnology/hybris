$('.js-tabs li').on('click', function() {
    var $this = $(this)
    var rel = $(this).attr('data-rel');
    $('.js-tabs li').removeClass('active');
    $(this).addClass('active');
    $('.tab-content').removeClass('active');
    $('.' + rel).addClass('active');
    if ($('.downloads.active').length != 0) {
        $('.downloads .pdp-list').syncHeight();
    }
});

var allPanels = $('.recent-list .order-body').hide();
$('.recent-list li').eq(0).find('.order-body').slideDown();
$('.recent-list li').eq(0).addClass('active');
$('.recent-list li').on('click', function() {
    var that = $(this);
    if (!$(this).hasClass('active')) {
        $('.order-body').stop(true, false).slideUp({
            duration: 350,
            easing: 'easeOutQuart',
            queue: false,
            complete: function() {
                $('.recent-list li').removeClass('active');
                that.addClass('active');
            }
        });
        $(this).find('.order-body').stop(true, false).slideDown({
            duration: 350,
            easing: 'easeOutQuart',
            queue: false,
            complete: function() {
                $('.order-body').css('height', '');
            }
        });
    }
});
if ($(window).innerWidth() < 769) {
    $('.tab-container .tabbody').hide();
    $('.tab-container .tabhead').removeClass('active');
    $('.tab-container .tabbody').eq(0).slideDown();
    $('.tab-container .tabhead').eq(0).addClass('active');
} else {
    $('.tab-container .tabbody').show();
    $('.tab-container .tabhead').addClass('active');
}

enquire.register('screen and (max-width:768px)', {
    match: function() {
        $('.tab-container .tabbody').hide();
        $('.tab-container .tabhead').removeClass('active');
        $('.tab-container .tabbody').eq(0).slideDown();
        $('.tab-container .tabhead').eq(0).addClass('active');
    },
    unmatch: function() {
        $('.tab-container .tabbody').show();
        $('.tab-container .tabhead').addClass('active');
    },
});



$('.tab-container').off('click').on('click', '.tabhead', function() {
    var that = $(this);
    $('.link-nav').removeClass('active');
    $('#' + $(this).parents('.tab-area').data('link') + '').addClass('active');


    if (!$(this).hasClass('active')) {
        $(this).parents('.tab-container').find('.tabbody').stop(true, false).slideDown({
            duration: 350,
            easing: 'easeOutQuart',
            queue: false,
            complete: function() {
                $(this).css('height', '');
                that.addClass('active');
                $(this).parents('.tab-container').find('.tabbody').addClass('active');
            }
        });
    } else {
        $(this).parents('.tab-container').find('.tabbody').stop(true, false).slideUp({
            duration: 350,
            easing: 'easeOutQuart',
            queue: false,
            complete: function() {
                $(this).css('height', '');
                //$('.tabbody').removeClass('active');
                that.removeClass('active');
                $(this).parents('.tab-container').find('.tabbody').removeClass('active');
            }
        });
    }
});
var accountHeading = $('.sidebar-body li.active a').text();
$('.account-head').html(accountHeading + '<i class="fa-angle-down fa"></i>');

$('.account-head').click(function() {
    if ($(this).hasClass('active')) {
        $('.account-sidebar').stop(true, false).slideUp({
            duration: 350,
            easing: 'easeOutQuart',
            queue: false
        });
        $(this).removeClass('active');
    } else {
        $('.account-sidebar').stop(true, false).slideDown({
            duration: 350,
            easing: 'easeOutQuart',
            queue: false
        });
        $(this).addClass('active');
    }
});

$('.link-nav a').on('click', function(e) {
    e.preventDefault();
    $('.link-nav').removeClass('active');
    $(this).parent().addClass('active');

    var tabid = $(this).parents('li').data('nav');
    if ($('#' + tabid).find('.tabbody').length != 0) {
        $('.tabbody').stop(true, false).slideUp({
            duration: 350,
            easing: 'easeOutQuart',
            queue: false,
            complete: function() {
                $(this).css('height', '');
                $('.tabbody').removeClass('active');
                $('.tabhead').removeClass('active');
                $('#' + tabid).find('.tabhead').addClass('active');
            }
        });
        $('#' + tabid).find('.tabbody').stop(true, false).slideDown({
            duration: 350,
            easing: 'easeOutQuart',
            queue: false,
            complete: function() {
                $('#' + tabid).css('height', '');
                $('#' + tabid).find('.tabbody').addClass('active');
            }
        });
    }
});

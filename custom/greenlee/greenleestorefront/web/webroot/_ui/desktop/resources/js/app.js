var $ = require('jquery');
window.jQuery = $;
window.$ = $;
var List = require('./list.min.js');
window.List = List;
var enquire = require('./enquire.min.js');
window.enquire = enquire;
var _ = require('./underscore-min.js');
window._ = _;
require('./jquery.verticalSlide.js');
require('./jquery-ui.min.js');
require('./owl.carousel.min.js');
require('./jquery.colorbox-min.js');
require('./jquery.form.min.js');
require('./jquery.easing.1.3.js');
require('./jquery.validate.min.js');
require('./jquery.syncheight.custom.js');
require('./jquery.pstrength.custom-1.2.0.js');
require('./textext.core.js');
require('./textext.plugin.tags.js');
require('./jquery.dotdotdot.min.js');

$(document).ready(function() {
	
    require('./selectors.js');
    require('./forgotpassword.js');
    
    
    $('.ft-social i').text('');
    var asmCheck = $('#_asm').length;
    if (asmCheck != 0) {

        $('body').addClass('asmmodule');
        $('.navbar-burger').removeClass('navbar-fixed-top');
        var amsHeight = $('#_asm').outerHeight(true) + 67;
        $('.sub-menu').css({
            top: amsHeight
        });
    }
    var colorbox = require('./colorbox.js');
    require('./singlecarousel.js');
    require('./fourcarousel.js');
    require('./navigate.js');
    require('./scrolly.js');
    require('./compare.js');
    require('./selectbox.js');
    require('./header.js');
    require('./register.js');
    require('./cart.js');
    require('./productlist.js');
    require('./account-switch.js');
    require('./request-demo.js');
    require('./zoom.js');
    require('./minicart.js');
    require('./warranty.js');
    require('./rating-star.js');
    require('./writereview.js');
    require('./pdp-tabs.js');
    require('./pdp-imageGallery.js');

    require('./email-share.js');
    require('./review.js');
    require('./doc-search.js');
    require('./checkout.js');
    require('./category-landing.js');
    require('./footer.js');
    require('./product.js');
    require('./validate.js');
    require('./order.js');
    require('./maketherightchoice.js');
    
    ratingstars.bindRatingStars();
    ratingstars.bindRatingStarsSet();
    imagegallery.bindImageGallery();
    makechooice.makechooicehelpmeFormFn();
   
});

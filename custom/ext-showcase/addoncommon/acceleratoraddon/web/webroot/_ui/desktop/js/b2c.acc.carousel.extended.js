if (typeof(ACC.carousel) != "undefined")
{
    ACC.carousel.bindJCarousel = function () {
    
    var FALLBACK_DIMENSION = 300;
    
        jQuery('.span-4 .jcarousel-skin').jcarousel({
            vertical: true,
            itemFallbackDimension: FALLBACK_DIMENSION
        });

        jQuery('.span-10  .jcarousel-skin').jcarousel({
			itemFallbackDimension: FALLBACK_DIMENSION
        });

        jQuery('.span-18  .jcarousel-skin').jcarousel({
			itemFallbackDimension: FALLBACK_DIMENSION
        });

        jQuery('.span-20  .jcarousel-skin').jcarousel({
			itemFallbackDimension: FALLBACK_DIMENSION
        });

        jQuery('.span-24  .jcarousel-skin').jcarousel({
			itemFallbackDimension: FALLBACK_DIMENSION
        });
        
    	jQuery('div.section1 .jcarousel-skin').jcarousel({
            itemFallbackDimension: FALLBACK_DIMENSION
        });

        $(".modal").colorbox({
            onComplete: function ()
            {
                ACC.common.refreshScreenReaderBuffer();
            },
            onClosed: function ()
            {
                ACC.common.refreshScreenReaderBuffer();
            }
        });

        $('#homepage_slider').waitForImages(function ()
        {
            $(this).slideView({toolTip: true, ttOpacity: 0.6, autoPlay: true, autoPlayTime: 8000});
        });
    };
}
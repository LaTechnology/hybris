$(document).on('click', '.js-zoom-link', function (e) {
    e.preventDefault();
    colorbox.open(
        $(this).data('cboxTitle'), {
            href: $(this).attr('href'),
            width: '1071px',
            onOpen: function () {

            },
            onComplete: function () {
                var $image = $('.zoom-gallery-image');
                var $mainImage = $('.js-gallery-image');

                $image.on('initialized.owl.carousel ', function (e) {
                    setTimeout(function () {
                        var index = $mainImage.find('.owl-item.active .indexed-div').attr('data-index');
                        $image.trigger('to.owl.carousel', [index, 300, true]);
                    }, 0);

                });
                $image.on('loaded.owl.lazy', function (e) {
                    setTimeout(function () {
                        colorbox.resize();
                    }, 0);

                });
                $image.on('play.owl.video', function (e) {
                    setTimeout(function () {
                        colorbox.resize();
                    }, 0);

                });
                var $imageOwl = $image.owlCarousel({
                    items: 1,
                    dots: false,
                    nav: false,
                    lazyLoad: true,
                    video: true,
                    callbacks: true,
                    navText: ['', ''],
                    center: true
                }).on('changed.owl.carousel', function (e) {
                    $('.zoom-vertical .item').removeClass('active');
                    $('.zoom-vertical a').eq(e.item.index).addClass('active');
                    var objOwl = e;
                    var maxH = 0;

                    //  console.log(objOwl.target.find('item'));
                    $(objOwl.target).find('.item').each(function () {
                        if (parseInt($(this).find('img').css('height'), 10) > maxH) {
                            maxH = $(this).find('img').css('height');
                        }
                    });
                    $(objOwl.target).find('.owl-video-tn').css('height', maxH);
                    colorbox.resize();
                });


                $('.zoom-vertical').on('click', '.item', function (e) {
                    e.preventDefault();
                    var that = $(this);
                    $('.zoom-vertical .item').removeClass('active');
                    $(this).addClass('active');
                    var index = that.attr('data-index');
                    $image.trigger('to.owl.carousel', [index, 300, true]);
                });
                colorbox.resize();
            }
        }
    );
});

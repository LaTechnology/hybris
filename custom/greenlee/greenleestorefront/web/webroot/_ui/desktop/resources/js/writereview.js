var writeReviewFn = function () {
    $('.js-write-review').css({
        'pointer-events': 'auto'
    });
    $(document).on('click', '.js-write-review', function (e) {
        e.preventDefault();
        colorbox.open(
            $(this).data('cboxTitle'), {
                href: $(this).attr('href'),
                width: '575px',
                onOpen: function () {

                },
                onComplete: function () {
                    //ratingstars.bindRatingStars();
                    ratingstars.bindRatingStarsSet();
                    $('.btn-close').on('click', function () {
                        colorbox.close();
                    });
                    $('form#greenleeReviewForm').ajaxForm({
                        success: function (data) {

                            if ($(data).find('.has-error').length != 0) {
                                $('.review-data').html($(data).find('.review-data').html());
                                ratingstars.bindRatingStarsSet();
                                $('.review-data').find('.js-rationIconSet').removeClass('active fa-star').addClass('fa-star-o');
                                var rating = $('.js-ratingSetInput').val();
                                var $i = $('.review-data').find('.js-rationIconSet:lt(' + rating * 2 + ')')
                                $i.addClass('active fa-star').removeClass('fa-star-o');
                                colorbox.resize();
                            } else {
                                $('.write-review').html(data);
                                colorbox.resize();
                                setTimeout(function () {
                                    colorbox.close();
                                }, 2000);
                            }
                        }
                    });
                }
            }
        );
    });
};
writeReviewFn();

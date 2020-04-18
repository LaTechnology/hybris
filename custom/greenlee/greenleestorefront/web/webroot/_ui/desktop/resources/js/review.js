module.exports =
    reviewAjax = {
        totalReview: 0,
        reviewSize: 4,
        totalCall: 0,
        currentPage: 1,
        reviewPagination: function(flag) {
            var that = this;
            var ajaxData = {};
            if (flag) {
                ajaxData.data = {
                    page: that.currentPage,
                    pageSize: that.reviewSize,
                }
            } else {
                ajaxData.data = {
                    pageSize: that.currentPage * that.reviewSize,
                }
            }
            $.ajax({
                url: location.pathname + '/reviewdatahtml',
                async: true,
                data: ajaxData.data,
            }).done(function(data) {
                $('.review-wrp').html(data);
                ratingstars.bindRatingStars();
                that.bindEventReview();
            });
        },
        resizeReview: function() {
            var self = this;
            $(window).resize(_.debounce(function() {

                if ($(window).innerWidth() < 769) {
                    self.reviewPagination(false);
                } else {
                    self.reviewPagination(true);
                }
            }, 900));
        },
        bindEventReview: function() {
            var self = this;
            self.totalReview = parseInt($('input[name=totalreview]').val(), 10);
            self.totalCall = Math.ceil(this.totalReview / this.reviewSize);
            $('.review-wrp .reviews-nav a').off('click').on('click', function(e) {
                e.preventDefault();
                var _that = $(this);
                if (_that.hasClass('next')) {
                    if (self.totalCall > self.currentPage) {
                        self.currentPage++;
                        self.reviewPagination(true);
                    }
                } else if (self.currentPage > 1) {
                    self.currentPage--;
                    self.reviewPagination(true);
                }
            });
            $('.product-reviews a.btn-white').off('click').on('click', function(e) {
                e.preventDefault();
                var _that = $(this);
                if (self.totalCall > self.currentPage) {
                    self.currentPage++;
                    self.reviewPagination(false);
                }
            });
        }

    };
reviewAjax.bindEventReview();
if ($('.review-wrp').length != 0) {
    reviewAjax.resizeReview();
}

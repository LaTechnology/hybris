	module.exports = imagegallery = {
	    bindImageGallery: function () {
	        $('.js-gallery').each(function () {
	            var $image = $(this).find('.js-gallery-image');
	            var $carousel = $(this).find('.js-gallery-carousel')
	            var imageTimeout;
	            var flag = false;
	            var duration = 300;
	            $image.owlCarousel({
	                items: 1,
	                dots: false,
	                nav: false,
	                lazyLoad: true,
	                video: true,
	                loop: false,
	                center: true,
	                navText: ['', ''],

	                navigationText: ['<span class=\'glyphicon glyphicon-chevron-left\'></span>', '<span class=\'glyphicon glyphicon-chevron-right\'></span>'],
	            }).on('initialize.owl.carousel', function (event) {

	                // Or set the height directly, say to 300px
	                // $(objOwl.dom.$oItems).find(".owl-video-tn").css("height", 300);

	            }).on('changed.owl.carousel', function (e) {
	                if (e.namespace && e.property.name === 'position' && !flag) {
	                    flag = true;
	                    $carousel.trigger('to.owl.carousel', [e.item.index, duration, true]);
	                    flag = false;
	                }
	                var objOwl = e;
	                var maxH = 0;

	                //  console.log(objOwl.target.find('item'));
	                $(objOwl.target).find('.item').each(function () {
	                    if (parseInt($(this).find('img').css('height'), 10) > maxH) {
	                        maxH = $(this).find('img').css('height');
	                    }
	                });
	                $(objOwl.target).find('.owl-video-tn').css('height', maxH);
	            });
	            $carousel.owlCarousel({
	                margin: 10,
	                nav: true,
	                navText: ['', ''],
	                dots: false,
	                items: 5,
	                loop: false, 
	                autoWidth: true,
	                responsive: {
	                    0: {
	                        items: 4
	                    },
	                    992: {
	                        items: 5 
	                    }
	                },
	            onInitialize: function (event) {
                     if ($('.js-gallery-carousel .item').length < 5) {
                        this.settings.loop = false;
                     } 
                 }
	            }).on('click', '.owl-item', function (e) {
	                e.preventDefault();
	                $image.trigger('to.owl.carousel', [$(this).index(), duration, true]);

	            })
	                .on('changed.owl.carousel', function (e) {
	                    if (e.namespace && e.property.name === 'position' && !flag) {
	                        flag = true;
	                        $image.trigger('to.owl.carousel', [e.item.index, duration, true]);
	                        flag = false;
	                    }
	                });
	        });
	    }
	};
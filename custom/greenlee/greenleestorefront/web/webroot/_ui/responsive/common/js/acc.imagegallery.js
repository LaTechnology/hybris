ACC.imagegallery = {

	_autoload: [
		"bindImageGallery"
	],

	bindImageGallery: function (){

		$(".js-gallery").each(function(){
			var $image = $(this).find(".js-gallery-image");
			var $carousel = $(this).find(".js-gallery-carousel")
			var imageTimeout;
			var flag = false;
			var duration=300;
			
			$image.owlCarousel({
				items:1,
				dots:false,
				nav:false,
				lazyLoad:true,
				navText:['',''],
				navigationText : ["<span class='glyphicon glyphicon-chevron-left'></span>", "<span class='glyphicon glyphicon-chevron-right'></span>"],
				afterAction : function(){
					ACC.imagegallery.syncPosition($image,$carousel,this.currentItem)
					$image.data("zoomEnable",true)				
				},
				startDragging: function(){
					
					$image.data("zoomEnable",false)
				},
				afterLazyLoad:function(e){

					var b = $image.data("owlCarousel") || {}
					if(!b.currentItem){
						b.currentItem = 0
					}
	
					var $e=$($image.find("img.lazyOwl")[b.currentItem]);
					startZoom($e.parent())
				}
			}).on('changed.owl.carousel', function (e) {
				if (!flag) {
					flag = true;
					$carousel.trigger('to.owl.carousel', [e.item.index, duration, true]);
					flag = false;
				}
			});


			$carousel.owlCarousel({
				margin:10,
			   	nav:true,
			   	loop:true,
			   	navText:['',''],
			   	dots:false,
				responsive:{
			   		0:{
			            items:4
			        },
			        992:{
			            items:5
			        }
			   	},
				lazyLoad:true,
				afterAction : function(){

				},
			}).on('click', '.owl-item', function () {
				$image.trigger('to.owl.carousel', [$(this).index(), duration, true]);

			})
			.on('changed.owl.carousel', function (e) {
				if (!flag) {
					flag = true;		
					$image.trigger('to.owl.carousel', [e.item.index, duration, true]);
					flag = false;
				}
			});


			$carousel.on("click","a.item",function(e){
				e.preventDefault();
			console.log($(this).parent(".owl-item").data("owlItem"));
				$image.trigger("owl.goTo",$(this).parent(".owl-item").data("owlItem"));
			})



			function startZoom(e){
			


				$(e).zoom({
					url: $(e).find("img.lazyOwl").data("zoomImage"),
					touch: true,
					on: "grab",
					touchduration:300,

					onZoomIn:function(){
					
					},

					onZoomOut:function(){
						var owl = $image.data('owlCarousel');
						owl.dragging(true)
						$image.data("zoomEnable",true)
					},

					zoomEnableCallBack:function(){
						var bool = $image.data("zoomEnable")

						var owl = $image.data('owlCarousel');
						if(bool==false){
							owl.dragging(true)
						}
						else{

						 	owl.dragging(false)
						}
						return bool;
					}
				});
			}
		})
	},


	syncPosition: function($image,$carousel,currentItem){
		$carousel.trigger("owl.goTo",currentItem);
	}
};
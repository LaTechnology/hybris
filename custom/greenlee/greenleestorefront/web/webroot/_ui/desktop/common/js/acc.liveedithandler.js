ACC.liveEditHandler={
		
		handlerYoutubeVideoSectionOnHomePage:function()
		{
			if($(".video__container div.video iframe[src*='youtube.com']").length>0)
				{
					$(".video__container").each(function(){
						height=$(this).height();
						width=$(this).width();
						div=document.createElement("div");
						p=document.createElement("p");
						$(p).css({'margin-top':'5%'}).text('Youtube video section. click to bring up the editor');
						parentContainer=$($(this).find('iframe')[0]).parent();
						div.iframecontent=$($(this).find('iframe')[0]);
						$(div).css({'text-align':'center','font-size':(width*0.035)+'px'}).html(p);
						$(div).height(height).width(width);
						$($(this).find('iframe')[0]).remove();
						$(parentContainer).html(div);
					});
				}
		},
		handleOverlay:function()
		{
			ACC.liveEditHandler.handleOverlayOnHomePage();
			ACC.liveEditHandler.handleOverlayOnCategoryLandingPage();
		},
		handleOverlayOnHomePage:function()
		{
			if($("div.simple-responsive-banner-component a[href*='youtube.com/embed']").length>0)
				{
					colorbox={open:function(){}};
					$(this).attr('ahref',$(this).attr('href'));
					$(this).removeAttr('href');
				}
		},
		handleOverlayOnCategoryLandingPage:function()
		{
			if($("div.choose-section").length>0)
				{
					colorbox={open:function(){}};
					$($(this).find(".simple-banner")[0]).attr('ahref',$($(this).find(".simple-banner")[0]).attr('href'));
					$($(this).find(".simple-banner")[0]).removeAttr('href');
				}
			
			$(".responsive-video").each(function(){
				height=$(this).height();
				width=$(this).width();
				div=document.createElement("div");
				parentContainer=$($(this).find('iframe')[0]).parent();
				div.iframecontent=$($(this).find('iframe')[0]);
				$(div).css({'text-align':'center','font-size':(width*0.035)+'px'}).text('Youtube video section. click to bring up the editor');
				$(div).height(height).width(width);
				$($(this).find('iframe')[0]).remove();
				$(parentContainer).html(div);
			});
		}
};

$(document).ready(function(){
	if($('body').hasClass('yCmsLiveEdit'))
		{
			ACC.liveEditHandler.handlerYoutubeVideoSectionOnHomePage();
			ACC.liveEditHandler.handleOverlay();
		}
	
	
});

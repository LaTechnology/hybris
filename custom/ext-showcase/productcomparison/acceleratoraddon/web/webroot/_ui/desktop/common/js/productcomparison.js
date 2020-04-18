ACC.productComparison = {
	COMPARE_BTN: '.compareBtn',
	COMPARE_BTN_LABEL: '#compareBtnLabel',
	PRODUCT_COMPARE_TABLE: '.product_compare .middle table',
	CLEAR_BTN: '#pcClearBtnId',
	CMS_SITE_CHANNEL: '#cmsSiteChannel',
	ADD_TO_CART_LAYER: '#addToCartLayer',
	COMPARE_COUNT:typeof(prdCompare) != "undefined" ? prdCompare.length : $('.compare-carousel').find('.item').length,
}

function pcUpdateComparableState(code, url, cb, compareBtnLabel) {
	
	if (code != '') {
		var selectUrl = url + (cb.checked ? '/add' : '/cbremove');
		if(cb.checked && ACC.productComparison.COMPARE_COUNT != 6){
			
			if(_.indexOf(prdCompare, code) == -1){
				$.postJSON(selectUrl, {
					code: code
				}, function(data) {
					ACC.productComparison.COMPARE_COUNT = data;
					checkCompareBtn();
					pcUpdateDisableAttribuite(compareBtnLabel, data);
				});
				prdCompare.push(code);
				
			}
			prdCompare.push()
		} else if(!cb.checked && ACC.productComparison.COMPARE_COUNT <= 6){
			if(_.indexOf(prdCompare, code) != -1){
			$.postJSON(selectUrl, {
				code: code
			}, function(data) {
				ACC.productComparison.COMPARE_COUNT = data;
				pcUpdateDisableAttribuite(compareBtnLabel, data);
			});
			prdCompare= _.without(prdCompare,code);
			$('.glcheckbox input').removeAttr("disabled");
			}
		}
	}
	
}
function checkCompareBtn(){
	var totalPrd = [];
	
	if(ACC.productComparison.COMPARE_COUNT == 6){
		$('.glcheckbox input').each(function(){
			totalPrd.push($(this).data('product'));
		});
		var diffList = _.difference(totalPrd, prdCompare);
		console.log(diffList);
		_.each(diffList, function(val){
			console.log(val);
			$('#compare-'+val).attr('disabled', 'disabled');
		});
	}
	
}

function pcUpdateDisableAttribuite(compareBtnLabel, data) {
	$(ACC.productComparison.COMPARE_BTN).each(function () {
		this.textContent = compareBtnLabel + ' (' + data + ')';
		if (data < 2) {
            $(this).attr('disabled', 'disabled');
		} else {
            $(this).removeAttr("disabled");
		}
	});
	//checkCompareBtn();
}

function pcShowComparePage(url, openPopup, closePopupAfterAddToCart) {
	if (openPopup) {
		var close = closePopupAfterAddToCart ? '&close=true' : '';
		$.get(url + 'compare/list?popup=true' + close).done(function(data) {
			$.colorbox({
				html: data,
				opacity:"0",
				scrolling: "false",
				onComplete: function ()
				{
					pcUpdateDiffRows();
					$(this).colorbox.resize();
				}
			});
		});
	} else {
		location.href = url + 'comparison';
	}
}

function pcRemoveProduct(code, url, openPopup) {
	var selectUrl = url + 'compare/cbremove';
	$.postJSON(selectUrl, {
		code : code
	}, function(data) {
		if (openPopup) {
			var compareBtnLabel = $(ACC.productComparison.COMPARE_BTN_LABEL).value;
			pcUpdateDisableAttribuite(compareBtnLabel, data)
		}
	});
	pcShowComparePage(url, openPopup, false);
}

function cpAddToCart(pcPopupClose, url, productCode) {
	var qty = $("#qty_" + productCode).val(),
		timeoutId;

	$.postJSON(url, {
		productCodePost: productCode,
		qty: qty
	}, function(data) {
		$('#header').append(data.addToCartLayer);

		$(ACC.productComparison.ADD_TO_CART_LAYER).fadeIn(function(){
			$.colorbox.close();
			if (typeof timeoutId != 'undefined')
			{
				clearTimeout(timeoutId);
			}
			timeoutId = setTimeout(function ()
			{
				$(ACC.productComparison.ADD_TO_CART_LAYER).fadeOut(function(){
					$(ACC.productComparison.ADD_TO_CART_LAYER).remove();

				});
			}, 5000);
		});

		var cmsSiteChannel = $(ACC.productComparison.CMS_SITE_CHANNEL).value;
		ACC.minicart.refreshMiniCartCount();
	});
}

function pcUpdateDiffRows() {
	$(ACC.productComparison.PRODUCT_COMPARE_TABLE).each(function () {
		$(this).find('tr').each(function () {
			var diffFound = false;
			var tmp = null;
			$(this).find('td').each(function () {
				var tdText = $(this).text();
				if (tdText == null || tdText == '') {
					$(this).html('-');
				}
				if (tmp == null) {
					tmp = $(this);
				} else {
					if (tmp.text() != $(this).text()) {
						diffFound = true;
					}
				}
			});
			if (diffFound) {
				$(this).addClass( 'diff' );
				$(this).find('th').first().addClass( 'thDiff' );
			}
		});
	});
}

function pcClearList(url, openPopup) {
	$(ACC.productComparison.CLEAR_BTN).attr("disabled", "disabled");
	var clearListUrl = url + 'compare/clear';
	$.postJSON(clearListUrl, {}, null).always(function() {
		if (openPopup) {
			$.colorbox.close();
			var compareBtnLabel = $(ACC.productComparison.COMPARE_BTN_LABEL).value;
			pcUpdateDisableAttribuite(compareBtnLabel, '0')
		} else {
			location.href = url + 'search?q=:relevance&text=#';
		}
	});
}

$(document).ready(function(){

	var prdCompare = _.uniq(window.prdCompare) || [];
	
	_.each(prdCompare, function(val){
		$('#compare-'+val).attr("checked",true);
	});
	checkCompareBtn();

});

ACC.productComparison = {
	COMPARE_BTN: '.js-productcomparison-show',
	COMPARE_TOGGLE_BTN: '.js-productcomparison-toggle',
	COMPARE_REMOVE_BTN: '.js-productcomparison-remove',
	COMPARE_CLEAR_BTN: '.js-productcomparison-clear',
	COMPARE_COUNT: '.js-productcomparison-count',
	PRODUCT_COMPARE_DIFF: '.product_compare tr.middle',

	_autoload: [
		"bindActionHandlers",
		"bindListHandlers"
	],

	bindActionHandlers: function() {
		$(ACC.productComparison.COMPARE_TOGGLE_BTN).on('click', pcToggleButtonHandle);
		$(ACC.productComparison.COMPARE_BTN).on('click', pcShowCompareButtonHandle);
	},

	bindListHandlers: function() {
		$('.product_compare .add_to_cart_form').ajaxForm({success: ACC.product.displayAddToCartPopup});
		$(ACC.productComparison.COMPARE_REMOVE_BTN).on('click', pcRemoveButtonHandle);
		$(ACC.productComparison.COMPARE_CLEAR_BTN).on('click', pcClearButtonHandle);
	}
}

function pcToggleButtonHandle(e) {
	var cb = e.currentTarget,
		code = cb.getAttribute('data-product-code'),
		url = cb.getAttribute('href');
	pcUpdateComparableState(code, url, cb);
	e.preventDefault();
}

function pcShowCompareButtonHandle(e) {
	var cb = e.currentTarget,
		url = cb.getAttribute('href'),
		openPopup = cb.getAttribute('data-popup'),
		popupTitle = cb.getAttribute('data-popup-title');
	pcShowComparePage(url, openPopup, popupTitle);
	e.preventDefault();
}

function pcRemoveButtonHandle(e) {
	var cb = e.currentTarget,
		code = cb.getAttribute('data-product-code'),
		url = cb.getAttribute('href'),
		openPopup = cb.getAttribute('data-popup');
	var nth = $(cb).parents('td').index() + 1;
	$(cb).parents('.product_compare').find('td:nth-child(' + nth + ')').remove();
	pcRemoveProduct(code, url, openPopup);
	e.preventDefault();
}

function pcClearButtonHandle(e) {
	var cb = e.currentTarget,
		url = cb.getAttribute('href'),
		openPopup = cb.getAttribute('data-popup');
	pcClearList(url, openPopup);
	e.preventDefault();
}

function pcUpdateComparableState(code, url, cb) {
	if (code != '') {
		var isSelected = cb.getAttribute('aria-selected') === "true";
		var selectUrl = url + (isSelected ? '/cbremove' : '/add');
		$.postJSON(selectUrl, {
			code: code
		}, function(data) {
			pcUpdateDisableAttribuite(data);
			pcUpdateToggleButton(cb);
		});
	}
}

function pcUpdateToggleButton(cb) {
	if(cb) {
		var isSelected = cb.getAttribute('aria-selected') === "true";
		var label = isSelected ? "add" : "remove";
		cb.innerHTML = cb.getAttribute('data-label-' + label);
		cb.setAttribute('aria-selected', !isSelected);
	}
}

function pcUpdateDisableAttribuite(data) {
	$(ACC.productComparison.COMPARE_COUNT).text(data);
	$(ACC.productComparison.COMPARE_BTN).each(function () {
		if (data < 2) {
			$(this).attr('disabled', 'disabled');
		} else {
			$(this).removeAttr("disabled");
		}
	})
}

function pcShowComparePage(url, openPopup, popupTitle) {
	if (openPopup) {
		$.get(url + 'compare/list?popup=true').done(function(data) {
			$.colorbox({
				html: data,
				maxHeight:"95%",
				maxWidth:"95%",
				title: popupTitle,
				fixed: true,
				onComplete: function ()
				{
					pcUpdateDiffRows();
					ACC.productComparison.bindListHandlers();
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
			pcUpdateDisableAttribuite(data)
			var cb = $(ACC.productComparison.COMPARE_TOGGLE_BTN + '[data-product-code="' + code + '"]');
			if(cb.length > 0) {
				pcUpdateToggleButton(cb.get(0));
			}
		}
	});
}

function pcUpdateDiffRows() {
	$(ACC.productComparison.PRODUCT_COMPARE_DIFF).each(function () {
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
}

function pcClearList(url, openPopup) {
	var clearListUrl = url + 'compare/clear';
	$.postJSON(clearListUrl, {}, null).always(function() {
		if (openPopup) {
			$.colorbox.close();
			pcUpdateDisableAttribuite('0');
			$(ACC.productComparison.COMPARE_TOGGLE_BTN + '[aria-selected="true"]').each(function(index, elem){
				elem.innerText = elem.getAttribute('data-label-add');
				elem.setAttribute('aria-selected', 'false');
			});
		} else {
			location.href = url + 'comparison';
		}
	});
}

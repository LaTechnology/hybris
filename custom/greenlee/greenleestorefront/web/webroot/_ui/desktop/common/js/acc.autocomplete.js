ACC.autocomplete = {

    _autoload: [
        "bindSearchAutocomplete"
    ],

    bindSearchAutocomplete: function() {
        // extend the default autocomplete widget, to solve issue on multiple instances of the searchbox component
        $.widget("custom.yautocomplete", $.ui.autocomplete, {
            _create: function() {

                // get instance specific options form the html data attr
                var option = this.element.data("options");
                // set the options to the widget
                this._setOptions({
                    minLength: option.minCharactersBeforeRequest,
                    displayProductImages: option.displayProductImages,
                    delay: option.waitTimeBeforeRequest,
                    autocompleteUrl: option.autocompleteUrl,
                    source: this.source
                });

                // call the _super()
                $.ui.autocomplete.prototype._create.call(this);

            },
            options: {
                cache: {}, // init cache per instance
                focus: function(event, ui) {
                    //return false;
                }, // prevent textfield value replacement on item focus
                open: function(event, ui) {
                    $('.ui-autocomplete').off('hover mouseover mouseenter');
                },
                close: function(event, ui) {

                },
                select: function(e, ui) {
                    //console.log(ui);
                    window.location.href = ui.item.url;
                }
            },
            _renderItem: function(ul, item) {
                if (item.type == "autoSuggestion") {
                    var renderHtml = "<a class='ui-menu-item' href='" + item.url + "' ><div class='name'>" + item.value + "</div></a>";
                    return $("<li>")
                        .data("ui-autocomplete-item", item)
                        .append(renderHtml)
                        .addClass("autosuggestion")
                        .appendTo(ul);
                } else if (item.type == "productResult") {
                    var renderHtml = "<a class='ui-menu-item' href='" + item.url + "' >";
                    if (item.image != null) {
                        renderHtml += "<div class='thumb'><img src='" + item.image + "'  /></div>";
                    }
                    renderHtml += "<div class='name'>" + item.value + "</div>";
                    renderHtml += "</a>";
                    return $("<li>").data("ui-autocomplete-item", item).addClass("auto-product-list").append(renderHtml).appendTo(ul);
                } else if (item.type == "contentResult") {
                    var renderHtml = "<a class='ui-menu-item' href='" + item.url + "' ><div class='name'>" + item.value + "</div></a>";
                    return $("<li>").data("ui-autocomplete-item", item).addClass("contentresult").append(renderHtml).appendTo(ul);
                }
            },
            _renderMenu: function(ul, items) {
                var self = this,
                    autoFlag = 0,
                    productFlag = 0,
                    contentFlag = 0;
                $.each(items, function(index, item) {
                    if (item.type == "autoSuggestion") {
                        if (!autoFlag) {
                            // ul.append('<li><ol class="auto-suggest-list suggest-list"></ol></li>');
                            autoFlag = 1;
                        }
                        //self._renderItem($('.auto-suggest-list'), item);
                    } else if (item.type == "productResult") {
                        if (!productFlag) {
                            ul.find('li:last-child').addClass('last');
                            ul.append('<li></li>');
                            productFlag = 1
                        }
                        // self._renderItem($('.auto-product-list'), item);
                    } else if (item.type == "contentResult") {
                        if (!contentFlag) {
                            ul.find('li:last-child').addClass('last');
                            ul.append('<li></li>');
                            contentFlag = 1
                        }
                        //  self._renderItem($('.auto-content-list'), item);
                    }
                    self._renderItem(ul, item);
                });
            },
            source: function(request, response) {
                var self = this;
                var term = request.term.toLowerCase();
                if (term in self.options.cache) {
                    return response(self.options.cache[term]);
                }

                $.getJSON(self.options.autocompleteUrl, {
                    term: request.term
                }, function(data) {
                    var suggestData = [],
                        contentData = [],
                        produtData = [];
                    if (data.suggestions != null) {
                        $.each(data.suggestions, function(i, obj) {
                            suggestData.push({
                                value: obj.term,
                                url: ACC.config.encodedContextPath + "/search?text=" + obj.term,
                                type: "autoSuggestion"
                            });
                        });
                    }
                    if (data.content != null) {
                        $.each(data.content, function(i, obj) {
                            contentData.push({
                                value: obj.title,
                                url: obj.url,
                                type: "contentResult"
                            });
                        });
                    }
                    if (data.products != null) {
                        $.each(data.products, function(i, obj) {
                            produtData.push({
                                value: obj.name,
                                url: ACC.config.encodedContextPath + obj.url,
                                type: "productResult",
                                image: (obj.images != null && self.options.displayProductImages) ? obj.images[0].url : null // prevent errors if obj.images = null
                            });
                        });
                    }
                    var autoSearchData = _.union(suggestData, produtData, contentData);
                    self.options.cache[term] = autoSearchData;

                    return response(autoSearchData);

                });
            }

        });


        $search = $(".js-site-search-input");
        if ($search.length > 0) {
            $search.yautocomplete();
            $search.on('keyup keydown change focus', function() {
                if ($search.val().replace(/^\s+|\s+$/g, '').length > 0) {
                    $('.site-search input#js-site-search-input').addClass('opened');
                    if ($('.site-search .input-group-btn .gl-remove').length == 0) {
                        $('.site-search .input-group-btn').prepend('<i class="gl gl-remove"></i>');
                        $('.site-search .gl-remove').on('click', function() {
                            $search.val('');
                            $search.yautocomplete("close").val('');
                            $('.site-search .gl-remove').remove();
                        });
                    }
                } else {
                    $('.site-search input#js-site-search-input').removeClass('opened');
                    if ($('.site-search .input-group-btn .gl-remove').length != 0) {
                        $('.site-search .gl-remove').remove();
                    }
                }
            });



            $(window).scroll(function(event) {
                enquire.register("screen and (min-width:992px)", function() {
                    $search.yautocomplete("close");
                });
            });
        }
    }
};

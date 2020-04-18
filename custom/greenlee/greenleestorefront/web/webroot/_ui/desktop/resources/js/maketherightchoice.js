module.exports = makechooice = {
    bindAll: function() {
        this.makechooicehelpmeFormFn();
    },
    makechooicehelpmeFormFn: function() {
        var that = this;
        $('form#makethechoice').off('submit').on('submit', function(e) {
        	e.preventDefault();
            var checked = $('input[name="answerCode"]:checked');
            if (checked.length != 0) {
                if (checked.parents('.radio').attr('data-target')) {
                    window.location.href = checked.parents('.radio').attr('data-target');
                } else {
                    $.ajax({
                    	 url: $('form#makethechoice').attr('action'),
                         type: 'POST',
                         data: $('form#makethechoice').serialize(),
                         success: function(data, textStatus, jqXHR) {
                         	var prevData = $('.helpme-popup').detach();
     						$('.choose-section-new').html(data);
     						$('#prevQues').on('click', function(e) {
     						$('.choose-section-new').html(prevData);
     						$(window).scrollTop($('.helpme-popup').offset().top);
                                 that.makechooicehelpmeFormFn();
                             });
                            $(window).scrollTop($('.helpme-popup').offset().top);
                            // that.makechooicehelpmeFormFn();
                         }
                    });
                }
            }
        });
    }
};
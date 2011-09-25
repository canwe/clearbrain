jQuery(function($) {
	$('#m-notes-container, #m-profile-container').hover(function() {
		$(this).find('.dropdown').removeClass("hide-forced");
	}, function() {
		$(this).find('.dropdown').addClass("hide-forced");
	});
	$('#m-notes-container, #m-profile-container').find('.dropdown').live('hover', function(event) {
		event.stopPropagation();
	});
});

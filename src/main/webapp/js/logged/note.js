//Check if note's name is not empty.
$('#name').live('change', function() {
	var nameCheck = $('#name-check');
	reinitErrors('#name-error', nameCheck);

	if ($(this).isBlank())
		displayRightMsg(nameCheck, i18n['err.name'], false);
});
